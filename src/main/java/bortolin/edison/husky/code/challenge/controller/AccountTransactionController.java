package bortolin.edison.husky.code.challenge.controller;

import bortolin.edison.husky.code.challenge.cache.CacheHandler;
import bortolin.edison.husky.code.challenge.entity.AccountMovement;
import bortolin.edison.husky.code.challenge.memcache.MemCacheException;
import bortolin.edison.husky.code.challenge.memcache.MemCacheHandler;
import bortolin.edison.husky.code.challenge.repository.AccountMovementRepository;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountTransactionController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AccountTransactionController.class);

    private final SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

    private final SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Autowired
    private AccountMovementRepository repository;

    @Autowired
    private MemCacheHandler memCacheHandler;

    @Autowired
    private CacheHandler cacheHandler;
    
    @RequestMapping(value="/account/transaction/add/{id}/{date}/{description}/{value}", method=RequestMethod.POST)
    public ResponseEntity<AccountMovement> addTransaction(
            @PathVariable Long id, 
            @PathVariable String date,
            @PathVariable String description,
            @PathVariable BigDecimal value) throws MemCacheException {
        
        Date baseDate = null;
        
        try {
            baseDate = formatDateTime.parse(date);
        } catch (ParseException ex) {
            try {
                baseDate = formatDate.parse(date);
            } catch (ParseException ex2) {
                return ResponseEntity.badRequest().build();
            }
        }
        
        AccountMovement result = new AccountMovement();
        result.setAccountId(id);
        result.setDate(baseDate);
        result.setDescription(description);
        result.setValue(value);
        
        repository.save(result);
        
        cacheHandler.rebuildDailyCache(id, baseDate);

        return ResponseEntity.ok(result);
    }
    
}
