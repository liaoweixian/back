package xs.sense.api.entity;

import lombok.Data;

import java.util.List;

/**
 * @author mikehhuang
 * @date 2020/3/23 18:05
 */
@Data
public class DeviceControl {
    private Long currentTime ;//当前时间
    private  int holdTime ;// 持续时间 单位秒
    private  int alarmHoldTime ;// 警告声持续时间 单位秒
    private  int pluseNo ;// 脉冲次数，闪烁次数
    private  int alarm ;// 脉冲次数，闪烁次数
    private String status ;// //  1：红灯亮 2：黄灯亮 3：绿灯亮 4. 红灯闪烁 8 黄灯闪烁 9 绿灯闪烁
}
