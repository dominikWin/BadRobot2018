package org.usfirst.frc.team1014.robot.commands.auto;

import org.usfirst.frc.team1014.robot.commands.core.DriveStraightDistance;
import org.usfirst.frc.team1014.robot.commands.core.Spin;
import org.usfirst.frc.team1014.robot.subsystems.Drivetrain;
import org.usfirst.frc.team1014.robot.util.FieldSide;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoCentertoAB extends CommandGroup {

	double power, half;

	/**
	 * 
	 * 
	 * @param drivetrain
	 *            Drivetrain object
	 * @param direction
	 *            indicates direction(-1 for A(left), 1 for B(right))
	 * 
	 * 
	 */
	public AutoCentertoAB(Drivetrain drivetrain, FieldSide side) {
		this.addSequential(new DriveStraightDistance(drivetrain, 15.5));
		this.addSequential(new Spin(drivetrain, (-60 * side.flipAssumeRight())));
		this.addSequential(new DriveStraightDistance(drivetrain, 120)); // 140 was too long
		this.addSequential(new Spin(drivetrain, (60 * side.flipAssumeRight())));

	}

}