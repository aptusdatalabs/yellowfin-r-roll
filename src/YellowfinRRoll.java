

import org.apache.log4j.Logger;
import com.hof.mi.interfaces.AnalyticalFunction;
import com.hof.mi.interfaces.UserInputParameters;

/**
 * This is part of the R integration for business intelligence tooling developed by
 * <a href="www.blacklightsolutions.com">Blacklight Solutions, LLC</a>, a data analytics 
 * that focuses on creating new opportunities by bringing advanced analytics into organizations.
 * 
 * This is the primary class for the Yellowfin interface of the R integration.  
 * <a href="www.yellowfinbi.com">Yellowfin</a> is a web-based business intelligence tool 
 * focusing on ad-hoc report and dashboard development, as well as collaboration and mobile access.
 * 
 *      
 * This class handles implementing the Yellowfin Advanced Function interface as specified by the 
 * Yellowfin API, as well as setting up the parameters and invoking the R engine class.  
 * 
 * In order to compile and run these classes one will also need the renjin libraries 
 * referenced (available at http://www.renjin.org/ along with instructions for download and setup) 
 * as well as the Yellowfin i4-core.jar and i4-mi.jar 
 * (from the Yellowfin Install Directory/appserver/webapps/ROOT/WEB-INF/lib) 
 * and the Apache Log4j libraries.
 * 
 * @author Chance Coble
 */
public class YellowfinRRoll extends AnalyticalFunction {

	/** Indicator for whether or not this instance has been run - depends on 'instantiate once 
	 * per report' lifecycle.
	 */
	private Boolean booted = false;
	
	/** 
	 * The results of the R script will be stored here.
	 */
	private Object[] result = null;
	
	/** The logger object for Yellowfin log files.
	 * 
	 */
	private Logger log = Logger.getLogger(YellowfinRRoll.class);
	
	/**
	 * This function accepts numbers, text, dates, times, timestamp and boolean values
	 * 
	 * @param i	The type in question, as encoded through an enumeration in Yellowfin's library.
	 * @return Whether or not it accepts the given type identifier.
	 */
	@Override
	public boolean acceptsNativeType(int i) {
		return (i==TYPE_NUMERIC
				|| i==TYPE_TEXT
				|| i==TYPE_DATE
				|| i==TYPE_TIME
				|| i==TYPE_TIMESTAMP
				|| i==TYPE_BOOLEAN);	
	}

	/**
	 * This function is part of the analytical function interface from the Yellowfin API.
	 * This function works in an unusual way, but going through the entire dataset first, 
	 * iterating over each field to build a set of ordered arrays of those values.  Then 
	 * the R engine is invoked with the given script on them.  The result is stored in this 
	 * class as the result field (which is an Object array).
	 * 
	 * @param j 	The location (row) in the recordset as a zero-offset integer index
	 * @param val 	The value from the field on which this function is being applied.
	 */
	@Override
	public Object applyAnalyticFunction(int j, Object val) throws Exception {
		
		// 1. Go through the parameters and set them up in a data structure
		// 2. Invoke
		String filepath = (String) getParameterValue("filename");
		synchronized(booted) {
			if(!booted) {
				Object[] parameter0 = (Object[]) getParameterValue("parameter0");
				Object[] parameter1 = (Object[]) getParameterValue("parameter1");
				Object[] parameter2 = (Object[]) getParameterValue("parameter2");
				Object[] parameter3 = (Object[]) getParameterValue("parameter3");
				Object[] parameter4 = (Object[]) getParameterValue("parameter4");
				

				RenjinCaller rj = RenjinCaller.create(filepath);
				result = rj
						.addColumn("parameter0",parameter0)
						.addColumn("parameter1",parameter1)
						.addColumn("parameter2", parameter2)
						.addColumn("parameter3", parameter3)
						.addColumn("parameter4", parameter4)
						.result();

				if(rj.hasError() || result==null) {
					log.error("Failure in R Script Invocation 'YellowfinRRoll:applyAnalyticsFunction'");
					throw new Exception("Failure in R Script Invocation: " 
				                                     + rj.errorMessage());
				}
				booted = true;
			}
			return result[j];
		}

	}

	@Override
	public String getCategory() {
		return "Blacklight";
	}

	@Override
	public String getColumnHeading(String arg0) {
		return "R-script Result";
	}

