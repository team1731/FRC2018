package org.usfirst.frc.team1731.robot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.usfirst.frc.team1731.lib.util.CheesyDriveHelper;
import org.usfirst.frc.team1731.lib.util.CrashTracker;
import org.usfirst.frc.team1731.lib.util.DelayedBoolean;
import org.usfirst.frc.team1731.lib.util.DriveSignal;
import org.usfirst.frc.team1731.lib.util.InterpolatingDouble;
import org.usfirst.frc.team1731.lib.util.InterpolatingTreeMap;
import org.usfirst.frc.team1731.lib.util.LatchedBoolean;
import org.usfirst.frc.team1731.lib.util.math.RigidTransform2d;
import org.usfirst.frc.team1731.robot.auto.AutoModeBase;
import org.usfirst.frc.team1731.robot.auto.AutoModeExecuter;
import org.usfirst.frc.team1731.robot.auto.modes.AutoDetectAllianceSwitchThenPlaceMode;
import org.usfirst.frc.team1731.robot.auto.modes.LeftPutCubeOnLeftScale;
import org.usfirst.frc.team1731.robot.auto.modes.LeftPutCubeOnLeftScaleAndLeftSwitch;
import org.usfirst.frc.team1731.robot.auto.modes.LeftPutCubeOnRightScaleAndRightSwitch;
import org.usfirst.frc.team1731.robot.auto.modes.MiddleRightPutCubeOnLeftSwitch;
import org.usfirst.frc.team1731.robot.auto.modes.MiddleRightPutCubeOnRightSwitch;
import org.usfirst.frc.team1731.robot.auto.modes.RightPut3CubesOnLeftScale;
import org.usfirst.frc.team1731.robot.auto.modes.RightPutCubeOnLeftScale;
import org.usfirst.frc.team1731.robot.auto.modes.RightPutCubeOnLeftScaleAndLeftSwitch;
import org.usfirst.frc.team1731.robot.auto.modes.RightPut2CubesOnRightScale;
import org.usfirst.frc.team1731.robot.auto.modes.RightPutCubeOnRightScale;
import org.usfirst.frc.team1731.robot.auto.modes.StandStillMode;
import org.usfirst.frc.team1731.robot.auto.modes.TestAuto;
import org.usfirst.frc.team1731.robot.auto.modes._new.LeftDriveForward;
import org.usfirst.frc.team1731.robot.auto.modes._new.LeftPut1LeftScale1RightSwitchEnd;
import org.usfirst.frc.team1731.robot.auto.modes._new.LeftPut1LeftScaleEnd;
import org.usfirst.frc.team1731.robot.auto.modes._new.LeftPut1LeftScaleEnd1RightSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new.LeftPut1LeftSwitchEnd1LeftScaleEnd;
import org.usfirst.frc.team1731.robot.auto.modes._new.LeftPut1LeftSwitchEnd1LeftSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new.LeftPut1LeftSwitchEnd1RightScale;
import org.usfirst.frc.team1731.robot.auto.modes._new.LeftPut1RightScale2RightSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new.LeftPut2LeftScale1LeftSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new.LeftPut2LeftScale1RightSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new.LeftPut2LeftScaleEnd;
import org.usfirst.frc.team1731.robot.auto.modes._new.LeftPut2RightScale1RightSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new.LeftPut3LeftScale;
import org.usfirst.frc.team1731.robot.auto.modes._new.LeftPut3RightScale;
import org.usfirst.frc.team1731.robot.auto.modes._new.MiddleDriveForward;
import org.usfirst.frc.team1731.robot.auto.modes._new.MiddlePut1LeftSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new.MiddlePut1LeftSwitch1Exchange;
import org.usfirst.frc.team1731.robot.auto.modes._new.MiddlePut1RightSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new.MiddlePut1RightSwitch1Exchange;
import org.usfirst.frc.team1731.robot.auto.modes._new.MiddlePut2LeftSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new.MiddlePut2RightSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new.RightDriveForward;
import org.usfirst.frc.team1731.robot.auto.modes._new.RightPut1LeftScale2LeftSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new.RightPut1RightScale1LeftSwitchEnd;
import org.usfirst.frc.team1731.robot.auto.modes._new.RightPut1RightScaleEnd;
import org.usfirst.frc.team1731.robot.auto.modes._new.RightPut1RightScaleEnd1LeftSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new.RightPut1RightSwitchEnd1LeftScale;
import org.usfirst.frc.team1731.robot.auto.modes._new.RightPut1RightSwitchEnd1RightScaleEnd;
import org.usfirst.frc.team1731.robot.auto.modes._new.RightPut1RightSwitchEnd1RightSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new.RightPut2LeftScale1LeftSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new.RightPut2RightScale1LeftSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new.RightPut2RightScale1RightSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new.RightPut2RightScaleEnd;
import org.usfirst.frc.team1731.robot.auto.modes._new.RightPut3LeftScale;
import org.usfirst.frc.team1731.robot.auto.modes._new.RightPut3RightScale;
import org.usfirst.frc.team1731.robot.loops.Looper;
import org.usfirst.frc.team1731.robot.loops.RobotStateEstimator;
import org.usfirst.frc.team1731.robot.loops.VisionProcessor;
import org.usfirst.frc.team1731.robot.paths.DriveForward;
import org.usfirst.frc.team1731.robot.paths.profiles.PathAdapter;
import org.usfirst.frc.team1731.robot.subsystems.ConnectionMonitor;
import org.usfirst.frc.team1731.robot.subsystems.Drive;
import org.usfirst.frc.team1731.robot.subsystems.Elevator;
import org.usfirst.frc.team1731.robot.subsystems.Climber;
import org.usfirst.frc.team1731.robot.subsystems.Intake;
import org.usfirst.frc.team1731.robot.subsystems.LED;
import org.usfirst.frc.team1731.robot.subsystems.Superstructure;
import org.usfirst.frc.team1731.robot.vision.VisionServer;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The main robot class, which instantiates all robot parts and helper classes and initializes all loops. Some classes
 * are already instantiated upon robot startup; for those classes, the robot gets the instance as opposed to creating a
 * new object
 * 
 * After initializing all robot parts, the code sets up the autonomous and teleoperated cycles and also code that runs
 * periodically inside both routines.
 * 
 * This is the nexus/converging point of the robot code and the best place to start exploring.
 * 
 * The VM is configured to automatically run this class, and to call the functions corresponding to each mode, as
 * described in the IterativeRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the manifest file in the resource directory.
 */
