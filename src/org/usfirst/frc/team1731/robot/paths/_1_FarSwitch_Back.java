package org.usfirst.frc.team1731.robot.paths;

import java.util.ArrayList;

import org.usfirst.frc.team1731.lib.util.control.Path;
import org.usfirst.frc.team1731.lib.util.math.RigidTransform2d;
import org.usfirst.frc.team1731.lib.util.math.Rotation2d;
import org.usfirst.frc.team1731.lib.util.math.Translation2d;
import org.usfirst.frc.team1731.robot.paths.PathBuilder.Waypoint;
import org.usfirst.frc.team1731.robot.paths.PathContainer;

public class _1_FarSwitch_Back implements PathContainer {
    
    @Override
    public Path buildPath() {
        ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
        sWaypoints.add(new Waypoint(20,280,0,0));
        sWaypoints.add(new Waypoint(230,280,30,60));
        sWaypoints.add(new Waypoint(230,110,10,30));
        sWaypoints.add(new Waypoint(226,110,5,0));

        return PathBuilder.buildPathFromWaypoints(sWaypoints);
    }
    
    @Override
    public RigidTransform2d getStartPose() {
        return new RigidTransform2d(new Translation2d(20, 280), Rotation2d.fromDegrees(180.0)); 
    }

    @Override
    public boolean isReversed() {
        return false; 
    }
	// WAYPOINT_DATA: [{"position":{"x":20,"y":280},"speed":0,"radius":0,"comment":""},{"position":{"x":230,"y":280},"speed":60,"radius":30,"comment":""},{"position":{"x":230,"y":110},"speed":30,"radius":10,"comment":""},{"position":{"x":226,"y":110},"speed":0,"radius":5,"comment":""}]
	// IS_REVERSED: false
	// FILE_NAME: _1_FarSwitch_Back
}