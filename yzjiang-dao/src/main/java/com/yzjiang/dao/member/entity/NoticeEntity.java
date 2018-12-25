package com.yzjiang.dao.member.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * @author yzjiang
 * @description
 * @date 2018/11/2 0002 10:44
 */
@Entity
@Table(name = "t_member_notice", schema = "yzjiang_admin", catalog = "")
public class NoticeEntity {
    private Integer id;
    private String title;
    private String summary;
    private String content;
    private String status;
    private Date createTime;
    private Integer createId;
    private Date updateTime;
    private Integer updateId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "summary")
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "create_id")
    public Integer getCreateId() {
        return createId;
    }

    public void setCreateId(Integer createId) {
        this.createId = createId;
    }

    @Basic
    @Column(name = "update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Basic
    @Column(name = "update_id")
    public Integer getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoticeEntity that = (NoticeEntity) o;
        return id == that.id &&
                Objects.equals(title, that.title) &&
                Objects.equals(summary, that.summary) &&
                Objects.equals(content, that.content) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(createId, that.createId) &&
                Objects.equals(updateTime, that.updateTime) &&
                Objects.equals(updateId, that.updateId);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(id, title, summary, content, status, createTime, createId, updateTime, updateId);
        return result;
    }
}
