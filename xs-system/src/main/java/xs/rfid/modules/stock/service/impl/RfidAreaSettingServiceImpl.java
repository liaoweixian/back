package xs.rfid.modules.stock.service.impl;

import cn.hutool.core.date.DateUtil;
import xs.rfid.modules.stock.domain.RfidAreaSetting;
import xs.rfid.modules.stock.repository.RfidAreaSettingRepository;
import xs.rfid.modules.stock.service.RfidAreaSettingService;
import xs.rfid.modules.stock.service.dto.RfidAreaSettingDto;
import xs.rfid.modules.stock.service.dto.RfidAreaSettingQueryCriteria;
import xs.rfid.modules.stock.service.mapper.RfidAreaSettingMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xs.rfid.modules.system.domain.User;
import xs.rfid.modules.system.repository.UserRepository;
import xs.rfid.utils.FileUtil;
import xs.rfid.utils.PageUtil;
import xs.rfid.utils.QueryHelp;
import xs.rfid.utils.ValidationUtil;

import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author liao
* @date 2020-05-10
*/
@Service
//@CacheConfig(cacheNames = "rfidAreaSetting")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RfidAreaSettingServiceImpl implements RfidAreaSettingService {

    private final RfidAreaSettingRepository rfidAreaSettingRepository;

    private final RfidAreaSettingMapper rfidAreaSettingMapper;

    private final UserRepository userRepository;

    public RfidAreaSettingServiceImpl(RfidAreaSettingRepository rfidAreaSettingRepository, RfidAreaSettingMapper rfidAreaSettingMapper, UserRepository userRepository) {
        this.rfidAreaSettingRepository = rfidAreaSettingRepository;
        this.rfidAreaSettingMapper = rfidAreaSettingMapper;
        this.userRepository = userRepository;
    }

    @Override
    //@Cacheable
    public Map<String,Object> queryAll(RfidAreaSettingQueryCriteria criteria, Pageable pageable){
        Page<RfidAreaSetting> page = rfidAreaSettingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(rfidAreaSettingMapper::toDto));
    }

    @Override
    //@Cacheable
    public List<RfidAreaSettingDto> queryAll(RfidAreaSettingQueryCriteria criteria){
        return rfidAreaSettingMapper.toDto(rfidAreaSettingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    //@Cacheable(key = "#p0")
    public RfidAreaSettingDto findById(Long id) {
        RfidAreaSetting rfidAreaSetting = rfidAreaSettingRepository.findById(id).orElseGet(RfidAreaSetting::new);
        ValidationUtil.isNull(rfidAreaSetting.getId(),"RfidAreaSetting","id",id);
        return rfidAreaSettingMapper.toDto(rfidAreaSetting);
    }

    @Override
    //@CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public RfidAreaSettingDto create(RfidAreaSetting resources) {
        User user = userRepository.findById(resources.getUserId()).get();
        resources.setUserId(user.getId());
        resources.setUserName(user.getUsername());
        resources.setCreatedTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return rfidAreaSettingMapper.toDto(rfidAreaSettingRepository.save(resources));
    }

    @Override
    //@CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(RfidAreaSetting resources) {
        RfidAreaSetting rfidAreaSetting = rfidAreaSettingRepository.findById(resources.getId()).orElseGet(RfidAreaSetting::new);
        ValidationUtil.isNull( rfidAreaSetting.getId(),"RfidAreaSetting","id",resources.getId());
        rfidAreaSetting.copy(resources);
        User user = userRepository.findById(rfidAreaSetting.getUserId()).get();
        rfidAreaSetting.setUserId(user.getId());
        rfidAreaSetting.setUserName(user.getUsername());
        rfidAreaSettingRepository.save(rfidAreaSetting);
    }

    @Override
    //@CacheEvict(allEntries = true)
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            rfidAreaSettingRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RfidAreaSettingDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RfidAreaSettingDto rfidAreaSetting : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("区域名", rfidAreaSetting.getAreaName());
            map.put("ip地址", rfidAreaSetting.getIp());
            map.put("用户id", rfidAreaSetting.getUserId());
            map.put(" createdTime",  rfidAreaSetting.getCreatedTime());
            map.put("用户名", rfidAreaSetting.getUserName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}