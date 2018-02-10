package org.usfirst.frc.team1731.robot.auto.actions;

import org.usfirst.frc.team1731.robot.subsystems.Drive;
import org.usfirst.frc.team1731.robot.subsystems.Intake;
import org.usfirst.frc.team1731.robot.subsystems.Superstructure;

/**
 * Action to begin shooting.
 * 
 * @see Action
 * @see RunOnceAction
 */
public class BeginShootingAction extends RunOnceAction implements Action {

    @Override
    public void runOnce() {
        Drive.getInstance().setWantAimToGoal();
     //   Superstructure.getInstance().setWantedState(Superstructure.WantedState.SHOOT);
  //      Intake.getInstance().setOn(); // maybe intake a few missed balls if we're lucky
    }

}
