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
import org.usfirst.frc.team1731.robot.paths.PathContainer;
import org.usfirst.frc.team1731.robot.paths.Right3rdCubePickup;
import org.usfirst.frc.team1731.robot.paths.Right3rdCubeScoreScale3;
import org.usfirst.frc.team1731.robot.paths.RightScaleEndToRightSwitch;
import org.usfirst.frc.team1731.robot.paths.RightScaleToRightSwitch;
import org.usfirst.frc.team1731.robot.paths.RightSwitchToRightScale;
import org.usfirst.frc.team1731.robot.paths.RightSwitchToRightScaleEnd;
import org.usfirst.frc.team1731.robot.paths.RightToRightScale;
import org.usfirst.frc.team1731.robot.paths.RightToRightScaleEnd;

public class _43_RightPut2RightScaleEnd extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
    	System.out.println("Executing _43_RightPut2RightScaleEnd");

    	PathContainer Path = new RightToRightScale(); // PATH #1
    	runAction(new ResetPoseFromPathAction(Path));
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorUp(), 
        		new RotateIntakeActionUp(),
        		new DrivePathAction(Path)
        })));

    	runAction(new SpitAction()); // SPIT #1
    	Path = new RightScaleToRightSwitch(); // PATH #2
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new PickUpAction(), 
        		new DrivePathAction(Path)
        		
        })));
        
  
        System.out.println("Running final ParallelAction");
    	Path = new RightSwitchToRightScaleEnd();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorUp(), 
        		new RotateIntakeActionUp(),
        		new DrivePathAction(Path)
        })));
        System.out.println("SPITTT");
        runAction(new SpitAction());         
        
        System.out.println("Waiting 1 second");
    	runAction(new WaitAction(1));
	
	}
}
