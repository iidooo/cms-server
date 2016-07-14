package com.iidooo.cms.mapper;

import com.iidooo.cms.model.po.CmsPicture;

public interface CmsPictureMapper {
    
    /**
     * 根据内容ID把图片列表删掉
     * @param contentID 内容ID
     * @return 所影响的行数
     */
    int deleteByContentID(Integer contentID);

    /**
     * 插入图片数据
     * @param picture 该图片数据会被插入数据库
     * @return 所影响的行数
     */
    int insert(CmsPicture picture);

    CmsPicture selectByPrimaryKey(Integer pictureID);
    
    /**
     * 通过PictureID更新
     * @param picture
     * @return 所影响的行数
     */
    int updateByPictureID(CmsPicture picture);
}