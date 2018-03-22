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
import org.usfirst.frc.team1731.robot.auto.actions.SetElevatorPostition;
import org.usfirst.frc.team1731.robot.auto.actions.SpitAction;
import org.usfirst.frc.team1731.robot.auto.actions.WaitAction;
import org.usfirst.frc.team1731.robot.paths.DriveToScoreSwitchRight;
import org.usfirst.frc.team1731.robot.paths.DriveToScoreSwitchRight2;
import org.usfirst.frc.team1731.robot.paths.LeftToRightScale2;
import org.usfirst.frc.team1731.robot.paths.LeftToRightScale3;
import org.usfirst.frc.team1731.robot.paths.PathContainer;
import org.usfirst.frc.team1731.robot.paths.Right3rdCubeBackup;
import org.usfirst.frc.team1731.robot.paths.Right3rdCubePickup;
import org.usfirst.frc.team1731.robot.paths.Right3rdCubeScore;
import org.usfirst.frc.team1731.robot.paths.RightScaleToRightSwitch;
import org.usfirst.frc.team1731.robot.paths.RightScaleToRightSwitch2;

public class _20_LeftPut2RightScale1RightSwitch extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
    	System.out.println("Executing _20_LeftPut2RightScale1RightSwitch");
    	
    	PathContainer Path = new LeftToRightScale3();
    	runAction(new ResetPoseFromPathAction(Path));
        runAction(new ParallelAction(Arrays.asList(new Action[] { 
        		new RotateIntakeActionUp(),
        		new DrivePathAction(Path),
        })));
        runAction(new ElevatorUp());
        runAction(new SpitAction());
        

    	Path = new RightScaleToRightSwitch(); // PATH #2
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new PickUpAction(), 
        		new DrivePathAction(Path)
        		
        })));
  
    	Path = new DriveToScoreSwitchRight(); // PATH #3
		runAction(new DrivePathAction(Path));
		runAction(new SetElevatorPostition());
        runAction(new SpitAction()); // SPIT #2
        // approx 10 sec. to here

    	Path = new Right3rdCubeBackup(); // PATH #4
        runAction(new ParallelAction(Arrays.asList(new Action[] { 
        		new RotateIntakeActionUp(false),
        		//new ElevatorDown(),
        		new DrivePathAction(Path)
        })));
        
        
    	Path = new Right3rdCubePickup(); // PATH #5
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new PickUpAction(), 
        		new DrivePathAction(Path)
        })));
        
    	Path = new Right3rdCubeScore(); // PATH #6
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorUp(), 
        		new RotateIntakeActionUp(),
        		new DrivePathAction(Path),
        		new WaitAction(1)
        		
        })));

    	runAction(new SpitAction()); // SPIT #3
	}

}
