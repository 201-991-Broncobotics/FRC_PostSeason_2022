package frc.robot;



import edu.wpi.first.cameraserver.CameraServer;

import edu.wpi.first.cscore.UsbCamera;

import edu.wpi.first.wpilibj.DigitalInput;

import edu.wpi.first.wpilibj.Joystick;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;

import frc.Subsystems.Hardware.*;
import frc.Subsystems.ObjectiveController.*;
import frc.Subsystems.DriverController.*;





public class Robot extends TimedRobot {

  private final Joystick gamepad1 = new Joystick(0);
  private final Joystick gamepad2 = new Joystick(1);
  
  private Drivetrain dT;
  private AuxMotors aM;
  private ObjectiveController oC;
  private DriverController dC;
  private DigitalInput sensorLimit;
  private double startTime;
  private double elapsedTime;
  private double startPosition;
  private final double velocityMan = 76000;
  private final boolean runAutonomous = true;
  private Limelight l;
  private double targetArea = 0.45;
  private double shooting_align_speed = 0.1;
  private int how_many_balls = 2;



  @Override
  public void robotInit() {

    //  fl  fr  bl  br
    dT = new Drivetrain(0, 2, 7, 1);

    // In   Out   Conveyor   Angle  Climb1  Climb2 
    aM = new AuxMotors(5, 6, 4, 3, 8, 9);


    oC = new ObjectiveController(aM, dT);
    dC = new DriverController(aM, dT);
    sensorLimit = new DigitalInput(0);

    
    UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
   
    camera.setResolution(256, 256);
    camera.setFPS(15);
    l = new Limelight();


  }


@Override 
public void testPeriodic(){
  double[] vals = l.getValues();
  double LimeX = vals[0];
  double LimeY = vals[1];
  double LimeA = vals[2];
l.showTelemetry();
/*
  //TODO make while
  if(LimeX < 1){
    System.out.println("Right Forward, Left Backward");
  }else if(LimeX > -1){
    System.out.println("Left Forward, Right Backward");
  }
  if(LimeA > 1){
    System.out.println("Move backwards");
  }else if(LimeA < 0.8){
    System.out.println("Move forwards");
  }
  Timer.delay(1);
 // l.showTelemetry();*/

}

 @Override
 public void teleopInit(){
    

  l.showTelemetry();

  System.out.println("here1");
 }
  

  @Override
  public void teleopPeriodic() 
  {
   
    double lsy = (double) gamepad1.getRawAxis(1);
    double rsx = (double) gamepad1.getRawAxis(4);
    boolean x_button = gamepad1.getRawButton(3);

    //limelight.showTelemetry();

    dT.slowDownFactor = x_button ? 2 : 1;
    dT.tankDrive(lsy, rsx);

    oC.buttons(gamepad2);
    //oC.climb(gamepad2);
    oC.stick(gamepad2);

    dC.climb(gamepad1);
    dC.checkToggle(gamepad1);

    if(gamepad1.getRawButton(2)){
      System.out.println("here");
      l.showTelemetry();
      alignShooter();
      dT.setMode(false);

    }else if(gamepad1.getRawButton(6)){
      double newA = l.getValues()[2];
      if(newA != 0){
        System.out.println("Successfully Set Position");
        targetArea = newA;
      }
      
    }

   

  }








  @Override
  public void autonomousInit(){
    if(runAutonomous){
      
    startTime = Timer.getFPGATimestamp();
    startPosition = dT.position();

    switch(how_many_balls){
      case 1:
       autonomousMain1();
       break;

      case 2:
       autonomousMain();
       break;
    }
    
    }
  }





  @Override
  public void autonomousPeriodic(){
   
    
  }


  private void autonomousMain(){

    dropIntake();

    dT.travelTo(-50000 + startPosition);

    autonomousShoot();
  
    System.out.println("Completed 1st shot");

    aM.setStorage(-0.3);

    dT.travelTo(-25000 + dT.position());

    Timer.delay(1);
    aM.setIntake(0);

    aM.setIntakeAngle(0.7);

    Timer.delay(0.775);

    aM.setIntakeAngle(0);

    Timer.delay(1);
    aM.setStorage(-0.2);

    Timer.delay(0.5);

    aM.setShooter(0);



    dT.travelTo(45000 + dT.position());



    autonomousShoot();

    dT.travelTo(-50000 + dT.position());

    dT.setMode(false);

  }


  private void autonomousMain1(){
    dT.travelTo(-50000 + startPosition);

    autonomousShoot();
  
    System.out.println("Completed 1st shot");

    dT.travelTo(-40000 + dT.position());

    dT.setMode(false);
  }


public void autonomousShoot(){
  
  aM.manualShooter(-0.75);

  Timer.delay(1.75);

  aM.setStorage(0.6);

  Timer.delay(0.75);

  aM.setStorage(0);

  aM.manualShooter(0);
}

public void dropIntake(){
    aM.setIntakeAngle(-0.7);

    Timer.delay(0.75);

    aM.setIntakeAngle(0);

    aM.setIntake(0.5);

    Timer.delay(1.05);
}

public int alignShooter(){
  double[] vals = l.getValues();
  double LimeX = vals[0];
  double LimeY = vals[1];
  double LimeA = vals[2];
  l.showTelemetry();

  dT.setMode(true);
  if(LimeA != 0){
 
  while(LimeX < 1){
    dT.setPower(shooting_align_speed, -shooting_align_speed, false);
    LimeX = l.getValues()[0];
    if(gamepad1.getRawButton(5)){
      return 0;
    }
  }

  dT.setPower(0, 0, false);

  while(LimeX > -1){
     dT.setPower(-shooting_align_speed/2, shooting_align_speed/2, false);
     LimeX = l.getValues()[0];
     if(gamepad1.getRawButton(5)){
      return 0;
    }
  }

  dT.setPower(0, 0, false);

  while(LimeA > targetArea + 0.05){
    dT.setPower(-shooting_align_speed, -shooting_align_speed, false);
    LimeA = l.getValues()[2];
    if(gamepad1.getRawButton(5)){
      return 0;
    }
  }
  
  dT.setPower(0, 0, false);

  while(LimeA < targetArea - 0.05){
    dT.setPower(shooting_align_speed/2, shooting_align_speed/2, false);
    LimeA = l.getValues()[2];
    if(gamepad1.getRawButton(5)){
      return 0;
    }
  }

  dT.setPower(0, 0, false);
}
return 0;
}

}
