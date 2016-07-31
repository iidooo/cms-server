package com.iidooo.cms.service;

import java.util.List;

import com.iidooo.cms.model.po.CmsSite;

public interface SiteService {
    List<CmsSite> getSiteList(Integer ownerID);
    
    CmsSite getSite(Integer siteID);
    
    /**
     * 更新CmsSite对象
     * @param site 要更新的对象信息
     * @return 更新后的CmsSite 对象
     */
    CmsSite updateSite(CmsSite site);
}
