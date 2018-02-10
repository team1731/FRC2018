package org.usfirst.frc.team1731.robot.subsystems;

import java.util.Arrays;

import org.usfirst.frc.team1731.lib.util.Util;
import org.usfirst.frc.team1731.lib.util.drivers.TalonSRXFactory;
import org.usfirst.frc.team1731.robot.Constants;
import org.usfirst.frc.team1731.robot.loops.Loop;
import org.usfirst.frc.team1731.robot.loops.Looper;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;

/**

 * 
 * 1731 the feeder is the spinner thing that brings balls to the shooter.
 * 
 * @see Subsystem.java
 */
@SuppressWarnings("unused")
public class Climber extends Subsystem {
    private static final double kReversing = -1.0;
    private static final double kUnjamInPeriod = .2 * kReversing;
    private static final double kUnjamOutPeriod = .4 * kReversing;
    private static final double kUnjamInPower = 6.0 * kReversing / 12.0;
    private static final double kUnjamOutPower = -6.0 * kReversing / 12.0;
    private static final double kFeedVoltage = 10.0;
    private static final double kExhaustVoltage = kFeedVoltage * kReversing / 12.0;

    private static Climber sInstance = null;

    public static Climber getInstance() {
        if (sInstance == null) {
            sInstance = new Climber();
        }
        return sInstance;
    }

//    private final TalonSRX mElevatorMotor; 
    private final TalonSRX mMasterTalon;
    private final TalonSRX mSlaveTalon;

    public Climber() {
    
        mMasterTalon = TalonSRXFactory.createDefaultTalon(Constants.kClimberMasterId);
        mMasterTalon.setInverted(false);
        mMasterTalon.setNeutralMode(NeutralMode.Brake);

        mSlaveTalon = TalonSRXFactory.createPermanentSlaveTalon(Constants.kClimberSlaveId, Constants.kClimberMasterId);
        mSlaveTalon.setInverted(true);
        mSlaveTalon.setNeutralMode(NeutralMode.Brake);
       
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
    private boolean mStateChanged;

    private Loop mLoop = new Loop() {
        @Override
        public void onStart(double timestamp) {
            stop();
            synchronized (Climber.this) {
                mSystemState = SystemState.IDLE;
                mStateChanged = true;
                mCurrentStateStartTime = timestamp;
            }
        }

        @Override
        public void onLoop(double timestamp) {
            synchronized (Climber.this) {
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
                    System.out.println("Feeder state " + mSystemState + " to " + newState);
                    mSystemState = newState;
                    mCurrentStateStartTime = timestamp;
                    mStateChanged = true;
                } else {
                    mStateChanged = false;
                }
            }
        }

        private SystemState handleGoingDown() {
            mMasterTalon.set(ControlMode.PercentOutput, 1);

            return defaultStateTransfer();
        }

		private SystemState handleGoingUp() {
            mMasterTalon.set(ControlMode.PercentOutput, -1);
            
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
        mMasterTalon.set(ControlMode.PercentOutput, -1);
        return defaultStateTransfer();
    }

   

    public synchronized void setWantedState(WantedState state) {
        mWantedState = state;
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
        return true;
    }

}
