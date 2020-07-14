package com.codewarriors.hackathone.relaypension.customvariablesforparsing;

public class ConsituencyCustomVAR {
    String username,uresid,aadharno,consituency,applicationstate,errorinapplication;

    public ConsituencyCustomVAR() {
    }

    public ConsituencyCustomVAR(String username,String uresid, String aadharno, String consituency, String applicationstate,String errorinapplication) {
        this.username=username;
        this.uresid = uresid;
        this.aadharno = aadharno;
        this.consituency = consituency;
        this.applicationstate = applicationstate;
        this.errorinapplication=errorinapplication;
    }

    public String getUsername() {
        return username;
    }

    public String getUresid() {
        return uresid;
    }

    public String getAadharno() {
        return aadharno;
    }

    public String getConsituency() {
        return consituency;
    }

    public String getApplicationstate() {
        return applicationstate;
    }

    public String getErrorinapplication() {
        return errorinapplication;
    }
}
