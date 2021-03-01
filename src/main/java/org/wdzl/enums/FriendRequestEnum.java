package org.wdzl.enums;

public enum FriendRequestEnum {
    SUCCESS(0,"ok"),
    ALREADY_SENT(1,"好友请求已发送");

    private int state;
    private String stateInfo;

    public int getState(){
        return state;
    }

    public String getStateInfo(){
        return stateInfo;
    }

    //私有构造函数，防止被外界实例化，直接提供静态方法调用
    private FriendRequestEnum(int state, String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }


    //根据state查找对应的enum对象   values()->UserStateEnum中所有的enum对象
    public static FriendRequestEnum stateOf(int state){
        for (FriendRequestEnum stateEnum:values()){
            if (state == stateEnum.state){
                return stateEnum;
            }
        }
        return null;
    }
}
