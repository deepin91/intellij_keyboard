package interceptor;
@Slf4j
public class LoggerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.debug("-------- START --------"); //예외처리 부분
        log.debug("Request URL : " + request.getRequestURI()); //해당하는 부분이 실행 될 때 그에 대한 응답과, URI를 가져와서 로그로 출력
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception{
        log.debug("-------- END --------");
    }
}
