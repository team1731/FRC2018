package org.usfirst.frc.team1731.robot.auto.modes._new;

import java.util.Arrays;

import org.usfirst.frc.team1731.lib.util.math.Rotation2d;
import org.usfirst.frc.team1731.robot.auto.AutoModeBase;
import org.usfirst.frc.team1731.robot.auto.AutoModeEndedException;
import org.usfirst.frc.team1731.robot.auto.actions.Action;
import org.usfirst.frc.team1731.robot.auto.actions.DrivePathAction;
import org.usfirst.frc.team1731.robot.auto.actions.ElevatorDown;
import org.usfirst.frc.team1731.robot.auto.actions.ElevatorHome;
import org.usfirst.frc.team1731.robot.auto.actions.ParallelAction;
import org.usfirst.frc.team1731.robot.auto.actions.PickUpAction;
import org.usfirst.frc.team1731.robot.auto.actions.ResetPoseFromPathAction;
import org.usfirst.frc.team1731.robot.auto.actions.RotateIntakeActionUp;
import org.usfirst.frc.team1731.robot.auto.actions.SpitAction;
import org.usfirst.frc.team1731.robot.auto.actions.TurnToHeadingAction;
import org.usfirst.frc.team1731.robot.paths.MiddleToExchange_1;
import org.usfirst.frc.team1731.robot.paths.MiddleToRightSwitch;
import org.usfirst.frc.team1731.robot.paths.MiddleToRightSwitch_B2;
import org.usfirst.frc.team1731.robot.paths.MiddleToRightSwitch_B3;
import org.usfirst.frc.team1731.robot.paths.MiddleToRightSwitch_B3_1;
import org.usfirst.frc.team1731.robot.paths.MiddleToRightSwitch_B3_2;
import org.usfirst.frc.team1731.robot.paths.PathContainer;

public class _39_MiddlePut1RightSwitch1Exchange extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
    	System.out.println("Executing _39_MiddlePut1RightSwitch1Exchange");
    	
    	PathContainer Path = new MiddleToRightSwitch();
    	runAction(new ResetPoseFromPathAction(Path));
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorHome(),
        		new RotateIntakeActionUp(false),
        		new DrivePathAction(Path),
        })));

    	runAction(new SpitAction());
    	
    	Path = new MiddleToRightSwitch_B2();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorDown(),
        		new RotateIntakeActionUp(false),
        		new DrivePathAction(Path),
        })));
    	
    	Path = new MiddleToRightSwitch_B3();
    	runAction(new ParallelAction(Arrays.asList(new Action[] {
    			 new DrivePathAction(Path),
    			 new PickUpAction()
    	 })));

    	
    	// DO NOT USE !!! runAction(new TurnToHeadingAction(Rotation2d.fromDegrees(180.0)));
    	
    	Path = new MiddleToRightSwitch_B3_1();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorDown(),
        		new RotateIntakeActionUp(false),
        		new DrivePathAction(Path),
        })));
    	
    	
    	Path = new MiddleToRightSwitch_B3_2();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorDown(),
        		new RotateIntakeActionUp(false),
        		new DrivePathAction(Path),
        })));
    	
    	runAction(new SpitAction());
	}

}
