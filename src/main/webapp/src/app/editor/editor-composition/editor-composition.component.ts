import { ToastGeneralErrorService } from './../../services/toast-general-error.service';
import { WarningDialogService } from './../../services/warning-dialog.service';
import { CompositionFile } from './../../models/composition-file';
import { EditorCompositionFileComponent } from './editor-composition-file/editor-composition-file.component';
import { Composition } from './../../models/composition';
import { CompositionService } from './../../services/composition.service';
import { Component, OnInit } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { PendingChangesDialogService } from '../../services/pending-changes-dialog.service';
import { Observable } from 'rxjs';
import { map, catchError, finalize } from "rxjs/operators";
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { CompositionVideoFile } from '../../models/composition-video-file';
import { EditorCompositionLeadSheetComponent } from './editor-composition-lead-sheet/editor-composition-lead-sheet.component';
import { LeadSheet } from '../../models/lead-sheet';
import { Settings } from '../../models/settings';
import { SettingsService } from '../../services/settings.service';

@Component({
  selector: 'app-editor-composition',
  templateUrl: './editor-composition.component.html',
  styleUrls: ['./editor-composition.component.scss']
})
export class EditorCompositionComponent implements OnInit {

  searchName: string = '';

  settings: Settings;

  loadingComposition: boolean = false;
  savingComposition: boolean = false;

  private compositions: Composition[];
  filteredCompositions: Composition[];
  currentComposition: Composition;

  loadingCompositions: boolean = false;

  // The composition, as it was when we loaded it
  initialComposition: Composition;

  constructor(
    private compositionService: CompositionService,
    private modalService: BsModalService,
    private warningDialogService: WarningDialogService,
    private pendingChangesDialogService: PendingChangesDialogService,
    private toastrService: ToastrService,
    private translateService: TranslateService,
    private toastGeneralErrorService: ToastGeneralErrorService,
    private settingsService: SettingsService) {
  }

  private loadSettings() {
    this.settingsService.getSettings().pipe(map(result => {
      this.settings = result;
    })).subscribe();
  }

  ngOnInit() {
    this.loadCompositions();

    this.loadSettings();

    this.settingsService.settingsChanged.subscribe(() => {
      this.loadSettings();
    });
  }

  private loadCompositions() {
    this.loadingCompositions = true;

    this.compositionService.getCompositions(true).subscribe((compositions: Composition[]) => {
      this.compositions = compositions;
      this.filterCompositions();

      this.loadingCompositions = false;
    });
  }

  // Prevent the last item in the file-list to be draggable.
  // Taken from http://jsbin.com/tuyafe/1/edit?html,js,output
  sortMove(evt) {
    return evt.related.className.indexOf('no-sortjs') === -1;
  }

  // Filter the composition list
  filterCompositions(searchValue?: string) {
    if (!searchValue) {
      this.filteredCompositions = this.compositions;
      return;
    }

    this.filteredCompositions = [];

    for (let composition of this.compositions) {
      if (composition.name.toLowerCase().indexOf(searchValue.toLowerCase()) !== -1) {
        this.filteredCompositions.push(composition);
      }
    }
  }

  private copyInitialComposition() {
    let compositionString = this.currentComposition.stringify();

    this.currentComposition = new Composition(JSON.parse(compositionString));
    this.initialComposition = new Composition(JSON.parse(compositionString));
  }

  checkPendingChanges(): Observable<boolean> {
    return this.pendingChangesDialogService.check(this.initialComposition, this.currentComposition, 'editor.warning-composition-changes');
  }

  // Select a composition
  selectComposition(composition: Composition) {
    if (this.currentComposition && this.currentComposition.name == composition.name) {
      return;
    }

    this.checkPendingChanges().pipe(map(result => {
      if (result) {
        // Load the details of the selected composition
        this.loadingComposition = true;

        this.compositionService.getComposition(composition.name).subscribe((composition: Composition) => {
          this.currentComposition = composition;

          this.copyInitialComposition();
          this.loadingComposition = false;
        });
      }
    })).subscribe();
  }

  // Unselect a composition
  unselect() {
    this.currentComposition = undefined;
    this.initialComposition = undefined;
  }

  // Create a new composition
  createComposition() {
    this.currentComposition = new Composition();
    this.copyInitialComposition();
  }

  private saveApi(composition: Composition) {
    this.savingComposition = true;

    this.compositionService.saveComposition(composition).pipe(map(() => {
      this.loadCompositions();
      this.copyInitialComposition();

      this.compositionService.compositionsChanged.next();

      this.translateService.get(['editor.toast-composition-save-success', 'editor.toast-save-success-title']).subscribe(result => {
        this.toastrService.success(result['editor.toast-composition-save-success'], result['editor.toast-save-success-title']);
      });
    }),
      catchError((err) => {
        return this.toastGeneralErrorService.show(err);
      })
      ,finalize(() => {
        this.savingComposition = false;
      }))
      .subscribe();
  }

