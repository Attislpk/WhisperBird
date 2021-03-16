package org.wdzl.vo;

import lombok.Data;

/**
 * 向前端传递数据的vo，包括需要展示的发送请求用户的昵称，照片；还有userId，username
 */
@Data
public class FriendRequestVo {
    private String senderId;
    private String senderfaceImage;
    private String senderUsername;
    private String senderNickname;

}
