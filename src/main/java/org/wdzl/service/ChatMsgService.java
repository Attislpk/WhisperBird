package org.wdzl.service;

import org.wdzl.netty.chatentity.ChatMsg;

import java.util.List;

public interface ChatMsgService {


    String insert(ChatMsg record);

    void updateMsgSigned(List<String> msgList);

    //查询后端没有还没有签收的消息列表
    List<org.wdzl.entity.ChatMsg> getUnReadMsgList(String acceptId);

}
