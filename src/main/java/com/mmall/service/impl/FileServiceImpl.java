package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FtpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
@Service("iFileService")
public class FileServiceImpl implements IFileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        //扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        logger.info("开始上传文件，上传的文件名为:{},上传的路径:{},新文件名:{}", fileName, path, uploadFileName);
        File fileDir = new File(path);
        if (fileDir.exists()) {
            //赋予可写权限
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile=new File(path,uploadFileName);

        try {
            file.transferTo(targetFile);
            //已经将tergetFile上传到我们的FTP服务上 Guava
            FtpUtil.uploadFile(Lists.newArrayList(targetFile));
            //上传完成之后，删除upload下面的文件
            targetFile.delete();
        } catch (IOException e) {
            logger.info("文件上传异常",e);
            return null;
        }
        return targetFile.getName();
    }
}
