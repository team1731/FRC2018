package org.usfirst.frc.team1731.robot.subsystems;

import java.util.Optional;

import org.usfirst.frc.team1731.lib.util.CircularBuffer;
import org.usfirst.frc.team1731.lib.util.InterpolatingDouble;
import org.usfirst.frc.team1731.lib.util.drivers.RevRoboticsAirPressureSensor;
import org.usfirst.frc.team1731.robot.Constants;
import org.usfirst.frc.team1731.robot.Constants.GRABBER_POSITION;
import org.usfirst.frc.team1731.robot.Robot;
import org.usfirst.frc.team1731.robot.RobotState;
import org.usfirst.frc.team1731.robot.ShooterAimingParameters;
import org.usfirst.frc.team1731.robot.loops.Loop;
import org.usfirst.frc.team1731.robot.loops.Looper;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;


/**
 * The superstructure subsystem is the overarching superclass containing all components of the superstructure: the
 * intake, hopper, feeder, shooter and LEDs. The superstructure subsystem also contains some miscellaneous hardware that
 * is located in the superstructure but isn't part of any other subsystems like the compressor, pressure sensor, and
 * hopper wall pistons.
 * 
 * Instead of interacting with subsystems like the feeder and intake directly, the {@link Robot} class interacts with
 * the superstructure, which passes on the commands to the correct subsystem.
 * 
 * The superstructure also coordinates actions between different subsystems like the feeder and shooter.
 * 
 * @see Intake
 * @see Hopper
 * @see Elevator
 * @see LED
 * @see Subsystem
 */
public class Superstructure extends Subsystem {

    public enum FISHING_POLE_UPDOWN {
    	UP,
    	DOWN,
    	NONE
    }
    
    public enum FISHING_POLE_EXTEND_RETRACT{
    	EXTEND,
    	RETRACT,
    	NONE
    }
    
    
	static Superstructure mInstance = null;

    public static Superstructure getInstance() {
        if (mInstance == null) {
            mInstance = new Superstructure();
        }
        return mInstance;
    }

    private final Elevator mElevator = Elevator.getInstance();
    private final Climber mClimber = Climber.getInstance();
    private final FishingPole mFishingPole = FishingPole.getInstance();
    private final Intake mIntake = Intake.getInstance();
    private final LED mLED = LED.getInstance();
    private final Solenoid mOverTheTop1 = Constants.makeSolenoidForId(11, Constants.kOverTheTopSolenoid1);
    private final Solenoid mOverTheTop2 = Constants.makeSolenoidForId(11, Constants.kOverTheTopSolenoid2);
    private final Solenoid mFishingPole1 = Constants.makeSolenoidForId(Constants.kFishingPoleSolenoid1);
    private final Solenoid mFishingPole2 = Constants.makeSolenoidForId(Constants.kFishingPoleSolenoid2);
    //private final Solenoid mGrabber1 = Constants.makeSolenoidForId(Constants.kGrabberSolenoid1);
    //private final Solenoid mGrabber2 = Constants.makeSolenoidForId(Constants.kGrabberSolenoid2);
    private final Compressor mCompressor = new Compressor(0);
    private final RevRoboticsAirPressureSensor mAirPressureSensor = new RevRoboticsAirPressureSensor(3);
    
    

    // Superstructure doesn't own the drive, but needs to access it
    private final Drive mDrive = Drive.getInstance();

    // Intenal state of the system
    public enum SystemState {
        IDLE,
        WAITING_FOR_LOW_POSITION,
        WAITING_FOR_HIGH_POSITION,
        WAITING_FOR_POWERCUBE_INTAKE,
        CLIMBINGUP,
        CLIMBINGDOWN,
        CALIBRATINGUP,
        CALIBRATINGDOWN,
        SPITTING,
        WAITING_FOR_ROTATE,
        SPITTING_OUT_TOP, 
        RETURNINGFROMINTAKE,
        RETURNING_HOME,
        ELEVATOR_TRACKING
    };

