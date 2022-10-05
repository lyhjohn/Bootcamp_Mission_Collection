package mission.fastlmsmission.course.service.impl;

import lombok.RequiredArgsConstructor;
import mission.fastlmsmission.admin.mapper.TakeCourseMapper;
import mission.fastlmsmission.course.dto.TakeCourseDto;
import mission.fastlmsmission.course.entity.Course;
import mission.fastlmsmission.course.entity.TakeCourse;
import mission.fastlmsmission.course.entity.TakeCourseCode;
import mission.fastlmsmission.course.model.ServiceResult;
import mission.fastlmsmission.course.model.TakeCourseParam;
import mission.fastlmsmission.course.repository.TakeCourseRepository;
import mission.fastlmsmission.course.service.TakeCourseService;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TakeCourseServiceImpl implements TakeCourseService {
    private final TakeCourseRepository takeCourseRepository;
    private final TakeCourseMapper takeCourseMapper;

    @Override
    @Transactional
    public List<TakeCourseDto> list(TakeCourseParam parameter) {
        long totalCount = takeCourseMapper.selectListCount(parameter);
        List<TakeCourseDto> list = takeCourseMapper.selectList(parameter);
        System.out.println("list = " + list);


        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (TakeCourseDto c : list) {
                c.setTotalCount(totalCount);
                c.setSeq(totalCount - parameter.getPageStart() - i);
                i++;
            }
        }
        return list;
    }

    @Override
    @Transactional
    public ServiceResult updateStatus(long id, String status) {
        Optional<TakeCourse> optionalTakeCourse = takeCourseRepository.findById(id);

        if (optionalTakeCourse.isEmpty()) {
            return new ServiceResult(false, "수강 정보가 존재하지 앖습니다.");
        }

        optionalTakeCourse.get().setStatus(status);

        return new ServiceResult(true);
    }

    @Override
    @Transactional
    public List<TakeCourseDto> myCourseList(String userId) {

        TakeCourseParam parameter = new TakeCourseParam();
        parameter.setUserId(userId);
        return takeCourseMapper.selectListMyCourse(parameter);
    }

    @Override
    @Transactional
    public TakeCourseDto detail(long id) {
        Optional<TakeCourse> optionalTakeCourse = takeCourseRepository.findById(id);
        return optionalTakeCourse.map(TakeCourseDto::of).orElse(null);
    }

    @Override
    @Transactional
    public ServiceResult cancel(long id) {
        Optional<TakeCourse> optionalTakeCourse = takeCourseRepository.findById(id);
        if (optionalTakeCourse.isEmpty()) {
            return new ServiceResult(false, "수강 정보가 존재하지 않습니다.");
        }
        TakeCourse takeCourse = optionalTakeCourse.get();
        takeCourse.setStatus(TakeCourseCode.STATUS_CANCEL);

        return new ServiceResult();
    }
}

