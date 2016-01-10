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

    private final Map<FileType, FolderPackage> fileMetaData;

    public Utils(String rootPath, String packageName, String workDir) {
        this.workDir = workDir;
        this.rootPath = (workDir + rootPath);
        this.packageName = packageName;
        this.packagePath = this.rootPath + packageName.replace('.', '/');
        rootPackage = new FolderPackage(packagePath, packageName);

        fileMetaData = new HashMap<FileType, FolderPackage>() {{
            put(FileType.DTO, rootPackage.withAdded("dto"));
            put(FileType.APPLICATION_PROPERTIES, new FolderPackage(workDir + "src/main/resources", ""));
            put(FileType.MODEL, rootPackage.withAdded("model"));
            put(FileType.SERVICE_INTERFACE, rootPackage.withAdded("service.interfaces"));
            put(FileType.SERVICE, rootPackage.withAdded("service"));
            put(FileType.REPOSITORY, rootPackage.withAdded("repository"));
            put(FileType.CONTROLLER, rootPackage.withAdded("controller"));
            put(FileType.MAIN_CLASS, rootPackage);
            put(FileType.BUILD_SETTINGS, new FolderPackage(workDir + "/.", ""));
        }};
    }


    public void createMainClass() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriterWithEncoding(new File(
                rootPackage.getFolder(), "Application.java"), "UTF-8"));
        final String content = "package " + packageName + ";\n\n" +
                "import org.springframework.boot.SpringApplication;\n" +
                "import org.springframework.boot.autoconfigure.SpringBootApplication;\n" +
                "import org.springframework.boot.builder.SpringApplicationBuilder;\n" +
                "import org.springframework.boot.context.web.SpringBootServletInitializer;\n" +
                "\n" +
                "@SpringBootApplication\n" +
                "public class Application extends SpringBootServletInitializer {\n" +
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

    public void createDataSourceConfiguration() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriterWithEncoding(new File(
                rootPackage.getFolder(), "DataSourceConfiguration.java"), "UTF-8"));
        final String content = "package " + packageName + ";\n\n" + "import com.zaxxer.hikari.HikariConfig;\n" +
                "import com.zaxxer.hikari.HikariDataSource;\n" +
                "\n" +
                "import org.springframework.beans.factory.annotation.Value;\n" +
                "import org.springframework.context.annotation.Bean;\n" +
                "import org.springframework.context.annotation.Configuration;\n" +
                "\n" +
                "import java.util.Properties;\n" +
                "\n" +
                "import javax.sql.DataSource;\n" +
                "\n" +
                "@Configuration\n" +
                "public class DataSourceConfiguration {\n" +
                "\n" +
                "    @Value(\"${spring.datasource.username}\")\n" +
                "    private String user;\n" +
                "\n" +
                "    @Value(\"${spring.datasource.password}\")\n" +
                "    private String password;\n" +
                "\n" +
                "    @Value(\"${spring.datasource.url}\")\n" +
                "    private String dataSourceUrl;\n" +
                "\n" +
                "    @Value(\"${spring.datasource.driver-class-name}\")\n" +
                "    private String driverClassName;\n" +
                "\n" +
                "    @Value(\"${spring.datasource.connection-test-query}\")\n" +
                "    private String connectionTestQuery;\n" +
                "\n" +
                "\n" +
                "    @Bean\n" +
                "    public DataSource primaryDataSource() {\n" +
                "        Properties dsProps = new Properties();\n" +
                "        dsProps.setProperty(\"url\", dataSourceUrl);\n" +
                "        dsProps.setProperty(\"user\", user);\n" +
                "        dsProps.setProperty(\"password\", password);\n" +
                "\n" +
                "        Properties configProps = new Properties();\n" +
                "        configProps.setProperty(\"connectionTestQuery\", connectionTestQuery);\n" +
                "        configProps.setProperty(\"driverClassName\", driverClassName);\n" +
                "        configProps.setProperty(\"jdbcUrl\", dataSourceUrl);\n" +
                "\n" +
                "        HikariConfig hc = new HikariConfig(configProps);\n" +
                "        hc.setDataSourceProperties(dsProps);\n" +
                "        HikariDataSource dataSource =  new HikariDataSource(hc);\n" +
                "        dataSource.setMaximumPoolSize(100);\n" +
                "        dataSource.addDataSourceProperty(\"cachePrepStmts\", true);\n" +
                "        dataSource.addDataSourceProperty(\"prepStmtCacheSize\", 250);\n" +
                "        dataSource.addDataSourceProperty(\"prepStmtCacheSqlLimit\", 2048);\n" +
                "        dataSource.addDataSourceProperty(\"useServerPrepStmts\", true);\n" +
                "        return dataSource;\n" +
                "    }\n" +
                "\n" +
                "}\n";
        bufferedWriter.write(content);
        bufferedWriter.flush();
    }

    public Map<FileType, FolderPackage> getFileMetaData() {
        return fileMetaData;
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
