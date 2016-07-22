package com.iidooo.cms.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iidooo.cms.mapper.CmsFileMapper;
import com.iidooo.cms.model.po.CmsFile;
import com.iidooo.cms.service.FileService;

@Service
public class FileServiceImpl implements FileService {
    private static final Logger logger = Logger.getLogger(FileServiceImpl.class);

    @Autowired
    private CmsFileMapper fileMapper;

    @Override
    public CmsFile getFile(Integer fileID) {
        CmsFile result = null;
        try {
            result = fileMapper.selectByFileID(fileID);
        } catch (Exception e) {
            logger.fatal(e);
        }
        return result;
    }

    @Override
    public List<CmsFile> getFileList(Integer contentID) {
        List<CmsFile> result = new ArrayList<CmsFile>();
        try {
            result = fileMapper.selectByContentID(contentID);
        } catch (Exception e) {
            logger.fatal(e);
        }
        return result;
    }

    @Override
    public CmsFile createFile(CmsFile file) {
        CmsFile result = null;        
        try {
            if(fileMapper.insert(file) <= 0){
                throw new Exception();
            }
            result = fileMapper.selectByFileID(file.getFileID());
        } catch (Exception e) {
            logger.fatal(e);
        }        
        return result;
    }

    @Override
    public CmsFile updateFile(CmsFile file) {
        CmsFile result = null;        
        try {
            if(fileMapper.updateByFileID(file) <= 0){
                throw new Exception();
            }
            result = fileMapper.selectByFileID(file.getFileID());
        } catch (Exception e) {
            logger.fatal(e);
        }        
        return result;
    }

    @Override
    public boolean deleteFile(CmsFile file) {
        try {
            if (fileMapper.deleteByFileID(file) <= 0) {
                throw new Exception();
            }
            return true;
        } catch (Exception e) {
            logger.fatal(e);
            return false;
        }
    }

}
