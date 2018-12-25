package com.yzjiang.service.member;

import com.yzjiang.dao.member.entity.MemberInfo;
import com.yzjiang.dao.member.repository.MemberInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yzjiang
 * @description
 * @date 2018/10/30 0030 15:56
 */
@Service
public class MemberInfoService {

    @Autowired
    private MemberInfoRepository memberInfoRepository;

    public Page<MemberInfo> listMemberInfo(String name, String phone, Integer pageIndex, Integer pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");

        return null;
    }

    public List<MemberInfo> allMemberInfo() {
        return memberInfoRepository.findAll();
    }
}
