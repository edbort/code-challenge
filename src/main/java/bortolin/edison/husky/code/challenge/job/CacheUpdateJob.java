package bortolin.edison.husky.code.challenge.job;

import bortolin.edison.husky.code.challenge.cache.CacheHandler;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheUpdateJob {
    
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CacheUpdateJob.class);
    
    @Autowired
    private CacheHandler cacheHandler;
    
    @Scheduled(cron = "0 0 0 * * *")
    public void rebuildNDaysCaches() {
        log.info("Running CacheUpdateJob job...");

        try {
            cacheHandler.rebuildAllLastNDaysCaches();
            log.info("Job successfully executed");
        } catch (Exception ex) {
            log.error("CacheUpdateJob job error", ex);
        }
        
    }
    
}
