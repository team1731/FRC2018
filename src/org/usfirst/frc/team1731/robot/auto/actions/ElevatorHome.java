package org.usfirst.frc.team1731.robot.auto.actions;

import org.usfirst.frc.team1731.robot.subsystems.Elevator;
import org.usfirst.frc.team1731.robot.subsystems.Intake.WantedState;

import edu.wpi.first.wpilibj.Timer;

/**
 * Deploys the elevator up action
 * 
 * @see Action
 */
public class ElevatorHome implements Action {

    private static final double DESIRED_POSITION = 0.0;
	Elevator mElevator = Elevator.getInstance();

    @Override
    public boolean isFinished() {
        return Math.abs(mElevator.getCurrentPosition(false) - DESIRED_POSITION) < 0.05;
    }

    @Override
    public void update() {
    }

    @Override
    public void done() {
    }

    @Override
    public void start() {
    	mElevator.setWantedPosition(DESIRED_POSITION);
    }
}
