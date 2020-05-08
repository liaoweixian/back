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
package xs.rfid.modules.stock.rest;


import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import xs.rfid.aop.log.Log;
import xs.rfid.modules.stock.domain.RfidInvMst;
import xs.rfid.modules.stock.service.RfidInvMstService;
import xs.rfid.modules.stock.service.dto.RfidInvMstQueryCriteria;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://docs.auauz.net
* @description /
* @author lwx
* @date 2020-05-06
**/
@Api(tags = "库存信息管理")
@RestController
@RequestMapping("/api/rfidInvMst")
public class RfidInvMstController {

    private final RfidInvMstService rfidInvMstService;

    public RfidInvMstController(RfidInvMstService rfidInvMstService) {
        this.rfidInvMstService = rfidInvMstService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rfidInvMst:list')")
    public void download(HttpServletResponse response, RfidInvMstQueryCriteria criteria) throws IOException {
        rfidInvMstService.download(rfidInvMstService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询库存信息")
    @ApiOperation("查询库存信息")
    @PreAuthorize("@el.check('rfidInvMst:list')")
    public ResponseEntity<Object> getRfidInvMsts(RfidInvMstQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(rfidInvMstService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增库存信息")
    @ApiOperation("新增库存信息")
    @PreAuthorize("@el.check('rfidInvMst:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RfidInvMst resources){
        return new ResponseEntity<>(rfidInvMstService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改库存信息")
    @ApiOperation("修改库存信息")
    @PreAuthorize("@el.check('rfidInvMst:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody RfidInvMst resources){
        rfidInvMstService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除库存信息")
    @ApiOperation("删除库存信息")
    @PreAuthorize("@el.check('rfidInvMst:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        rfidInvMstService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}