package org.usfirst.frc.team1731.robot.auto.modes;

import org.usfirst.frc.team1731.robot.Robot;
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
public class AutoDetectAllianceSwitchThenPlaceMode  {
//	boolean isAllianceTrustworthy = true;
	public enum startingPositions {
		LEFT,
//		MIDDLELEFT,
		MIDDLERIGHT,
		RIGHT
	}
//	startingPositions startingPos = startingPositions.LEFT;
	public static AutoModeBase intenseTrust(startingPositions startingPos) {
		AutoModeBase selectedAutoMode = new StandStillMode();
		
		switch(startingPos){
			case LEFT:
				selectedAutoMode = new DriveForwardFromLeftMode();
			break;
			case MIDDLERIGHT:
				selectedAutoMode = new DriveForwardFromMiddleMode();
			break;
			case RIGHT:
				selectedAutoMode = new DriveForwardFromRightMode();
			break;
		}
		
		return selectedAutoMode;
	}
	
    public static AutoModeBase pickAutoMode(startingPositions startingPos, boolean isAllianceTrustworthy)  {
    	String gameData = Robot.getGameDataFromField();
    	
    	AutoModeBase defaultFallbackMode = new StandStillMode();
        AutoModeBase selectedAutoMode = defaultFallbackMode;
        //boolean isRed = true;
        //DriverStation.Alliance alliance = DriverStation.getInstance().getAlliance();
        if (gameData.length()>0 ) {
        switch(startingPos){
        	case LEFT:
        		if(gameData.charAt(0) == 'L' && gameData.charAt(1) == 'L') {
        			selectedAutoMode = new LeftPutCubeOnLeftScaleAndLeftSwitch();
        		} else if(gameData.charAt(0) == 'L' && gameData.charAt(1) == 'R') {
        			selectedAutoMode =  new LeftPutCubeOnLeftSwitch(); 
        		} else if(gameData.charAt(0) == 'R' && gameData.charAt(1) == 'R') {
        			selectedAutoMode = new LeftPutCubeOnRightScaleAndRightSwitch();
        		} else if(gameData.charAt(0) == 'R' && gameData.charAt(1) == 'L') {
        			selectedAutoMode = isAllianceTrustworthy ? new LeftPutCubeOnLeftScale() : new LeftPutCubeOnLeftScaleAndRightSwitch();
        		}
        	break;
 /*       	case MIDDLELEFT:
        		if(isAllianceTrustworthy) {
        			selectedAutoMode = new MiddleLeftPutInExchange();
        		} else {
        			selectedAutoMode = gameData.charAt(0) == 'L' ? new MiddleLeftPutCubeOnLeftSwitch() : new MiddleLeftPutCubeOnRightSwitch();
        		}
        	break;
*/
        	case MIDDLERIGHT:
        	//	if(isAllianceTrustworthy) {
        	//		selectedAutoMode = new MiddleRightPutInExchange();
        	//	} else {
        			selectedAutoMode = gameData.charAt(0) == 'L' ? new MiddleRightPutCubeOnLeftSwitch() : new MiddleRightPutCubeOnRightSwitch();
        	//	}
        	break;
        	case RIGHT:
        		if(gameData.charAt(0) == 'R' && gameData.charAt(1) == 'R') {
        			selectedAutoMode = new RightPutCubeOnRightScaleAndRightSwitch();
        			//selectedAutoMode = new RightPut2CubesOnRightScale(); //2 cubes
        		} else if(gameData.charAt(0) == 'R' && gameData.charAt(1) == 'L') {
        			//selectedAutoMode = new RightPutCubeOnRightSwitch();
        			selectedAutoMode = new RightPut3CubesOnLeftScale(); //2 cubes
        		} else if(gameData.charAt(0) == 'L' && gameData.charAt(1) == 'L') {
        			//selectedAutoMode =  new RightPutCubeOnLeftScaleAndLeftSwitch();
        			selectedAutoMode = new RightPut3CubesOnLeftScale(); //2 cubes
        		} else if(gameData.charAt(0) == 'L' && gameData.charAt(1) == 'R') {
        			//selectedAutoMode = isAllianceTrustworthy ? new RightPutCubeOnRightScale() : new RightPutCubeOnRightScaleAndLeftSwitch();
        			selectedAutoMode = new RightPutCubeOnRightScale(); //2 cubes
        		}
        	break;
        }
        }
        if(selectedAutoMode == defaultFallbackMode) {
        	DriverStation.reportWarning("Unable to logically choose correct autonomous path from gameData. Defaulting to "+defaultFallbackMode.toString()+" here's some fun facts: selectedAutoMode: "+selectedAutoMode.toString()+" isAllianceTrustworthy: "+isAllianceTrustworthy, false);
        }
        return selectedAutoMode;
        
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
