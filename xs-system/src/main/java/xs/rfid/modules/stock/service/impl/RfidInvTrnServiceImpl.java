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
import xs.rfid.modules.stock.domain.RfidInvTrn;
import xs.rfid.modules.stock.repository.RfidInvTrnRepository;
import xs.rfid.modules.stock.service.RfidInvTrnService;
import xs.rfid.modules.stock.service.dto.RfidInvTrnDto;
import xs.rfid.modules.stock.service.dto.RfidInvTrnQueryCriteria;
import xs.rfid.modules.stock.service.mapper.RfidInvTrnMapper;
import xs.rfid.utils.FileUtil;
import xs.rfid.utils.PageUtil;
import xs.rfid.utils.QueryHelp;
import xs.rfid.utils.ValidationUtil;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://docs.auauz.net
* @description /
* @author stock
* @date 2020-05-06
**/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RfidInvTrnServiceImpl implements RfidInvTrnService {

    private final RfidInvTrnRepository rfidInvTrnRepository;

    private final RfidInvTrnMapper rfidInvTrnMapper;

    public RfidInvTrnServiceImpl(RfidInvTrnRepository rfidInvTrnRepository, RfidInvTrnMapper rfidInvTrnMapper) {
        this.rfidInvTrnRepository = rfidInvTrnRepository;
        this.rfidInvTrnMapper = rfidInvTrnMapper;
    }

    @Override
    public Map<String,Object> queryAll(RfidInvTrnQueryCriteria criteria, Pageable pageable){
        Page<RfidInvTrn> page = rfidInvTrnRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(rfidInvTrnMapper::toDto));
    }

    @Override
    public List<RfidInvTrnDto> queryAll(RfidInvTrnQueryCriteria criteria){
        return rfidInvTrnMapper.toDto(rfidInvTrnRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public RfidInvTrnDto findById(Long id) {
        RfidInvTrn rfidInvTrn = rfidInvTrnRepository.findById(id).orElseGet(RfidInvTrn::new);
        ValidationUtil.isNull(rfidInvTrn.getId(),"RfidInvTrn","id",id);
        return rfidInvTrnMapper.toDto(rfidInvTrn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RfidInvTrnDto create(RfidInvTrn resources) {
        return rfidInvTrnMapper.toDto(rfidInvTrnRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RfidInvTrn resources) {
        RfidInvTrn rfidInvTrn = rfidInvTrnRepository.findById(resources.getId()).orElseGet(RfidInvTrn::new);
        ValidationUtil.isNull( rfidInvTrn.getId(),"RfidInvTrn","id",resources.getId());
        rfidInvTrn.copy(resources);
        rfidInvTrnRepository.save(rfidInvTrn);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            rfidInvTrnRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RfidInvTrnDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RfidInvTrnDto rfidInvTrn : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("礼品编号", rfidInvTrn.getGiftCod());
            map.put("库位编码", rfidInvTrn.getLocationCod());
            map.put("事务数量", rfidInvTrn.getTransCnt());
            map.put("事务类型（入库/出库）", rfidInvTrn.getTransType());
            map.put("事务时间", rfidInvTrn.getTransDat());
            map.put("状态（未上架/已上架/已下架/已出库/已返库）", rfidInvTrn.getStatus());
            map.put("是否删除", rfidInvTrn.getIsDelete());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}