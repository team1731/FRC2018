package org.usfirst.frc.team1731.robot.subsystems;

import java.util.Arrays;
import java.util.Optional;

import org.usfirst.frc.team1731.lib.util.DriveSignal;
import org.usfirst.frc.team1731.lib.util.ReflectingCSVWriter;
import org.usfirst.frc.team1731.lib.util.Util;
import org.usfirst.frc.team1731.lib.util.control.Lookahead;
import org.usfirst.frc.team1731.lib.util.control.Path;
import org.usfirst.frc.team1731.lib.util.control.PathFollower;
import org.usfirst.frc.team1731.lib.util.drivers.TalonSRXFactory;
import org.usfirst.frc.team1731.lib.util.drivers.NavX;
import org.usfirst.frc.team1731.lib.util.math.RigidTransform2d;
import org.usfirst.frc.team1731.lib.util.math.Rotation2d;
import org.usfirst.frc.team1731.lib.util.math.Twist2d;
import org.usfirst.frc.team1731.robot.Constants;
import org.usfirst.frc.team1731.robot.Kinematics;
import org.usfirst.frc.team1731.robot.RobotState;
import org.usfirst.frc.team1731.robot.ShooterAimingParameters;
import org.usfirst.frc.team1731.robot.loops.Loop;
import org.usfirst.frc.team1731.robot.loops.Looper;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This subsystem consists of the robot's drivetrain: 4 CIM motors, 4 talons, one solenoid and 2 pistons to shift gears,
 * and a navX board. The Drive subsystem has several control methods including open loop, velocity control, and position
 * control. The Drive subsystem also has several methods that handle automatic aiming, autonomous path driving, and
 * manual control.
 * 
 * @see Subsystem.java
 */
public class Drive extends Subsystem {

    private static Drive mInstance = new Drive();

    private static final int kLowGearPositionControlSlot = 0;
    private static final int kHighGearVelocityControlSlot = 1;

    public static Drive getInstance() {
        return mInstance;
    }

    
    // The robot drivetrain's various states.
    public enum DriveControlState {
        OPEN_LOOP, // open loop voltage control
        VELOCITY_SETPOINT, // velocity PID control
        PATH_FOLLOWING, // used for autonomous driving
        AIM_TO_GOAL, // turn to face the boiler
        TURN_TO_HEADING, // turn in place
        DRIVE_TOWARDS_GOAL_COARSE_ALIGN, // turn to face the boiler, then DRIVE_TOWARDS_GOAL_COARSE_ALIGN
        DRIVE_TOWARDS_GOAL_APPROACH // drive forwards until we are at optimal shooting distance
    }

    /**
     * Check if the drive talons are configured for velocity control
     */
    protected static boolean usesTalonVelocityControl(DriveControlState state) {
        if (state == DriveControlState.VELOCITY_SETPOINT || state == DriveControlState.PATH_FOLLOWING) {
            return true;
        }
        return false;
    }

    /**
     * Check if the drive talons are configured for position control
     */
    protected static boolean usesTalonPositionControl(DriveControlState state) {
        if (state == DriveControlState.AIM_TO_GOAL ||
                state == DriveControlState.TURN_TO_HEADING ||
                state == DriveControlState.DRIVE_TOWARDS_GOAL_COARSE_ALIGN ||
                state == DriveControlState.DRIVE_TOWARDS_GOAL_APPROACH) {
            return true;
        }
        return false;
    }

    // Control states
    private DriveControlState mDriveControlState;

    // Hardware
    private final TalonSRX mLeftMaster, mRightMaster, mLeftSlave, mRightSlave;
    private final Solenoid mShifter;
    private final NavX mNavXBoard;

    // Controllers
    private RobotState mRobotState = RobotState.getInstance();
    private PathFollower mPathFollower;

    // These gains get reset below!!
    private Rotation2d mTargetHeading = new Rotation2d();
    private Path mCurrentPath = null;

    // Hardware states
    private boolean mIsHighGear;
    private boolean mIsBrakeMode;
    private boolean mIsOnTarget = false;
    private boolean mIsApproaching = false;

    // Logging
    private final ReflectingCSVWriter<PathFollower.DebugOutput> mCSVWriter;

    private final Loop mLoop = new Loop() {
        @Override
        public void onStart(double timestamp) {
            synchronized (Drive.this) {
                setOpenLoop(DriveSignal.NEUTRAL);
                setBrakeMode(false);
                setVelocitySetpoint(0, 0);
                mNavXBoard.reset();
            }
        }

        @Override
        public void onLoop(double timestamp) {
            synchronized (Drive.this) {
                switch (mDriveControlState) {
                case OPEN_LOOP:
                    return;
                case VELOCITY_SETPOINT:
                    return;
                case PATH_FOLLOWING:
                    if (mPathFollower != null) {
                        updatePathFollower(timestamp);
                        mCSVWriter.add(mPathFollower.getDebug());
                    }
                    return;
                case AIM_TO_GOAL:
                    if (!Superstructure.getInstance().isShooting()) {
                        updateGoalHeading(timestamp);
                    }
                    // fallthrough intended
                case TURN_TO_HEADING:
                    updateTurnToHeading(timestamp);
                    return;
                case DRIVE_TOWARDS_GOAL_COARSE_ALIGN:
                    updateDriveTowardsGoalCoarseAlign(timestamp);
                    return;
                case DRIVE_TOWARDS_GOAL_APPROACH:
                    updateDriveTowardsGoalApproach(timestamp);
                    return;
                default:
                    System.out.println("Unexpected drive control state: " + mDriveControlState);
                    break;
                }
            }
        }

        @Override
        public void onStop(double timestamp) {
            stop();
            mCSVWriter.flush();
        }
    };

