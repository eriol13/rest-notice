package com.example.rest.file.repository;

import com.example.common.domain.file.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileJpaRepository extends JpaRepository<File, Integer> {
}
