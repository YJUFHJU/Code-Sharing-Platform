package platform.bussiness;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Snippet {
    public static final String DATE_PATTERN = "yyyy/MM/dd HH:mm:ss";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Integer id;

    @JsonIgnore
    @Column(name = "uuid", unique = true)
    private String uuid;

    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "date")
    private String date;

    @NotEmpty
    @Column(name = "code", length = 10_000)
    private String code;

    @Column(name = "time")
    private Long time;

    @Column(name = "views")
    private Integer views;

    @JsonIgnore
    @Column(name = "open")
    private Boolean open;

    @JsonIgnore
    @Column(name = "time_restrict")
    private Boolean timeRestrict;

    @JsonIgnore
    @Column(name = "views_restrict")
    private Boolean viewsRestrict;

    @JsonIgnore
    @Column(name ="expiration_date")
    private LocalDateTime expDate;

    public Snippet(String code, Long time, Integer views) {
        date = LocalDateTime.now().format(FORMATTER);
        uuid = UUID.randomUUID().toString();
        this.code = code;

        if (time == null)
            time = 0L;
        if (views == null)
            views = 0;

        if (views <= 0 && time <= 0) {
            this.views = 0;
            this.time = 0L;
            timeRestrict = false;
            viewsRestrict = false;
            open = true;
        } else {
            if (time > 0) {
                this.time = time;
                timeRestrict = true;
                expDate = LocalDateTime.now().plusSeconds(time);
            } else {
                this.time = 0L;
                timeRestrict = false;
            }

            if (views > 0) {
                this.views = views;
                viewsRestrict = true;
            } else {
                this.views = 0;
                viewsRestrict = false;
            }
            open = false;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date.format(FORMATTER);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDate() {
        date = LocalDateTime.now().format(FORMATTER);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setTime(String time) {
        this.time = Long.parseLong(time);
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public void setViews(String views) {
        this.views = Integer.parseInt(views);
    }

    public void viewsDecr() {
        views--;
    }

    public Boolean isOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Boolean isTimeRestrict() {
        return timeRestrict;
    }

    public void setTimeRestrict(Boolean timeRestrict) {
        this.timeRestrict = timeRestrict;
    }

    public Boolean isViewsRestrict() {
        return viewsRestrict;
    }

    public void setViewsRestrict(Boolean viewsRestrict) {
        this.viewsRestrict = viewsRestrict;
    }

    public LocalDateTime getExpDate() {
        return expDate;
    }

    public void setExpDate(LocalDateTime expDate) {
        this.expDate = expDate;
    }

    @Override
    public String toString() {
        return "Snippet{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", date='" + date + '\'' +
                ", code='" + code + '\'' +
                ", time=" + time +
                ", views=" + views +
                ", open=" + open +
                ", expDate=" + expDate +
                '}';
    }
}
