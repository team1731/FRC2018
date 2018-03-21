package org.usfirst.frc.team1731.robot.auto.modes._new;

import org.usfirst.frc.team1731.robot.auto.AutoModeBase;
import org.usfirst.frc.team1731.robot.auto.AutoModeEndedException;
import org.usfirst.frc.team1731.robot.auto.actions.WaitAction;

public class _09_RightPut2RightScale1LeftSwitch extends AutoModeBase {
	//
	// 9.  Low priority because I don't think we can do this in 15 sec.
	//
	@Override
	protected void routine() throws AutoModeEndedException {
		// TODO Auto-generated method stub
    	runAction(new WaitAction(15));
	}

}
