package tp.appliSpring.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Aspect  
@Component
@Profile("perf") //penser à @EnableAspectJAutoProxy sur une des classes de config
                 // et à System.setProperty("spring.profiles.active", "perf"); ou équivalent ( @ActiveProfiles({ "perf" }) sur Test)
public class MyPerfLogAspect {
	
	public MyPerfLogAspect(){
		//System.out.println("MyPerfLogAspect");
	}
	
	@Pointcut("execution(* tp.appliSpring.exemple.*.*(..))")
	public void exemplePointcut(){ 
	}
	//@within pour annotation @Aff (avec @Target(ElementType.TYPE) placée sur l'ensemble d'une classe 
	@Pointcut("@within(tp.appliSpring.annotation.Aff)")
	public void annotAffPointcut(){ 
	} 
	
	//@within pour annotation @LogExecutionTime (avec @Target(ElementType.METHOD) placée sur une méthode 
	@Pointcut("@annotation(tp.appliSpring.annotation.LogExecutionTime)")
	public void annotLogExecutionTimePointcut(){ 
	}
	
	@Pointcut("execution(* tp.appliSpring.core.service.*.*(..))")
	public void servicePointcut(){ 
	}
	
	@Pointcut("execution(* tp.appliSpring.core.dao.*.*(..))")
	public void daoPointcut(){ 
	}
	
	@AfterThrowing (pointcut = "servicePointcut()", throwing = "ex" )
    public void logAfterThrowingAllServiceMethodsAdvice(JoinPoint  jp , Exception ex ) throws Throwable 
    {
        System.out.println("**** LoggingAspect" + ex + " in "  +jp.getSignature().toLongString());
    }
	
	@Before ("servicePointcut()")
    public void beforeServiceMethodsAdvice(JoinPoint  jp) throws Throwable 
    {
        System.out.print("**** beforeServiceMethodsAdviceLoggingAspect: " +jp.getSignature().toLongString()
        		+ "was called with args= "); 
        for(Object a : jp.getArgs()) { System.out.print(" " + a ); }
        System.out.print("\n");
    }
	
	@AfterReturning (pointcut = "servicePointcut()" , returning = "res")
    public void logAfterReturningServiceMethodsAdvice(JoinPoint  jp , Object res) throws Throwable 
    {
        System.out.println("**** logAfterReturningServiceMethodsAdvice : returned value = " + res + " after execution of "  +jp.getSignature().toLongString());
    }
	
	@After ("servicePointcut()")
    public void logAfterServiceMethodsAdvice(JoinPoint  jp) throws Throwable 
    {
        System.out.println("**** logAfterServiceMethodsAdvice: "   +jp.getSignature().toLongString());
        //with no details/infos on result or exception
    }
	
	/*
	 * ProceedingJoinPoint is an extension of ProceedingJoinPoint with .proceed() additional method()
	 */
	
	//@Around("annotAffPointcut()")
	//@Around("exemplePointcut() && annotAffPointcut()")
	//@Around("annotLogExecutionTimePointcut()")
	@Around("servicePointcut() || daoPointcut()")
	public Object doPerfLogAdvice(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("<< trace == debut == " + pjp.getSignature().toLongString() + " <<");
		long td = System.nanoTime();
		Object objRes = pjp.proceed();
		//Object objRes = pjp.proceed(pjp.getArgs());
		long tf = System.nanoTime();
		System.out.println(
				">> trace == fin == " + pjp.getSignature().toShortString() + " [" + (tf - td) / 1000000.0 + " ms] >>");
		return objRes;
	}
}
