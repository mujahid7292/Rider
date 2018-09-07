package com.sand_corporation.www.uthaopartner.DeviceToDeviceNotification.ModelClass;

/**
 * Created by HP on 2/1/2018.
 */

public class Data {

    public String type;
    public String title;
    public String subtext;
    public String message;
    public String img_url;
    public String webUrl;

    public Data() {

    }

    public Data(String type, String title, String subtext, String message, String img_url, String webUrl) {
        this.type = type;
        this.title = title;
        this.subtext = subtext;
        this.message = message;
        this.img_url = img_url;
        this.webUrl = webUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtext() {
        return subtext;
    }

    public void setSubtext(String subtext) {
        this.subtext = subtext;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
