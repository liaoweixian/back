package xs.rfid.modules.stock.service;

import xs.rfid.modules.stock.domain.RfidGiftTrn;
import xs.rfid.modules.stock.service.dto.RfidGiftTrnDto;
import xs.rfid.modules.stock.service.dto.RfidGiftTrnQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author liao
* @date 2020-05-08
*/
public interface RfidGiftTrnService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(RfidGiftTrnQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<RfidGiftTrnDto>
    */
    List<RfidGiftTrnDto> queryAll(RfidGiftTrnQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return RfidGiftTrnDto
     */
    RfidGiftTrnDto findById(Long id);

    /**
    * 创建
    * @param resources /
    * @return RfidGiftTrnDto
    */
    RfidGiftTrnDto create(RfidGiftTrn resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(RfidGiftTrn resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Long[] ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<RfidGiftTrnDto> all, HttpServletResponse response) throws IOException;
}