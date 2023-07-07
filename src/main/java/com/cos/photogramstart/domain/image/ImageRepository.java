package com.cos.photogramstart.domain.image;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository<Image, Integer> {

	@Query(value = "SELECT * FROM image WHERE userId IN(SELECT toUserId FROM subscribe WHERE fromUserId = :principalId) ORDER BY id DESC",nativeQuery = true)
	Page<Image> mStroy(int principalId, Pageable pageable);
	
	@Query(value = "SELECT i.* FROM image i INNER JOIN (SELECT imageId, COUNT(imageId) AS likeCount FROM likes GROUP BY imageId ORDER BY likeCount DESC) c ON i.id = c.imageId", nativeQuery = true)
	List<Image> mPopular();
}
