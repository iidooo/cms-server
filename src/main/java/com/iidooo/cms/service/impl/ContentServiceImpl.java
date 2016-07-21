package com.iidooo.cms.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iidooo.cms.constant.CmsDictContant;
import com.iidooo.cms.enums.ContentType;
import com.iidooo.cms.mapper.CmsCommentMapper;
import com.iidooo.cms.mapper.CmsContentMapper;
import com.iidooo.cms.mapper.CmsContentNewsMapper;
import com.iidooo.cms.mapper.CmsFavoriteMapper;
import com.iidooo.cms.mapper.CmsFileMapper;
import com.iidooo.cms.mapper.CmsPictureMapper;
import com.iidooo.cms.model.po.CmsContent;
import com.iidooo.cms.model.po.CmsContentNews;
import com.iidooo.cms.model.po.CmsFavorite;
import com.iidooo.cms.model.po.CmsFile;
import com.iidooo.cms.model.po.CmsPicture;
import com.iidooo.cms.model.vo.SearchCondition;
import com.iidooo.cms.service.ContentService;
import com.iidooo.core.mapper.DictItemMapper;
import com.iidooo.core.model.Page;
import com.iidooo.core.model.po.DictItem;
import com.iidooo.core.util.DateUtil;

@Service
public class ContentServiceImpl implements ContentService {

    private static final Logger logger = Logger.getLogger(ContentServiceImpl.class);

    @Autowired
    private DictItemMapper dictItemMapper;

    @Autowired
    private CmsContentMapper cmsContentDao;

    @Autowired
    private CmsContentNewsMapper cmsContentNewsDao;

    @Autowired
    private CmsPictureMapper cmsPictureMapper;

    @Autowired
    private CmsFileMapper cmsFileMapper;

    @Autowired
    private CmsCommentMapper cmsCommentMapper;

    @Autowired
    private CmsFavoriteMapper favoriteMapper;

    @Override
    public CmsContent getContent(Integer contentID) {
        try {
            CmsContent result = null;

            result = cmsContentNewsDao.selectByContentID(contentID);
            if (result == null) {
                result = cmsContentDao.selectByContentID(contentID);
            }

            // 设置所剩余的显示秒数
            if (result != null) {
                result.setRemainTime(this.getRemainTime(result.getContentType(), result.getCreateTime()));
            }
            return result;
        } catch (Exception e) {
            logger.fatal(e);
            throw e;
        }
    }

    @Override
    public CmsContent getContent(Integer contentID, Integer userID) {
        try {
            CmsContent result = null;

            result = cmsContentNewsDao.selectByContentID(contentID);
            if (result == null) {
                result = cmsContentDao.selectByContentID(contentID);
            }

            if (result != null && userID != null) {
                CmsFavorite cmsFavorite = favoriteMapper.selectByUserContentID(userID, contentID);
                if (cmsFavorite != null) {
                    result.setFavoriteID(cmsFavorite.getFavoriteID().toString());
                }

                // 设置所剩余的显示秒数
                result.setRemainTime(this.getRemainTime(result.getContentType(), result.getCreateTime()));
            }
            return result;
        } catch (Exception e) {
            logger.fatal(e);
            throw e;
        }
    }

    @Override
    public CmsContent getContent(String siteCode, String channelPath) {
        try {
            CmsContent result = null;

            result = cmsContentNewsDao.selectChannelContent(siteCode, channelPath);
            if (result == null) {
                result = cmsContentDao.selectChannelContent(siteCode, channelPath);
            }
            return result;
        } catch (Exception e) {
            logger.fatal(e);
            throw e;
        }
    }

    @Override
    public CmsContent getContentByInfo(Integer createUserID, String contentType, String contentBody) {
        try {
            CmsContent result = cmsContentDao.selectByContentInfo(createUserID, contentType, contentBody);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.fatal(e);
            return null;
        }
    }

    @Override
    public List<CmsContent> getContentListByType(String channelPath, CmsContent cmsContent, Page page) {
        try {
            List<CmsContent> result = new ArrayList<CmsContent>();

            String contentType = cmsContent.getContentType();

            // 获得内容显示过期天数的配置字典项
            String dictClassCode = CmsDictContant.DICT_CLASS_CMS_PROPERTIES;
            String dictItemCode = "";
            if (contentType.equals(ContentType.News.getCode())) {
                dictItemCode = CmsDictContant.DICT_ITEM_CONTENT_TYPE_2_EXPIRE_DAY;
            } else {
                dictItemCode = CmsDictContant.DICT_ITEM_CONTENT_TYPE_1_EXPIRE_DAY;
            }
            DictItem expireDictItem = dictItemMapper.selectByClassItemCode(dictClassCode, dictItemCode);
            Integer expireDay = Integer.parseInt(expireDictItem.getDictItemValue());
            if (expireDay > 0) {
                Date createTime = DateUtil.getDate(new Date(), -expireDay);
                cmsContent.setCreateTime(createTime);
            }

            cmsContent.setStartShowDate(DateUtil.getNow(DateUtil.DATE_HYPHEN));
            cmsContent.setStartShowTime(DateUtil.getNow(DateUtil.TIME_COLON));
            cmsContent.setEndShowDate(DateUtil.getNow(DateUtil.DATE_HYPHEN));
            cmsContent.setEndShowTime(DateUtil.getNow(DateUtil.TIME_COLON));
            if (contentType.equals(ContentType.News.getCode())) {
                if (channelPath.equals("ToxicWave")) {
                    result = cmsContentNewsDao.selectContentListForToxicWaveTab1(cmsContent, page);
                } else {
                    result = cmsContentNewsDao.selectContentNewsList(channelPath, cmsContent, page);
                }
            } else {
                if (channelPath.equals("ToxicWave")) {
                    result = cmsContentDao.selectContentListForToxicWaveTab2(cmsContent, page);
                } else {
                    result = cmsContentDao.selectContentList(cmsContent, page);
                }
            }

            // 设置所剩余的显示秒数
            for (CmsContent item : result) {
                item.setRemainTime(this.getRemainTime(item.getContentType(), item.getCreateTime()));
            }

            return result;
        } catch (Exception e) {
            logger.fatal(e);
            throw e;
        }
    }