	@Override
	public String getDescription() {
		return "This function will invoke an R-script (in a separate file)" +
				" which is pointed to by a parameter.  The R-script will " +
				" return a 'result' value in a dataframe which will be included in the report";
	}

	@Override
	public String getName() {
		return "R-Script";
	}

	@Override
	public int getReturnType() {
		return AnalyticalFunction.TYPE_UNKNOWN; // unknown, base it on the same type that the selected column is.
	}
	
	/**
	 * This method sets up the parameters for the Yellowfin interface to use to collect
	 * labels, information and references to other fields.  For this approach there is one 
	 * field that collects the path of the R script (filename) and 5 others that can be used
	 * to refer to other fields in the Yellowfin table so they can be passed into the R script 
	 * as parameters.
	 * 
	 */
	protected void setupParameters() {
	
		
		com.hof.mi.interfaces.UserInputParameters.Parameter parameter = 
				new UserInputParameters.Parameter();
		parameter.setUniqueKey("filename");
		parameter.setDisplayName("R Script Path");
		parameter.setDescription("Point to the path for the R Script to be invoked.");
		parameter.setDataType(UserInputParameters.TYPE_TEXT);
		parameter.setDisplayType(UserInputParameters.DISPLAY_TEXT_LONG);
		parameter.setDefaultValue(new String(""));
		addParameter(parameter);
		
		parameter = new UserInputParameters.Parameter();
		parameter.setUniqueKey("parameter0");
		parameter
				.setDisplayName("Parameter 1");
		parameter
				.setDescription("First Parameter");
		parameter.setDataType(100);
		parameter.setDisplayType(6);
		parameter.setAcceptsFieldType(6, true);
		parameter.setAcceptsFieldType(3, true);
		parameter.setAcceptsFieldType(1, true);
		parameter.setAcceptsFieldType(2, true);
		parameter.setAcceptsFieldType(4, true);
		parameter.setAcceptsFieldType(5, true);
		addParameter(parameter);
		
		parameter = new UserInputParameters.Parameter();
		parameter.setUniqueKey("parameter1");
		parameter
				.setDisplayName("Parameter 2");
		parameter
				.setDescription("Second Parameter");
		parameter.setDataType(100);
		parameter.setDisplayType(6);
		parameter.setAcceptsFieldType(6, true);
		parameter.setAcceptsFieldType(3, true);
		parameter.setAcceptsFieldType(1, true);
		parameter.setAcceptsFieldType(2, true);
		parameter.setAcceptsFieldType(4, true);
		parameter.setAcceptsFieldType(5, true);
		addParameter(parameter);

		parameter = new UserInputParameters.Parameter();
		parameter.setUniqueKey("parameter2");
		parameter
				.setDisplayName("Parameter 3");
		parameter
				.setDescription("Third Parameter");
		parameter.setDataType(100);
		parameter.setDisplayType(6);
		parameter.setAcceptsFieldType(6, true);
		parameter.setAcceptsFieldType(3, true);
		parameter.setAcceptsFieldType(1, true);
		parameter.setAcceptsFieldType(2, true);
		parameter.setAcceptsFieldType(4, true);
		parameter.setAcceptsFieldType(5, true);
		addParameter(parameter);
		
		parameter = new UserInputParameters.Parameter();
		parameter.setUniqueKey("parameter3");
		parameter
				.setDisplayName("Parameter 4");
		parameter
				.setDescription("Fourth Parameter");
		parameter.setDataType(100);
		parameter.setDisplayType(6);
		parameter.setAcceptsFieldType(6, true);
		parameter.setAcceptsFieldType(3, true);
		parameter.setAcceptsFieldType(1, true);
		parameter.setAcceptsFieldType(2, true);
		parameter.setAcceptsFieldType(4, true);
		parameter.setAcceptsFieldType(5, true);
		addParameter(parameter);
		
		parameter = new UserInputParameters.Parameter();
		parameter.setUniqueKey("parameter4");
		parameter
				.setDisplayName("Parameter 5");
		parameter
				.setDescription("Fifth Parameter");
		parameter.setDataType(100);
		parameter.setDisplayType(6);
		parameter.setAcceptsFieldType(6, true);
		parameter.setAcceptsFieldType(3, true);
		parameter.setAcceptsFieldType(1, true);
		parameter.setAcceptsFieldType(2, true);
		parameter.setAcceptsFieldType(4, true);
		parameter.setAcceptsFieldType(5, true);
		addParameter(parameter);
	}
}
