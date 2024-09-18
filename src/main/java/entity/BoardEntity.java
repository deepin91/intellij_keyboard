package entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "board")
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bIdx;
    @Column(nullable = false, length = 100)
    private String bTitle;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String bContents;
    @Column(nullable = false)
    private String userNickname;
    @Column(nullable = false)
    private int bViews;
    @Column(nullable = false)
    private LocalDateTime bCreatedDt;
    @Column(nullable = true) // 이미지가 없을 수 있으므로 nullable 설정
    private String bImg;


    @PrePersist
    protected void onCreate() {
        this.bCreatedDt = LocalDateTime.now();
        if (this.bViews == 0) {
            this.bViews = 0;
        }
        if (this.bImg == null || this.bImg.isEmpty()) {
            this.bImg = "default.jpg";
        }
    }
    // Getter & Setter methods
    public int getBIdx() {
        return bIdx;
    }

    public void setBIdx(int bIdx) {
        this.bIdx = bIdx;
    }

    public String getBTitle() {
        return bTitle;
    }

    public void setBTitle(String bTitle) {
        this.bTitle = bTitle;
    }

    public String getBContents() {
        return bContents;
    }

    public void setBContents(String bContents) {
        this.bContents = bContents;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public int getBViews() {
        return bViews;
    }

    public void setBViews(int bViews) {
        this.bViews = bViews;
    }


//    public void setBCreatedDt(LocalDateTime bCreatedDt) {
//        this.bCreatedDt = bCreatedDt;
//    }

    public String getBImg() {
        return bImg;
    }

    public void setBImg(String bImg) {
        this.bImg = bImg;
    }

    public LocalDateTime getBCreatedDt() {
        return this.bCreatedDt;
    }

    public void setBCreatedDt(LocalDateTime bCreatedDtw) {
        this.bCreatedDt = bCreatedDt;
    }
}
