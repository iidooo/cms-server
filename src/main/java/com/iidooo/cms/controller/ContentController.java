package com.iidooo.cms.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.iidooo.cms.enums.ContentType;
import com.iidooo.cms.enums.TableName;
import com.iidooo.cms.model.po.CmsContent;
import com.iidooo.cms.model.po.CmsContentNews;
import com.iidooo.cms.model.po.CmsPicture;
import com.iidooo.cms.model.vo.SearchCondition;
import com.iidooo.cms.service.ContentService;
import com.iidooo.cms.service.FavoriteService;
import com.iidooo.core.constant.RegularConstant;
import com.iidooo.core.enums.MessageLevel;
import com.iidooo.core.enums.MessageType;
import com.iidooo.core.enums.ResponseStatus;
import com.iidooo.core.model.Message;
import com.iidooo.core.model.Page;
import com.iidooo.core.model.ResponseResult;
import com.iidooo.core.service.DictItemService;
import com.iidooo.core.service.HisOperatorService;
import com.iidooo.core.service.SecurityUserService;
import com.iidooo.core.util.PageUtil;
import com.iidooo.core.util.StringUtil;
import com.iidooo.core.util.ValidateUtil;
import com.iidooo.weixin.entity.Signature;
import com.iidooo.weixin.thread.WeixinThread;
import com.iidooo.weixin.util.WeixinUtil;

@Controller
public class ContentController {

    private static final Logger logger = Logger.getLogger(ContentController.class);

    @Autowired
    private ContentService contentService;

    @Autowired
    private HisOperatorService hisOperatorService;

    @Autowired
    private DictItemService dictItemService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private SecurityUserService sercurityUserService;

