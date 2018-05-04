package org.usfirst.frc.team1014.robot.subsystems;

import org.usfirst.frc.team1014.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import badlog.lib.BadLog;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Climber extends Subsystem {

	private TalonSRX climberMotor;

	public Climber() {
		climberMotor = new TalonSRX(RobotMap.CLIMBER_1_ID);

		BadLog.createTopic("Climber/Climber Output Percent", BadLog.UNITLESS,
				() -> climberMotor.getMotorOutputPercent());

		BadLog.createTopic("Climber/Climber Current", "A", () -> climberMotor.getOutputCurrent());

		BadLog.createTopic("Climber/Climber Voltage", "V", () -> climberMotor.getMotorOutputVoltage());
	}

	public void climb(double speed) {
		climberMotor.set(ControlMode.PercentOutput, speed);
	}

	@Override
	protected void initDefaultCommand() {
	}

}
