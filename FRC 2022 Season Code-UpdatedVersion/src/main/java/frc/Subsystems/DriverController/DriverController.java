package frc.Subsystems.DriverController;

import frc.Subsystems.Hardware.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

public class DriverController {
    private Drivetrain dT;
    private AuxMotors aM;
    private boolean a_past = false;
    private boolean dt_state = true;
    private boolean bottom = true;



    public DriverController(AuxMotors _a, Drivetrain _d){
            dT = _d;
            aM = _a;
    } 

    public void triggeredButton(){
        bottom = true;
    }

    public void climb(Joystick gamepad){
        if(!bottom){
            aM.climb1(gamepad.getRawAxis(2) > 0.1 ? -1 : (gamepad.getRawAxis(3) > 0.1) ? 1 : 0);    
        }else if(bottom && gamepad.getRawButton(4) && gamepad.getRawAxis(2) > 0.1 ){
            bottom = !bottom;
        }else if(gamepad.getRawAxis(3) > 0.1){
            bottom = false;
            aM.climb1(1);
            Timer.delay(1.5);
        }
        
    }

    public void checkToggle(Joystick gamepad){
        if(gamepad.getRawButton(1) && !a_past){
            dt_state = !dt_state;
            dT.setMode(dt_state);
        }
        a_past= gamepad.getRawButton(1);
    }
}
