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
public class RightPut3CubesOnLeftScaleThunder extends AutoModeBase {

    @Override
    protected void routine() throws AutoModeEndedException {
    	System.out.println("Executing RightPut3CubesOnLeftScaleThunder");
    	
    	PathContainer Path = new RightToLeftScaleThunder();
    	runAction(new ResetPoseFromPathAction(Path));
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new RotateIntakeActionUp(),
        		new DrivePathAction(Path),
        })));
        //runAction(new ElevatorUp()); This was here for some reason. Added it into the ParallelAction
		runAction(new ElevatorUp());
        runAction(new SpitAction());

    	Path = new LeftScaleToLeftSwitchThunder();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new PickUpAction(), 
        		new DrivePathAction(Path)
        })));
        
        Path = new LeftSwitchToLeftScaleThunder2();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorUp(),
        		new RotateIntakeActionUp(),
        		new DrivePathAction(Path)
        })));
        
        runAction(new SpitAction());
        
        
        Path = new Left3rdCubePickupThunder2();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new PickUpAction(), 
        		new DrivePathAction(Path)
        		
        })));
        
    	Path = new LeftSwitchToLeftScaleThunder3();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorUp(), 
        		new RotateIntakeActionUp(),
        		new DrivePathAction(Path)
        })));
        runAction(new SpitAction()); 

        
        
        
        runAction(new ElevatorHome());
    }
}
