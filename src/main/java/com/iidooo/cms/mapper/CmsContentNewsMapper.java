package com.iidooo.cms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iidooo.cms.model.po.CmsContent;
import com.iidooo.cms.model.po.CmsContentNews;
import com.iidooo.core.model.Page;

public interface CmsContentNewsMapper {

    /**
     * 插入新的新闻内容对象
     * @param contentNews 该新闻内容对象会被插入
     * @return 所影响的行数
     */
    int insert(CmsContentNews contentNews);

    /**
     * 根据内容ID获得新闻内容对象
     * @param contentID 通过该内容ID获得
     * @return 所获的的新闻内容对象
     */
    CmsContentNews selectByContentID(Integer contentID);
    
    /**
     * 获得一个栏目的一个内容
     * @param siteCode 站点Code
     * @param channelPath 栏目路径
     * @return 所获的的新闻内容对象
     */
    CmsContentNews selectChannelContent(@Param("siteCode")String siteCode, @Param("channelPath")String channelPath);

    /**
     * 根据ContentID更新新闻
     * @param contentNews 更新对象
     * @return 所影响的行数
     */
    int updateByContentID(CmsContentNews contentNews);
    
    /**
     * 得到指定栏目下的内容一览
     * @param channelPath 栏目路径
     * @param cmsContent 指定内容的参数
     * @param page 分页对象
     * @return 内容一览List
     */
    List<CmsContent> selectContentNewsList(@Param("channelPath")String channelPath, @Param("cmsContent")CmsContent cmsContent, @Param("page")Page page);

    /**
     * 毒电波用的Tab1新闻一览
     * @param cmsContent 指定内容的参数
     * @param page 分页显示对象
     * @return 内容一览List
     */
    List<CmsContent> selectContentListForToxicWaveTab1(@Param("cmsContent")CmsContent cmsContent, @Param("page") Page page);

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
}