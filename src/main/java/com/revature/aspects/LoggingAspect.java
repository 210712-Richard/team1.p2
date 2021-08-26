package com.revature.aspects;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {
	@Around("everything()")
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public Object log(ProceedingJoinPoint pjp) throws Throwable {
		Object result = null;
		
		//Get the log for the class
		Logger log = LogManager.getLogger(pjp.getTarget().getClass()); 
		log.trace("Method with signature: {}", pjp.getSignature());
		log.trace("With arguments: {}", Arrays.toString(pjp.getArgs()));
		try {
			result = pjp.proceed();
		} catch (Throwable t) {
			//Method used to log errors
			logError(log, t);
			throw t; 
		}
		log.trace("Method returning with: {}", result);
		return result;
	}

	private void logError(Logger log, Throwable t) {
		//Log the error
		log.error("Method threw exception: {}", t);
		//Loop through and log the stack trace
		for (StackTraceElement s : t.getStackTrace()) {
			log.warn(s);
		}
		//If the error is wrapped, continue logging the error
		if (t.getCause() != null) {
			logError(log, t.getCause());
		}
	}

	@Pointcut("execution( * com.revature..*(..) )")
	private void everything() {
		/* empty method for hook */}
}
