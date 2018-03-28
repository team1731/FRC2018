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
import org.usfirst.frc.team1731.robot.paths.RightToExchange_1;
import org.usfirst.frc.team1731.robot.paths.RightToExchange_2;

public class _56_RightPut1Exchange extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
    	System.out.println("Executing _56_RightPut1Exchange");
    	
    	PathContainer Path = new RightToExchange_1();
    	runAction(new ResetPoseFromPathAction(Path));
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorHome(),
        		new RotateIntakeActionUp(false),
        		new DrivePathAction(Path),
        })));

    	Path = new RightToExchange_2();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorDown(),
        		new RotateIntakeActionUp(false),
        		new DrivePathAction(Path),
        })));

        runAction(new SpitAction());
    	
	}

}
