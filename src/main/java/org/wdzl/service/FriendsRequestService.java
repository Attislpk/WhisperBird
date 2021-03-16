package org.wdzl.service;

import org.wdzl.entity.FriendsRequest;
import org.wdzl.entity.MyFriends;
import org.wdzl.vo.FriendRequestVo;

import java.util.List;

public interface FriendsRequestService {

    //向friendRquest表中插入好友请求的方法
    int insertFriendRequest(String myUserId, String friendUsername);

    //从friendRequest表中查询好友请求的方法
    int queryFriendRequest(String myUserId,String friendUsername);

    //联合friendRequst和User表，传入accptUserId,返回FriendRequestVo的list到前端并动态展示好友添加列表
    List<FriendRequestVo> queryFriendsList(String acceptUserId);

    //删除好友请求的方法
    void deleteFriendRequst(FriendsRequest friendsRequest);

}

