package bortolin.edison.husky.code.challenge.util;

import bortolin.edison.husky.code.challenge.entity.AccountMovement;
import bortolin.edison.husky.code.challenge.repository.AccountMovementRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Random;

/**
 * Randon Data Generator for Account Movements
 * @author edisonb
 */
public class MockData {

    private static final String[] description = new String[] {
        "Tax",
        "Fee",
        "Deposit",
        "Web Bill Payment",
        "Bill Payment",
        "Internet Transfer",
        "Insurance",
        "Direct Debit",
        "Payroll"
    };
    
    public int createSampleData(AccountMovementRepository repository) {
        int result = 0;
        
        // 50 accounts
        for(long accountId = 1; accountId <= 200; accountId++) {
            
            // 1 year, 40 days
            for(int i=365+40; i>=0; i--) {
                LocalDate auxDate = LocalDate.now().plusDays(-i);

                // 1-15 records (movement/transaction) per day
                for(int j=0; j<getNumberOfRecords(); j++) {
                    LocalTime auxTime = LocalTime.of(getRandonHour(), getRandonMinute());
                    LocalDateTime auxDateTime = LocalDateTime.of(auxDate, auxTime);

                    repository.save(
                        new AccountMovement(
                            accountId, 
                            Date.from(auxDateTime.toInstant(ZoneOffset.UTC)), 
                            getRandonDescription(), 
                            new BigDecimal(getRandonSignal() * getRandonValue() * getRandon(1,99))
                        )
                    );
                    
                    result++;
                }
            }
        }
        
        return result;
    }
    
    private String getRandonDescription() {
        int i = getRandon(0, description.length-1);
        return description[i];
    }
    
    private int getRandonSignal() {
        return (new Random().nextBoolean() ? 1 : -1);
    }
    
    private float getRandonValue() {
        return new Random().nextFloat();
    }

    private int getNumberOfRecords() {
        return getRandon(1, 15);
    }
    
    private int getRandonHour() {
        return getRandon(0,23);
    }
    
    private int getRandonMinute() {
        return getRandon(0,59);
    }
    
    private int getRandon(int low, int high) {
        return new Random().nextInt(high-low) + low;
    }
        
}