package org.usfirst.frc.team1731.robot;

import java.util.List;

import org.usfirst.frc.team1731.robot.loops.Looper;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Used to reset, start, stop, and update all subsystems at once
 */
public class SubsystemManager {

    private final List<org.usfirst.frc.team1731.robot.subsystems.Subsystem> mAllSubsystems;

    public SubsystemManager(List<org.usfirst.frc.team1731.robot.subsystems.Subsystem> list) {
        mAllSubsystems = list;
    }

    public void outputToSmartDashboard() {
        mAllSubsystems.forEach((s) -> s.outputToSmartDashboard());
    }

    public void writeToLog() {
        mAllSubsystems.forEach((s) -> s.writeToLog());
    }

    public void stop() {
        mAllSubsystems.forEach((s) -> s.stop());
    }

    public void zeroSensors() {
        mAllSubsystems.forEach((s) -> s.zeroSensors());
    }

    public void registerEnabledLoops(Looper enabledLooper) {
        mAllSubsystems.forEach((s) -> s.registerEnabledLoops(enabledLooper));
    }
}
