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


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.util.IdUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xs.rfid.modules.stock.domain.RfidLocMst;
import xs.rfid.modules.stock.repository.RfidLocMstRepository;
import xs.rfid.modules.stock.service.RfidLocMstService;
import xs.rfid.modules.stock.service.dto.RfidLocMstDto;
import xs.rfid.modules.stock.service.dto.RfidLocMstQueryCriteria;
import xs.rfid.modules.stock.service.mapper.RfidLocMstMapper;
import xs.rfid.utils.*;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://docs.auauz.net
* @description /
* @author lwx
* @date 2020-05-06
**/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RfidLocMstServiceImpl implements RfidLocMstService {

    private final RfidLocMstRepository rfidLocMstRepository;

    private final RfidLocMstMapper rfidLocMstMapper;

    public RfidLocMstServiceImpl(RfidLocMstRepository rfidLocMstRepository, RfidLocMstMapper rfidLocMstMapper) {
        this.rfidLocMstRepository = rfidLocMstRepository;
        this.rfidLocMstMapper = rfidLocMstMapper;
    }

    @Override
    public Map<String,Object> queryAll(RfidLocMstQueryCriteria criteria, Pageable pageable){
        Page<RfidLocMst> page = rfidLocMstRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(rfidLocMstMapper::toDto));
    }

    @Override
    public List<RfidLocMstDto> queryAll(RfidLocMstQueryCriteria criteria){
        return rfidLocMstMapper.toDto(rfidLocMstRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public RfidLocMstDto findById(Long id) {
        RfidLocMst rfidLocMst = rfidLocMstRepository.findById(id).orElseGet(RfidLocMst::new);
        ValidationUtil.isNull(rfidLocMst.getId(),"RfidLocMst","id",id);
        return rfidLocMstMapper.toDto(rfidLocMst);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RfidLocMstDto create(RfidLocMst resources) {
        resources.setIsDelete("0");
        resources.setLocationCod(NOUtils.nextItemNo("K"));
        return rfidLocMstMapper.toDto(rfidLocMstRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RfidLocMst resources) {
        RfidLocMst rfidLocMst = rfidLocMstRepository.findById(resources.getId()).orElseGet(RfidLocMst::new);
        ValidationUtil.isNull( rfidLocMst.getId(),"RfidLocMst","id",resources.getId());
        rfidLocMst.copy(resources);
        rfidLocMstRepository.save(rfidLocMst);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            rfidLocMstRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RfidLocMstDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RfidLocMstDto rfidLocMst : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("库位编号", rfidLocMst.getLocationCod());
            map.put("库位所属区域", rfidLocMst.getAreaCod());
            map.put("库位名称", rfidLocMst.getLocationName());
            map.put("是否删除", rfidLocMst.getIsDelete());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}