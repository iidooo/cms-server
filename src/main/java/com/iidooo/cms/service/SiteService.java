package com.iidooo.cms.service;

import java.util.List;

import com.iidooo.cms.model.po.CmsSite;

public interface SiteService {
    List<CmsSite> getSiteList(Integer ownerID);
    
    CmsSite getSite(Integer siteID);
    
    /**
     * 通过SiteCode获取站点对象
     * @param siteCode 站点Code
     * @return 所获的的站点对象
     */
    CmsSite getSiteByCode(String siteCode);
    
    /**
     * 创建新站点
     * @param site 站点对象
     * @return 所创建的站点对象
     */
    CmsSite createSite(CmsSite site);
    
    /**
     * 更新CmsSite对象
     * @param site 要更新的对象信息
     * @return 更新后的CmsSite 对象
     */
    CmsSite updateSite(CmsSite site);
}
