package com.example.administrator.emailcontact.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/27 0027.
 * 联系人详情
 */
public class ContactsInfo implements Serializable {
    private String name ;
    private String pinyin ; //姓名全拼
    private String pinyinfirst ; //首拼音
    private String phone ;
    private String note ; //备注
    private String email ;
    private String ground ;//分组
    private String headUrl ;


    public ContactsInfo() {
    }

    /**用于数据库存储*/
    public ContactsInfo(String name,String pinyinfirst,String pinyin ,String phone,
                        String note, String email,String ground,String headUrl){
        this.name = name;
        this.pinyin = pinyin;
        this.pinyinfirst = pinyinfirst ;
        this.phone = phone ;
        this.note = note ;
        this.email = email ;
        this.ground = ground ;
        this.headUrl = headUrl ;
    }

    /**用于显示*/
    public ContactsInfo(String name,String phone, String note, String email,String ground,String headUrl){
        this.name = name;
        this.phone = phone ;
        this.note = note ;
        this.email = email ;
        this.ground = ground ;
        this.headUrl = headUrl ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getPinyinfirst() {
        return pinyinfirst;
    }

    public void setPinyinfirst(String pinyinfirst) {
        this.pinyinfirst = pinyinfirst;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGround() {
        return ground;
    }

    public void setGround(String ground) {
        this.ground = ground;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    @Override
    public String toString() {
        return "联系人详情：ContactsInfo{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", note='" + note + '\'' +
                ", email='" + email + '\'' +
                ", ground='" + ground + '\'' +
                ", headUrl='" + headUrl + '\'' +
                '}';
    }
}
