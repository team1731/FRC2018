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
import com.ctre.PigeonImu.StatusFrameRate;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;


//import com.ctre.phoenix.motorcontrol.StatusFrameRate;
//import com.ctre.phoenix.motorcontrol.VelocityMeasWindow;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**

 * 
 * 1731 this system controls the elevator
 * 
 * @see Subsystem.java
 */
@SuppressWarnings("unused")
public class Elevator extends Subsystem {
	private Joystick joystick2;
	
    private static Elevator sInstance = null;
    
    public static Elevator getInstance() {
        if (sInstance == null) {
            sInstance = new Elevator();
        }
        return sInstance;
    }

    //private final TalonSRX mTalon; 
    private final VictorSP mVictor;
    
    public Elevator() {
    		mVictor = new VictorSP(0);
    	}
    	
    	public enum SystemState {	
        IDLE, // stop all motors
        MOVING_UP,//Run elevator up
        MOVING_DOWN,//Run elevator in reverse
    }

    public enum WantedState {
    		IDLE,   
    		STOP,
    		MOVE_UP,//Run elevator up
        MOVE_DOWN,//Run elevator in reverse
    }

    private SystemState mSystemState = SystemState.IDLE;
    private WantedState mWantedState = WantedState.IDLE;

    private double mCurrentStateStartTime;
    private boolean mStateChanged;

    private Loop mLoop = new Loop() {
        @Override
        public void onStart(double timestamp) {
            stop();
            synchronized (Elevator.this) {
                mSystemState = SystemState.IDLE;
                mStateChanged = true;
                mCurrentStateStartTime = timestamp;
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
                case MOVING_UP:
                    newState = handleMovingUp();
                    break;
                case MOVING_DOWN:
                    newState = handleMovingDown();
                    break;
                default:
                    newState = SystemState.IDLE;
                    
                }
                if (newState != mSystemState) {
                    System.out.println("Elevator state " + mSystemState + " to " + newState);
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
        case MOVE_UP:
            return SystemState.MOVING_UP;
        case MOVE_DOWN:
            return SystemState.MOVING_DOWN;
        /*case STOP:
            return SystemState.IDLE; */
        default:
            return SystemState.IDLE;
        }
    }
    
    private SystemState handleIdle() {
        //setOpenLoop(0.0f);
        //if motor is not off, turn motor off
		return defaultStateTransfer();
    }
    private SystemState handleMovingUp() {
    		if (mStateChanged) {
    			mVictor.set(0.25);
    			//turn motor forward	
    		}
        return defaultStateTransfer();
    }
    private SystemState handleMovingDown() {
    		if (mStateChanged) {
    			mVictor.set(-0.25);
    			//turn motor in reverse
    		}
        return defaultStateTransfer();
   }
 
 
    private SystemState handleFeeding() {
        if (mStateChanged) {
        	mVictor.set(1.0);
        }
        return defaultStateTransfer();
    }

    public synchronized void setWantedState(WantedState state) {
        mWantedState = state;
        DriverStation.reportError("Elevator WantedState: " + mWantedState, false);
    }

    private void setOpenLoop(double voltage) {
    		mVictor.set(voltage);
    }

    @Override
    public void outputToSmartDashboard() {
        // SmartDashboard.putNumber("feeder_speed", mMasterTalon.get() / Constants.kFeederSensorGearReduction);
    }

    @Override
    public void stop() {
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
