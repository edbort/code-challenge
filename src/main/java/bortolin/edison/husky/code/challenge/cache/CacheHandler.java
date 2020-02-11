package bortolin.edison.husky.code.challenge.cache;

import bortolin.edison.husky.code.challenge.entity.AccountMovement;
import bortolin.edison.husky.code.challenge.memcache.MemCacheException;
import bortolin.edison.husky.code.challenge.memcache.MemCacheHandler;
import bortolin.edison.husky.code.challenge.repository.AccountMovementRepository;
import bortolin.edison.husky.code.challenge.util.Util;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@ApplicationScope
public class CacheHandler {

    private static final Logger log = LoggerFactory.getLogger(CacheHandler.class);

    private final String DATE_FORMAT = "yyyyMMdd";
    
    private final SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
    
    private final AccountMovementRepository repository;
    
    private MemCacheHandler memCacheHandler;

    public CacheHandler(AccountMovementRepository repository, MemCacheHandler memCacheHandler) {
        this.repository = repository;
        this.memCacheHandler = memCacheHandler;
    }
    
    public void buildAll() throws MemCacheException {
        log.info("Building Cache...");
        
        for(Long accountId : repository.findAccount()) {
            buildCacheForAccount(accountId);
        }
        
        log.info("Done!");
    }    
    
    public void buildCacheForAccount(Long accountId) throws MemCacheException {
        List<AccountMovement> list = new ArrayList<>();
        
        String aux = "";
        for(AccountMovement am : repository.findMovementsByAccountId(accountId)) {
            String sDate = format.format(am.getDate());
            
            if (!sDate.equals(aux)) {
                if (list.size() > 0) {
                    buildDailyCache(accountId, list);
                }
                
                list = new ArrayList<>();
                aux = sDate;
            }
            
            list.add(am);
        }
        
        if (list.size() > 0) {
            buildDailyCache(accountId, list);
        }
        
        buildLastNDaysCache(accountId, 3);
        buildLastNDaysCache(accountId, 15);
        buildLastNDaysCache(accountId, 30);
        buildLastNDaysCache(accountId, 60);
        buildLastNDaysCache(accountId, 90);
    }
    
    public void buildDailyCache(Long accountId, List<AccountMovement> list) throws MemCacheException {
        CacheDailyBase cacheDailyBase = new CacheDailyBase(accountId);
        cacheDailyBase.setMovementList(list);
        
        memCacheHandler.setDailyBaseCache(getDailyBaseCacheKey(accountId, list.get(0).getDate()), cacheDailyBase);
    } 
    
    public void buildLastNDaysCache(Long accountId, int days) throws MemCacheException {
        CacheLastNDays cacheLastNDays = new CacheLastNDays(accountId);
                
        Calendar calendar = Calendar.getInstance();
        for(int i=1; i<=days; i++) {
            cacheLastNDays.addCacheDailyBaseKey(getDailyBaseCacheKey(accountId, calendar.getTime()));
            calendar.add(Calendar.DATE, -1);
        }
        
        memCacheHandler.setLastNDaysCache(getLastNDaysCacheKey(accountId, days), cacheLastNDays);
    }

    public void rebuildDailyCache(Long accountId, Date baseDate) throws MemCacheException {
        CacheDailyBase cacheDailyBase = new CacheDailyBase(accountId);
        cacheDailyBase.setMovementList(repository.findMovementsByAccountIdBetween(accountId, Util.getStartDateTimeOf(baseDate), Util.getEndDateTimeOf(baseDate)));
        memCacheHandler.setDailyBaseCache(getDailyBaseCacheKey(accountId, baseDate), cacheDailyBase);
    }
            
    public String getDailyBaseCacheKey(Long accountId, Date date) {
        return accountId + "-" + format.format(date);
    }

    public String getDailyBaseCacheKey(Long accountId, LocalDate localDate) {
        return accountId + "-" + localDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public String getLastNDaysCacheKey(Long accountId, int days) {
        return accountId + "-" + days + "d";
    }
        
}