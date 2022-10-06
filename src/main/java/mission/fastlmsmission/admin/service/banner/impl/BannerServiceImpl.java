package mission.fastlmsmission.admin.service.banner.impl;

import lombok.RequiredArgsConstructor;
import mission.fastlmsmission.admin.dto.banner.BannerDto;
import mission.fastlmsmission.admin.entity.Banner;
import mission.fastlmsmission.admin.repository.banner.BannerRepository;
import mission.fastlmsmission.admin.service.banner.BannerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;

    @Override
    @Transactional
    public List<BannerDto> bannerList() {
        Optional<List<Banner>> optionalList = bannerRepository.findBannerAll();

        if (optionalList.isEmpty() || optionalList.get().isEmpty()) {
            return null;
        }

        List<Banner> bannerList = optionalList.get();
        return BannerDto.of(bannerList);

    }
}
