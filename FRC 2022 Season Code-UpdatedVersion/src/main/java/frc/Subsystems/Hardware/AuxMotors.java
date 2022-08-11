package frc.Subsystems.Hardware;

import java.util.ArrayList;

import javax.xml.xpath.XPathEvaluationResult;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.NeutralMode;


import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import frc.Subsystems.Variables;



public class AuxMotors implements Variables
{
    // defines the motors and other sensors
 
    TalonFX intake;
    public TalonFX outtake;
    TalonFX conveyor;
    TalonSRX intake_angle;
    TalonSRX climber1;
    TalonSRX climber2;

    public double err_factor = 0.0;

    BuiltInAccelerometer gyro = new BuiltInAccelerometer();
   
    public AuxMotors(int __intake__, int __outtake__, int __conveyor__, int __intakeAngle__, int __climber1__, int __climber2__)
    {
        // defines names for the motors
        intake = new TalonFX(__intake__);
        outtake = new TalonFX(__outtake__);
        conveyor = new TalonFX(__conveyor__);
        intake_angle = new TalonSRX(__intakeAngle__);
        climber1 = new TalonSRX(__climber1__);
        climber2 = new TalonSRX(__climber2__);

        intake.setNeutralMode(NeutralMode.Coast);
        outtake.setNeutralMode(NeutralMode.Coast);
        conveyor.setNeutralMode(NeutralMode.Brake);
        intake_angle.setNeutralMode(NeutralMode.Brake);
        climber1.setNeutralMode(NeutralMode.Brake);
        climber2.setNeutralMode(NeutralMode.Brake);

        ArrayList<TalonFX> motorsFx = new ArrayList<TalonFX>();

        motorsFx.add(intake);
        motorsFx.add(outtake);
        motorsFx.add(conveyor);

        for(TalonFX m : motorsFx){
            m.configVoltageCompSaturation(12);
            m.enableVoltageCompensation(true);
        }

        


        // reverses motors so that you ovefoward with positive values
        intake.setInverted(false);
        outtake.setInverted(false);

    }

    public void setIntake(double speed){
        intake.set(ControlMode.PercentOutput, speed);
    }
    public void setShooter(double speed){
        outtake.set(TalonFXControlMode.Velocity, speed * (1+err_factor));
    }
    public void manualShooter(double speed){
        outtake.set(TalonFXControlMode.PercentOutput, Math.max(Math.min(speed * (1+err_factor), 1), -1));
    }
    public void setStorage(double speed){
        conveyor.set(ControlMode.PercentOutput, speed);
    }
    public void setIntakeAngle(double speed) {
        intake_angle.set(ControlMode.PercentOutput, speed);
    }
    public void climb1(double speed){
        climber1.set(ControlMode.PercentOutput, speed);
    }
    public void climb2(double speed){
        climber2.set(ControlMode.PercentOutput, speed);
    }
    

    
}

