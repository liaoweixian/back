package xs.sense.api.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xs.sense.api.mqtt.SendMessage;
import xs.sense.api.serial.listener.PortListener;
import xs.sense.api.utils.ConstantSense;

/**
 * @author mikehhuang
 * @date 2020/3/10 20:52
 */
@Slf4j
@Service
@AllArgsConstructor
public class SerialListener extends PortListener {
    private  final SendMessage sendMessage;
    @Override
    public void onReadException(Exception e) {
        log.info("发生异常"+e.getMessage());
    }

    @Override
    public void onReceive(byte[] data)  {
        if(ConstantSense.receiverStatus==3){
            //盘存状态
            //1.发送到消息队列
//            sendMessage.send(ConvertUtils.bytes2HexString(data),"/sense/serail/rs01/1");
        }else{
            //空闲状态
            String dataStr = new String(data).trim();
            sendMessage.send(dataStr,"/sense/serail/rs01/1");
        }

//        String dataStr = new String(data).trim();
//        String dataStr =  ConvertUtils.bytes2HexString(data);
//        log.info("串口接收到数据: " + dataStr);
//        String reply = "我收到你发送的数据啦";
//        serialPortVo.sendData(reply.getBytes());

    }
}
