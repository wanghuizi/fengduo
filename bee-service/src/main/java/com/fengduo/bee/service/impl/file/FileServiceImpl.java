/*
 * Copyright 2015-2020 Fengduo.co All right reserved. This software is the confidential and proprietary information of
 * Fengduo.co ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Fengduo.co.
 */
package com.fengduo.bee.service.impl.file;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fengduo.bee.commons.core.lang.Argument;
import com.fengduo.bee.commons.core.utils.Identities;
import com.fengduo.bee.commons.result.Result;
import com.fengduo.bee.commons.util.DateViewTools;
import com.fengduo.bee.service.interfaces.FileService;

/**
 * 文件上传服务实现
 * 
 * @author zxc Jun 10, 2015 5:56:10 PM
 */
@Service
public class FileServiceImpl implements FileService {

    private static String DELIMITER = ".";

    public static Logger  logger    = LoggerFactory.getLogger(FileService.class);

    @Value("${image.root.tmpDir}")
    private String        UPLOAD_TMP_PATH;                                       // 物理路径 /data/static/style/img/
    @Value("${image.prefix.tmpDir}")
    private String        STATIC_TMP_IMG;                                        // 相对路径 /img/

    public interface IFileHandle {

        // 自定义初始化图片路径
        public String parse(String prefix, String suffix);
    }

    public interface IFileCreate {

        // 自定义初始化图片路径
        public String build(String prefix, String suffix);
    }

    public File getFile(String path) {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (StringUtils.contains(path, STATIC_TMP_IMG)) {
            path = StringUtils.replace(path, STATIC_TMP_IMG, StringUtils.EMPTY);
        }
        File file = new File(UPLOAD_TMP_PATH + path);
        if (file == null || !file.isFile()) {
            return null;
        }
        if (file.getParentFile() == null || !file.getParentFile().exists()) {
            return null;
        }
        return file;
    }

    public Result createFilePath(MultipartFile file, IFileCreate... ihandle) {
        // 判断文件是否为空
        if (file == null) {
            return Result.failed();
        }
        if (!file.isEmpty()) {
            if (StringUtils.isEmpty(file.getOriginalFilename())) {
                return Result.failed();
            }
            String[] suffixArray = StringUtils.split(file.getOriginalFilename(), DELIMITER);
            if (Argument.isEmptyArray(suffixArray)) {
                return Result.failed();
            }
            String prefix = null;
            if (Argument.isEmptyArray(ihandle)) {
                prefix = Identities.uuid2();
            } else {
                prefix = ihandle[0].build(Identities.uuid2(), StringUtils.EMPTY);
            }
            String suffix = null;
            if (suffixArray.length == 1) {
                suffix = "jpg";
            } else {
                suffix = suffixArray[1];
            }
            String filePath = prefix + DELIMITER + suffix;
            // String filePath48 = prefix + "=48x48" + DELIMITER + suffix;
            try {
                File targetFile = new File(UPLOAD_TMP_PATH + filePath);
                if (targetFile != null && targetFile.getParentFile() != null && !targetFile.getParentFile().exists()) {
                    logger.error("文件路径不存在,正在创建!");
                    targetFile.getParentFile().mkdirs();
                }
                // 转存文件
                file.transferTo(new File(UPLOAD_TMP_PATH + filePath));
                // FileUtils.copyFile(new File(UPLOAD_TMP_PATH + filePath), new
                // File(UPLOAD_TMP_PATH + filePath48));
                return Result.success(null, STATIC_TMP_IMG + filePath);
            } catch (Exception e) {
                logger.error("文件上传错误:" + e.getMessage());
                return Result.failed("文件上传错误:" + e.getMessage());
            }
        }
        return Result.failed();
    }

    @Override
    public Result saveFileByName(String fileName, IFileHandle... ihandle) {
        if (StringUtils.isEmpty(fileName)) {
            return Result.failed();
        }
        File file = new File(UPLOAD_TMP_PATH + fileName);
        if (file == null || file.isFile()) {
            try {
                String path = ihandle[0].parse(UPLOAD_TMP_PATH, fileName);
                FileUtils.copyFile(file, new File(path));
                return Result.success(null, UPLOAD_TMP_PATH + fileName);
            } catch (Exception e) {
                logger.error(e.getMessage());
                return Result.failed();
            }
        }
        return Result.failed();
    }

