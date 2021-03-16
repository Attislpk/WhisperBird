package org.wdzl.service.impl;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wdzl.dao.MyFriendsMapper;
import org.wdzl.dao.UserMapper;
import org.wdzl.entity.User;
import org.n3r.idworker.Sid;
import org.wdzl.enums.SearchFriendEnum;
import org.wdzl.service.UserService;
import org.wdzl.utils.FastDFSClient;
import org.wdzl.utils.FileUtils;
import org.wdzl.utils.MD5Utils;
import org.wdzl.utils.QRCodeUtils;

import java.io.IOException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    Sid sid;

    @Autowired
    QRCodeUtils qrCodeUtils;

    @Autowired
    FastDFSClient fastDFSClient;

    @Autowired
    MyFriendsMapper myFriendsMapper;

    @Override
    public User selectUserById(String id) {
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }

    @Override
    public User queryByUsername(String username) {
        User user = userMapper.queryByUsername(username);
        return user;
    }

    @Override
    public int insertUser(User user) {
        //设置一些默认的非空信息和id
        user.setCid(user.getCid()); //设备id
        user.setId(sid.nextShort());
        user.setNickname(user.getUsername());
        user.setPassword(MD5Utils.getPwd(user.getPassword()));
        //为每个注册用户生成一个唯一的二维码
        String qrCodePath = "E:\\JAVACODE\\projectdev\\bird"+user.getUsername()+"qrcode.png"; //二维码存储的本地路径
        qrCodeUtils.createQRCode(qrCodePath,"bird_qrcode:"+user.getUsername()); //二维码的扫描内容
        MultipartFile multipartFile = FileUtils.fileToMultipart(qrCodePath);//将该路径下的文件转换为multipart
        String qrCodeUrl = "";
        try {
            qrCodeUrl = fastDFSClient.uploadQRCode(multipartFile); //此处的qrCodeUrl是在云端上保存的绝对路径
            System.out.println(qrCodeUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //用户注册时候的默认头像是其二维码
        user.setFaceImage(qrCodeUrl);
        user.setQrcode(qrCodeUrl);
        user.setFaceImageBig("");
        return userMapper.insert(user);
    }

    @Override
    public int updateByPrimaryKeySelective(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public Integer preconditionForFriendSearch(String myUserId, String friendsUsername) {
        User user = userMapper.queryByUsername(friendsUsername);
        if(user != null){
            //用户为自身
            if(user.getId().equals(myUserId)){
                return SearchFriendEnum.YOURSELF.getState();
            }
            //用户不为空，不是自己，也存在,则判断是否已经是好友
            int result = myFriendsMapper.queryIfFriends(myUserId, user.getId());
            if ((result == 1)){
                //已经是好友
                return SearchFriendEnum.ALREADY_FRIENDS.getState();
            }else{
                //还不是好友
                return SearchFriendEnum.SUCCESS.getState();
            }
        }else{
            //用户不存在
            return SearchFriendEnum.USER_NOT_EXIST.getState();
        }
    }
}
