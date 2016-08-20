package com.iidooo.cms.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.iidooo.cms.model.vo.SearchCondition;
import com.iidooo.cms.service.ContentService;
import com.iidooo.cms.service.FavoriteService;
import com.iidooo.core.constant.RegularConstant;
import com.iidooo.core.enums.ResponseStatus;
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
    @RequestMapping(value = "/admin/getContentCount", method = RequestMethod.POST)
    public ResponseResult getContentCount(HttpServletRequest request, HttpServletResponse response) {
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
            result.setStatus(ResponseStatus.OK.getCode());
            result.setData(recordSum);

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/admin/searchContentList", method = RequestMethod.POST)
    public ResponseResult searchContentList(HttpServletRequest request, HttpServletResponse response) {
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
            List<CmsContent> contentList = contentService.getContentList(condition, page);
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
    @RequestMapping(value = "/admin/deleteContent", method = RequestMethod.POST)
    public ResponseResult deleteContent(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            String contentID = request.getParameter("contentID");
            String operatorID = request.getParameter("operatorID");

            result.checkFieldRequired("contentID", contentID);
            result.checkFieldInteger("contentID", contentID);
            result.checkFieldRequired("operatorID", operatorID);
            result.checkFieldInteger("operatorID", operatorID);

            if (result.getMessages().size() > 0) {
                // 验证失败，返回message
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }

            CmsContent content = new CmsContent();
            content.setContentID(Integer.parseInt(contentID));
            content.setUpdateUserID(Integer.parseInt(operatorID));
            if(!contentService.deleteContent(content)){
                result.setStatus(ResponseStatus.Failed.getCode());
            } else{
                // 返回找到的内容对象
                result.setStatus(ResponseStatus.OK.getCode());
                result.setData("success");
            }
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
    @RequestMapping(value = { "/cms/getContent", "/cms/admin/getContent" }, method = RequestMethod.POST)
    public ResponseResult getContent(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            String contentID = request.getParameter("contentID");
            result.checkFieldRequired("contentID", contentID);
            result.checkFieldInteger("contentID", contentID);

            if (result.getMessages().size() > 0) {
                // 验证失败，返回message
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }
            
            // 查询获得内容对象
            CmsContent content = contentService.getContent(Integer.valueOf(contentID));
            if (content == null) {
                result.setStatus(ResponseStatus.QueryEmpty.getCode());
                return result;
            }

            if (request.getServletPath().equals("/cms/getContent")) {
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
    @RequestMapping(value = "/cms/getContentList", method = RequestMethod.POST)
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
            String operatorID = request.getParameter("operatorID");
            String contentType = request.getParameter("contentType");

            result.checkFieldRequired("siteID", siteID);
            result.checkFieldInteger("siteID", siteID);
            result.checkFieldRequired("channelID", channelID);
            result.checkFieldInteger("channelID", channelID);
            result.checkFieldRequired("operatorID", operatorID);
            result.checkFieldInteger("operatorID", operatorID);
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
            String status = request.getParameter("status");
            String isSilent = request.getParameter("isSilent");
            String stickyIndex = request.getParameter("stickyIndex");
            String contentImageTitle = request.getParameter("contentImageTitle");
            String contentSummary = request.getParameter("contentSummary");
            String contentBody = request.getParameter("contentBody");

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
            content.setContentImageTitle(contentImageTitle);
            content.setContentSummary(contentSummary);
            content.setContentBody(contentBody);
            
            if(StringUtil.isNotBlank(status) && ValidateUtil.isMatch(status, RegularConstant.REGEX_NUMBER)){
                content.setStatus(status);
            }
            if (StringUtil.isNotBlank(stickyIndex) &&  ValidateUtil.isMatch(stickyIndex, RegularConstant.REGEX_NUMBER)) {
                content.setStickyIndex(Integer.parseInt(stickyIndex));
            }
            if (StringUtil.isNotBlank(isSilent) && ValidateUtil.isMatch(isSilent, RegularConstant.REGEX_NUMBER)) {
                content.setIsSilent(Integer.parseInt(isSilent));
            }
            
            content.setCreateTime(new Date());
            content.setCreateUserID(Integer.parseInt(operatorID));
            content.setUpdateUserID(Integer.parseInt(operatorID));

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
            String operatorID = request.getParameter("operatorID");
            String contentType = request.getParameter("contentType");

            result.checkFieldRequired("siteID", siteID);
            result.checkFieldInteger("siteID", siteID);
            result.checkFieldRequired("contentID", contentID);
            result.checkFieldInteger("contentID", contentID);
            result.checkFieldRequired("channelID", channelID);
            result.checkFieldInteger("channelID", channelID);
            result.checkFieldRequired("operatorID", operatorID);
            result.checkFieldInteger("operatorID", operatorID);
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
            String contentImageTitle = request.getParameter("contentImageTitle");
            String contentSummary = request.getParameter("contentSummary");
            String contentBody = request.getParameter("contentBody");

            String status = request.getParameter("status");
            String isSilent = request.getParameter("isSilent");
            String stickyIndex = request.getParameter("stickyIndex");
            
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
            content.setContentImageTitle(contentImageTitle);
            content.setContentSummary(contentSummary);
            content.setContentBody(contentBody);
            
            if(StringUtil.isNotBlank(status) && ValidateUtil.isMatch(status, RegularConstant.REGEX_NUMBER)){
                content.setStatus(status);
            }
            if (StringUtil.isNotBlank(stickyIndex) &&  ValidateUtil.isMatch(stickyIndex, RegularConstant.REGEX_NUMBER)) {
                content.setStickyIndex(Integer.parseInt(stickyIndex));
            }
            if (StringUtil.isNotBlank(isSilent) && ValidateUtil.isMatch(isSilent, RegularConstant.REGEX_NUMBER)) {
                content.setIsSilent(Integer.parseInt(isSilent));
            }
            
            content.setUpdateUserID(Integer.parseInt(operatorID));

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
