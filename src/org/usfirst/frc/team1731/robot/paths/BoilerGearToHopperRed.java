package org.usfirst.frc.team1731.robot.paths;

import org.usfirst.frc.team1731.lib.util.control.Path;
import org.usfirst.frc.team1731.lib.util.math.RigidTransform2d;
import org.usfirst.frc.team1731.lib.util.math.Rotation2d;
import org.usfirst.frc.team1731.lib.util.math.Translation2d;
import org.usfirst.frc.team1731.robot.auto.modes.GearThenHopperShootModeRed;
import org.usfirst.frc.team1731.robot.paths.profiles.PathAdapter;

/**
 * Path from the red boiler side peg to the red hopper.
 * 
 * Used in GearThenHopperShootModeRed
 * 
 * @see GearThenHopperShootModeRed
 * @see PathContainer
 */
public class BoilerGearToHopperRed implements PathContainer {

    @Override
    public Path buildPath() {
        return PathAdapter.getRedHopperPath();
    }

    @Override
    public RigidTransform2d getStartPose() {
        return new RigidTransform2d(new Translation2d(112, 115), Rotation2d.fromDegrees(0.0));
    }

    @Override
    public boolean isReversed() {
        return false;
    }

}
