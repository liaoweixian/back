package xs.rfid.modules.stock.rest;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xs.rfid.modules.mqtt.SendMessage;
import xs.rfid.modules.stock.domain.RfidVdrMst;
import xs.rfid.modules.stock.repository.RfidVdrMstRepository;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "demo")
@RestController
@RequestMapping("/api/demo")
public class DemoController {

    private final SendMessage sendMessage;

    private final RfidVdrMstRepository rfidVdrMstRepository;

    public DemoController(SendMessage sendMessage, RfidVdrMstRepository rfidVdrMstRepository) {
        this.sendMessage = sendMessage;
        this.rfidVdrMstRepository = rfidVdrMstRepository;
    }

    @GetMapping("/send")
    public void send() {
        RfidVdrMst member = rfidVdrMstRepository.findById(4l).get();
        Map<String, Object> map = new HashMap<>();
        map.put("cmd", "member");
        map.put("data", member);
        sendMessage.send(JSON.toJSONString(map),"/member/info/1");
        System.out.println("--------------------------------------");
    }


}
