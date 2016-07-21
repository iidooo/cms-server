package com.iidooo.cms.mapper;

import java.util.List;

import com.iidooo.cms.model.po.CmsContent;
import com.iidooo.cms.model.po.CmsFile;

public interface CmsFileMapper {

    /**
     * 插入文件
     * @param file 文件对象
     * @return 插入所影响的行数
     */
    int insert(CmsFile file);
    
    /**
     * 通过ContentID获得文件列表
     * @param contentID 内容ID
     * @return 所获得的文件一览
     */
    List<CmsFile> selectByContentID(Integer contentID);

    /**
     * 通过FileID获取CmsFile对象
     * @param fileID 查询条件FileID
     * @return 所获得的CmsFile对象
     */
    CmsFile selectByFileID(Integer fileID);

    /**
     * 通过FileID更新文件对象
     * @param file 文件对象
     * @return 更新所影响的行数
     */
    int updateByFileID(CmsFile file);    

    /**
     * 逻辑删除
     * @param file 要删除的CmsFile对象
     * @return 删除所影响的行数
     */
    int deleteByFileID(CmsFile file);
    
    /**
     * 根据ContentID把所有的关联文件删掉
     * @param content 要删除图片的Content对象
     * @return 所影响的行数
     */
    int deleteByContentID(CmsContent content);
}