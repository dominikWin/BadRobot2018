package org.usfirst.frc.team1014.robot.commands.auto;

import org.usfirst.frc.team1014.robot.commands.core.DriveStraightDistance;
import org.usfirst.frc.team1014.robot.subsystems.Drivetrain;
import org.usfirst.frc.team1014.robot.subsystems.Grabber;
import org.usfirst.frc.team1014.robot.subsystems.Lifter;
import org.usfirst.frc.team1014.robot.util.FieldConfiguration;
import org.usfirst.frc.team1014.robot.util.FieldSide;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class StartSide extends CommandGroup {
	/**
	 * 
	 * @param driveTrain
	 * @param lifter
	 * @param grabber
	 * @param prohibit
	 *            - object to not do, 0 for none, 1 for switch, 2 for scale
	 * @param scaleSide
	 *            - 1 for right, -1 for left
	 * @param switchSide
	 *            - 1 for right, -1 for left
	 */
	public StartSide(Drivetrain driveTrain, Lifter lifter, Grabber grabber, Prohibit prohibit, FieldSide ourSide,
			FieldConfiguration fieldConfiguration) {
		if (fieldConfiguration.getScale() == ourSide && prohibit != Prohibit.NO_SCALE) {
			this.addSequential(new AutoRLScale(driveTrain, lifter, grabber, fieldConfiguration.getScale()));
		} else if (fieldConfiguration.getOurSwitch() == ourSide && prohibit != Prohibit.NO_SWITCH) {
			this.addSequential(new AutoRLSwitch(driveTrain, lifter, grabber, fieldConfiguration.getOurSwitch()));
		} else {
			this.addSequential(new DriveStraightDistance(driveTrain, 100));
		}

	}

}
