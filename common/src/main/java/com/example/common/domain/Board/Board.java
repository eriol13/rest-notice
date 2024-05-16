package com.example.common.domain.Board;

import com.example.common.domain.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "BOARD")
@NoArgsConstructor
@ToString
@Getter
public class Board extends BaseEntity {
    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    public Board(String title, String content, String createdBy, String lastModifiedBy) {
        this.title = title;
        this.content = content;
        this.createdBy = createdBy;
        this.lastModifiedBy = lastModifiedBy;
    }

    public void update(String title, String content, String lastModifiedBy) {
        this.title = title;
        this.content = content;
        this.lastModifiedBy = lastModifiedBy;
    }
}
