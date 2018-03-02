package org.usfirst.frc.team1014.robot.commands.teleop;

import org.usfirst.frc.team1014.robot.subsystems.Grabber;
import org.usfirst.frc.team1014.robot.subsystems.Lifter;
import org.usfirst.frc.team1014.robot.util.LogUtil;

import badlog.lib.BadLog;
import badlog.lib.DataInferMode;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Command;

public class TeleGrab extends Command {
	private XboxController controller;
	private Grabber grabber;
	private Lifter lifter;

	boolean grabState = false, grabDown = false;
	long startLastGrab = 0;

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub
		if (controller.getTriggerAxis(Hand.kRight) > .3) {
			// Collect cubes
			grabber.turnCollect(1);
			grabState = false;
		} else if (controller.getBumper(Hand.kRight)) {
			// release
			grabber.turnRelease(.6);
			grabState = false;
		} else {
			if (grabState) {
				grabber.turnCollect(isGrabbing() ? .2 : 0);
				controller.setRumble(RumbleType.kLeftRumble, isGrabbing() ? 1 : 0);
				controller.setRumble(RumbleType.kRightRumble, isGrabbing() ? 1 : 0);

			} else {
				grabber.turnCollect(0);
				controller.setRumble(RumbleType.kLeftRumble, 0);
				controller.setRumble(RumbleType.kRightRumble, 0);
			}
		}

		if (controller.getAButton()) {
			if (!grabDown) {
				grabDown = true;
				grabState = !grabState;
				startLastGrab = System.currentTimeMillis();
			}
		} else {
			grabDown = false;
		}

		BadLog.publish("Grabber/Heartbeat", LogUtil.fromBool(grabState));

		{
			double speed = (controller.getBumper(Hand.kLeft) ? 1 : 0)
					- (controller.getTriggerAxis(Hand.kLeft) > .5 ? 1 : 0);
			boolean overrideLimits = false;
			
			if(Math.abs(controller.getY(Hand.kRight)) > .05) {
				speed = -controller.getY(Hand.kRight);
				overrideLimits = true;
			}
			
			lifter.move(speed, overrideLimits);
		}
	}

	private boolean isGrabbing() {
		return (System.currentTimeMillis() - startLastGrab) % 1000 < 250;
	}

	public TeleGrab(XboxController controller, Grabber grabber, Lifter lifter) {
		requires(grabber);
		requires(lifter);
		this.controller = controller;
		this.grabber = grabber;
		this.lifter = lifter;

		BadLog.createTopicSubscriber("Grabber/Heartbeat", "bool", DataInferMode.DEFAULT);
	}

}
