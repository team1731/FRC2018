package org.usfirst.frc.team1731.robot.auto.actions;

import org.usfirst.frc.team1731.robot.subsystems.Elevator;
import org.usfirst.frc.team1731.robot.subsystems.Intake.WantedState;
import org.usfirst.frc.team1731.robot.subsystems.Superstructure;

import edu.wpi.first.wpilibj.Timer;

/**
 * Deploys the elevator down action
 * 
 * @see Action
 */
public class ElevatorDown implements Action {

    private static final double DESIRED_POSITION = -1.0;
	Elevator mElevator = Elevator.getInstance();
	Superstructure mSuperstructure = Superstructure.getInstance();


    @Override
    public boolean isFinished() {
        return Math.abs(mElevator.getCurrentPosition(true) - DESIRED_POSITION) < 0.05;
    }

    @Override
    public void update() {
    	mSuperstructure.setWantedState(Superstructure.WantedState.ELEVATOR_TRACKING);
    }

    @Override
    public void done() {
    }

    @Override
    public void start() {
    	mSuperstructure.setWantedElevatorPosition(DESIRED_POSITION);
    	//mElevator.setWantedPosition(DESIRED_POSITION);
    }
}
