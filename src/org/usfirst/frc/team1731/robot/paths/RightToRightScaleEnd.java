package org.usfirst.frc.team1731.robot.paths;

import java.util.ArrayList;

import org.usfirst.frc.team1731.lib.util.control.Path;
import org.usfirst.frc.team1731.lib.util.math.RigidTransform2d;
import org.usfirst.frc.team1731.lib.util.math.Rotation2d;
import org.usfirst.frc.team1731.lib.util.math.Translation2d;
import org.usfirst.frc.team1731.robot.paths.PathBuilder.Waypoint;
import org.usfirst.frc.team1731.robot.paths.PathContainer;

public class RightToRightScaleEnd implements PathContainer {
    
    @Override
    public Path buildPath() {
        ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
        sWaypoints.add(new Waypoint(19,47,0,0));
        sWaypoints.add(new Waypoint(250,35,0,110));
        sWaypoints.add(new Waypoint(300,30,12,110));     
        sWaypoints.add(new Waypoint(325,35,8,110));     
        sWaypoints.add(new Waypoint(325,50,0,60));

        return PathBuilder.buildPathFromWaypoints(sWaypoints);
    }
    
    @Override
    public RigidTransform2d getStartPose() {
        return new RigidTransform2d(new Translation2d(19, 47), Rotation2d.fromDegrees(180.0)); 
    }

    @Override
    public boolean isReversed() {
        return true; 
    }
	// WAYPOINT_DATA: [{"position":{"x":25,"y":50},"speed":0,"radius":0,"comment":""},{"position":{"x":70,"y":50},"speed":60,"radius":0,"comment":""}]
	// IS_REVERSED: false
	// FILE_NAME: RightToRightScale
}