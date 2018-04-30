package org.usfirst.frc.team1014.robot.util;

public enum FieldSide {
	LEFT, RIGHT;
	
	public double flipAssumeRight() {
		return this == RIGHT ? 1 : -1;
	}
}
