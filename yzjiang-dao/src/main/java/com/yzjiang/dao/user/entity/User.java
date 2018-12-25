package com.yzjiang.dao.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @Description
 * @author yzjiang
 * @Date 2018/10/10 0010 18:02
 */
@Entity
@Table(name = "t_sys_user")
public class User implements Serializable {

    private Long id;

    private String username;

    private String password;

    private Date createTime;

    private Integer deleted;

    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    @Column(name = "create_time")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    @Column(name = "deleted")
    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
