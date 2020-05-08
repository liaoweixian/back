package xs.sense.api.serial.exception;

public class SerialPortParameterFailure extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SerialPortParameterFailure() {
	}

	@Override
	public String toString() {
		return "从串口读取数据时出错！";
	}

}
