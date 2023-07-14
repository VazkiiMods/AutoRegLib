/*
 *  This class is licensed under the WTFPL
 *  http://www.wtfpl.net/
 */
package vazkii.arl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AutoRegLib {
	public static final String MOD_ID = "autoreglib";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static void init() {
		LOGGER.info("AutoRegLib Initialized");
	}
}