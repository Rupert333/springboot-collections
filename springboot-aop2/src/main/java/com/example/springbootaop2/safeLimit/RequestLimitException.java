package com.example.springbootaop2.safeLimit;

/**
 * Title: 访问异常处理类,
 *
 * @author yangyuegang
 * @create: 2021/2/4
 */
public class RequestLimitException extends Exception{
    private static final long serialVersionUID = 1555967171104727461L;

    public RequestLimitException(){
        super("HTTP请求超出设定的限制");
    }

    public RequestLimitException(String message){
        super(message);
    }
}
