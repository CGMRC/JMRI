// just print a confirmation
var Turnout = Java.type("jmri.Turnout");
var logger =  Java.type("org.slf4j.LoggerFactory").getLogger("JavaScriptTest.js");

logger.warn("JavaScriptTest: Turnout.THROWN is "+Turnout.THROWN+" (WARN OK here)");
