package org.usfirst.frc.team1731.robot.auto.modes;

import java.util.Arrays;

import org.usfirst.frc.team1731.robot.auto.AutoModeBase;
import org.usfirst.frc.team1731.robot.auto.AutoModeEndedException;
import org.usfirst.frc.team1731.robot.auto.actions.Action;
import org.usfirst.frc.team1731.robot.auto.actions.BeginShootingAction;
import org.usfirst.frc.team1731.robot.auto.actions.DeployIntakeAction;
import org.usfirst.frc.team1731.robot.auto.actions.DrivePathAction;
import org.usfirst.frc.team1731.robot.auto.actions.ElevatorHome;
import org.usfirst.frc.team1731.robot.auto.actions.ElevatorUp;
import org.usfirst.frc.team1731.robot.auto.actions.EndShootingAction;
import org.usfirst.frc.team1731.robot.auto.actions.ParallelAction;
import org.usfirst.frc.team1731.robot.auto.actions.PickUpAction;
import org.usfirst.frc.team1731.robot.auto.actions.ResetPoseFromPathAction;
import org.usfirst.frc.team1731.robot.auto.actions.RotateIntakeActionUp;
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
        System.out.println("Executing TestAuto");
        runAction(new WaitAction(2));
        PathContainer autoPath = new PatrickPath();
        runAction(new ResetPoseFromPathAction(autoPath));
        runAction(new DrivePathAction(autoPath));
        runAction(new SpitAction());
        runAction(new PickUpAction());
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorUp(), 
        		new RotateIntakeActionUp()
        })));
        runAction(new SpitAction());
        runAction(new ElevatorHome());
        runAction(new WaitAction(15));

    }
}
