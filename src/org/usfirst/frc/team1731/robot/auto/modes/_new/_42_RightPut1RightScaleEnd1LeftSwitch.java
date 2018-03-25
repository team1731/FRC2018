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
import org.usfirst.frc.team1731.robot.auto.modes.RightPutCubeOnRightScaleAndLeftSwitch;
import org.usfirst.frc.team1731.robot.paths.DriveToScoreSwitchLeft2;
import org.usfirst.frc.team1731.robot.paths.LeftSwitchBackup;
import org.usfirst.frc.team1731.robot.paths.LeftSwitchToLeftSwitchEnd;
import org.usfirst.frc.team1731.robot.paths.PathContainer;
import org.usfirst.frc.team1731.robot.paths.Right3rdCubePickup;
import org.usfirst.frc.team1731.robot.paths.Right3rdCubeScoreScale3;
import org.usfirst.frc.team1731.robot.paths.RightScaleToLeftSwitch;
import org.usfirst.frc.team1731.robot.paths.RightScaleToLeftSwitch2;
import org.usfirst.frc.team1731.robot.paths.RightScaleToRightSwitch;
import org.usfirst.frc.team1731.robot.paths.RightSwitchToLeftSwitch;
import org.usfirst.frc.team1731.robot.paths.RightSwitchToRightScale;
import org.usfirst.frc.team1731.robot.paths.RightToRightScale;

public class _42_RightPut1RightScaleEnd1LeftSwitch extends AutoModeBase {
	//
	// not the same as the diagram -- why bother putting it on the end of
	// the scale if you are driving down to the other end anyway. it would
	// be faster to do the normal scale thing, then go down to the other
	// switch.
	//
	@Override
	protected void routine() throws AutoModeEndedException {
    	System.out.println("Executing _42_RightPut1RightScaleEnd1LeftSwitch");
    	
    	PathContainer Path = new RightToRightScale(); // PATH #1
    	runAction(new ResetPoseFromPathAction(Path));
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorUp(), 
        		new RotateIntakeActionUp(),
        		new DrivePathAction(Path),
        })));


    	runAction(new SpitAction());
    	
    	Path = new RightScaleToLeftSwitch2();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new PickUpAction(), 
        		new DrivePathAction(Path)
        })));
        
    	Path = new LeftSwitchBackup();
		runAction(new DrivePathAction(Path));

    	Path = new LeftSwitchToLeftSwitchEnd();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorUp(), 
        		new RotateIntakeActionUp(),
        		new DrivePathAction(Path)
        })));
        runAction(new SpitAction()); 

    	runAction(new WaitAction(1));
    	
	}

}
