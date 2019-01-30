package com.contactsImprove.entity.admin;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.contactsImprove.utils.PageUtil;

public class Role extends PageUtil{
	
    private Long id;

    private String roleName;

    private String description;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    
    private List<ResourcesUrl> resourcesUrls;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public List<ResourcesUrl> getResourcesUrls() {
		return resourcesUrls;
	}

	public void setResourcesUrls(List<ResourcesUrl> resourcesUrls) {
		this.resourcesUrls = resourcesUrls;
	}
}