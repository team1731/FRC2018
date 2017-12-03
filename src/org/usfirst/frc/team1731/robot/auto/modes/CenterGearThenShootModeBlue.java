package org.usfirst.frc.team1731.robot.auto.modes;

import org.usfirst.frc.team1731.robot.auto.AutoModeBase;
import org.usfirst.frc.team1731.robot.auto.AutoModeEndedException;
import org.usfirst.frc.team1731.robot.auto.actions.BeginShootingAction;
import org.usfirst.frc.team1731.robot.auto.actions.DeployIntakeAction;
import org.usfirst.frc.team1731.robot.auto.actions.DrivePathAction;
import org.usfirst.frc.team1731.robot.auto.actions.ResetPoseFromPathAction;
import org.usfirst.frc.team1731.robot.auto.actions.ScoreGearAction;
import org.usfirst.frc.team1731.robot.auto.actions.WaitAction;
import org.usfirst.frc.team1731.robot.paths.CenterGearToShootBlue;
import org.usfirst.frc.team1731.robot.paths.PathContainer;
import org.usfirst.frc.team1731.robot.paths.StartToCenterGearBlue;

/**
 * Scores the preload gear onto the center peg then shoots the 10 preloaded fuel
 * 
 * @see AutoModeBase
 */
public class CenterGearThenShootModeBlue extends AutoModeBase {

    @Override
    protected void routine() throws AutoModeEndedException {
        PathContainer gearPath = new StartToCenterGearBlue();
        runAction(new ResetPoseFromPathAction(gearPath));
        runAction(new DrivePathAction(gearPath));
        runAction(new DeployIntakeAction());
        runAction(new ScoreGearAction());
        runAction(new DrivePathAction(new CenterGearToShootBlue()));
        runAction(new BeginShootingAction());
        runAction(new WaitAction(15));
    }
}