    // Desired function from user
    public enum WantedState {
        IDLE,
        CLIMBINGUP, 
        CLIMBINGDOWN, 
        INTAKING,
        AUTOINTAKING,
        SPITTING,
        CALIBRATINGDOWN, 
        CALIBRATINGUP,
        OVERTHETOP,
        ELEVATOR_TRACKING
    }

    private SystemState mSystemState = SystemState.IDLE;
    private WantedState mWantedState = WantedState.IDLE;


    private boolean mCompressorOverride = false;
    private double mCurrentStateStartTime;
    private boolean mStateChanged;
    private double mElevatorJoystickPosition = 0;
    //private boolean mIsOverTheTop = false;
    private GRABBER_POSITION mIsOverTheTop = GRABBER_POSITION.FLIP_UN_INIT; // Set to unknown to force it to be set
    private Loop mLoop = new Loop() {

        // Every time we transition states, we update the current state start
        // time and the state changed boolean (for one cycle)
        private double mWantStateChangeStartTime;

        @Override
        public void onStart(double timestamp) {
            synchronized (Superstructure.this) {
                mWantedState = WantedState.IDLE;
                mCurrentStateStartTime = timestamp;
                mWantStateChangeStartTime = timestamp;
                mSystemState = SystemState.IDLE;
                mStateChanged = true;
            }
        }

        @Override
        public void onLoop(double timestamp) {
            synchronized (Superstructure.this) {
                SystemState newState = mSystemState;
                switch (mSystemState) {
                case IDLE:
                    newState = handleIdle(mStateChanged);
                    break;
                case WAITING_FOR_LOW_POSITION:
                    newState = handleWaitingForLowPosition();
                    break;
                case WAITING_FOR_HIGH_POSITION:
                    newState = handleWaitingForHightPosition();
                    break;
                case WAITING_FOR_POWERCUBE_INTAKE:
                    newState = waitingForPowerCubeIntake();
                    break;
                case CLIMBINGUP:
                    newState = handleClimbingUp();
                    break;
                case CLIMBINGDOWN:
                    newState = handleClimbingDown();
                    break;
                case CALIBRATINGUP:
                    newState = handleCalibrationUp();
                    break;
                case CALIBRATINGDOWN:
                    newState = handleCalibrationDown();
                    break;
                case SPITTING:
                    newState = handleSpitting();
                    break;
                case WAITING_FOR_ROTATE:
                    newState = handleWaitingForRotate(timestamp);
                    break;
                case SPITTING_OUT_TOP:
                    newState = handleSpittingOutTop();
                    break;
                case ELEVATOR_TRACKING:
                    newState = handleElevatorTracking();
                    break;
                case RETURNING_HOME:
                    newState = handleReturningHome();
                    break;
                default:
                	
                    newState = SystemState.IDLE;
                }

                if (newState != mSystemState) {
                    System.out.println("Superstructure state " + mSystemState + " to " + newState + " Timestamp: "
                            + Timer.getFPGATimestamp());
                    mSystemState = newState;
                    mCurrentStateStartTime = timestamp;
                    mStateChanged = true;
                } else {
                    mStateChanged = false;
                }
            }
        }

        private SystemState handleElevatorTracking() {
        	mElevator.setWantedPosition(mElevatorJoystickPosition);
        	mElevator.setWantedState(Elevator.WantedState.ELEVATORTRACKING);
            mIntake.setWantedState(Intake.WantedState.IDLE);
            mClimber.setWantedState(Climber.WantedState.IDLE);
        	
            switch (mWantedState) {
            case CLIMBINGUP:
                return SystemState.CLIMBINGUP;
            case CLIMBINGDOWN:
                return SystemState.CLIMBINGDOWN;
            case AUTOINTAKING:
                return SystemState.WAITING_FOR_LOW_POSITION;
            case INTAKING:
                return SystemState.WAITING_FOR_POWERCUBE_INTAKE;
            case SPITTING:
                return SystemState.SPITTING;
            case CALIBRATINGDOWN:
                return SystemState.CALIBRATINGDOWN;
            case CALIBRATINGUP:
                return SystemState.CALIBRATINGUP;
            case OVERTHETOP:
                return SystemState.WAITING_FOR_HIGH_POSITION;
            case ELEVATOR_TRACKING:
                return SystemState.ELEVATOR_TRACKING;
            default:
                return SystemState.IDLE;
            }
        }
        
        private SystemState handleReturningHome() {
        	mElevator.setWantedPosition(0);
        	mElevator.setWantedState(Elevator.WantedState.ELEVATORTRACKING);
            mIntake.setWantedState(Intake.WantedState.IDLE);
           // mClimber.setWantedState(Climber.WantedState.IDLE);
        	
            switch (mWantedState) {
            case CLIMBINGUP:
                return SystemState.CLIMBINGUP;
            case CLIMBINGDOWN:
                return SystemState.CLIMBINGDOWN;
            case AUTOINTAKING:
                return SystemState.RETURNING_HOME;
            case INTAKING:
                return SystemState.WAITING_FOR_POWERCUBE_INTAKE;
            case SPITTING:
                return SystemState.SPITTING;
            case CALIBRATINGDOWN:
                return SystemState.CALIBRATINGDOWN;
            case CALIBRATINGUP:
                return SystemState.CALIBRATINGUP;
            case OVERTHETOP:
                return SystemState.WAITING_FOR_HIGH_POSITION;
            case ELEVATOR_TRACKING:
                return SystemState.ELEVATOR_TRACKING;
            default:
                return SystemState.IDLE;
            }
        }

		private SystemState handleSpittingOutTop() {
        	mElevator.setWantedPosition(1);
        	mElevator.setWantedState(Elevator.WantedState.ELEVATORTRACKING);
        	mIntake.setWantedState(Intake.WantedState.SPITTING);
        	
            switch (mWantedState) {
            case CLIMBINGUP:
                return SystemState.CLIMBINGUP;
            case CLIMBINGDOWN:
                return SystemState.CLIMBINGDOWN;
            case AUTOINTAKING:
                return SystemState.WAITING_FOR_LOW_POSITION;
            case INTAKING:
                return SystemState.WAITING_FOR_POWERCUBE_INTAKE;
            case SPITTING:
                return SystemState.SPITTING;
            case CALIBRATINGDOWN:
                return SystemState.CALIBRATINGDOWN;
            case CALIBRATINGUP:
                return SystemState.CALIBRATINGUP;
            case OVERTHETOP:
                return SystemState.SPITTING_OUT_TOP;
            case ELEVATOR_TRACKING:
                return SystemState.ELEVATOR_TRACKING;
            default:
                return SystemState.IDLE;
            }
        }

		private SystemState handleWaitingForRotate(double timestamp) {
        	mElevator.setWantedPosition(1);
        	mElevator.setWantedState(Elevator.WantedState.ELEVATORTRACKING);
        	mIntake.setIdle();
        	setOverTheTop(GRABBER_POSITION.FLIP_UP);
        
       
            switch (mWantedState) {
            case CLIMBINGUP:
            	setOverTheTop(GRABBER_POSITION.FLIP_DOWN);
                return SystemState.CLIMBINGUP;
            case CLIMBINGDOWN:
                return SystemState.CLIMBINGDOWN;
            case AUTOINTAKING:
                return SystemState.WAITING_FOR_LOW_POSITION;
            case INTAKING:
                return SystemState.WAITING_FOR_POWERCUBE_INTAKE;
            case SPITTING:
                return SystemState.SPITTING;
            case CALIBRATINGDOWN:
                return SystemState.CALIBRATINGDOWN;
            case CALIBRATINGUP:
                return SystemState.CALIBRATINGUP;
            case OVERTHETOP:
                if ((timestamp - mCurrentStateStartTime < Constants.kRotateTime)) {
                	return SystemState.SPITTING_OUT_TOP;
                }
            case ELEVATOR_TRACKING:
                return SystemState.ELEVATOR_TRACKING;
            default:
                return SystemState.IDLE;
            }
        }

		private SystemState handleSpitting() {
        	mElevator.setWantedPosition(mElevatorJoystickPosition);
        	mElevator.setWantedState(Elevator.WantedState.ELEVATORTRACKING);
        	mIntake.setWantedState(Intake.WantedState.SPITTING);
        	
            switch (mWantedState) {
            case CLIMBINGUP:
                return SystemState.CLIMBINGUP;
            case CLIMBINGDOWN:
                return SystemState.CLIMBINGDOWN;
            case AUTOINTAKING:
                return SystemState.WAITING_FOR_LOW_POSITION;
            case INTAKING:
                return SystemState.WAITING_FOR_POWERCUBE_INTAKE;
            case SPITTING:
                return SystemState.SPITTING;
            case CALIBRATINGDOWN:
                return SystemState.CALIBRATINGDOWN;
            case CALIBRATINGUP:
                return SystemState.CALIBRATINGUP;
            case OVERTHETOP:
                return SystemState.SPITTING_OUT_TOP;
            case ELEVATOR_TRACKING:
                return SystemState.ELEVATOR_TRACKING;
            default:
                return SystemState.IDLE;
            }
        }

		private SystemState handleCalibrationDown() {
        	mElevator.setWantedState(Elevator.WantedState.CALIBRATINGDOWN);
        	
            switch (mWantedState) {
            case CLIMBINGUP:
                return SystemState.CLIMBINGUP;
            case CLIMBINGDOWN:
                return SystemState.CLIMBINGDOWN;
            case AUTOINTAKING:
                return SystemState.WAITING_FOR_LOW_POSITION;
            case INTAKING:
                return SystemState.WAITING_FOR_POWERCUBE_INTAKE;
            case SPITTING:
                return SystemState.SPITTING;
            case CALIBRATINGDOWN:
                return SystemState.CALIBRATINGDOWN;
            case CALIBRATINGUP:
                return SystemState.CALIBRATINGUP;
            case OVERTHETOP:
                return SystemState.SPITTING_OUT_TOP;
            case ELEVATOR_TRACKING:
                return SystemState.ELEVATOR_TRACKING;
            default:
                return SystemState.IDLE;
            }
        }

		private SystemState handleCalibrationUp() {
        	mElevator.setWantedState(Elevator.WantedState.CALIBRATINGUP);
        	
            switch (mWantedState) {
            case CLIMBINGUP:
                return SystemState.CLIMBINGUP;
            case CLIMBINGDOWN:
                return SystemState.CLIMBINGDOWN;
            case AUTOINTAKING:
                return SystemState.WAITING_FOR_LOW_POSITION;
            case INTAKING:
                return SystemState.WAITING_FOR_POWERCUBE_INTAKE;
            case SPITTING:
                return SystemState.SPITTING;
            case CALIBRATINGDOWN:
                return SystemState.CALIBRATINGDOWN;
            case CALIBRATINGUP:
                return SystemState.CALIBRATINGUP;
            case OVERTHETOP:
                return SystemState.SPITTING_OUT_TOP;
            case ELEVATOR_TRACKING:
                return SystemState.ELEVATOR_TRACKING;
            default:
                return SystemState.IDLE;
            }
        }
		private SystemState handleClimbingDown() {
            mClimber.setWantedState(Climber.WantedState.MECHANISM_DOWN);

            switch (mWantedState) {
            case CLIMBINGUP:
                return SystemState.CLIMBINGUP;
            case CLIMBINGDOWN:
                return SystemState.CLIMBINGDOWN;
            case AUTOINTAKING:
                return SystemState.WAITING_FOR_LOW_POSITION;
            case INTAKING:
                return SystemState.WAITING_FOR_POWERCUBE_INTAKE;
            case SPITTING:
                return SystemState.SPITTING;
            case CALIBRATINGDOWN:
                return SystemState.CALIBRATINGDOWN;
            case CALIBRATINGUP:
                return SystemState.CALIBRATINGUP;
            case OVERTHETOP:
                return SystemState.SPITTING_OUT_TOP;
            case ELEVATOR_TRACKING:
                return SystemState.ELEVATOR_TRACKING;
            default:
                return SystemState.IDLE;
            }
        }

		private SystemState handleClimbingUp() {
            mClimber.setWantedState(Climber.WantedState.MECHANISM_UP);

            switch (mWantedState) {
            case CLIMBINGUP:
                return SystemState.CLIMBINGUP;
            case CLIMBINGDOWN:
                return SystemState.CLIMBINGDOWN;
            case AUTOINTAKING:
                return SystemState.WAITING_FOR_LOW_POSITION;
            case INTAKING:
                return SystemState.WAITING_FOR_POWERCUBE_INTAKE;
            case SPITTING:
                return SystemState.SPITTING;
            case CALIBRATINGDOWN:
                return SystemState.CALIBRATINGDOWN;
            case CALIBRATINGUP:
                return SystemState.CALIBRATINGUP;
            case OVERTHETOP:
                return SystemState.SPITTING_OUT_TOP;
            case ELEVATOR_TRACKING:
                return SystemState.ELEVATOR_TRACKING;
            default:
                return SystemState.IDLE;
            }
		}

		private SystemState waitingForPowerCubeIntake() {

       	mIntake.setWantedState(Intake.WantedState.INTAKING);
        	
            switch (mWantedState) {
            case CLIMBINGUP:
                return SystemState.CLIMBINGUP;
            case CLIMBINGDOWN:
                return SystemState.CLIMBINGDOWN;
            case AUTOINTAKING:
            	if (mIntake.gotCube()) {
            		return SystemState.RETURNING_HOME;
            	}
                return SystemState.WAITING_FOR_POWERCUBE_INTAKE;
            case INTAKING:
                return SystemState.WAITING_FOR_POWERCUBE_INTAKE;
            case SPITTING:
                return SystemState.SPITTING;
            case CALIBRATINGDOWN:
                return SystemState.CALIBRATINGDOWN;
            case CALIBRATINGUP:
                return SystemState.CALIBRATINGUP;
            case OVERTHETOP:
                return SystemState.SPITTING_OUT_TOP;
            case ELEVATOR_TRACKING:
                return SystemState.ELEVATOR_TRACKING;
            default:
                return SystemState.IDLE;
            }
        }

		private SystemState handleWaitingForHightPosition() {
			// TODO Auto-generated method stub
			return SystemState.IDLE;
		}

		private SystemState handleWaitingForLowPosition() {
        	mElevator.setWantedPosition(-1);
        	mElevator.setWantedState(Elevator.WantedState.ELEVATORTRACKING);
        	mIntake.setWantedState(Intake.WantedState.INTAKING);
        	
            switch (mWantedState) {
            case CLIMBINGUP:
                return SystemState.CLIMBINGUP;
            case CLIMBINGDOWN:
                return SystemState.CLIMBINGDOWN;
            case AUTOINTAKING:{
            	if (mElevator.atBottom())
            		return SystemState.WAITING_FOR_POWERCUBE_INTAKE; 
            	else  
                return SystemState.WAITING_FOR_LOW_POSITION;
            	}
            	
            case INTAKING:
                return SystemState.WAITING_FOR_POWERCUBE_INTAKE;
            case SPITTING:
                return SystemState.SPITTING;
            case CALIBRATINGDOWN:
                return SystemState.CALIBRATINGDOWN;
            case CALIBRATINGUP:
                return SystemState.CALIBRATINGUP;
            case OVERTHETOP:
                return SystemState.SPITTING_OUT_TOP;
            case ELEVATOR_TRACKING:
                return SystemState.ELEVATOR_TRACKING;
            default:
                return SystemState.IDLE;
            }
        }

		@Override
        public void onStop(double timestamp) {
            stop();
        }
    };


