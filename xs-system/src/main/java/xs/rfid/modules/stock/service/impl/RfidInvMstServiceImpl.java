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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xs.rfid.modules.stock.domain.RfidInvMst;
import xs.rfid.modules.stock.domain.RfidInvTrn;
import xs.rfid.modules.stock.repository.RfidInvMstRepository;
import xs.rfid.modules.stock.service.RfidInvMstService;
import xs.rfid.modules.stock.service.RfidInvTrnService;
import xs.rfid.modules.stock.service.dto.RfidInvMstDto;
import xs.rfid.modules.stock.service.dto.RfidInvMstQueryCriteria;
import xs.rfid.modules.stock.service.mapper.RfidInvMstMapper;
import xs.rfid.utils.FileUtil;
import xs.rfid.utils.PageUtil;
import xs.rfid.utils.QueryHelp;
import xs.rfid.utils.ValidationUtil;

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
public class RfidInvMstServiceImpl implements RfidInvMstService {

    private final RfidInvMstRepository rfidInvMstRepository;

    private final RfidInvMstMapper rfidInvMstMapper;

    private final RfidInvTrnService rfidInvTrnService;

    public RfidInvMstServiceImpl(RfidInvMstRepository rfidInvMstRepository, RfidInvMstMapper rfidInvMstMapper, RfidInvTrnService rfidInvTrnService) {
        this.rfidInvMstRepository = rfidInvMstRepository;
        this.rfidInvMstMapper = rfidInvMstMapper;
        this.rfidInvTrnService = rfidInvTrnService;
    }

    @Override
    public Map<String,Object> queryAll(RfidInvMstQueryCriteria criteria, Pageable pageable){
        Page<RfidInvMst> page = rfidInvMstRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(rfidInvMstMapper::toDto));
    }

    @Override
    public List<RfidInvMstDto> queryAll(RfidInvMstQueryCriteria criteria){
        return rfidInvMstMapper.toDto(rfidInvMstRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public RfidInvMstDto findById(Long id) {
        RfidInvMst rfidInvMst = rfidInvMstRepository.findById(id).orElseGet(RfidInvMst::new);
        ValidationUtil.isNull(rfidInvMst.getId(),"RfidInvMst","id",id);
        return rfidInvMstMapper.toDto(rfidInvMst);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RfidInvMstDto create(RfidInvMst resources) {
        RfidInvMst save = rfidInvMstRepository.save(resources);
        return rfidInvMstMapper.toDto(save);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RfidInvMst resources) {
        RfidInvMst rfidInvMst = rfidInvMstRepository.findById(resources.getId()).orElseGet(RfidInvMst::new);
        ValidationUtil.isNull( rfidInvMst.getId(),"RfidInvMst","id",resources.getId());
        rfidInvMst.copy(resources);

        // 库存事务添加
        RfidInvTrn rfidInvTrn = new RfidInvTrn();
        rfidInvTrn.setGiftCod(rfidInvMst.getGiftCod());
        rfidInvTrn.setLocationCod(rfidInvMst.getLocationCod());
        rfidInvTrn.setTransCnt("1");
        rfidInvTrn.setTransType("1"); // 入库
        rfidInvTrn.setTransDat(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        rfidInvTrn.setStatus("1"); // 未上架
        rfidInvTrn.setIsDelete("0");
        rfidInvTrnService.create(rfidInvTrn);


        rfidInvMstRepository.save(rfidInvMst);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            rfidInvMstRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RfidInvMstDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RfidInvMstDto rfidInvMst : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("礼品编号", rfidInvMst.getGiftCod());
            map.put("库位编号", rfidInvMst.getLocationCod());
            map.put("库存数量", rfidInvMst.getInventoryCnt());
            map.put("最后变更时间", rfidInvMst.getLastChangeDat());
            map.put("最后变更用户", rfidInvMst.getLastChangeUserName());
            map.put("最后变更编号", rfidInvMst.getLastChangeTransCod());
            map.put("是否删除", rfidInvMst.getIsDelete());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}