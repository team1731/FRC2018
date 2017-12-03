package org.usfirst.frc.team1731.robot.paths;

import org.usfirst.frc.team1731.lib.util.control.Path;
import org.usfirst.frc.team1731.lib.util.math.RigidTransform2d;
import org.usfirst.frc.team1731.robot.auto.modes.GearThenHopperShootModeBlue;
import org.usfirst.frc.team1731.robot.paths.profiles.PathAdapter;

/**
 * Path from the blue alliance wall to the blue boiler peg.
 * 
 * Used in GearThenHopperShootModeBlue
 * 
 * @see GearThenHopperShootModeBlue
 * @see PathContainer
 */
public class StartToBoilerGearBlue implements PathContainer {

    @Override
    public Path buildPath() {
        return PathAdapter.getBlueGearPath();
    }

    @Override
    public RigidTransform2d getStartPose() {
        return PathAdapter.getBlueStartPose();
    }

    @Override
    public boolean isReversed() {
        return true;
    }
}