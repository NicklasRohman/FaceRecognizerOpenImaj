package gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.sarxos.webcam.WebcamPanel;

import application.Controller;
import application.Database;
import application.FaceRecognizer;
import application.TheFaceDetector;

public class GuiClass {
	TheFaceDetector theFaceDetector = TheFaceDetector.getInstace();
	Database database = Database.getInstance();
	FaceRecognizer faceRec = FaceRecognizer.getInstance();
	JButton startCam;
	private JButton stopRecogniseBtn;
	private JButton recogniseBtn;
	private JButton saveBtn;
	public JPanel tile;
	WebcamPanel imageFrame;
	private JFrame frame;
	Controller controller;
	public JLabel warning = new JLabel();
	public JLabel savedLabel = new JLabel();
	private TextField code, fname, lname, reg, sec, age;
	TextArea eventLog;

	/**
	 * Create the application.
	 */
	private GuiClass() {
		initialize();
	}

	private static GuiClass instance;

	public static GuiClass getInstance() {
		if (instance == null) {
			instance = new GuiClass();
		}
		return instance;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		int buttonHeight = 55;
		int buttonWidth = 100;
		int textFiledHeight = 25;
		int textFiledWidth = 125;
		int buttonYaxel = 500;

		frame = new JFrame();
		frame.setBounds(100, 100, 1270, 900);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel dataPane = new JPanel();
		dataPane.setBounds(10, 11, 1234, 684);
		frame.getContentPane().add(dataPane);
		dataPane.setLayout(null);
		frame.setVisible(true);

		/**
		 * Panel West
		 */
		JPanel panel_WEST = new JPanel();
		panel_WEST.setBounds(0, 0, 218, 540);
		dataPane.add(panel_WEST);
		panel_WEST.setLayout(null);

		code = new TextField();
		code.setBounds(87, 31, textFiledWidth, textFiledHeight);
		panel_WEST.add(code);

		JLabel lblNewLabel_1 = new JLabel("Person  Data:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(10, 11, 181, 25);
		panel_WEST.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("Code:");
		lblNewLabel_2.setBounds(10, 36, 61, 22);
		panel_WEST.add(lblNewLabel_2);

		JLabel lblFname = new JLabel("FName:");
		lblFname.setBounds(10, 64, 61, 22);
		panel_WEST.add(lblFname);

		fname = new TextField();
		fname.setBounds(87, 61, textFiledWidth, textFiledHeight);
		panel_WEST.add(fname);

		JLabel lblLname = new JLabel("LName:");
		lblLname.setBounds(10, 92, 61, 22);
		panel_WEST.add(lblLname);

		lname = new TextField();
		lname.setBounds(87, 89, textFiledWidth, textFiledHeight);
		panel_WEST.add(lname);

		reg = new TextField();
		reg.setBounds(87, 122, textFiledWidth, textFiledHeight);
		panel_WEST.add(reg);

		JLabel lblReg = new JLabel("Reg:");
		lblReg.setBounds(10, 125, 61, 22);
		panel_WEST.add(lblReg);

		JLabel lblAge = new JLabel("Age:");
		lblAge.setBounds(10, 154, 61, 22);
		panel_WEST.add(lblAge);

		age = new TextField();
		age.setBounds(87, 153, textFiledWidth, textFiledHeight);
		panel_WEST.add(age);

		sec = new TextField();
		sec.setBounds(87, 184, textFiledWidth, textFiledHeight);
		panel_WEST.add(sec);

		JLabel lblSection = new JLabel("Section:");
		lblSection.setBounds(10, 187, 69, 22);
		panel_WEST.add(lblSection);

		/**
		 * Panel Center
		 */
		JPanel panel_CENTER = new JPanel();
		panel_CENTER.setBounds(228, 0, 714, 570);
		dataPane.add(panel_CENTER);
		panel_CENTER.setLayout(null);

		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		horizontalStrut_1.setBounds(25, 492, 679, 0);
		panel_CENTER.add(horizontalStrut_1);

		startCam = new JButton("Start Cam");
		startCam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller = new Controller();
				if (startCam.getText().equalsIgnoreCase("Start Cam")) {
					startCam.setText("Stop Cam");

					if (imageFrame == null) {
						imageFrame = theFaceDetector.panel;
						imageFrame.setBounds(25, 10, 635, 370);
						panel_CENTER.add(imageFrame);
					}

					try {
						controller.startCamera();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				} else {
					startCam.setText("Start Cam");
					getRecogniseBtn().setEnabled(false);
					getStopRecogniseBtn().setEnabled(false);
					getSaveBtn().setEnabled(false);
					imageFrame = null;
					controller.stopCamera();
				}
			}
		});
		startCam.setBounds(24, buttonYaxel, buttonWidth, buttonHeight);
		panel_CENTER.add(startCam);

