package com.iidooo.cms.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iidooo.cms.model.vo.SearchCondition;
import com.iidooo.cms.service.SiteUserService;
import com.iidooo.core.enums.ResponseStatus;
import com.iidooo.core.model.ResponseResult;

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
            
            SearchCondition condition = new SearchCondition();
            condition.setSiteID(Integer.parseInt(siteID));
            condition.setStartDateTime(startDateTime);
            condition.setEndDateTime(endDateTime);
            
            int recordSum = siteUserService.getSiteUserCount(condition);
            result.setStatus(ResponseStatus.OK.getCode());
            result.setData(recordSum);
            
        } catch (Exception e) {
            logger.fatal(e);
            result.checkException(e);
        }
        return result;
    }
}
