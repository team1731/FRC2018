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
import org.usfirst.frc.team1731.robot.Constants.GRABBER_POSITION;
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
import org.usfirst.frc.team1731.robot.auto.modes._new._55_LeftDriveForward;
import org.usfirst.frc.team1731.robot.auto.modes._new._56_RightPut1Exchange;
import org.usfirst.frc.team1731.robot.auto.modes._new._57_RightPut2Exchange;
import org.usfirst.frc.team1731.robot.auto.modes._new._58_LeftPut1Exchange;
import org.usfirst.frc.team1731.robot.auto.modes._new._59_LeftPut2Exchange;
import org.usfirst.frc.team1731.robot.auto.modes._new._60_RightPut3LeftScale;
import org.usfirst.frc.team1731.robot.auto.modes._new._61_RightPut3RightScaleChamps;
import org.usfirst.frc.team1731.robot.auto.modes._new._24_LeftPut1LeftScale1RightSwitchEnd;
import org.usfirst.frc.team1731.robot.auto.modes._new._49_LeftPut1LeftScaleEnd;
import org.usfirst.frc.team1731.robot.auto.modes._new._52_LeftPut1LeftScaleEnd1RightSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new._50_LeftPut1LeftSwitchEnd1LeftScaleEnd;
import org.usfirst.frc.team1731.robot.auto.modes._new._27_LeftPut1LeftSwitchEnd1LeftSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new._28_LeftPut1LeftSwitchEnd1RightScale;
import org.usfirst.frc.team1731.robot.auto.modes._new._54_LeftPut1RightScale2RightSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new._31_LeftPut2LeftScale1LeftSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new._25_LeftPut2LeftScale1RightSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new._48_LeftPut2LeftScaleEnd;
import org.usfirst.frc.team1731.robot.auto.modes._new._20_LeftPut2RightScale1RightSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new._23_LeftPut3LeftScale;
import org.usfirst.frc.team1731.robot.auto.modes._new._19_LeftPut3RightScale;
import org.usfirst.frc.team1731.robot.auto.modes._new._47_MiddleDriveForward;
import org.usfirst.frc.team1731.robot.auto.modes._new._34_MiddlePut1LeftSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new._36_MiddlePut1LeftSwitch1Exchange;
import org.usfirst.frc.team1731.robot.auto.modes._new._37_MiddlePut1RightSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new._39_MiddlePut1RightSwitch1Exchange;
import org.usfirst.frc.team1731.robot.auto.modes._new._35_MiddlePut2LeftSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new._38_MiddlePut2RightSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new._05_RightDriveForward;
import org.usfirst.frc.team1731.robot.auto.modes._new._40_RightPut1LeftScale2LeftSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new._08_RightPut1RightScale1LeftSwitchEnd;
import org.usfirst.frc.team1731.robot.auto.modes._new._41_RightPut1RightScaleEnd;
import org.usfirst.frc.team1731.robot.auto.modes._new._42_RightPut1RightScaleEnd1LeftSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new._12_RightPut1RightSwitchEnd1LeftScale;
import org.usfirst.frc.team1731.robot.auto.modes._new._46_RightPut1RightSwitchEnd1RightScaleEnd;
import org.usfirst.frc.team1731.robot.auto.modes._new._11_RightPut1RightSwitchEnd1RightSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new._03_RightPut2LeftScale1LeftSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new._09_RightPut2RightScale1LeftSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new._15_RightPut2RightScale1RightSwitch;
import org.usfirst.frc.team1731.robot.auto.modes._new._43_RightPut2RightScaleEnd;
import org.usfirst.frc.team1731.robot.auto.modes._new._00_DO_NOTHING;
import org.usfirst.frc.team1731.robot.auto.modes._new._02_RightPut3LeftScale;
import org.usfirst.frc.team1731.robot.auto.modes._new._07_RightPut3RightScale;
import org.usfirst.frc.team1731.robot.loops.Looper;
import org.usfirst.frc.team1731.robot.loops.RobotStateEstimator;
import org.usfirst.frc.team1731.robot.loops.VisionProcessor;
import org.usfirst.frc.team1731.robot.paths.DriveForward;
import org.usfirst.frc.team1731.robot.paths.profiles.PathAdapter;
import org.usfirst.frc.team1731.robot.subsystems.ConnectionMonitor;
import org.usfirst.frc.team1731.robot.subsystems.Drive;
import org.usfirst.frc.team1731.robot.subsystems.Elevator;
import org.usfirst.frc.team1731.robot.subsystems.FishingPole;
import org.usfirst.frc.team1731.robot.subsystems.Climber;
import org.usfirst.frc.team1731.robot.subsystems.Intake;
import org.usfirst.frc.team1731.robot.subsystems.LED;
import org.usfirst.frc.team1731.robot.subsystems.Superstructure;
import org.usfirst.frc.team1731.robot.vision.VisionServer;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Solenoid;
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
	}
	public static AutoScheme CHOSEN_AUTO_SCHEME = AutoScheme.NEW_SCHEME; // or, AutoScheme.OLD_SCHEME
		
	private static final String AUTO_CODES = "AutoCodes";
    private static Map<Integer, AutoModeBase> AUTO_MODES; // 35 modes defined in Mark's "BIBLE"
    private static Map<String, String[]> ALLOWABLE_AUTO_MODES; //  as defined in Mark's "BIBLE"
    
	static {
		initAutoModes();
        initAllowableAutoModes();
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
        if(gameData.length() < 2) {
        	gameData = "LR";
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
                                    Elevator.getInstance(), Intake.getInstance(), Climber.getInstance(), FishingPole.getInstance(),
                                    ConnectionMonitor.getInstance(), LED.getInstance() ));

    // Initialize other helper objects
    private CheesyDriveHelper mCheesyDriveHelper = new CheesyDriveHelper();
    private ControlBoardInterface mControlBoard = GamepadControlBoard.getInstance();

    private Looper mEnabledLooper = new Looper();

    //private VisionServer mVisionServer = VisionServer.getInstance();

    private AnalogInput mCheckLightButton = new AnalogInput(Constants.kLEDOnId);

    //private DelayedBoolean mDelayedAimButton;

    private InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> mTuningFlywheelMap = new InterpolatingTreeMap<>();

	private static Solenoid _24vSolenoid = Constants.makeSolenoidForId(11, 2);

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
            	SmartDashboard.putString(AUTO_CODES, "3 7 2 15");
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
            	String autoCodes = SmartDashboard.getString("AutoCodes", "3 7 2 15");// JUSTIN's numbers
            	System.out.println("Stephen: There's no logical reason for it to be doing this! The codes being sent are "+autoCodes+", but we put in "+SmartDashboard.getString("AutoCodes", "CODES NOT FOUND, USING DEFAULTS"));
                autoModeToExecute = determineAutoModeToExecute(gameData, autoCodes);
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
    private AutoModeBase determineAutoModeToExecute(String gameData, String autoCodes) {
    	System.out.println("Got field configuration: " + gameData);
    	System.out.println("Got these auto modes from the dashboard: " + autoCodes);
    	                                         //LL LR RL RR
    	String[] autoCodeArray = autoCodes.split(" ");//"3  8 12 15" for example
    	String LLcode = "3";
    	String LRcode = "7";
    	String RLcode = "2";
    	String RRcode = "15";
    	if(autoCodeArray.length == 4) {
        	LLcode = autoCodeArray[0];
        	LRcode = autoCodeArray[1];
        	RLcode = autoCodeArray[2];
        	RRcode = autoCodeArray[3];
    	}
    	
        AutoModeBase selectedAutoMode = null;
        String fieldSetup = gameData.substring(0, 2);//"RR" for example
        switch(fieldSetup) {
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
        
        selectedAutoMode = performSanityCheck(selectedAutoMode, fieldSetup);
        
        System.out.println("running auto mode: " + selectedAutoMode);
        
		return selectedAutoMode;
	}

    private AutoModeBase performSanityCheck(AutoModeBase selectedAutoMode, String fieldSetup) {
    	String humanSelectedAutoModeName = selectedAutoMode.getClass().getSimpleName();
    	String[] allowedAutoModeNamesForThisFieldSetup = ALLOWABLE_AUTO_MODES.get(fieldSetup);
    	if(!Arrays.asList(allowedAutoModeNamesForThisFieldSetup).contains(humanSelectedAutoModeName)) {
    		return new _00_DO_NOTHING();
    	}
		return selectedAutoMode;
	}

	private static void initAutoModes() {
    	AUTO_MODES = new HashMap<Integer, AutoModeBase>();//THESE ARE FROM MARK'S "BIBLE"
        AUTO_MODES.put(2,  /*   Far SC X3 				 */ new _02_RightPut3LeftScale());
        AUTO_MODES.put(3,  /* 	Far SC-Far SW-Far SC 	 */ new _03_RightPut2LeftScale1LeftSwitch());
        AUTO_MODES.put(5,  /* 	Drive Forward 			 */ new _05_RightDriveForward());
        AUTO_MODES.put(7,  /* 	SC x3 					 */ new _07_RightPut3RightScale());
        AUTO_MODES.put(8,  /* 	SC-Far SW 				 */ new _08_RightPut1RightScale1LeftSwitchEnd());
        AUTO_MODES.put(9,  /* 	SC x2-Far SW 			 */ new _00_DO_NOTHING()); //new _09_RightPut2RightScale1LeftSwitch());
        AUTO_MODES.put(11, /* 	SW x2 					 */ new _11_RightPut1RightSwitchEnd1RightSwitch());
        AUTO_MODES.put(12, /* 	SW- Far SC 				 */ new _12_RightPut1RightSwitchEnd1LeftScale());
        AUTO_MODES.put(15, /* 	SC - SW - SC	 		 */ new _15_RightPut2RightScale1RightSwitch());
        AUTO_MODES.put(19, /* 	Far SC X3		 		 */ new _19_LeftPut3RightScale());
        AUTO_MODES.put(20, /* 	Far SC - Far SW - Far SC */ new _20_LeftPut2RightScale1RightSwitch());
        AUTO_MODES.put(23, /* 	SC x3					 */ new _23_LeftPut3LeftScale());
        AUTO_MODES.put(24, /* 	SC - Far SW				 */ new _24_LeftPut1LeftScale1RightSwitchEnd());
        AUTO_MODES.put(25, /* 	SC X2 - Far SW			 */ new _00_DO_NOTHING()); //new _25_LeftPut2LeftScale1RightSwitch());
        AUTO_MODES.put(27, /* 	SW x2					 */ new _27_LeftPut1LeftSwitchEnd1LeftSwitch());
        AUTO_MODES.put(28, /* 	SW - Far SC				 */ new _28_LeftPut1LeftSwitchEnd1RightScale());
        AUTO_MODES.put(31, /* 	SC - SW - SC			 */ new _31_LeftPut2LeftScale1LeftSwitch());
        AUTO_MODES.put(34, /* 	SW						 */ new _34_MiddlePut1LeftSwitch());
        AUTO_MODES.put(35, /* 	SW x2					 */ new _35_MiddlePut2LeftSwitch());
        AUTO_MODES.put(36, /* 	SW - EX					 */ new _36_MiddlePut1LeftSwitch1Exchange());
        AUTO_MODES.put(37, /* 	SW						 */ new _37_MiddlePut1RightSwitch());
        AUTO_MODES.put(38, /* 	SW x2					 */ new _38_MiddlePut2RightSwitch());
        AUTO_MODES.put(39, /* 	SW - EX					 */ new _39_MiddlePut1RightSwitch1Exchange());
        AUTO_MODES.put(40, /* 	Far SC-Far SW x2		 */ new _00_DO_NOTHING()); //new _40_RightPut1LeftScale2LeftSwitch());
        AUTO_MODES.put(41, /* 	SC End					 */ new _41_RightPut1RightScaleEnd());
        AUTO_MODES.put(42, /* 	SC End-Far SW			 */ new _42_RightPut1RightScaleEnd1LeftSwitch());
        AUTO_MODES.put(43, /* 	SC End x2				 */ new _43_RightPut2RightScaleEnd());
        AUTO_MODES.put(46, /* 	SW - SC End		 		 */ new _00_DO_NOTHING()); //new _46_RightPut1RightSwitchEnd1RightScaleEnd());
        AUTO_MODES.put(47, /* 	Drive FWD				 */ new _47_MiddleDriveForward());
        AUTO_MODES.put(48, /* 	SC End x2				 */ new _48_LeftPut2LeftScaleEnd());
        AUTO_MODES.put(49, /* 	SC End					 */ new _49_LeftPut1LeftScaleEnd());
        AUTO_MODES.put(50, /* 	SW - SC End				 */ new _00_DO_NOTHING()); //new _50_LeftPut1LeftSwitchEnd1LeftScaleEnd());
        AUTO_MODES.put(52, /* 	SC End - Far SW			 */ new _52_LeftPut1LeftScaleEnd1RightSwitch());
        AUTO_MODES.put(54, /* 	Far SC - Far SW X2		 */ new _00_DO_NOTHING()); //new _54_LeftPut1RightScale2RightSwitch());
        AUTO_MODES.put(55, /* 	Drive Forward			 */ new _55_LeftDriveForward());
        AUTO_MODES.put(56, /* 	Drive-EX  				 */ new _56_RightPut1Exchange());
        AUTO_MODES.put(57, /* 	Drive-EX x2				 */ new _57_RightPut2Exchange());
        AUTO_MODES.put(58, /* 	Drive-EX  				 */ new _58_LeftPut1Exchange());
        AUTO_MODES.put(59, /* 	Drive-EX x2				 */ new _59_LeftPut2Exchange());
        AUTO_MODES.put(60, /* 	Thunder Auto			 */ new _60_RightPut3LeftScale());
        AUTO_MODES.put(61,  /* 	SC x3 sped up			 */ new _61_RightPut3RightScaleChamps());
        
    }
                                        //   41 for example
	private AutoModeBase lookupMode(String autoCode) {
		AutoModeBase mode = null;
		if(autoCode != null && autoCode.length() > 0) {
			try {
				mode = AUTO_MODES.get(Integer.parseInt(autoCode));
			} catch (NumberFormatException e) {
				System.err.println("UNABLE TO PARSE DESIRED AUTO MODE!!!");
			}
		}
		return mode == null ? new StandStillMode() : mode;
	}

	private static void initAllowableAutoModes() {
		ALLOWABLE_AUTO_MODES = new HashMap<String, String[]>();// as defined in Mark's "BIBLE"
		ALLOWABLE_AUTO_MODES.put("LL", new String[]{"_00_DO_NOTHING",
													"_02_RightPut3LeftScale",
													"_03_RightPut2LeftScale1LeftSwitch",
													"_40_RightPut1LeftScale2LeftSwitch",
													"_05_RightDriveForward",
													"_35_MiddlePut2LeftSwitch",
													"_34_MiddlePut1LeftSwitch",
													"_36_MiddlePut1LeftSwitch1Exchange",
						//"_43_RightPut2RightScaleEnd", //TEST ONLY - comment-out for competitions
													"_47_MiddleDriveForward",
													"_31_LeftPut2LeftScale1LeftSwitch",
													"_23_LeftPut3LeftScale",
													"_48_LeftPut2LeftScaleEnd",
													"_49_LeftPut1LeftScaleEnd",
													"_50_LeftPut1LeftSwitchEnd1LeftScaleEnd",
													"_55_LeftDriveForward",
													"_56_RightPut1Exchange",
													"_57_RightPut2Exchange",
													"_60_RightPut3LeftScale"
													});
		
		ALLOWABLE_AUTO_MODES.put("LR", new String[]{"_00_DO_NOTHING", 
													"_08_RightPut1RightScale1LeftSwitchEnd",
													"_07_RightPut3RightScale",
													"_09_RightPut2RightScale1LeftSwitch",
													"_41_RightPut1RightScaleEnd",
													"_42_RightPut1RightScaleEnd1LeftSwitch",
													"_43_RightPut2RightScaleEnd",
													"_05_RightDriveForward",
													"_34_MiddlePut1LeftSwitch",
													"_35_MiddlePut2LeftSwitch",
													"_47_MiddleDriveForward",
						//"_48_LeftPut2LeftScaleEnd",	//TEST ONLY - comment-out for competitions												
													"_27_LeftPut1LeftSwitchEnd1LeftSwitch",
													"_19_LeftPut3RightScale",
													"_28_LeftPut1LeftSwitchEnd1RightScale",
													"_55_LeftDriveForward",
													"_61_RightPut3RightScaleChamps"
													});
		
		ALLOWABLE_AUTO_MODES.put("RL", new String[]{"_00_DO_NOTHING", 
													"_11_RightPut1RightSwitchEnd1RightSwitch",
													"_02_RightPut3LeftScale",
													"_12_RightPut1RightSwitchEnd1LeftScale",
													"_05_RightDriveForward",
													"_37_MiddlePut1RightSwitch",
													"_38_MiddlePut2RightSwitch",
													"_39_MiddlePut1RightSwitch1Exchange",
						//"_43_RightPut2RightScaleEnd", //TEST ONLY - comment-out for competitions
													"_47_MiddleDriveForward",
													"_24_LeftPut1LeftScale1RightSwitchEnd",
													"_23_LeftPut3LeftScale",
													"_25_LeftPut2LeftScale1RightSwitch",
													"_49_LeftPut1LeftScaleEnd",
													"_52_LeftPut1LeftScaleEnd1RightSwitch",
													"_48_LeftPut2LeftScaleEnd",
													"_55_LeftDriveForward",
													"_60_RightPut3LeftScale"
													});
		
		ALLOWABLE_AUTO_MODES.put("RR", new String[]{"_00_DO_NOTHING", 
													"_15_RightPut2RightScale1RightSwitch",
													"_07_RightPut3RightScale",
													"_43_RightPut2RightScaleEnd",
													"_41_RightPut1RightScaleEnd",
													"_46_RightPut1RightSwitchEnd1RightScaleEnd",
													"_05_RightDriveForward",
													"_37_MiddlePut1RightSwitch",
													"_38_MiddlePut2RightSwitch",
													"_39_MiddlePut1RightSwitch1Exchange",
													"_47_MiddleDriveForward",
						//"_48_LeftPut2LeftScaleEnd",	//TEST ONLY - comment-out for competitions													
													"_19_LeftPut3RightScale",
													"_20_LeftPut2RightScale1RightSwitch",
													"_54_LeftPut1RightScale2RightSwitch",
													"_55_LeftDriveForward",
													"_58_LeftPut1Exchange",
													"_59_LeftPut2Exchange",
													"_61_RightPut3RightScaleChamps"
													});
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
            boolean flipUp = mControlBoard.getFlipUpButton();
            boolean flipDown = mControlBoard.getFlipDownButton();
            boolean grabCube = mControlBoard.getGrabCubeButton();
            boolean calibrateDown = mControlBoard.getCalibrateDown();
            boolean calibrateUp = mControlBoard.getCalibrateUp();
            boolean spitting = mControlBoard.getSpit();
            boolean pickUp = mControlBoard.getAutoPickUp();
            boolean fishingPoleUp = mControlBoard.getFishingPoleUp();
            boolean fishingPoleDown = mControlBoard.getFishingPoleDown();
            boolean fishingPoleExtend = mControlBoard.getFishingPoleExtend();
            boolean fishingPoleRetract =mControlBoard.getFishingPoleRetract();
            
            //if (mControlBoard.getElevatorButton()) {  Lydia doesn't want to hold the button
//                if (overTheTop) {
//                    mSuperstructure.setOverTheTop(true);
//                }
//                else {
//                    mSuperstructure.setOverTheTop(false);
//                }
                mSuperstructure.setWantedElevatorPosition(-1 * mControlBoard.getElevatorControl());
            //} else {
            //    mSuperstructure.setWantedElevatorPosition(0);
            //}

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
            } else {
            	mSuperstructure.setWantedState(Superstructure.WantedState.ELEVATOR_TRACKING);
            }
            	
            if (flipUp) {
            	//_24vSolenoid.set(true);
                mSuperstructure.setOverTheTop(GRABBER_POSITION.FLIP_UP);
            } else if (flipDown) {
                mSuperstructure.setOverTheTop(GRABBER_POSITION.FLIP_DOWN);
            } else {
            	//_24vSolenoid.set(false);
                mSuperstructure.setOverTheTop(GRABBER_POSITION.FLIP_NONE);
            }
            
            if (fishingPoleUp) {
            	mSuperstructure.setFishingPoleUpdown(Superstructure.FISHING_POLE_UPDOWN.UP);
            } else if (fishingPoleDown) {
            	mSuperstructure.setFishingPoleUpdown(Superstructure.FISHING_POLE_UPDOWN.DOWN);
            } else {
            	mSuperstructure.setFishingPoleUpdown(Superstructure.FISHING_POLE_UPDOWN.NONE);
            }
            
            if (fishingPoleExtend) {
            	mSuperstructure.setFishingPoleExtendRetract(Superstructure.FISHING_POLE_EXTEND_RETRACT.EXTEND);
            } else if (fishingPoleRetract) {
            	mSuperstructure.setFishingPoleExtendRetract(Superstructure.FISHING_POLE_EXTEND_RETRACT.RETRACT);
            } else {
            	mSuperstructure.setFishingPoleExtendRetract(Superstructure.FISHING_POLE_EXTEND_RETRACT.NONE);
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
        String autoCodes = SmartDashboard.getString("AutoCodes", "3 7 2 15");
        SmartDashboard.putString("AutoCodesReceived", autoCodes);
        ConnectionMonitor.getInstance().setLastPacketTime(Timer.getFPGATimestamp());
    }
}
