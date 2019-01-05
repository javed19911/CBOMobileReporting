package com.cbo.cbomobilereporting.ui_new.utilities_activities.VisualAdsDownload;

/**
 * Created by cboios on 31/12/18.
 */

public class mVisualAds {
    private String ItemName;
    private String FileName;
    private String directory;
    private boolean folderYN=false;
    private boolean downloadStarted=false;
    private boolean downloadCompleted=false;
    private boolean downloadFailed=false;
    private float progess =0f;
    private int trys =0;


    public mVisualAds(String itemName, String fileName, boolean folderYN) {
        ItemName = itemName;
        FileName = fileName;
        this.folderYN = folderYN;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public boolean isFolderYN() {
        return folderYN;
    }

    public void setFolderYN(boolean folderYN) {
        this.folderYN = folderYN;
    }

    public float getProgess() {
        return progess;
    }

    public void setProgess(float progess) {
        this.progess = progess;
    }

    public boolean isDownloadStarted() {
        return downloadStarted;
    }

    public void setDownloadStarted(boolean downloadStarted) {
        this.downloadStarted = downloadStarted;
    }

    public boolean isDownloadCompleted() {
        return downloadCompleted;
    }

    public void setDownloadCompleted(boolean downloadCompleted) {
        this.downloadCompleted = downloadCompleted;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public boolean isDownloadFailed() {
        return downloadFailed;
    }

    public void setDownloadFailed(boolean downloadFailed) {
        this.downloadFailed = downloadFailed;
    }

    public int getTrys() {
        return trys;
    }

    public void setTrys(int trys) {
        this.trys = trys;
    }
}
