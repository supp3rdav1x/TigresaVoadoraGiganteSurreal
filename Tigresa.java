package TVGS;

import robocode.*;
import java.awt.*;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;
import static robocode.util.Utils.normalRelativeAngleDegrees;
//import java.awt.Color;

/**
 * Tigresa - a robot by (your name here)
 */

public class Tigresa extends AdvancedRobot {
	int dist = 50; // distância para se mover
	int count = 0; // Mantém o controle de quanto tempo temos
	// vem procurando nosso alvo

	double gunTurnAmt;
	String trackName;
	boolean movingForward;
	int turnDirection = 1; // Se mover no sentido horario ou antihorario

	/**
	 * run: Tigresa's default behavior
	 */

	public void run() {

		setBodyColor(Color.orange);
		setGunColor(Color.orange);
		setRadarColor(Color.black);
		setScanColor(Color.magenta);

		setBulletColor(Color.white);
		// setBulletColor(Color.magenta);
		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar

		// Robot main loop
		while (true) {
			// Diga ao jogo que quando agirmos,
			// nós também vamos querer virar muito á direita.
			setTurnRight(10000);
			// Limite nossa velocidade para 5
			setMaxVelocity(5);
			// Comece a se mover (e girar)
			ahead(10000);
			// Repetir
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	// O que fazer quando você vê outro robô:

	public void onScannedRobot(ScannedRobotEvent e) {

		// Se tivermos um alvo, e não é isso, retorne imediatamente
		// para que possamos obter mais Eventos com energia.
		if (trackName != null && !e.getName().equals(trackName)) {
			return;
		}

		// Se não temos um alvo, bem, agora temos!
		if (trackName == null) {
			trackName = e.getName();
			out.println("Tracking " + trackName);
		}
		// Esse é o nosso alvo. Contagem de reset (veja o método de execução)
		count = 0;
		// Se nosso alvo está muito longe, vire-se e vá em direção a ele.
		if (e.getDistance() > 150) {
			gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));

			turnGunRight(gunTurnAmt); // Tente alterá-los para definirTurnGunRight,
			turnRight(e.getBearing()); // e ver o quanto o Tracker melhora...
			// (terá que fazer do Tracker um AdvancedRobot)
			ahead(e.getDistance() - 140);
			return;
		}

		// Nosso alvo está perto, atire em intencidade 3
		gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
		turnGunRight(gunTurnAmt);
		fire(3);

		// Nosso alvo está muito perto! Recuar.
		if (e.getDistance() < 100) {
			if (e.getBearing() > -90 && e.getBearing() <= 90) {
				back(80);
			} else {
				ahead(40);
			}
		}
		scan();
	}

	/**
	 * O que fazer quando você bate em uma parede:
	 */

	public void onHitByBullet(HitByBulletEvent e) {
		back(10);
	}

	public void onHitRobot(HitRobotEvent e) {
		if (e.getBearing() >= 0) {
			turnDirection = 1;
		} else {
			turnDirection = -1;
		}
		turnRight(e.getBearing());

		// Determine um tiro que não matará o oponente...
		// Queremos fuzilar o oponente, não pegar bonus
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
		ahead(40); // Fuzila ele denovo!
	}

	public void onHitWall(HitWallEvent e) {
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