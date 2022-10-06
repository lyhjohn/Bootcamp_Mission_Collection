package mission.fastlmsmission.member.history.service;

import mission.fastlmsmission.course.model.ServiceResult;
import mission.fastlmsmission.member.history.dto.HistoryDto;
import mission.fastlmsmission.member.history.entity.History;

public interface HistoryService {
    ServiceResult saveHistory(String userId, String ip, String userAgent);
}
