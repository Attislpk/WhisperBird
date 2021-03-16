package org.wdzl.service.impl;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wdzl.dao.FriendsRequestMapper;
import org.wdzl.dao.MyFriendsMapper;
import org.wdzl.dao.UserMapper;
import org.wdzl.entity.FriendsRequest;
import org.wdzl.entity.MyFriends;
import org.wdzl.entity.User;
import org.wdzl.enums.FriendRequestEnum;
import org.wdzl.service.FriendsRequestService;
import org.wdzl.vo.FriendRequestVo;

import javax.enterprise.inject.Alternative;
import java.util.Date;
import java.util.List;

@Service
public class FriendsRequestServiceImpl implements FriendsRequestService {
    @Autowired
    FriendsRequestMapper friendsRequestMapper;

    @Autowired
    Sid sid;

    @Autowired
    UserMapper userMapper;

    @Autowired
    MyFriendsMapper myFriendsMapper;

    @Override
    public int insertFriendRequest(String myUserId, String friendUsername) {
        //friendUsername因为是搜索出来的，因此能够保证是合法且存在的
        User friend = userMapper.queryByUsername(friendUsername);
        FriendsRequest friendsRequest = new FriendsRequest();
        friendsRequest.setId(sid.nextShort());
        friendsRequest.setSendUserId(myUserId);
        friendsRequest.setAcceptUserId(friend.getId());
        friendsRequest.setRequestDateTime(new Date());
        //向数据库中插入friendRequest
        return friendsRequestMapper.insert(friendsRequest);
    }

    @Override
    public int queryFriendRequest(String myUserId, String friendUsername) {
        User friend = userMapper.queryByUsername(friendUsername);
        int result = friendsRequestMapper.queryFriendRequest(myUserId, friend.getId());
        if (result == FriendRequestEnum.ALREADY_SENT.getState()){
            //如果已经存在好友请求
            return FriendRequestEnum.ALREADY_SENT.getState();
        }else {
            //还不存在好友请求，则可以发送好友请求
            return FriendRequestEnum.SUCCESS.getState();
        }
    }

    @Override
    public List<FriendRequestVo> queryFriendsList(String acceptUserId) {
        //acceptUserId是从前端传来的，能够确保数据是合法的，此处不进行验证
        return friendsRequestMapper.queryFriendsList(acceptUserId);
    }

    //删除好友请求的方法
    @Override
    public void deleteFriendRequst(FriendsRequest friendsRequest) {
        friendsRequestMapper.deleteFriendRequst(friendsRequest);
    }

}
