package org.usfirst.frc.team1731.lib.util.drivers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.*;

/**
 * This class is a thin wrapper around the TalonSRX that reduces CAN bus / CPU overhead by skipping duplicate set
 * commands. (By default the Talon flushes the Tx buffer on every set call).
 */
public class LazyTalonSRX extends TalonSRX {
    protected double mLastSet = Double.NaN;
    protected ControlMode mLastControlMode = null;

   /* public LazyTalonSRX(int deviceNumber, int controlPeriodMs, int enablePeriodMs) {
        super(deviceNumber, controlPeriodMs, enablePeriodMs);
    }

    public LazyTalonSRX(int deviceNumber, int controlPeriodMs) {
        super(deviceNumber, controlPeriodMs);
    }
*/
    public LazyTalonSRX(int deviceNumber) {
        super(deviceNumber);
    }

    @Override
    public void set(ControlMode controlMode, double value) {
       // if (value != mLastSet || controlMode != mLastControlMode) {
       //     mLastSet = value;
       //     mLastControlMode = controlMode;
            super.set(controlMode,value);
        //}
    }
}
