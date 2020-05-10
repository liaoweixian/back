package xs.rfid.modules.stock.service;

import xs.rfid.modules.stock.domain.RfidAreaSetting;
import xs.rfid.modules.stock.service.dto.RfidAreaSettingDto;
import xs.rfid.modules.stock.service.dto.RfidAreaSettingQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author liao
* @date 2020-05-10
*/
public interface RfidAreaSettingService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(RfidAreaSettingQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<RfidAreaSettingDto>
    */
    List<RfidAreaSettingDto> queryAll(RfidAreaSettingQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return RfidAreaSettingDto
     */
    RfidAreaSettingDto findById(Long id);

    /**
    * 创建
    * @param resources /
    * @return RfidAreaSettingDto
    */
    RfidAreaSettingDto create(RfidAreaSetting resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(RfidAreaSetting resources);

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
    void download(List<RfidAreaSettingDto> all, HttpServletResponse response) throws IOException;
}