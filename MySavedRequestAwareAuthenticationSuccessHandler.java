package com.mpa.service.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

public class MySavedRequestAwareAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	@Autowired
    RequestCache requestCache;
   
	   @Override
	   	public void onAuthenticationSuccess(HttpServletRequest arg0,HttpServletResponse arg1,Authentication authentication) throws ServletException, IOException 
	       {
	
	         SavedRequest savedrequest=requestCache.getRequest(arg0, arg1);
	         if(savedrequest==null){
	       	  clearAuthenticationAttributes(arg0);
	         }

	         String targetUrlParam = getTargetUrlParameter();
	         if (isAlwaysUseDefaultTargetUrl()|| (targetUrlParam != null && StringUtils.hasText(arg0.getParameter(targetUrlParam)))) {
	        	 requestCache.removeRequest(arg0, arg1);
	        	 clearAuthenticationAttributes(arg0);
	        	 return;
	         }
            clearAuthenticationAttributes(arg0);

	   }

}


