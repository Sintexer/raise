package com.ilyabuglakov.raise.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

@Test
public class LoggerTest {

    public static Logger logger = LogManager.getLogger(LoggerTest.class);

    @Test
    public void writeSomeLogs() {
        logger.debug("DebugMessage");
        logger.trace("TraceMessage");
        logger.info("InfoMessage");
        logger.warn("WarnMessage");
        logger.error("ErrorMessage");
        logger.fatal("FatalMessage");
    }

}