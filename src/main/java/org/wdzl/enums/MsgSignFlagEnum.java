package org.wdzl.enums;

public enum MsgSignFlagEnum {
    UNSIGNED(0,"未签收"),
    SIGNED(1,"已签收");

    private int state;
    private String stateInfo;

    public int getState(){
        return state;
    }

    public String getStateInfo(){
        return stateInfo;
    }

    //私有构造函数，防止被外界实例化，直接提供静态方法调用
    private MsgSignFlagEnum(int state, String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }


    //根据state查找对应的enum对象   values()->UserStateEnum中所有的enum对象
    public static MsgSignFlagEnum stateOf(int state){
        for (MsgSignFlagEnum stateEnum:values()){
            if (state == stateEnum.state){
                return stateEnum;
            }
        }
        return null;
    }
}
