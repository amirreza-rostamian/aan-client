package ir.amin.HaftTeen.vasni.model.api.splash;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseModelSplashApiData {
    @SerializedName("appName")
    @Expose
    private String appName;
    @SerializedName("changeLog")
    @Expose
    private String changeLog;
    @SerializedName("apkUrl")
    @Expose
    private String apkUrl;
    @SerializedName("updateTips")
    @Expose
    private String updateTips;
    @SerializedName("forceUpgrade")
    @Expose
    private Boolean forceUpgrade;
    @SerializedName("launcher")
    @Expose
    private Boolean launcher;
    @SerializedName("versionCode")
    @Expose
    private String versionCode;
    @SerializedName("versionName")
    @Expose
    private String versionName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getChangeLog() {
        return changeLog;
    }

    public void setChangeLog(String changeLog) {
        this.changeLog = changeLog;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getUpdateTips() {
        return updateTips;
    }

    public void setUpdateTips(String updateTips) {
        this.updateTips = updateTips;
    }

    public Boolean getForceUpgrade() {
        return forceUpgrade;
    }

    public void setForceUpgrade(Boolean forceUpgrade) {
        this.forceUpgrade = forceUpgrade;
    }

    public Boolean getLauncher() {
        return launcher;
    }

    public void setLauncher(Boolean launcher) {
        this.launcher = launcher;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
