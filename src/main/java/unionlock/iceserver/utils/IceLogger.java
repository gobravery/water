package unionlock.iceserver.utils;


public class IceLogger {
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("ICESERVER");
	public static void log(String msg) {
		logger.info(msg);
	}
}
