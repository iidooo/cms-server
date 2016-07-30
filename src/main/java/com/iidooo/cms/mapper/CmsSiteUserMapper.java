package com.iidooo.cms.mapper;

import java.util.List;

import com.iidooo.cms.model.po.CmsSiteUser;
import com.iidooo.cms.model.vo.SearchCondition;

public interface CmsSiteUserMapper {
    int deleteByPrimaryKey(Integer relID);

    int insert(CmsSiteUser record);

    int insertSelective(CmsSiteUser record);

    CmsSiteUser selectByPrimaryKey(Integer relID);
    
    /**
     * 检索站点的用户数量
     * 
     * @param condition 检索的条件封装
     * @return 得到的站点用户数量
     */
    int selectCountForSearch(SearchCondition condition);

    int updateByPrimaryKeySelective(CmsSiteUser record);

    int updateByPrimaryKey(CmsSiteUser record);
}