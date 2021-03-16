package org.wdzl.enums;

public enum MsgActionEnum {
    CONNECT(1,"第一次(或重连)初始化连接"),
    CHAT(2,"聊天消息"),
    SIGNED(3,"消息签收"),
    KEEPALIVE(4,"客户端保持心跳"),
    PULL_FRIEND(5,"客户端拉取好友列表");

    private int state;
    private String stateInfo;

    public int getState(){
        return state;
    }

    public String getStateInfo(){
        return stateInfo;
    }

    //私有构造函数，防止被外界实例化，直接提供静态方法调用
    private MsgActionEnum(int state, String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }


    //根据state查找对应的enum对象   values()->UserStateEnum中所有的enum对象
    public static MsgActionEnum stateOf(int state){
        for (MsgActionEnum stateEnum:values()){
            if (state == stateEnum.state){
                return stateEnum;
            }
        }
        return null;
    }
}
