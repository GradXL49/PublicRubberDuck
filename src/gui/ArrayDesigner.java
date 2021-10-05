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

import javax.swing.JComboBox;
import javax.swing.JDialog;
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
import javax.swing.event.ChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

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
}
