package xs.rfid.modules.stock.repository;

import xs.rfid.modules.stock.domain.RfidAreaSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author liao
* @date 2020-05-10
*/
public interface RfidAreaSettingRepository extends JpaRepository<RfidAreaSetting, Long>, JpaSpecificationExecutor<RfidAreaSetting> {

    public RfidAreaSetting findByIp(String id);
}