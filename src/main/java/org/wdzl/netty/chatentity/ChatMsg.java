package org.wdzl.netty.chatentity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
@Data
public class ChatMsg implements Serializable {

    @Serial
    private static final long serialVersionUID = 139702673131055280L;

    private String senderId;      //发送者的用户id
    private String receiverId;  //接收者的用户id
    private String msg;    //聊天内容
    private String msgId;  //用于进行消息的签收
}
