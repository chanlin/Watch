package com.jajale.watch.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by lilonghui on 2015/12/8.
 * Email:lilonghui@bjjajale.com
 */
public class QuestionData {


    public static String Key="QuestionData";

    /**
     * title : 发语音会消耗流量吗？
     * contents :
     * sort : 1
     */

    private List<ProblemListEntity> problemList;

    public void setProblemList(List<ProblemListEntity> problemList) {
        this.problemList = problemList;
    }

    public List<ProblemListEntity> getProblemList() {
        return problemList;
    }

    public static class ProblemListEntity implements Parcelable {



        private String question;
        private String answer;
        private String sort;

        public void setTitle(String title) {
            this.question = title;
        }

        public void setContents(String contents) {
            this.answer = contents;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getTitle() {
            return question;
        }

        public String getContents() {
            return answer;
        }

        public String getSort() {
            return sort;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.question);
            dest.writeString(this.answer);
            dest.writeString(this.sort);
        }

        public ProblemListEntity() {
        }

        protected ProblemListEntity(Parcel in) {
            this.question = in.readString();
            this.answer = in.readString();
            this.sort = in.readString();
        }

        public static final Parcelable.Creator<ProblemListEntity> CREATOR = new Parcelable.Creator<ProblemListEntity>() {
            public ProblemListEntity createFromParcel(Parcel source) {
                return new ProblemListEntity(source);
            }

            public ProblemListEntity[] newArray(int size) {
                return new ProblemListEntity[size];
            }
        };
    }
}
