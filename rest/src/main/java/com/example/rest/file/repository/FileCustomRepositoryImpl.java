package com.example.rest.file.repository;

import com.example.common.domain.file.File;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Repository
public class FileCustomRepositoryImpl implements FileCustomRepository {

    private final JdbcTemplate jdbcTemplate;

    public FileCustomRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Integer> batchUpdate(List<File> files) {
        return Arrays.stream(
                jdbcTemplate.batchUpdate(
                        "INSERT INTO FILE(uuid, original_file_name, save_file_path, save_file_name, board_id, board_type, file_size)" +
                                "VALUES (?, ?, ?, ?, ?, ?, ?)",
                        new BatchPreparedStatementSetter() {
                            @Override
                            public void setValues(PreparedStatement ps, int i) throws SQLException {
                                //uuid, original_file_name, save_file_name, board_id, board_type, file_size
                                ps.setString(1, files.get(i).getUUID());
                                ps.setString(2, files.get(i).getOriginalFileName());
                                ps.setString(3, files.get(i).getSaveFilePath());
                                ps.setString(4, files.get(i).getSaveFileName());
                                ps.setInt(5, files.get(i).getBoardId());
                                ps.setString(6, files.get(i).getBoardType());
                                ps.setLong(7, files.get(i).getFileSize());
                            }

                            @Override
                            public int getBatchSize() {
                                return files.size();
                            }
                        }
                )
        ).boxed()
        .toList();
    }
}
