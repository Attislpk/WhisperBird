package org.wdzl.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wdzl.bo.UserBo;
import org.wdzl.dao.FriendsRequestMapper;
import org.wdzl.entity.FriendsRequest;
import org.wdzl.entity.User;
import org.wdzl.enums.FriendRequestEnum;
import org.wdzl.enums.SearchFriendEnum;
import org.wdzl.service.FriendsRequestService;
import org.wdzl.service.UserService;
import org.wdzl.utils.FastDFSClient;
import org.wdzl.utils.FileUtils;
import org.wdzl.utils.MD5Utils;
import org.wdzl.vo.UserVo;

import javax.swing.plaf.nimbus.State;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    FastDFSClient fastDFSClient;

    @Autowired
    FriendsRequestService friendsRequestService;

    /**
     * 后端的登录/注册功能的实现
     * 当succes为true时，向前端返回success:true, object:object
     * 当success为false时，向前端返回success:false, errMsg:errMsg
     * @param user 前端传递进来的user对象，封装了username和password属性
     * @return userExecution, 包括执行成功的状态，VO；执行失败的错误码，错误信息
     */
    @RequestMapping("/registerorlogin")
    @ResponseBody //将UserExecution以json字符串的格式返回前端
    public Map<String, Object> registerOrLogin(User user) {
        Map<String, Object> modelMap = new HashMap<>();
        UserVo userVo = null;
        //非空判断, 在前端已经完成
        //从数据库中查找用户
        User userResult = userService.queryByUsername(user.getUsername());
        if (userResult != null) {
            //判断密码是否正确, 与进行MD5加密后的密码比较
            if (!MD5Utils.getPwd(user.getPassword()).equals(userResult.getPassword())) {
                //密码错误，返回错误信息
                modelMap.put("success", false);
                modelMap.put("errMsg", "用户名或密码错误");
            } else {
                //密码正确，成功登录
                userVo = new UserVo();
                BeanUtils.copyProperties(userResult, userVo);
                modelMap.put("success", true);
                modelMap.put("data", userVo);
            }
        } else {
            //userResult为空，代表数据库中没有对应的用户信息，则进行用户注册
            user.setNickname("");
            int effectNum = userService.insertUser(user);
            if (effectNum == 1) {
                //注册成功, 返回登录页面, 携带用户的非敏感信息
                userVo = new UserVo();
                userResult = userService.queryByUsername(user.getUsername());
                BeanUtils.copyProperties(userResult, userVo);
                modelMap.put("success", true);
                modelMap.put("data", userVo);
            } else {
                //数据插入数据库失败
                modelMap.put("success", false);
                modelMap.put("errMsg", "系统错误，请联系管理员");
            }
        }
        return modelMap;
    }

    /**
     * 修改昵称的方法, nickname可以重复
     */
    @PostMapping("/nicknameSetting")
    @ResponseBody
    public Map<String,Object> changeNickname(User user){
        Map<String,Object> modelMap = new HashMap<>();
        UserVo data = new UserVo();
        //前端已经对user的合法性进行了保证, 保证了nickname不为空，不超过12位
        if (user != null){
            try {
                int effectNum = userService.updateByPrimaryKeySelective(user);
                if (effectNum == 1){
                    User userResult = userService.selectUserById(user.getId());
                    BeanUtils.copyProperties(userResult,data);
                    modelMap.put("success",true);
                    modelMap.put("data",data);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg","昵称修改失败");
                }
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
            }
        }else {
            modelMap.put("success",false);
            modelMap.put("errMsg","用户信息不合法");
        }
        return modelMap;
    }


