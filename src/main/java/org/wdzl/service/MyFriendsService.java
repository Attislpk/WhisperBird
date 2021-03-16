package org.wdzl.service;

import org.wdzl.vo.MyFriendVo;

import java.util.List;

public interface MyFriendsService {

    void acceptFriendRequest(String senderId,String accepterId);

    List<MyFriendVo> queryFriendsList(String myUserId);
}
