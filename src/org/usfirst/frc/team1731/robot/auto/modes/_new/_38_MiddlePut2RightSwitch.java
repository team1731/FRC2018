package org.usfirst.frc.team1731.robot.auto.modes._new;

import java.util.Arrays;

import org.usfirst.frc.team1731.robot.auto.AutoModeBase;
import org.usfirst.frc.team1731.robot.auto.AutoModeEndedException;
import org.usfirst.frc.team1731.robot.auto.actions.Action;
import org.usfirst.frc.team1731.robot.auto.actions.DrivePathAction;
import org.usfirst.frc.team1731.robot.auto.actions.ElevatorHome;
import org.usfirst.frc.team1731.robot.auto.actions.ParallelAction;
import org.usfirst.frc.team1731.robot.auto.actions.PickUpAction;
import org.usfirst.frc.team1731.robot.auto.actions.ResetPoseFromPathAction;
import org.usfirst.frc.team1731.robot.auto.actions.RotateIntakeActionUp;
import org.usfirst.frc.team1731.robot.auto.actions.SpitAction;
import org.usfirst.frc.team1731.robot.paths.MiddleToLeftSwitch;
import org.usfirst.frc.team1731.robot.paths.MiddleToLeftSwitch_B2;
import org.usfirst.frc.team1731.robot.paths.MiddleToLeftSwitch_B3;
import org.usfirst.frc.team1731.robot.paths.MiddleToLeftSwitch_B4;
import org.usfirst.frc.team1731.robot.paths.MiddleToLeftSwitch_B5;
import org.usfirst.frc.team1731.robot.paths.MiddleToRightSwitch;
import org.usfirst.frc.team1731.robot.paths.MiddleToRightSwitch_B2;
import org.usfirst.frc.team1731.robot.paths.MiddleToRightSwitch_B3;
import org.usfirst.frc.team1731.robot.paths.MiddleToRightSwitch_B4;
import org.usfirst.frc.team1731.robot.paths.MiddleToRightSwitch_B5;
import org.usfirst.frc.team1731.robot.paths.PathContainer;

public class _38_MiddlePut2RightSwitch extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
    	System.out.println("Executing _38_MiddlePut2RightSwitch");
    	
    	PathContainer Path = new MiddleToRightSwitch();
    	runAction(new ResetPoseFromPathAction(Path));
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorHome(),
        		new RotateIntakeActionUp(false),
        		new DrivePathAction(Path),
        })));

    	runAction(new SpitAction());
    	
    	Path = new MiddleToRightSwitch_B2();
    	runAction(new DrivePathAction(Path));
    	
    	Path = new MiddleToRightSwitch_B3();
    	runAction(new ParallelAction(Arrays.asList(new Action[] {
    			 new DrivePathAction(Path),
    			 new PickUpAction()
    	 })));

    	
    	Path = new MiddleToRightSwitch_B4();
    	runAction(new DrivePathAction(Path));
    	
    	
    	Path = new MiddleToRightSwitch_B5();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorHome(),
        		new RotateIntakeActionUp(false),
        		new DrivePathAction(Path),
        })));
    	
    	runAction(new SpitAction());
	}

}
