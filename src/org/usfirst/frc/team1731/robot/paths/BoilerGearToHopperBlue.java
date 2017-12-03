package org.usfirst.frc.team1731.robot.paths;

import org.usfirst.frc.team1731.lib.util.control.Path;
import org.usfirst.frc.team1731.lib.util.math.RigidTransform2d;
import org.usfirst.frc.team1731.lib.util.math.Rotation2d;
import org.usfirst.frc.team1731.lib.util.math.Translation2d;
import org.usfirst.frc.team1731.robot.auto.modes.GearThenHopperShootModeBlue;
import org.usfirst.frc.team1731.robot.paths.profiles.PathAdapter;

/**
 * Path from the blue boiler side peg to the blue hopper.
 * 
 * Used in GearThenHopperShootModeBlue
 * 
 * @see GearThenHopperShootModeBlue
 * @see PathContainer
 */
public class BoilerGearToHopperBlue implements PathContainer {

    @Override
    public Path buildPath() {
        return PathAdapter.getBlueHopperPath();
    }

    @Override
    public RigidTransform2d getStartPose() {
        return new RigidTransform2d(new Translation2d(116, 209), Rotation2d.fromDegrees(0.0));
    }

    @Override
    public boolean isReversed() {
        return false;
    }

}
