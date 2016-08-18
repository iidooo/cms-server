package com.iidooo.cms.mapper;

import java.util.List;

import com.iidooo.cms.model.po.CmsSite;

public interface CmsSiteMapper {
    int deleteByPrimaryKey(Integer siteID);

    /**
     * 插入站点数据
     * @param site 站点数据对象
     * @return 所影响的行数
     */
    int insert(CmsSite site);

    /**
     * 通过站点ID获取站点对象
     * @param siteID 站点ID
     * @return 站点对象
     */
    CmsSite selectBySiteID(Integer siteID);
    
    /**
     * 通过站点Code获取站点对象
     * @param siteCode 站点Code
     * @return 站点对象
     */
    CmsSite selectBySiteCode(String siteCode);
    
    /**
     * 查询获取拥有者的站点列表
     * @param userID 拥有者ID
     * @return 站点列表
     */
    List<CmsSite> selectByOwner(Integer userID);

    /**
     * 通过站点ID来更新站点
     * @param site 站点信息对象
     * @return 更新操作所影响的行
     */
    int updateBySiteID(CmsSite site);
}