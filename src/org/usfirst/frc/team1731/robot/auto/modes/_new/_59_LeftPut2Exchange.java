package org.usfirst.frc.team1731.robot.auto.modes._new;

import java.util.Arrays;

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
import org.usfirst.frc.team1731.robot.paths.LeftToExchange_1;
import org.usfirst.frc.team1731.robot.paths.LeftToExchange_2;
import org.usfirst.frc.team1731.robot.paths.LeftToExchange_3;
import org.usfirst.frc.team1731.robot.paths.LeftToExchange_4;
import org.usfirst.frc.team1731.robot.paths.LeftToExchange_5;
import org.usfirst.frc.team1731.robot.paths.LeftToExchange_6;
import org.usfirst.frc.team1731.robot.paths.PathContainer;

public class _59_LeftPut2Exchange extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
    	System.out.println("Executing _59_LeftPut2Exchange");
    	
    	PathContainer Path = new LeftToExchange_1();
    	runAction(new ResetPoseFromPathAction(Path));
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorHome(),
        		new RotateIntakeActionUp(false),
        		new DrivePathAction(Path),
        })));

    	Path = new LeftToExchange_2();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorDown(),
        		new RotateIntakeActionUp(false),
        		new DrivePathAction(Path),
        })));

        runAction(new SpitAction());
    	
    	Path = new LeftToExchange_3();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorDown(),
        		new RotateIntakeActionUp(false),
        		new DrivePathAction(Path),
        })));
    	
    	Path = new LeftToExchange_4();
    	runAction(new ParallelAction(Arrays.asList(new Action[] {
    			 new DrivePathAction(Path),
    			 new PickUpAction()
    	 })));

    	
    	
    	Path = new LeftToExchange_5();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorDown(),
        		new RotateIntakeActionUp(false),
        		new DrivePathAction(Path),
        })));
    	
    	
    	Path = new LeftToExchange_6();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorDown(),
        		new RotateIntakeActionUp(false),
        		new DrivePathAction(Path),
        })));
    	
    	runAction(new SpitAction());
	}

}
