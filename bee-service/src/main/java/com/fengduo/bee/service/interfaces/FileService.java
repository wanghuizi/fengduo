/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.service.interfaces;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import com.fengduo.bee.commons.result.Result;
import com.fengduo.bee.service.impl.file.FileServiceImpl.IFileCreate;
import com.fengduo.bee.service.impl.file.FileServiceImpl.IFileHandle;

/**
 * 文件上传接口
 * 
 * @author zxc Jun 10, 2015 5:56:35 PM
 */
public interface FileService {

    /**
     * 根据文件路径读取文件
     * 
     * @param path
     * @return
     */
    public File getFile(String path);

    /**
     * 根据ID生成临时文件路径
     * 
     * @param file
     * @param fileName
     * @return
     */
    Result createFilePath(MultipartFile file, IFileCreate... ihandle);

    /**
     * 根据文件名字,生成真正的文件路径,IFileHandle特定的文件路径生成策略
     * 
     * @param fileName
     * @return
     */
    Result saveFileByName(String fileName, IFileHandle... ihandle);

    /**
     * 删除 真正的生成文件路径
     * 
     * @param fileName
     * @return
     */
    Result delFileByPath(String filePath);
}
