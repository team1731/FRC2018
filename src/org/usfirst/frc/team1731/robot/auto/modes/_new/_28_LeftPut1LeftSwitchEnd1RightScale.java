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
import org.usfirst.frc.team1731.robot.paths.DriveToScoreSwitchLeft4;
import org.usfirst.frc.team1731.robot.paths.DriveToScoreSwitchRight2;
import org.usfirst.frc.team1731.robot.paths.LeftScaleToRightSwitch;
import org.usfirst.frc.team1731.robot.paths.LeftToRightScale4;
import org.usfirst.frc.team1731.robot.paths.PathContainer;
import org.usfirst.frc.team1731.robot.paths.RightScaleToLeftSwitch4;
import org.usfirst.frc.team1731.robot.paths.RightToLeftScale;

public class _28_LeftPut1LeftSwitchEnd1RightScale extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
    	System.out.println("Executing _28_LeftPut1LeftSwitchEnd1RightScale");
    	
    	PathContainer Path = new LeftToRightScale4();
    	runAction(new ResetPoseFromPathAction(Path));
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new RotateIntakeActionUp(),
        		new DrivePathAction(Path),
        })));

		runAction(new ElevatorUp());
        runAction(new SpitAction());

    	Path = new RightScaleToLeftSwitch4();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new PickUpAction(), 
        		new DrivePathAction(Path)
        })));
        
        //drive forward and spit
    	Path = new DriveToScoreSwitchLeft4();
    	runAction(new DrivePathAction(Path));
        runAction(new SpitAction()); 

        runAction(new ElevatorHome());
	}

}
