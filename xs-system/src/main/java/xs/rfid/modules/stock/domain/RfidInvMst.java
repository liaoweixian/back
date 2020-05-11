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
package xs.rfid.modules.stock.domain;

import lombok.Builder;
import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
* @website https://docs.auauz.net
* @description /
* @author lwx
* @date 2020-05-06
**/
@Entity
@Data
@Table(name="rfid_inv_mst")
public class RfidInvMst implements Serializable {

    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gift_cod")
    @ApiModelProperty(value = "礼品编号")
    private String giftCod;

    @Column(name = "location_cod")
    @ApiModelProperty(value = "库位编号")
    private String locationCod;

    @Column(name = "inventory_cnt")
    @ApiModelProperty(value = "库存数量")
    private String inventoryCnt;

    @Column(name = "last_change_dat")
    @ApiModelProperty(value = "最后变更时间")
    private String lastChangeDat;

    @Column(name = "last_change_user_name")
    @ApiModelProperty(value = "最后变更用户")
    private String lastChangeUserName;

    @Column(name = "last_change_trans_cod")
    @ApiModelProperty(value = "最后变更编号")
    private String lastChangeTransCod;

    @Column(name = "is_delete")
    @ApiModelProperty(value = "是否删除")
    private String isDelete;

    @Column(name = "gift_name")
    @ApiModelProperty(value = "礼品名")
    private String giftName;

    public void copy(RfidInvMst source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}