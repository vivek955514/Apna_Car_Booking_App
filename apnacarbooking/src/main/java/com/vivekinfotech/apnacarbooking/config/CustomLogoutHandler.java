package com.vivekinfotech.apnacarbooking.config;

import org.jspecify.annotations.Nullable;
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
