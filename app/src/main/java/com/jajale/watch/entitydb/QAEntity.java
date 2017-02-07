package com.jajale.watch.entitydb;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by athena on 2015/11/11.
 * Email: lizhiqiang@bjjajale.com
 */

@DatabaseTable(tableName = "QAEntity")
public class QAEntity implements Serializable {

    @DatabaseField(id = true,columnName = "qa_id")
    public String qa_id;

    @DatabaseField(columnName="q_url")
    public  String q_url;
    @DatabaseField(columnName="q_type")
    public  String q_type;
    @DatabaseField(columnName="q_txt")
    public  String q_txt;
    @DatabaseField(columnName="category")
    public  String category;
    @DatabaseField(columnName="agelevel")
    public  String agelevel;
    @DatabaseField(columnName="a_url")
    public  String a_url;
    @DatabaseField(columnName="a_type")
    public  String a_type;
    @DatabaseField(columnName="a_txt")
    public  String a_txt;



    public String getQa_id() {
        return qa_id;
    }

    public void setQa_id(String qa_id) {
        this.qa_id = qa_id;
    }

    public String getQ_url() {
        return q_url;
    }

    public void setQ_url(String q_url) {
        this.q_url = q_url;
    }

    public String getQ_type() {
        return q_type;
    }

    public void setQ_type(String q_type) {
        this.q_type = q_type;
    }

    public String getQ_txt() {
        return q_txt;
    }

    public void setQ_txt(String q_txt) {
        this.q_txt = q_txt;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAgelevel() {
        return agelevel;
    }

    public void setAgelevel(String agelevel) {
        this.agelevel = agelevel;
    }

    public String getA_url() {
        return a_url;
    }

    public void setA_url(String a_url) {
        this.a_url = a_url;
    }

    public String getA_type() {
        return a_type;
    }

    public void setA_type(String a_type) {
        this.a_type = a_type;
    }

    public String getA_txt() {
        return a_txt;
    }

    public void setA_txt(String a_txt) {
        this.a_txt = a_txt;
    }

    @Override
    public String toString() {
        return "QAEntity{" +
                "qa_id='" + qa_id + '\'' +
                ", q_url='" + q_url + '\'' +
                ", q_type='" + q_type + '\'' +
                ", q_txt='" + q_txt + '\'' +
                ", category='" + category + '\'' +
                ", agelevel='" + agelevel + '\'' +
                ", a_url='" + a_url + '\'' +
                ", a_type='" + a_type + '\'' +
                ", a_txt='" + a_txt + '\'' +
                '}';
    }
}
