package org.usfirst.frc.team1731.robot.auto.modes;

import org.usfirst.frc.team1731.robot.auto.AutoModeBase;
import org.usfirst.frc.team1731.robot.auto.AutoModeEndedException;
import org.usfirst.frc.team1731.robot.auto.actions.BeginShootingAction;
import org.usfirst.frc.team1731.robot.auto.actions.DeployIntakeAction;
import org.usfirst.frc.team1731.robot.auto.actions.DrivePathAction;
import org.usfirst.frc.team1731.robot.auto.actions.EndShootingAction;
import org.usfirst.frc.team1731.robot.auto.actions.ResetPoseFromPathAction;
import org.usfirst.frc.team1731.robot.auto.actions.SpitAction;
import org.usfirst.frc.team1731.robot.auto.actions.WaitAction;
import org.usfirst.frc.team1731.robot.paths.*;

/**
 * Scores the preload gear onto the boiler-side peg then shoots the 10 preloaded fuel
 * 
 * @see AutoModeBase
 */
public class TestAuto extends AutoModeBase {

    @Override
    protected void routine() throws AutoModeEndedException {
        runAction(new WaitAction(2));
        PathContainer autoPath = new _4_FarSwitch_Back();
      //  PathContainer backPath = new DriveBack();
        runAction(new ResetPoseFromPathAction(autoPath));
        runAction(new DrivePathAction(autoPath));
        runAction(new SpitAction());
        runAction(new WaitAction(15));
      //  runAction(new ResetPoseFromPathAction(backPath));
     //   runAction(new DrivePathAction(backPath));
        runAction(new WaitAction(15));
    }
}
