package org.usfirst.frc.team1014.robot.subsystems;

import org.usfirst.frc.team1014.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import badlog.lib.BadLog;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Grabber extends Subsystem {

	private TalonSRX rightWheel, leftWheel;

	public Grabber() {
		rightWheel = new TalonSRX(RobotMap.GRABBER_RIGHT_1_ID);
		leftWheel = new TalonSRX(RobotMap.GRABBER_LEFT_1_ID);

		BadLog.createTopic("Grabber/Right Output Percent", BadLog.UNITLESS, () -> rightWheel.getMotorOutputPercent(),
				"hide", "join:Grabber/Output Percents");
		BadLog.createTopic("Grabber/Left Output Percent", BadLog.UNITLESS, () -> leftWheel.getMotorOutputPercent(), "hide",
				"join:Grabber/Output Percents");

		BadLog.createTopic("Grabber/Right Current", "A", () -> rightWheel.getOutputCurrent(), "hide",
				"join:Grabber/Output Currents");
		BadLog.createTopic("Grabber/Left Current", "A", () -> leftWheel.getOutputCurrent(), "hide",
				"join:Grabber/Output Currents");
		
		BadLog.createTopic("Grabber/Right Voltage", "V", () -> rightWheel.getMotorOutputVoltage(), "hide",
				"join:Grabber/Output Voltages");
		BadLog.createTopic("Grabber/Left Voltage", "V", () -> leftWheel.getMotorOutputVoltage(), "hide",
				"join:Grabber/Output Voltages");
	}

	public void turnRelease(double wheelSpeed) {
		leftWheel.set(ControlMode.PercentOutput, wheelSpeed);
		rightWheel.set(ControlMode.PercentOutput, -wheelSpeed);
	}

	public void turnCollect(double wheelSpeed) {
		leftWheel.set(ControlMode.PercentOutput, -wheelSpeed);
		rightWheel.set(ControlMode.PercentOutput, wheelSpeed);
	}

	@Override
	protected void initDefaultCommand() {
	}
}
