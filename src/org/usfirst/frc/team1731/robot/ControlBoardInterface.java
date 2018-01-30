package org.usfirst.frc.team1731.robot;

/**
 * A basic framework for robot controls that other controller classes implement
 */
public interface ControlBoardInterface {
    // DRIVER CONTROLS
    double getThrottle();

    double getTurn();

    boolean getQuickTurn();

    boolean getLowGear();

    boolean getAimButton();

    boolean getDriveAimButton();

    // OPERATOR CONTROLS
    boolean getFeedButton();

    boolean getIntakeButton();

    boolean getShooterOpenLoopButton();

    boolean getExhaustButton();

    boolean getUnjamButton();

    boolean getShooterClosedLoopButton();

    boolean getFlywheelSwitch();

    boolean getHangButton();

    boolean getGrabGearButton();

    boolean getScoreGearButton();

    boolean getActuateHopperButton();

    boolean getBlinkLEDButton();

    boolean getRangeFinderButton();

    boolean getWantGearDriveLimit();

	boolean getElevatorUpButton();
	
	boolean getElevatorDownButton();
}
