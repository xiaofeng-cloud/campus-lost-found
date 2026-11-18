package com.xiaofeng.lostfound.claim;
import com.xiaofeng.lostfound.claim.dto.*;
import com.xiaofeng.lostfound.common.BusinessException;
import com.xiaofeng.lostfound.item.*;
import com.xiaofeng.lostfound.notification.NotificationService;
import com.xiaofeng.lostfound.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ClaimService {
    private final ClaimRepository claimRepository; private final ItemService itemService; private final NotificationService notifications;
    public ClaimService(ClaimRepository claimRepository, ItemService itemService, NotificationService notifications) {
        this.claimRepository = claimRepository; this.itemService = itemService; this.notifications = notifications;
    }
    @Transactional public ClaimView submit(User applicant, Long itemId, ClaimRequest request) {
        ItemPost item = itemService.require(itemId);
        if (item.getPublisher().getId().equals(applicant.getId())) throw new BusinessException("不能认领自己发布的信息");
        if (item.getStatus() != ItemStatus.SEARCHING) throw new BusinessException("当前信息不能申请认领");
        if (claimRepository.existsByItemAndApplicantAndStatusIn(item, applicant, List.of(ClaimStatus.PENDING, ClaimStatus.APPROVED)))
            throw new BusinessException("请勿重复提交认领申请");
        ClaimApplication claim = claimRepository.save(new ClaimApplication(item, applicant, request.proof().trim()));
        notifications.send(item.getPublisher(), "收到新的认领申请", applicant.getNickname() + "申请认领“" + item.getTitle() + "”");
        return ClaimView.from(claim);
    }
    @Transactional public ClaimView process(User publisher, Long claimId, boolean approved, String comment) {
        ClaimApplication claim = require(claimId); itemService.requireOwner(publisher, claim.getItem());
        if (claim.getStatus() != ClaimStatus.PENDING) throw new BusinessException("该申请已处理");
        claim.process(approved, comment);
        if (approved) {
            if (claim.getItem().getStatus() != ItemStatus.SEARCHING) throw new BusinessException("该物品已有通过的认领申请");
            claim.getItem().markClaiming();
            claimRepository.findByItemAndStatus(claim.getItem(), ClaimStatus.PENDING).stream()
                    .filter(other -> !other.getId().equals(claim.getId()))
                    .forEach(other -> {
                        other.process(false, "已有其他认领申请通过");
                        notifications.send(other.getApplicant(), "认领申请未通过", "“" + claim.getItem().getTitle() + "”已有其他申请通过");
                    });
        }
        notifications.send(claim.getApplicant(), approved ? "认领申请已通过" : "认领申请未通过",
                "“" + claim.getItem().getTitle() + "”的认领申请已处理");
        return ClaimView.from(claim);
    }
    @Transactional public ClaimView confirmReturned(User publisher, Long claimId) {
        ClaimApplication claim = require(claimId); itemService.requireOwner(publisher, claim.getItem());
        if (claim.getStatus() != ClaimStatus.APPROVED) throw new BusinessException("只有已通过的申请可以确认归还");
        claim.complete(); claim.getItem().markFound();
        notifications.send(claim.getApplicant(), "物品已确认归还", "感谢你完成“" + claim.getItem().getTitle() + "”的归还流程");
        return ClaimView.from(claim);
    }
    @Transactional(readOnly = true) public List<ClaimView> mine(User user) { return claimRepository.findByApplicantOrderByCreatedAtDesc(user).stream().map(ClaimView::from).toList(); }
    @Transactional(readOnly = true) public List<ClaimView> received(User user) { return claimRepository.findByItemPublisherOrderByCreatedAtDesc(user).stream().map(ClaimView::from).toList(); }
    private ClaimApplication require(Long id) { return claimRepository.findById(id).orElseThrow(() -> new BusinessException("认领申请不存在")); }
}
