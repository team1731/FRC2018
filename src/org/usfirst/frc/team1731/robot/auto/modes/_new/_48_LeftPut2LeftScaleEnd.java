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
import org.usfirst.frc.team1731.robot.paths.LeftScaleEndToLeftSwitch;
import org.usfirst.frc.team1731.robot.paths.LeftScaleToLeftSwitch;
import org.usfirst.frc.team1731.robot.paths.LeftSwitchToLeftScaleEnd;
import org.usfirst.frc.team1731.robot.paths.LeftToLeftScale;
import org.usfirst.frc.team1731.robot.paths.LeftToLeftScaleEnd;
import org.usfirst.frc.team1731.robot.paths.PathContainer;
import org.usfirst.frc.team1731.robot.paths.RightScaleEndToRightSwitch;
import org.usfirst.frc.team1731.robot.paths.RightSwitchToRightScaleEnd;
import org.usfirst.frc.team1731.robot.paths.RightToRightScaleEnd;

public class _48_LeftPut2LeftScaleEnd extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
    	System.out.println("Executing _48_LeftPut2LeftScaleEnd");
    	
    	PathContainer Path = new LeftToLeftScale();
    	runAction(new ResetPoseFromPathAction(Path));
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorUp(), 
        		new RotateIntakeActionUp(),
        		new DrivePathAction(Path)
        })));

    	runAction(new SpitAction());

    	Path = new LeftScaleToLeftSwitch();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new PickUpAction(), 
        		new DrivePathAction(Path)
        })));
  
    	Path = new LeftSwitchToLeftScaleEnd();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorUp(), 
        		new RotateIntakeActionUp(),
        		new DrivePathAction(Path)
        })));
        runAction(new SpitAction());         
        
    	runAction(new WaitAction(1));
	
	}

}
