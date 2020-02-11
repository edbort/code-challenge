package bortolin.edison.husky.code.challenge.cache;

import bortolin.edison.husky.code.challenge.entity.AccountMovement;
import bortolin.edison.husky.code.challenge.util.Exclude;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CacheDailyBase implements Serializable {

    @Exclude
    private Long accountId;

    private List<AccountMovement> movementList;

    public CacheDailyBase() {
        movementList = new ArrayList<>();
    }

    public CacheDailyBase(Long accountId) {
        this();
        this.accountId = accountId;
    }

    public CacheDailyBase(Long accountId, List<AccountMovement> movementList) {
        this();
        this.accountId = accountId;
        this.movementList = movementList;
    }
    
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    
    public List<AccountMovement> getMovementList() {
        return movementList;
    }

    public void setMovementList(List<AccountMovement> movementList) {
        this.movementList = movementList;
    }

    @Override
    public String toString() {
        return "CacheDailyBase{" + "accountId=" + accountId + ", movementList=" + movementList + '}';
    }

}