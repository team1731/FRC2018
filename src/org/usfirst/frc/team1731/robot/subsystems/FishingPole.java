package org.usfirst.frc.team1731.robot.subsystems;

import java.util.Arrays;

import org.usfirst.frc.team1731.lib.util.Util;
import org.usfirst.frc.team1731.lib.util.drivers.TalonSRXFactory;
import org.usfirst.frc.team1731.robot.Constants;
import org.usfirst.frc.team1731.robot.loops.Loop;
import org.usfirst.frc.team1731.robot.loops.Looper;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;

/**
 * 
 * 1731 the climber enable the endgame climb.
 * 
 * @see Subsystem.java
 */
@SuppressWarnings("unused")
public class FishingPole extends Subsystem {

    
    private static FishingPole sInstance = null;
    private DigitalInput mDigitalSensorDown;
    private DigitalInput mDigitalSensorUp;
    
    private boolean fishingPoleLocked;
    
    public static FishingPole getInstance() {
        if (sInstance == null) {
            sInstance = new FishingPole();
        }
        return sInstance;
    }

    private final TalonSRX mTalon;

    public FishingPole() {
    
        mTalon = TalonSRXFactory.createDefaultTalon(Constants.kFishingPoleId);
        mTalon.setInverted(false);
        mTalon.setNeutralMode(NeutralMode.Brake);
        mTalon.enableCurrentLimit(true);
    	mDigitalSensorDown = new DigitalInput(1);
    	mDigitalSensorUp = new DigitalInput(2);
    }
    
    public enum SystemState {
	        GOING_UP, // used for unjamming fuel
	        GOING_DOWN, // stop all motors
	        IDLE // run feeder in reverse
    }

    public enum WantedState {
        IDLE,
        MECHANISM_UP,
        MECHANISM_DOWN
    }

    private SystemState mSystemState = SystemState.IDLE;
    private WantedState mWantedState = WantedState.IDLE;

    private double mCurrentStateStartTime;
    private boolean mStateChanged = false;

    private Loop mLoop = new Loop() {
        @Override
        public void onStart(double timestamp) {
            stop();
            synchronized (FishingPole.this) {
                mSystemState = SystemState.IDLE;
                mStateChanged = true;
                mCurrentStateStartTime = timestamp;
                fishingPoleLocked = false;
                mTalon.setSelectedSensorPosition(0, 0, 10);
                mTalon.configContinuousCurrentLimit(Constants.kFishingPoleContinuousCurrentLimitAmps, Constants.kTimeoutMs);
                mTalon.configPeakCurrentLimit(Constants.kFishingPolePeakCurrentLimitAmps, Constants.kTimeoutMs);
                mTalon.configPeakCurrentDuration(Constants.kFishingPolePeakCurrentDurationMs, Constants.kTimeoutMs);
            }
        }

        @Override
        public void onLoop(double timestamp) {
            synchronized (FishingPole.this) {
                SystemState newState;
                switch (mSystemState) {
                case IDLE:
                    newState = handleIdle();
                    break;
                case GOING_UP:
                    newState = handleGoingUp();
                    break;
                case GOING_DOWN:
                    newState = handleGoingDown();
                    break;
                default:
                    newState = SystemState.IDLE;
                }
                if (newState != mSystemState) {
                    mSystemState = newState;
                    mCurrentStateStartTime = timestamp;
                    //DriverStation.reportWarning("Climber SystemState: " + mSystemState, false);
                    mStateChanged = true;
                } else {
                    mStateChanged = false;
                }
            }
        }

        private SystemState handleGoingDown() {
        	if (mDigitalSensorDown.get() && !fishingPoleLocked) {
        		mTalon.set(ControlMode.PercentOutput, 1.0);
        	} else {
        		mTalon.set(ControlMode.PercentOutput, 0);
                fishingPoleLocked = true;
        	}

            return defaultStateTransfer();
        }

        private SystemState handleGoingUp() {
        	if (mDigitalSensorUp.get() && !fishingPoleLocked){
        		mTalon.set(ControlMode.PercentOutput, -1.0);
        	}else {
            	mTalon.set(ControlMode.PercentOutput, 0);
                fishingPoleLocked = true;
        	}
            
            return defaultStateTransfer();
        }

        @Override
        public void onStop(double timestamp) {
            stop();
        }
    };

