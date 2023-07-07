package com.cos.photogramstart.domain.user;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.cos.photogramstart.domain.image.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Java Persistence API(자바로 데이터를 영구적으로 저장(DB)할 수 있는 API를 제공)

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 번호 증가 전략이 DB를 따라간다
	private int id;
	
	@Column(length = 100, unique = true) // OAuth2 로그인을 위해 length 늘리기
	private String username;
	
	@Column(nullable=false)
	private String password;
	
	@Column(nullable=false)
	private String name;
	
	private String website;
	private String bio;
	
	@Column(nullable=false)
	private String email;
	
	private String tel;
	private String gender;
	private String profileImageUrl;
	private String role;
	
	// 나는 연관관례의 주인이 아니다. 그러므로 테이블에 칼럼을 만들지마.
	// User를 Select할 때 해당 User id로 등록된 image들을 다 가져와
	// LAZY = User를 Select할 때 해당 User id로 등록된 image들을 가져오지마 - 대신 getImages()함수의 image들이 호출될 때 가져와
	// EAGER = User를 Select할 때 해당 User id로 등록된 image들을 전부 Join해서 가져와
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"user"})
	private List<Image> images; // 양방향 매핑
	
	private LocalDateTime createTime;

	@PrePersist // DB에 INSERT 되기 직전에 실행
	public void createDate() {
		this.createTime = LocalDateTime.now();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", website="
				+ website + ", bio=" + bio + ", email=" + email + ", tel=" + tel + ", gender=" + gender
				+ ", profileImageUrl=" + profileImageUrl + ", role=" + role + ", createTime="
				+ createTime + "]";
	}
	
}
