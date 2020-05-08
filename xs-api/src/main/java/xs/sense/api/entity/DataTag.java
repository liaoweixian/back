package xs.sense.api.entity;

import lombok.Data;

/**
 * @author mikehhuang
 * @date 2020/3/23 18:35
 */
@Data
public class DataTag {
//    private String doorStatus;  //出入状态 ，如：1:入库 2：出库
//    private String loadStatus;  //1：装载 2： 卸载
    private String rfid;  //rfid 标签
    private String tagType;  //1.货架号标签2.地面货位号标签3.栈板标签4.箱标签5.叉车号标签,6.人员标签 7.容器标签 8.月台标签 9 备货区

}
