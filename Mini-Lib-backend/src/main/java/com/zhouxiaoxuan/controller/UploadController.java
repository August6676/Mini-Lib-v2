package com.zhouxiaoxuan.controller;

import com.zhouxiaoxuan.utils.OSSUtil;
import com.zhouxiaoxuan.utils.ResponseResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
public class UploadController {
    @PostMapping("/upload")
    public ResponseResult<String> upload(MultipartFile file) throws IOException{
        //上传OSS
        OSSUtil.upload(file.getInputStream(),file.getOriginalFilename());
        //获得url
        String url = OSSUtil.getURL(file.getOriginalFilename());
        System.out.println(url);
        return  ResponseResult.ok(url);
    }
}