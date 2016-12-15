package wad.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="Word",schema="public")
public class Word implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ID")
    private Long Id;
    
    @ManyToOne
    @JoinColumn(name="account_id")
    private Account account;
    
    @OneToOne
    private WordDetail word_detail;

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

    public WordDetail getWord_detail() {
        return word_detail;
    }

    public void setWord_detail(WordDetail word_detail) {
        this.word_detail = word_detail;
    }
       
}
