package com.lmm.controller.interceptor;

import com.lmm.utils.IMoocJSONResult;
import com.lmm.utils.JsonUtils;
import com.lmm.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class MiniInterceptor implements HandlerInterceptor {
    @Autowired
    public RedisOperator redis;
    public static final String USER_REDIS_SESSION="user-redis-session";
    private Logger logger = LoggerFactory.getLogger(MiniInterceptor.class);
    /**
     * 拦截请求，在controller调用之前
     * @param request
     * @param response
     * @param o
     * @return
     * 返回false；请求被拦截，返回
     * 返回true，请求OK，可以继续执行，放行
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String userId = request.getHeader("userId");
        String userToken = request.getHeader("userToken");
        logger.info("拦截请求入参：userId:{},userToken:{}",userId,userToken);
        if (StringUtils.isNotBlank(userId)&&StringUtils.isNotBlank(userToken)){
            String uniqueToken = redis.get(USER_REDIS_SESSION+":"+userId);
            //session过期重新登录
            if (StringUtils.isEmpty(uniqueToken)&&StringUtils.isBlank(uniqueToken)){
                System.out.println("登录超时,请登录.....");
                returnErrorResponse(response,new IMoocJSONResult().errorTokenMsg("登录超时,请登录....."));
                return false;
            }else {
                //设置账号单点登录
                if (!uniqueToken.equals(userToken)){
                    System.out.println("账号在别的设备已经登录.....");
                    returnErrorResponse(response,new IMoocJSONResult().errorTokenMsg("账号在别的设备已经登录....."));
                    return false;
                }
            }
        }else {
            System.out.println("请登录.....");
            returnErrorResponse(response,new IMoocJSONResult().errorTokenMsg("请登录..."));
            return false;
        }
        return true;
    }

    /**
     * 请求controller之后，渲染视图之前
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 整个请求controller之后，视图渲染之后
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    /**
     * 错误的信息以对象抛出
     * @param response
     * @param result
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public void returnErrorResponse(HttpServletResponse response, IMoocJSONResult result)
            throws IOException, UnsupportedEncodingException {
        OutputStream out=null;
        try{
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            out = response.getOutputStream();
            out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
            out.flush();
        } finally{
            if(out!=null){
                out.close();
            }
        }
    }
}
