package com.gradeBook.service;

import com.gradeBook.entity.AppSettings;
import com.gradeBook.repository.AppSettingsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppSettingsService {

    private final AppSettingsRepo appSettingsRepo;

    public AppSettings findByName(String name) {
        AppSettings appSettings = appSettingsRepo.findByName(name);
        if (appSettings == null && TokenService.TOKE_VALID_TIME.equals(name)) {
            appSettings = new AppSettings();
            appSettings.setName(TokenService.TOKE_VALID_TIME);
            appSettings.setDescription("Time for expiring token in minutes");
            appSettings.setValue("5");
            appSettings = save(appSettings);
        }
        return appSettings;
    }

    public AppSettings save(AppSettings appSettings) {
        return appSettingsRepo.saveAndFlush(appSettings);
    }

}
