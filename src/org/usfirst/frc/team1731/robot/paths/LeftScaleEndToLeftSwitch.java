package org.usfirst.frc.team1731.robot.paths;

import java.util.ArrayList;

import org.usfirst.frc.team1731.lib.util.control.Path;
import org.usfirst.frc.team1731.lib.util.math.RigidTransform2d;
import org.usfirst.frc.team1731.lib.util.math.Rotation2d;
import org.usfirst.frc.team1731.lib.util.math.Translation2d;
import org.usfirst.frc.team1731.robot.paths.PathBuilder.Waypoint;
import org.usfirst.frc.team1731.robot.paths.PathContainer;

public class LeftScaleEndToLeftSwitch implements PathContainer {
    
    @Override
    public Path buildPath() {
        ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
        sWaypoints.add(new Waypoint(325,274,0,0));
        sWaypoints.add(new Waypoint(325,286,10,50));
        sWaypoints.add(new Waypoint(240,286,10,60));
        sWaypoints.add(new Waypoint(240,250,0,60));
        sWaypoints.add(new Waypoint(240,235,10,0));
        sWaypoints.add(new Waypoint(220,235,0,30));
        return PathBuilder.buildPathFromWaypoints(sWaypoints);
    }
    
    @Override
    public RigidTransform2d getStartPose() {
        return new RigidTransform2d(new Translation2d(325, 274), Rotation2d.fromDegrees(0.0)); 
    }

    @Override
    public boolean isReversed() {
        return false; 
    }
	// WAYPOINT_DATA: [{"position":{"x":285,"y":241},"speed":0,"radius":0,"comment":""},{"position":{"x":239,"y":231},"speed":50,"radius":0,"comment":""},{"position":{"x":233,"y":230},"speed":20,"radius":0,"comment":""}]
	// IS_REVERSED: false
	// FILE_NAME: LeftScaleToLeftSwitch

	public static void main(String[] args) {
		
	}
}