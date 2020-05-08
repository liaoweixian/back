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
package xs.rfid.modules.stock.service;



import org.springframework.data.domain.Pageable;
import xs.rfid.modules.stock.domain.RfidVdrMst;
import xs.rfid.modules.stock.service.dto.RfidVdrMstDto;
import xs.rfid.modules.stock.service.dto.RfidVdrMstQueryCriteria;

import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://docs.auauz.net
* @description /
* @author lwx
* @date 2020-05-07
**/
public interface RfidVdrMstService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(RfidVdrMstQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<RfidVdrMstDto>
    */
    List<RfidVdrMstDto> queryAll(RfidVdrMstQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return RfidVdrMstDto
     */
    RfidVdrMstDto findById(Long id);

    /**
    * 创建
    * @param resources /
    * @return RfidVdrMstDto
    */
    RfidVdrMstDto create(RfidVdrMst resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(RfidVdrMst resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Long[] ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<RfidVdrMstDto> all, HttpServletResponse response) throws IOException;
}