		setStopRecogniseBtn(new JButton("Stop Recognise"));
		getStopRecogniseBtn().setEnabled(false);
		getStopRecogniseBtn().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				stopRecogniseBtn.setEnabled(false);
				recogniseBtn.setEnabled(true);
				theFaceDetector.setRecFace(false);
				putOnLog("Stop Recognize faces");
			}
		});
		getStopRecogniseBtn().setBounds(597, buttonYaxel, 107, 55);
		panel_CENTER.add(getStopRecogniseBtn());

		setRecogniseBtn(new JButton("Recognise Face"));
		getRecogniseBtn().setEnabled(false);
		getRecogniseBtn().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopRecogniseBtn.setEnabled(true);
				recogniseBtn.setEnabled(false);
				theFaceDetector.setRecFace(true);
				putOnLog("Recognize faces activated");
			}
		});
		getRecogniseBtn().setBounds(462, buttonYaxel, 125, 55);
		panel_CENTER.add(getRecogniseBtn());

		setSaveBtn(new JButton("Save Face"));
		getSaveBtn().setEnabled(false);
		getSaveBtn().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller = new Controller();
				controller.savePicture();

			}
		});
		getSaveBtn().setBounds(126, buttonYaxel, buttonWidth, buttonHeight);
		panel_CENTER.add(getSaveBtn());

		/**
		 * Panel East
		 */
		JPanel panel_EAST = new JPanel();
		panel_EAST.setBounds(1033, 0, 201, 570);
		dataPane.add(panel_EAST);
		panel_EAST.setLayout(null);

		JLabel lblOutput = new JLabel("Output");
		lblOutput.setBounds(10, 11, 181, 28);
		panel_EAST.add(lblOutput);

		eventLog = new TextArea();
		eventLog.setEditable(false);
		eventLog.setBounds(10, 34, 181, 526);
		panel_EAST.add(eventLog);

		/**
		 * panel South
		 */
		JPanel panel_SOUTH = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel_SOUTH.setBounds(0, 581, 1234, 510);
		dataPane.add(panel_SOUTH);
		panel_SOUTH.setLayout(null);

		tile = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tile.setBounds(10, 11, 1214, 500);
		panel_SOUTH.add(tile);

		JLabel lblDatabas = new JLabel("Databas");
		lblDatabas.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDatabas.setBounds(0, 540, 56, 14);
		dataPane.add(lblDatabas);

	}

	public void putOnLog(String data) {
		StringBuilder sb = new StringBuilder();
		sb.append(eventLog.getText() + "\n" + data);

		eventLog.setText(sb.toString());

	}

	public JButton getRecogniseBtn() {
		return recogniseBtn;
	}

	public void setRecogniseBtn(JButton recogniseBtn) {
		this.recogniseBtn = recogniseBtn;
	}

	public JButton getStopRecogniseBtn() {
		return stopRecogniseBtn;
	}

	public void setStopRecogniseBtn(JButton stopRecogniseBtn) {
		this.stopRecogniseBtn = stopRecogniseBtn;
	}

	public JButton getSaveBtn() {
		return saveBtn;
	}

	public void setSaveBtn(JButton saveBtn) {
		this.saveBtn = saveBtn;
	}

	public TextField getCode() {
		return code;
	}

	public void setCode(TextField code) {
		this.code = code;
	}

	public TextField getFname() {
		return fname;
	}

	public void setFname(TextField fname) {
		this.fname = fname;
	}

	public TextField getLname() {
		return lname;
	}

	public void setLname(TextField lname) {
		this.lname = lname;
	}

	public TextField getReg() {
		return reg;
	}

	public void setReg(TextField reg) {
		this.reg = reg;
	}

	public TextField getSec() {
		return sec;
	}

	public void setSec(TextField sec) {
		this.sec = sec;
	}

	public TextField getAge() {
		return age;
	}

	public void setAge(TextField age) {
		this.age = age;
	}

	// public JScrollPane getScrollPane() {
	// return scrollPane;
	// }
	//
	// public void setScrollPane(JScrollPane scrollPane) {
	// this.scrollPane = scrollPane;
	// }
}
