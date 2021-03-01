package org.wdzl.dao;

import org.wdzl.entity.MyFriends;

public interface MyFriendsMapper {
    int deleteByPrimaryKey(String id);

    int insert(MyFriends record);

    int insertSelective(MyFriends record);

    MyFriends selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MyFriends record);

    int updateByPrimaryKey(MyFriends record);

    //查询两个id是否是好友
    int queryIfFriends(String myUserId, String myFriendUserId);
}