package com.xiaofeng.lostfound;

import com.xiaofeng.lostfound.auth.AuthService;
import com.xiaofeng.lostfound.auth.dto.RegisterRequest;
import com.xiaofeng.lostfound.claim.ClaimService;
import com.xiaofeng.lostfound.claim.dto.ClaimRequest;
import com.xiaofeng.lostfound.common.BusinessException;
import com.xiaofeng.lostfound.item.*;
import com.xiaofeng.lostfound.item.dto.ItemRequest;
import com.xiaofeng.lostfound.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ItemClaimWorkflowIntegrationTest {
    @Autowired private AuthService authService;
    @Autowired private UserRepository userRepository;
    @Autowired private ItemService itemService;
    @Autowired private ClaimService claimService;

    private com.xiaofeng.lostfound.user.User publisher;
    private com.xiaofeng.lostfound.user.User claimant;

    @BeforeEach
    void setUp() {
        authService.register(new RegisterRequest("publisher", "发布者", "abc12345", "13800000011"));
        authService.register(new RegisterRequest("claimant", "认领者", "abc12345", "13800000012"));
        publisher = userRepository.findByUsername("publisher").orElseThrow();
        claimant = userRepository.findByUsername("claimant").orElseThrow();
    }

    @Test
    void 新发布信息审核通过前不可公开检索() {
        var item = itemService.create(publisher, request());

        assertThat(item.status()).isEqualTo("PENDING_REVIEW");
        assertThat(itemService.publicList("校园卡", null, null, 0, 10).getTotalElements()).isZero();

        itemService.review(item.id(), true, "信息完整");

        assertThat(itemService.publicList("校园卡", null, null, 0, 10).getTotalElements()).isEqualTo(1);
    }

    @Test
    void 发布者不能认领自己的信息() {
        var item = itemService.create(publisher, request());
        itemService.review(item.id(), true, "信息完整");

        assertThatThrownBy(() -> claimService.submit(publisher, item.id(), new ClaimRequest("这是我的校园卡")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("不能认领自己发布的信息");
    }

    @Test
    void 认领申请通过后发布者可以确认归还() {
        var item = itemService.create(publisher, request());
        itemService.review(item.id(), true, "信息完整");
        var claim = claimService.submit(claimant, item.id(), new ClaimRequest("卡背面有蓝色贴纸"));

        claimService.process(publisher, claim.id(), true, "请到服务中心核验");
        claimService.confirmReturned(publisher, claim.id());

        assertThat(itemService.getPublic(item.id()).status()).isEqualTo("FOUND");
        assertThat(claimService.mine(claimant).get(0).status()).isEqualTo("COMPLETED");
    }

    @Test
    void 通过一个认领申请后其他待处理申请应自动拒绝() {
        authService.register(new RegisterRequest("claimant2", "第二认领者", "abc12345", "13800000013"));
        var claimant2 = userRepository.findByUsername("claimant2").orElseThrow();
        var item = itemService.create(publisher, request());
        itemService.review(item.id(), true, "信息完整");
        var first = claimService.submit(claimant, item.id(), new ClaimRequest("卡背面有蓝色贴纸"));
        claimService.submit(claimant2, item.id(), new ClaimRequest("卡号末尾是0011"));

        claimService.process(publisher, first.id(), true, "第一位申请人证明充分");

        assertThat(claimService.mine(claimant2).get(0).status()).isEqualTo("REJECTED");
    }

    private ItemRequest request() {
        return new ItemRequest(PostType.LOST, "丢失一张校园卡", "姓名为张同学，卡套为蓝色",
                ItemCategory.CARD, "第一教学楼", LocalDateTime.now().minusDays(1),
                "13800000011", "https://example.com/card.jpg");
    }
}
