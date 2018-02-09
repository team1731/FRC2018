package org.usfirst.frc.team1731.robot.subsystems;

import java.util.Arrays;

import org.usfirst.frc.team1731.lib.util.MovingAverage;
import org.usfirst.frc.team1731.lib.util.Util;
import org.usfirst.frc.team1731.lib.util.drivers.TalonSRXFactory;
import org.usfirst.frc.team1731.robot.Constants;
import org.usfirst.frc.team1731.robot.loops.Loop;
import org.usfirst.frc.team1731.robot.loops.Looper;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DoubleSolenoid;


//import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;

/**
 * 1731 the intake picks up cubes and ejects them
 * 
 * @see Subsystem.java
 */
@SuppressWarnings("unused")
public class Intake extends Subsystem {
    private static Intake sInstance = null;

    public static Intake getInstance() {
        if (sInstance == null) {
            sInstance = new Intake();
        }
        return sInstance;
    }

    // hardware
//    private CANTalon mMasterTalon, mSlaveTalon;
//    private Solenoid mDeploySolenoid
    private final VictorSPX mVictorLeft;
    private final VictorSPX mVictorRight;

    private MovingAverage mThrottleAverage = new MovingAverage(50);

    private Intake() {
    	mVictorLeft = new VictorSPX(Constants.kIntakeVictorLeft);
    	mVictorRight = new VictorSPX(Constants.kIntakeVictorRight);
    	//mVictor = new VictorSP(Constants.kIntakeVictor);
    	//todo setup IR sensor
/*        mMasterTalon = CANTalonFactory.createDefaultTalon(Constants.kIntakeMasterId);
        mMasterTalon.setStatusFrameRateMs(CANTalon.StatusFrameRate.General, 1000);
        mMasterTalon.setStatusFrameRateMs(CANTalon.StatusFrameRate.Feedback, 1000);
        mMasterTalon.changeControlMode(CANTalon.TalonControlMode.Voltage);

        mSlaveTalon = CANTalonFactory.createDefaultTalon(Constants.kIntakeSlaveId);
        mSlaveTalon.setStatusFrameRateMs(CANTalon.StatusFrameRate.General, 1000);
        mSlaveTalon.setStatusFrameRateMs(CANTalon.StatusFrameRate.Feedback, 1000);
        mSlaveTalon.changeControlMode(CANTalon.TalonControlMode.Voltage);

        mDeploySolenoid = new Solenoid(Constants.kIntakeDeploySolenoidId);
        */
    }


    public enum SystemState {
    	STOP,//stop
        FULL, // cube is loaded
        LOADING, // loading cube
        IDLE, // stop all motors
        EJECTING, // ejecting cube
        
    }

    public enum WantedState {
    	IDLE,
        LOAD,
        EJECT,
        STOP,
    }

    private SystemState mSystemState = SystemState.IDLE;
    private WantedState mWantedState = WantedState.IDLE;

    private double mCurrentStateStartTime;
	DoubleSolenoid PincerLeft = new DoubleSolenoid (1, 0);
	DoubleSolenoid PincerRight = new DoubleSolenoid (2, 3);
    private boolean mStateChanged;
    private boolean mSensorFull = false;