public class Robot extends IterativeRobot {
	
	public static enum AutoScheme { 
		OLD_SCHEME, // Haymarket, Alexandria
		NEW_SCHEME  // Maryland, Detroit
	};
	public static AutoScheme CHOSEN_AUTO_SCHEME = AutoScheme.NEW_SCHEME; // or, AutoScheme.OLD_SCHEME
	
	private static String autoCode; // JUSTIN types-in 4 numbers
	
    private static Map<Integer, AutoModeBase> AUTO_MODES; // 35 modes defined in Mark's "BIBLE"
	static {
		initAutoModes();
	}
	
	public static String getGameDataFromField() {     // "LLR" for example
        String gameData = DriverStation.getInstance().getGameSpecificMessage().trim();
        int retries = 100;
          	
        while (gameData.length() < 2 && retries > 0) {
            retries--;
            try {
                Thread.sleep(5);
            } catch (InterruptedException ie) {
                // Just ignore the interrupted exception
            }
            gameData = DriverStation.getInstance().getGameSpecificMessage().trim();
        }
        return gameData;
	}
	

	
	// Get subsystem instances
    private Drive mDrive = Drive.getInstance();
    private Superstructure mSuperstructure = Superstructure.getInstance();
    private LED mLED = LED.getInstance();
    private RobotState mRobotState = RobotState.getInstance();
    private AutoModeExecuter mAutoModeExecuter = null;
//    private Command autonomousCommand;
    private AutoModeBase autoModeToExecute;
    private SendableChooser autoChooser;
    
    private SendableChooser startingPosition;
    private SendableChooser areTeammatesCool;
    private enum startingPositions {
    	LEFT,
 //   	MIDDLELEFT,
    	MIDDLERIGHT,
    	RIGHT
    };

    private final SubsystemManager mSubsystemManager = new SubsystemManager(
                            Arrays.asList(Drive.getInstance(), Superstructure.getInstance(),
                                    Elevator.getInstance(), Intake.getInstance(), Climber.getInstance(),
                                    ConnectionMonitor.getInstance(), LED.getInstance() ));

    // Initialize other helper objects
    private CheesyDriveHelper mCheesyDriveHelper = new CheesyDriveHelper();
    private ControlBoardInterface mControlBoard = GamepadControlBoard.getInstance();

