package com.flowforge.auth.sechduler;

import com.flowforge.auth.service.PasswordResetTokenCleanupService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetTokenCleanupScheduler {
    private final PasswordResetTokenCleanupService cleanupService;

    public PasswordResetTokenCleanupScheduler(
            PasswordResetTokenCleanupService cleanupService) {

        this.cleanupService = cleanupService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void cleanupExpiredTokens() {

        long deleted =
                this.cleanupService.cleanupExpiredTokens();

        System.out.println(
                "Expired password reset tokens deleted: "
                        + deleted
        );
    }
}
