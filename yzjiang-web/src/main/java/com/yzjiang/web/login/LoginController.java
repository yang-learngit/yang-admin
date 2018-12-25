package com.yzjiang.web.login;

import com.yzjiang.common.constant.ApiCode;
import com.yzjiang.common.constant.Constants;
import com.yzjiang.dao.user.entity.User;
import com.yzjiang.service.user.UserService;
import com.yzjiang.web.common.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @Description
 * @author yzjiang
 * @Date 2018/9/29 0029 16:30
 */
@RestController
@RequestMapping("/api/login")
public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;



    /**
     * 登录验证
     */
    @RequestMapping("")
    public ResultVO login(HttpServletRequest request, HttpServletResponse response) {
        String userName = request.getParameter("userName");
        if (StringUtils.isEmpty(userName)) {
            return ResultVO.fail(ApiCode.BAD_REQUEST_EMPTY_PARAMER, "用户名为空！");
        }
        String password = request.getParameter("password");
        if (StringUtils.isEmpty(password)) {
            return ResultVO.fail(ApiCode.BAD_REQUEST_EMPTY_PARAMER, "密码为空！");
        }
        String verification = request.getParameter("kaptcha");
        if (StringUtils.isEmpty(verification)) {
            return ResultVO.fail(ApiCode.BAD_REQUEST_EMPTY_PARAMER, "验证码为空！");
        }
        String kaptchaCode = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (StringUtils.isEmpty(kaptchaCode)) {
            return ResultVO.fail(ApiCode.BAD_REQUEST_EMPTY_PARAMER, "验证码过期，刷新验证码！");
        }
        if (!verification.contentEquals(kaptchaCode)) {
            return ResultVO.fail(ApiCode.BAD_REQUEST_KAPTCHA, "验证码不正确！");
        } else {
            User user = userService.getUserInfo(userName, password);
            if (user == null) {
                return ResultVO.fail(ApiCode.BAD_REQUEST, "用户名或密码不正确！");
            }

        }
        return ResultVO.ok();
    }

}
