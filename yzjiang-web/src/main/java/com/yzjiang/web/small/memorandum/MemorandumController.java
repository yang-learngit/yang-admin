package com.yzjiang.web.small.memorandum;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yzjiang.dao.small.memorandum.entity.MemorandumEntity;
import com.yzjiang.service.small.memorandum.MemorandumService;
import com.yzjiang.web.common.ResultVO;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yzjiang
 * @description
 * @date 2018/11/1 0001 16:03
 */
@RestController
@RequestMapping("/api/small/memorandum/")
public class MemorandumController {

    @Autowired
    private MemorandumService memorandumService;

    @RequestMapping("/list")
    public ResultVO listMemberInfo() {
        try{
            List<MemorandumEntity> list = memorandumService.allMemberInfo();
            List<JSONObject> datas = list.stream().map(obj -> {
                JSONObject jsonObject = (JSONObject) JSON.toJSON(obj);
                jsonObject.put("done", jsonObject.getIntValue("done")==0?true:false);
                jsonObject.put("beginTime", jsonObject.getTimestamp("beginTime").getHours());
                jsonObject.put("finishTime", jsonObject.getTimestamp("finishTime").getHours());
                System.out.println(jsonObject.toJSONString());
                //System.out.println(DateUtils.parseDate(jsonObject.getString("endTime"),"DD.hh"));
                return jsonObject;
            }).collect(Collectors.toList());
            //System.out.println(DateUtils.parseDate("","DD.hh"));

            return ResultVO.of(datas);

        } catch (Exception e) {
            return ResultVO.fail("", "");
        }
    }


}
