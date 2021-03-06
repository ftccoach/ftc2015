package com.qualcomm.ftcrobotcontroller.opmodes;

//------------------------------------------------------------------------------
//
// MyTeleOp8800
//
//------------------------------------------------------------------------------

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * This is the main file for the telop program that allows us to drive the
 * robot via the gamepad
 *
 * @author Robotinatorz
 * @version 2015-10-01
 */
public class MyTeleOp extends OpMode {

	/*
	 * Note: the configuration of the servos is such that
	 * as the arm servo approaches 0, the arm position moves up (away from the floor).
	 * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
	 */
	// TETRIX VALUES.
	final static double ARM_MIN_RANGE  = 0.20;
	final static double ARM_MAX_RANGE  = 0.90;
	final static double CLAW_MIN_RANGE  = 0.20;
	final static double CLAW_MAX_RANGE  = 0.7;

	private DcMotor.Direction driveDirection = DcMotor.Direction.REVERSE;

	// position of the arm servo.
	double arm1Position;
	double arm2Position;

	// amount to change the arm servo position.
	double armDelta = 0.1;

    int turboDriver = 2;

	DcMotor motorRight;
	DcMotor motorLeft;
    DcMotor motorFrontArmExtend;
    DcMotor motorFrontArmFlip;

	Servo arm1;
	Servo arm2;

	/**
	 * Constructor
	 */
	public MyTeleOp() {

	}

	/*
	 * Code to run when the op mode is first enabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */
	@Override
	public void init() {


		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

        //setup the drive motors
        motorRight = hardwareMap.dcMotor.get("motor_2");
		motorLeft = hardwareMap.dcMotor.get("motor_1");
		motorRight.setDirection(driveDirection);
		motorLeft.setDirection(driveDirection);

        //setup the main arm motors
        motorFrontArmExtend = hardwareMap.dcMotor.get("motor_3");
		motorFrontArmFlip = hardwareMap.dcMotor.get("motor_4");

        //setup the side servos
        arm1 = hardwareMap.servo.get("servo_1");//right arm
		arm2 = hardwareMap.servo.get("servo_2");//left arm

		// assign the starting position of the wrist and claw
        arm1Position = 0.85;//start pos
        arm2Position = 0.2;//start pos

		arm1.setPosition(arm1Position);
		arm2.setPosition(arm2Position);
	}

	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {

        //initaliaze arm positions to 'up'
        arm1.setPosition(arm1Position);
		arm2.setPosition(arm2Position);

		/*
		 * Controller 1
		 * 
		 * Gamepad 1 controls the motors via the left stick and right sticks
		 */

		// tank drive
		// note that if y equal -1 then joystick is pushed all of the way forward.
		float left = -gamepad1.left_stick_y;
		float right = -gamepad1.right_stick_y;

		// clip the right/left values so that the values never exceed +/- 1
		right = Range.clip(right, -1, 1);
		left = Range.clip(left, -1, 1);

        //we provided a turbo button that allows the robot to run full speed
        //should we need the power on the ramp
        if (gamepad1.right_trigger > 0.5){
            turboDriver = 1;
        } else {
            turboDriver = 2;
        }

		// scale the joystick value to make it easier tocontrol
		// the robot more precisely at slower speeds.
		right = (float)scaleInput(right/turboDriver);
		left =  (float)scaleInput(left/turboDriver);

        motorRight.setDirection(driveDirection);
        motorLeft.setDirection(driveDirection);

		// write the values to the motors
		motorRight.setPower(right);
		motorLeft.setPower(left);

        /*
		 * Controller 2
		 *
		 * Gamepad 2 controls the motors via the left stick and right sticks
		 */
        float ctrl_two_arm_extend = gamepad2.left_stick_y;

        // clip the right/left values so that the values never exceed +/- 1
		ctrl_two_arm_extend = Range.clip(ctrl_two_arm_extend, -1, 1);

        // write the values to the motors
		motorFrontArmExtend.setPower(ctrl_two_arm_extend);

        float ctrl_two_front_arm_flip = gamepad2.right_stick_y / 4;

        // clip the right/left values so that the values never exceed +/- 1
		ctrl_two_front_arm_flip = Range.clip(ctrl_two_front_arm_flip, -1, 1);

        /* scale the joystick value to make it easier to control
        the robot more precisely at slower speeds.*/
        ctrl_two_front_arm_flip =  (float)scaleInput(ctrl_two_front_arm_flip);

        motorFrontArmFlip.setPower(ctrl_two_front_arm_flip);

        //provide a button to change which direction of the robot is forwards
        if (gamepad1.left_bumper){
            driveDirection = DcMotor.Direction.FORWARD;
        }

        if (gamepad1.right_bumper){
            driveDirection = DcMotor.Direction.REVERSE;
        }

        //the following 2 blocks move the side arms that we use to hit the climbers levers
        if (gamepad2.a){
            arm1Position = 0.2;
            arm2Position = 0.79;
            arm1.setPosition(arm1Position);
            arm2.setPosition(arm2Position);
        }

        if (gamepad2.b) {
            arm1Position = 0.8;
            arm2Position = 0.2;
            arm1.setPosition(arm1Position);
            arm2.setPosition(arm2Position);
        }

		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        telemetry.addData("Text", "*** Robot Data***");
		telemetry.addData("arm1", "arm1:  " + String.format("%.2f", arm1Position));
		telemetry.addData("arm2", "arm2:  " + String.format("%.2f", arm2Position));
		telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));

	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {

	}

    	
	/*
	 * This method scales the joystick input so for low joystick values, the 
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */
	double scaleInput(double dVal)  {
		double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
				0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };
		
		// get the corresponding index for the scaleInput array.
		int index = (int) (dVal * 16.0);
		
		// index should be positive.
		if (index < 0) {
			index = -index;
		}

		// index cannot exceed size of array minus 1.
		if (index > 16) {
			index = 16;
		}

		// get value from the array.
		double dScale = 0.0;
		if (dVal < 0) {
			dScale = -scaleArray[index];
		} else {
			dScale = scaleArray[index];
		}

		// return scaled value.
		return dScale;


    }

}
