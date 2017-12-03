package org.usfirst.frc.team1731.robot.subsystems;

import java.util.Arrays;

import org.usfirst.frc.team1731.lib.util.MovingAverage;
import org.usfirst.frc.team1731.lib.util.Util;
import org.usfirst.frc.team1731.lib.util.drivers.CANTalonFactory;
import org.usfirst.frc.team1731.robot.Constants;
import org.usfirst.frc.team1731.robot.loops.Looper;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;

/**
 * 1731 the intake picks up balls off the ground
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
//    private Solenoid mDeploySolenoid;
    private final VictorSP mVictor;

    private MovingAverage mThrottleAverage = new MovingAverage(50);

    private Intake() {
    	mVictor = new VictorSP(Constants.kIntakeVictor);
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

    @Override
    public void outputToSmartDashboard() {

    }

    @Override
    public synchronized void stop() {
        mThrottleAverage.clear();
        setOff();
    }

    @Override
    public void zeroSensors() {

    }

    @Override
    public void registerEnabledLoops(Looper in) {

    }

    public synchronized void setCurrentThrottle(double currentThrottle) {
        mThrottleAverage.addNumber(currentThrottle);
    }

  /*  public synchronized void deploy() {
        mDeploySolenoid.set(true);
    }

    public synchronized void reset() { // only use this in autoInit to reset the intake
        mDeploySolenoid.set(false);
    }
*/
    public synchronized void setOn() {
 //       deploy();
        setOpenLoop(getScaledIntakeVoltage());
    }

    public synchronized void setOnWhileShooting() {
  //      deploy();
        setOpenLoop(Constants.kIntakeShootingVoltage);
    }

    public synchronized void setOff() {
        setOpenLoop(0.0);
    }

    public synchronized void setReverse() {
        setOpenLoop(-Constants.kIntakeVoltageMax);
    }

    private double getScaledIntakeVoltage() {
        // Perform a linear interpolation from the Abs of throttle. Keep in mind we want to run at
        // full throttle when in reverse.

        double scale;
        if (mThrottleAverage.getSize() > 0) {
            scale = Math.min(0.0, Math.max(0.0, mThrottleAverage.getAverage()));
        } else {
            scale = 0.0;
        }

        return Constants.kIntakeVoltageMax - scale * Constants.kIntakeVoltageDifference;
    }

    private void setOpenLoop(double voltage) {
        // voltage = -voltage; // Flip so +V = intake
        mVictor.set(-voltage);
        mVictor.set(voltage);
    }

    public boolean checkSystem() {
 /*       final double kCurrentThres = 0.5;

        mMasterTalon.set(0.0);
        mSlaveTalon.set(0.0);

        mMasterTalon.set(-6.0f);
        Timer.delay(4.0);
        final double currentMaster = mMasterTalon.getOutputCurrent();
        mMasterTalon.set(0.0);

        Timer.delay(2.0);

        mSlaveTalon.set(6.0f);
        Timer.delay(4.0);
        final double currentSlave = mSlaveTalon.getOutputCurrent();
        mSlaveTalon.set(0.0);

        System.out.println("Intake Master Current: " + currentMaster + " Slave current: " + currentSlave);

        boolean failure = false;

        if (currentMaster < kCurrentThres) {
            failure = true;
            System.out.println("!!!!!!!!!!!!!!!!!!! Intake Master Current Low !!!!!!!!!!!!!!");
        }

        if (currentSlave < kCurrentThres) {
            failure = true;
            System.out.println("!!!!!!!!!!!!!!!!!!! Intake Slave Current Low !!!!!!!!!!!!!!!!");
        }

        if (!Util.allCloseTo(Arrays.asList(currentMaster, currentSlave), currentMaster, 5.0)) {
            failure = true;
            System.out.println("!!!!!!!!!!!!!!!!!!!! Intake Currents different !!!!!!!!!!!!!!!");
        }

        return !failure;
        */
    	return true;
    }

}