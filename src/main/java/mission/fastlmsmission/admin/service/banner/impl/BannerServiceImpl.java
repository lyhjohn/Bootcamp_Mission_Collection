package mission.fastlmsmission.admin.service.banner.impl;

import lombok.RequiredArgsConstructor;
import mission.fastlmsmission.admin.dto.banner.BannerDto;
import mission.fastlmsmission.admin.dto.member.MemberDto;
import mission.fastlmsmission.admin.entity.Banner;
import mission.fastlmsmission.admin.mapper.BannerMapper;
import mission.fastlmsmission.admin.model.banner.BannerInput;
import mission.fastlmsmission.admin.model.banner.BannerParam;
import mission.fastlmsmission.admin.repository.banner.BannerRepository;
import mission.fastlmsmission.admin.savefile.SaveFile;
import mission.fastlmsmission.admin.service.banner.BannerService;
import mission.fastlmsmission.course.model.ServiceResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;

    @Override
    @Transactional
    public List<BannerDto> bannerList(BannerParam parameter) {
        Optional<List<Banner>> optionalList = bannerRepository.findAllByOrderBySeqAsc();
        if (optionalList.isEmpty()) {
            return null;
        }
        long totalCount = bannerMapper.selectListCount(parameter);

        List<Banner> bannerList = optionalList.get();

        List<BannerDto> banners = BannerDto.of(bannerList);
        int i = 0;
        for (BannerDto x : banners) {
            x.setTotalCount(totalCount);
            x.setNum(totalCount - parameter.getPageStart() - i);
            i++;
        }

        return banners;
    }

    @Override
    @Transactional
    public ServiceResult add(BannerInput parameter, MultipartFile file) {

        if (file == null || file.getOriginalFilename() == null) {
            return new ServiceResult(false, "파일이 존재하지 않습니다.");
        }

        // 파일 저장 메서드
        String[] fileNames = SaveFile.saveFilename(file);

        Optional<Banner> findByName = bannerRepository.findByName(parameter.getName());
        if (findByName.isPresent()) {
            return new ServiceResult(false, "동일한 이름의 배너가 존재합니다.");
        }

        if (fileNames == null) {
            return new ServiceResult(false, "파일이 존재하지 않습니다.");
        }


        Banner banner = Banner.createBanner(parameter, fileNames);
        bannerRepository.save(banner);


        BannerDto.of(banner);

        return new ServiceResult();
    }

    @Override
    @Transactional
    public ServiceResult delete(BannerParam param) {

        List<Long> idList = param.getIdList();

        for (Long id : idList) {
            if (idList.size() < 1) {
                return new ServiceResult(false, "삭제할 배너를 선택하세요.");

            }
            Optional<Banner> optionalBanner = bannerRepository.findById(id);

            if (optionalBanner.isEmpty()) {
                return new ServiceResult(false, "삭제할 배너가 존재하지 않습니다.");
            }

            bannerRepository.delete(optionalBanner.get());

        }

        return new ServiceResult();
    }

    @Override
    @Transactional
    public BannerDto findOne(BannerInput param) {
        Optional<Banner> optionalBanner = bannerRepository.findById(param.getId());
        if (optionalBanner.isEmpty()) {
            return null;
        }

        return BannerDto.of(optionalBanner.get());
    }

    @Override
    @Transactional
    public ServiceResult update(BannerInput parameter, MultipartFile file) {

        if (file == null) {
            return new ServiceResult(false, "파일이 존재하지 않습니다.");
        }

        Optional<Banner> optionalBanner = bannerRepository.findById(parameter.getId());
        if (optionalBanner.isEmpty()) {
            return new ServiceResult(false, "배너가 존재하지 않습니다.");
        }

        Banner banner = optionalBanner.get();

        // 파일 저장 메서드
        String[] fileNames = SaveFile.saveFilename(file);

        // 엔티티 업데이트 메서드
        banner.updateBanner(parameter, fileNames);

        return new ServiceResult();
    }
}
