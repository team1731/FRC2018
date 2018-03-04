package org.usfirst.frc.team1731.robot.paths;

import java.util.ArrayList;

import org.usfirst.frc.team1731.lib.util.control.Path;
import org.usfirst.frc.team1731.lib.util.math.RigidTransform2d;
import org.usfirst.frc.team1731.lib.util.math.Rotation2d;
import org.usfirst.frc.team1731.lib.util.math.Translation2d;
import org.usfirst.frc.team1731.robot.paths.PathBuilder.Waypoint;
import org.usfirst.frc.team1731.robot.paths.PathContainer;

public class Left3rdCubeScore2 implements PathContainer {
    
    @Override
    public Path buildPath() {
        ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
        sWaypoints.add(new Waypoint(214,214,0,0));
        sWaypoints.add(new Waypoint(249,230,0,60));
        sWaypoints.add(new Waypoint(284,230,0,40));
        
        return PathBuilder.buildPathFromWaypoints(sWaypoints);
    }
    
    @Override
    public RigidTransform2d getStartPose() {
        return new RigidTransform2d(new Translation2d(214, 214), Rotation2d.fromDegrees(180.0)); 
    }

    @Override
    public boolean isReversed() {
        return true; 
    }
	// WAYPOINT_DATA: [{"position":{"x":214,"y":214},"speed":0,"radius":0,"comment":""},{"position":{"x":249,"y":230},"speed":60,"radius":0,"comment":""},{"position":{"x":284,"y":230},"speed":40,"radius":0,"comment":""}]
	// IS_REVERSED: false
	// FILE_NAME: Left3rdCubeScore2
}