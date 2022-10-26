package com.gradeBook.interceptor;

import com.gradeBook.entity.AccessLevel;
import com.gradeBook.entity.Token;
import com.gradeBook.service.TokenService;
import com.gradeBook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class GradeBookInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest requestServlet, HttpServletResponse responseServlet, Object handler) throws Exception {
        Token token = tokenService.findTokenByString(requestServlet.getParameter("token"));
        if (token == null || !token.isValid()) {
            requestServlet.setAttribute("token", token);
            requestServlet.setAttribute("level", AccessLevel.LEVEL.BASIC);
        } else {
            requestServlet.setAttribute("level", token.getUser().getAccessLevel().getLevel());
            requestServlet.setAttribute("token", token);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //  System.out.println("MINIMAL: INTERCEPTOR POSTHANDLE CALLED");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
        //   System.out.println("MINIMAL: INTERCEPTOR AFTERCOMPLETION CALLED");
    }

}