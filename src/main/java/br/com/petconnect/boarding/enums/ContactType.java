package br.com.petconnect.boarding.enums;

public enum ContactType {
    TELEFONE("T"),
    CELULAR("C");


    private String codeType;

    public String getCodeType() {
        return codeType;
    }

    ContactType(String codeType){
        this.codeType = codeType;
    }

    public static boolean isValidCode(String code) {
        for (ContactType type : ContactType.values()) {
            if (type.getCodeType().equals(code)) {
                return true;
            }
        }
        return false;
    }
}
