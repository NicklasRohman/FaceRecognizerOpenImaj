package application;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.github.sarxos.webcam.Webcam;

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
	int codeText, regText, secText, ageText;
	String fnameText, lnameText;
	FilenameFilter IMAGE_FILTER;
	int count = 15;

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
					ImageIcon imageIcon = new ImageIcon(img);
					Image image = imageIcon.getImage();
					Image newimg = image.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH);
					imageIcon = new ImageIcon(newimg);
					JLabel label = new JLabel(imageIcon);
					gui.tile.add(label);
				} catch (Exception e) {
				}

			}
		}

		gui.putOnLog("Real Time WebCam Stream Started !");
		theFaceDetector.start();
		// **********************************************************************************************
	}

	private void fillListOfFaceImages() {

		final String[] EXTENSION = new String[] { "png", "jpg" };
		IMAGE_FILTER = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				for (String s : EXTENSION) {
					if (name.endsWith("." + s)) {
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

		// Input Validation
		if (gui.getFname().getText().trim().isEmpty() || gui.getReg().getText().trim().isEmpty()
				|| gui.getCode().getText().trim().isEmpty()) {

			new Thread(() -> {

				try {
					gui.warning.setVisible(true);

					Thread.sleep(2000);

					gui.warning.setVisible(false);

				} catch (InterruptedException ex) {
				}

			}).start();

		} else {

			gui.savedLabel.setVisible(true);

			new Thread(() -> {

				try {

					setTextValues();

					database.insert();

					gui.savedLabel.setVisible(true);
					Thread.sleep(2000);

					gui.savedLabel.setVisible(false);
					String fileName = "faces/" + database.code + "-" + database.fname + "_" + database.lname + "_"
							+ count + ".jpg";
					count++;
					Webcam webcam = Webcam.getDefault();
					webcam.open();
					BufferedImage image = webcam.getImage();
					try {
						ImageIO.write(image, "jpg", new File(fileName));
					} catch (IOException e) {
						e.printStackTrace();
					}

				} catch (InterruptedException ex) {
				}

			}).start();
		}
	}

	private void setTextValues() {
		database.fname = gui.getFname().getText();
		database.lname = gui.getLname().getText();
		database.sec = gui.getSec().getText();
		try {
			database.code = Integer.parseInt(gui.getCode().getText());
		} catch (Exception e) {
			database.code = 0;
			JOptionPane.showMessageDialog(null, "Not correct value on Code.\n" + "Code value set 0");
		}
		try {
			database.reg = Integer.parseInt(gui.getReg().getText());
		} catch (Exception e) {
			database.code = 0;
			JOptionPane.showMessageDialog(null, "Not correct value on Reg.\n" + "Reg value set 0");
			database.reg = 0;
		}
		try {
			database.age = Integer.parseInt(gui.getAge().getText());
		} catch (Exception e) {
			database.code = 0;
			JOptionPane.showMessageDialog(null, "Not correct value on age.\n" + "Age value set 0");
			database.age = 0;
		}
	}
}
