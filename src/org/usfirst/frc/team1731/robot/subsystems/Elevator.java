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

    private static final double kReversing = -1.0;
    private static final double kUnjamInPeriod = .2 * kReversing;
    private static final double kUnjamOutPeriod = .4 * kReversing;
    private static final double kUnjamInPower = 6.0 * kReversing / 12.0;
    private static final double kUnjamOutPower = -6.0 * kReversing / 12.0;
    private static final double kFeedVoltage = 10.0;
    private static final double kExhaustVoltage = kFeedVoltage * kReversing / 12.0;
	
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
        mTalon.setSelectedSensorPosition(0, 0, 10);
        
        /* choose to ensure sensor is positive when output is positive */
        mTalon.setSensorPhase(Constants.kSensorPhase);

        /* choose based on what direction you want forward/positive to be.
         * This does not affect sensor phase. */ 
        mTalon.setInverted(true); //Constants.kMotorInvert);

        /* set the peak and nominal outputs, 12V means full */
        mTalon.configNominalOutputForward(0, Constants.kTimeoutMs);
        mTalon.configNominalOutputReverse(0, Constants.kTimeoutMs);
        mTalon.configPeakOutputForward(1, Constants.kTimeoutMs);
        mTalon.configPeakOutputReverse(-1, Constants.kTimeoutMs);
        /*
         * set the allowable closed-loop error, Closed-Loop output will be
         * neutral within this range. See Table in Section 17.2.1 for native
         * units per rotation.
         */
        mTalon.configAllowableClosedloopError(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);

    }
    	
    public enum SystemState {	
        IDLE,   // stop all motors
        MOVING, // moving
    }

    public enum WantedState {
    		IDLE,   
        MOVING, // moving
        STOP,
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
                DriverStation.reportError("Elevator SystemState: " + mSystemState, false);
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
                    case MOVING:
                        newState = handleMoving();
                        break;
                    default:
                        newState = SystemState.IDLE;                    
                }

                if (newState != mSystemState) {
                    //System.out.println("Elevator state " + mSystemState + " to " + newState);
                    mSystemState = newState;
                    mCurrentStateStartTime = timestamp;
                    DriverStation.reportError("Elevator SystemState: " + mSystemState, false);
                    mStateChanged = true;
                } else {
                    mStateChanged = false;
                }
            }
        }
        
        @Override
        public void onStop(double timestamp) {
            stop();
        }
    };

    private SystemState defaultStateTransfer() {
        switch (mWantedState) {
            case MOVING:
                return SystemState.MOVING;
            /*case STOP:
                return SystemState.IDLE; */
            default:
                return SystemState.IDLE;
        }
    }
    
    private SystemState handleIdle() {
        //setOpenLoop(0.0f);
        //if motor is not off, turn motor off
        if (mStateChanged) {
            mTalon.set(ControlMode.Position, 0);
        }
		return defaultStateTransfer();
    }

    private SystemState handleMoving() {
        /* 10 Rotations * 4096 u/rev in either direction */
        //targetPositionRotations = mWantedPosition * 4096;
        DriverStation.reportError("Elevator SetPosition: " + Double.toString(mWantedPosition), false);
        mTalon.set(ControlMode.Position, mWantedPosition); // * 4096);

        return defaultStateTransfer();
    }

    public synchronized void setWantedPosition(double position) {
        if (position != mWantedPosition) {
            mWantedPosition = position;
            mWantedState = WantedState.MOVING;
        } else if (position == 0) {
        	mWantedState = WantedState.IDLE;
        }
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
