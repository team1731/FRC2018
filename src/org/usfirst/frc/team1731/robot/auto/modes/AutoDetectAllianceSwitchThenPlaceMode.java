package org.usfirst.frc.team1731.robot.auto.modes;

import org.usfirst.frc.team1731.robot.auto.AutoModeBase;
import org.usfirst.frc.team1731.robot.auto.AutoModeEndedException;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * Auto detect which alliance we are part of then run the GearThenHopperShoot auto. Default to RED side.
 * 
 * @see AutoModeBase
 * @see SwitchThenPlaceModeBlue
 * @see PlaceOnRightSwitch
 */
public class AutoDetectAllianceSwitchThenPlaceMode extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
    	int startingPosition = 0; //0 is leftmost, 1 is middle-left, 2 is middle-right, and 3 is rightmost
        AutoModeBase selectedAutoMode = new PlaceOnLeftSwitch();
        //boolean isRed = true;
        //DriverStation.Alliance alliance = DriverStation.getInstance().getAlliance();
        
        selectedAutoMode.run();
        
        //The below code will be what we actually run, but we haven't created the paths and/or modes yet
        /*
        switch(startingPosition) {
        	case 0:
        		if(gameData.charAt(0) == 'L' && gameData.charAt(1) == 'L') {
        			selectedAutoMode = new LeftPutCubeOnLeftSwitchAndLeftScale();
        		} else if(gameData.charAt(0) == 'L' && gameData.charAt(1) == 'R') {
        			selectedAutoMode = new LeftPutCubeOnLeftSwitch();
        		} else if(gameData.charAt(0) == 'R') {
        			selectedAutoMode = new LeftPutCubeOnRightSwitch();
        		}
        	case 1:
        		if(gameData.charAt(0) == 'L') {
        			selectedAutoMode = new MiddleLeftPutCubeOnLeftSwitch();
        		} else {
        			selectedAutoMode = new MiddleLeftPutCubeOnRightSwitch();
        		}
        	case 2:
        		if(gameData.charAt(0) == 'L') {
        			selectedAutoMode = new MiddleRightPutCubeOnLeftSwitch();
        		} else {
        			selectedAutoMode = new MiddleRightPutCubeOnRightSwitch();
        		}
        	break;
        	case 3:
        		if(gameData.charAt(0) == 'R' && gameData.charAt(1) == 'R') {
        			selectedAutoMode = new RightPutCubeOnRightSwitchAndRightScale();
        		} else if(gameData.charAt(0) == 'R' && gameData.charAt(1) == 'L') {
        			selectedAutoMode = new RightPutCubeOnRightSwitch();
        		} else if(gameData.charAt(0) == 'L') {
        			selectedAutoMode = new RightPutCubeOnLeftSwitch();
        		}
        	break;
        }
        */
    }
}
