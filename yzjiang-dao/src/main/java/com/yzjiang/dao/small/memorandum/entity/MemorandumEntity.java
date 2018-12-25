package com.yzjiang.dao.small.memorandum.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author yzjiang
 * @description
 * @date 2018/11/1 0001 16:06
 */
@Entity
@Table(name = "t_memorandum_content", schema = "yzjiang_admin", catalog = "")
public class MemorandumEntity {
    private int id;
    private Integer userId;
    private String content;
    private Integer done;
    private Timestamp operDate;
    private Timestamp beginTime;
    private Timestamp finishTime;
    private Timestamp createTime;

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
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "done")
    public Integer getDone() {
        return done;
    }

    public void setDone(Integer done) {
        this.done = done;
    }

    @Basic
    @Column(name = "oper_date")
    public Timestamp getOperDate() {
        return operDate;
    }

    public void setOperDate(Timestamp operDate) {
        this.operDate = operDate;
    }

    @Basic
    @Column(name = "begin_time")
    public Timestamp getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }

    @Basic
    @Column(name = "finish_time")
    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemorandumEntity that = (MemorandumEntity) o;
        return id == that.id &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(content, that.content) &&
                Objects.equals(done, that.done) &&
                Objects.equals(operDate, that.operDate) &&
                Objects.equals(beginTime, that.beginTime) &&
                Objects.equals(finishTime, that.finishTime) &&
                Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userId, content, done, operDate, beginTime, finishTime, createTime);
    }
}
