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
	private Talon Elevator;
	
	
	/*    private static final double kReversing = -1.0;
    private static final double kUnjamInPeriod = .2 * kReversing;
    private static final double kUnjamOutPeriod = .4 * kReversing;
    private static final double kUnjamInPower = 6.0 * kReversing / 12.0;
    private static final double kUnjamOutPower = -6.0 * kReversing / 12.0;
    private static final double kFeedVoltage = 10.0;
    private static final double kExhaustVoltage = kFeedVoltage * kReversing / 12.0;
*/
    private static Elevator sInstance = null;
    
    public static Elevator getInstance() {
        if (sInstance == null) {
            sInstance = new Elevator();
        }
        return sInstance;
    }

    private final TalonSRX mTalon; 
//    private final CANTalon mMasterTalon, mSlaveTalon;

    
    public void robotInit() {
    	Elevator = new Talon(7);
   
    }
    
    public void teleopPeriodic() {
    	
    }
    
    	public Elevator() {
    	mTalon = new TalonSRX(Constants.kElevatorTalon);
    	mTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
    	mTalon.set(ControlMode.PercentOutput, 0);
    	mTalon.configVelocityMeasurementWindow(10, 0);
    	mTalon.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_5Ms, 0);
    	mTalon.config_kP(Constants.SlotIdx, Constants.kElevatorTalonKP, Constants.kTimeoutMs );
    	mTalon.config_kI(Constants.SlotIdx, Constants.kElevatorTalonKI, Constants.kTimeoutMs );
    mTalon.config_kD(Constants.SlotIdx, Constants.kElevatorTalonKD, Constants.kTimeoutMs);
 	mTalon.config_kF(Constants.SlotIdx, Constants.kElevatorTalonKF, Constants.kTimeoutMs );
 	mTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_12_Feedback1, 1000, 1000);
 	
    	
    	
    	
    	
    	
/*      mMasterTalon = CANTalonFactory.createDefaultTalon(Constants.kFeederMasterId);
        mMasterTalon.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
        mMasterTalon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        mMasterTalon.SetVelocityMeasurementWindow(16);
        mMasterTalon.SetVelocityMeasurementPeriod(CANTalon.VelocityMeasurementPeriod.Period_5Ms);

        mMasterTalon.setVoltageRampRate(Constants.kFeederRampRate);
        mMasterTalon.reverseOutput(false);
        mMasterTalon.enableBrakeMode(true);

        mMasterTalon.setP(Constants.kFeederKP);
        mMasterTalon.setI(Constants.kFeederKI);
        mMasterTalon.setD(Constants.kFeederKD);
        mMasterTalon.setF(Constants.kFeederKF);
        mMasterTalon.setVoltageCompensationRampRate(Constants.kFeederVoltageCompensationRampRate);
        mMasterTalon.setNominalClosedLoopVoltage(12.0);

        mMasterTalon.setStatusFrameRateMs(CANTalon.StatusFrameRate.Feedback, 1000);

        mSlaveTalon = CANTalonFactory.createPermanentSlaveTalon(Constants.kFeederSlaveId, Constants.kFeederMasterId);
        mSlaveTalon.reverseOutput(true);
        mSlaveTalon.enableBrakeMode(true);
        */
    }

    public enum SystemState {	
        IDLE, // stop all motors
        EXHAUSTING // run elevator in reverse?
    }

    public enum WantedState {
    		IDLE,   
    		STOP,
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
                case UNJAMMING_OUT:
                    newState = handleUnjammingOut(timestamp, mCurrentStateStartTime);
                    break;
                case UNJAMMING_IN:
                    newState = handleUnjammingIn(timestamp, mCurrentStateStartTime);
                    break;
                case FEEDING:
                    newState = handleFeeding();
                    break;
                case EXHAUSTING:
                    newState = handleExhaust();
                    break;
                default:
                    newState = SystemState.IDLE;
                }
                if (newState != mSystemState) {
                    System.out.println("Feeder state " + mSystemState + " to " + newState);
                    mSystemState = newState;
                    mCurrentStateStartTime = timestamp;
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
        case FEED:
            return SystemState.FEEDING;
        case UNJAM:
            return SystemState.UNJAMMING_OUT;
        case EXHAUST:
            return SystemState.EXHAUSTING;
        default:
            return SystemState.IDLE;
        }
    }

    private SystemState handleIdle() {
        setOpenLoop(0.0f);
        return defaultStateTransfer();
    }

    private SystemState handleUnjammingOut(double now, double startStartedAt) {
        setOpenLoop(kUnjamOutPower);
        SystemState newState = SystemState.UNJAMMING_OUT;
        if (now - startStartedAt > kUnjamOutPeriod) {
            newState = SystemState.UNJAMMING_IN;
        }
        switch (mWantedState) {
        case FEED:
            return SystemState.FEEDING;
        case UNJAM:
            return newState;
        case EXHAUST:
            return SystemState.EXHAUSTING;
        default:
            return SystemState.IDLE;
        }
    }

    private SystemState handleUnjammingIn(double now, double startStartedAt) {
        setOpenLoop(kUnjamInPower);
        SystemState newState = SystemState.UNJAMMING_IN;
        if (now - startStartedAt > kUnjamInPeriod) {
            newState = SystemState.UNJAMMING_OUT;
        }
        switch (mWantedState) {
        case FEED:
            return SystemState.FEEDING;
        case UNJAM:
            return newState;
        case EXHAUST:
            return SystemState.EXHAUSTING;
        default:
            return SystemState.IDLE;
        }
    }

    private SystemState handleFeeding() {
        if (mStateChanged) {
            // mMasterTalon.changeControlMode(TalonControlMode.Speed);
            // mMasterTalon.setSetpoint(Constants.kFeederFeedSpeedRpm * Constants.kFeederSensorGearReduction);
//            mMasterTalon.set(1.0);
        	mVictor.set(1.0);
        }
        return defaultStateTransfer();
    }

    private SystemState handleExhaust() {
        setOpenLoop(kExhaustVoltage);
        return defaultStateTransfer();
    }

    public synchronized void setWantedState(WantedState state) {
        mWantedState = state;
    }

    private void setOpenLoop(double voltage) {
 //       if (mStateChanged) {
 //           mMasterTalon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
 //       }
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
 /*       final double kCurrentThres = 0.5;
        final double kRpmThes = 2000.0;

        mSlaveTalon.changeControlMode(TalonControlMode.Voltage);
        mMasterTalon.changeControlMode(TalonControlMode.Voltage);

        mSlaveTalon.set(0.0);
        mMasterTalon.set(0.0);

        mMasterTalon.set(6.0f);
        Timer.delay(4.0);
        final double currentMaster = mMasterTalon.getOutputCurrent();
        final double rpmMaster = mMasterTalon.getSpeed();
        mMasterTalon.set(0.0f);

        Timer.delay(2.0);

        mSlaveTalon.set(-6.0f);
        Timer.delay(4.0);
        final double currentSlave = mSlaveTalon.getOutputCurrent();
        final double rpmSlave = mMasterTalon.getSpeed();
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
//        return true;
 //   }

}
