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
public class LeftPutCubeOnLeftSwitch extends AutoModeBase {

    @Override
    protected void routine() throws AutoModeEndedException {
    	System.out.println("Executing LeftPutCubeOnLeftSwitch");
    	PathContainer Path = new LeftToLeftSwitch_A2();
    	runAction(new ResetPoseFromPathAction(Path));
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new RotateIntakeActionUp(false), //stay down
        		new DrivePathAction(Path)
        })));
        
        //drive forward and spit
    	Path = new LeftToLeftSwitch_B2();
    	runAction(new DrivePathAction(Path));
    	runAction(new SpitAction());
    	    	
    	//back up
    	Path = new LeftToLeftSwitch_C2();
    	runAction(new DrivePathAction(Path));

    	//drive forward and pick up
    	Path = new LeftToLeftSwitch_D2();
    	runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new PickUpAction(), 
        		new DrivePathAction(Path)
        })));
  
    	Path = new LeftToLeftSwitch_E2();; 
        runAction(new DrivePathAction(Path));
		runAction(new SetElevatorPostition());
        runAction(new SpitAction()); 
    }
}
