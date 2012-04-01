package controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import play.*;
import play.mvc.*;
import play.data.validation.*;
import play.data.validation.Error;

import edu.illinois.dais.trr.TTRExtractor;

import utils.TTRResponseData;;

public class TTRController extends Controller {
	
    /** 
     * author recommends using 3 clusters
     * 
     * http://www.cs.illinois.edu/homes/weninge1/cetr/ 
     * */
    private static final int K = 3;
    
    
	/** demo */
	public static void index(){
		render();
	}
	
	/** main logic */
	public static void extract(
			@Required(message = "rawHtml is required") 
			String rawHtml,
			
			@Required(message = "extractorType is required") 
			@Match(value = "default", message = "unknown extractor type")
			String extractorType
	){
		
		// initialize response 
		TTRResponseData responseData =  new TTRResponseData();
		
		// catch any validation errors
		if(validation.hasErrors()){
			
			String em = "Validation: ";
			for(Error e : validation.errors())
				em += e.message() + " ,";
			
			responseData.setStatusError();
			responseData.setErrorMsg(em);
			
			renderJSON(responseData);
		}
		
		// for now we only support the default TTRExtractor
		TTRExtractor extractor = new TTRExtractor();
		String[] resultLines = extractor.extractText(rawHtml, K);
		
        StringBuilder result = new StringBuilder();
        for (String line : resultLines){
            result.append(line).append("\n");
        }
        
        responseData.setResult(result.toString());
		
		renderJSON(responseData);
	}

}
