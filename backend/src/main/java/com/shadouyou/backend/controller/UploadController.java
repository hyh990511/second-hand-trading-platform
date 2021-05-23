package com.shadouyou.backend.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shadouyou.backend.JsonResult;
import com.shadouyou.backend.service.UploadPictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/QiNiu")
public class UploadController {
    @Autowired
    private UploadPictureService uploadPictureService;
    @PostMapping("/uploadimg")
    @CrossOrigin
    public JsonResult<String> uploadPicture(@RequestParam("file") MultipartFile file) throws IOException {

        String pictureUrl = uploadPictureService.uploadPicture(file);

        return  new JsonResult<>(pictureUrl);
    }
}

