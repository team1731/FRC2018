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
public class RightPutCubeOnRightScaleAndLeftSwitch extends AutoModeBase {

    @Override
    protected void routine() throws AutoModeEndedException {
    	System.out.println("Executing RightPutCubeOnRightScaleAndLeftSwitch");
    	
    	PathContainer Path = new RightToRightScale();
    	runAction(new ResetPoseFromPathAction(Path));
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new ElevatorUp(), 
        		new RotateIntakeActionUp(),
        		new DrivePathAction(Path)
        		 //       		new SeriesAction (Arrays.asList(new Action[] {
        		 //               		new WaitForPathMarkerAction("spit"), 
        		 //               		new SpitAction()
        		 //       		}))
        })));

    	runAction(new SpitAction());
    	Path = new RightScaleToRightSwitch();
        runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new PickUpAction(), 
        		new DrivePathAction(Path)
        })));
        
    	Path = new RightSwitchToLeftSwitchBackup();
        runAction(new ParallelAction(Arrays.asList(new Action[] { 
        		new RotateIntakeActionUp(false),
        		new DrivePathAction(Path)
        })));
        
    	Path = new RightSwitchToLeftSwitch();
        runAction(new ParallelAction(Arrays.asList(new Action[] { 
        		new RotateIntakeActionUp(false),
        		new DrivePathAction(Path)
        })));
        runAction(new SpitAction()); 

    	runAction(new WaitAction(1));
    	
    }
}
