package org.wdzl.service.impl;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.wdzl.dao.ChatMsgMapper;
import org.wdzl.netty.chatentity.ChatMsg;
import org.wdzl.enums.MsgSignFlagEnum;
import org.wdzl.service.ChatMsgService;

import java.util.Date;
import java.util.List;
@Service
public class ChatMsgServiceImpl implements ChatMsgService {

    @Autowired
    Sid sid;

    @Autowired
    ChatMsgMapper chatMsgMapper;

    @Override
    public String insert(ChatMsg msg) {
        //新建entity包下的ChatMsg对象，用于插入数据库
        org.wdzl.entity.ChatMsg chatMsg = new org.wdzl.entity.ChatMsg();
        chatMsg.setId(sid.nextShort());
        chatMsg.setAcceptUserId(msg.getReceiverId());
        chatMsg.setSendUserId(msg.getSenderId());
        chatMsg.setMsg(msg.getMsg());
        chatMsg.setSignFlag(MsgSignFlagEnum.UNSIGNED.getState());
        chatMsg.setCreateTime(new Date());
        int result = chatMsgMapper.insert(chatMsg);
        if (result == 1){
            return chatMsg.getId();
        }
        else {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateMsgSigned(List<String> msgList) {
        chatMsgMapper.batchUpdateMsgStatus(msgList);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<org.wdzl.entity.ChatMsg> getUnReadMsgList(String acceptId) {
        return chatMsgMapper.getUnReadMsgList(acceptId);
    }
}
