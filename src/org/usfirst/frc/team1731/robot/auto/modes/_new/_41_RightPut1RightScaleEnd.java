package org.usfirst.frc.team1731.robot.auto.modes._new;

import java.util.Arrays;

import org.usfirst.frc.team1731.robot.auto.AutoModeBase;
import org.usfirst.frc.team1731.robot.auto.AutoModeEndedException;
import org.usfirst.frc.team1731.robot.auto.actions.Action;
import org.usfirst.frc.team1731.robot.auto.actions.DrivePathAction;
import org.usfirst.frc.team1731.robot.auto.actions.ElevatorUp;
import org.usfirst.frc.team1731.robot.auto.actions.ParallelAction;
import org.usfirst.frc.team1731.robot.auto.actions.PickUpAction;
import org.usfirst.frc.team1731.robot.auto.actions.ResetPoseFromPathAction;
import org.usfirst.frc.team1731.robot.auto.actions.RotateIntakeActionUp;
import org.usfirst.frc.team1731.robot.auto.actions.SpitAction;
import org.usfirst.frc.team1731.robot.auto.actions.WaitAction;
import org.usfirst.frc.team1731.robot.paths.PathContainer;
import org.usfirst.frc.team1731.robot.paths.Right3rdCubePickup;
import org.usfirst.frc.team1731.robot.paths.Right3rdCubeScoreScale3;
import org.usfirst.frc.team1731.robot.paths.RightScaleToRightSwitch;
import org.usfirst.frc.team1731.robot.paths.RightSwitchToRightScale;
import org.usfirst.frc.team1731.robot.paths.RightToRightScale;
import org.usfirst.frc.team1731.robot.paths.RightToRightScaleEnd;

public class _41_RightPut1RightScaleEnd extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
    	System.out.println("Executing _41_RightPut1RightScaleEnd");
    	
    	PathContainer Path = new RightToRightScaleEnd();
    	runAction(new ResetPoseFromPathAction(Path));
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorUp(), 
        		new RotateIntakeActionUp(),
        		new DrivePathAction(Path)
        })));

    	runAction(new SpitAction());

    	runAction(new WaitAction(10));
    	
	}

}
