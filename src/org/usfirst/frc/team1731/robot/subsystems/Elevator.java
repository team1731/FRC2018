package org.usfirst.frc.team1731.robot.subsystems;

import java.util.Arrays;

import org.usfirst.frc.team1731.lib.util.Util;
import org.usfirst.frc.team1731.lib.util.drivers.TalonSRXFactory;
import org.usfirst.frc.team1731.robot.Constants;
import org.usfirst.frc.team1731.robot.loops.Loop;
import org.usfirst.frc.team1731.robot.loops.Looper;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;

//import com.ctre.PigeonImu.StatusFrameRate;

// com.ctre.PigeonImu.StatusFrameRate;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.ParamEnum;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;


//import com.ctre.phoenix.motorcontrol.StatusFrameRate;
//import com.ctre.phoenix.motorcontrol.VelocityMeasWindow;

import edu.wpi.first.wpilibj.Timer;
//import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 * 1731 this system controls the elevator
 * 
 * @see Subsystem.java
 */

//stemrobotics.cs.pdx.edu/sites/default/files/WPILib_programming.pdf

@SuppressWarnings("unused")
public class Elevator extends Subsystem {
	//private Joystick joystick2;

    private static final double kTopEncoderValue = 6000;
    private static final double kBottomEncoderValue= 5300;
    private static final double kHomeEncoderValue = 0;

	
    private static Elevator sInstance = null;
    
    public static Elevator getInstance() {
        if (sInstance == null) {
            sInstance = new Elevator();
        }
        return sInstance;
    }

    private final TalonSRX mTalon; 
    //private final VictorSP mVictor;
    
