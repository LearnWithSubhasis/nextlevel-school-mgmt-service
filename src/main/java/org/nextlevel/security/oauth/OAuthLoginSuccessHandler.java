package org.nextlevel.security.oauth;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nextlevel.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import org.nextlevel.user.UserService;

@Component
public class OAuthLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired UserService userService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
		String oauth2ClientName = oauth2User.getOauth2ClientName();
		String email = oauth2User.getEmail();

		Map<String, Object> userAttributes = oauth2User.getAttributes();
		String profileImageURL = "";
		String userName = "";
		for (String key: userAttributes.keySet()) {
			System.out.println(key + ":" + userAttributes.get(key));
			if(key.equalsIgnoreCase("Picture")) {
				profileImageURL = (String) userAttributes.get(key);
			} else if(key.equalsIgnoreCase("username")) {
				userName = (String) userAttributes.get(key);
			}
		}

		if (null == profileImageURL || profileImageURL.trim().length()==0) {
			profileImageURL = "images/profile_2.png";
		}

		if (null == userName || userName.trim().length() == 0) {
			userName = email;
		}

		//userService.updateAuthenticationType(username, oauth2ClientName);
		User user = new User();
		user.setOpauth2ClientName(oauth2ClientName);
		user.setUsername(userName);
		user.setEmail(email);
		user.setProfileImageURL(profileImageURL);

		try {
			userService.updateUserDetails(user);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		super.onAuthenticationSuccess(request, response, authentication);
	}

}
