package com.example.common.domain.file;

import com.example.common.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity(name = "FILE")
@Getter
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "uuid")
    private String UUID;
    @Column(name = "original_file_name")
    private String originalFileName;
    @Column(name = "save_file_path")
    private String saveFilePath;
    @Column(name = "save_file_name")
    private String saveFileName;

    @Column(name = "board_id")
    private Integer boardId;
    @Column(name = "board_type")
    private String boardType;
    @Column(name = "file_size")
    private Long fileSize;

    public File(String UUID, String saveFilePath, String originalFileName, String saveFileName, Integer boardId, String boardType, Long fileSize) {
        this.UUID = UUID;
        this.originalFileName = originalFileName;
        this.saveFilePath = saveFilePath;
        this.saveFileName = saveFileName;
        this.boardId = boardId;
        this.boardType = boardType;
        this.fileSize = fileSize;
    }

    public File() {

    }
}
