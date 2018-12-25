package com.yzjiang.service.member;

import com.yzjiang.dao.member.entity.NoticeEntity;
import com.yzjiang.dao.member.repository.NoticeRepository;
import com.yzjiang.service.member.model.NoticeDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import java.util.Optional;

/**
 * @author yzjiang
 * @description
 * @date 2018/11/2 0002 10:45
 */
@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    public List<NoticeEntity> allNotice() {
        return noticeRepository.findAll();
    }

    public NoticeEntity addNotice(NoticeDTO noticeDTO) {
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setTitle(noticeDTO.getTitle());
        noticeEntity.setContent(noticeDTO.getContent());
        noticeEntity.setCreateTime(new Date());
        noticeEntity.setUpdateTime(new Date());
        NoticeEntity result = noticeRepository.save(noticeEntity);
        return result;
    }

    public NoticeEntity findNotice(Integer noticeId) {
        return noticeRepository.findById(noticeId).get();
    }
}
