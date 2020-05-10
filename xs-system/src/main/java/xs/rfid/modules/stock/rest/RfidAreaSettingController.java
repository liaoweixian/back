package xs.rfid.modules.stock.rest;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import xs.rfid.aop.log.Log;
import xs.rfid.modules.mqtt.SendMessage;
import xs.rfid.modules.stock.domain.RfidAreaSetting;
import xs.rfid.modules.stock.domain.RfidVdrMst;
import xs.rfid.modules.stock.repository.RfidAreaSettingRepository;
import xs.rfid.modules.stock.repository.RfidVdrMstRepository;
import xs.rfid.modules.stock.service.RfidAreaSettingService;
import xs.rfid.modules.stock.service.dto.RfidAreaSettingQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

/**
* @author liao
* @date 2020-05-10
*/
@Api(tags = "区域配置管理")
@RestController
@RequestMapping("/api/rfidAreaSetting")
public class RfidAreaSettingController {

    private final RfidAreaSettingService rfidAreaSettingService;

    private final RfidAreaSettingRepository rfidAreaSettingRepository;

    private final RfidVdrMstRepository rfidVdrMstRepository;

    private final SendMessage sendMessage;

    private static final String TOPIC_PREFIX = "/member/info/";

    public RfidAreaSettingController(RfidAreaSettingService rfidAreaSettingService, RfidAreaSettingRepository rfidAreaSettingRepository, RfidVdrMstRepository rfidVdrMstRepository, SendMessage sendMessage) {
        this.rfidAreaSettingService = rfidAreaSettingService;
        this.rfidAreaSettingRepository = rfidAreaSettingRepository;
        this.rfidVdrMstRepository = rfidVdrMstRepository;
        this.sendMessage = sendMessage;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rfidAreaSetting:list')")
    public void download(HttpServletResponse response, RfidAreaSettingQueryCriteria criteria) throws IOException {
        rfidAreaSettingService.download(rfidAreaSettingService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询区域配置")
    @ApiOperation("查询区域配置")
    @PreAuthorize("@el.check('rfidAreaSetting:list')")
    public ResponseEntity<Object> getRfidAreaSettings(RfidAreaSettingQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(rfidAreaSettingService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增区域配置")
    @ApiOperation("新增区域配置")
    @PreAuthorize("@el.check('rfidAreaSetting:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RfidAreaSetting resources){
        return new ResponseEntity<>(rfidAreaSettingService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改区域配置")
    @ApiOperation("修改区域配置")
    @PreAuthorize("@el.check('rfidAreaSetting:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody RfidAreaSetting resources){
        rfidAreaSettingService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除区域配置")
    @ApiOperation("删除区域配置")
    @PreAuthorize("@el.check('rfidAreaSetting:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        rfidAreaSettingService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *  区域读取客户信息，上传显示大屏接口
     * @return
     */
    @Log("区域读取客户信息，上传显示大屏接口")
    @GetMapping("/showMonitor")
    @ApiOperation("区域读取客户信息，上传显示大屏接口")
    public ResponseEntity<Object> showMonitor(RfidAreaSettingQueryCriteria criteria) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", "4000");
        if (StrUtil.isEmpty(criteria.getIp())) {
            map.put("msg", "请上传ip地址");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        if (StrUtil.isEmpty(criteria.getRfidCode())) {
            map.put("msg", "请上传客户身份信息");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        RfidAreaSetting areaSetting = rfidAreaSettingRepository.findByIp(criteria.getIp());
        RfidVdrMst member = rfidVdrMstRepository.findByRfidCod(criteria.getRfidCode());
        Map<String, Object> memberMap = new HashMap<>();
        memberMap.put("cmd", "member");
        memberMap.put("data", member);
        sendMessage.send(
                JSON.toJSONString(memberMap),
                TOPIC_PREFIX + String.valueOf(areaSetting.getUserId()));
        map.put("code","2000");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}