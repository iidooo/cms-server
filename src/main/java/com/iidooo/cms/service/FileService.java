package com.iidooo.cms.service;

import java.util.List;

import com.iidooo.cms.model.po.CmsFile;

public interface FileService {
    CmsFile getFile(Integer fileID);

    List<CmsFile> getFileList(Integer contentID);

    CmsFile createFile(CmsFile file);

    CmsFile updateFile(CmsFile file);

    boolean deleteFile(CmsFile file);
}
