package com.mmall.service.impl;

import com.mmall.service.IFileService;
import org.springframework.web.multipart.MultipartFile;

public class FileServiceImpl implements IFileService {
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        //扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf("."));
        return null;
    }
}
