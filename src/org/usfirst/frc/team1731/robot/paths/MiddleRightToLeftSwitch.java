package org.usfirst.frc.team1731.robot.paths;

import java.util.ArrayList;

import org.usfirst.frc.team1731.lib.util.control.Path;
import org.usfirst.frc.team1731.lib.util.math.RigidTransform2d;
import org.usfirst.frc.team1731.lib.util.math.Rotation2d;
import org.usfirst.frc.team1731.lib.util.math.Translation2d;
import org.usfirst.frc.team1731.robot.paths.PathBuilder.Waypoint;

public class MiddleRightToLeftSwitch implements PathContainer {
    
    @Override
    public Path buildPath() {
        ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
        sWaypoints.add(new Waypoint(16,155,0,0));
        sWaypoints.add(new Waypoint(48,155,0,60));
        sWaypoints.add(new Waypoint(64,220,0,60));
        sWaypoints.add(new Waypoint(110,220,0,60));

        return PathBuilder.buildPathFromWaypoints(sWaypoints);
    }
    
    @Override
    public RigidTransform2d getStartPose() {
        return new RigidTransform2d(new Translation2d(16, 155), Rotation2d.fromDegrees(180.0)); 
    }

    @Override
    public boolean isReversed() {
        return false; 
    }
	// WAYPOINT_DATA: [{"position":{"x":16,"y":155},"speed":0,"radius":0,"comment":""},{"position":{"x":48,"y":155},"speed":60,"radius":0,"comment":""},{"position":{"x":64,"y":220},"speed":60,"radius":0,"comment":""},{"position":{"x":110,"y":220},"speed":60,"radius":0,"comment":""}]
	// IS_REVERSED: false
	// FILE_NAME: MiddleRightToLeftSwitch
}