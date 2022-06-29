package com.ilovesshan.sms_receive_plugin_example.pojo;

public class Sms {
    private String senderPhone;
    private StringBuilder receiveMessage;

    public Sms(String senderPhone, StringBuilder receiveMessage) {
        this.senderPhone = senderPhone;
        this.receiveMessage = receiveMessage;
    }


    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public StringBuilder getReceiveMessage() {
        return receiveMessage;
    }

    public void setReceiveMessage(StringBuilder receiveMessage) {
        this.receiveMessage = receiveMessage;
    }

    @Override
    public String toString() {
        return "{\"senderPhone\":\"" + senderPhone + "\",\"receiveMessage\":\"" + receiveMessage + "\"}";
    }
}
