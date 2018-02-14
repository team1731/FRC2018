package org.usfirst.frc.team1731.robot.auto.modes;

import java.util.Arrays;

import org.usfirst.frc.team1731.lib.util.math.RigidTransform2d;
import org.usfirst.frc.team1731.robot.auto.AutoModeBase;
import org.usfirst.frc.team1731.robot.auto.AutoModeEndedException;
import org.usfirst.frc.team1731.robot.auto.actions.*;
import org.usfirst.frc.team1731.robot.paths.*;
import org.usfirst.frc.team1731.robot.paths.profiles.PathAdapter;

import edu.wpi.first.wpilibj.Timer;

/**
 * Scores the preload gear onto the boiler-side peg then deploys the hopper and shoots all 60 balls (10 preload + 50
 * hopper).
 * 
 * This was the primary autonomous mode used at SVR, St. Louis Champs, and FOC.
 * 
 * @see AutoModeBase
 */
public class LeftPutCubeOnLeftSwitch extends AutoModeBase {

    @Override
    protected void routine() throws AutoModeEndedException {
    	PathContainer straightPath = new LeftToLeftSwitch();
    	runAction(new ResetPoseFromPathAction(straightPath));
    	runAction(new DrivePathAction(straightPath));
    	runAction(new WaitAction(1));
    	//Run a parallel action to prepare the cube to drop while driving, then drop it
    }
}
