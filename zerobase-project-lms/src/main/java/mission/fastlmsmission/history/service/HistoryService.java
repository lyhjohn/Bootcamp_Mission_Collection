package mission.fastlmsmission.history.service;

import mission.fastlmsmission.course.model.ServiceResult;

public interface HistoryService {
    ServiceResult saveHistory(String userId, String ip, String userAgent);
}
