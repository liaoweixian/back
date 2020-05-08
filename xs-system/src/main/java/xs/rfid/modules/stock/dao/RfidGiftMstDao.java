package xs.rfid.modules.stock.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import xs.rfid.modules.stock.domain.RfidGiftMst;
import xs.rfid.modules.stock.service.dto.RfidGiftMstQueryCriteria;


import java.util.List;

public interface RfidGiftMstDao extends BaseMapper<RfidGiftMst> {

    IPage<RfidGiftMst> findAll(Page<RfidGiftMst> page, @Param("criteria") RfidGiftMstQueryCriteria criteria);
}
