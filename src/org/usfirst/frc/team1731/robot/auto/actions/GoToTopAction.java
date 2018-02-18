package org.usfirst.frc.team1731.robot.auto.actions;

import org.usfirst.frc.team1731.robot.Constants;
import org.usfirst.frc.team1731.robot.subsystems.Elevator;
import org.usfirst.frc.team1731.robot.subsystems.Intake;
import org.usfirst.frc.team1731.robot.subsystems.Intake.WantedState;
import org.usfirst.frc.team1731.robot.subsystems.Superstructure;

import edu.wpi.first.wpilibj.Timer;

/**
 * Deploys the intake spit action
 * 
 * @see Action
 */
public class GoToTopAction implements Action {

    Intake mIntake = Intake.getInstance();
    Elevator mElevator = Elevator.getInstance();
    Superstructure mSuperstructure = Superstructure.getInstance();
    double startTime;


    public GoToTopAction() {
    }

    @Override
    public boolean isFinished() {
        return mElevator.atTop();
    }

    @Override
    public void update() {
    }

    @Override
    public void done() {
    }

    @Override
    public void start() {
        startTime = Timer.getFPGATimestamp();
        mSuperstructure.setWantedElevatorPosition(1);
        mSuperstructure.setWantedState(Superstructure.WantedState.ELEVATOR_TRACKING);
    }
}
