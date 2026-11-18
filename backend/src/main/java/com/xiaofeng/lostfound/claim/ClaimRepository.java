package com.xiaofeng.lostfound.claim;
import com.xiaofeng.lostfound.item.ItemPost;
import com.xiaofeng.lostfound.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;
public interface ClaimRepository extends JpaRepository<ClaimApplication, Long> {
    boolean existsByItemAndApplicantAndStatusIn(ItemPost item, User applicant, Collection<ClaimStatus> statuses);
    List<ClaimApplication> findByApplicantOrderByCreatedAtDesc(User applicant);
    List<ClaimApplication> findByItemPublisherOrderByCreatedAtDesc(User publisher);
    List<ClaimApplication> findByItemAndStatus(ItemPost item, ClaimStatus status);
}
