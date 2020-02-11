package bortolin.edison.husky.code.challenge.repository;

import bortolin.edison.husky.code.challenge.entity.AccountMovement;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AccountMovementRepository extends CrudRepository<AccountMovement, Long> {

    @Query("select distinct am.accountId from AccountMovement am order by am.accountId")
    List<Long> findAccount();

    @Query("select am from AccountMovement am where am.accountId = ?1 order by am.date")
    List<AccountMovement> findMovementsByAccountId(Long accountId);

    @Query("select am from AccountMovement am where am.accountId = ?1 AND am.date >= ?2 AND am.date <= ?3 order by am.date")
    List<AccountMovement> findMovementsByAccountIdBetween(Long accountId, Date startDate, Date endDate);

}