package com.yzjiang.service.small.memorandum;

import com.yzjiang.dao.member.entity.MemberInfo;
import com.yzjiang.dao.small.memorandum.entity.MemorandumEntity;
import com.yzjiang.dao.small.memorandum.repository.MemorandumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yzjiang
 * @description
 * @date 2018/11/1 0001 16:08
 */
@Service
public class MemorandumService {
    @Autowired
    private MemorandumRepository memorandumRepository;

    public List<MemorandumEntity> allMemberInfo() {
        return memorandumRepository.findAll();
    }
}
