package mission.fastlmsmission.course.repository.service.impl;

import lombok.RequiredArgsConstructor;
import mission.fastlmsmission.admin.dto.MemberDto;
import mission.fastlmsmission.admin.mapper.CourseMapper;
import mission.fastlmsmission.course.dto.CourseDto;
import mission.fastlmsmission.course.entity.Course;
import mission.fastlmsmission.course.exception.CourseException;
import mission.fastlmsmission.course.model.CourseInput;
import mission.fastlmsmission.course.model.CourseParam;
import mission.fastlmsmission.course.repository.CourseRepository;
import mission.fastlmsmission.course.repository.service.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    @Transactional
    public boolean add(CourseInput parameter) {
        Optional<Course> optionalCourse = courseRepository.findBySubject(parameter.getSubject());
        if (optionalCourse.isPresent()) {
            return false;
        }

        Course course = Course.builder()
                .subject(parameter.getSubject())
                .regDt(LocalDateTime.now())
                .build();
        courseRepository.save(course);
        return true;
    }

    @Override
    @Transactional
    public List<CourseDto> list(CourseParam parameter) {

        long totalCount = courseMapper.selectListCount(parameter);
        List<CourseDto> list = courseMapper.selectList(parameter);
        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (CourseDto c: list) {
                c.setTotalCount(totalCount);
                c.setSeq(totalCount - parameter.getStartDataNum() - i);
                i++;
            }
        }

        return list;
    }

    @Override
    @Transactional
    public CourseDto getById(Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        return optionalCourse.map(CourseDto::of).orElse(null);
    }

    @Override
    @Transactional
    public boolean update(CourseInput parameter) {
        Optional<Course> optionalCourse = courseRepository.findById(parameter.getId());
        if (optionalCourse.isEmpty()) {
            return false;
        }
        Course course = optionalCourse.get();
        course.setSubject(parameter.getSubject());
        course.setUdtDt(LocalDateTime.now());

        return true;
    }
}
