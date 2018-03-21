package org.usfirst.frc.team1731.robot.auto.modes._new;

import java.util.Arrays;

import org.usfirst.frc.team1731.robot.auto.AutoModeBase;
import org.usfirst.frc.team1731.robot.auto.AutoModeEndedException;
import org.usfirst.frc.team1731.robot.auto.actions.Action;
import org.usfirst.frc.team1731.robot.auto.actions.DrivePathAction;
import org.usfirst.frc.team1731.robot.auto.actions.ElevatorHome;
import org.usfirst.frc.team1731.robot.auto.actions.ParallelAction;
import org.usfirst.frc.team1731.robot.auto.actions.PickUpAction;
import org.usfirst.frc.team1731.robot.auto.actions.ResetPoseFromPathAction;
import org.usfirst.frc.team1731.robot.auto.actions.RotateIntakeActionUp;
import org.usfirst.frc.team1731.robot.auto.actions.SpitAction;
import org.usfirst.frc.team1731.robot.paths.PathContainer;
import org.usfirst.frc.team1731.robot.paths.RightToRightSwitch_A;
import org.usfirst.frc.team1731.robot.paths.RightToRightSwitch_B;
import org.usfirst.frc.team1731.robot.paths.RightToRightSwitch_F;
import org.usfirst.frc.team1731.robot.paths.RightToRightSwitch_G;
import org.usfirst.frc.team1731.robot.paths.RightToRightSwitch_H;

public class _11_RightPut1RightSwitchEnd1RightSwitch extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
    	System.out.println("Executing _11_RightPut1RightSwitchEnd1RightSwitch");
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

    	//back up
    	Path = new RightToRightSwitch_F();
    	runAction(new DrivePathAction(Path));

    	//drive forward and pick up
    	Path = new RightToRightSwitch_G();
    	runAction(new ParallelAction(Arrays.asList(new Action[] {
        		new PickUpAction(), 
        		new DrivePathAction(Path)
        })));
  
        //drive forward and spit
    	Path = new RightToRightSwitch_H();
    	runAction(new DrivePathAction(Path));
    	runAction(new SpitAction());
	}

}
