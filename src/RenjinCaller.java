import javax.script.*;
import org.apache.log4j.*;
import org.renjin.sexp.ListVector;
import org.renjin.sexp.Vector;
import java.util.*;

/**
 * This is part of the R integration for business intelligence tooling developed by
 * <a href="www.blacklightsolutions.com">Blacklight Solutions, LLC</a>, a data analytics 
 * that focuses on creating new opportunities by bringing advanced analytics into organizations.
 *      
 * This class handles calling directly on the Renjin API to invoke R once populated by the 
 * calling object.  
 * 
 * In order to compile and run these classes one will also need the renjin libraries 
 * referenced (available at http://www.renjin.org/ along with instructions for download and setup) 
 * as well as the Yellowfin i4-core.jar and i4-mi.jar 
 * (from the Yellowfin Install Directory/appserver/webapps/ROOT/WEB-INF/lib) 
 * and the Apache Log4j libraries.
 * 
 * @author Chance Coble
 */
public class RenjinCaller {

	Map<String,Object[]> params = new HashMap<String,Object[]>();
	String filePath = "";
	boolean hasError = false;
	String errorMessage = "";
	Logger log = Logger.getLogger(RenjinCaller.class);
	
	public RenjinCaller withError(String errorMsg) {
		hasError = true;
		errorMessage = errorMsg;
		return this;
	}
	
	public boolean hasError()  {
		return hasError;
	}
	
	public String errorMessage() {
		if(hasError) return errorMessage;
		else return "No errors detected";
	}
	
	public static RenjinCaller create(String filepath) {
		RenjinCaller rj = new RenjinCaller().setFilePath(filepath);
		return rj;
	}

	public RenjinCaller setFilePath(String path) {
		filePath = path;
		return this;
	}
	
	public RenjinCaller addColumn(String nm, Object[] values) {
		params.put(nm, values);
		return this;
	}
	
	public Object[] result() {
		Object[] yield = null;
		String df = dataFrame();
		ScriptEngineManager manager = new ScriptEngineManager();
	    // create a Renjin engine:
	    ScriptEngine engine = manager.getEngineByName("Renjin");
	    // check if the engine has loaded correctly:
	    if(engine == null) {
	        throw new RuntimeException("Renjin Script Engine not found on the classpath.");
	    }

	    try {
	    	log.info("REngine started successfully and preparing to execute script @ " + filePath);
	    	engine.eval(df);
	    	engine.eval(new java.io.FileReader(filePath));
	    	ListVector model = (ListVector) engine.eval("yf_dataset");
	    	Vector results = model.getElementAsVector("result");
	    	yield = new Object[results.length()];
	    	for(int i =0;i<results.length();i++) {
	    		yield[i] = results.getElementAsObject(i);
	    	}
	    	log.info("R script @ " + filePath + " executed and " + yield.length + " results returned");
	    	
	    } catch(Exception e) {
	    	withError("Failed to execute R-Script.\n" + e.getMessage());
	    	log.error("Failed to execute R-Script.",e);
	    }
	    return yield;
	}

	private String stringJoin(Object[] obs) {
		if(obs!=null && obs.length>0) {
			boolean quote = obs[0].getClass().getName().equalsIgnoreCase("java.lang.String");
			StringBuilder sb = new StringBuilder();
			if(quote) sb.append("\"");
			sb.append(obs[0]);
			if(quote) sb.append("\"");
			for(int i = 1;i<obs.length;i++) {
				sb.append(",");
				if(quote) sb.append("\"");
				sb.append(obs[i]);
				if(quote) sb.append("\"");
			}
			return sb.toString();
		}
		else return "";
	}
	
	private String dataFrame() {
		StringBuilder sb = new StringBuilder("yf_dataset <- data.frame(");
		boolean first = true;
		for(String p : params.keySet()) {
			if(!first) sb.append(", ");
		    first = false;
			sb.append(p + "=c(" + stringJoin(params.get(p)) + ")");
		}
		sb.append(")");
		return sb.toString();		
	}

}