    private Loop mLoop = new Loop() {
        @Override
        public void onStart(double timestamp) {
            stop();
            synchronized (Intake.this) {
                mSystemState = SystemState.IDLE;
                mStateChanged = true;
                mCurrentStateStartTime = timestamp;
            }
        }

        @Override
        public void onLoop(double timestamp) {
            synchronized (Intake.this) {
                SystemState newState;
                switch (mSystemState) {
                case IDLE:
                    newState = handleIdle();
                    break;
                case STOP:
                    newState = handleStop();
                    break;
                case FULL:
                    newState = handleFull(timestamp, mCurrentStateStartTime);
                    break;
                case LOADING:
                    newState = handleLoading();
                    break;
                case EJECTING:
                    newState = handleEjecting();
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
        case LOAD:
            return SystemState.LOADING;
        case EJECT:
            return SystemState.EJECTING;
        case STOP:
            return SystemState.STOP;
        default:
            return SystemState.IDLE;
        }
    }

    private SystemState handleIdle() {
    	if (mStateChanged) {
        //setOpenLoop(0.0f);
    		mVictorLeft.set(ControlMode.PercentOutput, 0);
    		mVictorRight.set(ControlMode.PercentOutput, 0);//TURN MOTORS OFF
    		PincerRight.set(DoubleSolenoid.Value.kReverse);
    		PincerLeft.set(DoubleSolenoid.Value.kReverse);
    	}
        return defaultStateTransfer();
    }
    private SystemState handleStop() {
    	if (mStateChanged) {
        //setOpenLoop(0.0f);
    		mVictorLeft.set(ControlMode.PercentOutput, 0);
    		mVictorRight.set(ControlMode.PercentOutput, 0);//TURN MOTORS OFF
       		PincerRight.set(DoubleSolenoid.Value.kOff);
    		PincerLeft.set(DoubleSolenoid.Value.kOff);
    	}
        return defaultStateTransfer();
    }

    private SystemState handleFull(double now, double startStartedAt) {
        //setOpenLoop(kUnjamOutPower);
    	if (mStateChanged) {
            //setOpenLoop(0.0f);
        		mVictorLeft.set(ControlMode.PercentOutput, 0);
        		mVictorRight.set(ControlMode.PercentOutput, 0);//TURN MOTORS OFF
        		PincerLeft.set(DoubleSolenoid.Value.kForward);
           		PincerRight.set(DoubleSolenoid.Value.kForward);
        	}
        SystemState newState = SystemState.FULL;
        //if (now - startStartedAt > kUnjamOutPeriod) {
          //  newState = SystemState.UNJAMMING_IN;
       // }
        //TURN MOTOR OFF
        //SET PNEUMATICS TO KOFF
        /*switch (mWantedState) {
        case FEED:
            return SystemState.FEEDING;
        case UNJAM:
            return newState;
        case EXHAUST:
            return SystemState.EXHAUSTING;
        default:
            return SystemState.IDLE;
        } */
        return newState;
    }

    private SystemState handleLoading() {
    	if (mSensorFull) { 
    		
       		PincerLeft.set(DoubleSolenoid.Value.kReverse);
       	    PincerRight.set(DoubleSolenoid.Value.kReverse);
    		mSystemState = SystemState.FULL;
    	} else if (mStateChanged) {
            // mMasterTalon.changeControlMode(TalonControlMode.Speed);
            // mMasterTalon.setSetpoint(Constants.kFeederFeedSpeedRpm * Constants.kFeederSensorGearReduction);
//            mMasterTalon.set(1.0);
        	mVictorLeft.set(ControlMode.PercentOutput, -0.5);
        	mVictorRight.set(ControlMode.PercentOutput, 0.5);
        } 
    	//CLAMP 
    	//TURN ON MOTORS
    	//
        return defaultStateTransfer(); 
    }

    private SystemState handleEjecting() {
       // setOpenLoop(kExhaustVoltage);
    	if (mSensorFull) { 
    		if (mStateChanged) {
    	   		PincerLeft.set(DoubleSolenoid.Value.kReverse);
    	   	    PincerLeft.set(DoubleSolenoid.Value.kReverse);
                // mMasterTalon.changeControlMode(TalonControlMode.Speed);
                // mMasterTalon.setSetpoint(Constants.kFeederFeedSpeedRpm * Constants.kFeederSensorGearReduction);
//                mMasterTalon.set(1.0);
            	mVictorLeft.set(ControlMode.PercentOutput, 0.5);
            	mVictorRight.set(ControlMode.PercentOutput, -0.5);
            } 
    	} else mSystemState = SystemState.IDLE;
        return defaultStateTransfer();
    }

    public synchronized void setWantedState(WantedState state) {
        mWantedState = state;
    }

    @Override
    public void outputToSmartDashboard() {
        // SmartDashboard.putNumber("feeder_speed", mMasterTalon.get() / Constants.kFeederSensorGearReduction);
    }

    public void setOff() {
    	mWantedState = WantedState.STOP; 
    }

    @Override
    public void zeroSensors() {
    }


    private void setOpenLoop(double voltage) {
        // voltage = -voltage; // Flip so +V = intake
        mVictorLeft.set(ControlMode.PercentOutput, -voltage);
        mVictorRight.set(ControlMode.PercentOutput, voltage);
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

	public void setOut() {
		mWantedState = WantedState.EJECT;
		// TODO Auto-generated method stub
		
	}

	public void setOn() {
		mWantedState = WantedState.IDLE;
		// TODO Auto-generated method stub
		
	}

	public void setIn() { 
		mWantedState = WantedState.LOAD;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	public void setReverse() {
		// TODO Auto-generated method stub
		
	}

}

