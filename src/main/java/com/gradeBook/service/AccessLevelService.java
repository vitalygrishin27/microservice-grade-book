package com.gradeBook.service;

import com.gradeBook.entity.AccessLevel;
import com.gradeBook.repository.AccessLevelRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessLevelService {

    private final AccessLevelRepo accessLevelRepo;

    public List<AccessLevel> findAll() {
        return accessLevelRepo.findAll();
    }
    public AccessLevel findByLevel(AccessLevel.LEVEL level) {
        return accessLevelRepo.findByLevel(level);
    }

    public AccessLevel save(AccessLevel accessLevel) {
        return accessLevelRepo.saveAndFlush(accessLevel);
    }

}
