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

    private final Joystick mGamepad;
    private final Joystick mExtreme3D;

    protected GamepadControlBoard() {
    	mExtreme3D = new Joystick(0);
        mGamepad = new Joystick(1);
    }
    
    @Override
    public boolean getGrabCubeButton() {
        return mExtreme3D.getRawButton(8);
    }
    
    @Override
    public boolean getOverTheTopButton() {
        return mExtreme3D.getRawButton(9);
    }


    @Override
    public double getThrottle() {
        return -mGamepad.getRawAxis(1);
    }

    boolean getGrabCubeButton = false;  
    
    @Override
    public double getTurn() {
        return mGamepad.getRawAxis(4);
    }

    @Override
    public boolean getQuickTurn() {
        // R1
        return mGamepad.getRawButton(6);
    }

    @Override
    public boolean getAimButton() {
        return mGamepad.getRawButton(8);
    }

    @Override
    public boolean getLowGear() {
        // L1
        return mGamepad.getRawButton(5);

    }

    @Override
    public boolean getHangButton() {
        // A
        return mGamepad.getRawButton(1);
    }

    @Override
    public boolean getIntakeButton() {
        // L1
        return mGamepad.getRawButton(5);
    }

    @Override
    public boolean getOuttakeButton() {
        // L1
        return mGamepad.getRawButton(6);
    }

    @Override
    public boolean getFeedButton() {
        // X
        return false;
    }

    @Override
    public boolean getGrabGearButton() {
        // L Trigger
        return mGamepad.getRawAxis(2) > 0.1;
    }

    @Override
    public boolean getScoreGearButton() {
        return mGamepad.getRawAxis(3) > 0.1;
    }

    @Override
    public boolean getShooterOpenLoopButton() {
        // Y
        return mGamepad.getRawButton(4);
    }

    @Override
    public boolean getExhaustButton() {
        return false;
    }

    @Override
    public boolean getUnjamButton() {
        return false;
    }

    @Override
    public boolean getShooterClosedLoopButton() {
        // Back
        return false;
    }

    @Override
    public boolean getFlywheelSwitch() {
        return false;
    }

    @Override
    public boolean getActuateHopperButton() {
        return mGamepad.getRawButton(9);
    }

    @Override
    public boolean getBlinkLEDButton() {
        return false;
    }

    @Override
    public boolean getRangeFinderButton() {
        // B
        return mGamepad.getRawButton(3);
    }

    @Override
    public boolean getWantGearDriveLimit() {
        return false;
    }

    @Override
    public boolean getDriveAimButton() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
	public boolean getElevatorUpButton() {
		// TODO Auto-generated method stub
		return false;
	}
    @Override
   	public boolean getElevatorDownButton() {
   		// TODO Auto-generated method stub
   		return false;
   	}
    @Override
	public boolean getButtonB() {
		return mGamepad.getRawButton(2);
	}
    @Override
   	public boolean getButtonA() {
   		return mGamepad.getRawButton(1);
   	}

	@Override
	public double getElevatorControl() {
		return mExtreme3D.getRawAxis(3);
		//return 0.3;
	}
}
