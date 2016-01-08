package de.fhb;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class Utils {

    private final String ROOT_PATH;
    private final String PACKAGE_PATH;

    public Utils(String rootPath, String packagePath) {
        ROOT_PATH = rootPath;
        PACKAGE_PATH = ROOT_PATH +packagePath;
    }

    public Map<FileType, File> getfileFolderPaths() {
       return new HashMap<FileType, File>() {{
            put(FileType.DTO, new File(PACKAGE_PATH + "dto"));
            put(FileType.APPLICATION_PROPERTIES, new File(ROOT_PATH + "resources"));
            put(FileType.MODEL, new File(PACKAGE_PATH + "model"));
            put(FileType.SERVICE_INTERFACE, new File(PACKAGE_PATH + "service/interface"));
            put(FileType.SERVICE, new File(PACKAGE_PATH + "service"));
            put(FileType.REPOSITORY, new File(PACKAGE_PATH + "repository"));
            put(FileType.CONTROLLER, new File(PACKAGE_PATH + "controller"));
            put(FileType.MAIN_CLASS, new File(PACKAGE_PATH));
        }};
    }

    public String getROOT_PATH() {
        return ROOT_PATH;
    }

    public String getPACKAGE_PATH() {
        return PACKAGE_PATH;
    }
}
