package bortolin.edison.husky.code.challenge;

import bortolin.edison.husky.code.challenge.cache.CacheHandler;
import bortolin.edison.husky.code.challenge.memcache.MemCacheHandler;
import bortolin.edison.husky.code.challenge.repository.AccountMovementRepository;
import bortolin.edison.husky.code.challenge.util.MockData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MainApplication {

    private static final Logger log = LoggerFactory.getLogger(MainApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(AccountMovementRepository repository, MemCacheHandler memCacheHandler) {
        return (args) -> {
            if (repository.count() < 1) {
                log.info("Creating Mock Data...");
                log.info(new MockData().createSampleData(repository) + " records created.");
            }
            
            try {
                memCacheHandler.connect();
                
                CacheHandler cacheHandler = new CacheHandler(repository, memCacheHandler);
                cacheHandler.buildAll();
                
                log.info("Application is Ready!");
            } catch (Exception ex) {
                log.error("Memcache problem. Can't continue", ex);
            }
        };
    }
    
}
