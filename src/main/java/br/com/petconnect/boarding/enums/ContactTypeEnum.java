package br.com.petconnect.boarding.enums;

public enum ContactTypeEnum {
    TELEFONE("T","Telefone"),
    CELULAR("C","Celular");


    private String codeType;
    private String description;
    public String getCodeType() {
        return codeType;
    }

    ContactTypeEnum(String codeType, String description){
        this.codeType = codeType;
        this.description = description;
    }

    public static boolean isValidCode(String code) {
        for (ContactTypeEnum type : ContactTypeEnum.values()) {
            if (type.getCodeType().equals(code)) {
                return true;
            }
        }
        return false;
    }
}
