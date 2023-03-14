package com.drps.ams.service;

import com.drps.ams.dto.ApiResponseEntity;

public interface DashboardService {

	ApiResponseEntity getMonthlyMaintenance();

	ApiResponseEntity getEventsReport();

	ApiResponseEntity getTodaysCollection();

}
