package org.wdzl.enums;

public enum UserStateEnum {
    OK(200,"ok"),
    EMPTY(501,"用户信息为空"),
    ERROR(502,"账号或密码不正确"),
    INNER_ERROR(503,"服务器内部错误");

    private int state;
    private String stateInfo;

    public int getState(){
        return state;
    }

    public String getStateInfo(){
        return stateInfo;
    }

    //私有构造函数，防止被外界实例化，直接提供静态方法调用
    private UserStateEnum(int state, String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }


    //根据state查找对应的enum对象   values()->UserStateEnum中所有的enum对象
    public static UserStateEnum stateOf(int state){
        for (UserStateEnum stateEnum:values()){
            if (state == stateEnum.state){
                return stateEnum;
            }
        }
        return null;
    }
}
