package org.usfirst.frc.team1014.robot.commands.auto;

import org.usfirst.frc.team1014.robot.commands.core.DriveStraight;
import org.usfirst.frc.team1014.robot.subsystems.Drivetrain;
import org.usfirst.frc.team1014.robot.subsystems.Grabber;
import org.usfirst.frc.team1014.robot.subsystems.Lifter;
import org.usfirst.frc.team1014.robot.util.FieldSide;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutoSwitchLongFromCenter extends CommandGroup{
	
	/**
	 * 
	 * @param driveTrain
	 * @param direction - -1 for left, 1 for right
	 */
	public AutoSwitchLongFromCenter(Drivetrain driveTrain, Lifter lifter, Grabber grabber, FieldSide side) { //Worked at past
		this.addSequential(new AutoCenterGoToSwitchLong(driveTrain, side));
		this.addSequential(new AutoMoveCloseSwitch(driveTrain, lifter, grabber));
		this.addSequential(new DriveStraight(driveTrain, -.3, 1));
		this.addSequential(new AutoRaiseSwitch(lifter, -1));
	}
}