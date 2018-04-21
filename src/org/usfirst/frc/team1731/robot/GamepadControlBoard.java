package org.usfirst.frc.team1731.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;

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
        mDriver = new Joystick(0);
    	mOperator = new Joystick(1);
    }
    
    @Override
    public boolean getGrabCubeButton() {
    	 return Math.abs(mOperator.getRawAxis(3)) > .8;
    }
    
    @Override
    public boolean getOverTheTopButton() {
        return mOperator.getRawButton(5);
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
		//    	Get the angle in degrees of a POV on the HID. 
		//
		//    	The POV angles start at 0 in the up direction, and increase clockwise
    	//		(eg right is 90, upper-left is 315).
		//    	Parameters:pov The index of the POV to read (starting at 0)
    	//		Returns:the angle of the POV in degrees, or -1 if the POV is not pressed.
		//
        int pov = mOperator.getPOV(0);    	
        return ((pov != -1) && (pov > 315 || pov < 45)) &&  mOperator.getRawButton(1);
        //return mOperator.getRawButton(1);
    }
    
    @Override
    public boolean getClimbDown() {
        // A
		//    	Get the angle in degrees of a POV on the HID. 
		//
		//    	The POV angles start at 0 in the up direction, and increase clockwise
    	//		(eg right is 90, upper-left is 315).
		//    	Parameters:pov The index of the POV to read (starting at 0)
    	//		Returns:the angle of the POV in degrees, or -1 if the POV is not pressed.
		//    	
        return (mOperator.getPOV(0) > 135 && mOperator.getPOV(0) < 225) &&  mOperator.getRawButton(1);
        //return mOperator.getRawButton(1);
    }

    @Override
    public boolean getFishingPoleExtend() {
        // A
		//    	Get the angle in degrees of a POV on the HID. 
		//
		//    	The POV angles start at 0 in the up direction, and increase clockwise
    	//		(eg right is 90, upper-left is 315).
		//    	Parameters:pov The index of the POV to read (starting at 0)
    	//		Returns:the angle of the POV in degrees, or -1 if the POV is not pressed.
		//
        int pov = mOperator.getPOV(0);    	
        return ((pov != -1) && (pov > 310 || pov < 50)) && fishingPoleEnabled();
    }
    
    @Override
    public boolean getFishingPoleRetract() {
        // A
		//    	Get the angle in degrees of a POV on the HID. 
		//
		//    	The POV angles start at 0 in the up direction, and increase clockwise
    	//		(eg right is 90, upper-left is 315).
		//    	Parameters:pov The index of the POV to read (starting at 0)
    	//		Returns:the angle of the POV in degrees, or -1 if the POV is not pressed.
		//  
    	int pov = mOperator.getPOV(0);
        return ((pov !=1) && (pov > 130) && (pov < 230)) && fishingPoleEnabled();
        //return mOperator.getRawButton(1);
    }
    
    @Override
    public boolean getFishingPoleUp() {
        // A
		//    	Get the angle in degrees of a POV on the HID. 
		//
		//    	The POV angles start at 0 in the up direction, and increase clockwise
    	//		(eg right is 90, upper-left is 315).
		//    	Parameters:pov The index of the POV to read (starting at 0)
    	//		Returns:the angle of the POV in degrees, or -1 if the POV is not pressed.
		//
        int pov = mOperator.getPOV(0);    	
        return ((pov != -1) && (pov > 45 && pov < 135)) && fishingPoleEnabled();
        //return mOperator.getRawButton(1);
    }
    
    @Override
    public boolean getFishingPoleDown() {
        // A
		//    	Get the angle in degrees of a POV on the HID. 
		//
		//    	The POV angles start at 0 in the up direction, and increase clockwise
    	//		(eg right is 90, upper-left is 315).
		//    	Parameters:pov The index of the POV to read (starting at 0)
    	//		Returns:the angle of the POV in degrees, or -1 if the POV is not pressed.
		//  
    	int pov = mOperator.getPOV(0);
        return ((pov !=1) && (pov > 225) && (pov < 315)) && fishingPoleEnabled();
        //return mOperator.getRawButton(1);
    }

  
    @Override
    public boolean getBlinkLEDButton() {
        return false;
    }

    @Override
	public boolean getElevatorButton() {
		return mOperator.getRawButton(2); // getButtonB
	}

    @Override
    public boolean getFlipDownButton() {
        return mOperator.getRawButton(3); // getButtonX
    }

    @Override
    public boolean getFlipUpButton() {
        return mOperator.getRawButton(4); // getButtonY
    }

	@Override
	public double getElevatorControl() {
		return mOperator.getRawAxis(1);
		//return 0.3;
	}
	
	@Override
    public boolean getAutoPickUp() {
        // R1
        return mOperator.getRawButton(6);
    }
	
	private boolean fishingPoleEnabled() {
		return mOperator.getRawButton(10);
	}
}
