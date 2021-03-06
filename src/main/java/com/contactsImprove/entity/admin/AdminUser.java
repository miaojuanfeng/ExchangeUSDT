package com.contactsImprove.entity.admin;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.contactsImprove.utils.PageUtil;

public class AdminUser extends PageUtil{
	
    private Long id;

    private Long powerId;

    private String userName;

    private String password;

    private String name;

    private String phone;

    private Integer isValid;

    private Date createTime;

    private Date updateTime;

    private Date loginTime;

    private String ip;
    
    private ResourcesUrl resourcesUrl;
    
    private Powers powers;
    
    private List<Powers> lpowers;
    
    private  Set<String> urls;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPowerId() {
        return powerId;
    }

    public void setPowerId(Long powerId) {
        this.powerId = powerId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

	public ResourcesUrl getResourcesUrl() {
		return resourcesUrl;
	}

	public void setResourcesUrl(ResourcesUrl resourcesUrl) {
		this.resourcesUrl = resourcesUrl;
	}

	public Powers getPowers() {
		return powers;
	}

	public void setPowers(Powers powers) {
		this.powers = powers;
	}

	public List<Powers> getLpowers() {
		return lpowers;
	}

	public void setLpowers(List<Powers> lpowers) {
		this.lpowers = lpowers;
	}

	public Set<String> getUrls() {
		return urls;
	}

	public void setUrls(Set<String> urls) {
		this.urls = urls;
	}
}