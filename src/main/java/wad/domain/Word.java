package wad.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name="Word",schema="public")
public class Word implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ID")
    private Long Id;
    
    private String LangCode;
    
    private String Type;
    
    @NotBlank
    @Length(min = 1, max =100)
    private String Content;
            
    private String Pronounce;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date CreatedDate;
    
    @ManyToOne
    @JoinColumn(name="account_id")
    private Account account;
    
    @OneToMany(mappedBy = "word", cascade = CascadeType.ALL)
    private List<WordDetail> word_details;

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

    public List<WordDetail> getWord_details() {
        return word_details;
    }

    public void setWord_details(List<WordDetail> word_details) {
        this.word_details = word_details;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getPronounce() {
        return Pronounce;
    }

    public void setPronounce(String Pronounce) {
        this.Pronounce = Pronounce;
    }       

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Date CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public String getLangCode() {
        return LangCode;
    }

    public void setLangCode(String LangCode) {
        this.LangCode = LangCode;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }
    
    
}
