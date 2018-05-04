package org.usfirst.frc.team1014.robot.subsystems;

import org.usfirst.frc.team1014.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import badlog.lib.BadLog;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Lifter extends Subsystem {

	private TalonSRX liftMotor;

	public Lifter() {

		liftMotor = new TalonSRX(RobotMap.LIFT_1_ID);
		
		BadLog.createTopic("Lift/Lifter Output Percent", BadLog.UNITLESS, () -> liftMotor.getMotorOutputPercent());

		BadLog.createTopic("Lift/Lifter Current", "A", () -> liftMotor.getOutputCurrent());

		BadLog.createTopic("Lift/Lifter Voltage", "V", () -> liftMotor.getMotorOutputVoltage());
	}
	
	public boolean isAtBottomLimit() {
		return !liftMotor.getSensorCollection().isRevLimitSwitchClosed();
	}
	

	public void move(double speed, boolean overrideLimits) {
		liftMotor.overrideLimitSwitchesEnable(!overrideLimits);
		liftMotor.set(ControlMode.PercentOutput, speed);
	}

	@Override
	protected void initDefaultCommand() {
	}

}