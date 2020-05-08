/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package xs.rfid.modules.stock.service.impl;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.util.IdUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xs.rfid.modules.stock.dao.RfidGiftMstDao;
import xs.rfid.modules.stock.domain.RfidGiftMst;
import xs.rfid.modules.stock.domain.RfidInvMst;
import xs.rfid.modules.stock.repository.RfidGiftMstRepository;
import xs.rfid.modules.stock.service.RfidGiftMstService;
import xs.rfid.modules.stock.service.RfidInvMstService;
import xs.rfid.modules.stock.service.dto.RfidGiftMstDto;
import xs.rfid.modules.stock.service.dto.RfidGiftMstQueryCriteria;
import xs.rfid.modules.stock.service.dto.RfidInvMstDto;
import xs.rfid.modules.stock.service.mapper.RfidGiftMstMapper;
import xs.rfid.modules.stock.service.mapper.RfidInvMstMapper;
import xs.rfid.utils.*;

import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://docs.auauz.net
* @description /
* @author lwx
* @date 2020-05-06
**/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RfidGiftMstServiceImpl implements RfidGiftMstService {

    private final RfidGiftMstRepository rfidGiftMstRepository;

    private final RfidGiftMstMapper rfidGiftMstMapper;

    private final RfidInvMstService rfidInvMstService;

    private final RfidInvMstMapper rfidInvMstMapper;

    private final RfidGiftMstDao rfidGiftMstDao;

    public RfidGiftMstServiceImpl(RfidGiftMstRepository rfidGiftMstRepository, RfidGiftMstMapper rfidGiftMstMapper, RfidInvMstService rfidInvMstService, RfidInvMstMapper rfidInvMstMapper, RfidGiftMstDao rfidGiftMstDao) {
        this.rfidGiftMstRepository = rfidGiftMstRepository;
        this.rfidGiftMstMapper = rfidGiftMstMapper;
        this.rfidInvMstService = rfidInvMstService;
        this.rfidInvMstMapper = rfidInvMstMapper;
        this.rfidGiftMstDao = rfidGiftMstDao;
    }

    @Override
    public Map<String,Object> queryAll(RfidGiftMstQueryCriteria criteria, Pageable pageable){
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<RfidGiftMst> page =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageable.getPageNumber(), pageable.getPageSize());
        IPage<RfidGiftMst> all = rfidGiftMstDao.findAll(page, criteria);
        //Page<RfidGiftMst> page = rfidGiftMstRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        // PageUtil.toPage(page.map(rfidGiftMstMapper::toDto));
        return PageUtil.toPage(all);
    }

    @Override
    public List<RfidGiftMstDto> queryAll(RfidGiftMstQueryCriteria criteria){
        return rfidGiftMstMapper.toDto(rfidGiftMstRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public RfidGiftMstDto findById(Long id) {
        RfidGiftMst rfidGiftMst = rfidGiftMstRepository.findById(id).orElseGet(RfidGiftMst::new);
        ValidationUtil.isNull(rfidGiftMst.getId(),"RfidGiftMst","id",id);
        return rfidGiftMstMapper.toDto(rfidGiftMst);
    }

    @Override
    public RfidGiftMstDto findByGiftModel(String giftModel) {
        RfidGiftMst byGiftModel = rfidGiftMstRepository.findByGiftModel(giftModel);
        RfidGiftMstDto rfidGiftMstDto = rfidGiftMstMapper.toDto(byGiftModel);
        return rfidGiftMstDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RfidGiftMstDto create(RfidGiftMst resources) {
        // resources.setId(IdUtil.simpleUUID());
        // @TODO 礼品编号 
        resources.setGiftCod(NOUtils.nextItemNo("G"));
        resources.setRegisterDat(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        RfidGiftMst save = rfidGiftMstRepository.save(resources);
        this.createInvMst(save);
        return rfidGiftMstMapper.toDto(save);
    }

    /**
     * 创建库存
     * @param rfidGiftMst
     * @return
     */
    private RfidInvMstDto createInvMst(RfidGiftMst rfidGiftMst) {
        RfidInvMst stock = new RfidInvMst();
        stock.setGiftCod(rfidGiftMst.getGiftCod());
        // 库存数量
        stock.setInventoryCnt("0");
        // 最后修改时间
        stock.setLastChangeDat(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
        // 最后变更用户
        stock.setLastChangeUserName(SecurityUtils.getUsername());
        // 0 有效
        stock.setIsDelete("0");
        return rfidInvMstService.create(stock);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RfidGiftMst resources) {
        RfidGiftMst rfidGiftMst = rfidGiftMstRepository.findById(resources.getId()).orElseGet(RfidGiftMst::new);
        ValidationUtil.isNull( rfidGiftMst.getId(),"RfidGiftMst","id",resources.getId());
        rfidGiftMst.copy(resources);
        rfidGiftMstRepository.save(rfidGiftMst);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            rfidGiftMstRepository.deleteById(id);
        }
    }

    @Override
    public void discontinue(Long[] ids) {
        ArrayList<RfidGiftMst> list = new ArrayList<>();
        for (Long id : ids) {
            RfidGiftMst rfidGiftMst = rfidGiftMstRepository.findById(id).get();
            // @TODO 停用
            rfidGiftMst.setIsUse("1");
            list.add(rfidGiftMst);
        }
        rfidGiftMstRepository.saveAll(list);
    }

    @Override
    public void download(List<RfidGiftMstDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RfidGiftMstDto rfidGiftMst : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("礼品编号", rfidGiftMst.getGiftCod());
            map.put("礼品名称", rfidGiftMst.getGiftName());
            map.put("礼品类型", rfidGiftMst.getGiftType());
            map.put("型号", rfidGiftMst.getGiftModel());
            map.put("推荐货位", rfidGiftMst.getToLocationCod());
            map.put("停用标志", rfidGiftMst.getIsUse());
            map.put("RFID编码", rfidGiftMst.getRfidCod());
            map.put(" registerDat",  rfidGiftMst.getRegisterDat());
            map.put("到访状态", rfidGiftMst.getVisitSta());
            map.put("是否删除", rfidGiftMst.getIsDelete());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}