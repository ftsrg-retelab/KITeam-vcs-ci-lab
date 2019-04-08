package hu.bme.mit.train.controller;

import hu.bme.mit.train.interfaces.TrainController;

public class TrainControllerImpl implements TrainController {
	private int step = 2;
	private int referenceSpeed = 0;
	private int speedLimit = 0;
	private int refreshRate = 300;
	private Thread referenceSpeedThread;

	public TrainControllerImpl() {
			referenceSpeedThread = new Thread() {
				public void run() {
					try {
						followSpeed();
						referenceSpeedThread.sleep(refreshRate);
					} catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			};
			//referenceSpeedThread.start();
		}

	@Override
	public void followSpeed() {
		if (referenceSpeed < 0) {
			referenceSpeed = 0;
		} else {
		    if(referenceSpeed+step > 0) {
                referenceSpeed += step;
            } else {
		        referenceSpeed = 0;
            }
		}

		enforceSpeedLimit();
		TachoGraph.insertRecord(System.currentTimeMillis(),step,referenceSpeed);
	}

	@Override 
	public void emergencyBrake(){
	    referenceSpeed = 0;
	}

	@Override
	public int getReferenceSpeed() {
		return referenceSpeed;
	}

	@Override
	public void setSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
		enforceSpeedLimit();
		
	}

	private void enforceSpeedLimit() {
		if (referenceSpeed > speedLimit) {
			referenceSpeed = speedLimit;
		}
	}

	@Override
	public void setJoystickPosition(int joystickPosition) {
		this.step = joystickPosition;		
	}

}
