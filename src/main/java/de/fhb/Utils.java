package de.fhb;

import org.apache.commons.io.output.FileWriterWithEncoding;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class Utils {

    private final String rootPath;
    private final String packageName;
    private final String packagePath;
    private final String workDir;

    private final FolderPackage rootPackage;

    private final Map<FileType, FolderPackage> getFileMetaData = new HashMap<FileType, FolderPackage>() {{
        put(FileType.DTO, rootPackage.withAdded("dto"));
        put(FileType.APPLICATION_PROPERTIES, new FolderPackage(rootPath + "resources", ""));
        put(FileType.MODEL, rootPackage.withAdded("model"));
        put(FileType.SERVICE_INTERFACE, rootPackage.withAdded("service.interface"));
        put(FileType.SERVICE, rootPackage.withAdded("service"));
        put(FileType.REPOSITORY, rootPackage.withAdded("repository"));
        put(FileType.CONTROLLER, rootPackage.withAdded("controller"));
        put(FileType.MAIN_CLASS, rootPackage);
        put(FileType.BUILD_SETTINGS, new FolderPackage(".", ""));
    }};

    public Utils(String rootPath, String packageName, String workdir) {
        workDir = workdir;
        this.rootPath = workdir + rootPath;
        this.packageName = packageName;
        this.packagePath = rootPath + packageName.replace('.', '/');
        rootPackage = new FolderPackage(packagePath, packageName);

    }


    public void createMainClass() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriterWithEncoding(new File(
                rootPackage.getFolder(), "Application.java"), "UTF("));
        final String content = "package " + packageName + ";" +
                "import org.springframework.boot.SpringApplication;\n" +
                "import org.springframework.boot.autoconfigure.SpringBootApplication;\n" +
                "import org.springframework.boot.builder.SpringApplicationBuilder;\n" +
                "import org.springframework.boot.context.web.SpringBootServletInitializer;\n" +
                "\n" +
                "@SpringBootApplication\n" +
                "public class CampusAppEvalBackendApplication extends SpringBootServletInitializer {\n" +
                "\n" +
                "    @Override\n" +
                "    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {\n" +
                "        return application.sources(CampusAppEvalBackendApplication.class);\n" +
                "    }\n" +
                "\n" +
                "    public static void main(String[] args) {\n" +
                "        SpringApplication.run(CampusAppEvalBackendApplication.class, args);\n" +
                "    }\n" +
                "}";
        bufferedWriter.write(content);
        bufferedWriter.flush();

    }

    public Map<FileType, FolderPackage> getGetFileMetaData() {
        return getFileMetaData;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public FolderPackage getRootPackage() {
        return rootPackage;
    }

    public String getWorkDir() {
        return workDir;
    }

    public String getRootPath() {
        return rootPath;
    }

    public String getPackageName() {
        return packageName;
    }
}
