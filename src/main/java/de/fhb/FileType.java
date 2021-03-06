package de.fhb;

public enum FileType {
    DTO, MODEL, SERVICE, SERVICE_INTERFACE, REPOSITORY, APPLICATION_PROPERTIES, MAIN_CLASS,
    CONTROLLER, BUILD_SETTINGS, DTO_MAPPER;

    public static FileType byString(String type){
        switch (type){
            case "DTO": return FileType.DTO;
            case "Model": return FileType.MODEL;
            case "Service": return FileType.SERVICE;
            case "ServiceInterface": return FileType.SERVICE_INTERFACE;
            case "Repository": return FileType.REPOSITORY;
            case "ApplicationProperties": return FileType.APPLICATION_PROPERTIES;
            case "Controller": return FileType.CONTROLLER;
            case "BuildSettings": return FileType.BUILD_SETTINGS;
            case "DTOMapper": return FileType.DTO_MAPPER;
            default: return null;
        }
    }

}
