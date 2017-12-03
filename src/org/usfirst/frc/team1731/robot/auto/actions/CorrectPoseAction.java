package org.usfirst.frc.team1731.robot.auto.actions;

import org.usfirst.frc.team1731.lib.util.math.RigidTransform2d;
import org.usfirst.frc.team1731.robot.RobotState;

import edu.wpi.first.wpilibj.Timer;

/**
 * Transform's the robot's current pose by a correction constant. Used to correct for error in robot position
 * estimation.
 * 
 * @see Action
 * @see RunOnceAction
 */
public class CorrectPoseAction extends RunOnceAction {
    RigidTransform2d mCorrection;

    public CorrectPoseAction(RigidTransform2d correction) {
        mCorrection = correction;
    }

    @Override
    public void runOnce() {
        RobotState rs = RobotState.getInstance();
        rs.reset(Timer.getFPGATimestamp(), rs.getLatestFieldToVehicle().getValue().transformBy(mCorrection));
    }

}
