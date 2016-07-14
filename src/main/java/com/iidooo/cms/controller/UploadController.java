package com.iidooo.cms.controller;

import java.io.File;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.aliyun.oss.OSSClient;
import com.iidooo.aliyun.util.OSSUtil;
import com.iidooo.cms.model.po.CmsSite;
import com.iidooo.cms.service.SiteService;
import com.iidooo.cms.service.UploadService;
import com.iidooo.core.enums.ResponseStatus;
import com.iidooo.core.model.ResponseResult;
import com.iidooo.core.service.HisOperatorService;
import com.iidooo.core.util.DateUtil;
import com.iidooo.core.util.FileUtil;
import com.iidooo.core.util.PictureUtil;
import com.iidooo.core.util.StringUtil;

@Controller
public class UploadController {
    private static final Logger logger = Logger.getLogger(UploadController.class);

    @Autowired
    private UploadService uploadService;

    @Autowired
    private HisOperatorService hisOperatorService;

    @Autowired
    private SiteService siteService;

    @ResponseBody
    @RequestMapping(value = { "/uploadFile", "/admin/uploadFile" }, method = RequestMethod.POST)
    public ResponseResult uploadFile(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            ServletContext sc = request.getServletContext();

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile("file");
            String siteID = multipartRequest.getParameter("siteID");

            result.checkFieldRequired("file", file);
            result.checkFieldRequired("siteID", siteID);
            result.checkFieldInteger("siteID", siteID);
            if (result.getMessages().size() > 0) {
                // 验证失败，返回message
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }

            // 保存上传的文件到服务器的既定路径下
            Properties systemProperties = (Properties) sc.getAttribute("system.properties");
            String uploadFolderPath = systemProperties.getProperty("SERVER_UPLOAD_FOLDER");
            String yearMonth = DateUtil.getNow(DateUtil.DATE_YEAR_MONTH_SIMPLE);
            uploadFolderPath = uploadFolderPath + File.separator + yearMonth;

            // 写出文件到指定路径下
            String fileName = file.getOriginalFilename();
            String suffix = FileUtil.getFileSuffix(fileName);
            String nowStr = DateUtil.getNow(DateUtil.DATE_TIME_FULL_SIMPLE);
            String uploadFilePath = FileUtil.save(file.getBytes(), uploadFolderPath, nowStr + "." + suffix);
            if (StringUtil.isBlank(uploadFilePath)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("url", "");
                result.setStatus(ResponseStatus.Failed.getCode());
                result.setData(jsonObject);
                return result;
            }

            String isCompress = multipartRequest.getParameter("isCompress");
            String width = multipartRequest.getParameter("width");
            String height = multipartRequest.getParameter("height");
            if (isCompress.equals("true")) {
                PictureUtil.compress(uploadFilePath, uploadFilePath, Integer.parseInt(width), Integer.parseInt(height), true);
            }
            // if (PictureUtil.isImage(uploadFilePath)) {
            // if (fileType.equals(FileType.UserPhoto.getCode())) {
            // PictureUtil.MaintainOrientation(uploadFilePath);
            // PictureUtil.cutSquare(uploadFilePath, uploadFilePath);
            // PictureUtil.compress(uploadFilePath, uploadFilePath, 200, 200, false);
            // } else if (fileType.equals(FileType.NewsPicture.getCode())) {
            // PictureUtil.MaintainOrientation(uploadFilePath);
            // PictureUtil.compress(uploadFilePath, uploadFilePath, 500, 500, true);
            // } else if (fileType.equals(FileType.ContentPicture.getCode())) {
            // PictureUtil.MaintainOrientation(uploadFilePath);
            // PictureUtil.compress(uploadFilePath, uploadFilePath, 1000, 1000, true);
            // }
            // }

            CmsSite site = siteService.getSite(Integer.parseInt(siteID));
            if (site == null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("url", "");
                result.setStatus(ResponseStatus.Failed.getCode());
                result.setData(jsonObject);
                return result;
            }
            
            // 把文件上传到阿里云OSS的既定路径下
            Properties aliyunProperties = (Properties) sc.getAttribute("aliyun.properties");

            OSSClient ossClient = OSSUtil.getOSSClient(aliyunProperties);
            String newKeyURL = OSSUtil.uploadFile(aliyunProperties, ossClient, site.getSiteCode() + "/" + yearMonth + "/", uploadFilePath);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", newKeyURL);

            result.setStatus(ResponseStatus.OK.getCode());
            result.setData(jsonObject);

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }
}
