package com.iidooo.cms.mapper;

import java.util.List;

import com.iidooo.cms.model.po.CmsContent;
import com.iidooo.cms.model.po.CmsPicture;

public interface CmsPictureMapper {

    /**
     * 插入图片数据
     * @param picture 该图片数据会被插入数据库
     * @return 所影响的行数
     */
    int insert(CmsPicture picture);
    
    /**
     * 根据ContentID获取图片列表
     * @param contentID 内容ID
     * @return 所获的的图片列表
     */
    List<CmsPicture> selectByContentID(Integer contentID);

    /**
     * 通过PictureID获取图片
     * @param pictureID 图片ID
     * @return 所获的图片对象
     */
    CmsPicture selectByPictureID(Integer pictureID);
    
    /**
     * 通过PictureID更新
     * @param picture 要更新的图片对象
     * @return 所影响的行数
     */
    int updateByPictureID(CmsPicture picture);    

    /**
     * 根据PictureID把内容图片删掉
     * @param picture 要删除的图片对象
     * @return 所影响的行数
     */
    int deleteByPictureID(CmsPicture picture);
    
    /**
     * 根据ContentID把所有的关联图片删掉
     * @param content 要删除图片的Content对象
     * @return 所影响的行数
     */
    int deleteByContentID(CmsContent content);
}