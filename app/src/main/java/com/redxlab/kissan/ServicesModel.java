package com.redxlab.kissan;

public class ServicesModel {
    private String serviceName;
    private int serviceImage;

    public ServicesModel(String serviceName, int serviceImage) {
        this.serviceName = serviceName;
        this.serviceImage = serviceImage;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getServiceImage() {
        return serviceImage;
    }

    public void setServiceImage(int serviceImage) {
        this.serviceImage = serviceImage;
    }
}
