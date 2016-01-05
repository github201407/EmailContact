package com.example.administrator.emailcontact.model;

/**
 * 扩展联系人
 * Created by Administrator on 2016/1/4.
 */
public class Plaxo {
    public String id;             //本地id
    public String userid;         //联系人服务器id
    public String regtime;        //写入时间	格式：2016-01-01 10:00
    public String actiontype;     //操作类型	默认：空，新添加：add
    public String updatetime;     //操作时间	格式：2016-01-01 10:00
    public String states;         //当前状态	默认：true，删除：false
    public String name;           //姓名
    public String phone;          //个人号码
    public String workphone;      //办公电话
    public String homephone;      //家庭电话
    public String photo;          //头像
    public String email;          //个人邮箱
    public String workemail;      //工作邮箱
    public String address;        //地址
    public String groups;         //分组
    public String custom;         //自定义数据

    public Plaxo(){}

    public Plaxo(String id, String userid, String regtime, String actiontype, String updatetime,
                 String states, String name, String phone, String workphone, String homephone,
                 String photo, String email, String workemail, String address, String groups,
                 String custom) {
        this.id = id;
        this.userid = userid;
        this.regtime = regtime;
        this.actiontype = actiontype;
        this.updatetime = updatetime;
        this.states = states;
        this.name = name;
        this.phone = phone;
        this.workphone = workphone;
        this.homephone = homephone;
        this.photo = photo;
        this.email = email;
        this.workemail = workemail;
        this.address = address;
        this.groups = groups;
        this.custom = custom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Plaxo plaxo = (Plaxo) o;

        if (!name.equals(plaxo.name)) return false;
        if (phone != null ? !phone.equals(plaxo.phone) : plaxo.phone != null) return false;
        return email != null ? email.equals(plaxo.email) : plaxo.email == null;

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
