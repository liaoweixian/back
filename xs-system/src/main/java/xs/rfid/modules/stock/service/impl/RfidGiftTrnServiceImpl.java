package xs.rfid.modules.stock.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import xs.rfid.modules.stock.dao.RfidGiftTrnDao;
import xs.rfid.modules.stock.domain.*;
import xs.rfid.modules.stock.repository.RfidGiftTrnRepository;
import xs.rfid.modules.stock.repository.RfidInvMstRepository;
import xs.rfid.modules.stock.repository.RfidVdrMstRepository;
import xs.rfid.modules.stock.service.RfidGiftTrnService;
import xs.rfid.modules.stock.service.RfidInvTrnService;
import xs.rfid.modules.stock.service.dto.RfidGiftTrnDto;
import xs.rfid.modules.stock.service.dto.RfidGiftTrnQueryCriteria;
import xs.rfid.modules.stock.service.dto.RfidInvTrnDto;
import xs.rfid.modules.stock.service.mapper.RfidGiftTrnMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import xs.rfid.utils.*;

import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author liao
* @date 2020-05-08
*/
@Service
//@CacheConfig(cacheNames = "rfidGiftTrn")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RfidGiftTrnServiceImpl implements RfidGiftTrnService {

    private final RfidGiftTrnRepository rfidGiftTrnRepository;

    private final RfidGiftTrnMapper rfidGiftTrnMapper;

    private final RfidInvMstRepository rfidInvMstRepository;

    private final RfidInvTrnService rfidInvTrnService;

    private final RfidGiftTrnDao rfidGiftTrnDao;

    private final RfidVdrMstRepository rfidVdrMstRepository;

    public RfidGiftTrnServiceImpl(RfidGiftTrnRepository rfidGiftTrnRepository, RfidGiftTrnMapper rfidGiftTrnMapper, RfidInvMstRepository rfidInvMstRepository, RfidInvTrnService rfidInvTrnService, RfidGiftTrnDao rfidGiftTrnDao, RfidVdrMstRepository rfidVdrMstRepository) {
        this.rfidGiftTrnRepository = rfidGiftTrnRepository;
        this.rfidGiftTrnMapper = rfidGiftTrnMapper;
        this.rfidInvMstRepository = rfidInvMstRepository;
        this.rfidInvTrnService = rfidInvTrnService;
        this.rfidGiftTrnDao = rfidGiftTrnDao;
        this.rfidVdrMstRepository = rfidVdrMstRepository;
    }

    @Override
    //@Cacheable
    public Map<String,Object> queryAll(RfidGiftTrnQueryCriteria criteria, Pageable pageable){
        //Page<RfidGiftTrn> page = rfidGiftTrnRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<RfidGiftTrn> page =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageable.getPageNumber(), pageable.getPageSize());
        IPage<RfidGiftMst> iPage = rfidGiftTrnDao.findPage(page, criteria);
        return PageUtil.toPage(iPage);
    }

    @Override
    //@Cacheable
    public List<RfidGiftTrnDto> queryAll(RfidGiftTrnQueryCriteria criteria){
        return rfidGiftTrnMapper.toDto(rfidGiftTrnRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    //@Cacheable(key = "#p0")
    public RfidGiftTrnDto findById(Long id) {
        RfidGiftTrn rfidGiftTrn = rfidGiftTrnRepository.findById(id).orElseGet(RfidGiftTrn::new);
        ValidationUtil.isNull(rfidGiftTrn.getId(),"RfidGiftTrn","id",id);
        return rfidGiftTrnMapper.toDto(rfidGiftTrn);
    }

    @Override
    //@CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public RfidGiftTrnDto create(RfidGiftTrn resources) {

        // 订单数据包装
        Date date = new Date();
        String giftCod = resources.getGiftCod();
        // 获取库存信息
        RfidInvMst warehouse = rfidInvMstRepository.findByGiftCod(giftCod);
        resources.setLocationCod(warehouse.getLocationCod());
        resources.setGiftCnt("1"); // 礼品数量
        resources.setStatus("1"); // 1、选择下单 2、已备货、3、已上架、4、已生产、5、已打印、6、已领取、7、返库
        if (!Optional.ofNullable(resources.getId()).isPresent()) {
            resources.setCreateName(SecurityUtils.getUsername());
            resources.setCreateTime(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss"));
        }

        // 插入用户信息
        RfidVdrMst member = rfidVdrMstRepository.findByClientCod(resources.getClientCod());
        resources.setClientName(member.getClientName());

        // 扣减库存数量
        Integer inventoryCnt = Integer.parseInt(warehouse.getInventoryCnt());
        warehouse.setInventoryCnt(String.valueOf((inventoryCnt-1)));
        rfidInvMstRepository.save(warehouse);

        // 生成库存事务
        RfidInvTrn rfidInvTrn = new RfidInvTrn();
        rfidInvTrn.setGiftCod(warehouse.getGiftCod());
        rfidInvTrn.setLocationCod(warehouse.getLocationCod());
        rfidInvTrn.setTransCnt("1"); // 事务数量
        rfidInvTrn.setTransType("2"); // 1、入库 2、出库
        rfidInvTrn.setStatus("1"); // 1、未上架 2、已上架 3、已下架 4、已出库 5、已返库
        rfidInvTrn.setTransDat(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss"));
        rfidInvTrn.setIsDelete("0");
        RfidInvTrnDto rfidInvTrnDto = rfidInvTrnService.create(rfidInvTrn);
        resources.setTransCod(rfidInvTrnDto.getId().toString());
        return rfidGiftTrnMapper.toDto(rfidGiftTrnRepository.save(resources));
    }

    @Override
    //@CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(RfidGiftTrn resources) {
        RfidGiftTrn rfidGiftTrn = rfidGiftTrnRepository.findById(resources.getId()).orElseGet(RfidGiftTrn::new);
        resources.setUpdateTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        resources.setUpdateName(SecurityUtils.getUsername());
        ValidationUtil.isNull( rfidGiftTrn.getId(),"RfidGiftTrn","id",resources.getId());
        rfidGiftTrn.copy(resources);
        rfidGiftTrnRepository.save(rfidGiftTrn);
       /* if (resources.getGiftCod().equals(rfidGiftTrn.getGiftCod())) {

        } else {
            this.create(resources);
        }*/
    }

    @Override
    //@CacheEvict(allEntries = true)
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            rfidGiftTrnRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RfidGiftTrnDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RfidGiftTrnDto rfidGiftTrn : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("客户名称", rfidGiftTrn.getClientCod());
            map.put("礼品编号", rfidGiftTrn.getGiftCod());
            map.put("库位编码", rfidGiftTrn.getLocationCod());
            map.put("数量", rfidGiftTrn.getGiftCnt());
            map.put("状态（选择下单、已备货、已上架、已生产、已打印、已领取、返库）", rfidGiftTrn.getStatus());
            map.put("库存事务编号", rfidGiftTrn.getTransCod());
            map.put("是否删除", rfidGiftTrn.getIsDelete());
            map.put("推荐货位编号", rfidGiftTrn.getToLocationCod());
            map.put("订单编号", rfidGiftTrn.getOrderSn());
            map.put(" createTime",  rfidGiftTrn.getCreateTime());
            map.put(" createName",  rfidGiftTrn.getCreateName());
            map.put(" updateTime",  rfidGiftTrn.getUpdateTime());
            map.put(" updateName",  rfidGiftTrn.getUpdateName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}