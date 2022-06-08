package com.board.aop;

import java.util.Collections;
import java.util.List;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Configuration
public class TransactionAspect {

	@Autowired
	private TransactionManager transactionManager;	//DBConfiguration 클래스에 빈(Bean)으로 등록했던 객체

	private static final String EXPRESSION = "execution(* com.board..service.*Impl.*(..))";	//포인트컷. 서비스임플의 모든 메서드에서 실행

	@Bean
	public TransactionInterceptor transactionAdvice() {

		//롤백을 수행하는 규칙들이 담긴 배열.  인자로 Exception클래스를 줬기 때문에 모든 예외에 대해서 무조건 롤백 실행
		List<RollbackRuleAttribute> rollbackRules = Collections.singletonList(new RollbackRuleAttribute(Exception.class));

		RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();
		transactionAttribute.setRollbackRules(rollbackRules);	//트랜잭션요소를 생성하고 롤백규칙들을 세팅
		transactionAttribute.setName("*"); //트랜잭션의 이름 설정

		MatchAlwaysTransactionAttributeSource attributeSource = new MatchAlwaysTransactionAttributeSource();
		attributeSource.setTransactionAttribute(transactionAttribute);

		return new TransactionInterceptor(transactionManager, attributeSource);

	}

	@Bean
	public Advisor transactionAdvisor() {

		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(EXPRESSION);

		return new DefaultPointcutAdvisor(pointcut, transactionAdvice());
	}

}