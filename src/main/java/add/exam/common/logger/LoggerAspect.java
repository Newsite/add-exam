package add.exam.common.logger;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging methods
 */
@Aspect
@Component
public class LoggerAspect {

	private static final Logger LOG = Logger.getLogger(LoggerAspect.class);

	private static final String RETURN_FORMAT_STRING = "%s : %s return %s";
	private static final String INVOKE_FORMAT_STRING = "%s : invoke in %s";

	@Pointcut("execution(* add.exam..*.*(..))")
	public void log() {
	}

	@Pointcut("execution(public * *(..))")
	private void publicOperation() {
	}

	/**
	 * Logging all public methods in application
	 * 
	 * @param joinPoint
     *          join point
	 * @throws Throwable
	 */
	@Around("log() && publicOperation()")
	public Object logWebServiceCall(ProceedingJoinPoint joinPoint) throws Throwable {
		String className = joinPoint.getSignature().getDeclaringTypeName();
		String methodName = joinPoint.getSignature().getName();
		LOG.debug(String.format(INVOKE_FORMAT_STRING, className, methodName));
		Object result = joinPoint.proceed();
		LOG.debug(String.format(RETURN_FORMAT_STRING, className, methodName, result));
		return result;
	}

}
