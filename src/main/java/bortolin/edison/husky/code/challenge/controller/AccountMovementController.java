package bortolin.edison.husky.code.challenge.controller;

import bortolin.edison.husky.code.challenge.cache.CacheDailyBase;
import bortolin.edison.husky.code.challenge.cache.CacheHandler;
import bortolin.edison.husky.code.challenge.cache.CacheLastNDays;
import bortolin.edison.husky.code.challenge.entity.AccountMovement;
import bortolin.edison.husky.code.challenge.memcache.MemCacheException;
import bortolin.edison.husky.code.challenge.memcache.MemCacheHandler;
import bortolin.edison.husky.code.challenge.repository.AccountMovementRepository;
import bortolin.edison.husky.code.challenge.util.Util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountMovementController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AccountMovementController.class);

    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    
    @Autowired
    private AccountMovementRepository repository;

    @Autowired
    private MemCacheHandler memCacheHandler;

    @Autowired
    private CacheHandler cacheHandler;
 
    @RequestMapping("/account/movement/date")
    public ResponseEntity<CacheDailyBase> accounts(Long accountId, String date) throws MemCacheException {
        Date baseDate = null;
        
        try {
            baseDate = format.parse(date);
        } catch (ParseException ex) {
            return ResponseEntity.badRequest().build();
        }
        
        String key = cacheHandler.getDailyBaseCacheKey(accountId, baseDate);
        
        CacheDailyBase result = memCacheHandler.getDailyBaseCache(key);
        if (result == null) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/account/movement/{id}/{days}")
    public ResponseEntity<GsonResponseList<AccountMovement>> accountMovementDays(@PathVariable Long id, @PathVariable int days) throws MemCacheException {
        try {
            GsonResponseList<AccountMovement> result = new GsonResponseList<>(true);
            
            String key = cacheHandler.getLastNDaysCacheKey(id, days);

            CacheLastNDays cacheLastNDays = memCacheHandler.getLastNDaysCache(key);
            if (cacheLastNDays == null) {
                return ResponseEntity.noContent().build();
            }

            List<AccountMovement> s = memCacheHandler.getMovementListOf(cacheLastNDays.getCacheDailyBaseKeyList());
            Collections.sort(s);

            result.setList(s);
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            log.error("Erro while get cached of account", ex);
            return accountMovementDBDays(id, days);
        }
    }
    
    @RequestMapping("/account/movement/db/{id}/{days}")
    public ResponseEntity<GsonResponseList<AccountMovement>> accountMovementDBDays(@PathVariable Long id, @PathVariable int days) throws MemCacheException {
        GsonResponseList<AccountMovement> result = new GsonResponseList<>(false);
        
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, -(days-1)); 
        Util.setStartTime(startDate);
        
        Calendar endDate = Calendar.getInstance();
        Util.setEndTime(endDate);
        
        result.setList(repository.findMovementsByAccountIdBetween(id, startDate.getTime(), endDate.getTime()));
        return ResponseEntity.ok(result);
    }

}