//    @RequestMapping("/uploadFaceBase64")
//    @ResponseBody
//    //用户上传头像的方法
//    public Map<String,Object> uploadFace(UserBo userBo){
//        Map<String,Object> modelMap = new HashMap<>();
//        String url = null;
//        //获取从前端传来的base64的字符串，并将其转为文件对象后再上传
//        String base64Data = userBo.getFaceData();
//        String userFacePath = "D:\\"+userBo.getUserId()+"userFaceBase64.png";
//        try{
//            //调用FileUtiles中的方法将base64字符串转为文件对象
//            FileUtils.base64ToFile(userFacePath,base64Data);
//            MultipartFile multipartFile = FileUtils.fileToMultipart(userFacePath);
//            //使用fastDFS上传图片，并获取fastDFS上传图片的路径, 此处上传的是裁剪过后的大图
//            url = fastDFSClient.uploadBase64(multipartFile); //url:http://47.108.24.240:8888/group1/M00/00/00/rBNmaWA6AIKANWilABoSAQkrh48557.png
//        }catch (Exception e){
//            modelMap.put("success",false);
//            modelMap.put("errMsg",e.toString());
//        }
//        System.out.println(url);
//        //创建缩略图的存放路径
//        //String bigFace = "xxxxxxxx147767419.png" 上传的图片路径
//        //String thumbFace = "xxxxxxxx147767419_150x150.png"
//        String thumb = "_150x150.";
//        String[] arr = url.split("\\.");// \\转义
//        String thumbImgUrl = arr[0]+thumb+arr[1];
//        //更新用户信息
//        User user = new User();
//        user.setId(userBo.getUserId());
//        user.setFaceImageBig(url);
//        user.setFaceImage(thumbImgUrl);
//        try {
//            int effectNum = userService.updateByPrimaryKeySelective(user);
//            if (effectNum == 1){
//                modelMap.put("success",true);
//                modelMap.put("data",user);
//            }else {
//                modelMap.put("success",false);
//                modelMap.put("errMsg","图片上传失败！");
//            }
//        }catch (Exception e){
//            modelMap.put("success",false);
//            modelMap.put("errMsg",e.toString());
//        }
//        return modelMap;
//    }

    /**
     * 搜索用户名并添加好友的方法
     */
    @GetMapping(value = "/searchfriend")
    @ResponseBody
    public Map<String,Object> searchFriend(String myUserid, String friendUsername){
        Map<String,Object> modelMap = new HashMap<>();
        UserVo userVo = new UserVo();
        /**
         * 前置条件
         * 1.搜索用户不存在，返回该用户不存在
         * 2.搜索账号为自身，返回不能添加自己为好友
         * 3.搜索的朋友已经是好友，返回该用户已经是你的好友
         */
       //在UserService中完成搜索好友的前置接口
        Integer result = userService.preconditionForFriendSearch(myUserid, friendUsername);
        if(result == SearchFriendEnum.SUCCESS.getState()){
            User user = userService.queryByUsername(friendUsername);
            modelMap.put("success",true);
            BeanUtils.copyProperties(user,userVo);
            modelMap.put("data",userVo);
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg",SearchFriendEnum.stateOf(result).getStateInfo());
        }
        return modelMap;
    }


    /**
     * 发送好友请求的方法
     */
    @GetMapping(value = "/addfriend")
    @ResponseBody
    public Map<String,Object> addFriend(String myUserid, String friendUsername){
        //前端页面已经是搜索过后的信息，因此能够保证发送过来的myUserid和friendUsername都是合法有效的
        Map<String,Object> modelMap = new HashMap<>();
        //查询该好友请求是否已经在数据库中，如果在库中则不能继续发送请求
        int result = friendsRequestService.queryFriendRequest(myUserid, friendUsername);
        if(result == FriendRequestEnum.ALREADY_SENT.getState()){
            modelMap.put("success",false);
            modelMap.put("errMsg","好友请求已发送，请勿重复发送");
        }else{
            result = friendsRequestService.insertFriendRequest(myUserid, friendUsername);
            if(result == 1){
                modelMap.put("success",true);
            }else{
                modelMap.put("success",false);
                modelMap.put("errMsg","好友请求发送失败");
            }
        }
        return modelMap;
    }
}
