package mission.fastlmsmission.course.service.impl;

import lombok.RequiredArgsConstructor;
import mission.fastlmsmission.admin.mapper.CourseMapper;
import mission.fastlmsmission.course.dto.CourseDto;
import mission.fastlmsmission.course.entity.Course;
import mission.fastlmsmission.course.entity.TakeCourse;
import mission.fastlmsmission.course.model.CourseInput;
import mission.fastlmsmission.course.model.CourseParam;
import mission.fastlmsmission.course.model.ServiceResult;
import mission.fastlmsmission.course.model.TakeCourseInput;
import mission.fastlmsmission.course.repository.CourseRepository;
import mission.fastlmsmission.course.repository.TakeCourseRepository;
import mission.fastlmsmission.course.service.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static mission.fastlmsmission.course.entity.TakeCourse.*;
import static mission.fastlmsmission.course.entity.TakeCourseCode.STATUS_COMPLETE;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final TakeCourseRepository takeCourseRepository;

    @Override
    @Transactional
    public boolean add(CourseInput parameter) {
        Optional<Course> optionalCourse = courseRepository.findBySubject(parameter.getSubject());
        if (optionalCourse.isPresent()) {
            return false;
        }

        Course course = Course.builder()
                .subject(parameter.getSubject())
                .categoryId(parameter.getCategoryId())
                .regDt(LocalDateTime.now())
                .keyword(parameter.getKeyword())
                .imagePath(parameter.getImagePath())
                .summary(parameter.getSummary())
                .price(parameter.getPrice())
                .salePrice(parameter.getSalePrice())
                .udtDt(parameter.getUdtDt())
                .contents(parameter.getContents())
                .fileName(parameter.getSaveFileName())
                .urlFileName(parameter.getUrlFileName())
                .build();

        course.setSaleEndDt(parameter.getSaleEndDt());
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
            for (CourseDto c : list) {
                c.setTotalCount(totalCount);
                c.setSeq(totalCount - parameter.getPageStart() - i);
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
        course.setCategoryId(parameter.getCategoryId());
        course.setContents(parameter.getContents());
        course.setImagePath(parameter.getImagePath());
        course.setPrice(parameter.getPrice());
        course.setSaleEndDt(parameter.getSaleEndDt());
        course.setSalePrice(parameter.getSalePrice());
        course.setUdtDt(parameter.getUdtDt());
        course.setRegDt(parameter.getRegDt());
        course.setKeyword(parameter.getKeyword());
        course.setSummary(parameter.getSummary());
        course.setFileName(parameter.getSaveFileName());
        course.setUrlFileName(parameter.getUrlFileName());

        return true;
    }

    @Override
    @Transactional
    public boolean del(String idList) {
        if (idList != null && idList.length() > 0) {
            String[] ids = idList.split(",");
            long id = 0;
            for (String s : ids) {
                try {
                    id = Long.parseLong(s);
                } catch (Exception e) { // NumberFormatException 대비

                }
                if (id > 0) {
                    courseRepository.deleteById(id);
                }
            }
        }
        return true;
    }

    @Override
    @Transactional
    public List<CourseDto> frontList(CourseParam parameter) {
        if (parameter.getCategoryId() < 1) {
            List<Course> courseList = courseRepository.findAll();
            System.out.println("courseList = " + courseList);
            return CourseDto.of(courseList);
        }

        Optional<List<Course>> optionalCourses = courseRepository.findByCategoryId(parameter.getCategoryId());
        if (optionalCourses.isPresent()) {
            System.out.println("optionalCourses = " + optionalCourses);
            return CourseDto.of(optionalCourses.get());
        }
        return null;
    }

    @Override
    @Transactional
    public CourseDto frontDetail(long id) {
        return courseRepository.findById(id).map(CourseDto::of).orElse(null);
    }

    @Override
    @Transactional
    public ServiceResult req(TakeCourseInput parameter) {
        ServiceResult result = new ServiceResult();

        Optional<Course> optionalCourse = courseRepository.findById(parameter.getCourseId());
        if (optionalCourse.isEmpty()) {
            result.setResult(false);
            result.setMessage("강좌 정보가 존재하지 않습니다.");
            return result;
        }
        Course course = optionalCourse.get();

        String[] statusList = {STATUS_REQ, STATUS_COMPLETE};

        // 동일한 유저아이디로 동일한 강좌를 신청했고 신청 상태가 REQ, COMPLETE일 경우 중복신청 방지
        //CourseId와 UserId와 Status가 서로끼리 같은 경우의 횟수를 셈
        long count = takeCourseRepository.countByCourseIdAndUserIdAndStatusIn(course.getId(), parameter.getUserId(),
                Arrays.asList(statusList));

        if (count > 0) {
            result.setResult(false);
            result.setMessage("이미 신청한 강좌 정보가 존재합니다.");
            return result;
        }


        TakeCourse takeCourse = builder()
                .courseId(course.getId())
                .userId(parameter.getUserId())
                .payPrice(course.getSalePrice())
                .regDt(LocalDateTime.now())
                .status(STATUS_REQ)
                .build();

        takeCourseRepository.save(takeCourse);

        result.setResult(true);
        result.setMessage("");

        return result;
    }

    @Override
    @Transactional
    public List<CourseDto> listAll() {
        List<Course> courseList = courseRepository.findAll();
        return CourseDto.of(courseList);
    }
}

