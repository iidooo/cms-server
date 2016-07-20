package com.iidooo.cms.service;

import java.util.List;

import com.iidooo.cms.model.po.CmsPicture;

public interface PictureService {
    CmsPicture getPicture(Integer pictureID);
    
    List<CmsPicture> getPictures(Integer contentID);
    
    CmsPicture createPicture(CmsPicture picture);
    
    CmsPicture updatePicture(CmsPicture picture);
    
    boolean deletePicture(CmsPicture picture);
}
