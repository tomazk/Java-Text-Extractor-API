package utils;

/** Response data container */
public class TTRResponseData implements ResponseData{

	/** result string */
	private String result;
	
	/** status (OK|ERROR) */
	private String status;
	
	/** empty or some sort of error feedback */
	private String errorMsg;
	
	public TTRResponseData(){
		result = "";
		status = "OK";
		errorMsg = "";
	}

	public void setStatusError(){
		status = "ERROR";
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
}
