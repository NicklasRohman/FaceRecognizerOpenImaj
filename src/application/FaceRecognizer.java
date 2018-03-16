package application;

import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.feature.DoubleFV;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;

public class FaceRecognizer {

	private FaceRecognizer(){}
	
	public static FaceRecognizer instance;
	VFSGroupDataset<FImage>dataset;
	TheFaceDetector facedetect = TheFaceDetector.getInstace();
	
	public static FaceRecognizer getInstance(){
		if (instance == null) {
			instance = new FaceRecognizer();
		}
		return instance;
	}

	public void startRecognise() {
		
		double correct = 0, incorrect = 0;
		for (String truePerson : testing.getGroups()) {
		    for (FImage face : testing.get(truePerson)) {
		        DoubleFV testFeature = eigen.extractFeature(face);

		        String bestPerson = null;
		        double minDistance = Double.MAX_VALUE;
		        for (final String person : features.keySet()) {
		            for (final DoubleFV fv : features.get(person)) {
		                double distance = fv.compare(testFeature, DoubleFVComparison.EUCLIDEAN);

		                if (distance < minDistance) {
		                    minDistance = distance;
		                    bestPerson = person;
		                }
		            }
		        }

		        System.out.println("Actual: " + truePerson + "\tguess: " + bestPerson);

		        if (truePerson.equals(bestPerson))
		            correct++;
		        else
		            incorrect++;
		    }
		}

		System.out.println("Accuracy: " + (correct / (correct + incorrect)));
		
	}
	
}
