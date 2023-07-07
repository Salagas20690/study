package com.cos.photogramstart.service;


import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.web.dto.iamge.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {
	
	private final ImageRepository imageRepository;
	
	@Value("${file.path}")
	private String uploadFolder;
	
	@Transactional
	public void ImageUpload(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
		UUID uuid = UUID.randomUUID();
		String imageFileName = uuid + "_" + imageUploadDto.getFile().getOriginalFilename();
		System.out.println("이미지 파일이름 : " + imageFileName);
		// 통신 I/O -> 예외가 발생할 수 있다.
		try {
			/*
			String encoded = new String(Base64.getUrlEncoder().encode(imageUploadDto.getFile().getOriginalFilename().getBytes()), "utf-8");
			imageFileName += encoded;
					
			System.out.println("암호화 : " + imageFileName);
			
			byte[] decoded = Base64.getUrlDecoder().decode(encoded);
			System.out.println("디코딩 : " + new String(decoded, StandardCharsets.UTF_8));
			
			*/
			
			Path imageFilePath = Paths.get(uploadFolder + imageFileName);
			
			Files.write(imageFilePath, imageUploadDto.getFile().getBytes());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		// image 테이블에 저장
		Image image = imageUploadDto.toEntity(imageFileName, principalDetails.getUser());
		imageRepository.save(image);
		
		//System.out.println(imageEntity); JPA 무한 참조 
	}
	
	@Transactional
	public Page<Image> imageStory(int principalId, Pageable pagealbe){
		Page<Image> images = imageRepository.mStroy(principalId, pagealbe);
		
		// 좋아요 상태 담아가기
		images.forEach((image) -> {
			
			image.setLikeCount(image.getLikes().size());
			
			image.getLikes().forEach((like) -> {
				
				if(like.getUser().getId() == principalId) {
					image.setLikeState(true);
				}
				
			});
			
		});
		
		return images;
	}
	
	@Transactional(readOnly = true)
	public List<Image> popularPhoto() {
		return imageRepository.mPopular();
	}
	
}
