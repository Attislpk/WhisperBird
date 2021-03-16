package org.wdzl.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wdzl.entity.FriendsRequest;
import org.wdzl.entity.User;
import org.wdzl.enums.FriendRequestEnum;
import org.wdzl.enums.SearchFriendEnum;
import org.wdzl.enums.RequestOperateTypeEnum;
import org.wdzl.service.FriendsRequestService;
import org.wdzl.service.MyFriendsService;
import org.wdzl.service.UserService;
import org.wdzl.vo.FriendRequestVo;
import org.wdzl.vo.MyFriendVo;
import org.wdzl.vo.UserVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/friend")
public class FriendsController {

    @Autowired
    UserService userService;

    @Autowired
    FriendsRequestService friendsRequestService;

    @Autowired
    MyFriendsService myFriendsService;

    /**
     * 搜索用户名并添加好友的方法
     */
    @GetMapping(value = "/searchfriend")
    @ResponseBody
    public Map<String, Object> searchFriend(String myUserid, String friendUsername) {
        Map<String, Object> modelMap = new HashMap<>();
        UserVo userVo = new UserVo();
        /**
         * 前置条件
         * 1.搜索用户不存在，返回该用户不存在
         * 2.搜索账号为自身，返回不能添加自己为好友
         * 3.搜索的朋友已经是好友，返回该用户已经是你的好友
         */
        //在UserService中完成搜索好友的前置接口
        Integer result = userService.preconditionForFriendSearch(myUserid, friendUsername);
        if (result == SearchFriendEnum.SUCCESS.getState()) {
            User user = userService.queryByUsername(friendUsername);
            modelMap.put("success", true);
            BeanUtils.copyProperties(user, userVo);
            modelMap.put("data", userVo);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", SearchFriendEnum.stateOf(result).getStateInfo());
        }
        return modelMap;
    }


    /**
     * 发送好友请求的方法
     */
    @GetMapping(value = "/addfriend")
    @ResponseBody
    public Map<String, Object> addFriend(String myUserid, String friendUsername) {
        //前端页面已经是搜索过后的信息，因此能够保证发送过来的myUserid和friendUsername都是合法有效的
        Map<String, Object> modelMap = new HashMap<>();
        //查询该好友请求是否已经在数据库中，如果在库中则不能继续发送请求
        int result = friendsRequestService.queryFriendRequest(myUserid, friendUsername);
        if (result == FriendRequestEnum.ALREADY_SENT.getState()) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "好友请求已发送，请勿重复发送");
        } else {
            result = friendsRequestService.insertFriendRequest(myUserid, friendUsername);
            if (result == 1) {
                modelMap.put("success", true);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "好友请求发送失败");
            }
        }
        return modelMap;
    }

    /**
     * 向前端返回某用户好友申请列表的方法, acceptUserId直接从前端的全局用户中获取的userId
     */
    @PostMapping(value = "/friendRequestList")
    @ResponseBody
    public Map<String, Object> getFriendRequestList(String acceptUserId) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            List<FriendRequestVo> friendRequestVoList = friendsRequestService.queryFriendsList(acceptUserId);
            //是否有好友添加的请求，交给前端进行判断
            modelMap.put("success", true);
            modelMap.put("data", friendRequestVoList);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
        }
        return modelMap;
    }

    /**
     * 对好友请求进行处理的方法
     */
    @PostMapping(value = "/operatefriendrequest")
    @ResponseBody
    @Transactional //删除好友请求表和添加好友关系表必须同时成功or失败  事务要求
    public Map<String, Object> operateFriendRequest(String senderId, String accepterId, int operateType) {
        Map<String, Object> modelMap = new HashMap<>();

        FriendsRequest friendsRequest = new FriendsRequest();
        friendsRequest.setSendUserId(senderId);
        friendsRequest.setAcceptUserId(accepterId);
        try {
            //对操作类型进行判断
            if (operateType == RequestOperateTypeEnum.IGNORE.getState()) {
                //如果是忽略好友请求，则直接删除掉好友请求表中对应的数据即可
                friendsRequestService.deleteFriendRequst(friendsRequest);
            } else if (operateType == RequestOperateTypeEnum.ACCEPT.getState()) {
                //如果是通过好友请求，则需要删除好友请求表中的数据，还需要添加好友数据到friends表中
                friendsRequestService.deleteFriendRequst(friendsRequest);
                //添加好友数据到my_friends表中
                myFriendsService.acceptFriendRequest(senderId, accepterId);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "请求类型不合法");
                return modelMap;
            }
            //在忽略好友请求或者接受好友请求之后重写获取最新的好友列表
            List<MyFriendVo> friendVoList = myFriendsService.queryFriendsList(accepterId);
            modelMap.put("success", true);
            modelMap.put("data", friendVoList);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
        }
        return modelMap;
    }


    /**
     * 获取后端好友列表的方法
     *
     * @param myUserId 用户id
     * @return
     */
    @GetMapping(value = "/friendlist")
    @ResponseBody
    public Map<String, Object> operateFriendRequest(String myUserId) {
        Map<String, Object> modelMap = new HashMap<>();
        try {
            List<MyFriendVo> myFriendVoList = myFriendsService.queryFriendsList(myUserId);
            modelMap.put("success",true);
            modelMap.put("data",myFriendVoList);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
        }
        return modelMap;
    }
}
