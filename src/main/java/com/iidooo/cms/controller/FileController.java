package com.iidooo.cms.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iidooo.cms.model.po.CmsFile;
import com.iidooo.cms.service.FileService;
import com.iidooo.core.constant.RegularConstant;
import com.iidooo.core.enums.ResponseStatus;
import com.iidooo.core.model.ResponseResult;
import com.iidooo.core.util.FileUtil;
import com.iidooo.core.util.StringUtil;
import com.iidooo.core.util.ValidateUtil;

@Controller
public class FileController {
    private static final Logger logger = Logger.getLogger(FileController.class);
    
    @Autowired
    private FileService fileService;
    
    @ResponseBody
    @RequestMapping(value = { "/admin/getFile" }, method = RequestMethod.POST)
    public ResponseResult getFile(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            String fileID = request.getParameter("fileID");
            result.checkFieldRequired("fileID", fileID);
            result.checkFieldInteger("fileID", fileID);
            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }

            CmsFile file = fileService.getFile(Integer.parseInt(fileID));
            if(file == null){
                result.setStatus(ResponseStatus.Failed.getCode());
            } else {
                result.setStatus(ResponseStatus.OK.getCode());
                result.setData(file);                
            }

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }
    
    @ResponseBody
    @RequestMapping(value = {"/admin/getFileList"}, method = RequestMethod.POST)
    public ResponseResult getFileList(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {            
            String contentID = request.getParameter("contentID");
            result.checkFieldRequired("contentID", contentID);
            result.checkFieldInteger("contentID", contentID);
            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }
            
            List<CmsFile> files = fileService.getFileList(Integer.parseInt(contentID));   

            // 返回找到的内容对象
            result.setStatus(ResponseStatus.OK.getCode());
            result.setData(files);
            
        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }
    
    @ResponseBody
    @RequestMapping(value = { "/admin/createFile" }, method = RequestMethod.POST)
    public ResponseResult createFile(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            String operatorID = request.getParameter("operatorID");
            String contentID = request.getParameter("contentID");
            String fileURL = request.getParameter("fileURL");
            
            result.checkFieldRequired("operatorID", operatorID);
            result.checkFieldInteger("operatorID", operatorID);
            result.checkFieldRequired("contentID", contentID);
            result.checkFieldInteger("contentID", contentID);
            result.checkFieldRequired("fileURL", fileURL);
            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }
            
            String fileName = request.getParameter("fileName");
            String fileSize = request.getParameter("fileSize");
            
            CmsFile file = new CmsFile();
            file.setContentID(Integer.parseInt(contentID));
            file.setFileURL(fileURL);
            file.setFileName(fileName);
            if (StringUtil.isNotBlank(fileSize) && ValidateUtil.isMatch(fileSize, RegularConstant.REGEX_NUMBER)) {
                file.setFileSize(Long.parseLong(fileSize));
            }
            file.setFileType(FileUtil.getFileSuffix(fileURL));
            file.setCreateTime(new Date());
            file.setCreateUserID(Integer.parseInt(operatorID));
            file.setUpdateUserID(Integer.parseInt(operatorID));
            file = fileService.createFile(file);
            if (file == null) {
                result.setStatus(ResponseStatus.Failed.getCode());
            } else {
                result.setStatus(ResponseStatus.OK.getCode());
                result.setData(file);
            }

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }
    
    @ResponseBody
    @RequestMapping(value = { "/admin/updateFile" }, method = RequestMethod.POST)
    public ResponseResult updateFile(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            String operatorID = request.getParameter("operatorID");
            String fileID = request.getParameter("fileID");
            String fileURL = request.getParameter("fileURL");
            
            result.checkFieldRequired("operatorID", operatorID);
            result.checkFieldInteger("operatorID", operatorID);
            result.checkFieldRequired("fileID", fileID);
            result.checkFieldInteger("fileID", fileID);
            result.checkFieldRequired("fileURL", fileURL);
            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }
            
            String fileName = request.getParameter("fileName");
            String fileSize = request.getParameter("fileSize");
            
            CmsFile file = new CmsFile();
            file.setFileID(Integer.parseInt(fileID));
            file.setFileURL(fileURL);
            file.setFileName(fileName);
            if (StringUtil.isNotBlank(fileSize) && ValidateUtil.isMatch(fileSize, RegularConstant.REGEX_NUMBER)) {
                file.setFileSize(Long.parseLong(fileSize));
            }
            file.setFileType(FileUtil.getFileSuffix(fileURL));
            file.setUpdateUserID(Integer.parseInt(operatorID));
            file = fileService.updateFile(file);
            if (file == null) {
                result.setStatus(ResponseStatus.Failed.getCode());
            } else {
                result.setStatus(ResponseStatus.OK.getCode());
                result.setData(file);
            }

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }
    
    @ResponseBody
    @RequestMapping(value = { "/admin/deleteFile" }, method = RequestMethod.POST)
    public ResponseResult deleteFile(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            String operatorID = request.getParameter("operatorID");
            String fileID = request.getParameter("fileID");
            
            result.checkFieldRequired("operatorID", operatorID);
            result.checkFieldInteger("operatorID", operatorID);
            result.checkFieldRequired("fileID", fileID);
            result.checkFieldInteger("fileID", fileID);
            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }
            
            CmsFile file = new CmsFile();
            file.setFileID(Integer.parseInt(fileID));
            file.setUpdateUserID(Integer.parseInt(operatorID));
            if (!fileService.deleteFile(file)) {
                result.setStatus(ResponseStatus.Failed.getCode());
            } else {
                result.setStatus(ResponseStatus.OK.getCode());
            }

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }
}
