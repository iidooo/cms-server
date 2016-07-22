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

import com.iidooo.cms.model.po.CmsPicture;
import com.iidooo.cms.service.PictureService;
import com.iidooo.core.enums.ResponseStatus;
import com.iidooo.core.model.ResponseResult;

@Controller
public class PictureController {
    private static final Logger logger = Logger.getLogger(PictureController.class);

    @Autowired
    private PictureService pictureService;

    @ResponseBody
    @RequestMapping(value = { "/admin/getPicture" }, method = RequestMethod.POST)
    public ResponseResult getPicture(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            String pictureID = request.getParameter("pictureID");
            result.checkFieldRequired("pictureID", pictureID);
            result.checkFieldInteger("pictureID", pictureID);
            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }

            CmsPicture picture = pictureService.getPicture(Integer.parseInt(pictureID));
            if(picture == null){
                result.setStatus(ResponseStatus.Failed.getCode());
            } else {
                result.setStatus(ResponseStatus.OK.getCode());
                result.setData(picture);                
            }

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }
    
    @ResponseBody
    @RequestMapping(value = { "/admin/getPictureList" }, method = RequestMethod.POST)
    public ResponseResult getPictureList(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            String contentID = request.getParameter("contentID");
            result.checkFieldRequired("contentID", contentID);
            result.checkFieldInteger("contentID", contentID);
            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }

            List<CmsPicture> pictures = pictureService.getPictureList(Integer.parseInt(contentID));

            // 返回找到的内容对象
            result.setStatus(ResponseStatus.OK.getCode());
            result.setData(pictures);

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = { "/admin/createPicture" }, method = RequestMethod.POST)
    public ResponseResult createPicture(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            String userID = request.getParameter("userID");
            String contentID = request.getParameter("contentID");
            String pictureURL = request.getParameter("pictureURL");
            
            result.checkFieldRequired("userID", userID);
            result.checkFieldInteger("userID", userID);
            result.checkFieldRequired("contentID", contentID);
            result.checkFieldInteger("contentID", contentID);
            result.checkFieldRequired("pictureURL", pictureURL);
            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }
            
            String pictureName = request.getParameter("pictureName");
            String href = request.getParameter("href");
            String description = request.getParameter("description");
            
            CmsPicture picture = new CmsPicture();
            picture.setContentID(Integer.parseInt(contentID));
            picture.setPictureURL(pictureURL);
            picture.setPictureName(pictureName);
            picture.setHref(href);
            picture.setDescription(description);
            picture.setCreateTime(new Date());
            picture.setCreateUserID(Integer.parseInt(userID));
            picture.setUpdateUserID(Integer.parseInt(userID));
            picture = pictureService.createPicture(picture);
            if (picture == null) {
                result.setStatus(ResponseStatus.Failed.getCode());
            } else {

                // 返回找到的内容对象
                result.setStatus(ResponseStatus.OK.getCode());
                result.setData(picture);
            }

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }
    
    @ResponseBody
    @RequestMapping(value = { "/admin/updatePicture" }, method = RequestMethod.POST)
    public ResponseResult updatePicture(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            String userID = request.getParameter("userID");
            String pictureID = request.getParameter("pictureID");
            String pictureURL = request.getParameter("pictureURL");
            
            result.checkFieldRequired("userID", userID);
            result.checkFieldInteger("userID", userID);
            result.checkFieldRequired("pictureID", pictureID);
            result.checkFieldInteger("pictureID", pictureID);
            result.checkFieldRequired("pictureURL", pictureURL);
            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }
            
            String pictureName = request.getParameter("pictureName");
            String href = request.getParameter("href");
            String description = request.getParameter("description");
            
            CmsPicture picture = new CmsPicture();
            picture.setPictureID(Integer.parseInt(pictureID));
            picture.setPictureURL(pictureURL);
            picture.setPictureName(pictureName);
            picture.setHref(href);
            picture.setDescription(description);
            picture.setUpdateUserID(Integer.parseInt(userID));
            picture = pictureService.updatePicture(picture);
            if (picture == null) {
                result.setStatus(ResponseStatus.Failed.getCode());
            } else {
                result.setStatus(ResponseStatus.OK.getCode());
                result.setData(picture);
            }

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }
    
    @ResponseBody
    @RequestMapping(value = { "/admin/deletePicture" }, method = RequestMethod.POST)
    public ResponseResult deletePicture(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            String userID = request.getParameter("userID");
            String pictureID = request.getParameter("pictureID");
            
            result.checkFieldRequired("userID", userID);
            result.checkFieldInteger("userID", userID);
            result.checkFieldRequired("pictureID", pictureID);
            result.checkFieldInteger("pictureID", pictureID);
            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }
            
            CmsPicture picture = new CmsPicture();
            picture.setPictureID(Integer.parseInt(pictureID));
            picture.setUpdateUserID(Integer.parseInt(userID));
            if (!pictureService.deletePicture(picture)) {
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
