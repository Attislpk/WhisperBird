package org.wdzl.dao;

import org.wdzl.entity.FriendsRequest;
import org.wdzl.vo.FriendRequestVo;

import java.util.List;

public interface FriendsRequestMapper {
    int deleteByPrimaryKey(String id);

    //插入好友请求，即发送好友请求的方法
    int insert(FriendsRequest record);

    //查询好友请求是否在数据库中存在的方法
    int queryFriendRequest(String myUserId,String friendId);

    int insertSelective(FriendsRequest record);

    FriendsRequest selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(FriendsRequest record);

    int updateByPrimaryKey(FriendsRequest record);

    List<FriendRequestVo> queryFriendsList(String acceptUserId);

    //删除好友请求的方法
    void deleteFriendRequst(FriendsRequest friendsRequest);
}