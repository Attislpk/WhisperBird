package org.wdzl.service;

import org.wdzl.entity.FriendsRequest;

public interface FriendsRequestService {

    int insertFriendRequest(String myUserId, String friendUsername);

    int queryFriendRequest(String myUserId,String friendUsername);
}
