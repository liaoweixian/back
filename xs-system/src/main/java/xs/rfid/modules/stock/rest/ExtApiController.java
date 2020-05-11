package xs.rfid.modules.stock.rest;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import xs.rfid.aop.log.Log;
import xs.rfid.modules.mqtt.SendMessage;
import xs.rfid.modules.stock.domain.RfidAreaSetting;
import xs.rfid.modules.stock.domain.RfidGiftMst;
import xs.rfid.modules.stock.domain.RfidGiftTrn;
import xs.rfid.modules.stock.domain.RfidVdrMst;
import xs.rfid.modules.stock.repository.RfidAreaSettingRepository;
import xs.rfid.modules.stock.repository.RfidGiftMstRepository;
import xs.rfid.modules.stock.repository.RfidGiftTrnRepository;
import xs.rfid.modules.stock.repository.RfidVdrMstRepository;
import xs.rfid.modules.stock.service.dto.RfidAreaSettingQueryCriteria;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "对外Api")
@RestController
@RequestMapping("/api/ext")
public class ExtApiController {

    private final RfidGiftTrnRepository rfidGiftTrnRepository;

    private final RfidGiftMstRepository rfidGiftMstRepository;

    private final RfidVdrMstRepository rfidVdrMstRepository;

    private final RfidAreaSettingRepository rfidAreaSettingRepository;

    private final SendMessage sendMessage;

    private static final String TOPIC_PREFIX = "/member/info/";

    public ExtApiController(RfidGiftTrnRepository rfidGiftTrnRepository, RfidGiftMstRepository rfidGiftMstRepository, RfidVdrMstRepository rfidVdrMstRepository, RfidAreaSettingRepository rfidAreaSettingRepository, SendMessage sendMessage) {
        this.rfidGiftTrnRepository = rfidGiftTrnRepository;
        this.rfidGiftMstRepository = rfidGiftMstRepository;
        this.rfidVdrMstRepository = rfidVdrMstRepository;
        this.rfidAreaSettingRepository = rfidAreaSettingRepository;
        this.sendMessage = sendMessage;
    }

    /**
     * 每次调用该接口，状态加1
     * @param rfidGiftMst
     * @return
     */
    @PostMapping("/updateOrderStatus")
    @Log("修改订单状态")
    @ApiOperation("修改订单状态")
    // @PreAuthorize("@el.check('ext:updOrderStatus')")
    public ResponseEntity<Object> updOrderStatus( @RequestBody RfidGiftMst rfidGiftMst) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("code","4000");
        if (StrUtil.isEmpty(rfidGiftMst.getRfidCod())) {
            resultMap.put("msg","请上传RFID编码");
            return new ResponseEntity<Object>(resultMap, HttpStatus.OK);
        }

        RfidGiftMst gift = rfidGiftMstRepository.findByRfidCod(rfidGiftMst.getRfidCod());
        RfidGiftTrn order = rfidGiftTrnRepository.findByGiftId(gift.getId());
        Integer status = Integer.parseInt(order.getStatus());
        if (status == 3) {
            if (StrUtil.isNotEmpty(rfidGiftMst.getRfidCodTwo())) {
                // 以生产
                order.setStatus("4");
            }
        } else {
            order.setStatus(String.valueOf(status+1));
        }

        rfidGiftTrnRepository.save(order);
        resultMap.put("code","2000");
        resultMap.put("msg","成功");
        return new ResponseEntity<Object>(resultMap, HttpStatus.OK);
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
