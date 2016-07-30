package com.iidooo.cms.controller;

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
    @RequestMapping(value = {"/admin/getRelatedSiteList"}, method = RequestMethod.POST)
    public ResponseResult getRelatedSiteList(HttpServletRequest request, HttpServletResponse response) {
        ResponseResult result = new ResponseResult();
        try {
            
            String userID = request.getParameter("userID");
            result.checkFieldRequired("userID", userID);
            result.checkFieldInteger("userID", userID);
            if (result.getMessages().size() > 0) {
                result.setStatus(ResponseStatus.Failed.getCode());
                return result;
            }
            
            List<CmsSite> sites = siteService.getSiteList(Integer.parseInt(userID));   

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