    @Override
    public int getContentsCount(SearchCondition condition) {
        try {
            int count = cmsContentDao.selectCountForSearch(condition);
            return count;
        } catch (Exception e) {
            logger.fatal(e);
            throw e;
        }
    }

    @Override
    public List<CmsContent> getContents(SearchCondition condition, Page page) {
        try {
            List<CmsContent> result = cmsContentDao.selectForSearch(condition, page);

            return result;
        } catch (Exception e) {
            logger.fatal(e);
            throw e;
        }
    }

    @Override
    public int getContentListCount(CmsContent content) {
        int result = 0;
        try {
            result = cmsContentDao.selectContentListCount(content);
        } catch (Exception e) {
            logger.fatal(e);
            throw e;
        }
        return result;
    }

    @Override
    public List<CmsContent> getContentList(CmsContent content, Page page) {
        List<CmsContent> result = new ArrayList<CmsContent>();
        try {
            result = cmsContentDao.selectContentList(content, page);
        } catch (Exception e) {
            logger.fatal(e);
            throw e;
        }
        return result;
    }

    @Override
    @Transactional
    public CmsContent createContent(CmsContent content) {
        CmsContent result = null;
        try {
            if (cmsContentDao.insert(content) <= 0) {
                throw new Exception();
            }

            if (content.getContentType().equals(ContentType.News.getCode())) {
                CmsContentNews cmsContentNews = (CmsContentNews) content;
                if (cmsContentNewsDao.insert(cmsContentNews) <= 0) {
                    throw new Exception();
                }
            }

            result = getContent(content.getContentID());
        } catch (Exception e) {
            logger.fatal(e);
        }
        return result;
    }

    @Override
    @Transactional
    public CmsContent updateContent(CmsContent content) {
        CmsContent result = null;
        try {
            if (cmsContentDao.updateByContentID(content) <= 0) {
                throw new Exception();
            }

            if (content.getContentType().equals(ContentType.News.getCode())) {
                CmsContentNews cmsContentNews = (CmsContentNews) content;
                if (cmsContentNews.getSource() != null || cmsContentNews.getSourceURL() != null) {
                    if (cmsContentNewsDao.updateByContentID(cmsContentNews) <= 0) {
                        throw new Exception();
                    }
                }
            }

            result = getContent(content.getContentID());
        } catch (Exception e) {
            logger.fatal(e);
        }
        return result;
    }

    @Override
    public int getUserContentCount(Integer userID, String contentType) {
        try {
            int result = cmsContentDao.selectCountByUserID(userID, contentType);
            return result;
        } catch (Exception e) {
            logger.fatal(e);
            throw e;
        }
    }

    @Override
    public int getPVCountSum() {
        try {
            int result = cmsContentDao.selectPVCountSum();
            return result;
        } catch (Exception e) {
            logger.fatal(e);
            throw e;
        }
    }

    @Override
    public void updateViewCount(Integer contentID, int pvCount, int uvCount) {
        try {
            cmsContentDao.updateViewCount(contentID, pvCount, uvCount);
        } catch (Exception e) {
            logger.fatal(e);
        }
    }

    @Override
    public void updateCommentCount(Integer contentID) {
        try {
            int commentCount = cmsCommentMapper.selectCommentCount(contentID);
            cmsContentDao.updateCommentCount(contentID, commentCount);
        } catch (Exception e) {
            logger.fatal(e);
        }
    }

    @Override
    public int getContentStarCount(Integer contentID) {
        try {
            return cmsContentDao.selectStarCount(contentID);

        } catch (Exception e) {
            e.printStackTrace();
            logger.fatal(e);
            return 0;
        }
    }

    private long getRemainTime(String contentType, Date createTime) {
        try {
            // 获得内容显示过期天数的配置字典项
            String dictClassCode = CmsDictContant.DICT_CLASS_CMS_PROPERTIES;
            String dictItemCode = "";
            if (contentType.equals(ContentType.News.getCode())) {
                dictItemCode = CmsDictContant.DICT_ITEM_CONTENT_TYPE_2_EXPIRE_DAY;
            } else {
                dictItemCode = CmsDictContant.DICT_ITEM_CONTENT_TYPE_1_EXPIRE_DAY;
            }
            DictItem expireDictItem = dictItemMapper.selectByClassItemCode(dictClassCode, dictItemCode);
            Integer expireDay = Integer.parseInt(expireDictItem.getDictItemValue());

            long result = Long.MAX_VALUE;
            if (expireDay > 0) {
                Date expireDate = DateUtil.getDate(createTime, expireDay);
                result = DateUtil.subtract(expireDate, new Date());
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.fatal(e);
            return 0;
        }
    }

    @Override
    @Transactional
    public boolean deleteContent(CmsContent content) {
        try {
            if (cmsContentDao.deleteByContentID(content) <= 0) {
                throw new Exception();
            } else {
                cmsPictureMapper.deleteByContentID(content);
                cmsFileMapper.deleteByContentID(content);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.fatal(e);
            return false;
        }
    }

}
