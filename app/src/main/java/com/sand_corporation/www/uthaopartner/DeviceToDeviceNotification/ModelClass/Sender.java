package com.sand_corporation.www.uthaopartner.DeviceToDeviceNotification.ModelClass;

/**
 * Created by HP on 1/30/2018.
 */

public class Sender {

    public String to;
    public Data data;


    public Sender() {

    }

    public Sender(Data data, String to) {
        this.to = to;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
