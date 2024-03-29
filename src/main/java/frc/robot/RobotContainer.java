// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.DriveSubsystem;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final DriveSubsystem DRIVE_SUBSYSTEM = new DriveSubsystem(DriveSubsystem.initizalizeHardware());
  private final CommandXboxController  PRIMARY_CONTROLLER = new CommandXboxController(Constants.HID.PRIMARY_CONTROLLER_PORT);

  private static final SendableChooser<SequentialCommandGroup> m_automodeChooser = new SendableChooser<>();
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    DRIVE_SUBSYSTEM.setDefaultCommand(
       new RunCommand(() -> DRIVE_SUBSYSTEM.teleop(PRIMARY_CONTROLLER.getLeftY(), 
       PRIMARY_CONTROLLER.getLeftX()), 
       DRIVE_SUBSYSTEM
    )
    );
    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {}

  private void autoModeChooser(){
    m_automodeChooser.setDefaultOption("Do Nothing", new SequentialCommandGroup());
    m_automodeChooser.addOption("Drive Forward", new SequentialCommandGroup(
      DRIVE_SUBSYSTEM.run(() -> DRIVE_SUBSYSTEM.set(0.5, 0.0)
      .withTimeout(5)
      .andThen(() -> DRIVE_SUBSYSTEM
      .stop()))
      )

    );
    m_automodeChooser.addOption("Drive Forward and do a little jig", new SequentialCommandGroup(
      DRIVE_SUBSYSTEM.run(() -> DRIVE_SUBSYSTEM.set(0.5, 0.0)
      .withTimeout(3)
      .andThen( () -> DRIVE_SUBSYSTEM.run(() -> DRIVE_SUBSYSTEM.set(0.0, 90))
      .withTimeout(3)
      .andThen( () -> DRIVE_SUBSYSTEM.run(() -> DRIVE_SUBSYSTEM.set(0.0, -45))
      .withTimeout(6)
      .andThen( () -> DRIVE_SUBSYSTEM.run( () -> DRIVE_SUBSYSTEM.stop()
       ) ) ))
    )))   ;

  }
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return m_automodeChooser.getSelected();
  }
}
