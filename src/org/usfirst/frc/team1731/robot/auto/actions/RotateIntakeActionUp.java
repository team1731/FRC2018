package org.usfirst.frc.team1731.robot.auto.actions;

import org.usfirst.frc.team1731.robot.subsystems.Drive;
import org.usfirst.frc.team1731.robot.subsystems.Intake;
import org.usfirst.frc.team1731.robot.subsystems.Superstructure;
import org.usfirst.frc.team1731.robot.Constants;

/**
 * Action to begin shooting.
 * 
 * @see Action
 * @see RunOnceAction
 */
public class RotateIntakeActionUp extends RunOnceAction implements Action {

	private boolean up = true;
	
	public RotateIntakeActionUp() {
	}
	
	public RotateIntakeActionUp(boolean up) {
		this.up = up;
	}
	
    @Override
    public void runOnce() {

        //Superstructure.getInstance().setOverTheTop(up);
        if (!up) {
        	Superstructure.getInstance.setOverTheTop(Constants.kElevatorFlipDown);
        } else {
        	Superstructure.getInstance.setOverTheTop(Constants.kElevatorFlipUp);        	
        }

    }

}
