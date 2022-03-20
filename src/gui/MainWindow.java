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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.ArrayList;

import backend.MessageService;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;

public class MainWindow {

	public JFrame frmRubberDuck;
	private MessageService messages;
	private DefaultListModel<String> lmMessages;
	private JButton btnSubmit;
	private JTextArea taInput;
	private JMenuItem mntmExit;
	private JScrollPane spConvo;
	private JMenuItem mntmRestart;
	private JMenuItem mntmSave;
	private JMenuItem mntmLoad;
	private ArrayList<String> inputs;

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
		this.inputs = new ArrayList<String>();
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
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(spConvo, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(spInput, GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSubmit, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(spConvo, GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(btnSubmit, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(spInput, GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE))
					.addContainerGap())
		);
		
		lmMessages = new DefaultListModel<String>();
		JList<String> lsMessages = new JList<String>(lmMessages);
		lsMessages.setFont(new Font("Tahoma", Font.PLAIN, 15));
		spConvo.setViewportView(lsMessages);
		
		taInput = new JTextArea();
		taInput.setFont(new Font("Courier New", Font.PLAIN, 15));
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
		
		mntmRestart = new JMenuItem("Restart");
		mnFile.add(mntmRestart);
		
		mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		
		mntmLoad = new JMenuItem("Load");
		mnFile.add(mntmLoad);
	}
	
	private void initializeActions() {
		//FILE MENU
		//exit
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		//restart
		mntmRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lmMessages.clear();
				inputs.clear();
				sendMessage("restart");
				lmMessages.removeElement("You: restart");
				taInput.setText("");
			}
		});
		
		//save
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path;
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileNameExtensionFilter("Rubber Duck Conversation (.rdc)", "rdc"));
				fc.setCurrentDirectory(null);
				int val = fc.showSaveDialog(null);
				if(val == JFileChooser.APPROVE_OPTION) {
					path = fc.getSelectedFile().toString();
					if(!path.endsWith(".rdc"))
						path += ".rdc";
					messages.saveConvo(path, inputs);
				}
				displayNewMessages();
			}
		});
		
		//load
		mntmLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path;
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileNameExtensionFilter("Rubber Duck Conversation (.rdc)", "rdc"));
				fc.setCurrentDirectory(null);
				int val = fc.showOpenDialog(null);
				if(val == JFileChooser.APPROVE_OPTION) {
					path = fc.getSelectedFile().toString();
					mntmRestart.doClick();
					for(String m : messages.loadConvo(path)) {
						sendMessage(m);
					}
				}
			}
		});
		
		//submit button clicked or enter pressed
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = taInput.getText();
				if(message.toLowerCase().contains("restart")) {
					mntmRestart.doClick();
					return;
				}
				else if(message.toLowerCase().contains("exit")) {
					mntmExit.doClick();
				}
				
				sendMessage(message);
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
		inputs.add(message);
		lmMessages.addElement("You: "+message);
		messages.receiveMessage(message);
		displayNewMessages();
	}
}
