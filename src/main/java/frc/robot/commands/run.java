
package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Utils.pure_pursuite.Path;
import frc.robot.Utils.pure_pursuite.PurePursuit;
import frc.robot.Utils.pure_pursuite.Vector;
import frc.robot.Utils.pure_pursuite.Waypoint;
import frc.robot.subsystems.DriveTrain;

public class run extends Command {
  private Waypoint[] path1 = {
    new Waypoint(0,0),
    new Waypoint(1,1)
  };
  private Path path;
  private DriveTrain driver;
  private PurePursuit pp;
  private double lookDistance = 0.3;
  public run() {
    this.path = new Path(0.5,3,2);
    this.driver = DriveTrain.getInstance();
  }

  @Override
  protected void initialize() {
    this.path.initPath(path1);
    this.path.addVelocities(5);
    driver.resetOdometry();
    driver.setLPO(0);
    this.pp = new PurePursuit(this.path.toArray(),this.lookDistance);
    driver.setRPO(0);
  }

  @Override
  protected void execute() {
    driver.UpdateOdometry();
    double angle = driver.getAngle();
    Vector position = driver.getRobotOdometry();
    Waypoint closest = pp.getClosestPoint(this.path.toArray(), position);
    Vector look = pp.getLookaheadPoint(position,this.path.toArray());

    double curv = pp.getCurvature(angle,position, look);   
    // double sc = Math.abs(curv)*pp.getSignesCurvature(look, Math.toRadians(driver.getAngle()), position);
    // double ls = pp.getLeftTargetVelocity(closest, sc, 0.55);
    // double rs = pp.getRightTargetVelocity(closest, sc, 0.55);
    SmartDashboard.putNumber("gyro", driver.getAngle());
    SmartDashboard.putNumber("curveture", curv);
    // SmartDashboard.putNumber("LS", ls);
    // SmartDashboard.putNumber("RS", rs);
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
