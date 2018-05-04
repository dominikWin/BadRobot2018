package org.usfirst.frc.team1014.robot.util;

import edu.wpi.first.wpilibj.DriverStation;

public class FieldConfiguration {

	private FieldSide ourSwitch, scale, opponentSwitch;

	public FieldSide getOurSwitch() {
		return ourSwitch;
	}

	public FieldSide getScale() {
		return scale;
	}

	public FieldSide getOpponentSwitch() {
		return opponentSwitch;
	}

	public FieldConfiguration() {
		String fmsData = DriverStation.getInstance().getGameSpecificMessage();

		if (fmsData == null || fmsData.isEmpty() || fmsData.length() != 3) {
			// Something is really wrong
			System.out.println("Bad fms data: '" + fmsData + "'");
			System.err.println("Bad fms data: '" + fmsData + "'");
		}

		ourSwitch = getSide(fmsData.charAt(0));
		scale = getSide(fmsData.charAt(1));
		opponentSwitch = getSide(fmsData.charAt(2));
	}

	private FieldSide getSide(char c) {
		if (c == 'L')
			return FieldSide.LEFT;
		if (c == 'R')
			return FieldSide.RIGHT;

		System.out.println("Bad fms char: '" + c + "'");
		System.err.println("Bad fms char: '" + c + "'");

		// Yes, this is wrong, but its really unlikely it will run
		return FieldSide.LEFT;
	}
}
