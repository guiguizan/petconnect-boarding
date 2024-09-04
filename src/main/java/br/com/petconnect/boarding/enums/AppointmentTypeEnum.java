package br.com.petconnect.boarding.enums;

import br.com.petconnect.boarding.exception.BusinessException;

public enum AppointmentTypeEnum {
    SCHEDULE("SCHEDULE"),
    BATH("BATH");


    public String getServiceType() {
        return serviceType;
    }

    private String serviceType;

     AppointmentTypeEnum(String serviceType){
        this.serviceType = serviceType;
    }

    public static String fromServiceType(String serviceType) {
        for (AppointmentTypeEnum type : AppointmentTypeEnum.values()) {
            if (type.getServiceType().equalsIgnoreCase(serviceType)) {
                return type.getServiceType();
            }
        }
        throw new BusinessException("Código de serviço desconhecido: " + serviceType);
    }
}
