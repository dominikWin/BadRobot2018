package org.usfirst.frc.team1014.robot.commands.auto;

import org.usfirst.frc.team1014.robot.commands.core.DriveStraightDistance;
import org.usfirst.frc.team1014.robot.commands.core.Spin;
import org.usfirst.frc.team1014.robot.subsystems.Drivetrain;
import org.usfirst.frc.team1014.robot.subsystems.Grabber;
import org.usfirst.frc.team1014.robot.subsystems.Lifter;
import org.usfirst.frc.team1014.robot.util.FieldSide;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoSwitchShortFromCenter extends CommandGroup {

	/**
	 * 
	 * @param driveTrain
	 * @param direction
	 *            - -1 for left, 1 for right
	 */
	public AutoSwitchShortFromCenter(Drivetrain driveTrain, Lifter lifter, Grabber grabber, FieldSide side) {
		this.addSequential(new AutoCentertoAB(driveTrain, side));
		this.addSequential(new DriveStraightDistance(driveTrain, 20));
		this.addSequential(new Spin(driveTrain, 90 * side.flipAssumeRight()));
		this.addSequential(new AutoMoveCloseSwitch(driveTrain, lifter, grabber));
	}
}
