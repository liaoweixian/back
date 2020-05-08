package xs.sense.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mikehhuang
 * @date 2020-03-21
 */
@RestController
@RequestMapping("/api/localhost")
public class ApiController {

	private static final double GB = 1024*1024*1024.00;

	@GetMapping
    public ResponseEntity<Object> getServerInfo(){
    	Map<String,Object> resultMap = new HashMap<>(8);

			resultMap.put("diskTotal","aa");
			resultMap.put("diskUsed","aa");


		return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

}
