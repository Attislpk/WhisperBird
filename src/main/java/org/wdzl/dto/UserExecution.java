package org.wdzl.dto;

import org.wdzl.entity.User;
import org.wdzl.vo.UserVo;

public class UserExecution {

    private boolean success;
    private int errorCode;
    private String errMsg;
    private UserVo userVo;

    //成功时执行的构造方法
    public UserExecution(boolean success, UserVo userVo){
        this.success = success;
        this.userVo = userVo;
    }

    //失败时执行的构造方法
    public UserExecution(boolean success, int errorCode, String errMsg){
        this.success = success;
        this.errorCode = errorCode;
        this.errMsg = errMsg;
    }
}
