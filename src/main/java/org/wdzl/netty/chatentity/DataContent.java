package org.wdzl.netty.chatentity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
@Data
public class DataContent implements Serializable {
    @Serial
    private static final long serialVersionUID = -3587911808639762765L;

    private Integer action; //动作类型
    private ChatMsg chatMsg; //用户的聊天内容  entity
    private String extend; //拓展字段
}
