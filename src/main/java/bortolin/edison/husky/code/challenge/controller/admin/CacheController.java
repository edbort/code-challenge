package bortolin.edison.husky.code.challenge.controller.admin;

import bortolin.edison.husky.code.challenge.cache.CacheHandler;
import bortolin.edison.husky.code.challenge.memcache.MemCacheException;
import bortolin.edison.husky.code.challenge.memcache.MemCacheHandler;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CacheController {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CacheController.class);

    @Autowired
    private MemCacheHandler memCacheHandler;
    
    @Autowired
    private CacheHandler cacheHandler;
    
    @RequestMapping("/admin/cache/{key}")
    public ResponseEntity<Object> getCache(@PathVariable String key) throws MemCacheException {
        return ResponseEntity.ok(memCacheHandler.get(key));
    }  
    
    @RequestMapping(value = "/admin/cache/{key}", method = RequestMethod.DELETE)
    public ResponseEntity deleteCache(@PathVariable String key) throws MemCacheException {
        memCacheHandler.invalidade(key);
        return ResponseEntity.ok().build();
    }  

    @RequestMapping(value="/admin/cache/rebuildAll", method=RequestMethod.GET)
    public ResponseEntity rebuildAll() throws MemCacheException {
        cacheHandler.buildAll();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value="/admin/cache/build/{accountId}", method=RequestMethod.GET)
    public ResponseEntity rebuildAccountCache(@PathVariable Long accountId) throws MemCacheException {
        cacheHandler.buildCacheForAccount(accountId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value="/admin/cache/rebuildLastNDaysCaches", method=RequestMethod.GET)
    public ResponseEntity rebuildLastNDaysCaches() throws MemCacheException {
        cacheHandler.rebuildAllLastNDaysCaches();
        return ResponseEntity.ok().build();
    }
    
    @RequestMapping(value="/admin/cache/flush", method=RequestMethod.GET)
    public ResponseEntity flush() throws MemCacheException {
        memCacheHandler.flushAll();
        return ResponseEntity.ok().build();
    }
    
}
