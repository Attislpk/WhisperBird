package org.wdzl.service;

import org.wdzl.entity.User;

public interface UserService {

    User selectUserById(String id);

    User queryByUsername(String username);

    int insertUser(User user);

    int updateByPrimaryKeySelective(User user);

    Integer preconditionForFriendSearch(String myUserId, String friendsUsername);
}
