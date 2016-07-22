package com.iidooo.cms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iidooo.cms.mapper.CmsPictureMapper;
import com.iidooo.cms.model.po.CmsPicture;
import com.iidooo.cms.service.PictureService;

@Service
public class PictureServiceImpl implements PictureService {

    private static final Logger logger = Logger.getLogger(PictureServiceImpl.class);

    @Autowired
    private CmsPictureMapper pictureMapper;    
    
    @Override
    public CmsPicture getPicture(Integer pictureID) {
        CmsPicture result = null;        
        try {
            result = pictureMapper.selectByPictureID(pictureID);
        } catch (Exception e) {
            logger.fatal(e);
        }        
        return result;
    }

    @Override
    public List<CmsPicture> getPictureList(Integer contentID) {
        List<CmsPicture> result = new ArrayList<CmsPicture>();
        try {
            result = pictureMapper.selectByContentID(contentID);
        } catch (Exception e) {
            logger.fatal(e);
        }
        return result;
    }

    @Override
    public CmsPicture createPicture(CmsPicture picture) {
        CmsPicture result = null;

        try {
            if (pictureMapper.insert(picture) <= 0) {
                throw new Exception();
            }

            result = pictureMapper.selectByPictureID(picture.getPictureID());
        } catch (Exception e) {
            logger.fatal(e);
        }

        return result;
    }

    @Override
    public CmsPicture updatePicture(CmsPicture picture) {
        CmsPicture result = null;

        try {
            if (pictureMapper.updateByPictureID(picture) <= 0) {
                throw new Exception();
            }

            result = pictureMapper.selectByPictureID(picture.getPictureID());
        } catch (Exception e) {
            logger.fatal(e);
        }

        return result;
    }

    @Override
    public boolean deletePicture(CmsPicture picture) {
        try {
            if (pictureMapper.deleteByPictureID(picture) <= 0) {
                throw new Exception();
            }
            return true;
        } catch (Exception e) {
            logger.fatal(e);
            return false;
        }
    }

}
