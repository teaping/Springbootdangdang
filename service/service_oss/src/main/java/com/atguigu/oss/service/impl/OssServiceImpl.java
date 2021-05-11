package com.atguigu.oss.service.impl;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    //上传头像到oss中
    @Override
    public String uploadFileAvatar(MultipartFile file) {

        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = ConstantPropertiesUtils.END_POIND;
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;
        OSS ossClient =null;
        try {

            // 创建OSSClient实例。
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
            InputStream inputStream = file.getInputStream();
            //获取文件名称
            String fileName = file.getOriginalFilename();

            //在文件名称里面添加随机唯一的值
            String uuid  = UUID.randomUUID().toString().replaceAll("-","");
            fileName=uuid+fileName;
            // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。

            //把文件按照日期进行分类
            //获取当前日期
            String dataPath = new DateTime().toString("yyyy/MM/dd");

            fileName = dataPath+"/"+fileName;

            //三个参数
            //第一个参数 Bucket名称
            //第二参数 上传到oss文件路径何和名称
            //上传文件的输入流
            ossClient.putObject(bucketName, fileName, inputStream);

            //把上传之后的文件路径返回 手动拼接
            //https://edu-3411.oss-cn-beijing.aliyuncs.com/100.jpg
            String url="https://"+bucketName+"."+endpoint+"/"+fileName;

            return url;

        }catch (Exception e){
            return null;
        }finally {
            // 关闭OSSClient。
            ossClient.shutdown();

        }
    }
}
