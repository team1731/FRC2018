package org.usfirst.frc.team1731.robot;

/**
 * A basic framework for robot controls that other controller classes implement
 */
public interface ControlBoardInterface {

		// DRIVER CONTROLS

		boolean getElevatorButton();

		boolean getFlipUpButton();

		boolean getFlipDownButton();

		double getElevatorControl();

		double getThrottle();

		double getTurn();

		boolean getQuickTurn();

		boolean getLowGear();

		boolean getBlinkLEDButton();

		boolean getGrabCubeButton();

		boolean getOverTheTopButton();

		boolean getCalibrateUp();

		boolean getCalibrateDown();

		boolean getSpit();

		boolean getClimbUp();

		boolean getClimbDown();

		boolean getAutoPickUp();

		boolean getFishingPoleDown();
		boolean getFishingPoleUp();
		boolean getFishingPoleExtend();
		boolean getFishingPoleRetract();

}
