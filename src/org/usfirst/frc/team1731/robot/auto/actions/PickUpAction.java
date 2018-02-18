package org.usfirst.frc.team1731.robot.auto.actions;

import org.usfirst.frc.team1731.robot.subsystems.Intake;
import org.usfirst.frc.team1731.robot.subsystems.Intake.WantedState;
import org.usfirst.frc.team1731.robot.subsystems.Superstructure;

import edu.wpi.first.wpilibj.Timer;

/**
 * Deploys the intake spit action
 * 
 * @see Action
 */
public class PickUpAction implements Action {

    Intake mIntake = Intake.getInstance();
    Superstructure mSuperstructure = Superstructure.getInstance();
    double startTime;


    public PickUpAction() {
    }

    @Override
    public boolean isFinished() {
        return mIntake.gotCube();
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
        mSuperstructure.setWantedState(Superstructure.WantedState.AUTOINTAKING);
    }
}
