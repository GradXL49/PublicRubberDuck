/*
 * Grady Landers
 * Master's Project - Code name Rubber Duck
 * array creator gui for front-end
 */

package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import backend.MessageService;
import backend.MyUtilities;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.WindowEvent;
import java.io.*;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.event.ChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

public class ArrayDesigner extends JDialog {

	/**
	 * default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnSubmit;
	private DefaultTableModel tmData;
	private JSpinner spinnerSize;
	private JComboBox<String> cbDataType;
	private JMenuItem mntmSave;
	private JMenuItem mntmLoad;

	/**
	 * Launch the application.
	 *
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ArrayDesigner frame = new ArrayDesigner();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public ArrayDesigner(boolean[] success, MessageService data) {
		setModal(true);
		initializeComponents();
		initializeActions(success, data);
	}
	
	private void initializeComponents() {
		setTitle("Make Your Array");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		
		mntmLoad = new JMenuItem("Load");
		mnFile.add(mntmLoad);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblDataType = new JLabel("Data Type:");
		lblDataType.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		cbDataType = new JComboBox<String>();
		cbDataType.setModel(new DefaultComboBoxModel<String>(new String[] {"String", "Integer", "Double"}));
		
		JLabel lblSize = new JLabel("Array Size:");
		lblSize.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		spinnerSize = new JSpinner();
		spinnerSize.setModel(new SpinnerNumberModel(0, 0, null, 1));
		
		JScrollPane spData = new JScrollPane();
		
		btnSubmit = new JButton("Submit");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnSubmit, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblDataType)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(cbDataType, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(lblSize)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(spinnerSize, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE))
						.addComponent(spData, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE))
					.addGap(0))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDataType)
						.addComponent(cbDataType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblSize)
						.addComponent(spinnerSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(spData, GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSubmit))
		);
		
		tmData = new DefaultTableModel();
		initializeDataTable();
		JTable tblData = new JTable(tmData);
		tblData.setRowSelectionAllowed(false);
		tblData.setFont(new Font("Tahoma", Font.PLAIN, 13));
		spData.setViewportView(tblData);
		contentPane.setLayout(gl_contentPane);
	}
	
	private void initializeActions(boolean[] success, MessageService data) {
		//FILE MENU
		//save
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = "No valid array has been entered.";
				
				if(!validData()) {
					JOptionPane.showMessageDialog(null, message);
					return;
				}
				
				String path;
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileNameExtensionFilter("Rubber Duck Data (.rdd)", "rdd"));
				fc.setCurrentDirectory(null);
				int val = fc.showSaveDialog(null);
				if(val == JFileChooser.APPROVE_OPTION) {
					path = fc.getSelectedFile().toString();
					if(!path.endsWith(".rdd"))
						path += ".rdd";
					
					if(saveData(path)) message = "Data saved successfully.";
					else message = "There was a problem saving the data.\nCheck the console for more info.";
					
					JOptionPane.showMessageDialog(null, message);
				}
			}
		});
		
		//load
		mntmLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(validData()) {
					if(JOptionPane.showConfirmDialog(null, "There is a valid array present. Are you sure you want to overwrite it?") == JOptionPane.CANCEL_OPTION);
						return;
				}
				
				String path;
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileNameExtensionFilter("Rubber Duck Data (.rdd)", "rdd"));
				fc.setCurrentDirectory(null);
				int val = fc.showOpenDialog(null);
				if(val == JFileChooser.APPROVE_OPTION) {
					path = fc.getSelectedFile().toString();
					
					if(!loadData(path)) JOptionPane.showMessageDialog(null, "There was a problem loading the data.\nCheck the console for more info.");
				}
			}
		});
		
		//handle close button
		SwingUtilities.getWindowAncestor(contentPane).addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent a) {
				//ask the user if they're sure they want to close it
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to close this window?\nYour choices will not be saved.", getTitle(), JOptionPane.YES_NO_OPTION);
				
				if(choice == JOptionPane.YES_OPTION) {
					//graceful close
					success[0] = false;
					dispose();
				}
			}
		});
		
		//handle data type switch
		cbDataType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(cbDataType.getSelectedItem().toString()!="String")
					resetDataTable();
			}
		});
		
		//handle size spinner
		spinnerSize.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateDataTable((Integer)spinnerSize.getValue());
			}
		});
		
		//handle data entry
		tmData.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if(e.getType()==TableModelEvent.UPDATE && e.getColumn()==1) {
					Object value = tmData.getValueAt(e.getLastRow(), 1);
					if(value==null || cbDataType.getSelectedItem().toString()=="String") return;
					else if(cbDataType.getSelectedItem().toString() == "Integer") {
						if(!MyUtilities.isInt(value.toString())) {
							JOptionPane.showMessageDialog(null, "Invalid Input:\nYour input could not be determined as an Integer.");
							tmData.setValueAt(null, e.getLastRow(), 1);
						}
					}
					else if(cbDataType.getSelectedItem().toString() == "Double") {
						if(!MyUtilities.isDouble(value.toString())) {
							JOptionPane.showMessageDialog(null, "Invalid Input:\nYour input could not be determined as a Double.");
							tmData.setValueAt(null, e.getLastRow(), 1);
						}
					}
				}
			}
		});
		
		//handle submit button
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				success[0] = initializeData(data);
				if(success[0]) dispose();
				else JOptionPane.showMessageDialog(null, "Something went wrong.\nMost likely an entry was not properly entered. Make sure to press enter after modifying a cell to ensure the change is recorded.");
			}
		});
	}
	
	//set up table for data entry
	private void initializeDataTable() {
		//set column names
		tmData.addColumn("Array Index");
		tmData.addColumn("Data");
	}
	
	private void updateDataTable(int value) {
		int row = tmData.getRowCount();
		if(value-row > 0) {
			for(int i=row; i<value; i++) {
				tmData.addRow(new Object[] {"array["+(i)+"]", null});
			}
		}
		else {
			tmData.setRowCount(value);
		}
	}
	
	private void updateDataTable(Object[] data) {
		tmData.setRowCount(0);
		for(int i=0; i<data.length; i++) {
			tmData.addRow(new Object[] {"array["+(i)+"]", data[i].toString()});
		}
	}
	
	private void resetDataTable() {
		for(int i=0; i<tmData.getRowCount(); i++)
			tmData.setValueAt(null, i, 1);
	}
	
	private boolean initializeData(MessageService back) {
		try {
			Object[] data = new Object[(Integer)spinnerSize.getValue()];
			if(cbDataType.getSelectedItem().toString() == "String") {
				for(int i=0; i<tmData.getRowCount(); i++) {
					data[i] = tmData.getValueAt(i, 1).toString();
				}
			}
			else if(cbDataType.getSelectedItem().toString() == "Integer") {
				for(int i=0; i<tmData.getRowCount(); i++) {
					data[i] = Integer.parseInt(tmData.getValueAt(i, 1).toString());
				}
			}
			else if(cbDataType.getSelectedItem().toString() == "Double") {
				for(int i=0; i<tmData.getRowCount(); i++) {
					data[i] = Double.parseDouble(tmData.getValueAt(i, 1).toString());
				}
			}
			
			back.setDataArray(data);
			
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean saveData(String path) {
		try {
			//initialize file writer
			FileWriter fw = new FileWriter(new File(path));
			
			//write data structure
			fw.append(cbDataType.getSelectedItem().toString() + "\n");
			for(int i=0; i<tmData.getRowCount(); i++) {
				fw.append(tmData.getValueAt(i, 1).toString());
				if(i < tmData.getRowCount()-1) fw.append(","); 
			}
			
			//return
			fw.close();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean loadData(String path) {
		try {
			//initialize file reader
			BufferedReader fr = new BufferedReader(new FileReader(path));
			
			//read data structure
			cbDataType.setSelectedItem(fr.readLine());
			String[] temp = fr.readLine().split(",");
			fr.close();
			
			updateDataTable(temp);
			spinnerSize.setValue(temp.length);
			
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean validData() {
		if((Integer)spinnerSize.getValue() < 1) return false;
		
		for(int i=0; i<tmData.getRowCount(); i++) {
			if(tmData.getValueAt(i, 1) == null) return false;
		}
		
		return true;
	}
}
