package com.ascargon.rocketshow;

import com.ascargon.rocketshow.audio.AudioBus;
import com.ascargon.rocketshow.audio.AudioDevice;
import com.ascargon.rocketshow.midi.MidiControl;
import com.ascargon.rocketshow.midi.MidiDevice;
import com.ascargon.rocketshow.midi.MidiMapping;
import com.ascargon.rocketshow.midi.MidiRouting;
import com.ascargon.rocketshow.raspberry.RaspberryGpioControl;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Settings {

    // Create an own logging enum to save it in the settings xml
    public enum LoggingLevel {
        ERROR, WARN, INFO, DEBUG, TRACE
    }

    // Possible audio outputs
    public enum AudioOutput {
        DEFAULT, HEADPHONES, HDMI, DEVICE
    }

    private String basePath;

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    private String mediaPath;
    private String midiPath;
    private String audioPath;
    private String videoPath;
    private String leadSheetPath;

    private MidiDevice midiInDevice;
    private MidiDevice midiOutDevice;

    private List<RemoteDevice> remoteDeviceList = new ArrayList<>();

    private List<MidiControl> midiControlList = new ArrayList<>();
    private MidiMapping midiMapping;

    private List<RaspberryGpioControl> raspberryGpioControlList = new ArrayList<>();

    private int lightingSendDelayMillis;

    // Global play offset on file types
    private int offsetMillisMidi;
    private int offsetMillisAudio;
    private int offsetMillisVideo;

    private List<MidiRouting> deviceInMidiRoutingList = new ArrayList<>();
    private List<MidiRouting> remoteMidiRoutingList = new ArrayList<>();

    private String defaultComposition;

    private LoggingLevel loggingLevel;

    private String language = "en";

    private String deviceName;

    private boolean resetUsbAfterBoot = false;

    private AudioOutput audioOutput;
    private AudioDevice audioDevice;
    private int audioRate;
    private Integer alsaPeriodSize;
    private Integer alsaBufferSize;
    private Integer alsaPeriodTime;
    private List<AudioBus> audioBusList = new ArrayList<>();

    private boolean wlanApEnable;
    private String wlanApSsid = "Rocket Show";
    private String wlanApPassphrase = "";
    private boolean wlanApSsidHide = false;

    private boolean enableRaspberryGpio;
    private int raspberryGpioDebounceMillis = 500;
    private boolean raspberryGpioNoHardwareTrigger = false;
    private int raspberryGpioTimerPeriodMillis = 2;
    private int raspberryGpioCyclesHigh = 3;

    public MidiDevice getMidiInDevice() {
        return midiInDevice;
    }

    public void setMidiInDevice(MidiDevice midiInDevice) {
        this.midiInDevice = midiInDevice;
    }

    public MidiDevice getMidiOutDevice() {
        return midiOutDevice;
    }

    public void setMidiOutDevice(MidiDevice midiOutDevice) {
        this.midiOutDevice = midiOutDevice;
    }

    public int getLightingSendDelayMillis() {
        return lightingSendDelayMillis;
    }

    public void setLightingSendDelayMillis(int lightingSendDelayMillis) {
        this.lightingSendDelayMillis = lightingSendDelayMillis;
    }

    @XmlElement(name = "remoteDevice")
    @XmlElementWrapper(name = "remoteDeviceList")
    public List<RemoteDevice> getRemoteDeviceList() {
        return remoteDeviceList;
    }

    public void setRemoteDeviceList(List<RemoteDevice> remoteDeviceList) {
        this.remoteDeviceList = remoteDeviceList;
    }

    public MidiMapping getMidiMapping() {
        return midiMapping;
    }

    public void setMidiMapping(MidiMapping midiMapping) {
        this.midiMapping = midiMapping;
    }

    @XmlElement(name = "deviceInMidiRouting")
    @XmlElementWrapper(name = "deviceInMidiRoutingList")
    public List<MidiRouting> getDeviceInMidiRoutingList() {
        return deviceInMidiRoutingList;
    }

    public void setDeviceInMidiRoutingList(List<MidiRouting> deviceInMidiRoutingList) {
        this.deviceInMidiRoutingList = deviceInMidiRoutingList;
    }

    @XmlElement(name = "remoteMidiRouting")
    @XmlElementWrapper(name = "remoteMidiRoutingList")
    public List<MidiRouting> getRemoteMidiRoutingList() {
        return remoteMidiRoutingList;
    }

    public void setRemoteMidiRoutingList(List<MidiRouting> remoteMidiRoutingList) {
        this.remoteMidiRoutingList = remoteMidiRoutingList;
    }

    @XmlElement(name = "midiControl")
    @XmlElementWrapper(name = "midiControlList")
    public List<MidiControl> getMidiControlList() {
        return midiControlList;
    }

    public void setMidiControlList(List<MidiControl> midiControlList) {
        this.midiControlList = midiControlList;
    }

    @XmlElement(name = "raspberryGpioControl")
    @XmlElementWrapper(name = "raspberryGpioControlList")
    public List<RaspberryGpioControl> getRaspberryGpioControlList() {
        return raspberryGpioControlList;
    }

    public void setRaspberryGpioControlList(List<RaspberryGpioControl> raspberryGpioControlList) {
        this.raspberryGpioControlList = raspberryGpioControlList;
    }

    private List<Instrument> instrumentList = new ArrayList<>();

    public String getDefaultComposition() {
        return defaultComposition;
    }

    public void setDefaultComposition(String defaultComposition) {
        this.defaultComposition = defaultComposition;
    }

    public int getOffsetMillisMidi() {
        return offsetMillisMidi;
    }

    public void setOffsetMillisMidi(int offsetMillisMidi) {
        this.offsetMillisMidi = offsetMillisMidi;
    }

    public int getOffsetMillisAudio() {
        return offsetMillisAudio;
    }

    public void setOffsetMillisAudio(int offsetMillisAudio) {
        this.offsetMillisAudio = offsetMillisAudio;
    }

    public int getOffsetMillisVideo() {
        return offsetMillisVideo;
    }

    public void setOffsetMillisVideo(int offsetMillisVideo) {
        this.offsetMillisVideo = offsetMillisVideo;
    }

    public LoggingLevel getLoggingLevel() {
        return loggingLevel;
    }

    public void setLoggingLevel(LoggingLevel loggingLevel) {
        this.loggingLevel = loggingLevel;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean isResetUsbAfterBoot() {
        return resetUsbAfterBoot;
    }

    public void setResetUsbAfterBoot(boolean resetUsbAfterBoot) {
        this.resetUsbAfterBoot = resetUsbAfterBoot;
    }

    public AudioOutput getAudioOutput() {
        return audioOutput;
    }

    public void setAudioOutput(AudioOutput audioOutput) {
        this.audioOutput = audioOutput;
    }

    @XmlElement(name = "audioBus")
    @XmlElementWrapper(name = "audioBusList")
    public List<AudioBus> getAudioBusList() {
        return audioBusList;
    }

    public void setAudioBusList(List<AudioBus> audioBusList) {
        this.audioBusList = audioBusList;
    }

    public int getAudioRate() {
        return audioRate;
    }

    public void setAudioRate(int audioRate) {
        this.audioRate = audioRate;
    }

    public AudioDevice getAudioDevice() {
        return audioDevice;
    }

    public void setAudioDevice(AudioDevice audioDevice) {
        this.audioDevice = audioDevice;
    }

    public boolean isWlanApEnable() {
        return wlanApEnable;
    }

    public void setWlanApEnable(boolean wlanApEnable) {
        this.wlanApEnable = wlanApEnable;
    }

    public String getWlanApSsid() {
        return wlanApSsid;
    }

    public void setWlanApSsid(String wlanApSsid) {
        this.wlanApSsid = wlanApSsid;
    }

    public String getWlanApPassphrase() {
        return wlanApPassphrase;
    }

    public void setWlanApPassphrase(String wlanApPassphrase) {
        this.wlanApPassphrase = wlanApPassphrase;
    }

    public boolean isWlanApSsidHide() {
        return wlanApSsidHide;
    }

    public void setWlanApSsidHide(boolean wlanApSsidHide) {
        this.wlanApSsidHide = wlanApSsidHide;
    }

    public boolean isEnableRaspberryGpio() {
        return enableRaspberryGpio;
    }

    public void setEnableRaspberryGpio(boolean enableRaspberryGpio) {
        this.enableRaspberryGpio = enableRaspberryGpio;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getMidiPath() {
        return midiPath;
    }

    public void setMidiPath(String midiPath) {
        this.midiPath = midiPath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getLeadSheetPath() {
        return leadSheetPath;
    }

    @SuppressWarnings("WeakerAccess")
    public void setLeadSheetPath(String leadSheetPath) {
        this.leadSheetPath = leadSheetPath;
    }

    public int getRaspberryGpioDebounceMillis() {
        return raspberryGpioDebounceMillis;
    }

    @SuppressWarnings("unused")
    public void setRaspberryGpioDebounceMillis(int raspberryGpioDebounceMillis) {
        this.raspberryGpioDebounceMillis = raspberryGpioDebounceMillis;
    }

    public boolean isRaspberryGpioNoHardwareTrigger() {
        return raspberryGpioNoHardwareTrigger;
    }

    @SuppressWarnings("unused")
    public void setRaspberryGpioNoHardwareTrigger(boolean raspberryGpioNoHardwareTrigger) {
        this.raspberryGpioNoHardwareTrigger = raspberryGpioNoHardwareTrigger;
    }

    public int getRaspberryGpioTimerPeriodMillis() {
        return raspberryGpioTimerPeriodMillis;
    }

    @SuppressWarnings("unused")
    public void setRaspberryGpioTimerPeriodMillis(int raspberryGpioTimerPeriodMillis) {
        this.raspberryGpioTimerPeriodMillis = raspberryGpioTimerPeriodMillis;
    }

    public int getRaspberryGpioCyclesHigh() {
        return raspberryGpioCyclesHigh;
    }

    @SuppressWarnings("unused")
    public void setRaspberryGpioCyclesHigh(int raspberryGpioCyclesHigh) {
        this.raspberryGpioCyclesHigh = raspberryGpioCyclesHigh;
    }

    public List<Instrument> getInstrumentList() {
        return instrumentList;
    }

    public void setInstrumentList(List<Instrument> instrumentList) {
        this.instrumentList = instrumentList;
    }

    @SuppressWarnings("WeakerAccess")
    public Integer getAlsaBufferSize() {
        return alsaBufferSize;
    }

    @SuppressWarnings("unused")
    public void setAlsaBufferSize(Integer alsaBufferSize) {
        this.alsaBufferSize = alsaBufferSize;
    }

    @SuppressWarnings("WeakerAccess")
    public Integer getAlsaPeriodSize() {
        return alsaPeriodSize;
    }

    @SuppressWarnings("unused")
    public void setAlsaPeriodSize(Integer alsaPeriodSize) {
        this.alsaPeriodSize = alsaPeriodSize;
    }

    @SuppressWarnings("WeakerAccess")
    public Integer getAlsaPeriodTime() {
        return alsaPeriodTime;
    }

    @SuppressWarnings("unused")
    public void setAlsaPeriodTime(Integer alsaPeriodTime) {
        this.alsaPeriodTime = alsaPeriodTime;
    }

}
