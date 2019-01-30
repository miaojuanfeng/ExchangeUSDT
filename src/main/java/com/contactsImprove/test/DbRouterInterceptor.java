package com.contactsImprove.test;

import java.lang.reflect.Method;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.contactsImprove.utils.LoggerUtil;

@Aspect
@Component
public class DbRouterInterceptor {
	
	@Pointcut("@annotation( com.contactsImprove.test.Router)")
	public void aopPoint() {
		
	}
	@Before("aopPoint()")
	public Object doRoute(JoinPoint jp) throws Throwable{		
		Method method = ((MethodSignature)jp.getSignature()).getMethod();
        Router router = method.getAnnotation(Router.class);
        String routeField = router.routerField();
        Object[] args = jp.getArgs();
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                long t2 = System.currentTimeMillis();
                //通过反射得到对象args[i] 的 routeField 字段的值
//                String routeFieldValue = BeanUtils.getProperty(args[i],
//                        routeField);
                LoggerUtil.info("routeFieldValue{}" + (System.currentTimeMillis() - t2));
/*                if (StringUtils.isNotEmpty(routeFieldValue)) {
                    //看这个值是否为默认的分库分表字段，如果是设置库和表的名称
//                    if (RouterConstants.ROUTER_FIELD_DEFAULT.equals(routeField)) {
//                        //根据hashcode取%
//                        dBRouter.doRouteByResource("" + RouterUtils.getResourceCode(routeFieldValue));
//                        break;
//                    } 
                }*/
            }
        }

		return true;
	}
/**
 * JoinPoint
 * java.lang.Object[] getArgs()：获取连接点方法运行时的入参列表； 
 Signature getSignature() ：获取连接点的方法签名对象； 
 java.lang.Object getTarget() ：获取连接点所在的目标对象； 
 java.lang.Object getThis() ：获取代理对象本身；
 * 2)ProceedingJoinPoint 
	ProceedingJoinPoint继承JoinPoint子接口，它新增了两个用于执行连接点方法的方法： 
 java.lang.Object proceed() throws java.lang.Throwable：通过反射执行目标对象的连接点处的方法； 

 Java.lang.Object proceed(java.lang.Object[] args) throws java.lang.Throwable：通过反射执行目标对象连接点处的方法，不过使用新的入参替换原来的入参。 
 */
	private Method getMethod(JoinPoint jp) throws NoSuchMethodException {
        Signature sig = jp.getSignature();
        MethodSignature msig = (MethodSignature) sig;
        return getClass(jp).getMethod(msig.getName(), msig.getParameterTypes());
    }

    private Class<? extends Object> getClass(JoinPoint jp)throws NoSuchMethodException {
        return jp.getTarget().getClass();
    }

	
}
