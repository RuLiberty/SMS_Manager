package ru.geekbrains.sms_manager;

import io.realm.RealmObject;

public class SMSTable extends RealmObject {

    private String from;
    private String message;
    private String statusInOut;

    public SMSTable(){}

    public SMSTable(String from, String message, String statusInOut){
        this.from = from;
        this.message = message;
        this.statusInOut = statusInOut;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatusInOut() {
        return statusInOut;
    }

    public void setStatusInOut(String statusInOut) {
        this.statusInOut = statusInOut;
    }

}
