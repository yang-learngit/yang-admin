package com.yzjiang.dao.user.repository;

import com.yzjiang.dao.BaseRepository;
import com.yzjiang.dao.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author yzjiang
 * @date 2018/10/11 0011 11:11
 */
@Repository
public interface UserRepository extends BaseRepository<User, Long> {

    /**
     * 根据用户名和密码获取信息
     * @param username
     * @param password
     * @return
     */
    @Query("select u from User u where u.username = ?1 and u.password = ?2 and u.deleted = 0")
    User findByUsernameAndPassword(String username, String password);

}
