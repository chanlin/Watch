package com.jajale.watch.entity;

import com.jajale.watch.entitydb.FamilyMember;

import java.util.List;

/**
 *
 * 家庭成员实体类
 * Created by lilonghui on 2015/12/1.
 * Email:lilonghui@bjjajale.com
 */
public class FamilyMemberData {


    /**
     * userID : E580C8FD-DE40-487A-844C-A73E7A1EA2C4
     * phone : 13667466789
     * relation : 爸爸
     * isManage : 1
     */

    private List<FamilyMember> familyList;

    public void setFamilyList(List<FamilyMember> familyList) {
        this.familyList = familyList;
    }

    public List<FamilyMember> getFamilyList() {
        return familyList;
    }

}
