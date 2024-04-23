package com.mealtoyou.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.endpoint.RefreshEndpoint;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class ConfigRefresher {

    private final RefreshEndpoint refreshEndpoint;

    @Autowired
    ConfigRefresher(RefreshEndpoint refreshEndpoint) {
        this.refreshEndpoint = refreshEndpoint;
    }

    @Scheduled(fixedDelay = 300000)
    public void refreshConfig() {
        refreshEndpoint.refresh();
    }
}
