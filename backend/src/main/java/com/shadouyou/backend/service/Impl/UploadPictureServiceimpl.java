package com.shadouyou.backend.service.Impl;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.shadouyou.backend.service.UploadPictureService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Random;

@Service
public class UploadPictureServiceimpl implements UploadPictureService {
    @Override
    public String uploadPicture(MultipartFile file) throws IOException {
        String qiniuUrl = "qb5brafn6.bkt.clouddn.com";
        Configuration configuration = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(configuration);
        String accessKey = "0_Zt4ujJVFDZ0tHKdCD2QQA3-hcGxgL6CidSjGWS";
        String secretKey = "CaoRJOVJNsBtgcJ8d1ExPZbpzjTcqNpDqO_bzo_K";
        String bucket = "sdyimg";
        String key = getRandomCharacterAndNumber(10) + ".jpg";//生成随机文件名
        Auth auth = Auth.create(accessKey,secretKey);
        String uptoken = auth.uploadToken(bucket);
        String imgUrl="http://";
        try{


                byte[] localFile = file.getBytes();
                Response response = uploadManager.put(localFile,key,uptoken);
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                imgUrl=imgUrl +qiniuUrl + '/' +putRet.key;

//            七牛云对象空间暂设为公有，无须下载凭证
//            //获取下载令牌
//            String fileName = key;//文件名
//            String domainOfBucket = "http://"+qiniuUrl;
//            String encodedFileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20");
//            String publicUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
//            Auth auth1 = Auth.create(accessKey, secretKey);
//            long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
//            String finalUrl = auth1.privateDownloadUrl(publicUrl, expireInSeconds);
//            System.out.println(finalUrl);//输出访问链接

        }catch (QiniuException e){
            Response r = e.response;
        }
        return imgUrl;//访问图片链接
    }


    public static String getRandomCharacterAndNumber(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字

            if ("char".equalsIgnoreCase(charOrNum)) // 字符串
            {
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写字母还是小写字母
                val += (char) (choice + random.nextInt(26));
                // int choice = 97; // 指定字符串为小写字母
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) // 数字
            {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}