  // Save a new composition
  save(composition: Composition) {
    composition.name = composition.name.replace(/\//g, '').replace(/\\/g, '');

    if (composition.name.length < 1) {
      return;
    }

    // Delete the old composition, if the name changed
    if (this.initialComposition && this.initialComposition.name && this.initialComposition.name != composition.name && this.initialComposition.name.length > 0) {
      this.compositionService.deleteComposition(this.initialComposition.name).pipe(map(() => {
        this.saveApi(composition);
      })
        ,catchError((err) => {
          return this.toastGeneralErrorService.show(err);
        }))
        .subscribe();
    } else {
      this.saveApi(composition);
    }
  }

  // Delete the composition
  delete(composition: Composition) {
    this.warningDialogService.show('editor.warning-delete-composition').pipe(map(result => {
      if (result) {
        this.compositionService.deleteComposition(this.initialComposition.name).pipe(map(() => {
          this.unselect();
          this.loadCompositions();

          this.compositionService.compositionsChanged.next();

          this.translateService.get(['editor.toast-composition-delete-success', 'editor.toast-delete-success-title']).subscribe(result => {
            this.toastrService.success(result['editor.toast-composition-delete-success'], result['editor.toast-delete-success-title']);
          });
        })
          ,catchError((err) => {
            return this.toastGeneralErrorService.show(err);
          }))
          .subscribe();
      }
    })).subscribe();
  }

  // Add a new file to the composition
  addCompositionFile() {
    this.editCompositionFileDetails(0, true);
  }

  // Add a new lead sheet to the composition
  addLeadSheet() {
    this.editLeadSheet(0, true);
  }

  // Toggle the active state (mute)
  toggleActive(file: CompositionFile) {
    file.active = !file.active;
  }

  deleteFile(fileIndex: number) {
    this.currentComposition.fileList.splice(fileIndex, 1);
  }

  // Edit a composition file's details
  editCompositionFileDetails(fileIndex: number, addNew: boolean = false) {
    // Create a backup of the current composition
    let compositionCopy: Composition = new Composition(JSON.parse(this.currentComposition.stringify()));

    if (addNew) {
      // Add a new file, if necessary
      let newFile: CompositionFile = new CompositionFile();
      compositionCopy.fileList.push(newFile);
      fileIndex = compositionCopy.fileList.length - 1;
    }

    // Show the file details dialog
    let fileDialog = this.modalService.show(EditorCompositionFileComponent, { keyboard: true, ignoreBackdropClick: true, class: 'modal-lg', initialState: { fileIndex: fileIndex, file: compositionCopy.fileList[fileIndex], composition: compositionCopy } });

    (<EditorCompositionFileComponent>fileDialog.content).onClose.subscribe(result => {
      if (result === 1) {
        // OK has been pressed -> save
        this.currentComposition.fileList[fileIndex] = (<EditorCompositionFileComponent>fileDialog.content).file;
      }
    });
  }

  // Edit a lead sheet's details
  editLeadSheet(leadSheetIndex: number, addNew: boolean = false) {
    // Create a backup of the current composition
    let compositionCopy: Composition = new Composition(JSON.parse(this.currentComposition.stringify()));

    if (addNew) {
      // Add a new lead sheez, if necessary
      let newLeadSheet: LeadSheet = new LeadSheet();
      compositionCopy.leadSheetList.push(newLeadSheet);
      leadSheetIndex = compositionCopy.leadSheetList.length - 1;
    }

    // Show the lead sheet details dialog
    let leadSheetDialog = this.modalService.show(EditorCompositionLeadSheetComponent, { keyboard: true, ignoreBackdropClick: true, class: 'modal-lg', initialState: { leadSheetIndex: leadSheetIndex, leadSheet: compositionCopy.leadSheetList[leadSheetIndex], composition: compositionCopy }});

    (<EditorCompositionLeadSheetComponent>leadSheetDialog.content).onClose.subscribe(result => {
      if (result === 1) {
        // OK has been pressed -> save
        this.currentComposition.leadSheetList[leadSheetIndex] = (<EditorCompositionLeadSheetComponent>leadSheetDialog.content).leadSheet;
      }
    });
  }

  multipleVideoImage(): boolean {
    let videoImageCount: number = 0;

    if (!this.currentComposition) {
      return false;
    }

    if (!this.currentComposition.fileList) {
      return false;
    }

    for (let file of this.currentComposition.fileList) {
      // TODO also check image files
      if (file instanceof CompositionVideoFile) {
        videoImageCount++;

        if (videoImageCount > 1) {
          return true;
        }
      }
    }

    return false;
  }

  instrumentNameFromUuid(uuid: string): string {
    if(!this.settings) {
      return undefined;
    }

    for(let instrument of this.settings.instrumentList) {
      if(instrument.uuid == uuid) {
        return instrument.name
      }
    }

    return undefined;
  }

}
