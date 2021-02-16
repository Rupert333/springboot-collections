package com.example.springbootaop2.config;

import com.example.springbootaop2.safeLimit.RequestLimit;
import com.example.springbootaop2.safeLimit.RequestLimitException;
import com.example.springbootaop2.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Title: 限制同一IP单位时间内访问次数,
 *
 * @author yangyuegang
 * @create: 2021/2/4
 */
@Aspect
@Component
@Slf4j
public class ApiRequestLimit {
    private static final Logger logger = LoggerFactory.getLogger(ApiRequestLimit.class);

    @Resource
    private RedisUtil RedisUtil;
    private static String limitPath = "/safeLimit/limit";

    /**
     * 切入点
     */
    @Pointcut("execution(public * *..*controller.*.*(..))")
    public void pointCut() {
    }


//    @Before("within(@com.example.springbootaop2.controller.*controller *) && @annotation(limit)")
    @Before("pointCut()&& @annotation(limit)")
    public void requestLimit(final JoinPoint joinPoint, RequestLimit limit) throws RequestLimitException {
        log.info("进入了切点前操作");
        try {
            Object[] args = joinPoint.getArgs();
            HttpServletRequest request = null;
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = servletRequestAttributes.getResponse();
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof HttpServletRequest) {
                    request = (HttpServletRequest) args[i];
                    break;
                }
            }
            if (request == null) {
                throw new RequestLimitException("方法中缺失HttpServletRequest参数");
            }
            String ip = request.getLocalAddr();
            String url = request.getRequestURL().toString();
            String key = "req_limit_".concat(url).concat(ip);
            if (!RedisUtil.hasKey(key) || StringUtils.isEmpty(RedisUtil.get(key))) {
                RedisUtil.set(key, String.valueOf(1));
            } else {
                Integer getValue = Integer.parseInt(String.valueOf(RedisUtil.get(key))) + 1;
                RedisUtil.set(key, String.valueOf(getValue));
            }
            int count = Integer.parseInt(String.valueOf(RedisUtil.get(key)));
            if (count > 0) {
                //创建一个定时器
                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        RedisUtil.del(key);
                    }
                };
                //这个定时器设定在time规定的时间之后会执行上面的remove方法，也就是说在这个时间后它可以重新访问
                timer.schedule(timerTask, limit.time());
            }
            if (count > limit.count()) {
                log.info("用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数[" + limit.count() + "]");
//                throw new RequestLimitException();
                String toLomitPath = "http://" + request.getServerName() + ":" + request.getServerPort() + limitPath;   //端口号
                response.sendRedirect(toLomitPath);
            }
        } catch (RequestLimitException e) {
            throw e;
        } catch (Exception e) {
            logger.error("发生异常", e);
        }
    }
}
