package org.usfirst.frc.team1731.robot;

import java.util.function.Supplier;

import org.json.simple.JSONArray;
import org.usfirst.frc.team1731.robot.auto.AutoModeBase;
import org.usfirst.frc.team1731.robot.auto.modes.*;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Class that allows a user to select which autonomous mode to execute from the web dashboard.
 */
public class AutoModeSelector {

    public static final String AUTO_OPTIONS_DASHBOARD_KEY = "auto_options";
    public static final String SELECTED_AUTO_MODE_DASHBOARD_KEY = "selected_auto_mode";

    private static class AutoModeCreator {
        private final String mDashboardName;
        private final Supplier<AutoModeBase> mCreator;

        private AutoModeCreator(String dashboardName, Supplier<AutoModeBase> creator) {
            mDashboardName = dashboardName;
            mCreator = creator;
        }
    }

    private static final AutoModeCreator mDefaultMode = new AutoModeCreator(
            "AutoDetect Alliance Gear than Hopper Shoot",
            () -> new AutoDetectAllianceSwitchThenPlaceMode());
    private static final AutoModeCreator[] mAllModes = {
    		new AutoModeCreator("Auto test (from right side): Place cube on right switch", () -> new PlaceOnRightSwitch()),
    		new AutoModeCreator("Auto test (from left side): Place cube on left switch", () -> new PlaceOnLeftSwitch()),
            new AutoModeCreator("Left: Put cube on left switch", () -> new LeftPutCubeOnLeftSwitch()),
            new AutoModeCreator("Left: Put cube on left switch and left scale", () -> new LeftPutCubeOnLeftSwitchAndLeftScale()),
            new AutoModeCreator("Left: Put cube on right switch", () -> new LeftPutCubeOnRightSwitch()),
            new AutoModeCreator("Middle-Left: Put cube on left switch", () -> new MiddleLeftPutCubeOnLeftSwitch()),
            new AutoModeCreator("Middle-Left: Put cube on right switch", () -> new MiddleLeftPutCubeOnRightSwitch()),
            new AutoModeCreator("Middle-Right: Put cube on left switch", () -> new MiddleRightPutCubeOnLeftSwitch()),
            new AutoModeCreator("Middle-Right: Put cube on right switch", () -> new MiddleRightPutCubeOnRightSwitch()),
            new AutoModeCreator("Right: Put cube on left switch", () -> new RightPutCubeOnLeftSwitch()),
            new AutoModeCreator("Right: Put cube on right switch", () -> new RightPutCubeOnRightSwitch()),
            new AutoModeCreator("Right: Put cube on right switch and right scale", () -> new RightPutCubeOnRightSwitchAndRightScale()),
            new AutoModeCreator("Standstill", () -> new StandStillMode()),
    };

    public static void initAutoModeSelector() {
        JSONArray modesArray = new JSONArray();
        for (AutoModeCreator mode : mAllModes) {
            modesArray.add(mode.mDashboardName);
        }
        SmartDashboard.putString(AUTO_OPTIONS_DASHBOARD_KEY, modesArray.toString());
        SmartDashboard.putString(SELECTED_AUTO_MODE_DASHBOARD_KEY, mDefaultMode.mDashboardName);
    }

    public static AutoModeBase getSelectedAutoMode() {
        String selectedModeName = SmartDashboard.getString(
                SELECTED_AUTO_MODE_DASHBOARD_KEY,
                "NO SELECTED MODE!!!!");
        for (AutoModeCreator mode : mAllModes) {
            if (mode.mDashboardName.equals(selectedModeName)) {
                return mode.mCreator.get();
            }
        }
        DriverStation.reportError("Failed to select auto mode: " + selectedModeName, false);
        return mDefaultMode.mCreator.get();
    }
}
