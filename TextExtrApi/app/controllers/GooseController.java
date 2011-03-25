package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;
import play.data.validation.Error;

import com.jimplush.goose.ContentExtractor;

import utils.GooseResponseData;

public class GooseController extends Controller {
	
	/** demo */
	public static void index(){
		render();
	}
	
	public static void extract(
			@Required(message = "rawHtml is required") 
			String rawHtml
	){
		
		// init response container
		GooseResponseData responseData = new GooseResponseData();
		
		// catch any validation errors
		if(validation.hasErrors()){
			
			String em = "Validation: ";
			for(Error e : validation.errors())
				em += e.message() + " ,";
			
			responseData.setStatusError();
			responseData.setErrorMsg(em);
			
			renderJSON(responseData);
		}
		
		try {
			ContentExtractor extractor = new ContentExtractor();
			String result = extractor.extractContentFromHtmlString(rawHtml);
			responseData.setResult(result);
		} catch (Exception e) {
			// any sort of exception that goose might throw at us
			
			responseData.setStatusError();
			responseData.setErrorMsg( "Goose Error: " + e.getMessage());
			renderJSON(responseData);
		}
	
		renderJSON(responseData);		
	}

}
