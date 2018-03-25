package org.usfirst.frc.team1731.robot.paths;

import java.util.ArrayList;

import org.usfirst.frc.team1731.lib.util.control.Path;
import org.usfirst.frc.team1731.lib.util.math.RigidTransform2d;
import org.usfirst.frc.team1731.lib.util.math.Rotation2d;
import org.usfirst.frc.team1731.lib.util.math.Translation2d;
import org.usfirst.frc.team1731.robot.paths.PathBuilder.Waypoint;

public class LeftToLeftScaleEnd implements PathContainer {
    
    @Override
    public Path buildPath() {
        ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
        sWaypoints.add(new Waypoint(19,277,0,0)); //Left
        sWaypoints.add(new Waypoint(250,289,0,110));
        sWaypoints.add(new Waypoint(300,294,12,110));
        sWaypoints.add(new Waypoint(325,289,8,110));
        sWaypoints.add(new Waypoint(325,274,0,60));

        return PathBuilder.buildPathFromWaypoints(sWaypoints);
    }
    
    @Override
    public RigidTransform2d getStartPose() {
        return new RigidTransform2d(new Translation2d(19, 277), Rotation2d.fromDegrees(180.0)); 
    }

    @Override
    public boolean isReversed() {
        return true; 
    }
	// WAYPOINT_DATA: [{"position":{"x":19,"y":277},"speed":0,"radius":0,"comment":"Left"},{"position":{"x":199,"y":277},"speed":110,"radius":40,"comment":""},{"position":{"x":281,"y":239},"speed":110,"radius":0,"comment":""},{"position":{"x":285,"y":241},"speed":110,"radius":0,"comment":""}]
	// IS_REVERSED: false
	// FILE_NAME: LeftToLeftScale
}