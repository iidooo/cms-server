package com.iidooo.cms.model.po;

public class CmsContentFile extends CmsContent{
    private Integer contentID;

    private String fileURL;

    public Integer getContentID() {
        return contentID;
    }

    public void setContentID(Integer contentID) {
        this.contentID = contentID;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL == null ? null : fileURL.trim();
    }
}