package com.jajale.watch.entitydb;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by guokm on 2016/5/20.
 *
 *
 */

@DatabaseTable(tableName = "QAEntity")
public class QACategoryEntity implements Serializable {

    @DatabaseField(id = true,columnName = "qa_id")
    public String qa_id;
    @DatabaseField(columnName="q_url")
    public  String q_url;
    @DatabaseField(columnName="q_type")
    public  String q_type;
    @DatabaseField(columnName="category")
    public  String category;



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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "QACategoryEntity{" +
                "qa_id='" + qa_id + '\'' +
                ", q_url='" + q_url + '\'' +
                ", q_type='" + q_type + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