    public Elevator() {
        mTalon = new TalonSRX(Constants.kElevatorTalon);
        mTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
        mTalon.set(ControlMode.Position, 0);
        mTalon.configVelocityMeasurementWindow(10, 0);
        mTalon.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_5Ms, 0);
        mTalon.selectProfileSlot(0, 0);
        mTalon.config_kP(Constants.SlotIdx, Constants.kElevatorTalonKP, Constants.kTimeoutMs );
        mTalon.config_kI(Constants.SlotIdx, Constants.kElevatorTalonKI, Constants.kTimeoutMs );
        mTalon.config_kD(Constants.SlotIdx, Constants.kElevatorTalonKD, Constants.kTimeoutMs);
        mTalon.config_kF(Constants.SlotIdx, Constants.kElevatorTalonKF, Constants.kTimeoutMs );
        mTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_12_Feedback1, 1000, 1000);
        mTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
        mTalon.setSelectedSensorPosition(0, 0, 10);
        mTalon.overrideLimitSwitchesEnable(false);
        
        /* choose to ensure sensor is positive when output is positive */
        mTalon.setSensorPhase(Constants.kSensorPhase);

        /* choose based on what direction you want forward/positive to be.
         * This does not affect sensor phase. */ 
        mTalon.setInverted(true); //Constants.kMotorInvert);

        /* set the peak and nominal outputs, 12V means full */
        mTalon.configNominalOutputForward(.2, Constants.kTimeoutMs);
        mTalon.configNominalOutputReverse(.2, Constants.kTimeoutMs);
        mTalon.configPeakOutputForward(1, Constants.kTimeoutMs);
        mTalon.configPeakOutputReverse(-0.5, Constants.kTimeoutMs);
        /*
         * set the allowable closed-loop error, Closed-Loop output will be
         * neutral within this range. See Table in Section 17.2.1 for native
         * units per rotation.
         */
        mTalon.configAllowableClosedloopError(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);

    }
    	
    public enum SystemState {	
        IDLE,   // stop all motors
        CALIBRATINGUP,
        CALIBRATINGDOWN,
        ELEVATORTRACKING, // moving
    }

    public enum WantedState {
    	IDLE,   
        ELEVATORTRACKING, // moving
        CALIBRATINGUP,
        CALIBRATINGDOWN,
    }

    private SystemState mSystemState = SystemState.IDLE;
    private WantedState mWantedState = WantedState.IDLE;

    private double mCurrentStateStartTime;
    private double mWantedPosition = 0;
    private boolean mStateChanged = false;

    private Loop mLoop = new Loop() {
        @Override
        public void onStart(double timestamp) {
            stop();
            synchronized (Elevator.this) {
                mSystemState = SystemState.IDLE;
                mStateChanged = true;
                mWantedPosition = 0;
                mCurrentStateStartTime = timestamp;
                mTalon.setSelectedSensorPosition(0, 0, 10);                
              //  DriverStation.reportError("Elevator SystemState: " + mSystemState, false);
            }
        }

        @Override
        public void onLoop(double timestamp) {
   	
        	synchronized (Elevator.this) {
                SystemState newState;
                switch (mSystemState) {
                    case IDLE:
                        newState = handleIdle();
                        break;
                    case ELEVATORTRACKING:
                        newState = handleElevatorTracking();
                        break;
                    case CALIBRATINGUP:
                        newState = handleCalibratingUp();
                        break;
                    case CALIBRATINGDOWN:
                        newState = handleCalibratingDown();
                        break;
                    default:
                        newState = SystemState.IDLE;                    
                }

                if (newState != mSystemState) {
                    //System.out.println("Elevator state " + mSystemState + " to " + newState);
                    mSystemState = newState;
                    mCurrentStateStartTime = timestamp;
                    DriverStation.reportWarning("Elevator SystemState: " + mSystemState, false);
                    mStateChanged = true;
                } else {
                    mStateChanged = false;
                }
            }
        }
        
        private SystemState handleCalibratingDown() {
            if (mStateChanged) {
                mTalon.set(ControlMode.PercentOutput, -0.3);
            }
    		mTalon.setSelectedSensorPosition(0, 0, 0);
    		return defaultStateTransfer();
		}

		private SystemState handleCalibratingUp() {
            if (mStateChanged) {
                mTalon.set(ControlMode.PercentOutput, 0.5);
            }
    		mTalon.setSelectedSensorPosition(0, 0, 0);
    		return defaultStateTransfer();
		}

		@Override
        public void onStop(double timestamp) {
            stop();
        }
    };

    private SystemState defaultStateTransfer() {
        switch (mWantedState) {
            case ELEVATORTRACKING:
                return SystemState.ELEVATORTRACKING;
            case CALIBRATINGUP:
                return SystemState.CALIBRATINGUP;
            case CALIBRATINGDOWN:
                return SystemState.CALIBRATINGDOWN;

            default:
                return SystemState.IDLE;
        }
    }
    
    private SystemState handleIdle() {
        //setOpenLoop(0.0f);
        //if motor is not off, turn motor off
        if (mStateChanged) {
            mTalon.set(ControlMode.PercentOutput, 0);
        }
		return defaultStateTransfer();
    }

    private SystemState handleElevatorTracking() {

    	if (mWantedPosition > 0) {
    		mTalon.set(ControlMode.Position, (int)(mWantedPosition*kTopEncoderValue)); 
    	}
    	else {
    		mTalon.set(ControlMode.Position, (int)(mWantedPosition*kBottomEncoderValue));
    	} 

    	return defaultStateTransfer();
    }

    public synchronized void setWantedPosition(double position) {
    	
    	mWantedPosition = position;
    	

    }


    public synchronized void setWantedState(WantedState state) {
        if (state != mWantedState) {
            mWantedState = state;
            DriverStation.reportError("Elevator WantedState: " + mWantedState, false);
        }
    }

    @Override
    public void outputToSmartDashboard() {
        SmartDashboard.putNumber("ElevWantPos", mWantedPosition);
        SmartDashboard.putNumber("ElevCurPos", mTalon.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("ElevQuadPos", mTalon.getSensorCollection().getQuadraturePosition());
        SmartDashboard.putBoolean("ElevRevSw", mTalon.getSensorCollection().isRevLimitSwitchClosed());
    }

    @Override
    public void stop() {
        // mVictor.set(0);
        setWantedState(WantedState.IDLE);
    }

    @Override
    public void zeroSensors() {
    }

    @Override
    public void registerEnabledLoops(Looper in) {
        in.register(mLoop);
    }

    public boolean checkSystem() {
        System.out.println("Testing FEEDER.-----------------------------------");
        return false;
    }
}
