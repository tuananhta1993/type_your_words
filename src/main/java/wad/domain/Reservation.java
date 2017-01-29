package wad.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name="Reservation", schema="public")
public class Reservation implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ID")
    private Long Id;
    
    @ManyToOne
    @JoinColumn(name="account_id")
    private Account account;

    @CreatedDate
    private Date createdDate;
    
    private String subject;
    
    private Boolean isPublic;
    
    private Timestamp reservationFrom;

    private Timestamp reservationTo;
    
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    
    
    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }
    
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getReservationFrom() {
        return reservationFrom;
    }

    public void setReservationFrom(Timestamp reservationFrom) {
        this.reservationFrom = reservationFrom;
    }

    public Date getReservationTo() {
        return reservationTo;
    }

    public void setReservationTo(Timestamp reservationTo) {
        this.reservationTo = reservationTo;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
   
    public boolean isOverlapped(Reservation another) {
        return true;
    }
}
