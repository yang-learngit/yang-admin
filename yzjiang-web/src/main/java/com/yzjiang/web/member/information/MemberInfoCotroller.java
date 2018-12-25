package com.yzjiang.web.member.information;

import com.yzjiang.dao.member.entity.MemberInfo;
import com.yzjiang.service.member.MemberInfoService;
import com.yzjiang.web.common.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yzjiang
 * @description
 * @date 2018/10/30 0030 16:32
 */
@RestController
@RequestMapping("/api/member/information/")
public class MemberInfoCotroller {

    @Autowired
    private MemberInfoService memberInfoService;

    @RequestMapping("/list")
    public ResultVO listMemberInfo() {
        try{
            List<MemberInfo> list = memberInfoService.allMemberInfo();
            return ResultVO.of(list);

        } catch (Exception e) {
            return ResultVO.fail("", "");
        }
    }
}
