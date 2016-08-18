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

import com.iidooo.cms.model.po.CmsSite;
import com.iidooo.cms.service.SiteService;
import com.iidooo.core.enums.ResponseStatus;
import com.iidooo.core.model.ResponseResult;

@Controller
public class SiteController {
    private static final Logger logger = Logger.getLogger(SiteController.class);

    @Autowired
    private SiteService siteService;


    @ResponseBody
    @RequestMapping(value = { "/admin/createSite" }, method = RequestMethod.POST)
    public ResponseResult createSite(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            String siteCode = request.getParameter("siteCode");
            String operatorID = request.getParameter("operatorID");
            result.checkFieldRequired("siteCode", siteCode);
            result.checkFieldEnglish("siteCode", siteCode);
            result.checkFieldRequired("operatorID", operatorID);
            result.checkFieldInteger("operatorID", operatorID);
            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }
            
            // 判断站点Code是否重复
            if(siteService.getSiteByCode(siteCode) != null){
                result.checkFieldUnique("siteCode", siteCode);
                result.setStatus(ResponseStatus.DuplicateFailed.getCode());
                return result;
            }

            String siteName = request.getParameter("siteName");
            String siteURL = request.getParameter("siteURL");
            String remarks = request.getParameter("remarks");
            
            CmsSite site = new CmsSite();
            site.setSiteCode(siteCode);
            site.setSiteName(siteName);
            site.setSiteURL(siteURL);
            site.setRemarks(remarks);
            site.setCreateUserID(Integer.parseInt(operatorID));
            site.setUpdateUserID(Integer.parseInt(operatorID));
            site.setCreateTime(new Date());
            site = siteService.createSite(site);
            if (site == null) {
                result.setStatus(ResponseStatus.Failed.getCode());
            } else {
                result.setStatus(ResponseStatus.OK.getCode());
                result.setData(site);
            }
        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }
    
    @ResponseBody
    @RequestMapping(value = { "/admin/getSite" }, method = RequestMethod.POST)
    public ResponseResult getSite(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {

            String siteID = request.getParameter("siteID");
            result.checkFieldRequired("siteID", siteID);
            result.checkFieldInteger("siteID", siteID);
            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }

            CmsSite site = siteService.getSite(Integer.parseInt(siteID));
            if (site == null) {
                result.setStatus(ResponseStatus.Failed.getCode());
            } else {
                result.setStatus(ResponseStatus.OK.getCode());
                result.setData(site);
            }
        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }
    
    @ResponseBody
    @RequestMapping(value = { "/admin/updateSite" }, method = RequestMethod.POST)
    public ResponseResult updateSite(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            String siteID = request.getParameter("siteID");
            String operatorID = request.getParameter("operatorID");
            result.checkFieldRequired("siteID", siteID);
            result.checkFieldInteger("siteID", siteID);
            result.checkFieldRequired("operatorID", operatorID);
            result.checkFieldInteger("operatorID", operatorID);
            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }

            String siteName = request.getParameter("siteName");
            String siteURL = request.getParameter("siteURL");
            String remarks = request.getParameter("remarks");
            
            CmsSite site = new CmsSite();
            site.setSiteID(Integer.parseInt(siteID));
            site.setSiteName(siteName);
            site.setSiteURL(siteURL);
            site.setRemarks(remarks);
            site.setUpdateUserID(Integer.parseInt(operatorID));
            site = siteService.updateSite(site);
            if (site == null) {
                result.setStatus(ResponseStatus.Failed.getCode());
            } else {
                result.setStatus(ResponseStatus.OK.getCode());
                result.setData(site);
            }
        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = { "/admin/getRelatedSiteList" }, method = RequestMethod.POST)
    public ResponseResult getRelatedSiteList(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {

            String operatorID = request.getParameter("operatorID");
            result.checkFieldRequired("userID", operatorID);
            result.checkFieldInteger("userID", operatorID);
            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }

            List<CmsSite> sites = siteService.getSiteList(Integer.parseInt(operatorID));

            // 返回找到的内容对象
            result.setStatus(ResponseStatus.OK.getCode());
            result.setData(sites);

        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }
}
