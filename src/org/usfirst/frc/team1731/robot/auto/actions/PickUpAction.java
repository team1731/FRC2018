package org.usfirst.frc.team1731.robot.auto.actions;

import org.usfirst.frc.team1731.robot.Constants;
import org.usfirst.frc.team1731.robot.Constants.GRABBER_POSITION;
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
    boolean gotCube;

    public PickUpAction() {
    }

    @Override
    public boolean isFinished() {
    //	System.out.println("in isFinished:" + mIntake.gotCube() + ", " + mElevator.getCurrentPosition(true));
        return (gotCube && (Math.abs(mElevator.getCurrentPosition(true))  < 0.1));

    }

    @Override
    public void update() {
    	if(mIntake.gotCube()) {
    		gotCube = true;
    	}
    }

    @Override
    public void done() {
    	System.out.println("finished pickup action"); 	
    }

    @Override
    public void start() {
    	gotCube = false;
        startTime = Timer.getFPGATimestamp();
        mSuperstructure.setWantedState(Superstructure.WantedState.AUTOINTAKING);
        //Superstructure.getInstance().setOverTheTop(false);
        Superstructure.getInstance().setOverTheTop(GRABBER_POSITION.FLIP_DOWN);
    	System.out.println("started pickup action"); 	
    }
}
