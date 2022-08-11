package frc.Subsystems.ObjectiveController;

import java.text.DecimalFormat;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import frc.Subsystems.Hardware.AuxMotors;
import frc.Subsystems.Hardware.Drivetrain;


public class ObjectiveController extends VarNames{


    public ObjectiveController(AuxMotors _a, Drivetrain _d){
        dT = _d;
        aM = _a;
    }
    public void buttons(Joystick gamepad){

        if(gamepad.getRawButton(1)){
            b_motor = false;
            a_motor = !a_past ? !a_motor : a_motor;
            if(!a_past) aM.setIntake(a_motor ? -0.4 : 0);
        }

        if(gamepad.getRawButton(2)){
            a_motor = false;
            b_motor = !b_past ? !b_motor : b_motor;
        
            
            if(!b_past) aM.setIntake(b_motor ? 0.6 : 0);
        }

        if(gamepad.getRawButton(4)){
           /* 
            aM.setStorage(0.6);
        }else{
            if(y_past){
                aM.setStorage(0);
            }*/
            
            y_motor = !y_past ? !y_motor : y_motor;
            if(!y_past) aM.setStorage(y_motor ? 0.4 : 0);
            if(!y_past) shooter_backward = y_motor;
            
        }

        if(gamepad.getRawButton(3)){
            y_motor = false;
            aM.setStorage(-0.6);
        }else{
            if(x_past){
                aM.setStorage(0);
            }
        }

        if(gamepad.getRawButton(5)){
           aM.err_factor -= !lb_past ? 0.05 : 0; 
           if(!lb_past){
               
            System.out.format("High Goal: %.2f \n", (aM.err_factor + 0.75));
            System.out.format("Low Goal: %.2f \n", (aM.err_factor + 0.4));
            System.out.println("----------------------");
           }
        }
        if(gamepad.getRawButton(6)){
            aM.err_factor += !rb_past ? 0.05 : 0;
            if(!rb_past){
                
            System.out.format("High Goal: %.2f \n", (aM.err_factor + 0.75));
            System.out.format("Low Goal: %.2f \n", (aM.err_factor + 0.4));
            System.out.println("----------------------");
            }
        }

        

        a_past = gamepad.getRawButton(1);
        b_past = gamepad.getRawButton(2);
        x_past = gamepad.getRawButton(3);
        y_past = gamepad.getRawButton(4);
        lb_past = gamepad.getRawButton(5);
        rb_past = gamepad.getRawButton(6);
    }

    public void stick(Joystick gamepad) {

        if(gamepad.getRawAxis(2) > 0.1){
            if(shooter_backward && !already_running){
                aM.outtake.setNeutralMode(NeutralMode.Brake);
                aM.manualShooter(0);
                Timer.delay(0.2);
                aM.outtake.setNeutralMode(NeutralMode.Coast);
                shooter_backward = false;
                sameer_thing = true;
            }
            already_running = true;
            aM.manualShooter(-0.4);
        }else if(gamepad.getRawAxis(3) > 0.1){
            if(shooter_backward && !already_running){
                aM.outtake.setNeutralMode(NeutralMode.Brake);
                aM.manualShooter(0);
                Timer.delay(0.2);
                aM.outtake.setNeutralMode(NeutralMode.Coast);
                shooter_backward = false;
                sameer_thing = true;
            }
            already_running = true;
            aM.manualShooter(-0.7);
        }else if(shooter_backward){
            if(sameer_thing){
            aM.outtake.setNeutralMode(NeutralMode.Brake);
            aM.manualShooter(0);
            Timer.delay(0.2);
            aM.outtake.setNeutralMode(NeutralMode.Coast);
            sameer_thing = false;
            }
            already_running = false;
            aM.manualShooter(0.05);
            
        }else{
            aM.manualShooter(0);
        }

        aM.setIntakeAngle(gamepad.getRawAxis(1) < -0.5 ? 0.6 : gamepad.getRawAxis(1) > 0.5 ? -0.6 : 0);
    }

    public void climb(Joystick gamepad){
        if(!bottom){
            aM.climb1(gamepad.getRawAxis(2) > 0.1 ? -1 : (gamepad.getRawAxis(3) > 0.1) ? 1 : 0);    
        }else if(bottom && gamepad.getRawButton(4) && gamepad.getRawAxis(2) > 0.1 ){

        } if(gamepad.getRawAxis(3) > 0.1){
            bottom = false;
            aM.climb1(1);
            Timer.delay(1.5);
        }
        
    }

}
