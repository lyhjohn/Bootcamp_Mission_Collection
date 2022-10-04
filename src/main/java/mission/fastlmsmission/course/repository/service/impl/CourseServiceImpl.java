package mission.fastlmsmission.course.repository.service.impl;

import lombok.RequiredArgsConstructor;
import mission.fastlmsmission.admin.mapper.CourseMapper;
import mission.fastlmsmission.course.dto.CourseDto;
import mission.fastlmsmission.course.entity.Course;
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
                .categoryId(parameter.getCategoryId())
                .regDt(LocalDateTime.now())
                .keyword(parameter.getKeyword())
                .imagePath(parameter.getImagePath())
                .summary(parameter.getSummary())
                .price(parameter.getPrice())
                .salePrice(parameter.getSalePrice())
                .udtDt(parameter.getUdtDt())
                .contents(parameter.getContents())
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
}

