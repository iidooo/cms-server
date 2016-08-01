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

import com.iidooo.cms.model.po.CmsChannel;
import com.iidooo.cms.service.ChannelService;
import com.iidooo.cms.util.ChannelUtil;
import com.iidooo.core.constant.RegularConstant;
import com.iidooo.core.enums.ResponseStatus;
import com.iidooo.core.model.ResponseResult;
import com.iidooo.core.util.ValidateUtil;

@Controller
public class ChannelController {
    private static final Logger logger = Logger.getLogger(ChannelController.class);

    @Autowired
    private ChannelService channelService;

    @ResponseBody
    @RequestMapping(value = { "/admin/getChannel" }, method = RequestMethod.POST)
    public ResponseResult getChannel(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            String channelID = request.getParameter("channelID");
            result.checkFieldRequired("channelID", channelID);
            result.checkFieldInteger("channelID", channelID);

            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }

            CmsChannel channel = channelService.getChannel(Integer.parseInt(channelID));
            if (channel == null) {
                result.setStatus(ResponseStatus.Failed.getCode());
            } else {
                result.setStatus(ResponseStatus.OK.getCode());
                result.setData(channel);
            }

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }
    
    @ResponseBody
    @RequestMapping(value = { "/admin/createChannel" }, method = RequestMethod.POST)
    public ResponseResult createChannel(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            String userID = request.getParameter("userID");
            String siteID = request.getParameter("siteID");
            String channelName = request.getParameter("channelName");
            String channelPath = request.getParameter("channelPath");
            result.checkFieldRequired("userID", userID);
            result.checkFieldInteger("userID", userID);
            result.checkFieldRequired("siteID", siteID);
            result.checkFieldInteger("siteID", siteID);
            result.checkFieldRequired("channelName", channelName);
            result.checkFieldRequired("channelPath", channelPath);

            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }

            String parentID = request.getParameter("parentID");
            String metaTitle = request.getParameter("metaTitle");
            String metaKeywords = request.getParameter("metaKeywords");
            String metaDescription = request.getParameter("metaDescription");
            String remarks = request.getParameter("remarks");

            CmsChannel channel = new CmsChannel();
            channel.setSiteID(Integer.parseInt(siteID));
            channel.setChannelName(channelName);
            channel.setChannelPath(channelPath);
            if (ValidateUtil.isMatch(parentID, RegularConstant.REGEX_NUMBER)) {
                channel.setParentID(Integer.parseInt(parentID));
            }
            channel.setMetaTitle(metaTitle);
            channel.setMetaKeywords(metaKeywords);
            channel.setMetaDescription(metaDescription);
            channel.setRemarks(remarks);
            channel.setCreateTime(new Date());
            channel.setCreateUserID(Integer.parseInt(userID));
            channel.setUpdateUserID(Integer.parseInt(userID));
            
            // 检查channel path 是否重复
            if(channelService.getChannelByPath(channel) != null){
                result.checkFieldUnique("channelPath", channelPath);
                result.setStatus(ResponseStatus.DuplicateFailed.getCode());
                return result;
            }
            
            channel = channelService.createChannel(channel);
            if (channel == null) {
                result.setStatus(ResponseStatus.InsertFailed.getCode());
            } else {
                result.setStatus(ResponseStatus.OK.getCode());
                result.setData(channel);
            }
        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }
    
    @ResponseBody
    @RequestMapping(value = { "/admin/updateChannel" }, method = RequestMethod.POST)
    public ResponseResult updateChannel(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            String userID = request.getParameter("userID");
            String siteID = request.getParameter("siteID");
            String channelID = request.getParameter("channelID");
            String channelName = request.getParameter("channelName");
            String channelPath = request.getParameter("channelPath");
            result.checkFieldRequired("userID", userID);
            result.checkFieldInteger("userID", userID);
            result.checkFieldRequired("siteID", siteID);
            result.checkFieldInteger("siteID", siteID);
            result.checkFieldRequired("channelID", channelID);
            result.checkFieldInteger("channelID", channelID);
            result.checkFieldRequired("channelName", channelName);
            result.checkFieldRequired("channelPath", channelPath);

            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }

            String parentID = request.getParameter("parentID");
            String metaTitle = request.getParameter("metaTitle");
            String metaKeywords = request.getParameter("metaKeywords");
            String metaDescription = request.getParameter("metaDescription");
            String remarks = request.getParameter("remarks");

            CmsChannel channel = new CmsChannel();
            channel.setSiteID(Integer.parseInt(siteID));
            channel.setChannelID(Integer.parseInt(channelID));
            channel.setChannelName(channelName);
            channel.setChannelPath(channelPath);
            if (ValidateUtil.isMatch(parentID, RegularConstant.REGEX_NUMBER)) {
                channel.setParentID(Integer.parseInt(parentID));
            }
            channel.setMetaTitle(metaTitle);
            channel.setMetaKeywords(metaKeywords);
            channel.setMetaDescription(metaDescription);
            channel.setRemarks(remarks);
            channel.setUpdateUserID(Integer.parseInt(userID));
            
            // 检查channel path是否重复
            CmsChannel existChannel = channelService.getChannelByPath(channel);
            if(existChannel != null && !existChannel.getChannelID().equals(channel.getChannelID())){
                result.checkFieldUnique("channelPath", channelPath);
                result.setStatus(ResponseStatus.DuplicateFailed.getCode());
                return result;
            }
            
            channel = channelService.updateChannel(channel);
            if (channel == null) {
                result.setStatus(ResponseStatus.UpdateFailed.getCode());
            } else {
                result.setStatus(ResponseStatus.OK.getCode());
                result.setData(channel);
            }

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = { "/getChannelList", "/admin/getChannelList" }, method = RequestMethod.POST)
    public ResponseResult getChannelList(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {

            String siteCode = request.getParameter("siteCode");
            result.checkFieldRequired("siteCode", siteCode);

            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }

            List<CmsChannel> channels = channelService.getChannelList(siteCode);

            // 返回找到的内容对象
            result.setStatus(ResponseStatus.OK.getCode());
            result.setData(channels);

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = { "/admin/getChannelTree" }, method = RequestMethod.POST)
    public ResponseResult getChannelTree(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {

            String siteID = request.getParameter("siteID");
            result.checkFieldRequired("siteID", siteID);
            result.checkFieldInteger("siteID", siteID);

            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }

            List<CmsChannel> channels = channelService.getChannelList(Integer.parseInt(siteID));

            // 返回找到的内容对象
            result.setStatus(ResponseStatus.OK.getCode());
            result.setData(ChannelUtil.constructChannelTree(channels));

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }
    
    @ResponseBody
    @RequestMapping(value = { "/admin/deleteChannel" }, method = RequestMethod.POST)
    public ResponseResult deleteChannel(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            String userID = request.getParameter("userID");
            String channelID = request.getParameter("channelID");
            result.checkFieldRequired("userID", userID);
            result.checkFieldInteger("userID", userID);
            result.checkFieldRequired("channelID", channelID);
            result.checkFieldInteger("channelID", channelID);

            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }

            CmsChannel channel = new CmsChannel();
            channel.setChannelID(Integer.parseInt(channelID));
            channel.setUpdateUserID(Integer.parseInt(userID));
            if(channelService.deleteChannel(channel)){
                result.setStatus(ResponseStatus.OK.getCode());
            } else {
                result.setStatus(ResponseStatus.Failed.getCode());
            }

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }
}
