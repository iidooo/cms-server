package com.iidooo.cms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iidooo.cms.model.po.CmsSiteUser;
import com.iidooo.cms.model.vo.SearchCondition;
import com.iidooo.core.model.Page;

public interface CmsSiteUserMapper {
    int deleteByPrimaryKey(Integer relID);

    int insert(CmsSiteUser record);

    int insertSelective(CmsSiteUser record);

    CmsSiteUser selectByPrimaryKey(Integer relID);
    
    /**
     * 通过站点ID和用户ID获得站点用户对象
     * @param siteID
     * @param userID
     * @return 所获的的用户信息
     */
    CmsSiteUser selectBySiteUserID(@Param("siteID")Integer siteID, @Param("userID")Integer userID);
    
    /**
     * 检索站点的用户数量
     * 
     * @param condition 检索的条件封装
     * @return 得到的站点用户数量
     */
    int selectCountForSearch(SearchCondition condition);
    
    /**
     * 检索站点的用户
     * @param condition 检索条件
     * @param page 分页对象
     * @return 得到的站点用户列表
     */
    List<CmsSiteUser> selectForSearch(@Param("condition")SearchCondition condition, @Param("page")Page page);

    /**
     * 通过站点ID和用户ID更新站点用户信息
     * @param siteUser 站点用户对象
     * @return 更新所影响的行
     */
    int updateBySiteUserID(CmsSiteUser siteUser);
}