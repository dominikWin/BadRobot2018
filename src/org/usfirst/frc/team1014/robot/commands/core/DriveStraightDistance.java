package org.usfirst.frc.team1014.robot.commands.core;

import org.usfirst.frc.team1014.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;

public class DriveStraightDistance extends Command {
	private Drivetrain driveTrain;
	private double seconds;
	private double speed;

	private long startTime;

	private double linRegOffset = -11.6;
	private double linRegScaler = 91.8;

	/**
	 * 
	 * @param driveTrain
	 * @param distance
	 *            - distance in inches
	 */

	public DriveStraightDistance(Drivetrain driveTrain, double distance) {
		this.speed = .7;

		// Inverse of graph from linear regression
		this.seconds = (distance - linRegOffset) / linRegScaler;
		requires(driveTrain);
		this.driveTrain = driveTrain;
	}

	@Override
	protected void initialize() {
		startTime = System.currentTimeMillis();
	}

	@Override
	protected void execute() {
		driveTrain.driveStraight(speed);
	}

	@Override
	protected boolean isFinished() {
		return (System.currentTimeMillis() - startTime) > (int) (1000d * seconds);
	}

	@Override
	protected void end() {
		driveTrain.directDrive(0, 0);
	}

}
