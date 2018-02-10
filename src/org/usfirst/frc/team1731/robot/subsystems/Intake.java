package org.usfirst.frc.team1731.robot.subsystems;

import java.util.Arrays;

import org.usfirst.frc.team1731.lib.util.MovingAverage;
import org.usfirst.frc.team1731.lib.util.Util;
import org.usfirst.frc.team1731.lib.util.drivers.TalonSRXFactory;
import org.usfirst.frc.team1731.robot.Constants;
import org.usfirst.frc.team1731.robot.loops.Looper;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

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


    private VictorSPX mVictor1;
    private VictorSPX mVictor2;



    private Intake() {
    	mVictor1 = new VictorSPX(Constants.kIntakeVictor1);
    	mVictor2 = new VictorSPX(Constants.kIntakeVictor2);
    }

	public void setEjecting() {
        mVictor1.set(ControlMode.PercentOutput, 1);
        mVictor2.set(ControlMode.PercentOutput, -1);
    }
	
	public void setIntaking() {
        mVictor1.set(ControlMode.PercentOutput, -1);
        mVictor2.set(ControlMode.PercentOutput, 1);
    }
		

    @Override
    public void outputToSmartDashboard() {

    }

    @Override
    public synchronized void stop() {
        mVictor1.set(ControlMode.PercentOutput, 0);
        mVictor2.set(ControlMode.PercentOutput, 0);
    }

    @Override
    public void zeroSensors() {

    }

    @Override
    public void registerEnabledLoops(Looper in) {

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



	public void setIdle() {
		// TODO Auto-generated method stub
		
	}



}
