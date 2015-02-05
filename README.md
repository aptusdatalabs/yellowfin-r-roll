# yellowfin-r-roll
An advanced analytics function for Yellowfin to incorporate R scripts into report generation

This project is an advanced function integration into Yellowfin's (www.yellowfinbi.com) report development interface so that R scripts can be called on.  Prior to applying this function to a dataset in Yellowfin you will need the R-script in a location on your Yellowfin application server.  The script may assume that a dataframe (which will be populated by the integrated function) contains a dataframe called <code>yf_dataset</code>.  That dataframe can have up to 5 fields (<code>parameter0, parameter1, ...,parameter4</code>) which represent the 5 features for each record that the model can be applied to.  The assumption is that the model is applied to each of the feature sets and yields a value.  That value can be written then as a list to <code>yf_dataset$result</code>.  <code>yf_dataset$result</code> is the value that will appear in the resulting column in the Yellowfin report.   <u><i>If your R script uses CRAN packages, you will need to follow the instructions on www.renjin.org for integrating those CRAN packages into your local environment.</i></u>

<b>Not that the slower the R-script is, the slower the report will be to generate. </b>

To simply download and run this project, download the jar files from the latest release.   Copy those files into your Yellowfin web application library ([Yellowfin Install Directory ]/appserver/webapps/ROOT/WEB-INF/lib) and restart Yellowfin.  You should see the additional functions available once you build a new report.

To build this project you will need to include the libraries provided in the /deps folder in your build path.  You can also get these online through the www.renjin.org project, as well as the log4j repository. 
You will also need to reference the two jar files below in your Yellowfin web application library : 
[Yellowfin Install Directory ]/appserver/webapps/ROOT/WEB-INF/lib
   - i4-core.jar
   - i4-mi.jar

Once you have built the jar from the source code, copy the resulting jar as well as the contents of the deps folder into your Yellowfin web application library (see path above).   Restart Yellowfin, and when you build a report and add an advanced function (more here : http://wiki.yellowfin.com.au/display/USER71/Advanced+Functions).

