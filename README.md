# yellowfin-r-roll
An advanced analytics function for Yellowfin to incorporate R scripts into report generation

This project is an advanced function integration into Yellowfin's (www.yellowfinbi.com) report development interface.   To build this project you will need to include the libraries provided in the /deps folder in your build path.   
You will also need to reference the two jar files below in your Yellowfin web application library : 
[Yellowfin Install Directory ]/appserver/webapps/ROOT/WEB-INF/lib
   - i4-core.jar
   - i4-mi.jar

Once you have built the jar from the source code, copy the resulting jar as well as the contents of the deps folder into your Yellowfin web application library (see path above).   Restart Yellowfin, and when you build a report and add an advanced function (more here : http://wiki.yellowfin.com.au/display/USER71/Advanced+Functions).

