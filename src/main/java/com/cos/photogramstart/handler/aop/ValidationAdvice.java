 package com.cos.photogramstart.handler.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;

@Component
@Aspect
public class ValidationAdvice {

	@Around("execution(* com.cos.photogramstart.web.api.*Controller.*(..))")
	public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		System.out.println("wep api 컨트롤러==========================================");
		
		Object[] args = proceedingJoinPoint.getArgs();
		
		for(Object arg : args) {
			if(arg instanceof BindingResult) {
				System.out.println("유효성 검사하는 함수입니다.");
				
				BindingResult bindingResult = (BindingResult)arg;
				
				if(bindingResult.hasErrors()) {	
					Map<String, String> errorMap = new HashMap<>();
					
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
						System.out.println("------------------------------------------");
						System.out.println(error.getDefaultMessage());
						System.out.println("------------------------------------------");
					}
					throw new CustomValidationApiException("유효성 검사 실패", errorMap);
				}
			}
		}
		// proceedingJoinPoint => profile 함수의 모든 곳에 접근할 수 있는 변수
		// profile 함수보다 먼저 실행
		
		return proceedingJoinPoint.proceed(); // profile 함수가 실행됨
	}
	
	@Around("execution(* com.cos.photogramstart.web.*Controller.*(..))")
	public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
		System.out.println("wep 컨트롤러==========================================");
		
		Object[] args = proceedingJoinPoint.getArgs();
		
		for(Object arg : args) {
			if(arg instanceof BindingResult) {
				System.out.println("유효성 검사하는 함수입니다.");
				
				BindingResult bindingResult = (BindingResult)arg;
				
				if(bindingResult.hasErrors()) {	
					Map<String, String> errorMap = new HashMap<>();
					
					for(FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
						System.out.println("------------------------------------------");
						System.out.println(error.getDefaultMessage());
						System.out.println("------------------------------------------");
					}
					throw new CustomValidationException("유효성 검사 실패", errorMap);
				}
			}
		}
		
		return proceedingJoinPoint.proceed();
	}
}