    private SystemState defaultStateTransfer() {
        switch (mWantedState) {
        case MECHANISM_DOWN:
            return SystemState.GOING_DOWN;
        case MECHANISM_UP:
            return SystemState.GOING_UP;
        default:
            return SystemState.IDLE;
        }
        
    }

    private SystemState handleIdle() {
        mTalon.set(ControlMode.PercentOutput, 0);
        fishingPoleLocked = false;
        return defaultStateTransfer();
    }

    public synchronized void setWantedState(WantedState state) {
        if (state != mWantedState) {
            mWantedState = state;
            DriverStation.reportError("fishingpole WantedState: " + mWantedState, false);
        }
    }

    @Override
    public void outputToSmartDashboard() {
        SmartDashboard.putBoolean("DigitalInput1", mDigitalSensorDown.get());
        SmartDashboard.putBoolean("DigitalInput2", mDigitalSensorUp.get());
   //     SmartDashboard.putNumber("Fishingpole mWantedState", mWantedState);
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
        System.out.println("Testing FishingPole.-----------------------------------");
 /*       final double kCurrentThres = 0.5;
        final double kRpmThes = 2000.0;

        mSlaveTalon.changeControlMode(TalonControlMode.Voltage);
        mTalon.changeControlMode(TalonControlMode.Voltage);

        mSlaveTalon.set(0.0);
        mTalon.set(0.0);

        mTalon.set(6.0f);
        Timer.delay(4.0);
        final double currentMaster = mTalon.getOutputCurrent();
        final double rpmMaster = mTalon.getSpeed();
        mTalon.set(0.0f);

        Timer.delay(2.0);

        mSlaveTalon.set(-6.0f);
        Timer.delay(4.0);
        final double currentSlave = mSlaveTalon.getOutputCurrent();
        final double rpmSlave = mTalon.getSpeed();
        mSlaveTalon.set(0.0f);

        mSlaveTalon.changeControlMode(TalonControlMode.Follower);
        mSlaveTalon.set(Constants.kFeederMasterId);

        System.out.println("Feeder Master Current: " + currentMaster + " Slave Current: " + currentSlave
                + " rpmMaster: " + rpmMaster + " rpmSlave: " + rpmSlave);

        boolean failure = false;

        if (currentMaster < kCurrentThres) {
            failure = true;
            System.out.println("!!!!!!!!!!!!!! Feeder Master Current Low !!!!!!!!!!!!!!!!");
        }

        if (currentSlave < kCurrentThres) {
            failure = true;
            System.out.println("!!!!!!!!!!!!!! Feeder Slave Current Low !!!!!!!!!!!!!!!!!");
        }

        if (!Util.allCloseTo(Arrays.asList(currentMaster, currentSlave), currentMaster, 5.0)) {
            failure = true;
            System.out.println("!!!!!!!!!!!!!!! Feeder currents different!!!!!!!!!!!!!!!");
        }

        if (rpmMaster < kRpmThes) {
            failure = true;
            System.out.println("!!!!!!!!!!!!!! Feeder Master RPM Low !!!!!!!!!!!!!!!!!!!!!!!!!");
        }

        if (rpmSlave < kRpmThes) {
            failure = true;
            System.out.println("!!!!!!!!!!!!!! Feeder Slave RPM Low !!!!!!!!!!!!!!!!!!!!!!!!!");
        }

        if (!Util.allCloseTo(Arrays.asList(rpmMaster, rpmSlave), rpmMaster, 250)) {
            failure = true;
            System.out.println("!!!!!!!!!!!!!! Feeder RPM different !!!!!!!!!!!!!!!!!!!!!!!!!");
        }
*/
//        return !failure;
        return true;
    }

}
