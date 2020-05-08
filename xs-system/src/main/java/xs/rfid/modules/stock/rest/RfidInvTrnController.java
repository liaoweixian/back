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
import xs.rfid.modules.stock.domain.RfidInvTrn;
import xs.rfid.modules.stock.service.RfidInvTrnService;
import xs.rfid.modules.stock.service.dto.RfidInvTrnQueryCriteria;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://docs.auauz.net
* @description /
* @author stock
* @date 2020-05-06
**/
@Api(tags = "stock管理")
@RestController
@RequestMapping("/api/rfidInvTrn")
public class RfidInvTrnController {

    private final RfidInvTrnService rfidInvTrnService;

    public RfidInvTrnController(RfidInvTrnService rfidInvTrnService) {
        this.rfidInvTrnService = rfidInvTrnService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rfidInvTrn:list')")
    public void download(HttpServletResponse response, RfidInvTrnQueryCriteria criteria) throws IOException {
        rfidInvTrnService.download(rfidInvTrnService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询stock")
    @ApiOperation("查询stock")
    @PreAuthorize("@el.check('rfidInvTrn:list')")
    public ResponseEntity<Object> getRfidInvTrns(RfidInvTrnQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(rfidInvTrnService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增stock")
    @ApiOperation("新增stock")
    @PreAuthorize("@el.check('rfidInvTrn:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RfidInvTrn resources){
        return new ResponseEntity<>(rfidInvTrnService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改stock")
    @ApiOperation("修改stock")
    @PreAuthorize("@el.check('rfidInvTrn:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody RfidInvTrn resources){
        rfidInvTrnService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除stock")
    @ApiOperation("删除stock")
    @PreAuthorize("@el.check('rfidInvTrn:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        rfidInvTrnService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}