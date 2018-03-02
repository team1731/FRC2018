package org.usfirst.frc.team1731.robot.auto.modes;

import java.util.Arrays;

import org.usfirst.frc.team1731.lib.util.math.RigidTransform2d;
import org.usfirst.frc.team1731.robot.auto.AutoModeBase;
import org.usfirst.frc.team1731.robot.auto.AutoModeEndedException;
import org.usfirst.frc.team1731.robot.auto.actions.*;
import org.usfirst.frc.team1731.robot.paths.*;

import edu.wpi.first.wpilibj.Timer;

/**
 * Scores the preload gear onto the boiler-side peg then deploys the hopper and shoots all 60 balls (10 preload + 50
 * hopper).
 * 
 * This was the primary autonomous mode used at SVR, St. Louis Champs, and FOC.
 * 
 * @see AutoModeBase
 */
public class RightPutCubeOnRightScaleAndRightSwitch extends AutoModeBase {

    @Override
    protected void routine() throws AutoModeEndedException {
    	System.out.println("executing RightPutCubeOnRightScaleAndRightSwitch");
    	
    	PathContainer Path = new RightToRightScale(); // PATH #1
    	runAction(new ResetPoseFromPathAction(Path));
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorUp(), 
        		new RotateIntakeActionUp(),
        		new DrivePathAction(Path),
        })));

    	runAction(new SpitAction()); // SPIT #1
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
