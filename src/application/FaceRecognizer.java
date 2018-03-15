package application;


public class FaceRecognizer {

	private FaceRecognizer(){}
	
	public static FaceRecognizer instance;
	
	public static FaceRecognizer getInstance(){
		if (instance == null) {
			instance = new FaceRecognizer();
		}
		return instance;
	}

	public void stopRecognis() {
		
	}
	
}
