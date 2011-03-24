package utils;

/** Common representation for response data */
public interface ResponseData {
	
	public abstract void setStatusError();

	public abstract void setErrorMsg(String errorMsg);
	
	public abstract void setResult(String result);

}
