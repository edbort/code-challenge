package bortolin.edison.husky.code.challenge.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

@Entity
public class AccountMovement implements Serializable, Comparable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "account_id", nullable = false)
    private Long accountId;

    @Column(name = "date", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date date;
    
    @Column(length = 50, nullable = false)
    private String description;
    
    @Column(nullable = false)
    private BigDecimal value;

    public AccountMovement(Long accountId, Date date, String description, BigDecimal value) {
        this.accountId = accountId;
        this.date = date;
        this.description = description;
        this.value = value;
    }
    
    public AccountMovement() {
        //
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public int compareTo(Object o) {
        return this.date.compareTo(((AccountMovement)o).getDate());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AccountMovement other = (AccountMovement) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BankRecord{" + "id=" + id + ", accountId=" + accountId + ", date=" + date + ", description=" + description + ", value=" + value + '}';
    }

}
