package org.wdzl.service.impl;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wdzl.dao.FriendsRequestMapper;
import org.wdzl.dao.MyFriendsMapper;
import org.wdzl.entity.FriendsRequest;
import org.wdzl.entity.MyFriends;
import org.wdzl.enums.MsgActionEnum;
import org.wdzl.netty.chatentity.DataContent;
import org.wdzl.netty.chatentity.UserChannelRel;
import org.wdzl.service.MyFriendsService;
import org.wdzl.utils.JsonUtils;
import org.wdzl.vo.MyFriendVo;

import java.util.List;

@Service
public class MyFriendsServiceImpl implements MyFriendsService {
    @Autowired
    MyFriendsMapper myFriendsMapper;

    @Autowired
    FriendsRequestMapper friendsRequestMapper;

    @Autowired
    Sid sid;

    @Override
    @Transactional
    public void acceptFriendRequest(String senderId, String accepterId) {
        //双向插入好友数据
        saveFriends(senderId,accepterId);
        saveFriends(accepterId,senderId);
        //好友添加完成之后还需要删除好友请求
        deleteFriendRequest(senderId,accepterId);

        //添加完好友之后，使用websocket主动对添加者的好友list进行更新
        Channel sendChannel = UserChannelRel.get(senderId);
        if(sendChannel != null){
            DataContent dataContent = new DataContent();
            dataContent.setAction(MsgActionEnum.PULL_FRIEND.getState());
            //将dataContent以JSON字符串的形式发送到前端
            sendChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(dataContent)));
        }
    }

    @Override
    public List<MyFriendVo> queryFriendsList(String myUserId) {
        return myFriendsMapper.queryFriendsList(myUserId);
    }

    public void saveFriends(String senderId, String accepterId){
        MyFriends friends = new MyFriends();
        friends.setId(sid.nextShort());
        friends.setMyFriendUserId(senderId);
        friends.setMyUserId(accepterId);
        myFriendsMapper.insert(friends);
    }

    public void deleteFriendRequest(String senderId,String accepterId){
        FriendsRequest friendsRequest = new FriendsRequest();
        friendsRequest.setSendUserId(senderId);
        friendsRequest.setAcceptUserId(accepterId);
        friendsRequestMapper.deleteFriendRequst(friendsRequest);
    }
}
