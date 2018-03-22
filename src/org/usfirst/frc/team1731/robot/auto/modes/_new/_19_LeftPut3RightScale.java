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
import org.usfirst.frc.team1731.robot.paths.LeftToRightScale2;
import org.usfirst.frc.team1731.robot.paths.LeftToRightScale3;
import org.usfirst.frc.team1731.robot.paths.PathContainer;
import org.usfirst.frc.team1731.robot.paths.Right3rdCubeBackup;
import org.usfirst.frc.team1731.robot.paths.Right3rdCubePickup;
import org.usfirst.frc.team1731.robot.paths.Right3rdCubeScore;
import org.usfirst.frc.team1731.robot.paths.Right3rdCubeScoreScale3;
import org.usfirst.frc.team1731.robot.paths.RightScaleToRightSwitch;
import org.usfirst.frc.team1731.robot.paths.RightScaleToRightSwitch2;
import org.usfirst.frc.team1731.robot.paths.RightSwitchToRightScale;

public class _19_LeftPut3RightScale extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
    	System.out.println("Executing _19_LeftPut3RightScale");
    	
    	PathContainer Path = new LeftToRightScale3();
    	runAction(new ResetPoseFromPathAction(Path));
        runAction(new ParallelAction(Arrays.asList(new Action[] { 
        		new RotateIntakeActionUp(),
        		new DrivePathAction(Path),
        })));
        runAction(new ElevatorUp());
        runAction(new SpitAction());
        

    	Path = new RightScaleToRightSwitch();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new PickUpAction(), 
        		new DrivePathAction(Path)
        })));
  
    	Path = new RightSwitchToRightScale();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorUp(), 
        		new RotateIntakeActionUp(),
        		new DrivePathAction(Path)
        })));
        runAction(new SpitAction()); 

    	Path = new Right3rdCubePickup(); // PATH #5
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new PickUpAction(), 
        		new DrivePathAction(Path)
        })));
        
        
    	Path = new Right3rdCubeScoreScale3();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorUp(), 
        		new RotateIntakeActionUp(),
        		new DrivePathAction(Path)
        })));
        runAction(new SpitAction()); 
        
        
        
    	runAction(new WaitAction(1));
	}

}
