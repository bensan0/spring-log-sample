package com.example.springlogsample.aopsample.aspect;

import com.example.springlogsample.aopsample.anno.Log;
import com.example.springlogsample.model.WebLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Aspect
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    /**
     * web層切點
     * 1. @Pointcut("execution(public * com.hyh.web.*.*(..))")  web層的所有方法
     * 2. @Pointcut("@annotation(com.hyh.annotation.Log)")      Log註解標註的方法
     */

    @Pointcut("@annotation(com.example.springlogsample.aopsample.anno.Log)")
    public void webLog() {
    }

    @Before("webLog()")
    public void before(){
        log.info("=================before通知=====================");
    }

    @After("webLog()")
    public void after(){
        log.info("=================after通知=====================");
    }

    @AfterReturning(value = "webLog()", returning = "result")
    public void afterReturning(Object result){
        log.info("=================afterReturning通知=====================" + result);
    }

    @AfterThrowing(value = "webLog()", throwing = "e")
    public void afterThrowing(Throwable e){
        log.info("=================afterThrowing通知=====================" + e.getMessage());
    }

    /**
     * 環繞通知
     */
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //獲取請求物件
        HttpServletRequest request = getRequest();
        WebLog webLog = new WebLog();
        Object result = null;
        try {
            log.info("=================前置通知=====================");
            long start = System.currentTimeMillis();
            result = joinPoint.proceed();
            log.info("=================返回通知=====================");
            long timeCost = System.currentTimeMillis() - start;
            // 獲取Log註解
            Log logAnnotation = getAnnotation(joinPoint);
            // 封裝webLog物件
            webLog.setMethodType(logAnnotation.methodType().name());
            webLog.setDescription(logAnnotation.description());
            webLog.setTimeCost((int) timeCost);
            webLog.setStartTime(start);
            webLog.setIpAddress(request.getRemoteAddr());
            webLog.setHttpMethod(request.getMethod());
            webLog.setParams(getParams(joinPoint));
            webLog.setResult(result);
            webLog.setUri(request.getRequestURI());
            webLog.setUrl(request.getRequestURL().toString());
            log.info(webLog.toString());
        } catch (Throwable e) {
            log.info("==================異常通知=====================");
            log.error(e.getMessage());
            throw new Throwable(e);
        }finally {
            log.info("=================後置通知=====================");
        }
        return result;
    }

    /**
     * 獲取方法上的註解
     */
    private Log getAnnotation(ProceedingJoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        return method.getAnnotation(Log.class);
    }


    /**
     * 獲取引數 params:{"name":"天喬巴夏"}
     */
    private Object getParams(ProceedingJoinPoint joinPoint) {
        // 引數名
        String[] paramNames = getMethodSignature(joinPoint).getParameterNames();
        // 引數值
        Object[] paramValues = joinPoint.getArgs();
        // 儲存引數
        Map<String, Object> params = new LinkedHashMap<>();
        for (int i = 0; i < paramNames.length; i++) {
            Object value = paramValues[i];
            // MultipartFile物件以檔名作為引數值
            if (value instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) value;
                value = file.getOriginalFilename();
            }
            params.put(paramNames[i], value);
        }
        return params;
    }

    private MethodSignature getMethodSignature(ProceedingJoinPoint joinPoint) {
        return (MethodSignature) joinPoint.getSignature();
    }


    private HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

}
