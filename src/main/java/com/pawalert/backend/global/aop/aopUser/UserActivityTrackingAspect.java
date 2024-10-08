package com.pawalert.backend.global.aop.aopUser;

import com.pawalert.backend.global.config.redis.RedisService;
import com.pawalert.backend.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class UserActivityTrackingAspect {
    private final RedisService redisService;

    //UserService 클래스 내의 모든 메서드에 AOP 적용
    @Around("execution(* com.pawalert.backend.domain.user.service.LoginMemberUserService.*(..))")
    public Object trackUserActivity(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메소드 실행
        Object result = joinPoint.proceed();
        // IP 주소 가져오기
        String userIp = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest()
                .getRemoteAddr();

        // 메소드 이름
        String methodName = joinPoint.getSignature().getName();

        // 사용자 UI는 메소드 인자에서 가져올 수 있음
        String userUid = "undefined";
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof CustomUserDetails) {
                userUid = ((CustomUserDetails) arg).getUid();
                break;
            }
        }
        LocalDateTime now = LocalDateTime.now();

        redisService.trackUserActivity(userUid, userIp, methodName, now);

        return result;
    }


}
