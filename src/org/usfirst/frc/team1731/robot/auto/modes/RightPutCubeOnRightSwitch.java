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
public class RightPutCubeOnRightSwitch extends AutoModeBase {

    @Override
    protected void routine() throws AutoModeEndedException {
    	System.out.println("Executing PutCubeOnrightSwitch");
    	PathContainer Path = new RightToRightSwitch_A();
    	runAction(new ResetPoseFromPathAction(Path));
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new RotateIntakeActionUp(false), //stay down
        		new DrivePathAction(Path),
        		new ElevatorHome()
        })));
        
        //drive forward and spit
    	Path = new RightToRightSwitch_B();
    	runAction(new DrivePathAction(Path));
    	runAction(new SpitAction());
/*  we still need to debug and tweak the rest of this auto mode! RDB 3-8-18  	    	
    	//back up
    	Path = new RightToRightSwitch_C();
    	runAction(new DrivePathAction(Path));

    	//drive forward and pick up
    	Path = new RightToRightSwitch_D();
    	runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new PickUpAction(), 
        		new DrivePathAction(Path)
        })));
  
    	Path = new RightToRightSwitch_E();
*/
        
    }
}
