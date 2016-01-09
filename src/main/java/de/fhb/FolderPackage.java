package de.fhb;

import java.io.File;

public class FolderPackage {
    private File folder;
    private String folderPath;
    private String packageName;

    public FolderPackage(String folderPath, String packageName) {
        this.folderPath = folderPath;
        this.packageName = packageName;
        folder = new File(folderPath);
    }


    public FolderPackage withAdded(String packagePlus) {
        return new FolderPackage(folderPath + "/" + packagePlus.replace('.', '/'), packageName + "." + packagePlus);
    }

    public File getFolder() {
        return folder;
    }

    public void setFolder(File folder) {
        this.folder = folder;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

}
