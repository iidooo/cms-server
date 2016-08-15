package com.iidooo.cms.service;

import java.util.List;

import com.iidooo.cms.model.po.CmsComment;
import com.iidooo.cms.model.vo.SearchCondition;
import com.iidooo.core.model.Page;

/**
 * @author Ethan
 *
 */
/**
 * @author Ethan
 *
 */
public interface CommentService {
    List<CmsComment> getCommentListByContentID(Integer contentID, Page page);
    
    List<CmsComment> getNoticeCommentListByUserID(Integer userID, Page page);
    
    /**
     * 根据CommentID获取一条评论
     * @param commentID 获取该ID的评论
     * @return 获取的评论对象
     */
    CmsComment getCommentByID(Integer commentID);
    
    /**
     * 根据Comment信息获取一条评论
     * @param createUserID 创建者ID
     * @param contentID 内容ID
     * @param comment 评论内容
     * @return 获取的评论对象
     */
    CmsComment getCommentByInfo(Integer createUserID, Integer contentID, String comment);
    
    /**
     * 得到Comment一览的数量
     * @param condition 查询条件
     * @return Comment一览的数量
     */
    int getCommentListCount(SearchCondition condition);
    
    /**
     * 得到Comment一览
     * @param condition 查询条件
     * @param page 翻页对象
     * @return Comment一览
     */
    List<CmsComment> getCommentList(SearchCondition condition, Page page);
    
    /**
     * 创建一条评论
     * @param cmsComment 创建的评论对象
     * @return 创建成功或者失败
     * @throws Exception 抛出的未处理异常
     */
    boolean createComment(CmsComment cmsComment) throws Exception;
    
    /**
     * 更新一条评论
     * @param cmsComment 要更新的评论对象
     * @return 更新的评论
     */
    CmsComment updateComment(CmsComment cmsComment);
        
    /**
     * 删除评论
     * @param comment 要删除的评论对象
     * @return 删除是否成功
     */
    boolean deleteComment(CmsComment comment);
}
