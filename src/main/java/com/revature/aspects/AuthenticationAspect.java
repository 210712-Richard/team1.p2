package com.revature.aspects;

import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebSession;

import com.revature.beans.User;

import reactor.core.publisher.Mono;

@Component
@Aspect
public class AuthenticationAspect {
	private Logger log = LogManager.getLogger(AuthenticationAspect.class);

	@Around("loginMonoHook()")
	public Object checkLoggedInMono(ProceedingJoinPoint pjp) throws Throwable {
		log.trace("Checking if user is logged in");

		if (!isLoggedIn(pjp.getArgs())) {
			return Mono.just(ResponseEntity.status(401).build());
		}
		return pjp.proceed();
	}
	
	@Around("loginFluxHook()")
	public Object checkLoggedInFlux(ProceedingJoinPoint pjp) throws Throwable {
		log.trace("Checking if user is logged in");

		if (!isLoggedIn(pjp.getArgs())) {
			return ResponseEntity.status(401).build();
		}
		return pjp.proceed();
	}

	private Boolean isLoggedIn(Object[] args) {

		WebSession session = (WebSession) Stream.of(args)
				.filter(o -> o instanceof WebSession)
				.findFirst()
				.orElse(null);
		if (session == null) {
			return false;
		}
		User loggedUser = (User) session.getAttribute("loggedUser");
		log.debug("Logged In user: " + loggedUser);
		if (loggedUser == null) {
			return false;
		}
		return true;
	}

	@Pointcut("@annotation(com.revature.aspects.LoggedInMono)")
	private void loginMonoHook() {
		/* Empty */}
	
	@Pointcut("@annotation(com.revature.aspects.LoggedInFlux)")
	private void loginFluxHook() {
		/* Empty */}
}
