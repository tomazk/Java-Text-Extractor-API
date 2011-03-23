package controllers;

import play.*;
import play.mvc.*;
import play.data.validation.*;
import play.data.validation.Error;

import de.l3s.boilerpipe.extractors.ExtractorBase;
import de.l3s.boilerpipe.extractors.DefaultExtractor;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import de.l3s.boilerpipe.BoilerpipeProcessingException;

import utils.BoilerpipeResponseData;

public class BoilerpipeController extends Controller {
	
	/** demo */
	public static void index(){
		render();
	}
	
	/** main logic */
	public static void extract(
			@Required(message = "rawHtml is required") 
			String rawHtml,
			
			@Required(message = "extractorType is required") 
			@Match(value = "default|article", message = "unknown extractor type")
			String extractorType
	){
		
		// initialize response 
		BoilerpipeResponseData responseData =  new BoilerpipeResponseData();
		
		// catch any validation errors
		if(validation.hasErrors()){
			
			String em = "Validation: ";
			for(Error e : validation.errors())
				em += e.message() + " ,";
			
			responseData.setStatusError();
			responseData.setErrorMsg(em);
			
			renderJSON(responseData);
		}
		
		ExtractorBase extractor = null;
		
		// get extractor instance based on the given type
		if(extractorType.equals("default"))
			extractor = DefaultExtractor.getInstance();
		if(extractorType.equals("article"))
			extractor = ArticleExtractor.getInstance();
		
		// extract
		try {
			responseData.setResult(extractor.getText(rawHtml));
		
		} catch (BoilerpipeProcessingException e) {
			/* 
			 * if there are any problems while extraction is taking place
			 * set status to ERROR and set the errorMsg to whatever 
			 * BoilerpipeProcessingException getMessage returns
			 */
		
			responseData.setStatusError();
			responseData.setErrorMsg( "BoilerpipeProcessingException: " + e.getMessage());
		}
		
		renderJSON(responseData);
	}

}
