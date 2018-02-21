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
public class SpitAction implements Action {

    Intake mIntake = Intake.getInstance();
    Superstructure mSuperstructure = Superstructure.getInstance();
    double startTime;
    boolean runIntake;

    public SpitAction() {
        runIntake = false;
    }

    public SpitAction(boolean runIntake) {
        this.runIntake = runIntake;
    }

    @Override
    public boolean isFinished() {
        if (runIntake) {
            return Timer.getFPGATimestamp() - startTime > 1.0;
        } else {
            return true;
        }
    }

    @Override
    public void update() {
    }

    @Override
    public void done() {
        if (runIntake) {
            mIntake.setIdle();
        }
    }

    @Override
    public void start() {
    	runIntake = true;
        startTime = Timer.getFPGATimestamp();
        //mIntake.setWantedState(WantedState.SPITTING);
        //mSuperstructure.setWantedElevatorPosition(0);
        mSuperstructure.setWantedState(Superstructure.WantedState.SPITTING);
    }
}
