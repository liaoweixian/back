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
import xs.rfid.modules.stock.domain.RfidVdrMst;
import xs.rfid.modules.stock.repository.RfidVdrMstRepository;
import xs.rfid.modules.stock.service.RfidVdrMstService;
import xs.rfid.modules.stock.service.dto.RfidVdrMstDto;
import xs.rfid.modules.stock.service.dto.RfidVdrMstQueryCriteria;
import xs.rfid.modules.stock.service.mapper.RfidVdrMstMapper;
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
* @date 2020-05-07
**/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RfidVdrMstServiceImpl implements RfidVdrMstService {

    private final RfidVdrMstRepository rfidVdrMstRepository;

    private final RfidVdrMstMapper rfidVdrMstMapper;

    public RfidVdrMstServiceImpl(RfidVdrMstRepository rfidVdrMstRepository, RfidVdrMstMapper rfidVdrMstMapper) {
        this.rfidVdrMstRepository = rfidVdrMstRepository;
        this.rfidVdrMstMapper = rfidVdrMstMapper;
    }

    @Override
    public Map<String,Object> queryAll(RfidVdrMstQueryCriteria criteria, Pageable pageable){
        Page<RfidVdrMst> page = rfidVdrMstRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(rfidVdrMstMapper::toDto));
    }

    @Override
    public List<RfidVdrMstDto> queryAll(RfidVdrMstQueryCriteria criteria){
        return rfidVdrMstMapper.toDto(rfidVdrMstRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public RfidVdrMstDto findById(Long id) {
        RfidVdrMst rfidVdrMst = rfidVdrMstRepository.findById(id).orElseGet(RfidVdrMst::new);
        ValidationUtil.isNull(rfidVdrMst.getId(),"RfidVdrMst","id",id);
        return rfidVdrMstMapper.toDto(rfidVdrMst);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RfidVdrMstDto create(RfidVdrMst resources) {
        // 到访时间
        resources.setVisitDat(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        RfidVdrMst oldMember = rfidVdrMstRepository.findByRfidCod(resources.getRfidCod());
        if (Optional.ofNullable(oldMember).isPresent()) {
            resources.setId(oldMember.getId());
            resources.setVisitStatus("1");
        }
        return rfidVdrMstMapper.toDto(rfidVdrMstRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RfidVdrMst resources) {
        RfidVdrMst rfidVdrMst = rfidVdrMstRepository.findById(resources.getId()).orElseGet(RfidVdrMst::new);
        ValidationUtil.isNull( rfidVdrMst.getId(),"RfidVdrMst","id",resources.getId());
        rfidVdrMst.copy(resources);
        rfidVdrMstRepository.save(rfidVdrMst);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            rfidVdrMstRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RfidVdrMstDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RfidVdrMstDto rfidVdrMst : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("客户编号", rfidVdrMst.getClientCod());
            map.put("客户名称", rfidVdrMst.getClientName());
            map.put("公司名称", rfidVdrMst.getCompanyName());
            map.put("职务", rfidVdrMst.getPositionName());
            map.put("RFID编码", rfidVdrMst.getRfidCod());
            map.put("到访时间", rfidVdrMst.getVisitDat());
            map.put("访客状态1、预约， 2、到访， 3、已完成", rfidVdrMst.getVisitStatus());
            map.put("客户照片编码", rfidVdrMst.getClientPhotoCod());
            map.put("是否删除", rfidVdrMst.getIsDelete());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}