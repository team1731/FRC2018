package org.usfirst.frc.team1731.robot.auto.modes._new;

import java.util.Arrays;

import org.usfirst.frc.team1731.robot.auto.AutoModeBase;
import org.usfirst.frc.team1731.robot.auto.AutoModeEndedException;
import org.usfirst.frc.team1731.robot.auto.actions.Action;
import org.usfirst.frc.team1731.robot.auto.actions.DrivePathAction;
import org.usfirst.frc.team1731.robot.auto.actions.ElevatorHome;
import org.usfirst.frc.team1731.robot.auto.actions.ElevatorUp;
import org.usfirst.frc.team1731.robot.auto.actions.ParallelAction;
import org.usfirst.frc.team1731.robot.auto.actions.PickUpAction;
import org.usfirst.frc.team1731.robot.auto.actions.ResetPoseFromPathAction;
import org.usfirst.frc.team1731.robot.auto.actions.RotateIntakeActionUp;
import org.usfirst.frc.team1731.robot.auto.actions.SpitAction;
import org.usfirst.frc.team1731.robot.paths.DriveToScoreSwitchRight2;
import org.usfirst.frc.team1731.robot.paths.Left3rdCubePickup2;
import org.usfirst.frc.team1731.robot.paths.LeftScaleToLeftSwitch;
import org.usfirst.frc.team1731.robot.paths.LeftScaleToRightSwitch;
import org.usfirst.frc.team1731.robot.paths.LeftSwitchToLeftScale2;
import org.usfirst.frc.team1731.robot.paths.LeftSwitchToLeftScale3;
import org.usfirst.frc.team1731.robot.paths.PathContainer;
import org.usfirst.frc.team1731.robot.paths.RightToLeftScale;
import org.usfirst.frc.team1731.robot.paths.RightToRightSwitch_H;

public class _12_RightPut1RightSwitchEnd1LeftScale extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
    	System.out.println("Executing _12_RightPut1RightSwitchEnd1LeftScale");
    	
    	PathContainer Path = new RightToLeftScale();
    	runAction(new ResetPoseFromPathAction(Path));
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new RotateIntakeActionUp(),
        		new DrivePathAction(Path),
        })));
        //runAction(new ElevatorUp()); This was here for some reason. Added it into the ParallelAction
		runAction(new ElevatorUp());
        runAction(new SpitAction());

    	Path = new LeftScaleToRightSwitch();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new PickUpAction(), 
        		new DrivePathAction(Path)
        })));
        
        //drive forward and spit
    	Path = new DriveToScoreSwitchRight2();
    	runAction(new DrivePathAction(Path));
        runAction(new SpitAction()); 

        runAction(new ElevatorHome());
	}

}
