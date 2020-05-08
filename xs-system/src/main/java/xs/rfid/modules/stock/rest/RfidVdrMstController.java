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
import xs.rfid.modules.stock.domain.RfidVdrMst;
import xs.rfid.modules.stock.service.RfidVdrMstService;
import xs.rfid.modules.stock.service.dto.RfidVdrMstQueryCriteria;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://docs.auauz.net
* @description /
* @author lwx
* @date 2020-05-07
**/
@Api(tags = "客户管理")
@RestController
@RequestMapping("/api/rfidVdrMst")
public class RfidVdrMstController {

    private final RfidVdrMstService rfidVdrMstService;

    public RfidVdrMstController(RfidVdrMstService rfidVdrMstService) {
        this.rfidVdrMstService = rfidVdrMstService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rfidVdrMst:list')")
    public void download(HttpServletResponse response, RfidVdrMstQueryCriteria criteria) throws IOException {
        rfidVdrMstService.download(rfidVdrMstService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询客户")
    @ApiOperation("查询客户")
    @PreAuthorize("@el.check('rfidVdrMst:list')")
    public ResponseEntity<Object> getRfidVdrMsts(RfidVdrMstQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(rfidVdrMstService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增客户")
    @ApiOperation("新增客户")
    @PreAuthorize("@el.check('rfidVdrMst:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RfidVdrMst resources){
        return new ResponseEntity<>(rfidVdrMstService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改客户")
    @ApiOperation("修改客户")
    @PreAuthorize("@el.check('rfidVdrMst:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody RfidVdrMst resources){
        rfidVdrMstService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除客户")
    @ApiOperation("删除客户")
    @PreAuthorize("@el.check('rfidVdrMst:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        rfidVdrMstService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}