    private SystemState handleIdle(boolean stateChanged) {
        if (stateChanged) {
            stop();
            mLED.setWantedState(LED.WantedState.OFF);
            mElevator.setWantedState(Elevator.WantedState.IDLE);
            mIntake.setWantedState(Intake.WantedState.IDLE);
         //   mClimber.setWantedState(Climber.WantedState.IDLE);
        }
        
    	
        switch (mWantedState) {
        case CLIMBINGUP:
            return SystemState.CLIMBINGUP;
        case CLIMBINGDOWN:
            return SystemState.CLIMBINGDOWN;
        case AUTOINTAKING:
            return SystemState.WAITING_FOR_LOW_POSITION;
        case INTAKING:
            return SystemState.WAITING_FOR_POWERCUBE_INTAKE;      
        case SPITTING:
            return SystemState.SPITTING;
        case CALIBRATINGDOWN:
            return SystemState.CALIBRATINGDOWN;
        case CALIBRATINGUP:
            return SystemState.CALIBRATINGUP;
        case OVERTHETOP:
            return SystemState.WAITING_FOR_HIGH_POSITION;
        case ELEVATOR_TRACKING:
            return SystemState.ELEVATOR_TRACKING;
        default:
            return SystemState.IDLE;
        }
    }


    public synchronized void setWantedState(WantedState wantedState) {
        mWantedState = wantedState;
    }

