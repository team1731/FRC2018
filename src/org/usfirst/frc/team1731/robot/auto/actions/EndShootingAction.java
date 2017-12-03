package org.usfirst.frc.team1731.robot.auto.actions;

import org.usfirst.frc.team1731.robot.subsystems.Superstructure;

/**
 * Action to make the robot stop shooting
 * 
 * @see Action
 * @see RunOnceAction
 */
public class EndShootingAction extends RunOnceAction implements Action {

    @Override
    public void runOnce() {
        Superstructure.getInstance().setWantedState(Superstructure.WantedState.IDLE);
    }

}
