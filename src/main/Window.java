package main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import validation.AddressVerifier;
import validation.PortVerifier;

public class Window {
	private static final String MAIN_FRAME_TITLE = "Client";
	private static final String PORT_INPUT_LABEL = "Port";
	private static final String ADDRESS_INPUT_LABEL = "Address";
	private static final String SELECT_BUTTON_TITLE = "Select";
	private static JTextArea area;
	private static JButton selectButton;
	private static JButton sendButton;
	private JTextField portInput;
	private JLabel portInputLabel;
	private JTextField addressInput;
	private JLabel addressInputLabel;
	private File file = null;
	private int port = 2154;
	private String address = "127.0.0.1";

	Window() {
		JFrame mainFrame = new JFrame(MAIN_FRAME_TITLE);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(300, 300);
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setResizable(false);
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new GridLayout(2, 2));
		
		area = new JTextArea();
		JScrollPane sp = new JScrollPane(area);
		
		selectButton = new JButton(SELECT_BUTTON_TITLE);
		portInput = new JTextField((new Integer(port)).toString());
		portInput.setInputVerifier(new PortVerifier());
		portInputLabel = new JLabel(PORT_INPUT_LABEL);
		addressInput = new JTextField(address);
		addressInput.setInputVerifier(new AddressVerifier());
		addressInputLabel = new JLabel(ADDRESS_INPUT_LABEL);
		sendButton = new JButton("Send");

		dataPanel.add(portInputLabel);
		dataPanel.add(portInput);
		dataPanel.add(addressInputLabel);
		dataPanel.add(addressInput);

		JPanel actionPanel = new JPanel();
		actionPanel.setLayout(new GridLayout(1, 2));

		actionPanel.add(selectButton);
		actionPanel.add(sendButton);

		mainFrame.add(dataPanel, BorderLayout.NORTH);
		mainFrame.add(sp);
		mainFrame.add(actionPanel, BorderLayout.SOUTH);

		sendButton.setEnabled(false);
		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (Client.getInstance().createSocket(addressInput.getText(),
						portInput.getText())) {
					
					Client.getInstance().start(file);
				}else{
					log("can't create connection, aborted \n");
				}
			}
		});

		mainFrame.setVisible(true);
		
		selectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				area.setText("");
				int returnVal = chooser.showOpenDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					area.append("Выбранный файл для передачи: \n");
					file = chooser.getSelectedFile();
					area.append(file.getPath()+ "\n");
					sendButton.setEnabled(true);
				}
			}
		});
	}

	public static void log(String message) {
		area.append(message);
	}
	public static void setText(String message) {
		area.setText(message);
	}
	public static void blockButton() {
		sendButton.setEnabled(false);
		selectButton.setEnabled(false);
	}
	public static void unblockButton() {
		selectButton.setEnabled(true);
		sendButton.setEnabled(true);
	}

}
