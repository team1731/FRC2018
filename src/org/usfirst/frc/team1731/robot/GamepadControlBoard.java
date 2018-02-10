package org.usfirst.frc.team1731.robot;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Contains the button mappings for the Gamepad control board.  Like the drive code, one instance of the GamepadControlBoard 
 * object is created upon startup, then other methods request the singleton GamepadControlBoard instance.  Implements the 
 * ControlBoardInterface.
 * 
 * @see ControlBoardInterface.java
 */
public class GamepadControlBoard implements ControlBoardInterface {

    private final Joystick mDriver;
    private final Joystick mOperator;
    private static ControlBoardInterface mInstance = null;
    
    public static ControlBoardInterface getInstance() {
    	if (mInstance == null) {
    		mInstance = new GamepadControlBoard();
    	}
    	return mInstance;
    }

    protected GamepadControlBoard() {
    	mOperator = new Joystick(0);
        mDriver = new Joystick(1);
    }
    
    @Override
    public boolean getGrabCubeButton() {
    	 return Math.abs(mOperator.getRawAxis(3)) > .8;
    }
    
    @Override
    public boolean getOverTheTopButton() {
        return mOperator.getRawButton(4);
    }
    
    @Override
    public boolean getSpit() {
        return Math.abs(mOperator.getRawAxis(2)) > .8;
    }
    
    @Override
    public boolean getCalibrateDown() {
        return mOperator.getRawButton(7);
    }
    
    @Override
    public boolean getCalibrateUp() {
        return mOperator.getRawButton(8);
    }


    @Override
    public double getThrottle() {
        return -mDriver.getRawAxis(1);
    }

    boolean getGrabCubeButton = false;  
    
    @Override
    public double getTurn() {
        return mDriver.getRawAxis(4);
    }

    @Override
    public boolean getQuickTurn() {
        // R1
        return mDriver.getRawButton(6);
    }

    @Override
    public boolean getLowGear() {
        // L1
        return mDriver.getRawButton(5);

    }

    @Override
    public boolean getClimbUp() {
        // A
        return (mOperator.getPOV(1) == 1) &  mOperator.getRawButton(1);
    }
    
    @Override
    public boolean getClimbDown() {
        // A
        return (mOperator.getPOV(1) == 5) &  mOperator.getRawButton(1);
    }

  
    @Override
    public boolean getBlinkLEDButton() {
        return false;
    }

	@Override
	public double getElevatorControl() {
		return mOperator.getRawAxis(1);
		//return 0.3;
	}
}