    private Drive() {
        // Start all Talons in open loop mode.
        mLeftMaster = TalonSRXFactory.createDefaultTalon(Constants.kLeftDriveMasterId);
        //  mLeftMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        mLeftMaster.set(ControlMode.PercentOutput, 0);
        //mLeftMaster.setFeedbackDevice(TalonSRX.FeedbackDevice.CtreMagEncoder_Relative);
        mLeftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.kTimeoutMs);
        //mLeftMaster.reverseSensor(true);
        mLeftMaster.setInverted(true);
        //mLeftMaster.reverseOutput(false);
        mLeftMaster.setSensorPhase(false);

        int leftSensorPresent = mLeftMaster.getSensorCollection().getPulseWidthRiseToRiseUs();

        if (leftSensorPresent == 0) {
            DriverStation.reportError("Could not detect left encoder: " + leftSensorPresent, false);
        }

        mLeftSlave = TalonSRXFactory.createPermanentSlaveTalon(Constants.kLeftDriveSlaveId,
                Constants.kLeftDriveMasterId);
        //mLeftSlave.reverseOutput(false);
        mLeftSlave.setSensorPhase(false);
        //mLeftMaster.setStatusFrameRateMs(StatusFrameRate.Feedback, 5);

        mLeftMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, Constants.kTimeoutMs); 
        mLeftMaster.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 5, Constants.kTimeoutMs); 
     
        mRightMaster = TalonSRXFactory.createDefaultTalon(Constants.kRightDriveMasterId);

       // mRightMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        mRightMaster.set(ControlMode.PercentOutput, 0);
        //mRightMaster.reverseSensor(false);
        mRightMaster.setInverted(true);
       // mRightMaster.reverseOutput(true);
        mRightMaster.setSensorPhase(false);
        //mRightMaster.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
        mRightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.kTimeoutMs);
        
        int rightSensorPresent = mLeftMaster.getSensorCollection().getPulseWidthRiseToRiseUs();
        //CANTalon.FeedbackDeviceStatus leftSensorPresent = mLeftMaster
        //        .isSensorPresent(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
        if (rightSensorPresent == 0) {
            DriverStation.reportError("Could not detect right encoder: " + rightSensorPresent, false);
        }

        mRightSlave = TalonSRXFactory.createPermanentSlaveTalon(Constants.kRightDriverSlaveId,
                Constants.kRightDriveMasterId);
        //mRightSlave.reverseOutput(false);
        mRightSlave.setSensorPhase(false);
        //mRightMaster.setStatusFrameRateMs(StatusFrameRate.Feedback, 5);
        mRightMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, Constants.kTimeoutMs); 
        mRightMaster.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 5, Constants.kTimeoutMs);

       // mLeftMaster.SetVelocityMeasurementPeriod(VelocityMeasurementPeriod.Period_10Ms);
        mLeftMaster.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_10Ms, Constants.kTimeoutMs);
       // mLeftMaster.SetVelocityMeasurementWindow(32);
        mLeftMaster.configVelocityMeasurementWindow(32, Constants.kTimeoutMs);
        //mRightMaster.SetVelocityMeasurementPeriod(VelocityMeasurementPeriod.Period_10Ms);
        mRightMaster.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_10Ms, Constants.kTimeoutMs);
       // mRightMaster.SetVelocityMeasurementWindow(32);
        mRightMaster.configVelocityMeasurementWindow(32, Constants.kTimeoutMs);

        mShifter = Constants.makeSolenoidForId(Constants.kShifterSolenoidId);

        reloadGains();

        mIsHighGear = false;
        setHighGear(true);
        setOpenLoop(DriveSignal.NEUTRAL);

        // Path Following stuff
        mNavXBoard = new NavX(SPI.Port.kMXP);

        // Force a CAN message across.
        mIsBrakeMode = true;
        setBrakeMode(false);

        mCSVWriter = new ReflectingCSVWriter<PathFollower.DebugOutput>("/home/lvuser/PATH-FOLLOWER-LOGS.csv",
                PathFollower.DebugOutput.class);
    }

    @Override
    public void registerEnabledLoops(Looper in) {
        in.register(mLoop);
    }

    /**
     * Configure talons for open loop control
     */
    public synchronized void setOpenLoop(DriveSignal signal) {
        if (mDriveControlState != DriveControlState.OPEN_LOOP) {
           // mLeftMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
            mLeftMaster.set(ControlMode.PercentOutput, signal.getLeft());
            //mRightMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
            mRightMaster.set(ControlMode.PercentOutput, -signal.getRight());
            //mLeftMaster.configNominalOutputVoltage(0.0, 0.0);
            //mRightMaster.configNominalOutputVoltage(0.0, 0.0);
            mDriveControlState = DriveControlState.OPEN_LOOP;
            setBrakeMode(false);
        }
        // Right side is reversed, but reverseOutput doesn't invert PercentVBus.
        // So set negative on the right master.
       // mRightMaster.set(-signal.getRight());
       // mLeftMaster.set(signal.getLeft());
    }

    public boolean isHighGear() {
        return mIsHighGear;
    }

    public synchronized void setHighGear(boolean wantsHighGear) {
        if (wantsHighGear != mIsHighGear) {
            mIsHighGear = wantsHighGear;
            mShifter.set(!wantsHighGear);
        }
    }

    public boolean isBrakeMode() {
        return mIsBrakeMode;
    }

    public synchronized void setBrakeMode(boolean on) {
        if (mIsBrakeMode != on) {
            mIsBrakeMode = on;
           // mRightMaster.enableBrakeMode(on);
            //mRightSlave.enableBrakeMode(on);
           // mLeftMaster.enableBrakeMode(on);
            //mLeftSlave.enableBrakeMode(on);
            
            mRightMaster.setNeutralMode(com.ctre.phoenix.motorcontrol.NeutralMode.Brake);
            mRightSlave.setNeutralMode(com.ctre.phoenix.motorcontrol.NeutralMode.Brake);
            mLeftMaster.setNeutralMode(com.ctre.phoenix.motorcontrol.NeutralMode.Brake);
            mLeftSlave.setNeutralMode(com.ctre.phoenix.motorcontrol.NeutralMode.Brake);
        }
    }

    @Override
    public synchronized void stop() {
        setOpenLoop(DriveSignal.NEUTRAL);
    }

    @Override
    public void outputToSmartDashboard() {
        final double left_speed = getLeftVelocityInchesPerSec();
        final double right_speed = getRightVelocityInchesPerSec();
      //  SmartDashboard.putNumber("left voltage (V)", mLeftMaster.getOutputVoltage());
        //SmartDashboard.putNumber("right voltage (V)", mRightMaster.getOutputVoltage());
        
        SmartDashboard.putNumber("left voltage (V)", mLeftMaster.getMotorOutputVoltage());
        SmartDashboard.putNumber("right voltage (V)", mRightMaster.getMotorOutputVoltage());
        SmartDashboard.putNumber("left speed (ips)", left_speed);
        SmartDashboard.putNumber("right speed (ips)", right_speed);
        if (usesTalonVelocityControl(mDriveControlState)) {
            SmartDashboard.putNumber("left speed error (ips)",
                //    rpmToInchesPerSecond(mLeftMaster.getSetpoint()) - left_speed);
            		
            rpmToInchesPerSecond(mLeftMaster.getClosedLoopTarget(0)) - left_speed);
            SmartDashboard.putNumber("right speed error (ips)",
                  //  rpmToInchesPerSecond(mRightMaster.getSetpoint()) - right_speed);
            
            rpmToInchesPerSecond(mRightMaster.getClosedLoopTarget(0)) - right_speed);
        } else {
            SmartDashboard.putNumber("left speed error (ips)", 0.0);
            SmartDashboard.putNumber("right speed error (ips)", 0.0);
        }
        synchronized (this) {
            if (mDriveControlState == DriveControlState.PATH_FOLLOWING && mPathFollower != null) {
                SmartDashboard.putNumber("drive CTE", mPathFollower.getCrossTrackError());
                SmartDashboard.putNumber("drive ATE", mPathFollower.getAlongTrackError());
            } else {
                SmartDashboard.putNumber("drive CTE", 0.0);
                SmartDashboard.putNumber("drive ATE", 0.0);
            }
        }
        //SmartDashboard.putNumber("left position (rotations)", mLeftMaster.getPosition()); 
        //TOO DO need to divide by sensor units per rotation
        SmartDashboard.putNumber("left position (rotations)", mLeftMaster.getSelectedSensorPosition(0));
        //SmartDashboard.putNumber("right position (rotations)", mRightMaster.getPosition());
        SmartDashboard.putNumber("right position (rotations)", mRightMaster.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("gyro vel", getGyroVelocityDegreesPerSec());
        SmartDashboard.putNumber("gyro pos", getGyroAngle().getDegrees());
        SmartDashboard.putBoolean("drive on target", isOnTarget());
    }

    public synchronized void resetEncoders() {
        //mLeftMaster.setEncPosition(0);
        //mLeftMaster.setPosition(0);
        //mRightMaster.setPosition(0);
        //mRightMaster.setEncPosition(0);
        //mLeftSlave.setPosition(0);
        //mRightSlave.setPosition(0);
        

        mLeftMaster.setSelectedSensorPosition(0,Constants.kPidIdx,Constants.kTimeoutMs);
        mRightMaster.setSelectedSensorPosition(0,Constants.kPidIdx,Constants.kTimeoutMs);
        mLeftSlave.setSelectedSensorPosition(0,Constants.kPidIdx,Constants.kTimeoutMs);
        mRightSlave.setSelectedSensorPosition(0,Constants.kPidIdx,Constants.kTimeoutMs);
    }

    @Override
    public void zeroSensors() {
        resetEncoders();
        mNavXBoard.zeroYaw();
    }

    /**
     * Start up velocity mode. This sets the drive train in high gear as well.
     * 
     * @param left_inches_per_sec
     * @param right_inches_per_sec
     */
    public synchronized void setVelocitySetpoint(double left_inches_per_sec, double right_inches_per_sec) {
        configureTalonsForSpeedControl();
        mDriveControlState = DriveControlState.VELOCITY_SETPOINT;
        updateVelocitySetpoint(left_inches_per_sec, right_inches_per_sec);
    }

    /**
     * Configures talons for velocity control
     */
    private void configureTalonsForSpeedControl() {
        if (!usesTalonVelocityControl(mDriveControlState)) {
            // We entered a velocity control state.
           // mLeftMaster.changeControlMode(CANTalon.TalonControlMode.Speed);
            //mLeftMaster.setNominalClosedLoopVoltage(12.0);
           // mLeftMaster.setProfile(kHighGearVelocityControlSlot);
            //mLeftMaster.configNominalOutputVoltage(Constants.kDriveHighGearNominalOutput,
            //        -Constants.kDriveHighGearNominalOutput);
            //mRightMaster.changeControlMode(CANTalon.TalonControlMode.Speed);
            //mRightMaster.setNominalClosedLoopVoltage(12.0);
           // mRightMaster.setProfile(kHighGearVelocityControlSlot);
           // mRightMaster.configNominalOutputVoltage(Constants.kDriveHighGearNominalOutput,
            //        -Constants.kDriveHighGearNominalOutput);
        	// (ATTEMPT TO FIX DRIVING PROBLEM) mLeftMaster.set(ControlMode.Velocity, 0);
        	mLeftMaster.set(ControlMode.Velocity, 2);
        	mLeftMaster.configNominalOutputForward(Constants.kDriveHighGearNominalOutput,Constants.kTimeoutMs);
        	mLeftMaster.configNominalOutputReverse(-Constants.kDriveHighGearNominalOutput, Constants.kTimeoutMs);
        	mLeftMaster.selectProfileSlot(kHighGearVelocityControlSlot, Constants.kPidIdx);
        	// (SEE ABOVE LEFTMASTER) mRightMaster.set(ControlMode.Velocity, 0);
        	mRightMaster.set(ControlMode.Velocity, 2);
        	mRightMaster.configNominalOutputForward(Constants.kDriveHighGearNominalOutput,Constants.kTimeoutMs);
        	mRightMaster.configNominalOutputReverse(-Constants.kDriveHighGearNominalOutput, Constants.kTimeoutMs);
        	mRightMaster.selectProfileSlot(kHighGearVelocityControlSlot, Constants.kPidIdx);
            setBrakeMode(true);
        }
    }

    /**
     * Configures talons for position control
     */
    private void configureTalonsForPositionControl() {
        if (!usesTalonPositionControl(mDriveControlState)) {
            // We entered a position control state.
        	/*
            mLeftMaster.changeControlMode(CANTalon.TalonControlMode.MotionMagic);
            mLeftMaster.setNominalClosedLoopVoltage(12.0);
            mLeftMaster.setProfile(kLowGearPositionControlSlot);
            mLeftMaster.configNominalOutputVoltage(Constants.kDriveLowGearNominalOutput,
                    -Constants.kDriveLowGearNominalOutput);
            mRightMaster.changeControlMode(CANTalon.TalonControlMode.MotionMagic);
            mRightMaster.setNominalClosedLoopVoltage(12.0);
            mRightMaster.setProfile(kLowGearPositionControlSlot);
            mRightMaster.configNominalOutputVoltage(Constants.kDriveLowGearNominalOutput,
                    -Constants.kDriveLowGearNominalOutput);
            */
        	mLeftMaster.set(ControlMode.MotionMagic, 0);
        	mLeftMaster.configNominalOutputForward(Constants.kDriveLowGearNominalOutput,Constants.kTimeoutMs);
        	mLeftMaster.configNominalOutputReverse(-Constants.kDriveLowGearNominalOutput, Constants.kTimeoutMs);
        	mLeftMaster.selectProfileSlot(kLowGearPositionControlSlot, Constants.kPidIdx);
        	mRightMaster.set(ControlMode.MotionMagic, 0);
        	mRightMaster.configNominalOutputForward(Constants.kDriveLowGearNominalOutput,Constants.kTimeoutMs);
        	mRightMaster.configNominalOutputReverse(-Constants.kDriveLowGearNominalOutput, Constants.kTimeoutMs);
        	mRightMaster.selectProfileSlot(kLowGearPositionControlSlot, Constants.kPidIdx);
            setBrakeMode(true);
        }
    }

    /**
     * Adjust Velocity setpoint (if already in velocity mode)
     * 
     * @param left_inches_per_sec
     * @param right_inches_per_sec
     */
    private synchronized void updateVelocitySetpoint(double left_inches_per_sec, double right_inches_per_sec) {
        if (usesTalonVelocityControl(mDriveControlState)) {
            final double max_desired = Math.max(Math.abs(left_inches_per_sec), Math.abs(right_inches_per_sec));
            final double scale = max_desired > Constants.kDriveHighGearMaxSetpoint
                    ? Constants.kDriveHighGearMaxSetpoint / max_desired : 1.0;
 //           mLeftMaster.set(inchesPerSecondToRpm(left_inches_per_sec * scale));
 //           mRightMaster.set(inchesPerSecondToRpm(right_inches_per_sec * scale));
              mLeftMaster.set(ControlMode.Velocity, inchesPerSecondToRpm(left_inches_per_sec * scale));
              mRightMaster.set(ControlMode.Velocity, inchesPerSecondToRpm(right_inches_per_sec * scale));
        } else {
            System.out.println("Hit a bad velocity control state");
            //mLeftMaster.set(0);
            //mRightMaster.set(0);
            mLeftMaster.set(ControlMode.Velocity,0);
            mRightMaster.set(ControlMode.Velocity,0);
        }

    }

    /**
     * Adjust position setpoint (if already in position mode)
     * 
     * @param left_inches_per_sec
     * @param right_inches_per_sec
     */
    private synchronized void updatePositionSetpoint(double left_position_inches, double right_position_inches) {
        if (usesTalonPositionControl(mDriveControlState)) {
           // mLeftMaster.set(inchesToRotations(left_position_inches));
           // mRightMaster.set(inchesToRotations(right_position_inches));
        	mLeftMaster.set(ControlMode.Position, inchesToRotations(left_position_inches));
        	mRightMaster.set(ControlMode.Position, inchesToRotations(right_position_inches));
        } else {
            System.out.println("Hit a bad position control state");
           // mLeftMaster.set(0);
           // mRightMaster.set(0);
            mLeftMaster.set(ControlMode.Position,0);
            mRightMaster.set(ControlMode.Position,0);
        }
    }

    private static double rotationsToInches(double rotations) {
        return rotations * (Constants.kDriveWheelDiameterInches * Math.PI);
    }

    private static double rpmToInchesPerSecond(double rpm) {
        return rotationsToInches(rpm) / 60;
    }

    private static double inchesToRotations(double inches) {
        return inches / (Constants.kDriveWheelDiameterInches * Math.PI);
    }

    private static double inchesPerSecondToRpm(double inches_per_second) {
        return inchesToRotations(inches_per_second) * 60;
    }

    public double getLeftDistanceInches() {
     //   return rotationsToInches(mLeftMaster.getPosition());
        return rotationsToInches(mLeftMaster.getSelectedSensorPosition(Constants.kPidIdx));
    }

    public double getRightDistanceInches() {
     //   return rotationsToInches(mRightMaster.getPosition());
        return rotationsToInches(mRightMaster.getSelectedSensorPosition(Constants.kPidIdx));
    }

    public double getLeftVelocityInchesPerSec() {
       // return rpmToInchesPerSecond(mLeftMaster.getSpeed());
    	return rpmToInchesPerSecond(mLeftMaster.getSelectedSensorVelocity(Constants.kPidIdx));
    }

    public double getRightVelocityInchesPerSec() {
        //return rpmToInchesPerSecond(mRightMaster.getSpeed());
    	return rpmToInchesPerSecond(mRightMaster.getSelectedSensorVelocity(Constants.kPidIdx));
    }

    public synchronized Rotation2d getGyroAngle() {
        return mNavXBoard.getYaw();
    }

    public synchronized NavX getNavXBoard() {
        return mNavXBoard;
    }

    public synchronized void setGyroAngle(Rotation2d angle) {
        mNavXBoard.reset();
        mNavXBoard.setAngleAdjustment(angle);
    }

    public synchronized double getGyroVelocityDegreesPerSec() {
        return mNavXBoard.getYawRateDegreesPerSec();
    }

    /**
     * Update the heading at which the robot thinks the boiler is.
     * 
     * Is called periodically when the robot is auto-aiming towards the boiler.
     */
    private void updateGoalHeading(double timestamp) {
        Optional<ShooterAimingParameters> aim = mRobotState.getAimingParameters();
        if (aim.isPresent()) {
            mTargetHeading = aim.get().getRobotToGoal();
        }
    }

    /**
     * Turn the robot to a target heading.
     * 
     * Is called periodically when the robot is auto-aiming towards the boiler.
     */
    private void updateTurnToHeading(double timestamp) {
        if (Superstructure.getInstance().isShooting()) {
            // Do not update heading while shooting - just base lock. By not updating the setpoint, we will fight to
            // keep position.
            return;
        }
        final Rotation2d field_to_robot = mRobotState.getLatestFieldToVehicle().getValue().getRotation();

        // Figure out the rotation necessary to turn to face the goal.
        final Rotation2d robot_to_target = field_to_robot.inverse().rotateBy(mTargetHeading);

        // Check if we are on target
        final double kGoalPosTolerance = 0.75; // degrees
        final double kGoalVelTolerance = 5.0; // inches per second
        if (Math.abs(robot_to_target.getDegrees()) < kGoalPosTolerance
                && Math.abs(getLeftVelocityInchesPerSec()) < kGoalVelTolerance
                && Math.abs(getRightVelocityInchesPerSec()) < kGoalVelTolerance) {
            // Use the current setpoint and base lock.
            mIsOnTarget = true;
            updatePositionSetpoint(getLeftDistanceInches(), getRightDistanceInches());
            return;
        }

        Kinematics.DriveVelocity wheel_delta = Kinematics
                .inverseKinematics(new Twist2d(0, 0, robot_to_target.getRadians()));
        updatePositionSetpoint(wheel_delta.left + getLeftDistanceInches(),
                wheel_delta.right + getRightDistanceInches());
    }

    /**
     * Essentially does the same thing as updateTurnToHeading but sends the robot into the DRIVE_TOWARDS_GOAL_APPROACH
     * state if it detects we are not at an optimal shooting range
     */
    private void updateDriveTowardsGoalCoarseAlign(double timestamp) {
        updateGoalHeading(timestamp);
        updateTurnToHeading(timestamp);
        mIsApproaching = true;
        if (mIsOnTarget) {
            // Done coarse alignment.

            Optional<ShooterAimingParameters> aim = mRobotState.getAimingParameters();
            if (aim.isPresent()) {
                final double distance = aim.get().getRange();

                if (distance < Constants.kShooterOptimalRangeCeiling &&
                        distance > Constants.kShooterOptimalRangeFloor) {
                    // Don't drive, just shoot.
                    mDriveControlState = DriveControlState.AIM_TO_GOAL;
                    mIsApproaching = false;
                    mIsOnTarget = false;
                    updatePositionSetpoint(getLeftDistanceInches(), getRightDistanceInches());
                    return;
                }
            }

            mDriveControlState = DriveControlState.DRIVE_TOWARDS_GOAL_APPROACH;
            mIsOnTarget = false;
        }
    }

    /**
     * Drives the robot straight forwards until it is at an optimal shooting distance. Then sends the robot into the
     * AIM_TO_GOAL state for one final alignment
     */
    private void updateDriveTowardsGoalApproach(double timestamp) {
        Optional<ShooterAimingParameters> aim = mRobotState.getAimingParameters();
        mIsApproaching = true;
        if (aim.isPresent()) {
            final double distance = aim.get().getRange();
            double error = 0.0;
            if (distance < Constants.kShooterOptimalRangeFloor) {
                error = distance - Constants.kShooterOptimalRangeFloor;
            } else if (distance > Constants.kShooterOptimalRangeCeiling) {
                error = distance - Constants.kShooterOptimalRangeCeiling;
            }
            final double kGoalPosTolerance = 1.0; // inches
            if (Util.epsilonEquals(error, 0.0, kGoalPosTolerance)) {
                // We are on target. Switch back to auto-aim.
                mDriveControlState = DriveControlState.AIM_TO_GOAL;
                RobotState.getInstance().resetVision();
                mIsApproaching = false;
                updatePositionSetpoint(getLeftDistanceInches(), getRightDistanceInches());
                return;
            }
            updatePositionSetpoint(getLeftDistanceInches() + error, getRightDistanceInches() + error);
        } else {
            updatePositionSetpoint(getLeftDistanceInches(), getRightDistanceInches());
        }
    }

    /**
     * Called periodically when the robot is in path following mode. Updates the path follower with the robots latest
     * pose, distance driven, and velocity, the updates the wheel velocity setpoints.
     */
    private void updatePathFollower(double timestamp) {
        RigidTransform2d robot_pose = mRobotState.getLatestFieldToVehicle().getValue();
        Twist2d command = mPathFollower.update(timestamp, robot_pose,
                RobotState.getInstance().getDistanceDriven(), RobotState.getInstance().getPredictedVelocity().dx);
        if (!mPathFollower.isFinished()) {
            Kinematics.DriveVelocity setpoint = Kinematics.inverseKinematics(command);
            updateVelocitySetpoint(setpoint.left, setpoint.right);
        } else {
            updateVelocitySetpoint(0, 0);
        }
    }

    public synchronized boolean isOnTarget() {
        // return true;
        return mIsOnTarget;
    }

    public synchronized boolean isAutoAiming() {
        return mDriveControlState == DriveControlState.AIM_TO_GOAL;
    }

    /**
     * Configures the drivebase for auto aiming
     */
    public synchronized void setWantAimToGoal() {
        if (mDriveControlState != DriveControlState.AIM_TO_GOAL) {
            mIsOnTarget = false;
            configureTalonsForPositionControl();
            mDriveControlState = DriveControlState.AIM_TO_GOAL;
            updatePositionSetpoint(getLeftDistanceInches(), getRightDistanceInches());
            mTargetHeading = getGyroAngle();
        }
        setHighGear(false);
    }

    /**
     * Configures the drivebase for auto driving
     */
    public synchronized void setWantDriveTowardsGoal() {
        if (mDriveControlState != DriveControlState.DRIVE_TOWARDS_GOAL_COARSE_ALIGN &&
                mDriveControlState != DriveControlState.DRIVE_TOWARDS_GOAL_APPROACH &&
                mDriveControlState != DriveControlState.AIM_TO_GOAL) {
            mIsOnTarget = false;
            configureTalonsForPositionControl();
            mDriveControlState = DriveControlState.DRIVE_TOWARDS_GOAL_COARSE_ALIGN;
            updatePositionSetpoint(getLeftDistanceInches(), getRightDistanceInches());
            mTargetHeading = getGyroAngle();
        }
        setHighGear(false);
    }

    /**
     * Configures the drivebase to turn to a desired heading
     */
    public synchronized void setWantTurnToHeading(Rotation2d heading) {
        if (mDriveControlState != DriveControlState.TURN_TO_HEADING) {
            configureTalonsForPositionControl();
            mDriveControlState = DriveControlState.TURN_TO_HEADING;
            updatePositionSetpoint(getLeftDistanceInches(), getRightDistanceInches());
        }
        if (Math.abs(heading.inverse().rotateBy(mTargetHeading).getDegrees()) > 1E-3) {
            mTargetHeading = heading;
            mIsOnTarget = false;
        }
        setHighGear(false);
    }

    /**
     * Configures the drivebase to drive a path. Used for autonomous driving
     * 
     * @see Path
     */
    public synchronized void setWantDrivePath(Path path, boolean reversed) {
        if (mCurrentPath != path || mDriveControlState != DriveControlState.PATH_FOLLOWING) {
            configureTalonsForSpeedControl();
            RobotState.getInstance().resetDistanceDriven();
            mPathFollower = new PathFollower(path, reversed,
                    new PathFollower.Parameters(
                            new Lookahead(Constants.kMinLookAhead, Constants.kMaxLookAhead,
                                    Constants.kMinLookAheadSpeed, Constants.kMaxLookAheadSpeed),
                            Constants.kInertiaSteeringGain, Constants.kPathFollowingProfileKp,
                            Constants.kPathFollowingProfileKi, Constants.kPathFollowingProfileKv,
                            Constants.kPathFollowingProfileKffv, Constants.kPathFollowingProfileKffa,
                            Constants.kPathFollowingMaxVel, Constants.kPathFollowingMaxAccel,
                            Constants.kPathFollowingGoalPosTolerance, Constants.kPathFollowingGoalVelTolerance,
                            Constants.kPathStopSteeringDistance));
            mDriveControlState = DriveControlState.PATH_FOLLOWING;
            mCurrentPath = path;
        } else {
            setVelocitySetpoint(0, 0);
        }
    }

    public synchronized boolean isDoneWithPath() {
        if (mDriveControlState == DriveControlState.PATH_FOLLOWING && mPathFollower != null) {
            return mPathFollower.isFinished();
        } else {
            System.out.println("Robot is not in path following mode");
            return true;
        }
    }

    public synchronized void forceDoneWithPath() {
        if (mDriveControlState == DriveControlState.PATH_FOLLOWING && mPathFollower != null) {
            mPathFollower.forceFinish();
        } else {
            System.out.println("Robot is not in path following mode");
        }
    }

    public boolean isApproaching() {
        return mIsApproaching;
    }

    public synchronized boolean isDoneWithTurn() {
        if (mDriveControlState == DriveControlState.TURN_TO_HEADING) {
            return mIsOnTarget;
        } else {
            System.out.println("Robot is not in turn to heading mode");
            return false;
        }
    }

    public synchronized boolean hasPassedMarker(String marker) {
        if (mDriveControlState == DriveControlState.PATH_FOLLOWING && mPathFollower != null) {
            return mPathFollower.hasPassedMarker(marker);
        } else {
            System.out.println("Robot is not in path following mode");
            return false;
        }
    }

    public synchronized void reloadGains() {
        /*mLeftMaster.setPID(Constants.kDriveLowGearPositionKp, Constants.kDriveLowGearPositionKi,
                Constants.kDriveLowGearPositionKd, Constants.kDriveLowGearPositionKf,
                Constants.kDriveLowGearPositionIZone, Constants.kDriveLowGearPositionRampRate,
                kLowGearPositionControlSlot);
        mLeftMaster.setMotionMagicCruiseVelocity(Constants.kDriveLowGearMaxVelocity);
        mLeftMaster.setMotionMagicAcceleration(Constants.kDriveLowGearMaxAccel);
        mRightMaster.setPID(Constants.kDriveLowGearPositionKp, Constants.kDriveLowGearPositionKi,
                Constants.kDriveLowGearPositionKd, Constants.kDriveLowGearPositionKf,
                Constants.kDriveLowGearPositionIZone, Constants.kDriveLowGearPositionRampRate,
                kLowGearPositionControlSlot);
        mRightMaster.setMotionMagicCruiseVelocity(Constants.kDriveLowGearMaxVelocity);
        mRightMaster.setMotionMagicAcceleration(Constants.kDriveLowGearMaxAccel);
        mLeftMaster.setVoltageCompensationRampRate(Constants.kDriveVoltageCompensationRampRate);
        mRightMaster.setVoltageCompensationRampRate(Constants.kDriveVoltageCompensationRampRate);

        mLeftMaster.setPID(Constants.kDriveHighGearVelocityKp, Constants.kDriveHighGearVelocityKi,
                Constants.kDriveHighGearVelocityKd, Constants.kDriveHighGearVelocityKf,
                Constants.kDriveHighGearVelocityIZone, Constants.kDriveHighGearVelocityRampRate,
                kHighGearVelocityControlSlot);
        mRightMaster.setPID(Constants.kDriveHighGearVelocityKp, Constants.kDriveHighGearVelocityKi,
                Constants.kDriveHighGearVelocityKd, Constants.kDriveHighGearVelocityKf,
                Constants.kDriveHighGearVelocityIZone, Constants.kDriveHighGearVelocityRampRate,
                kHighGearVelocityControlSlot);
        mLeftMaster.setVoltageCompensationRampRate(Constants.kDriveVoltageCompensationRampRate);
        mRightMaster.setVoltageCompensationRampRate(Constants.kDriveVoltageCompensationRampRate);
        */
    	mLeftMaster.config_kD(kLowGearPositionControlSlot, Constants.kDriveLowGearPositionKd, Constants.kTimeoutMs);
    	mLeftMaster.config_kP(kLowGearPositionControlSlot, Constants.kDriveLowGearPositionKp, Constants.kTimeoutMs);
    	mLeftMaster.config_kI(kLowGearPositionControlSlot, Constants.kDriveLowGearPositionKi, Constants.kTimeoutMs);
    	mLeftMaster.config_kF(kLowGearPositionControlSlot, Constants.kDriveLowGearPositionKf, Constants.kTimeoutMs);
    	mLeftMaster.config_IntegralZone(kLowGearPositionControlSlot, Constants.kDriveLowGearPositionIZone, Constants.kTimeoutMs);
    	mLeftMaster.configClosedloopRamp(Constants.kDriveLowGearPositionRampRate, Constants.kTimeoutMs);
    	
    	mLeftMaster.config_kD(kHighGearVelocityControlSlot, Constants.kDriveHighGearVelocityKd, Constants.kTimeoutMs);
    	mLeftMaster.config_kP(kHighGearVelocityControlSlot, Constants.kDriveHighGearVelocityKp, Constants.kTimeoutMs);
    	mLeftMaster.config_kI(kHighGearVelocityControlSlot, Constants.kDriveHighGearVelocityKi, Constants.kTimeoutMs);
    	mLeftMaster.config_kF(kHighGearVelocityControlSlot, Constants.kDriveHighGearVelocityKf, Constants.kTimeoutMs);
    	mLeftMaster.config_IntegralZone(kHighGearVelocityControlSlot, Constants.kDriveHighGearVelocityIZone, Constants.kTimeoutMs);
    	//TODO not sure these ramp rates are done right.
    	mLeftMaster.configClosedloopRamp(Constants.kDriveHighGearVelocityRampRate, Constants.kTimeoutMs);
    	
    	mRightMaster.config_kD(kHighGearVelocityControlSlot, Constants.kDriveHighGearVelocityKd, Constants.kTimeoutMs);
    	mRightMaster.config_kP(kHighGearVelocityControlSlot, Constants.kDriveHighGearVelocityKp, Constants.kTimeoutMs);
    	mRightMaster.config_kI(kHighGearVelocityControlSlot, Constants.kDriveHighGearVelocityKi, Constants.kTimeoutMs);
    	mRightMaster.config_kF(kHighGearVelocityControlSlot, Constants.kDriveHighGearVelocityKf, Constants.kTimeoutMs);
    	mRightMaster.config_IntegralZone(kHighGearVelocityControlSlot, Constants.kDriveHighGearVelocityIZone, Constants.kTimeoutMs);
    	//TODO not sure these ramp rates are done right.
    	mRightMaster.configClosedloopRamp(Constants.kDriveHighGearVelocityRampRate, Constants.kTimeoutMs);
    	
    	
    	// this is in 
    	mLeftMaster.configMotionCruiseVelocity(Constants.kDriveLowGearMaxVelocity, Constants.kTimeoutMs);
    	mLeftMaster.configMotionAcceleration(Constants.kDriveLowGearMaxAccel, Constants.kTimeoutMs);
    	
    	mRightMaster.config_kD(kLowGearPositionControlSlot, Constants.kDriveLowGearPositionKd, Constants.kTimeoutMs);
    	mRightMaster.config_kP(kLowGearPositionControlSlot, Constants.kDriveLowGearPositionKp, Constants.kTimeoutMs);
    	mRightMaster.config_kI(kLowGearPositionControlSlot, Constants.kDriveLowGearPositionKi, Constants.kTimeoutMs);
    	mRightMaster.config_kF(kLowGearPositionControlSlot, Constants.kDriveLowGearPositionKf, Constants.kTimeoutMs);
    	mRightMaster.config_IntegralZone(kLowGearPositionControlSlot, Constants.kDriveLowGearPositionIZone, Constants.kTimeoutMs);
    	mRightMaster.configClosedloopRamp(Constants.kDriveLowGearPositionRampRate, Constants.kTimeoutMs);
    	// this is in 
    	mRightMaster.configMotionCruiseVelocity(Constants.kDriveLowGearMaxVelocity, Constants.kTimeoutMs);
    	mRightMaster.configMotionAcceleration(Constants.kDriveLowGearMaxAccel, Constants.kTimeoutMs);

    	//TODO not sure what the new syntax is for setVoltageCompensationRampRate
    	
    }

    public synchronized double getAccelX() {
        return mNavXBoard.getRawAccelX();
    }

    @Override
    public void writeToLog() {
        mCSVWriter.write();
    }

    public boolean checkSystem() {
        System.out.println("Testing DRIVE.---------------------------------");
        final double kCurrentThres = 0.5;
        final double kRpmThres = 300;

        /*mRightMaster.changeControlMode(CANTalon.TalonControlMode.Voltage);
        mRightSlave.changeControlMode(CANTalon.TalonControlMode.Voltage);
        mLeftMaster.changeControlMode(CANTalon.TalonControlMode.Voltage);
        mLeftSlave.changeControlMode(CANTalon.TalonControlMode.Voltage);

        mRightMaster.set(0.0);
        mRightSlave.set(0.0);
        mLeftMaster.set(0.0);
        mLeftSlave.set(0.0);
        */
        mRightMaster.set(ControlMode.PercentOutput, 0);
        mRightSlave.set(ControlMode.PercentOutput, 0);
        mLeftMaster.set(ControlMode.PercentOutput, 0);
        mLeftSlave.set(ControlMode.PercentOutput, 0);
        
        
        
        
        //mRightMaster.set(-6.0f);
        mRightMaster.set(ControlMode.PercentOutput,-6.0f);
        Timer.delay(4.0);
        final double currentRightMaster = mRightMaster.getOutputCurrent();
        //final double rpmRightMaster = mRightMaster.getSpeed();
        final double rpmRightMaster = mRightMaster.getSelectedSensorVelocity(Constants.kPidIdx);
        //mRightMaster.set(0.0f);
        mRightMaster.set(ControlMode.PercentOutput,0);
        Timer.delay(2.0);

        mRightSlave.set(ControlMode.PercentOutput,-6.0f);
        Timer.delay(4.0);
        final double currentRightSlave = mRightSlave.getOutputCurrent();
        final double rpmRightSlave = mRightMaster.getSelectedSensorVelocity(Constants.kPidIdx);
        mRightSlave.set(ControlMode.PercentOutput,0.0f);

        Timer.delay(2.0);

        mLeftMaster.set(ControlMode.PercentOutput,6.0f);
        Timer.delay(4.0);
        final double currentLeftMaster = mLeftMaster.getOutputCurrent();
        final double rpmLeftMaster = mLeftMaster.getSelectedSensorVelocity(Constants.kPidIdx);
        mLeftMaster.set(ControlMode.PercentOutput,0.0f);

        Timer.delay(2.0);

        mLeftSlave.set(ControlMode.PercentOutput,6.0f);
        Timer.delay(4.0);
        final double currentLeftSlave = mLeftSlave.getOutputCurrent();
        final double rpmLeftSlave = mLeftMaster.getSelectedSensorVelocity(Constants.kPidIdx);
        mLeftSlave.set(ControlMode.PercentOutput,0.0);


        mRightMaster.set(ControlMode.PercentOutput,0);
        mLeftMaster.set(ControlMode.PercentOutput,0);

        mRightSlave.set(ControlMode.Follower,Constants.kRightDriveMasterId);

 
        mLeftSlave.set(ControlMode.Follower,Constants.kLeftDriveMasterId);

        System.out.println("Drive Right Master Current: " + currentRightMaster + " Drive Right Slave Current: "
                + currentRightSlave);
        System.out.println(
                "Drive Left Master Current: " + currentLeftMaster + " Drive Left Slave Current: " + currentLeftSlave);
        System.out.println("Drive RPM RMaster: " + rpmRightMaster + " RSlave: " + rpmRightSlave + " LMaster: "
                + rpmLeftMaster + " LSlave: " + rpmLeftSlave);

        boolean failure = false;

        if (currentRightMaster < kCurrentThres) {
            failure = true;
            System.out.println("!!!!!!!!!!!!!!!!!! Drive Right Master Current Low !!!!!!!!!!");
        }

        if (currentRightSlave < kCurrentThres) {
            failure = true;
            System.out.println("!!!!!!!!!!!!!!!!!! Drive Right Slave Current Low !!!!!!!!!!");
        }

        if (currentLeftMaster < kCurrentThres) {
            failure = true;
            System.out.println("!!!!!!!!!!!!!!!!!! Drive Left Master Current Low !!!!!!!!!!");
        }

        if (currentLeftSlave < kCurrentThres) {
            failure = true;
            System.out.println("!!!!!!!!!!!!!!!!!! Drive Left Slave Current Low !!!!!!!!!!");
        }

        if (!Util.allCloseTo(Arrays.asList(currentRightMaster, currentRightSlave), currentRightMaster,
                5.0)) {
            failure = true;
            System.out.println("!!!!!!!!!!!!!!!!!! Drive Right Currents Different !!!!!!!!!!");
        }

        if (!Util.allCloseTo(Arrays.asList(currentLeftMaster, currentLeftSlave), currentLeftSlave,
                5.0)) {
            failure = true;
            System.out.println("!!!!!!!!!!!!!!!!!! Drive Left Currents Different !!!!!!!!!!!!!");
        }

        if (rpmRightMaster < kRpmThres) {
            failure = true;
            System.out.println("!!!!!!!!!!!!!!!!!! Drive Right Master RPM Low !!!!!!!!!!!!!!!!!!!");
        }

        if (rpmRightSlave < kRpmThres) {
            failure = true;
            System.out.println("!!!!!!!!!!!!!!!!!! Drive Right Slave RPM Low !!!!!!!!!!!!!!!!!!!");
        }

        if (rpmLeftMaster < kRpmThres) {
            failure = true;
            System.out.println("!!!!!!!!!!!!!!!!!! Drive Left Master RPM Low !!!!!!!!!!!!!!!!!!!");
        }

        if (rpmLeftSlave < kRpmThres) {
            failure = true;
            System.out.println("!!!!!!!!!!!!!!!!!! Drive Left Slave RPM Low !!!!!!!!!!!!!!!!!!!");
        }

        if (!Util.allCloseTo(Arrays.asList(rpmRightMaster, rpmRightSlave, rpmLeftMaster, rpmLeftSlave),
                rpmRightMaster, 250)) {
            failure = true;
            System.out.println("!!!!!!!!!!!!!!!!!!! Drive RPMs different !!!!!!!!!!!!!!!!!!!");
        }

        return !failure;
    }
}
