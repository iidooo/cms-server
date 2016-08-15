package com.iidooo.cms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iidooo.cms.model.po.CmsSiteUser;
import com.iidooo.cms.model.vo.SearchCondition;
import com.iidooo.cms.service.SiteUserService;
import com.iidooo.core.constant.MessageConstant;
import com.iidooo.core.constant.RegularConstant;
import com.iidooo.core.enums.ResponseStatus;
import com.iidooo.core.model.Page;
import com.iidooo.core.model.ResponseResult;
import com.iidooo.core.model.po.SecurityUser;
import com.iidooo.core.util.PageUtil;
import com.iidooo.core.util.StringUtil;
import com.iidooo.core.util.ValidateUtil;

@Controller
public class SiteUserController {
    private static final Logger logger = Logger.getLogger(SiteUserController.class);
    
    @Autowired
    private SiteUserService siteUserService;
    
    @ResponseBody
    @RequestMapping(value = {"/admin/getSiteUserCount"}, method = RequestMethod.POST)
    public ResponseResult getSiteUserCount(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {            
            String siteID = request.getParameter("siteID");
            result.checkFieldRequired("siteID", siteID);
            result.checkFieldInteger("siteID", siteID);
            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }
            
            String startDateTime = request.getParameter("startDateTime");
            String endDateTime = request.getParameter("endDateTime");
            String loginID = request.getParameter("loginID");
            String userName = request.getParameter("userName");
            String sex = request.getParameter("sex");
            String mobile = request.getParameter("mobile");
            String email = request.getParameter("email");
            String weixinID = request.getParameter("weixinID");
            String roles = request.getParameter("roles");
            
            SearchCondition condition = new SearchCondition();
            condition.setSiteID(Integer.parseInt(siteID));
            condition.setStartDateTime(startDateTime);
            condition.setEndDateTime(endDateTime);
            condition.setLoginID(loginID);
            condition.setUserName(userName);
            condition.setSex(sex);
            condition.setMobile(mobile);
            condition.setEmail(email);
            condition.setWeixinID(weixinID);
            if (StringUtil.isNotBlank(roles)) {
                JSONArray jsonArray = JSONArray.fromObject(roles);
                for (Object object : jsonArray) {
                    condition.getRoles().add(object.toString());
                }
            }
            
            int recordSum = siteUserService.getSiteUserCount(condition);
            result.setStatus(ResponseStatus.OK.getCode());
            result.setData(recordSum);
            
        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }
    
    @ResponseBody
    @RequestMapping(value = {"/admin/searchSiteUserList"}, method = RequestMethod.POST)
    public ResponseResult searchSiteUserList(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {            
            String siteID = request.getParameter("siteID");
            result.checkFieldRequired("siteID", siteID);
            result.checkFieldInteger("siteID", siteID);
            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }
            
            String startDateTime = request.getParameter("startDateTime");
            String endDateTime = request.getParameter("endDateTime");
            String loginID = request.getParameter("loginID");
            String userName = request.getParameter("userName");
            String sex = request.getParameter("sex");
            String mobile = request.getParameter("mobile");
            String email = request.getParameter("email");
            String weixinID = request.getParameter("weixinID");
            String roles = request.getParameter("roles");
            
            SearchCondition condition = new SearchCondition();
            condition.setSiteID(Integer.parseInt(siteID));
            condition.setStartDateTime(startDateTime);
            condition.setEndDateTime(endDateTime);
            condition.setLoginID(loginID);
            condition.setUserName(userName);
            condition.setSex(sex);
            condition.setMobile(mobile);
            condition.setEmail(email);
            condition.setWeixinID(weixinID);
            if (StringUtil.isNotBlank(roles)) {
                JSONArray jsonArray = JSONArray.fromObject(roles);
                for (Object object : jsonArray) {
                    condition.getRoles().add(object.toString());
                }
            }
            
            int recordSum = siteUserService.getSiteUserCount(condition);
            
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
            List<CmsSiteUser> userList = siteUserService.getSiteUserList(condition, page);
            data.put("page", page);
            data.put("userList", userList);
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
    @RequestMapping(value = "/admin/getSiteUser", method = RequestMethod.POST)
    public ResponseResult getSiteUser(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            String siteID = request.getParameter("siteID");
            String userID = request.getParameter("userID");
            
            result.checkFieldRequired("siteID", siteID);
            result.checkFieldInteger("siteID", siteID);
            result.checkFieldRequired("userID", userID);
            result.checkFieldInteger("userID", userID);
            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.ValidateFailed.getCode());
                return result;
            }

            CmsSiteUser siteUser = this.siteUserService.getSiteUser(Integer.parseInt(siteID), Integer.parseInt(userID));
            if (siteUser == null) {
                result.setStatus(ResponseStatus.QueryEmpty.getCode());
            } else {
                result.setStatus(ResponseStatus.OK.getCode());
                result.setData(siteUser);
            }

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }
    
    @ResponseBody
    @RequestMapping(value = "/admin/updateSiteUser", method = RequestMethod.POST)
    public ResponseResult updateSiteUser(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            String siteID = request.getParameter("siteID");
            String userID = request.getParameter("userID");
            String operatorID = request.getParameter("operatorID");
            
            result.checkFieldRequired("operatorID", operatorID);
            result.checkFieldInteger("operatorID", operatorID);
            result.checkFieldRequired("siteID", siteID);
            result.checkFieldInteger("siteID", siteID);
            result.checkFieldRequired("userID", userID);
            result.checkFieldInteger("userID", userID);
            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.ValidateFailed.getCode());
                return result;
            }

            String role = request.getParameter("role");
            
            CmsSiteUser siteUser = new CmsSiteUser();
            siteUser.setSiteID(Integer.parseInt(siteID));
            siteUser.setUserID(Integer.parseInt(userID));
            siteUser.setRole(role);
            siteUser.setUpdateUserID(Integer.parseInt(operatorID));
            siteUser = siteUserService.updateSiteUser(siteUser);
            if (siteUser == null) {
                result.setStatus(ResponseStatus.UpdateFailed.getCode());
            } else {
                result.setStatus(ResponseStatus.OK.getCode());
                result.setData(siteUser);
            }

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }
}
