package TVGS;
import robocode.*;
import java.awt.*;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;
//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Tigresa - a robot by (your name here)
 */
public class Tigresa extends AdvancedRobot 
{
int dist =50;
	boolean movingForward;
		int turnDirection = 1; // Clockwise or counterclockwise

	/**
	 * run: Tigresa's default behavior
	 */
	public void run() {
		// Initialization of the robot should be put here
		setBodyColor(Color.orange);
    	setGunColor(Color.orange);
		setRadarColor(Color.black);
		setScanColor(Color.magenta);
		
		setBulletColor(Color.white);
		//setBulletColor(Color.magenta);

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar

		// Robot main loop
			while (true) {
			// Tell the game we will want to move ahead 40000 -- some large number
			setAhead(25000);
			movingForward = true;
			// Tell the game we will want to turn right 90
			setTurnRight(90);
			// At this point, we have indicated to the game that *when we do something*,
			// we will want to move ahead and turn right.  That's what "set" means.
			// It is important to realize we have not done anything yet!
			// In order to actually move, we'll want to call a method that
			// takes real time, such as waitFor.
			// waitFor actually starts the action -- we start moving and turning.
			// It will not return until we have finished turning.
			waitFor(new TurnCompleteCondition(this));
			// Note:  We are still moving ahead now, but the turn is complete.
			// Now we'll turn the other way...
			setTurnLeft(180);
			// ... and wait for the turn to finish ...
			waitFor(new TurnCompleteCondition(this));
			// ... then the other way ...
			setTurnRight(180);
			// .. and wait for that turn to finish.
			waitFor(new TurnCompleteCondition(this));
			// then back to the top to do it all again
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		if (e.getDistance() < 50 && getEnergy() > 50) {
			fire(3);
		} // caso contrário, atira com intensidade 1.
		else {
			fire(1);
		}
		// Depois de atirar chama o radar novamente,
		// antes de girar o canhão
		scan();
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(10);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	
		public void onHitRobot(HitRobotEvent e) {
		if (e.getBearing() >= 0) {
			turnDirection = 1;
		} else {
			turnDirection = -1;
		}
		turnRight(e.getBearing());

		// Determine a shot that won't kill the robot...
		// We want to ram him instead for bonus points
		if (e.getEnergy() > 16) {
			fire(3);
		} else if (e.getEnergy() > 10) {
			fire(2);
		} else if (e.getEnergy() > 4) {
			fire(1);
		} else if (e.getEnergy() > 2) {
			fire(.5);
		} else if (e.getEnergy() > .4) {
			fire(.1);
		}
		ahead(40); // Ram him again!
	}

	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		reverseDirection();
	}	
	
	public void reverseDirection() {
		if (movingForward) {
			setBack(25000);
			movingForward = false;
		} else {
			setAhead(25000);
			movingForward = true;
		}
	}
}