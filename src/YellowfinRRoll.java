

import org.apache.log4j.Logger;

import com.hof.mi.interfaces.AnalyticalFunction;
import com.hof.mi.interfaces.UserInputParameters;

public class YellowfinRRoll extends AnalyticalFunction {

	private Boolean booted = false;
	private Object[] result = null;
	private Logger log = Logger.getLogger(YellowfinRRoll.class);
	/**
	 * This function accepts numbers, text, dates, times, timestamp and boolean values
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