    @ResponseBody
    @RequestMapping(value = "/admin/searchContents", method = RequestMethod.POST)
    public ResponseResult searchContents(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            String siteID = request.getParameter("siteID");
            result.checkFieldRequired("siteID", siteID);
            result.checkFieldInteger("siteID", siteID);
            if (result.getMessages().size() > 0) {
                // 验证失败，返回message
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }

            String channelID = request.getParameter("channelID");
            String contentTitle = request.getParameter("contentTitle");
            String contentType = request.getParameter("contentType");
            String startDateTime = request.getParameter("startDateTime");
            String endDateTime = request.getParameter("endDateTime");
            String contentStatus = request.getParameter("contentStatus");
            String createUserID = request.getParameter("createUserID");

            SearchCondition condition = new SearchCondition();
            condition.setSiteID(Integer.parseInt(siteID));

            if (StringUtil.isNotBlank(channelID) && ValidateUtil.isMatch(channelID, RegularConstant.REGEX_NUMBER)) {
                condition.setChannelID(Integer.parseInt(channelID));
            }
            condition.setContentTitle(contentTitle);
            condition.setContentType(contentType);
            condition.setStartDateTime(startDateTime);
            condition.setEndDateTime(endDateTime);
            condition.setContentStatus(contentStatus);

            if (StringUtil.isNotBlank(createUserID) && ValidateUtil.isMatch(createUserID, RegularConstant.REGEX_NUMBER)) {
                condition.setCreateUserID(Integer.parseInt(createUserID));
            }
            int recordSum = contentService.getContentsCount(condition);

            Page page = new Page();
            String sortField = request.getParameter("sortField");
            if (StringUtil.isNotBlank(sortField)) {
                page.setSortField(sortField);
            }
            String sortType = request.getParameter("sortType");
            if (StringUtil.isNotBlank(sortType)) {
                page.setSortType(sortType);
            }
            String pageSize = request.getParameter("pageSize");
            if (StringUtil.isNotBlank(pageSize) && ValidateUtil.isMatch(pageSize, RegularConstant.REGEX_NUMBER)) {
                page.setPageSize(Integer.parseInt(pageSize));
            }
            String currentPage = request.getParameter("currentPage");
            if (StringUtil.isNotBlank(currentPage) && ValidateUtil.isMatch(currentPage, RegularConstant.REGEX_NUMBER)
                    && Integer.parseInt(currentPage) > 0) {
                page.setCurrentPage(Integer.parseInt(currentPage));
            }
            page = PageUtil.executePage(recordSum, page);

            Map<String, Object> data = new HashMap<String, Object>();
            List<CmsContent> contents = contentService.getContents(condition, page);
            data.put("page", page);
            data.put("contents", contents);
            // 返回找到的内容对象
            result.setStatus(ResponseStatus.OK.getCode());
            result.setData(data);

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/admin/deleteContent", method = RequestMethod.POST)
    public ResponseResult deleteContent(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            String contentIDStr = request.getParameter("contentID");
            String userIDStr = request.getParameter("userID");

            if (StringUtil.isBlank(contentIDStr)) {
                Message message = new Message(MessageType.FieldRequired.getCode(), MessageLevel.WARN, "contentID");
                result.getMessages().add(message);
            } else if (!ValidateUtil.isMatch(contentIDStr, RegularConstant.REGEX_NUMBER)) {
                Message message = new Message(MessageType.FieldNumberRequired.getCode(), MessageLevel.WARN, "contentID");
                result.getMessages().add(message);
            }

            if (StringUtil.isBlank(userIDStr)) {
                Message message = new Message(MessageType.FieldRequired.getCode(), MessageLevel.WARN, "userID");
                result.getMessages().add(message);
            } else if (!ValidateUtil.isMatch(userIDStr, RegularConstant.REGEX_NUMBER)) {
                Message message = new Message(MessageType.FieldNumberRequired.getCode(), MessageLevel.WARN, "userID");
                result.getMessages().add(message);
            }

            if (result.getMessages().size() > 0) {
                // 验证失败，返回message
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }

            CmsContent content = new CmsContent();
            content.setContentID(Integer.parseInt(contentIDStr));
            content.setUpdateUserID(Integer.parseInt(userIDStr));
            contentService.deleteContent(content);

            // 返回找到的内容对象
            result.setStatus(ResponseStatus.OK.getCode());
            result.setData("success");
            // 更新浏览记录
            hisOperatorService.createHisOperator(TableName.CMS_CONTENT.toString(), content.getContentID(), request);

        } catch (Exception e) {

            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }

    @RequestMapping(value = "/content/{id}", method = RequestMethod.GET)
    public ModelAndView content(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView result = new ModelAndView("/resources/share.jsp");
        try {
            // 查询获得内容对象
            CmsContent content = contentService.getContent(id);
            if (content == null) {
                result.setViewName("/resources/404.html");
            } else {
                // 更新浏览记录
                hisOperatorService.createHisOperator(TableName.CMS_CONTENT.toString(), content.getContentID(), request);
                // 更新该内容的PV和UV
                // UV 的统计需要两个接口的请求
                List<String> optionList = new ArrayList<String>();
                optionList.add(request.getServletPath().substring(1));
                optionList.add("getContent");
                int pvCount = content.getPageViewCount() + 1;
                int uvCount = hisOperatorService.getUVCount(TableName.CMS_CONTENT.toString(), content.getContentID(), optionList);
                contentService.updateViewCount(content.getContentID(), pvCount, uvCount);
                content.setPageViewCount(pvCount);
                content.setUniqueVisitorCount(uvCount);

                result.addObject("content", content);
                String source = "";
                String sourceURL = "";
                if (content.getContentType().equals(ContentType.News.getCode())) {
                    CmsContentNews contentNews = (CmsContentNews) content;
                    source = contentNews.getSource();
                    sourceURL = contentNews.getSourceURL();
                }
                result.addObject("source", source);
                result.addObject("sourceURL", sourceURL);

                // 处理微信分享JS SDK的相关参数
                String jsAPITicket = WeixinThread.getJsAPITicket().getTicket();
                String url = request.getRequestURL().toString();
                if (request.getQueryString() != null && request.getQueryString().length() > 0) {
                    url = url + "?" + request.getQueryString();
                }
                Signature signature = WeixinUtil.getSignature(jsAPITicket, url);
                result.addObject("signature", signature);
                result.addObject("appID", WeixinThread.getAppID());
            }
        } catch (Exception e) {

            logger.fatal(e);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = { "/getChannleContent" }, method = RequestMethod.POST)
    public ResponseResult getContentByChannle(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            // 获取和验证字段
            String siteCode = request.getParameter("siteCode");
            String channelPath = request.getParameter("channelPath");

            result.checkFieldRequired("siteCode", siteCode);
            result.checkFieldRequired("channelPath", channelPath);

            if (result.getMessages().size() > 0) {
                // 验证失败，返回message
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }

            // 查询获得内容对象
            CmsContent content = contentService.getContent(siteCode, channelPath);
            if (content == null) {
                result.setStatus(ResponseStatus.QueryEmpty.getCode());
                return result;
            }

            // 返回找到的内容对象
            result.setStatus(ResponseStatus.OK.getCode());
            result.setData(content);

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = { "/getContent", "/admin/getContent" }, method = RequestMethod.POST)
    public ResponseResult getContent(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            // 获取和验证字段
            String contentID = request.getParameter("contentID");
            if (StringUtil.isBlank(contentID)) {
                Message message = new Message(MessageType.FieldRequired.getCode(), MessageLevel.WARN, "contentID");
                result.getMessages().add(message);
            } else if (!ValidateUtil.isMatch(contentID, RegularConstant.REGEX_NUMBER)) {
                Message message = new Message(MessageType.FieldNumberRequired.getCode(), MessageLevel.WARN, "contentID");
                result.getMessages().add(message);
            }

            if (result.getMessages().size() > 0) {
                // 验证失败，返回message
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }

            String userIDStr = request.getParameter("userID");
            Integer userID = null;
            if (StringUtil.isNotBlank(userIDStr) && ValidateUtil.isMatch(userIDStr, RegularConstant.REGEX_NUMBER)) {
                userID = Integer.valueOf(userIDStr);
            }

            // 查询获得内容对象
            CmsContent content = contentService.getContent(Integer.valueOf(contentID), userID);
            if (content == null) {
                result.setStatus(ResponseStatus.QueryEmpty.getCode());
                return result;
            }

            // 返回找到的内容对象
            result.setStatus(ResponseStatus.OK.getCode());
            result.setData(content);

            // 更新浏览记录
            hisOperatorService.createHisOperator(TableName.CMS_CONTENT.toString(), content.getContentID(), request);

            // 更新该内容的PV和UV
            List<String> optionList = new ArrayList<String>();
            optionList.add(request.getServletPath().substring(1));
            optionList.add("content/" + content.getContentID());
            int pvCount = content.getPageViewCount() + 1;
            int uvCount = hisOperatorService.getUVCount(TableName.CMS_CONTENT.toString(), content.getContentID(), optionList);
            contentService.updateViewCount(content.getContentID(), pvCount, uvCount);
            content.setPageViewCount(pvCount);
            content.setUniqueVisitorCount(uvCount);

        } catch (Exception e) {

            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getContentList", method = RequestMethod.POST)
    public ResponseResult getContentList(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            // 解析获得传入的参数
            // 必填参数
            String siteCode = request.getParameter("siteCode");
            String channelPath = request.getParameter("channelPath");

            result.checkFieldRequired("siteCode", siteCode);
            result.checkFieldRequired("channelPath", channelPath);

            if (result.getMessages().size() > 0) {
                // 验证失败，返回message
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }

            String sortField = request.getParameter("sortField");
            String sortType = request.getParameter("sortType");
            String start = request.getParameter("start");
            String pageSize = request.getParameter("pageSize");
            String currentPage = request.getParameter("currentPage");
            Page page = new Page();
            if (StringUtil.isNotBlank(sortField)) {
                page.setSortField(sortField);
            }
            if (StringUtil.isNotBlank(sortType)) {
                page.setSortType(sortType);
            }
            if (StringUtil.isNotBlank(start) && ValidateUtil.isMatch(start, RegularConstant.REGEX_NUMBER)) {
                page.setStart(Integer.valueOf(start));
            }
            if (StringUtil.isNotBlank(pageSize) && ValidateUtil.isMatch(pageSize, RegularConstant.REGEX_NUMBER)) {
                page.setPageSize(Integer.valueOf(pageSize));
            }
            if (StringUtil.isNotBlank(currentPage) && ValidateUtil.isMatch(currentPage, RegularConstant.REGEX_NUMBER)
                    && Integer.parseInt(currentPage) > 0) {
                page.setCurrentPage(Integer.parseInt(currentPage));
            }

            String contentType = request.getParameter("contentType");
            CmsContent content = new CmsContent();
            content.getChannel().getSite().setSiteCode(siteCode);
            content.getChannel().setChannelPath(channelPath);
            content.setContentType(contentType);

            // 执行分页操作
            int recordSum = this.contentService.getContentListCount(content);
            page = PageUtil.executePage(recordSum, page);
            List<CmsContent> contentList = this.contentService.getContentList(content, page);

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("page", page);
            data.put("contentList", contentList);
            // 返回找到的内容对象
            result.setStatus(ResponseStatus.OK.getCode());
            result.setData(data);

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = { "/createContent", "/admin/createContent" }, method = RequestMethod.POST)
    public ResponseResult createContent(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            // 解析获得传入的参数
            // 必填参数
            String siteID = request.getParameter("siteID");
            String channelID = request.getParameter("channelID");
            String userID = request.getParameter("userID");
            String contentType = request.getParameter("contentType");
            
            result.checkFieldRequired("siteID", siteID);
            result.checkFieldInteger("siteID", siteID);
            result.checkFieldRequired("channelID", channelID);
            result.checkFieldInteger("channelID", channelID);
            result.checkFieldRequired("userID", userID);
            result.checkFieldInteger("userID", userID);
            result.checkFieldRequired("contentType", contentType);
            result.checkFieldInteger("contentType", contentType);

            if (result.getMessages().size() > 0) {
                // 验证失败，返回message
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }

            // 获取可选参数
            String contentTitle = request.getParameter("contentTitle");
            String contentSubTitle = request.getParameter("contentSubTitle");
            String contentSummary = request.getParameter("contentSummary");
            String contentBody = request.getParameter("contentBody");
            String pictureList = request.getParameter("pictureList");
            
            // 工厂创建对象
            CmsContent content = null;
            if (contentType.equals(ContentType.Default.getCode())) {
                content = new CmsContent();
            } else if (contentType.equals(ContentType.News.getCode())) {
                content = new CmsContentNews();

                // 设置CmsContentNews参数
                String author = request.getParameter("author");
                String source = request.getParameter("source");
                String sourceURL = request.getParameter("sourceURL");
                CmsContentNews contentNews = (CmsContentNews) content;
                contentNews.setAuthor(author);
                contentNews.setSource(source);
                contentNews.setSourceURL(sourceURL);
            }

            // 设置CmsContent属性
            content.setSiteID(Integer.parseInt(siteID));
            content.setChannelID(Integer.parseInt(channelID));
            content.setContentType(contentType);
            content.setContentTitle(contentTitle);
            content.setContentSubTitle(contentSubTitle);
            content.setContentSummary(contentSummary);
            content.setContentBody(contentBody);
            content.setCreateTime(new Date());
            content.setCreateUserID(Integer.parseInt(userID));
            content.setUpdateUserID(Integer.parseInt(userID));

            // 创建图片列表
            if (StringUtil.isNotBlank(pictureList)) {
                JSONArray jsonArray = JSONArray.fromObject(pictureList);
                for (Object object : jsonArray) {
                    JSONObject jsonObject = (JSONObject)object;
                    String pictureURL = jsonObject.getString("pictureURL");
                    String pictureName = jsonObject.getString("pictureName");
                    CmsPicture picture = new CmsPicture();
                    picture.setPictureName(pictureName);
                    picture.setPictureURL(pictureURL);
                    content.getPictureList().add(picture);
                }
            }
            
            content = contentService.createContent(content);
            if (content != null) {
                result.setStatus(ResponseStatus.OK.getCode());
                result.setData(content);
            } else {
                result.setStatus(ResponseStatus.InsertFailed.getCode());
            }

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = { "/admin/updateContent" }, method = RequestMethod.POST)
    public ResponseResult updateContent(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            // 解析获得传入的参数
            // 必填参数
            String siteID = request.getParameter("siteID");
            String contentID = request.getParameter("contentID");
            String channelID = request.getParameter("channelID");
            String userID = request.getParameter("userID");
            String contentType = request.getParameter("contentType");

            result.checkFieldRequired("siteID", siteID);
            result.checkFieldInteger("siteID", siteID);
            result.checkFieldRequired("contentID", contentID);
            result.checkFieldInteger("contentID", contentID);
            result.checkFieldRequired("channelID", channelID);
            result.checkFieldInteger("channelID", channelID);
            result.checkFieldRequired("userID", userID);
            result.checkFieldInteger("userID", userID);
            result.checkFieldRequired("contentType", contentType);
            result.checkFieldInteger("contentType", contentType);

            if (result.getMessages().size() > 0) {
                // 验证失败，返回message
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }

         // 获取可选参数
            String contentTitle = request.getParameter("contentTitle");
            String contentSubTitle = request.getParameter("contentSubTitle");
            String contentSummary = request.getParameter("contentSummary");
            String contentBody = request.getParameter("contentBody");
            String pictureList = request.getParameter("pictureList");

            // 工厂创建对象
            CmsContent content = null;
            if (contentType.equals(ContentType.Default.getCode())) {
                content = new CmsContent();
            } else if (contentType.equals(ContentType.News.getCode())) {
                content = new CmsContentNews();

                // 设置CmsContentNews参数
                String author = request.getParameter("author");
                String source = request.getParameter("source");
                String sourceURL = request.getParameter("sourceURL");
                CmsContentNews contentNews = (CmsContentNews) content;
                contentNews.setAuthor(author);
                contentNews.setSource(source);
                contentNews.setSourceURL(sourceURL);
            }

            // 设置CmsContent属性
            content.setSiteID(Integer.parseInt(siteID));
            content.setContentID(Integer.parseInt(contentID));
            content.setChannelID(Integer.parseInt(channelID));
            content.setContentType(contentType);
            content.setContentTitle(contentTitle);
            content.setContentSubTitle(contentSubTitle);
            content.setContentSummary(contentSummary);
            content.setContentBody(contentBody);
            content.setCreateTime(new Date());
            content.setCreateUserID(Integer.parseInt(userID));
            content.setUpdateUserID(Integer.parseInt(userID));

            // 创建图片列表
            if (StringUtil.isNotBlank(pictureList)) {
                JSONArray jsonArray = JSONArray.fromObject(pictureList);
                for (Object object : jsonArray) {
                    JSONObject jsonObject = (JSONObject)object;
                    String pictureID = jsonObject.getString("pictureID");
                    String pictureURL = jsonObject.getString("pictureURL");
                    String pictureName = jsonObject.getString("pictureName");
                    CmsPicture picture = new CmsPicture();
                    if (StringUtil.isNotBlank(pictureID) && ValidateUtil.isMatch(pictureID, RegularConstant.REGEX_NUMBER)) {
                        picture.setPictureID(Integer.parseInt(pictureID));
                    }
                    picture.setPictureName(pictureName);
                    picture.setPictureURL(pictureURL);
                    content.getPictureList().add(picture);
                }
            }

            content = contentService.updateContent(content);

            if (content != null) {
                result.setStatus(ResponseStatus.OK.getCode());
                result.setData(content);
            } else {
                result.setStatus(ResponseStatus.UpdateFailed.getCode());
            }

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }
}
