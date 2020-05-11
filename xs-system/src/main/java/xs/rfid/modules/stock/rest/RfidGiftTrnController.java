package xs.rfid.modules.stock.rest;

import xs.rfid.aop.log.Log;
import xs.rfid.modules.stock.domain.RfidGiftTrn;
import xs.rfid.modules.stock.service.RfidGiftTrnService;
import xs.rfid.modules.stock.service.dto.RfidGiftTrnQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author liao
* @date 2020-05-08
*/
@Api(tags = "订单管理管理")
@RestController
@RequestMapping("/api/rfidGiftTrn")
public class RfidGiftTrnController {

    private final RfidGiftTrnService rfidGiftTrnService;

    public RfidGiftTrnController(RfidGiftTrnService rfidGiftTrnService) {
        this.rfidGiftTrnService = rfidGiftTrnService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rfidGiftTrn:list')")
    public void download(HttpServletResponse response, RfidGiftTrnQueryCriteria criteria) throws IOException {
        rfidGiftTrnService.download(rfidGiftTrnService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询订单管理")
    @ApiOperation("查询订单管理")
    // @PreAuthorize("@el.check('rfidGiftTrn:list')")
    public ResponseEntity<Object> getRfidGiftTrns(RfidGiftTrnQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(rfidGiftTrnService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增订单管理")
    @ApiOperation("新增订单管理")
    @PreAuthorize("@el.check('rfidGiftTrn:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RfidGiftTrn resources){
        return new ResponseEntity<>(rfidGiftTrnService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改订单管理")
    @ApiOperation("修改订单管理")
    @PreAuthorize("@el.check('rfidGiftTrn:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody RfidGiftTrn resources){
        rfidGiftTrnService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除订单管理")
    @ApiOperation("删除订单管理")
    @PreAuthorize("@el.check('rfidGiftTrn:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        rfidGiftTrnService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}