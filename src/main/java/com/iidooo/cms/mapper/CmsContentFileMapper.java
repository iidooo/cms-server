package com.iidooo.cms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iidooo.cms.model.po.CmsContent;
import com.iidooo.cms.model.po.CmsContentFile;
import com.iidooo.core.model.Page;

public interface CmsContentFileMapper {
    int deleteByPrimaryKey(Integer contentID);

    int insert(CmsContentFile record);

    int insertSelective(CmsContentFile record);

    /**
     * 根据栏目路径查询获得内容一览数目
     * 
     * @param content 封装参数的对象
     * @return 内容一览List对象数目
     */
    int selectContentListCount(CmsContent content);

    /**
     * 根据栏目路径查询获得内容一览
     * 
     * @param content 封装参数的对象
     * @param page 分页对象
     * @return 内容一览List对象
     */
    List<CmsContent> selectContentList(@Param("content") CmsContent content, @Param("page") Page page);

    int updateByPrimaryKeySelective(CmsContentFile record);

    int updateByPrimaryKey(CmsContentFile record);
}