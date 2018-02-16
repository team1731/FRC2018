package org.usfirst.frc.team1731.robot.paths;

import java.util.ArrayList;

import org.usfirst.frc.team1731.lib.util.control.Path;
import org.usfirst.frc.team1731.lib.util.math.RigidTransform2d;
import org.usfirst.frc.team1731.lib.util.math.Rotation2d;
import org.usfirst.frc.team1731.lib.util.math.Translation2d;
import org.usfirst.frc.team1731.robot.paths.PathBuilder.Waypoint;

public class LeftSwitchFromLeftToRightScale implements PathContainer {
    
    @Override
    public Path buildPath() {
        ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
        sWaypoints.add(new Waypoint(170,260,0,0)); //LeftSwitch
        sWaypoints.add(new Waypoint(240,265,0,60));
        sWaypoints.add(new Waypoint(240,70,0,60));
        sWaypoints.add(new Waypoint(280,80,0,60));

        return PathBuilder.buildPathFromWaypoints(sWaypoints);
    }
    
    @Override
    public RigidTransform2d getStartPose() {
        return new RigidTransform2d(new Translation2d(170, 260), Rotation2d.fromDegrees(180.0)); 
    }

    @Override
    public boolean isReversed() {
        return false; 
    }
	// WAYPOINT_DATA: [{"position":{"x":170,"y":260},"speed":0,"radius":0,"comment":"LeftSwitch"},{"position":{"x":240,"y":265},"speed":60,"radius":0,"comment":""},{"position":{"x":240,"y":70},"speed":60,"radius":0,"comment":""},{"position":{"x":280,"y":80},"speed":60,"radius":0,"comment":""}]
	// IS_REVERSED: false
	// FILE_NAME: LeftSwitchFromLeftToRightScale
}