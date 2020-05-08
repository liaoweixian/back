package xs.rfid.modules.stock.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import xs.rfid.modules.stock.domain.RfidGiftMst;
import xs.rfid.modules.stock.domain.RfidGiftTrn;
import xs.rfid.modules.stock.service.dto.RfidGiftTrnQueryCriteria;

public interface RfidGiftTrnDao extends BaseMapper<RfidGiftTrn> {

    IPage<RfidGiftMst> findPage(Page<RfidGiftTrn> page, @Param("criteria") RfidGiftTrnQueryCriteria criteria);
}
