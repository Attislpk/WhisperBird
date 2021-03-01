package org.wdzl.enums;

public enum SearchFriendEnum {
    SUCCESS(0,"ok"),
    USER_NOT_EXIST(1,"该用户不存在"),
    YOURSELF(2,"不能添加自己为好友"),
    ALREADY_FRIENDS(3,"该用户已经是你的好友");

    private int state;
    private String stateInfo;

    public int getState(){
        return state;
    }

    public String getStateInfo(){
        return stateInfo;
    }

    //私有构造函数，防止被外界实例化，直接提供静态方法调用
    private SearchFriendEnum(int state, String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }


    //根据state查找对应的enum对象   values()->UserStateEnum中所有的enum对象
    public static SearchFriendEnum stateOf(int state){
        for (SearchFriendEnum stateEnum:values()){
            if (state == stateEnum.state){
                return stateEnum;
            }
        }
        return null;
    }
}
