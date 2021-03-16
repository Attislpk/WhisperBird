package org.wdzl.controller;

import com.alibaba.druid.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wdzl.entity.ChatMsg;
import org.wdzl.service.ChatMsgService;
import org.wdzl.utils.JsonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("chat")
public class ChatMsgController {

    @Autowired
    private ChatMsgService chatMsgService;


    //手机客户端获取未签收的消息列表
    @PostMapping("/getunreadmsglist")
    @ResponseBody
    public Map<String,Object> getUnReadMsgList(String acceptId){
        Map<String,Object> modelMap = new HashMap<>();
        //acceptId不能为空
        if(StringUtils.isEmpty(acceptId)){
            modelMap.put("success",false);
            modelMap.put("errMsg","acceptId不能为空");
        }else {
            List<ChatMsg> unReadMsgList = chatMsgService.getUnReadMsgList(acceptId);
            modelMap.put("success",true);
            modelMap.put("data",unReadMsgList);
        }
        return modelMap;
    }
}
