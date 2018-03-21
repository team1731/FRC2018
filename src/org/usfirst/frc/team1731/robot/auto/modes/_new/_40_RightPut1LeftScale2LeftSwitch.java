package org.usfirst.frc.team1731.robot.auto.modes._new;

import org.usfirst.frc.team1731.robot.auto.AutoModeBase;
import org.usfirst.frc.team1731.robot.auto.AutoModeEndedException;
import org.usfirst.frc.team1731.robot.auto.actions.WaitAction;

public class _40_RightPut1LeftScale2LeftSwitch extends AutoModeBase {
	//
	//40. I can't think of a scenario where we would ever use this.
	//
	@Override
	protected void routine() throws AutoModeEndedException {
		// TODO Auto-generated method stub
    	runAction(new WaitAction(15));
	}

}
