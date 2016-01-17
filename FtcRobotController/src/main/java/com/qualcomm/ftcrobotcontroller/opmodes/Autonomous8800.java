package com.qualcomm.ftcrobotcontroller.opmodes;

//------------------------------------------------------------------------------
//
// Autonomous8800
//
//------------------------------------------------------------------------------

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Built on the basic autonomous operational mode that uses the left and right
 * drive motors
 *
 * @author Robotinatorz
 * @version 2015-10-01
 */
public class Autonomous8800 extends OpMode

{

    //configure the motors to use
    private DcMotor motorRight;
    private DcMotor motorLeft;

    public Autonomous8800 ()

    {

    } // PushBotAuto

    @Override
    public void init() {
        //initialize the 2 main driving motors
        motorRight = hardwareMap.dcMotor.get("motor_2");
        motorLeft = hardwareMap.dcMotor.get("motor_1");
    }

    int step = 0;

    @Override
    public void loop() {

        try {
            //use a switch statment to check which operation to run
            switch(step){
                //start the driving
                case 0:
                    motorLeft.setDirection(DcMotor.Direction.FORWARD);
                    motorRight.setDirection(DcMotor.Direction.FORWARD);
                    motorRight.setPower(0.25);
                    motorLeft.setPower(0.25);
                    Thread.sleep(2500);
                    motorRight.setPower(0);
                    motorLeft.setPower(0);
                    break;
                //stop the driving
                case 1:
                    break;
            }

            step++;


        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

} // PushBotAuto