    @Override
    public Result delFileByPath(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return Result.failed();
        }
        filePath = StringUtils.replace(filePath, STATIC_TMP_IMG, StringUtils.EMPTY);
        String tmpPath = UPLOAD_TMP_PATH.replaceAll(STATIC_TMP_IMG, "");
        File file = new File(tmpPath + filePath);
        if (file == null || file.isFile()) {
            FileUtils.deleteQuietly(file);
            return Result.success("删除成功", null);
        }
        return Result.failed();
    }

    /**
     * 从前段得到的文件流到图片存放路径
     * 
     * @param inputStream
     * @return
     * @throws IOException
     */
    public void inputStreamToFile(InputStream inputStream, String savePath) throws Exception {
        if (inputStream == null) {
            return;
        }
        int index = savePath.lastIndexOf(File.separator);
        String dirString = savePath.substring(0, index);
        FileOutputStream out = null;
        try {
            File dir = new File(dirString);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File outFile = new File(savePath);
            if (!outFile.exists()) {
                outFile.createNewFile();
            }
            out = new FileOutputStream(outFile);
            int c;
            byte buffer[] = new byte[1024];
            while ((c = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, c);
            }
        } catch (Exception e) {
            logger.error("图片转换错误:" + savePath + ":", e);
            throw e;
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 获得文件列表
     * 
     * @return
     * @throws Exception
     */
    public List<File> getFileList(String path) throws Exception {
        List<File> list = new ArrayList<File>();
        File[] filearr = null;

        File file = new File(path);
        if (file.exists()) {
            filearr = file.listFiles();
            for (int i = 0; i < filearr.length; i++) {
                if (filearr[i].isDirectory()) {
                    continue;
                } else {
                    list.add(filearr[i]);
                }
            }
            return list;
        } else return null;
    }

    /**
     * 生成图片文件名
     * 
     * @param userType
     * @param orderType
     * @return
     */
    public String generateFileName() {
        String date = DateViewTools.getCurrentDateYYMMDD();
        String hmssss = DateViewTools.getCurrentHMSSSS();
        Random random = new Random();
        int hzrandom = random.nextInt(10);
        String name = date + hmssss + hzrandom;
        return name;
    }

    /**
     * 下载文件到本地
     * 
     * @param urlString 被下载的文件地址
     * @param filename 本地文件名
     * @throws Exception 各种异常
     */
    public String download(String urlString, String localPath) throws Exception {
        String filename = StringUtils.EMPTY;
        if (urlString.lastIndexOf(DELIMITER) > -1) {
            filename = generateFileName() + urlString.substring(urlString.lastIndexOf("."), urlString.length());
        } else {
            throw new Exception("远程图片路径有异常！");
        }
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        // 输入流
        InputStream is = con.getInputStream();
        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        OutputStream os = new FileOutputStream(localPath + filename);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
        return filename;
    }

    /**
     * 生成图片缩略
     * 
     * @param source
     * @param targetW
     * @param targetH
     * @param ifScaling 是否等比缩放
     * @return
     */
    public static BufferedImage resize(BufferedImage source, int targetW, int targetH, boolean ifScaling) {
        // targetW，targetH分别表示目标长和宽
        int type = source.getType();
        BufferedImage target = null;
        double sx = (double) targetW / source.getWidth();
        double sy = (double) targetH / source.getHeight();
        // 这里想实现在targetW，targetH范围内实现等比缩放。如果不需要等比缩放,则将下面的if else语句注释即可
        if (ifScaling) {
            if (sx > sy) {
                sx = sy;
                targetW = (int) (sx * source.getWidth());
            } else {
                sy = sx;
                targetH = (int) (sy * source.getHeight());
            }
        }
        if (type == BufferedImage.TYPE_CUSTOM) { // handmade
            ColorModel cm = source.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(targetW, targetH);
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();
            target = new BufferedImage(cm, raster, alphaPremultiplied, null);
        } else target = new BufferedImage(targetW, targetH, type);
        Graphics2D g = target.createGraphics();
        // smoother than exlax:
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();
        return target;
    }

    public static void saveImageAsJpg(String fromFileStr, String saveToFileStr, int width, int hight, boolean isCut)
                                                                                                                    throws Exception {
        try {
            BufferedImage srcImage;
            // String ex =
            // fromFileStr.substring(fromFileStr.indexOf("."),fromFileStr.length());
            String imgType = "JPEG";
            if (fromFileStr.toLowerCase().endsWith(".png")) {
                imgType = "PNG";
            }
            File saveFile = new File(saveToFileStr);
            File fromFile = new File(fromFileStr);
            // 创建文件夹
            if (!saveFile.exists()) {
                saveFile.mkdirs();
            }
            if (!saveFile.exists()) {
                try {
                    saveFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            srcImage = ImageIO.read(fromFile);
            if (isCut) {
                if (width > 0 || hight > 0) {
                    srcImage = resize(srcImage, width, hight, false);
                }
            }
            ImageIO.write(srcImage, imgType, saveFile);
        } catch (IIOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
}
