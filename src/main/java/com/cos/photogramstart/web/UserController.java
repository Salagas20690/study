package com.cos.photogramstart.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.service.UserService;
import com.cos.photogramstart.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {
	
	private final UserService userService;
	
	@GetMapping("/user/{pageUserId}")
	public String profile(@PathVariable int pageUserId, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		UserProfileDto dto = userService.userProfile(pageUserId, principalDetails.getUser().getId());
		model.addAttribute("dto", dto);
		
		return "user/profile";
	}
	
	//header.jsp <sec:authorize> 사용으로 model 사용 안해도됨
	@GetMapping("/user/{id}/update")
	public String update(@PathVariable int id, @AuthenticationPrincipal PrincipalDetails principalDetails, HttpServletRequest req /* , Model model */) {

		System.out.println(req.getRequestURI());
		// System.out.println("세션 정보 : " + principalDetails.getUser()); 양반향 매핑 User객체 Image 순환
		
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// PrincipalDetails mPrincipalDetails = (PrincipalDetails)auth.getPrincipal();
		
		// System.out.println("직접 찾은 세션 정보 : " + mPrincipalDetails.getUser());
		
		//model.addAttribute("principal", principalDetails.getUser());
		
		return "user/update";
	}
	
}
