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
public class RightPutCubeOnLeftScale extends AutoModeBase {

    @Override
    protected void routine() throws AutoModeEndedException {
    	System.out.println("Executing RightPutCubeOnLeftScale");
    	
    	PathContainer Path = new RightToLeftScale();
    	runAction(new ResetPoseFromPathAction(Path));
        runAction(new ParallelAction(Arrays.asList(new Action[] { 
        		new RotateIntakeActionUp(),
        		new DrivePathAction(Path),
        })));
        runAction(new ElevatorUp());
        runAction(new SpitAction());
        

    	Path = new LeftScaleToLeftSwitch();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new PickUpAction(), 
        		new DrivePathAction(Path)
        		
        })));
  /*
    	Path = new DriveToScoreSwitchLeft();
		runAction(new DrivePathAction(Path));
        runAction(new SpitAction()); 
        // approx 10 sec. to here

    	runAction(new WaitAction(1));
    	*/
    }
}
