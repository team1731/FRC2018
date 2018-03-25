package org.usfirst.frc.team1731.robot.paths;

import java.util.ArrayList;

import org.usfirst.frc.team1731.lib.util.control.Path;
import org.usfirst.frc.team1731.lib.util.math.RigidTransform2d;
import org.usfirst.frc.team1731.lib.util.math.Rotation2d;
import org.usfirst.frc.team1731.lib.util.math.Translation2d;
import org.usfirst.frc.team1731.robot.paths.PathBuilder.Waypoint;
import org.usfirst.frc.team1731.robot.paths.PathContainer;

public class LeftSwitchToLeftScaleEnd implements PathContainer {
    
    @Override
    public Path buildPath() {
        ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
        sWaypoints.add(new Waypoint(220,238,0,0));  
        sWaypoints.add(new Waypoint(230,238,10,90));
        sWaypoints.add(new Waypoint(230,277,12,90));
        sWaypoints.add(new Waypoint(260,297,10,60));
        sWaypoints.add(new Waypoint(290,299,0,60));
        sWaypoints.add(new Waypoint(310,292,0,60));
        sWaypoints.add(new Waypoint(315,282,0,60));

        return PathBuilder.buildPathFromWaypoints(sWaypoints);
    }
    
    @Override
    public RigidTransform2d getStartPose() {
        return new RigidTransform2d(new Translation2d(220, 235), Rotation2d.fromDegrees(180.0)); 
    }

    @Override
    public boolean isReversed() {
        return true; 
    }
	// WAYPOINT_DATA: [{"position":{"x":274,"y":237},"speed":0,"radius":0,"comment":""},{"position":{"x":244,"y":219},"speed":60,"radius":0,"comment":""},{"position":{"x":214,"y":214},"speed":60,"radius":0,"comment":""}]
	// IS_REVERSED: false
	// FILE_NAME: Left3rdCubePickup2
}