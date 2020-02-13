package bortolin.edison.husky.code.challenge.memcache;

import bortolin.edison.husky.code.challenge.cache.CacheDailyBase;
import bortolin.edison.husky.code.challenge.cache.CacheLastNDays;
import bortolin.edison.husky.code.challenge.entity.AccountMovement;
import bortolin.edison.husky.code.challenge.util.Exclude;
import bortolin.edison.husky.code.challenge.util.Util;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import net.rubyeye.xmemcached.XMemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@ApplicationScope
public class MemCacheHandler {

    private static final Logger log = LoggerFactory.getLogger(MemCacheHandler.class);
    
    private static final Long TIMEOUT = 300L;
    
    private XMemcachedClient mc;
    
    @Value("${memcache.host}")
    private String host;
    
    @Value("${memcache.port}")
    private int port;

    private boolean connected;

    private final Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .setExclusionStrategies(getExclusionStrategy())
                .create();
    
    public void connect() throws MemCacheException {
        try {
            mc = new XMemcachedClient(host, port);
            connected = true;
        } catch (Exception ex) {
            connected = false;
            throw new MemCacheException("Can't connect to Memcache on " + host + ":" + port, ex);
        }
    }

    public boolean isConnected() {
        return connected;
    }
    
    private ExclusionStrategy getExclusionStrategy() {
        return new ExclusionStrategy() {
            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }

            @Override
            public boolean shouldSkipField(FieldAttributes field) {
                return field.getAnnotation(Exclude.class) != null;
            }
        };
    }        
    
    private void checkConnection() throws MemCacheException {
        if (!isConnected()) {
            throw new MemCacheException("Not connected to Memcache");
        }
    }

    public void setDailyBaseCache(String key, CacheDailyBase cacheDailyBase) throws MemCacheException {
        checkConnection();
        
        try {
            mc.set(key, 0, gson.toJson(cacheDailyBase)); // Never Expires
        } catch (Exception ex) {
            throw new MemCacheException("Memcache error", ex);
        }
    }
    
    public CacheDailyBase getDailyBaseCache(String key) throws MemCacheException {
        try {
            String content = mc.get(key);
            
            if (content == null) {
                return null;
            }
            
            return gson.fromJson(content, CacheDailyBase.class);
        } catch (Exception ex) {
            throw new MemCacheException("Memcache error", ex);
        }
    }
    
    public void setLastNDaysCache(String key, CacheLastNDays cacheLastNDays) throws MemCacheException {
        checkConnection();
        
        try {
            mc.set(key, expireToday(), gson.toJson(cacheLastNDays)); // Until today 23h59m55s
        } catch (Exception ex) {
            throw new MemCacheException("Memcache error", ex);
        }
    }
    
    public CacheLastNDays getLastNDaysCache(String key) throws MemCacheException {
        try {
            String content = mc.get(key, TIMEOUT);
            
            if (content == null) {
                throw new MemCacheException("Cache is not reliable");
            }
            
            return gson.fromJson(content, CacheLastNDays.class);
        } catch (Exception ex) {
            throw new MemCacheException("Memcache error", ex);
        }
    }
    
    public List<CacheDailyBase> getCacheDailyBaseListOf(List<String> keys) throws MemCacheException {
        final List<CacheDailyBase> result = new ArrayList<>();
        
        try {
            Map<String, String> aux = mc.get(keys);
            
            // All requested keys should be returned
            if (aux.size() != keys.size()) {
                throw new MemCacheException("Cache is not reliable. Requested " + keys.size() + " keys, received " + aux.size());
            }
            
            aux.forEach((String k, String v) -> {
                CacheDailyBase c = gson.fromJson(v, CacheDailyBase.class);
                result.add(c);
            });
        } catch (Exception ex) {
            throw new MemCacheException("Memcache error", ex);
        }
        
        return result;
    }
    
    public List<AccountMovement> getMovementListOf(List<String> keys) throws MemCacheException {
        final List<AccountMovement> result = new ArrayList<>();
        
        try {
            Map<String, String> aux = mc.get(keys);
            
            // All requested keys should be returned
            if (aux.size() != keys.size()) {
                throw new MemCacheException("Cache is not reliable. Requested " + keys.size() + " keys, received " + aux.size());
            }
            
            aux.forEach((String k, String v) -> {
                CacheDailyBase c = gson.fromJson(v, CacheDailyBase.class);
                result.addAll(c.getMovementList());
            });
        } catch (Exception ex) {
            throw new MemCacheException("Memcache error", ex);
        }
        
        return result;
    }

    public Object get(String key) throws MemCacheException {
        try {
            return mc.get(key, TIMEOUT);
        } catch (Exception ex) {
            throw new MemCacheException("Memcache error", ex);
        }
    }
        
    public void invalidade(String key) throws MemCacheException {
        try {
            mc.delete(key);
        } catch (Exception ex) {
            throw new MemCacheException("Memcache error", ex);
        }
    }
    
    public void flushAll() throws MemCacheException {
        try {
            mc.flushAll();
        } catch (Exception ex) {
            throw new MemCacheException("Memcache error", ex);
        }
    }

    private int expireToday() {
        Calendar c = Calendar.getInstance();
        Util.setEndTime(c);
        return Math.round((c.getTimeInMillis() - Calendar.getInstance().getTimeInMillis())/1000);
   }
    
}