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
* @author stock
* @date 2020-05-06
**/
@Entity
@Data
@Table(name="rfid_inv_trn")
public class RfidInvTrn implements Serializable {

    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gift_cod")
    @ApiModelProperty(value = "礼品编号")
    private String giftCod;

    @Column(name = "location_cod")
    @ApiModelProperty(value = "库位编码")
    private String locationCod;

    @Column(name = "trans_cnt")
    @ApiModelProperty(value = "事务数量")
    private String transCnt;

    @Column(name = "trans_type")
    @ApiModelProperty(value = "事务类型（入库/出库）")
    private String transType;

    @Column(name = "trans_dat")
    @ApiModelProperty(value = "事务时间")
    private String transDat;

    @Column(name = "status")
    @ApiModelProperty(value = "状态（未上架/已上架/已下架/已出库/已返库）")
    private String status;

    @Column(name = "is_delete")
    @ApiModelProperty(value = "是否删除")
    private String isDelete;

    public void copy(RfidInvTrn source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}