    private Looper mEnabledLooper = new Looper();

    //private VisionServer mVisionServer = VisionServer.getInstance();

    private AnalogInput mCheckLightButton = new AnalogInput(Constants.kLEDOnId);

    //private DelayedBoolean mDelayedAimButton;

    private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> mTuningFlywheelMap = new InterpolatingTreeMap<>();

    public Robot() {
        CrashTracker.logRobotConstruction();
    }

    public void zeroAllSensors() {
        mSubsystemManager.zeroSensors();
        mRobotState.reset(Timer.getFPGATimestamp(), new RigidTransform2d());
        mDrive.zeroSensors();
    }

    /**
     * This function is run when the robot is first started up and should be used for any initialization code.
     */
    @Override
    public void robotInit() {
        try {
            CrashTracker.logRobotInit();

            mSubsystemManager.registerEnabledLoops(mEnabledLooper);
          //  mEnabledLooper.register(VisionProcessor.getInstance());
            mEnabledLooper.register(RobotStateEstimator.getInstance());

            //mVisionServer.addVisionUpdateReceiver(VisionProcessor.getInstance());
            
            
            //http://roborio-1731-frc.local:1181/?action=stream
            //   /CameraPublisher/<camera name>/streams=["mjpeg:http://roborio-1731-frc.local:1181/?action=stream", "mjpeg:http://10.17.31.2:1181/?action=stream"]
            CameraServer.getInstance().startAutomaticCapture(0);
            
            
            switch(CHOSEN_AUTO_SCHEME) {
            
            case OLD_SCHEME: // Haymarket, Alexandria
                autoChooser = new SendableChooser();
                autoChooser.addDefault("Score Cubes", "ScoreCubes");
                autoChooser.addObject("Drive and do nothing", "DriveOnly");
                autoChooser.addObject("Do Nothing", new StandStillMode());
                autoChooser.addObject("Test", new TestAuto());
                autoChooser.addObject("3 on Right Scale", new RightPutCubeOnRightScale());
                autoChooser.addObject("3 on Left Scale", new RightPut3CubesOnLeftScale());
                SmartDashboard.putData("Autonomous Mode", autoChooser);
               
                startingPosition = new SendableChooser();
                startingPosition.addDefault("Left Position", startingPositions.LEFT);
//                startingPosition.addObject("Middle-Left Position", startingPositions.MIDDLELEFT);
                startingPosition.addObject("Middle-Right Position", startingPositions.MIDDLERIGHT);
                startingPosition.addObject("Right Position", startingPositions.RIGHT);
                SmartDashboard.putData("Starting Position", startingPosition);
                
                areTeammatesCool = new SendableChooser();
                areTeammatesCool.addDefault("Be cautious (A)", false);
                areTeammatesCool.addObject("It's fine (B)", true);
                SmartDashboard.putData("How should I react?", areTeammatesCool);
                
            	break;
            	
            case NEW_SCHEME: // Maryland, Detroit             //LL LR RL RR
            	autoCode = SmartDashboard.getString("AutoCode", "3  8 12 15");// JUSTIN's numbers
            	break;
            }
            
          //  mDelayedAimButton = new DelayedBoolean(Timer.getFPGATimestamp(), 0.1);
            // Force an true update now to prevent robot from running at start.
          //  mDelayedAimButton.update(Timer.getFPGATimestamp(), true);

            // Pre calculate the paths we use for auto.
            PathAdapter.calculatePaths();

        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
        zeroAllSensors();
    }

    /**
     * Initializes the robot for the beginning of autonomous mode (set drivebase, intake and superstructure to correct
     * states). Then gets the correct auto mode from the AutoModeSelector
     * 
     * @see AutoModeSelector.java
     */
    @Override
    public void autonomousInit() {
        try {
            CrashTracker.logAutoInit();

            System.out.println("Auto start timestamp: " + Timer.getFPGATimestamp());

            if (mAutoModeExecuter != null) {
                mAutoModeExecuter.stop();
            }

            zeroAllSensors();
            mSuperstructure.setWantedState(Superstructure.WantedState.IDLE);
            mSuperstructure.setOverrideCompressor(true);

            mAutoModeExecuter = null;

 //           Intake.getInstance().reset();

            // Shift to high
            mDrive.setHighGear(true);
            mDrive.setBrakeMode(true);

            mEnabledLooper.start();
            mSuperstructure.reloadConstants();
            
            switch(CHOSEN_AUTO_SCHEME) {
            
            case OLD_SCHEME: // Haymarket, Alexandria

	            if (autoChooser.getSelected().equals("ScoreCubes")) {
	            	autoModeToExecute = AutoDetectAllianceSwitchThenPlaceMode.pickAutoMode(
	            			(AutoDetectAllianceSwitchThenPlaceMode.startingPositions.valueOf(startingPosition.getSelected().toString())),
	            			(boolean) areTeammatesCool.getSelected());
	            
	            } else if(autoChooser.getSelected().equals("DriveOnly")) {
	            	autoModeToExecute = AutoDetectAllianceSwitchThenPlaceMode.intenseTrust(
	            			AutoDetectAllianceSwitchThenPlaceMode.startingPositions.valueOf(startingPosition.getSelected().toString()));
	            } else 
	            	autoModeToExecute = (AutoModeBase) autoChooser.getSelected();
	            
	  //          mAutoModeExecuter.setAutoMode(AutoModeSelector.getSelectedAutoMode());
	            break;
	            
            case NEW_SCHEME: // Maryland, Detroit
            	
            	String gameData = Robot.getGameDataFromField(); //RRL for example
                autoModeToExecute = determineAutoModeToExecute(gameData);
            	break;
            }
            
            mAutoModeExecuter = new AutoModeExecuter();
            mAutoModeExecuter.setAutoMode(autoModeToExecute);
            mAutoModeExecuter.start();
            
            //WPILIB WAY TO GET AUTONOMOUS MODE...
            //
            //
            //autonomousCommand = (Command) autoChooser.getSelected();
            //autonomousCommand.start();
            

        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }
                                                  // RRL for example
    private AutoModeBase determineAutoModeToExecute(String gameData) {
    	
    	                                         //LL LR RL RR
    	String[] autoCodes = autoCode.split(" ");//"3  8 12 15" for example
    	String LLcode = autoCodes[0];
    	String LRcode = autoCodes[1];
    	String RLcode = autoCodes[2];
    	String RRcode = autoCodes[3];
    	
        AutoModeBase selectedAutoMode = null;

        switch(gameData.substring(0, 2)) {
        case "LL":
        	selectedAutoMode = lookupMode(LLcode);
        	break;
        case "LR":
        	selectedAutoMode = lookupMode(LRcode);
        	break;
        case "RL":
        	selectedAutoMode = lookupMode(RLcode);
        	break;
        case "RR":
        	selectedAutoMode = lookupMode(RRcode);
        	break;
        }
        
        System.out.println("running auto mode: " + selectedAutoMode);
        
		return selectedAutoMode;
	}

    private static void initAutoModes() {
    	AUTO_MODES = new HashMap<Integer, AutoModeBase>();//THESE ARE FROM MARK'S "BIBLE"
        AUTO_MODES.put(2,  /*   Far SC X3 				 */ new RightPut3LeftScale());
        AUTO_MODES.put(3,  /* 	Far SC-Far SW-Far SC 	 */ new RightPut2LeftScale1LeftSwitch());
        AUTO_MODES.put(5,  /* 	Drive Forward 			 */ new RightDriveForward());
        AUTO_MODES.put(7,  /* 	SC x3 					 */ new RightPut3RightScale());
        AUTO_MODES.put(8,  /* 	SC-Far SW 				 */ new RightPut1RightScale1LeftSwitchEnd());
        AUTO_MODES.put(9,  /* 	SC x2-Far SW 			 */ new RightPut2RightScale1LeftSwitch());
        AUTO_MODES.put(11, /* 	SW x2 					 */ new RightPut1RightSwitchEnd1RightSwitch());
        AUTO_MODES.put(12, /* 	SW- Far SC 				 */ new RightPut1RightSwitchEnd1LeftScale());
        AUTO_MODES.put(15, /* 	SC - SW - SC	 		 */ new RightPut2RightScale1RightSwitch());
        AUTO_MODES.put(19, /* 	Far SC X3		 		 */ new LeftPut3RightScale());
        AUTO_MODES.put(20, /* 	Far SC - Far SW - Far SC */ new LeftPut2RightScale1RightSwitch());
        AUTO_MODES.put(23, /* 	SC x3					 */ new LeftPut3LeftScale());
        AUTO_MODES.put(24, /* 	SC - Far SW				 */ new LeftPut1LeftScale1RightSwitchEnd());
        AUTO_MODES.put(25, /* 	SC X2 - Far SW			 */ new LeftPut2LeftScale1RightSwitch());
        AUTO_MODES.put(27, /* 	SW x2					 */ new LeftPut1LeftSwitchEnd1LeftSwitch());
        AUTO_MODES.put(28, /* 	SW - Far SC				 */ new LeftPut1LeftSwitchEnd1RightScale());
        AUTO_MODES.put(31, /* 	SC - SW - SC			 */ new LeftPut2LeftScale1LeftSwitch());
        AUTO_MODES.put(34, /* 	SW						 */ new MiddlePut1LeftSwitch());
        AUTO_MODES.put(35, /* 	SW x2					 */ new MiddlePut2LeftSwitch());
        AUTO_MODES.put(36, /* 	SW - EX					 */ new MiddlePut1LeftSwitch1Exchange());
        AUTO_MODES.put(37, /* 	SW						 */ new MiddlePut1RightSwitch());
        AUTO_MODES.put(38, /* 	SW x2					 */ new MiddlePut2RightSwitch());
        AUTO_MODES.put(39, /* 	SW - EX					 */ new MiddlePut1RightSwitch1Exchange());
        AUTO_MODES.put(40, /* 	Far SC-Far SW x2		 */ new RightPut1LeftScale2LeftSwitch());
        AUTO_MODES.put(41, /* 	SC End					 */ new RightPut1RightScaleEnd());
        AUTO_MODES.put(42, /* 	SC End-Far SW			 */ new RightPut1RightScaleEnd1LeftSwitch());
        AUTO_MODES.put(43, /* 	SC End x2				 */ new RightPut2RightScaleEnd());
        AUTO_MODES.put(46, /* 	SW - SC End		 		 */ new RightPut1RightSwitchEnd1RightScaleEnd());
        AUTO_MODES.put(47, /* 	Drive FWD				 */ new MiddleDriveForward());
        AUTO_MODES.put(48, /* 	SC End x2				 */ new LeftPut2LeftScaleEnd());
        AUTO_MODES.put(49, /* 	SC End					 */ new LeftPut1LeftScaleEnd());
        AUTO_MODES.put(50, /* 	SW - SC End				 */ new LeftPut1LeftSwitchEnd1LeftScaleEnd());
        AUTO_MODES.put(52, /* 	SC End - Far SW			 */ new LeftPut1LeftScaleEnd1RightSwitch());
        AUTO_MODES.put(54, /* 	Far SC - Far SW X2		 */ new LeftPut1RightScale2RightSwitch());
        AUTO_MODES.put(55, /* 	Drive Forward			 */ new LeftDriveForward());
    }
    
	private AutoModeBase lookupMode(String autoCode) {
		int code = Integer.parseInt(autoCode);
		AutoModeBase mode = AUTO_MODES.get(code);
		return mode == null ? new StandStillMode() : mode;
	}

	/**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
        allPeriodic();
        
        //WPILIB WAY TO RUN AUTONOMOUS...
        //
        //
        //Scheduler.getInstance().run();
    }

    /**
     * Initializes the robot for the beginning of teleop
     */
    @Override
    public void teleopInit() {
        try {
            CrashTracker.logTeleopInit();

            // Start loopers
            mEnabledLooper.start();
            mDrive.setOpenLoop(DriveSignal.NEUTRAL);
            mDrive.setBrakeMode(false);
            // Shift to high
            mDrive.setHighGear(true);
            zeroAllSensors();
            mSuperstructure.reloadConstants();
            mSuperstructure.setOverrideCompressor(false);
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    /**
     * This function is called periodically during operator control.
     * 
     * The code uses state machines to ensure that no matter what buttons the driver presses, the robot behaves in a
     * safe and consistent manner.
     * 
     * Based on driver input, the code sets a desired state for each subsystem. Each subsystem will constantly compare
     * its desired and actual states and act to bring the two closer.
     */
    @Override
    public void teleopPeriodic() {
        try {
            double timestamp = Timer.getFPGATimestamp();
                
            boolean climbUp = mControlBoard.getClimbUp();
            boolean climbDown = mControlBoard.getClimbDown();
            boolean overTheTop = mControlBoard.getOverTheTopButton();
            boolean grabCube = mControlBoard.getGrabCubeButton();
            boolean calibrateDown = mControlBoard.getCalibrateDown();
            boolean calibrateUp = mControlBoard.getCalibrateUp();
            boolean spitting = mControlBoard.getSpit();
            boolean pickUp = mControlBoard.getAutoPickUp();
            
            if (mControlBoard.getElevatorButton()) {
                if (overTheTop) {
                    mSuperstructure.setOverTheTop(true);
                }
                else {
                    mSuperstructure.setOverTheTop(false);
                }
                mSuperstructure.setWantedElevatorPosition(-1 * mControlBoard.getElevatorControl());
            } else {
                mSuperstructure.setWantedElevatorPosition(0);
            }

            if (climbUp) {
            	mSuperstructure.setWantedState(Superstructure.WantedState.CLIMBINGUP);
            } else if (climbDown) {
            	mSuperstructure.setWantedState(Superstructure.WantedState.CLIMBINGDOWN);
            } else if (grabCube) {
            	mSuperstructure.setWantedState(Superstructure.WantedState.INTAKING);
            } else if (spitting) {
            	mSuperstructure.setWantedState(Superstructure.WantedState.SPITTING);
            } else if (calibrateDown) {
            	mSuperstructure.setWantedState(Superstructure.WantedState.CALIBRATINGDOWN);
            } else if (calibrateUp) {
            	mSuperstructure.setWantedState(Superstructure.WantedState.CALIBRATINGUP);
            } else if (pickUp) {
            	mSuperstructure.setWantedState(Superstructure.WantedState.AUTOINTAKING);
            } else if (overTheTop) {
            	//mSuperstructure.setWantedState(Superstructure.WantedState.OVERTHETOP);
            } else {
            	mSuperstructure.setWantedState(Superstructure.WantedState.ELEVATOR_TRACKING);
            }
            	


            // Drive base
            double throttle = mControlBoard.getThrottle();
            double turn = mControlBoard.getTurn();
            
            mDrive.setOpenLoop(mCheesyDriveHelper.cheesyDrive(throttle, turn, mControlBoard.getQuickTurn(),
                    !mControlBoard.getLowGear()));
            boolean wantLowGear = mControlBoard.getLowGear();
            mDrive.setHighGear(!wantLowGear);
            
            

            allPeriodic();
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    @Override
    public void disabledInit() {
        try {
            CrashTracker.logDisabledInit();

            if (mAutoModeExecuter != null) {
                mAutoModeExecuter.stop();
            }
            mAutoModeExecuter = null;

            mEnabledLooper.stop();

            // Call stop on all our Subsystems.
            mSubsystemManager.stop();

            mDrive.setOpenLoop(DriveSignal.NEUTRAL);

            PathAdapter.calculatePaths();

            // If are tuning, dump map so far.
            if (Constants.kIsShooterTuning) {
                for (Map.Entry<InterpolatingDouble, InterpolatingDouble> entry : mTuningFlywheelMap.entrySet()) {
                    System.out.println("{" +
                            entry.getKey().value + ", " + entry.getValue().value + "},");
                }
            }
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    @Override
    public void disabledPeriodic() {
        final double kVoltageThreshold = 0.15;
        if (mCheckLightButton.getAverageVoltage() < kVoltageThreshold) {
            mLED.setLEDOn();
        } else {
            mLED.setLEDOff();
        }

        zeroAllSensors();
        allPeriodic();
    }

    @Override
    public void testInit() {
        Timer.delay(0.5);

        boolean results = Elevator.getInstance().checkSystem();
        results &= Drive.getInstance().checkSystem();
        results &= Intake.getInstance().checkSystem();


        if (!results) {
            System.out.println("CHECK ABOVE OUTPUT SOME SYSTEMS FAILED!!!");
        } else {
            System.out.println("ALL SYSTEMS PASSED");
        }
    }

    @Override
    public void testPeriodic() {
    }

    /**
     * Helper function that is called in all periodic functions
     */
    public void allPeriodic() {
        mRobotState.outputToSmartDashboard();
        mSubsystemManager.outputToSmartDashboard();
        mSubsystemManager.writeToLog();
        mEnabledLooper.outputToSmartDashboard();
        //SmartDashboard.putBoolean("camera_connected", mVisionServer.isConnected());

        ConnectionMonitor.getInstance().setLastPacketTime(Timer.getFPGATimestamp());
    }
}
