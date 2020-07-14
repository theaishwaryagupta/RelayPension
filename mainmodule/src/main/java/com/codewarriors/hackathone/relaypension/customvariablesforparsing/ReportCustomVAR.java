package com.codewarriors.hackathone.relaypension.customvariablesforparsing;

public class ReportCustomVAR {
    private String aadharno,formno,messag,already;

    public ReportCustomVAR() {
    }

    public ReportCustomVAR(String aadharno, String formno, String messag,String already) {
        this.aadharno = aadharno;
        this.formno = formno;
        this.messag = messag;
        this.already=already;
    }


    public String getAadharno() {
        return aadharno;
    }

    public String getFormno() {
        return formno;
    }

    public String getMessag() {
        return messag;
    }

    public String getAlready() {
        return already;
    }
}
