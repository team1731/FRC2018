package org.usfirst.frc.team1731.robot.auto.actions;

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
public class PickUpAction implements Action {

    Intake mIntake = Intake.getInstance();
    Elevator mElevator = Elevator.getInstance();
    Superstructure mSuperstructure = Superstructure.getInstance();
    double startTime;


    public PickUpAction() {
    }

    @Override
    public boolean isFinished() {
    //	System.out.println("in isFinished:" + mIntake.gotCube() + ", " + mElevator.getCurrentPosition(true));
        return (mIntake.gotCube() && (Math.abs(mElevator.getCurrentPosition(true))  < 0.1));

    }

    @Override
    public void update() {
    }

    @Override
    public void done() {
    	System.out.println("finished pickup action"); 	
    }

    @Override
    public void start() {
        startTime = Timer.getFPGATimestamp();
        mSuperstructure.setWantedState(Superstructure.WantedState.AUTOINTAKING);
        Superstructure.getInstance().setOverTheTop(false);
    	System.out.println("started pickup action"); 	
    }
}
