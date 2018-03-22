package org.usfirst.frc.team1731.robot.paths;

import java.util.ArrayList;

import org.usfirst.frc.team1731.lib.util.control.Path;
import org.usfirst.frc.team1731.lib.util.math.RigidTransform2d;
import org.usfirst.frc.team1731.lib.util.math.Rotation2d;
import org.usfirst.frc.team1731.lib.util.math.Translation2d;
import org.usfirst.frc.team1731.robot.paths.PathBuilder.Waypoint;
import org.usfirst.frc.team1731.robot.paths.PathContainer;

public class LeftSwitchToRightSwitch2 implements PathContainer {
    
    @Override
    public Path buildPath() {
        ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
        sWaypoints.add(new Waypoint(249,247,0,0));
        sWaypoints.add(new Waypoint(249,208,0,100));
        sWaypoints.add(new Waypoint(249,67,10,100));
        sWaypoints.add(new Waypoint(219,27,10,80));
        sWaypoints.add(new Waypoint(169,27,10,80));
        sWaypoints.add(new Waypoint(164,59,0,60));
        

        return PathBuilder.buildPathFromWaypoints(sWaypoints);
    }
    
    @Override
    public RigidTransform2d getStartPose() {
        return new RigidTransform2d(new Translation2d(249, 247), Rotation2d.fromDegrees(0)); 
    }

    @Override
    public boolean isReversed() {
        return false; 
    }
	// WAYPOINT_DATA: [{"position":{"x":220,"y":90},"speed":0,"radius":0,"comment":""},{"position":{"x":240,"y":110},"speed":60,"radius":20,"comment":""},{"position":{"x":240,"y":180},"speed":60,"radius":10,"comment":""},{"position":{"x":260,"y":250},"speed":60,"radius":5,"comment":""},{"position":{"x":280,"y":250},"speed":60,"radius":5,"comment":""}]
	// IS_REVERSED: true
	// FILE_NAME: RightSwitchToLeftScale
}