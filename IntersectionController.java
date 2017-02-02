package com.signal.intersection.controller;

import com.signal.intersection.model.Intersection;
import com.signal.intersection.model.Signal;

public class IntersectionController {
	
	private Intersection northBound = null;
	private Intersection southBound = null;
	private Intersection eastBound = null;
	private Intersection westBound = null;
	private Signal signalOnSnell = null;
	private Signal signalOnWeiver = null;

	public static void main(String[] args) {
		int carCountAtSnell = 0;
		int carCountAtWeiver = 0;
		int greenSignalTime = 3;
		int redSignalTimer = 1;
		boolean turnOnRedSignal = false;
		IntersectionController controller = new IntersectionController();
		controller.init();
		controller.printIntersectionStatus(0);
		controller.updateIntersectionStatus("Weiver", ++carCountAtWeiver);
		
		try {
			for (int seconds = 1; seconds <= 20; seconds++) {
				Thread.sleep(1000);
				controller.printIntersectionStatus(seconds);
				carCountAtSnell++;
				carCountAtWeiver++;
				controller.updateIntersectionStatus("Snell", carCountAtSnell);
				controller.updateIntersectionStatus("Weiver", carCountAtWeiver);
				if (seconds % greenSignalTime == 0) {
					controller.signalOnSnell.setSignalLight(controller.signalOnSnell.getSignalLight().equals("Green")?"Red":"Green");
					controller.signalOnWeiver.setSignalLight(controller.signalOnWeiver.getSignalLight().equals("Green")?"Red":"Green");
					turnOnRedSignal = true;
				} else {
					if (controller.signalOnSnell.getSignalLight().equals("Green")) {
						carCountAtSnell--;
						controller.updateIntersectionStatus("Snell", carCountAtSnell);
					}
					
					if (controller.signalOnWeiver.getSignalLight().equals("Green")) {
						carCountAtWeiver--;
						controller.updateIntersectionStatus("Weiver", carCountAtWeiver);
					}
				}
				
				if(turnOnRedSignal) {
					controller.signalOnWeiver.setSignalLight(controller.signalOnWeiver.getSignalLight().equals("Red")?"Green":"Green");
					controller.signalOnSnell.setSignalLight(controller.signalOnSnell.getSignalLight().equals("Green")?"Red":"Green");
				}

			}

		} catch (InterruptedException ex) {

		}

	}

	private void updateIntersectionStatus(String roadName, int carCount) {
		if(roadName.equals("Snell")) {
			northBound.setCarCount(carCount);
			southBound.setCarCount(carCount);
		} else if (roadName.equals("Weiver")) {
			eastBound.setCarCount(carCount);
			westBound.setCarCount(carCount);
		}
	}

	private void init() {
		northBound = new Intersection("N", 0);
		southBound = new Intersection("N", 0);
		eastBound = new Intersection("N", 0);
		westBound = new Intersection("N", 0);
		signalOnSnell = new Signal("Snell", "Green");
		signalOnWeiver = new Signal("Weiver", "Red");
	}
	
	private void printIntersectionStatus(int rec) {
		System.out.println(rec + ": " +
		northBound.getSignalDirection() + " = " + northBound.getCarCount() + "; " +
		southBound.getSignalDirection() + " = " + southBound.getCarCount() + "; " +
		eastBound.getSignalDirection() + " = " + eastBound.getCarCount() + "; " +
		westBound.getSignalDirection() + " = " + westBound.getCarCount() + "; " );
	}

}
