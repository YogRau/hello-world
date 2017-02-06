package com.transit.traffic.signal.intersection.controller;

import org.apache.log4j.Logger;
import com.transit.traffic.signal.intersection.model.Intersection;
import com.transit.traffic.signal.intersection.model.Signal;

public class IntersectionController {
	
	final static Logger logger = Logger.getLogger(IntersectionController.class);
	
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
		boolean turnOnRedSignal = false;
		boolean isFirstSecOfGreenOnWeiver = false;
		boolean isFirstSecOfGreenOnSnell = false;
		String switchedSignalOn="";
		IntersectionController controller = new IntersectionController();
		controller.init();
		controller.printIntersectionStatus(0);
		
		try {
			for (int seconds = 1; seconds <= 20; seconds++) {
				Thread.sleep(1000);
				
				if (controller.signalOnSnell.getSignalLight().equals("Red") || isFirstSecOfGreenOnSnell) {
					carCountAtSnell++;
					controller.updateIntersectionStatus("Snell", carCountAtSnell);
					isFirstSecOfGreenOnSnell = false;
				}
				
				if (controller.signalOnWeiver.getSignalLight().equals("Red") || isFirstSecOfGreenOnWeiver) {
					carCountAtWeiver++;
					controller.updateIntersectionStatus("Weiver", carCountAtWeiver);
					isFirstSecOfGreenOnWeiver=false;
				}
				
				controller.printIntersectionStatus(seconds);
				
				greenSignalTime--;
				if (greenSignalTime == 0) {
					if(controller.signalOnSnell.getSignalLight().equals("Green")) {
						controller.signalOnSnell.setSignalLight("Red");
						switchedSignalOn = "Snell";
					}
					if(controller.signalOnWeiver.getSignalLight().equals("Green")) {
						controller.signalOnWeiver.setSignalLight("Red");
						switchedSignalOn = "Weiver";
					}
					turnOnRedSignal = true;
					continue;
				} 
				
				if(turnOnRedSignal) {
					if(controller.signalOnSnell.getSignalLight().equals("Red") && switchedSignalOn.equals("Snell")) {
						controller.signalOnWeiver.setSignalLight("Green");
						switchedSignalOn="";
						isFirstSecOfGreenOnWeiver = true;
						
					}
					if(controller.signalOnWeiver.getSignalLight().equals("Red") && switchedSignalOn.equals("Weiver")) {
						controller.signalOnSnell.setSignalLight("Green");
						switchedSignalOn="";
						isFirstSecOfGreenOnSnell = true;
					}
					turnOnRedSignal = false;
					greenSignalTime=3;
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
		southBound = new Intersection("S", 0);
		eastBound = new Intersection("E", 0);
		westBound = new Intersection("W", 0);
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
