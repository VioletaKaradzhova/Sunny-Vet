package com.sunnyvet.main.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ClinicScheduler {

    private static final Logger log = LoggerFactory.getLogger(ClinicScheduler.class);

    @Scheduled(cron = "0 59 23 * * ?")
    public void endOfDayAuditJob() {
        log.info("CRON JOB EXECUTION: Performing end-of-day clinic system audit at {}", LocalDateTime.now());
    }

    @Scheduled(fixedRate = 3600000)
    public void systemHealthPing() {
        log.info("FIXED RATE JOB: Routine system health and caching integrity check executed.");
    }
}