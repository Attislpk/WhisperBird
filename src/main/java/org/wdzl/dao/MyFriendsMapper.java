package org.wdzl.dao;

import org.wdzl.entity.MyFriends;
import org.wdzl.vo.MyFriendVo;

import java.util.List;

public interface MyFriendsMapper {
    int deleteByPrimaryKey(String id);

    //双向插入好友数据的方法
    int insert(MyFriends record);

    int insertSelective(MyFriends record);

    MyFriends selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MyFriends record);

    int updateByPrimaryKey(MyFriends record);

    //查询两个id是否是好友
    int queryIfFriends(String myUserId, String myFriendUserId);

    //查询好友列表的方法
    List<MyFriendVo> queryFriendsList(String myUserId);
}