/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.web.controller.upload;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fengduo.bee.commons.cons.ResultCode;
import com.fengduo.bee.commons.result.Result;
import com.fengduo.bee.service.constants.PicsInfoEnum;
import com.fengduo.bee.service.impl.file.FileServiceImpl.IFileCreate;
import com.fengduo.bee.service.interfaces.FileService;
import com.fengduo.bee.web.controller.BaseController;

/**
 * 图片上传,文件上传等操作
 * 
 * @author zxc Jun 10, 2015 5:53:40 PM
 */
@Controller
public class FileUploadController extends BaseController {

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/ajaxUpload")
    public ModelAndView ajaxUpload(@RequestParam("upload")
    MultipartFile... files) {
        if (files == null || files.length <= 0) {
            return createFileJsonMav(ResultCode.ERROR, "上传失败", null);
        }
        List<String> urlList = new ArrayList<String>();
        for (int i = 0; i < files.length; i++) {
            Result result = fileService.createFilePath(files[i]);
            if (result == null || result.getData() == null) {
                return createFileJsonMav(ResultCode.ERROR, "上传失败", null);
            }
            urlList.add((String) result.getData());
        }

        return createFileJsonMav(ResultCode.SUCCESS, "上传成功", urlList.get(0));
    }

    @RequestMapping(value = "/Upload", headers = "accept=*/*", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public DeferredResult<String> Upload(MultipartFile upload) {
        DeferredResult<String> deferredResult = new DeferredResult<String>();

        long size = upload.getSize();
        if (size == 0) {
            deferredResult.setResult("-1");
            return deferredResult;
        }
        PicsInfoEnum picInfoEnum = PicsInfoEnum.AVATAR_IMG;
        if (picInfoEnum == null) {
            deferredResult.setResult("-1");
            return deferredResult;
        }
        int maxSize = picInfoEnum.getMaxSize();
        if (size > maxSize) {
            deferredResult.setResult("-1");
            return deferredResult;
        }
        // 验证后缀合法性
        String suffix = getSuffix(upload.getOriginalFilename());
        boolean isLegal = checkSuffixLegal(picInfoEnum.getSuffixs(), suffix);
        if (!isLegal) {
            deferredResult.setResult("-1");
            return deferredResult;
        }
        long userId = getCurrentUserId();
        String relativeUrl = null;
        relativeUrl = picInfoEnum.getDirPrefix() + "/" + userId + "/";
        final String _filePath = relativeUrl;
        Result result = fileService.createFilePath(upload, new IFileCreate() {

            public String build(String filePath, String suffix) {
                return _filePath + filePath + suffix;
            }
        });
        if (result.isSuccess()) {
            deferredResult.setResult("http://fengduo.co" + result.getData().toString());
            return deferredResult;
        } else {
            deferredResult.setResult("IOError");
            return deferredResult;
        }
    }

    /**
     * 图片上传
     */
    @RequestMapping(value = "/upload")
    public ModelAndView upload(MultipartFile upload, String type) {
        long size = upload.getSize();
        if (size == 0) {
            return createFileJsonMav(ResultCode.ERROR, "没有上传的图片", null);
        }
        if (StringUtils.isEmpty(type)) {
            return createFileJsonMav(ResultCode.ERROR, "请求参数无效", null);
        }
        PicsInfoEnum picInfoEnum = PicsInfoEnum.getEnum(type);
        if (picInfoEnum == null) {
            return createFileJsonMav(ResultCode.ERROR, "请求参数无效", null);
        }
        int maxSize = picInfoEnum.getMaxSize();
        if (size > maxSize) {
            return createFileJsonMav(ResultCode.ERROR, "图片过大，最大不能超过" + maxSize / (1024 * 1024) + "MB", null);
        }
        // 验证后缀合法性
        String suffix = getSuffix(upload.getOriginalFilename());
        boolean isLegal = checkSuffixLegal(picInfoEnum.getSuffixs(), suffix);
        if (!isLegal) {
            return createFileJsonMav(ResultCode.ERROR, "上传的图片后缀不合法", null);
        }
        long userId = getCurrentUserId();
        String relativeUrl = null;
        relativeUrl = picInfoEnum.getDirPrefix() + "/" + userId + "/";
        final String _filePath = relativeUrl;
        Result result = fileService.createFilePath(upload, new IFileCreate() {

            public String build(String filePath, String suffix) {
                return _filePath + filePath + suffix;
            }
        });
        if (result.isSuccess()) {
            return createFileJsonMav(ResultCode.SUCCESS, "上传成功", result.getData().toString());
        } else {
            String msg = "上传出错!";
            if (result.getData() != null) {
                msg = result.getData().toString();
            }
            return createFileJsonMav(ResultCode.ERROR, msg, null);
        }
    }

    /**
     * 获得图片的后缀
     */
    private static String getSuffix(String picName) {
        if (StringUtils.isEmpty(picName)) {
            return "";
        }
        int index = picName.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        return picName.substring(index + 1, picName.length());
    }

    /**
     * 检查后缀合法性
     */
    private static boolean checkSuffixLegal(String[] srcSuffixs, String tarSuffix) {
        if (StringUtils.isEmpty(tarSuffix)) {
            return true;
        }
        for (String str : srcSuffixs) {
            if (tarSuffix.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }
}
