package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import gui.GuiClass;


public class Controller {

	Database database = Database.getInstance(); // Creating Database object
	TheFaceDetector theFaceDetector = TheFaceDetector.getInstace();
	ArrayList<String> user = new ArrayList<String>();
	GuiClass gui = GuiClass.getInstance();
	// Mention The file location path where the face will be saved & retrieved
	final File FOLDER = new File("faces");
	public boolean enabled = false;
	public boolean isDBready = false;
	int codeText,regText,secText,ageText;
	String fnameText,lnameText;
	FilenameFilter IMAGE_FILTER;

	
	public void startCamera() throws SQLException {
		// *******************************************************************************************
		// initializing objects from start camera button event
		
		if (!database.init()) {

			gui.putOnLog("Error: Database Connection Failed ! ");
		} else {
			isDBready = true;
			gui.putOnLog("Success: Database Connection Succesful ! ");
		}

		// *******************************************************************************************

		if (isDBready) {
			gui.getRecogniseBtn().setEnabled(true);
			gui.getSaveBtn().setEnabled(true);
		}

		
		if (!gui.getStopRecogniseBtn().isEnabled()) {
			gui.getStopRecogniseBtn().setEnabled(true);
			;
		}
		
		fillListOfFaceImages();
		
		if (FOLDER.isDirectory()) {
			for (final File f : FOLDER.listFiles()) {
				BufferedImage img = null;
				
				try {
					img = ImageIO.read(f);
					
					gui.tile.add(new JLabel(new ImageIcon(img)));
					
				} catch (Exception e) {
				}
				
				
			}
		}
		
		
		gui.putOnLog("Real Time WebCam Stream Started !");
		theFaceDetector.start();
		// **********************************************************************************************
	}

	private void fillListOfFaceImages() {
		
		
		final String[] EXTENSION = new String[]{
				"png","jpg"
		};
		IMAGE_FILTER = new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				for (String s : EXTENSION) {
					if (name.endsWith("."+s)) {
							return (true);
					}
				}
				return false;
			}
		};
		
	}

	public void stopCamera() {
		theFaceDetector.stop();
		gui.putOnLog("WebCam paused");
	}

	public void savePicture() {
		setTextValues();
		//Input Validation
		database.insert();

	}
	
	public void setTextValues() {
		database.fname = gui.getFname().getText();
		database.lname = gui.getLname().getText();
		database.sec = gui.getSec().getText();
		try {
		database.code = Integer.parseInt(gui.getCode().getText());
		} catch (Exception e) {
		database.code = 0;
		JOptionPane.showMessageDialog(null, "Not correct value on Code.\n"
				+ "Code value set 0");
		}
		try {
			database.reg = Integer.parseInt(gui.getReg().getText());
			} catch (Exception e) {
			database.code = 0;
			JOptionPane.showMessageDialog(null, "Not correct value on Reg.\n"
					+ "Reg value set 0");
			database.reg = 0;
			}
		try {
			database.age = Integer.parseInt(gui.getAge().getText());
			} catch (Exception e) {
			database.code = 0;
			JOptionPane.showMessageDialog(null, "Not correct value on age.\n"
					+ "Age value set 0");
			database.age = 0;
			}
	}
}
