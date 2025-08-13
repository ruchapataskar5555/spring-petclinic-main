package org.springframework.samples.petclinic.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

	// 🔹 Pointcut for REST controllers
	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	public void restControllerMethods() {
	}

	// 🔹 Pointcut for service classes
	@Pointcut("within(@org.springframework.stereotype.Service *)")
	public void serviceMethods() {
	}

	// 🔹 Around advice for REST
	@Around("restControllerMethods()")
	public Object logRestController(ProceedingJoinPoint joinPoint) throws Throwable {
		return logMethod(joinPoint, "REST");
	}

	// 🔹 Around advice for services
	@Around("serviceMethods()")
	public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
		return logMethod(joinPoint, "SERVICE");
	}

	// 🔹 Common method
	private Object logMethod(ProceedingJoinPoint joinPoint, String layer) throws Throwable {
		long start = System.currentTimeMillis();
		String className = joinPoint.getSignature().getDeclaringTypeName();
		String methodName = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();

		log.info("[{}] --> Entering: {}.{}() with args = {}", layer, className, methodName, args);

		try {
			Object result = joinPoint.proceed();
			long timeTaken = System.currentTimeMillis() - start;
			log.info("[{}] <-- Exiting: {}.{}() with result = {} ({} ms)", layer, className, methodName, result,
					timeTaken);
			return result;
		}
		catch (Throwable ex) {
			log.error("[{}] !! Exception in {}.{}(): {} - {}", layer, className, methodName,
					ex.getClass().getSimpleName(), ex.getMessage(), ex);
			throw ex; // rethrow to maintain flow
		}
	}

}
