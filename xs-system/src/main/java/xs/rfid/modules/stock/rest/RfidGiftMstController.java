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
import org.springframework.web.multipart.MultipartFile;
import xs.rfid.OssTemplate;
import xs.rfid.aop.log.Log;
import xs.rfid.model.RfidFile;
import xs.rfid.modules.stock.domain.RfidGiftMst;
import xs.rfid.modules.stock.service.RfidGiftMstService;
import xs.rfid.modules.stock.service.dto.RfidGiftMstQueryCriteria;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://docs.auauz.net
* @description /
* @author lwx
* @date 2020-05-06
**/
@Api(tags = "礼品管理")
@RestController
@RequestMapping("/api/rfidGiftMst")
public class RfidGiftMstController {

    private final RfidGiftMstService rfidGiftMstService;

    private final OssTemplate ossTemplate;

    public RfidGiftMstController(RfidGiftMstService rfidGiftMstService, OssTemplate ossTemplate) {
        this.rfidGiftMstService = rfidGiftMstService;
        this.ossTemplate = ossTemplate;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rfidGiftMst:list')")
    public void download(HttpServletResponse response, RfidGiftMstQueryCriteria criteria) throws IOException {
        rfidGiftMstService.download(rfidGiftMstService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询礼品")
    @ApiOperation("查询礼品")
    @PreAuthorize("@el.check('rfidGiftMst:list')")
    public ResponseEntity<Object> getRfidGiftMsts(RfidGiftMstQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(rfidGiftMstService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @GetMapping("/getGift")
    @Log("查询礼品")
    @ApiOperation("查询礼品")
    @PreAuthorize("@el.check('rfidGiftMst:list')")
    public ResponseEntity<Object> getRfidGiftMst(RfidGiftMstQueryCriteria criteria){
        return new ResponseEntity<>(rfidGiftMstService.findByGiftModel(criteria.getGiftModel()),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增礼品")
    @ApiOperation("新增礼品")
    @PreAuthorize("@el.check('rfidGiftMst:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RfidGiftMst resources){
        if (!Optional.ofNullable(resources.getIsDelete()).isPresent()) {
            resources.setIsDelete("0");
        }
        if (!Optional.ofNullable(resources.getIsUse()).isPresent()) {
            resources.setIsUse("0");
        }
        return new ResponseEntity<>(rfidGiftMstService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改礼品")
    @ApiOperation("修改礼品")
    @PreAuthorize("@el.check('rfidGiftMst:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody RfidGiftMst resources){
        rfidGiftMstService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除礼品")
    @ApiOperation("删除礼品")
    @PreAuthorize("@el.check('rfidGiftMst:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        rfidGiftMstService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("礼品停用")
    @ApiOperation("礼品停用")
    @PreAuthorize("@el.check('rfidGiftMst:batchDis')")
    @PostMapping("/batch_discontinue")
    public ResponseEntity<Object> batchDiscontinue(@RequestBody Long[] ids) {
        rfidGiftMstService.discontinue(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("图片上传")
    @ApiOperation("图片上传")
    // @PreAuthorize("@el.check('GiftMst:del')")
    @PostMapping("/put-file")
    public ResponseEntity<Object> imgupload(@RequestBody MultipartFile file) {
        RfidFile rfidFile = ossTemplate.putFile(file);
        System.out.println(rfidFile);
        return new ResponseEntity<>(rfidFile,HttpStatus.OK);
    }
}