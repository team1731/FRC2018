package org.usfirst.frc.team1731.robot.auto.actions;


import org.usfirst.frc.team1731.robot.subsystems.GearEjector;
import org.usfirst.frc.team1731.robot.subsystems.GearEjector.WantedState;

/**
 * Action for scoring a gear
 * 
 * @see Action
 * @see RunOnceAction
 */
public class ScoreGearAction extends RunOnceAction {

    @Override
    public void runOnce() {
        GearEjector.getInstance().setWantedState(WantedState.SCORE);
    }
}
