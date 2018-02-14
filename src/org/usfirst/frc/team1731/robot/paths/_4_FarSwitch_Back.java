package org.usfirst.frc.team1731.robot.paths;

import java.util.ArrayList;

import org.usfirst.frc.team1731.lib.util.control.Path;
import org.usfirst.frc.team1731.lib.util.math.RigidTransform2d;
import org.usfirst.frc.team1731.lib.util.math.Rotation2d;
import org.usfirst.frc.team1731.lib.util.math.Translation2d;
import org.usfirst.frc.team1731.robot.paths.PathBuilder.Waypoint;
import org.usfirst.frc.team1731.robot.paths.PathContainer;

public class _4_FarSwitch_Back implements PathContainer {
    
    @Override
    public Path buildPath() {
        ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
        sWaypoints.add(new Waypoint(20,50,0,0));
        sWaypoints.add(new Waypoint(230,50,40,100));
        sWaypoints.add(new Waypoint(240,200,10,100));
        sWaypoints.add(new Waypoint(230,210,5,100));
        sWaypoints.add(new Waypoint(210,210,0,100));

        return PathBuilder.buildPathFromWaypoints(sWaypoints);
    }
    
    @Override
    public RigidTransform2d getStartPose() {
        return new RigidTransform2d(new Translation2d(20, 50), Rotation2d.fromDegrees(0.0)); 
    }

    @Override
    public boolean isReversed() {
        return false; 
    }
	// WAYPOINT_DATA: [{"position":{"x":20,"y":50},"speed":0,"radius":0,"comment":""},{"position":{"x":230,"y":50},"speed":60,"radius":40,"comment":""},{"position":{"x":230,"y":220},"speed":60,"radius":10,"comment":""},{"position":{"x":215,"y":220},"speed":20,"radius":5,"comment":""}]
	// IS_REVERSED: false
	// FILE_NAME: _4_FarSwitch_Back
}