   // public synchronized void setGrabber(boolean grab) {
   //     mGrabber1.set(grab);
  //      mGrabber2.set(!grab);
  //  }

    public synchronized void setOverTheTop(GRABBER_POSITION wantsOverTheTop) {
        if (wantsOverTheTop != mIsOverTheTop) {
            mIsOverTheTop = wantsOverTheTop;
            //mOverTheTop1.set(!wantsOverTheTop);
            //mOverTheTop2.set(wantsOverTheTop);
//
            switch (mIsOverTheTop) {
                case FLIP_UP:
                	//System.out.println("flip up");
                    mOverTheTop1.set(false);
                    mOverTheTop2.set(true);
                    break;
                case FLIP_DOWN:
                	//System.out.println("flip down");
                    mOverTheTop1.set(true);
                    mOverTheTop2.set(false);
                    break;
                default: // Constants.kElevatorFlipNone
                	//System.out.println("flip default");
                    mOverTheTop1.set(false);
                    mOverTheTop2.set(false);
            }
//
        }
    }
    

    @Override
    public void outputToSmartDashboard() {
        SmartDashboard.putNumber("Air Pressure psi", mAirPressureSensor.getAirPressurePsi());
    }

    @Override
    public void stop() {

    }

    @Override
    public void zeroSensors() {

    }

