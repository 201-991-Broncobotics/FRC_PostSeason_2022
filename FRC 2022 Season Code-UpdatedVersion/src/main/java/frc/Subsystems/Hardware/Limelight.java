package frc.Subsystems.Hardware;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight {
    NetworkTableEntry tx;
    NetworkTableEntry ty;
    NetworkTableEntry ta;
    public Limelight(){
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
     tx = table.getEntry("tx");
     ty = table.getEntry("ty");
     ta = table.getEntry("ta");
    }


    public double[] getValues(){
        double x = tx.getDouble(0.0);
        double y = ty.getDouble(0.0);
        double area = ta.getDouble(0.0);
        return new double[] {x, y, area};
    }

    public void showTelemetry(){
        double[] vals = getValues();
        SmartDashboard.putNumber("tx", vals[0]);
        SmartDashboard.putNumber("ty", vals[1]);
        SmartDashboard.putNumber("ta", vals[2]);
   System.out.println("LimelightX" +vals[0]);
  System.out.println("LimelightY"+ vals[1]);
System.out.println("LimelightArea"+ vals[2]);
    }
}
