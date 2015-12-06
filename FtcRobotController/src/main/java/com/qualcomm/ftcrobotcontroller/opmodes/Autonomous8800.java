package com.qualcomm.ftcrobotcontroller.opmodes;

//------------------------------------------------------------------------------
//
// PushBotAuto
//

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Provide a basic autonomous operational mode that uses the left and right
 * drive motors and associated encoders implemented using a state machine for
 * the Push Bot.
 *
 * @author SSI Robotics
 * @version 2015-08-01-06-01
 */
public class Autonomous8800 extends OpMode

{
    private DcMotor motorRight;
    private DcMotor motorLeft;

    public Autonomous8800 ()

    {

    } // PushBotAuto

    @Override
    public void init() {
        motorRight = hardwareMap.dcMotor.get("motor_2");
        motorLeft = hardwareMap.dcMotor.get("motor_1");
    }

    @Override
    public void loop() {

        try {
            motorRight.setPower(1);
//            motorLeft.setPower(1);
            Thread.sleep(5000);
            motorRight.setPower(0);
            motorLeft.setPower(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

} // PushBotAuto