    @Override
    public void registerEnabledLoops(Looper enabledLooper) {
        enabledLooper.register(mLoop);
    }

    public void setWantedElevatorPosition(double position) {
        mElevatorJoystickPosition = position;
    }

    public void setOverrideCompressor(boolean force_off) {
        mCompressorOverride = force_off;
    }

    public void reloadConstants() {
 //       mShooter.refreshControllerConsts();
    }


	public void setFishingPoleUpdown(FISHING_POLE_UPDOWN updown) {
		// TODO Auto-generated method stub
        switch (updown) {
        case DOWN:
            mFishingPole1.set(false);
            mFishingPole2.set(true);
            break;
        case UP:
            mFishingPole1.set(true);
            mFishingPole2.set(false);
            break;
        default: // Constants.kElevatorFlipNone
            mFishingPole1.set(false);
            mFishingPole2.set(false);
        }
	}



	public void setFishingPoleExtendRetract(FISHING_POLE_EXTEND_RETRACT extendRetract) {
		if (extendRetract == FISHING_POLE_EXTEND_RETRACT.EXTEND) {
			mFishingPole.setWantedState(FishingPole.WantedState.MECHANISM_UP);
		} else if (extendRetract == FISHING_POLE_EXTEND_RETRACT.RETRACT)
			mFishingPole.setWantedState(FishingPole.WantedState.MECHANISM_DOWN);	
		else {
			mFishingPole.setWantedState(FishingPole.WantedState.IDLE);
		}
		
	}
}
