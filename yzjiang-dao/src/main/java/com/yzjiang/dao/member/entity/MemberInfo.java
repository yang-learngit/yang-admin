package com.yzjiang.dao.member.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author yzjiang
 * @description
 * @date 2018/10/30 0030 11:55
 */
@Entity
@Table(name = "t_member_info")
public class MemberInfo implements Serializable {
    private int id;
    private int userId;
    private String name;
    private String phone;
    private Integer sex;
    private Integer age;
    private String address;
    private Integer referee;
    private String isMember;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Integer deleted;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "sex")
    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "age")
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "referee")
    public Integer getReferee() {
        return referee;
    }

    public void setReferee(Integer referee) {
        this.referee = referee;
    }

    @Basic
    @Column(name = "is_member")
    public String getIsMember() {
        return isMember;
    }

    public void setIsMember(String isMember) {
        this.isMember = isMember;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "update_time")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Basic
    @Column(name = "deleted")
    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberInfo that = (MemberInfo) o;
        return id == that.id &&
                userId == that.userId &&
                Objects.equals(name, that.name) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(sex, that.sex) &&
                Objects.equals(age, that.age) &&
                Objects.equals(address, that.address) &&
                Objects.equals(referee, that.referee) &&
                Objects.equals(isMember, that.isMember) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime) &&
                Objects.equals(deleted, that.deleted);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userId, name, phone, sex, age, address, referee, isMember, createTime, updateTime, deleted);
    }
}
