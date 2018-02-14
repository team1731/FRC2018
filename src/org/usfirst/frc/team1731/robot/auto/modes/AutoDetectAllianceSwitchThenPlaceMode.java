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
	boolean isAllianceTrustworthy = true;
	enum startingPositions {
		LEFT,
		MIDDLELEFT,
		MIDDLERIGHT,
		RIGHT
	}
	startingPositions startingPos = startingPositions.LEFT;
	
    @Override
    protected void routine() throws AutoModeEndedException {
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
    	AutoModeBase defaultFallbackMode = new StandStillMode();
        AutoModeBase selectedAutoMode = defaultFallbackMode;
        //boolean isRed = true;
        //DriverStation.Alliance alliance = DriverStation.getInstance().getAlliance();
        
        switch(startingPos){
        	case LEFT:
        		if(gameData.charAt(0) == 'L' && gameData.charAt(1) == 'L') {
        			selectedAutoMode = new LeftPutCubeOnLeftSwitchAndLeftScale();
        		} else if(gameData.charAt(0) == 'L' && gameData.charAt(1) == 'R') {
        			selectedAutoMode = isAllianceTrustworthy ? new LeftPutCubeOnLeftSwitch() : new LeftPutCubeOnLeftSwitchAndRightScale();
        		} else if(gameData.charAt(0) == 'R' && gameData.charAt(1) == 'R') {
        			selectedAutoMode = isAllianceTrustworthy ? new LeftPutCubeOnRightScale() : new LeftPutCubeOnRightSwitch();
        		} else if(gameData.charAt(0) == 'R' && gameData.charAt(1) == 'L') {
        			selectedAutoMode = isAllianceTrustworthy ? new LeftPutCubeOnLeftScale() : new LeftPutCubeOnRightSwitchAndLeftScale();
        		}
        	break;
        	case MIDDLELEFT:
        		if(isAllianceTrustworthy) {
        			selectedAutoMode = new MiddleLeftPutInExchange();
        		} else {
        			selectedAutoMode = gameData.charAt(0) == 'L' ? new MiddleLeftPutCubeOnLeftSwitch() : new MiddleLeftPutCubeOnRightSwitch();
        		}
        	break;
        	case MIDDLERIGHT:
        		if(isAllianceTrustworthy) {
        			selectedAutoMode = new MiddleRightPutInExchange();
        		} else {
        			selectedAutoMode = gameData.charAt(0) == 'L' ? new MiddleRightPutCubeOnLeftSwitch() : new MiddleRightPutCubeOnRightSwitch();
        		}
        	break;
        	case RIGHT:
        		if(gameData.charAt(0) == 'R' && gameData.charAt(1) == 'R') {
        			selectedAutoMode = new RightPutCubeOnRightSwitchAndRightScale();
        		} else if(gameData.charAt(0) == 'R' && gameData.charAt(1) == 'L') {
        			selectedAutoMode = isAllianceTrustworthy ? new RightPutCubeOnRightSwitch() : new RightPutCubeOnRightSwitchAndLeftScale();
        		} else if(gameData.charAt(0) == 'L' && gameData.charAt(1) == 'L') {
        			selectedAutoMode = isAllianceTrustworthy ? new RightPutCubeOnLeftScale() : new RightPutCubeOnLeftSwitch();
        		} else if(gameData.charAt(0) == 'L' && gameData.charAt(1) == 'R') {
        			selectedAutoMode = isAllianceTrustworthy ? new RightPutCubeOnRightScale() : new RightPutCubeOnLeftSwitchAndRightScale();
        		}
        	break;
        }
        if(selectedAutoMode == defaultFallbackMode) {
        	DriverStation.reportWarning("Unable to logically choose correct autonomous path from gameData. Defaulting to "+defaultFallbackMode.toString()+" here's some fun facts: selectedAutoMode: "+selectedAutoMode.toString()+" isAllianceTrustworthy: "+isAllianceTrustworthy, false);
        }
        selectedAutoMode.run();
        
        /*
        	This is my thinking space
        		Set a variable for our known starting position. Make it an enum, right now it's an integer.
        		Set a boolean for "alliance trust". We don't know if the people we'll be working with are actually good or not until we're there
        		Alliance trust will make the robot behave differently. Mainly if it'll keep going for the scale.
        		Put the cube the robot starts with on the switch.
        		If we don't trust our alliance, and the scale is on the opposite side, pick up another cube and smash a box in there.
        		If time allows, keep grabbing boxes and putting them in the scale
        		Maybe we can go into teleop holding a box?
        */
    }
}
