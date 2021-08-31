package com.sda.auction.config;

import com.sda.auction.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    // == fields ==
    private final BidService bidService;

    // == constructor ==
    @Autowired
    public SchedulerConfig(BidService bidService) {
        this.bidService = bidService;
    }

    // will search for a winner every 5 seconds
    @Scheduled(fixedDelay = 5000)
    public void regularJob() {
        bidService.assignWinners(); // will assign the winner with highest bid
    }
}
