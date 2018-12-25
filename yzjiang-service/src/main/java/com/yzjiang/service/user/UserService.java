package com.yzjiang.service.user;

import com.yzjiang.dao.user.entity.User;
import com.yzjiang.dao.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yzjiang
 * @description
 * @date 2018/10/11 0011 17:17
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userDAO;

    public User getUserInfo(String userName, String password) {
        return userDAO.findByUsernameAndPassword(userName, password);
    }
}
