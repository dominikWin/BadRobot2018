package org.usfirst.frc.team1014.robot.commands.auto;

import org.usfirst.frc.team1014.robot.commands.core.DriveStraight;
import org.usfirst.frc.team1014.robot.commands.core.DriveStraightDistance;
import org.usfirst.frc.team1014.robot.commands.core.Spin;
import org.usfirst.frc.team1014.robot.subsystems.Drivetrain;
import org.usfirst.frc.team1014.robot.subsystems.Grabber;
import org.usfirst.frc.team1014.robot.subsystems.Lifter;
import org.usfirst.frc.team1014.robot.util.FieldConfiguration;
import org.usfirst.frc.team1014.robot.util.FieldSide;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class StartCenterScale extends CommandGroup{
	public StartCenterScale(Drivetrain driveTrain, Lifter lifter, Grabber grabber, FieldConfiguration fieldConfiguration) {
		FieldSide side = fieldConfiguration.getScale();
		
		this.addSequential(new AutoCentertoAB(driveTrain, side));
		this.addSequential(new DriveStraightDistance(driveTrain, 160)); // change so approach is from 45 degrees
		this.addSequential(new Spin(driveTrain, side.flipAssumeRight() * 45));

		this.addSequential(new AutoRaiseScale(lifter, 1));
		this.addSequential(new DriveStraight(driveTrain, .3, .4));
		this.addSequential(new AutoRelease(grabber));
		this.addSequential(new DriveStraight(driveTrain, -.3, 1));
		this.addSequential(new AutoRaiseScale(lifter, -1));
	}
}