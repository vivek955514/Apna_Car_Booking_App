package com.vivekinfotech.apnacarbooking.config;

import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLogoutHandler implements LogoutHandler {

	@Override
	public void logout(HttpServletRequest req, HttpServletResponse response, @Nullable Authentication authentication) {

		ServletContext servletContext = req.getServletContext();

		servletContext.setAttribute("logout", true);

	}

}
