package org.usfirst.frc.team1731.robot.auto.modes;

import org.usfirst.frc.team1731.robot.auto.AutoModeBase;
import org.usfirst.frc.team1731.robot.auto.AutoModeEndedException;
import org.usfirst.frc.team1731.robot.auto.actions.BeginShootingAction;
import org.usfirst.frc.team1731.robot.auto.actions.DeployIntakeAction;
import org.usfirst.frc.team1731.robot.auto.actions.DrivePathAction;
import org.usfirst.frc.team1731.robot.auto.actions.EndShootingAction;
import org.usfirst.frc.team1731.robot.auto.actions.ResetPoseFromPathAction;
import org.usfirst.frc.team1731.robot.auto.actions.WaitAction;
import org.usfirst.frc.team1731.robot.paths.BoilerGearToShootRed;
import org.usfirst.frc.team1731.robot.paths.DriveForward;
import org.usfirst.frc.team1731.robot.paths.PathContainer;
import org.usfirst.frc.team1731.robot.paths.StartToBoilerGearRed;

/**
 * Scores the preload gear onto the boiler-side peg then shoots the 10 preloaded fuel
 * 
 * @see AutoModeBase
 */
public class TestAuto extends AutoModeBase {

    @Override
    protected void routine() throws AutoModeEndedException {
        runAction(new WaitAction(2));
        PathContainer forwardPath = new DriveForward();
      //  PathContainer backPath = new DriveBack();
        runAction(new ResetPoseFromPathAction(forwardPath));
        runAction(new DrivePathAction(forwardPath));
        runAction(new WaitAction(15));
      //  runAction(new ResetPoseFromPathAction(backPath));
     //   runAction(new DrivePathAction(backPath));
        runAction(new WaitAction(15));
    }
}
