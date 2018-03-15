package application;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;

import javax.swing.JFrame;

import org.openimaj.image.*;
import org.openimaj.image.processing.face.detection.*;
import org.openimaj.math.geometry.shape.Rectangle;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

public class TheFaceDetector extends JFrame implements Runnable, WebcamPanel.Painter {

	Database database = Database.getInstance();
	FaceRecognizer faceRecognizer = FaceRecognizer.getInstance();
	
	private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();
	private static final HaarCascadeDetector detector = new HaarCascadeDetector();
	private static final Stroke STROKE = new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f,
			new float[] { 5.0f }, 0.0f);

	private Webcam webcam = Webcam.getDefault();
	private WebcamPanel.Painter painter = null;
	private List<DetectedFace> faces = null;
	private static final long serialVersionUID = 1L;
	public WebcamPanel panel = new WebcamPanel(webcam);

	private int code;
	private int reg;
	private int age;
	
	ArrayList<String> user;
	int recogniseCode;
	private boolean isRecFace = false;

	private String fname; // first name
	private String Lname; // last name
	private String sec; // section
	private String name;
	private boolean saveFace = false;

	private static TheFaceDetector instance;

	private TheFaceDetector() {	}

	public static TheFaceDetector getInstace() {
		if (instance == null) {
			instance = new TheFaceDetector();
		}
		return instance;
	}

	@Override
	public void run() {

		while (true) {
			if (!webcam.isOpen()) {
				return;
			}
			faces = detector.detectFaces(ImageUtilities.createFImage(webcam.getImage()));
			
		}
	}

	@Override
	public void paintPanel(WebcamPanel panel, Graphics2D g2) {
		if (painter != null) {
			painter.paintPanel(panel, g2);
		}
	}

	@Override
	public void paintImage(WebcamPanel panel, BufferedImage image, Graphics2D g2) {
		if (painter != null) {
			painter.paintImage(panel, image, g2);
		}

		if (faces == null) {
			return;
		}

		Iterator<DetectedFace> detectFace = faces.iterator();
		while (detectFace.hasNext()) {

			DetectedFace face = detectFace.next();
			Rectangle bounds = face.getBounds();
			int dx = (int) (0.1 * bounds.width);
			int dy = (int) (0.2 * bounds.height);
			int x = (int) bounds.x - dx;
			int y = (int) bounds.y - dy;
			int w = (int) bounds.width + dx;
			int h = (int) bounds.height + dy;

			g2.setStroke(STROKE);
			g2.setColor(Color.GREEN);
			g2.drawRect(x, y, w, h);
		
			if (isRecFace()) {
				//this.recogniseCode = faceRecognizer.recognize(temp);

				// getting recognised user from the database
				user = new ArrayList<String>();
				
				try {
					user = database.getUser(this.recogniseCode);
				} catch (SQLException e) {
					e.printStackTrace();
				}

				// printing recognised person name into the
				// frame
				g2.setColor(Color.WHITE);
				g2.setFont(new Font("Arial Black", Font.BOLD, 20));
				String names = user.get(1) + " " + user.get(2);
				g2.drawString(names, x,y);
			}
		}
	}

	public void start() {
		webcam.open();
		panel.setPainter(this);
		panel.setPainter(this);
		panel.start();

		painter = panel.getDefaultPainter();

		EXECUTOR.execute(this);
	}

	public void stop() {
		panel.stop();
		webcam.close();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getReg() {
		return reg;
	}

	public void setReg(int reg) {
		this.reg = reg;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return Lname;
	}

	public void setLname(String lname) {
		Lname = lname;
	}

	public String getSec() {
		return sec;
	}

	public void setSec(String sec) {
		this.sec = sec;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRecFace() {
		return isRecFace;
	}

	public void setRecFace(boolean isRecFace) {
		this.isRecFace = isRecFace;
	}
}
