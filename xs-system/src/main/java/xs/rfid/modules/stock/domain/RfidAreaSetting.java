package xs.rfid.modules.stock.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
* @author liao
* @date 2020-05-10
*/
@Entity
@Data
@Table(name="rfid_area_setting")
public class RfidAreaSetting implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** 区域名 */
    @Column(name = "area_name",nullable = false)
    @NotBlank
    private String areaName;

    /** ip地址 */
    @Column(name = "ip",nullable = false)
    @NotBlank
    private String ip;

    /** 用户id */
    @Column(name = "user_id",nullable = false)
    @NotNull
    private Long userId;

    @Column(name = "created_time")
    private String createdTime;

    /** 用户名 */
    @Column(name = "user_name")
    private String userName;

    public void copy(RfidAreaSetting source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}