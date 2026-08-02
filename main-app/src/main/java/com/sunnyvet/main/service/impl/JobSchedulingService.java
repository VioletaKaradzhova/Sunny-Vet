package com.sunnyvet.main.service.impl;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class JobSchedulingService {

    private static final Logger logger = LoggerFactory.getLogger(JobSchedulingService.class);

    @Scheduled(cron = "0 0 8 * * ?")
    public void executeDailyMorningJob() {
        logger.info("Executing daily morning system health check at 8 AM.");
    }

    @Scheduled(fixedRate = 3600000)
    public void executeHourlyCleanupJob() {
        logger.info("Executing hourly temporary system data cleanup.");
    }
}