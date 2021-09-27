/*
 * Grady Landers
 * Master's Project - Code name Rubber Duck
 * main interaction window for front-end
 * 09-21-2021
 */

package gui;

//imports
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.ArrayList;

import backend.MessageService;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainWindow {

	public JFrame frmRubberDuck;
	private MessageService messages;
	private DefaultListModel<String> lmMessages;
	private JButton btnSubmit;
	private JTextArea taInput;
	private JMenuItem mntmExit;
	private JScrollPane spConvo;

	/**
	 * Launch the application.
	 *
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the application.
	 */
	public MainWindow(MessageService m) {
		this.messages = m;
		this.initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.initializeComponents();
		this.initializeActions();
		this.displayNewMessages();
	}
	
	private void initializeComponents() {
		frmRubberDuck = new JFrame();
		frmRubberDuck.setTitle("Rubber Duck");
		frmRubberDuck.setBounds(100, 100, 750, 600);
		frmRubberDuck.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		spConvo = new JScrollPane();
		
		btnSubmit = new JButton("Submit");
		
		JScrollPane spInput = new JScrollPane();
		GroupLayout groupLayout = new GroupLayout(frmRubberDuck.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(spInput, GroupLayout.PREFERRED_SIZE, 597, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSubmit, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))
						.addComponent(spConvo, GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(spConvo, GroupLayout.PREFERRED_SIZE, 407, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(spInput, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
						.addComponent(btnSubmit, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
					.addContainerGap())
		);
		
		lmMessages = new DefaultListModel<String>();
		JList<String> lsMessages = new JList<String>(lmMessages);
		spConvo.setViewportView(lsMessages);
		
		taInput = new JTextArea();
		taInput.setTabSize(4);
		taInput.setWrapStyleWord(true);
		taInput.setLineWrap(true);
		spInput.setViewportView(taInput);
		frmRubberDuck.getContentPane().setLayout(groupLayout);
		
		JMenuBar menuBar = new JMenuBar();
		frmRubberDuck.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmExit = new JMenuItem("Exit");
		
		mnFile.add(mntmExit);
	}
	
	private void initializeActions() {
		//FILE MENU
		//exit
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		//submit button clicked or enter pressed
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lmMessages.addElement("You: "+taInput.getText());
				sendMessage(taInput.getText());
				taInput.setText("");
				
				JScrollBar sb = spConvo.getVerticalScrollBar();
				sb.setValue(sb.getMaximum());
			}
		});
		
		taInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					e.consume();
					btnSubmit.doClick();
				}
			}
		});
	}
	
	private void displayNewMessages() {
		ArrayList<String> newMessages = this.messages.getNewMessages();
		
		for(int i=0; i<newMessages.size(); i++) {
			this.lmMessages.addElement(newMessages.get(i));
		}
	}
	
	private void sendMessage(String message) {
		this.messages.receiveMessage(message);
		this.displayNewMessages();
	}
}
