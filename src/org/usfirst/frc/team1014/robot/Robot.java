package org.usfirst.frc.team1014.robot;

import java.util.Optional;

import org.usfirst.frc.team1014.robot.commands.Autonomous;
import org.usfirst.frc.team1014.robot.commands.Teleop;
import org.usfirst.frc.team1014.robot.commands.auto.AutoDelay;
import org.usfirst.frc.team1014.robot.commands.auto.AutoMode;
import org.usfirst.frc.team1014.robot.commands.auto.Prohibit;
import org.usfirst.frc.team1014.robot.commands.auto.StartCenterScale;
import org.usfirst.frc.team1014.robot.commands.auto.StartCenterSwitch;
import org.usfirst.frc.team1014.robot.commands.auto.StartSide;
import org.usfirst.frc.team1014.robot.subsystems.Climber;
import org.usfirst.frc.team1014.robot.subsystems.Drivetrain;
import org.usfirst.frc.team1014.robot.subsystems.Grabber;
import org.usfirst.frc.team1014.robot.subsystems.Lifter;
import org.usfirst.frc.team1014.robot.util.FieldConfiguration;
import org.usfirst.frc.team1014.robot.util.FieldSide;
import org.usfirst.frc.team1014.robot.util.LogUtil;

import badlog.lib.BadLog;
import badlog.lib.DataInferMode;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
	public static OI oi;

	private Drivetrain driveTrain;
	private Lifter lifter;
	private Grabber grabber;
	private Climber climber;

	private Teleop teleopCG;
	private Autonomous autoCG;

	private BadLog logger;
	private long startTimeNS;
	private long lastLog;

	private SendableChooser<AutoMode> autoChooser;
	private SendableChooser<Prohibit> prohibitChooser;

	private boolean initialized = false;

	/*
	 * Not a real init, delay until we have info. If there is no driver station
	 * attached, logging is not necessary.
	 */
	@Override
	public void robotInit() {
		if (shouldInit())
			init();
	}

	/*
	 * Checks to see if driver station is attached.
	 */
	private boolean shouldInit() {
		if (!DriverStation.getInstance().isDisabled())
			return true;

		if (DriverStation.getInstance().isDSAttached())
			return true;

		return false;
	}

	private void init() {
		if (initialized) {
			System.out.println("||| ATTEMPTED TO INIT SECOND TIME |||");
			System.err.println("||| ATTEMPTED TO INIT SECOND TIME |||");
			return;
		}
		initialized = true;

		startTimeNS = System.nanoTime();
		lastLog = System.currentTimeMillis();
		String session = LogUtil.genSessionName();
		System.out.println("Info: Starting session " + session);
		logger = BadLog.init("/home/lvuser/log/" + session + ".bag");
		{
			BadLog.createValue("Start Time", LogUtil.getTimestamp());
			BadLog.createValue("Event Name",
					Optional.ofNullable(DriverStation.getInstance().getEventName()).orElse(""));
			BadLog.createValue("Match Type", DriverStation.getInstance().getMatchType().toString());
			BadLog.createValue("Match Number", "" + DriverStation.getInstance().getMatchNumber());
			BadLog.createValue("Alliance", DriverStation.getInstance().getAlliance().toString());
			BadLog.createValue("Location", "" + DriverStation.getInstance().getLocation());

			BadLog.createTopicSubscriber("Time", "s", DataInferMode.DEFAULT, "hide", "delta", "xaxis");

			BadLog.createTopicStr("System/Browned Out", "bool", () -> LogUtil.fromBool(RobotController.isBrownedOut()));
			BadLog.createTopic("System/Battery Voltage", "V", () -> RobotController.getBatteryVoltage());
			BadLog.createTopicStr("System/FPGA Active", "bool", () -> LogUtil.fromBool(RobotController.isSysActive()));
			BadLog.createTopic("Match Time", "s", () -> DriverStation.getInstance().getMatchTime());

			oi = new OI();
			driveTrain = new Drivetrain();
			grabber = new Grabber();
			lifter = new Lifter();
			climber = new Climber();

			teleopCG = new Teleop(driveTrain, grabber, lifter, climber);
			autoCG = new Autonomous(driveTrain, lifter, grabber);

			autoChooser = new SendableChooser<AutoMode>();
			prohibitChooser = new SendableChooser<Prohibit>();

			autoChooser.addDefault("Center Switch", AutoMode.CENTER_SWITCH);
			autoChooser.addObject("Right Side", AutoMode.RIGHT);
			autoChooser.addObject("Left Side", AutoMode.LEFT);

			autoChooser.addObject("Center Scale", AutoMode.CENTER_SCALE);

			prohibitChooser.addDefault("None", Prohibit.NONE);
			prohibitChooser.addObject("No switch", Prohibit.NO_SWITCH);
			prohibitChooser.addObject("No Scale", Prohibit.NO_SCALE);

			SmartDashboard.putNumber("Delay", 0);
			SmartDashboard.putData("Prohibit Chooser", prohibitChooser);
			SmartDashboard.putData("Autonomous Mode Chooser", autoChooser);

			CameraServer.getInstance().startAutomaticCapture();
		}
		logger.finishInitialization();
	}

	@Override
	public void autonomousInit() {
		if (!initialized)
			init();

		Scheduler.getInstance().removeAll();

		driveTrain.resetAHRS();

		FieldConfiguration fieldConfiguration = new FieldConfiguration();

		autoCG.addSequential(new AutoDelay(SmartDashboard.getNumber("Delay", 0)));

		switch ((AutoMode) autoChooser.getSelected()) {

		case CENTER_SCALE:
			autoCG.addSequential(new StartCenterScale(driveTrain, lifter, grabber, fieldConfiguration));
			break;
		case RIGHT:
			autoCG.addSequential(new StartSide(driveTrain, lifter, grabber, (Prohibit) prohibitChooser.getSelected(),
					FieldSide.RIGHT, fieldConfiguration));
			break;

		case LEFT:
			autoCG.addSequential(new StartSide(driveTrain, lifter, grabber, (Prohibit) prohibitChooser.getSelected(),
					FieldSide.LEFT, fieldConfiguration));
			break;
		default: // Center Switch
			autoCG.addSequential(new StartCenterSwitch(driveTrain, lifter, grabber, fieldConfiguration));

		}

		autoCG.start();
	}

	@Override
	public void teleopInit() {
		if (!initialized)
			init();

		Scheduler.getInstance().removeAll();

		teleopCG.start();
	}

	@Override
	public void testInit() {
		if (!initialized)
			init();
		Scheduler.getInstance().removeAll();
	}

	@Override
	public void disabledInit() {
		// Do not init robot here, this will happen even without DS
		Scheduler.getInstance().removeAll();
	}

	@Override
	public void autonomousPeriodic() {
		periodic();
	}

	@Override
	public void teleopPeriodic() {

		periodic();
	}

	@Override
	public void testPeriodic() {
		periodic();
	}

	@Override
	public void disabledPeriodic() {
		if (!initialized) {
			if (shouldInit())
				init();
			else
				return;
		}

		periodic();
	}

	private void periodic() {
		double currentTime = ((double) (System.nanoTime() - startTimeNS)) / 1_000_000_000d;
		BadLog.publish("Time", currentTime);

		Scheduler.getInstance().run();

		logger.updateTopics();
		// Only log once every 250ms in disabled, to save disk space
		long currentMS = System.currentTimeMillis();
		if (!DriverStation.getInstance().isDisabled() || (currentMS - lastLog) >= 250) {
			lastLog = currentMS;
			logger.log();
		}
	}
}