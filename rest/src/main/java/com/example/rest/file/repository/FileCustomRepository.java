package com.example.rest.file.repository;

import com.example.common.domain.file.File;

import java.util.List;

public interface FileCustomRepository {
    public List<Integer> batchUpdate(List<File> files);
}
