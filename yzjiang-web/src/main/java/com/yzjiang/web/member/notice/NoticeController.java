package com.yzjiang.web.member.notice;

import com.yzjiang.dao.member.entity.NoticeEntity;
import com.yzjiang.service.member.NoticeService;
import com.yzjiang.service.member.model.NoticeDTO;
import com.yzjiang.web.common.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yzjiang
 * @description
 * @date 2018/11/1 0001 14:39
 */
@RestController
@RequestMapping("/api/member/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/list")
    public ResultVO listNotice() {
        try {
            List<NoticeEntity> list = noticeService.allNotice();
            System.out.println("list ==== " + list.size());
//            for(int i=0;i<list.size();i++){
//                redisTemplate.opsForList().rightPush("NOTICE_LIST", list.get(i).getId());
//                redisTemplate.opsForValue().set("NOTICE_"+list.get(i).getId(), list.get(i).getTitle());
//            }
            Long deleteAfter = redisTemplate.opsForList().size("NOTICE_LIST");
            System.out.println("deleteAfter ==== " + deleteAfter);
            return ResultVO.of(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.fail("", "");
        }
    }

    @RequestMapping("/add")
    @ResponseBody
    public ResultVO addNotice(@RequestBody NoticeDTO noticeDTO) {
        try {
            NoticeEntity addBean = noticeService.addNotice(noticeDTO);
            redisTemplate.opsForList().leftPush("NOTICE_LIST", addBean.getId());
            redisTemplate.opsForValue().set("NOTICE_" + addBean.getId(), addBean.getTitle());
            redisTemplate.opsForList().trim("", 0, 10);
            Long kk = redisTemplate.opsForList().size("NOTICE_LIST");
            List<String> kkr = new ArrayList<>();
            redisTemplate.opsForValue().multiGet(kkr);
            redisTemplate.opsForValue().increment("",1);


            System.out.println("redisLong ==== " + kk);
            return ResultVO.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.fail("", "");
        }
    }

    @RequestMapping("/find")
    @ResponseBody
    public ResultVO findNotice(@RequestBody NoticeDTO noticeDTO) {
        try {
            NoticeEntity notice = noticeService.findNotice(noticeDTO.getId());
            return ResultVO.of(notice);
        } catch (Exception e) {
            return ResultVO.fail("", "");
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ResultVO deleteNotice(@RequestBody NoticeDTO noticeDTO) {
        try {
            NoticeEntity notice = noticeService.findNotice(noticeDTO.getId());
            Long deleteBefore = redisTemplate.opsForList().size("NOTICE_LIST");
            System.out.println("deleteBefore ==== " + deleteBefore);
            redisTemplate.opsForList().remove("NOTICE_LIST", 1, notice.getId());
            redisTemplate.delete("NOTICE_" + notice.getId());
            Long deleteAfter = redisTemplate.opsForList().size("NOTICE_LIST");
            System.out.println("deleteAfter ==== " + deleteAfter);

            return ResultVO.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.fail("", "");
        }
    }


    @RequestMapping("/load-new")
    public ResultVO loadNewNotice() {
        try {
            Long deleteAfter = redisTemplate.opsForList().size("NOTICE_LIST");
            System.out.println("deleteAfter ==== " + deleteAfter);
            List<Integer> list = redisTemplate.opsForList().range("NOTICE_LIST", 0, 4);
            System.out.println("list ==== " + list.size());
            List<String> result = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i) + "=========" + deleteAfter);
                result.add(redisTemplate.opsForValue().get("NOTICE_" + list.get(i)).toString());
            }
            return ResultVO.of(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.fail("", "");
        }
    }
}
