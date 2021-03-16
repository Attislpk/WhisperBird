package org.wdzl.dao;

import org.wdzl.entity.ChatMsg;

import java.util.List;

public interface ChatMsgMapper {
    int deleteByPrimaryKey(String id);

    int insert(ChatMsg record);

    int insertSelective(ChatMsg record);

    ChatMsg selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ChatMsg record);

    int updateByPrimaryKey(ChatMsg record);

    void batchUpdateMsgStatus(List<String> msgList);

    List<ChatMsg> getUnReadMsgList(String acceptId);
}