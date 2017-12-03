package org.usfirst.frc.team1731.robot.auto.modes;

import java.util.Arrays;

import org.usfirst.frc.team1731.lib.util.DriveSignal;
import org.usfirst.frc.team1731.lib.util.math.Rotation2d;
import org.usfirst.frc.team1731.robot.auto.AutoModeBase;
import org.usfirst.frc.team1731.robot.auto.AutoModeEndedException;
import org.usfirst.frc.team1731.robot.auto.actions.Action;
import org.usfirst.frc.team1731.robot.auto.actions.BeginShootingAction;
import org.usfirst.frc.team1731.robot.auto.actions.DeployIntakeAction;
import org.usfirst.frc.team1731.robot.auto.actions.DrivePathAction;
import org.usfirst.frc.team1731.robot.auto.actions.ForceEndPathAction;
import org.usfirst.frc.team1731.robot.auto.actions.ParallelAction;
import org.usfirst.frc.team1731.robot.auto.actions.PrintDebugAction;
import org.usfirst.frc.team1731.robot.auto.actions.ResetPoseFromPathAction;
import org.usfirst.frc.team1731.robot.auto.actions.SeriesAction;
import org.usfirst.frc.team1731.robot.auto.actions.SetFlywheelRPMAction;
import org.usfirst.frc.team1731.robot.auto.actions.TurnUntilSeesTargetAction;
import org.usfirst.frc.team1731.robot.auto.actions.WaitAction;
import org.usfirst.frc.team1731.robot.auto.actions.WaitForPathMarkerAction;
import org.usfirst.frc.team1731.robot.paths.PathContainer;
import org.usfirst.frc.team1731.robot.paths.StartToHopperBlue;
import org.usfirst.frc.team1731.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.Timer;

/**
 * Rams the field hopper head on with the robot's intake then waits for the balls to fall into the robot and shoots
 * them.
 * 
 * @see AutoModeBase
 */
public class RamHopperShootModeBlue extends AutoModeBase {

    @Override
    protected void routine() throws AutoModeEndedException {
        PathContainer hopperPath = new StartToHopperBlue();
        runAction(new ResetPoseFromPathAction(hopperPath));
        double startTime = Timer.getFPGATimestamp();
        runAction(new DeployIntakeAction(true));
        runAction(
                new ParallelAction(Arrays.asList(new Action[] {
                        new DrivePathAction(hopperPath),
                        new SeriesAction(Arrays.asList(new Action[] {
                                new WaitForPathMarkerAction("RamWall"), new PrintDebugAction("RamWall"),
                                new WaitAction(0.25), new ForceEndPathAction()
                        }))
                }))); // Drive to hopper, cancel path once the robot runs into the wall
        runAction(new SetFlywheelRPMAction(3100));
        runAction(new WaitAction(2.6)); // wait for balls
        Drive.getInstance().setOpenLoop(new DriveSignal(-1, -1));
        runAction(new WaitAction(0.2));
        runAction(new TurnUntilSeesTargetAction(Rotation2d.fromDegrees(160)));
        System.out.println("Time to shoot: " + (Timer.getFPGATimestamp() - startTime));
        runAction(new BeginShootingAction()); // aim + fire
        runAction(new WaitAction(20)); // keep firing until auto ends
    }
}
