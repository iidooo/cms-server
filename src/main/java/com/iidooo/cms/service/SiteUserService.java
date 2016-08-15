package com.iidooo.cms.service;

import java.util.List;

import com.iidooo.cms.model.po.CmsSiteUser;
import com.iidooo.cms.model.vo.SearchCondition;
import com.iidooo.core.model.Page;

public interface SiteUserService {
    
    /**
     * 通过查询条件得到站点用户列表项目数
     * @param condition 查询条件
     * @return 项目数量
     */
    int getSiteUserCount(SearchCondition condition);
    
    /**
     * 通过查询条件得到站点用户列表
     * @param condition 查询条件
     * @param page 分页对象
     * @return 用户一览
     */
    List<CmsSiteUser> getSiteUserList(SearchCondition condition, Page page);
    
    /**
     * 通过站点ID和用户ID获得用户信息
     * @param siteID 站点ID
     * @param userID 用户ID
     * @return 站点用户信息
     */
    CmsSiteUser getSiteUser(Integer siteID, Integer userID);
    
    /**
     * 更新站点用户对象
     * @param siteUser 站点用户信息
     * @return 更新后的站点用户对象
     */
    CmsSiteUser updateSiteUser(CmsSiteUser siteUser);
}
