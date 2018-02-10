package org.usfirst.frc.team1731.robot.auto.actions;

import org.usfirst.frc.team1731.robot.subsystems.Superstructure;

/**
 * Action to extend (or retract) the hopper side panel pistons.
 * 
 * @see Action
 * @see RunOnceAction
 */
public class ActuateHopperAction extends RunOnceAction {

    boolean extend;

    public ActuateHopperAction(boolean extend) {
        this.extend = extend;
    }

    @Override
    public synchronized void runOnce() {
    //    Superstructure.getInstance().setActuateHopper(extend);
    }
}
