package jmri.jmrix.bachrus.speedmatcher.basic;

import jmri.jmrix.bachrus.speedmatcher.SpeedMatcher;

/**
 * Factory for creating the correct type of Basic speed matcher for the given
 * SpeedMatcherConfig
 *
 * @author Todd Wegter Copyright (C) 2024
 */
public class BasicSpeedMatcherFactory {

    /**
     * Gets the correct Basic Speed Matcher for the given speedTable
     *
     * @param speedTable
     * @param config BasicSpeedMatcherConfig
     * @return SpeedMatcher
     */
    public static SpeedMatcher getSpeedMatcher(BasicSpeedMatcherConfig.SpeedTable speedTable, BasicSpeedMatcherConfig config) {

        switch (speedTable) {
            case ESU:
                return new BasicESUTableSpeedMatcher(config);
            case ADVANCED:
                return new BasicSpeedTableSpeedMatcher(config);
            default:
                return new BasicSimpleCVSpeedMatcher(config);
        }
    }
}
