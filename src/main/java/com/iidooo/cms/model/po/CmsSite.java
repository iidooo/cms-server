package com.iidooo.cms.model.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.iidooo.core.model.po.SecurityUser;

public class CmsSite {
    private Integer siteID;

    private String siteCode;

    private String siteName;

    private String siteURL;

    private String remarks;

    private Date updateTime;

    private Integer updateUserID;

    private Date createTime;

    private Integer createUserID;
    
    private SecurityUser createUser;

    private Integer isDelete;

    private Integer version;

    private List<CmsSiteUser> ownerList;
    
    public CmsSite(){
        this.ownerList = new ArrayList<CmsSiteUser>();
    }
    
    public Integer getSiteID() {
        return siteID;
    }

    public void setSiteID(Integer siteID) {
        this.siteID = siteID;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode == null ? null : siteCode.trim();
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName == null ? null : siteName.trim();
    }

    public String getSiteURL() {
        return siteURL;
    }

    public void setSiteURL(String siteURL) {
        this.siteURL = siteURL == null ? null : siteURL.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUpdateUserID() {
        return updateUserID;
    }

    public void setUpdateUserID(Integer updateUserID) {
        this.updateUserID = updateUserID;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreateUserID() {
        return createUserID;
    }

    public void setCreateUserID(Integer createUserID) {
        this.createUserID = createUserID;
    }
    
    public SecurityUser getCreateUser() {
        return createUser;
    }

    public void setCreateUser(SecurityUser createUser) {
        this.createUser = createUser;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<CmsSiteUser> getOwnerList() {
        return ownerList;
    }

    public void setOwnerList(List<CmsSiteUser> ownerList) {
        this.ownerList = ownerList;
    } 
    
}