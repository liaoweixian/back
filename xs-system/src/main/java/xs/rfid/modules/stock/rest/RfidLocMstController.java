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
import xs.rfid.modules.stock.domain.RfidLocMst;
import xs.rfid.modules.stock.service.RfidLocMstService;
import xs.rfid.modules.stock.service.dto.RfidLocMstQueryCriteria;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://docs.auauz.net
* @description /
* @author lwx
* @date 2020-05-06
**/
@Api(tags = "库位管理")
@RestController
@RequestMapping("/api/rfidLocMst")
public class RfidLocMstController {

    private final RfidLocMstService rfidLocMstService;

    public RfidLocMstController(RfidLocMstService rfidLocMstService) {
        this.rfidLocMstService = rfidLocMstService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rfidLocMst:list')")
    public void download(HttpServletResponse response, RfidLocMstQueryCriteria criteria) throws IOException {
        rfidLocMstService.download(rfidLocMstService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询库位")
    @ApiOperation("查询库位")
    @PreAuthorize("@el.check('rfidLocMst:list')")
    public ResponseEntity<Object> getRfidLocMsts(RfidLocMstQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(rfidLocMstService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping("/all")
    @Log("查询库位")
    @ApiOperation("查询库位")
    // @PreAuthorize("@el.check('rfidLocMst:list')")
    public ResponseEntity<Object> getRfidLocMstAll(RfidLocMstQueryCriteria criteria){
        return new ResponseEntity<>(rfidLocMstService.queryAll(criteria),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增库位")
    @ApiOperation("新增库位")
    @PreAuthorize("@el.check('rfidLocMst:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RfidLocMst resources){
        return new ResponseEntity<>(rfidLocMstService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改库位")
    @ApiOperation("修改库位")
    @PreAuthorize("@el.check('rfidLocMst:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody RfidLocMst resources){
        rfidLocMstService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除库位")
    @ApiOperation("删除库位")
    @PreAuthorize("@el.check('rfidLocMst:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        rfidLocMstService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}