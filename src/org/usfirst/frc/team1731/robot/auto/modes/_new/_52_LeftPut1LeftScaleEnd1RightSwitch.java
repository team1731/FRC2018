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
import org.usfirst.frc.team1731.robot.auto.actions.SetElevatorPostition;
import org.usfirst.frc.team1731.robot.auto.actions.SpitAction;
import org.usfirst.frc.team1731.robot.auto.actions.WaitAction;
import org.usfirst.frc.team1731.robot.paths.DriveToScoreSwitchRight;
import org.usfirst.frc.team1731.robot.paths.LeftScaleEndToLeftSwitch;
import org.usfirst.frc.team1731.robot.paths.LeftScaleEndToRightSwitch;
import org.usfirst.frc.team1731.robot.paths.LeftSwitchToLeftScaleEnd;
import org.usfirst.frc.team1731.robot.paths.LeftToLeftScaleEnd;
import org.usfirst.frc.team1731.robot.paths.PathContainer;
import org.usfirst.frc.team1731.robot.paths.RightSwitchToRightSwitchScore;

public class _52_LeftPut1LeftScaleEnd1RightSwitch extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
    	System.out.println("Executing _48_LeftPut2LeftScaleEnd");
    	
    	PathContainer Path = new LeftToLeftScaleEnd();
    	runAction(new ResetPoseFromPathAction(Path));
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorUp(), 
        		new RotateIntakeActionUp(),
        		new DrivePathAction(Path)
        })));

    	runAction(new SpitAction());

    	Path = new LeftScaleEndToRightSwitch();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new PickUpAction(), 
        		new DrivePathAction(Path)
        })));
  
    	Path = new RightSwitchToRightSwitchScore();
    	runAction(new DrivePathAction(Path));
		runAction(new SetElevatorPostition());
        runAction(new SpitAction()); // SPIT #2
        
    	runAction(new WaitAction(1));
	
	}

}
