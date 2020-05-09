package lv.iljakorneckis.webloans.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Date;

@Entity
@JsonIgnoreProperties({"extensionDate"})
public class LoanExtension {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime extensionDate;

    public Long getId() {
        return id;
    }

    public Date getDate() {
        if(extensionDate != null) {
            return extensionDate.toDate();
        }

        return null;
    }

    public DateTime getExtensionDate() {
        return extensionDate;
    }

    public void setExtensionDate(DateTime extensionDate) {
        this.extensionDate = extensionDate;
    }
}