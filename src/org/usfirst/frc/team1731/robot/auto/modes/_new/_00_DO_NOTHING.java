package org.usfirst.frc.team1731.robot.auto.modes._new;

import org.usfirst.frc.team1731.robot.auto.AutoModeBase;
import org.usfirst.frc.team1731.robot.auto.AutoModeEndedException;
import org.usfirst.frc.team1731.robot.auto.actions.WaitAction;
import org.usfirst.frc.team1731.robot.auto.modes.RightPut3CubesOnLeftScale;

public class _00_DO_NOTHING extends AutoModeBase {

	@Override
	protected void routine() throws AutoModeEndedException {
		System.out.println("running autonomous mode _00_DO_NOTHING");
    	runAction(new WaitAction(15));
	}

}
