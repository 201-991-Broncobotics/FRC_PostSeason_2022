package frc.Subsystems.ObjectiveController;

public class ObjectiveLogic {
    public static double getRequiredSpeed(double dist){
        return (dist/(0.0871557427*Math.sqrt((dist*11.4300523-0.693)/9.80665)));
    }

    public static double convertVelocity(double input){
        return (input * 2048.0/600.0);
    }
}
