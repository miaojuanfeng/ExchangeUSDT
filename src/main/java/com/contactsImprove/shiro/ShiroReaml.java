package com.contactsImprove.shiro;

import java.util.Map;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import com.contactsImprove.entity.admin.AdminUser;
import com.contactsImprove.service.admin.AdminUserService;

public class ShiroReaml extends AuthorizingRealm {

	@Autowired
	private AdminUserService adminUserService;

	/**
	 * 权限认证
	 */
	@Override
	public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
		AdminUser user = (AdminUser) pc.getPrimaryPrincipal();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(user.getUrls());
		return info;
	}

	/**
	 * 登录认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authenticationToken)
			throws AuthenticationException {

		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
		AdminUser adminUser = new AdminUser();
		adminUser.setUserName(token.getUsername());
		AdminUser user = adminUserService.selectByUserName(adminUser);
		if (user == null) {
			throw new UnknownAccountException();
		}

		Map<String, Set<String>> resultMap = adminUserService.selectResourceMapByUserId(user.getPowerId());
		user.setUrls(resultMap.get("urls"));
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
				user , user.getPassword(), this.getName());
		return authenticationInfo;
	}
}
