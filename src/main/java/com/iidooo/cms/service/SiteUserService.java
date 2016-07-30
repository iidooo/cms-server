package com.iidooo.cms.service;

import com.iidooo.cms.model.vo.SearchCondition;

public interface SiteUserService {
    
    /**
     * 通过查询条件得到站点用户列表项目数
     * @param condition 查询条件
     * @return 项目数量
     */
    int getSiteUserCount(SearchCondition condition);
}
