package com.revature.aspects;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.WebSession;

import com.revature.beans.User;
import com.revature.beans.UserType;

import reactor.core.publisher.Mono;

@Component
@Aspect
public class AuthenticationAspect {
	private Logger log = LogManager.getLogger(AuthenticationAspect.class);

	@Around("loginMonoHook()")
	public Object checkLoggedInMono(ProceedingJoinPoint pjp) throws Throwable {
		log.trace("Checking if user is logged in");

		if (Boolean.FALSE.equals(isLoggedIn(pjp.getArgs()))) {
			return Mono.just(ResponseEntity.status(401).build());
		}
		return pjp.proceed();
	}

	@Around("loginFluxHook()")
	public Object checkLoggedInFlux(ProceedingJoinPoint pjp) throws Throwable {
		log.trace("Checking if user is logged in");

		if (Boolean.FALSE.equals(isLoggedIn(pjp.getArgs()))) {
			return ResponseEntity.status(401).build();
		}
		return pjp.proceed();
	}

	@Around("checkVacationerHook()")
	public Object checkVacationer(ProceedingJoinPoint pjp) throws Throwable {
		log.trace("Checking to see if the user is a vacationer");

		WebSession session = (WebSession) Stream.of(pjp.getArgs()).filter(WebSession.class::isInstance).findFirst()
				.orElse(null);

		if (session == null) {
			return Mono.just(ResponseEntity.status(401).build());
		}

		User loggedUser = session.getAttribute("loggedUser");
		log.debug("Logged In User: " + loggedUser);
		String username = null;

		// Get the method signature
		MethodSignature sig = (MethodSignature) pjp.getStaticPart().getSignature();

		// Get the method
		Method method = sig.getMethod();

		// Get all annotated parameters
		Annotation[][] paramAnnotations = method.getParameterAnnotations();

		// Loop through annotated arguments
		for (int i = 0; i < paramAnnotations.length; i++) {
			// Loop through annotated arguments
			for (Annotation annotate : paramAnnotations[i]) {
				// If the annotation isn't a path variable, continue
				if (!(annotate instanceof PathVariable)) {
					continue;
				}
				
				// Set the path variable
				PathVariable pathVariable = (PathVariable) annotate;
				
				// Make sure 
				if ("username".equals(pathVariable.value())) {
					// Set the username to the parameter location
					username = (String) pjp.getArgs()[i];
					log.debug("Username found: " + username);
				}

			}
		}

		// If the logged in user is not the same user specified or is not a vacationer
		if (loggedUser == null || username == null || !username.equals(loggedUser.getUsername())
				|| !UserType.VACATIONER.equals(loggedUser.getType())) {
			return Mono.just(ResponseEntity.status(403).build());
		}

		return pjp.proceed();
	}

	private Boolean isLoggedIn(Object[] args) {

		WebSession session = (WebSession) Stream.of(args).filter(WebSession.class::isInstance).findFirst().orElse(null);
		if (session == null) {
			return false;
		}
		User loggedUser = (User) session.getAttribute("loggedUser");
		log.debug("Logged In user: " + loggedUser);

		return loggedUser != null;
	}

	@Pointcut("@annotation(com.revature.aspects.LoggedInMono)")
	private void loginMonoHook() {
		/* Empty */}

	@Pointcut("@annotation(com.revature.aspects.LoggedInFlux)")
	private void loginFluxHook() {
		/* Empty */}

	@Pointcut("@annotation(com.revature.aspects.VacationerCheck)")
	private void checkVacationerHook() {
		/* Empty */
	}
}
