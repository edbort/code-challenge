package bortolin.edison.husky.code.challenge.cache;

import bortolin.edison.husky.code.challenge.util.Exclude;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CacheLastNDays implements Serializable {

    @Exclude
    private Long accountId;

    private List<String> cacheDailyBaseKeyList;
    
    public CacheLastNDays() {
        this.cacheDailyBaseKeyList = new ArrayList<>();
    }
    
    public CacheLastNDays(Long accountId) {
        this();
        this.accountId = accountId;
    }
    
    public Long getAccountId() {
        return accountId;
    }

    public List<String> getCacheDailyBaseKeyList() {
        return cacheDailyBaseKeyList;
    }

    public void addCacheDailyBaseKey(String cacheDailyBase) {
        this.cacheDailyBaseKeyList.add(cacheDailyBase);
    }

    @Override
    public String toString() {
        return "CacheLastNDays{" + "accountId=" + accountId + ", cacheDailyBaseKeyList=" + cacheDailyBaseKeyList + '}';
    }
    
}