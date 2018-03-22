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
public class LeftPutCubeOnLeftScaleAndRightSwitch extends AutoModeBase {

    @Override
    protected void routine() throws AutoModeEndedException {
    	System.out.println("Executing LeftPutCubeOnLeftScaleAndRightSwitch");
    	
    	PathContainer Path = new LeftToLeftScale2();
    	runAction(new ResetPoseFromPathAction(Path));
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorUp(), 
        		new RotateIntakeActionUp(),
        		new DrivePathAction(Path)
        })));

    	runAction(new SpitAction());
    	Path = new LeftScaleToLeftSwitch2();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new PickUpAction(), 
        		new DrivePathAction(Path)
        })));
        
    	Path = new LeftSwitchToRightSwitchBackup2();
        runAction(new ParallelAction(Arrays.asList(new Action[] { 
        		new RotateIntakeActionUp(false),
        		new DrivePathAction(Path)
        })));
        
    	Path = new LeftSwitchToRightSwitch2();
        runAction(new ParallelAction(Arrays.asList(new Action[] { 
        		new RotateIntakeActionUp(false),
        		new DrivePathAction(Path)
        })));
        runAction(new SpitAction()); 

    	runAction(new WaitAction(1));
    	
    }
}
