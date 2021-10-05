/*
 * Grady Landers
 * Master's Project - Code name Rubber Duck
 * graph creator gui for front-end
 */

package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import backend.MessageService;
import backend.MyUtilities;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;

public class GraphDesigner extends JDialog {
	/**
	 * default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<String> cbDataType;
	private JSpinner spinnerSize;
	private JLabel lblNodeContent;
	private JScrollPane spContent;
	private DefaultTableModel tmContent;
	private DefaultTableModel tmConnect;
	private JButton btnSubmit;

	/**
	 * Launch the application.
	 *
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GraphDesigner dialog = new GraphDesigner();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the dialog.
	 */
	public GraphDesigner(boolean[] success, MessageService data) {
		setModal(true);
		initializeComponents();
		initializeActions(success, data);
	}
	
	private void initializeComponents() {
		setTitle("Graph Designer");
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 750, 700);
		
		JLabel lblDataType = new JLabel("Data Type:");
		lblDataType.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		cbDataType = new JComboBox<String>();
		cbDataType.setModel(new DefaultComboBoxModel<String>(new String[] {"None", "String", "Integer", "Double"}));
		
		JLabel lblSize = new JLabel("Number of Nodes:");
		lblSize.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		spinnerSize = new JSpinner();
		spinnerSize.setModel(new SpinnerNumberModel(0, 0, null, 1));
		
		lblNodeContent = new JLabel("Node Content:");
		lblNodeContent.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNodeContent.setVisible(false);
		
		spContent = new JScrollPane();
		spContent.setVisible(false);
		
		JLabel lblNodeConnections = new JLabel("Node Connections");
		lblNodeConnections.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JScrollPane spConnections = new JScrollPane();
		
		btnSubmit = new JButton("Submit");
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(spConnections, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)
						.addComponent(spContent, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addComponent(lblDataType, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(cbDataType, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(lblSize)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(spinnerSize, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 231, Short.MAX_VALUE)
							.addComponent(btnSubmit))
						.addComponent(lblNodeContent, Alignment.LEADING)
						.addComponent(lblNodeConnections, Alignment.LEADING))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblDataType, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(1)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(cbDataType, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblSize, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
								.addComponent(spinnerSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSubmit))))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNodeContent, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(spContent, GroupLayout.PREFERRED_SIZE, 255, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNodeConnections)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(spConnections, GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		tmConnect = new DefaultTableModel();
		initializeConnectTable();
		JTable tblConnections = new JTable(tmConnect);
		tblConnections.setRowSelectionAllowed(false);
		spConnections.setViewportView(tblConnections);
		
		tmContent = new DefaultTableModel();
		initializeContentTable();
		JTable tblContent = new JTable(tmContent);
		spContent.setViewportView(tblContent);
		getContentPane().setLayout(groupLayout);
	}
	
	private void initializeActions(boolean[] success, MessageService data) {
		//handle close button
		SwingUtilities.getWindowAncestor(cbDataType).addWindowListener(new java.awt.event.WindowAdapter() {
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
				if(cbDataType.getSelectedItem().toString()=="None") {
					lblNodeContent.setVisible(false);
					spContent.setVisible(false);
					tmContent.setRowCount(0);
				}
				else {
					lblNodeContent.setVisible(true);
					spContent.setVisible(true);
					if(cbDataType.getSelectedItem().toString()!="String")
						resetContentTable();
				}
			}
		});
		
		//handle size spinner
		spinnerSize.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				updateContentTable((Integer)spinnerSize.getValue());
				updateConnectTable((Integer)spinnerSize.getValue());
			}
		});
		
		//handle content table entry
		tmContent.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if(e.getType()==TableModelEvent.UPDATE && e.getColumn()==1) {
					Object value = tmContent.getValueAt(e.getLastRow(), 1);
					if(value==null || cbDataType.getSelectedItem().toString()=="String") return;
					else if(cbDataType.getSelectedItem().toString() == "Integer") {
						if(!MyUtilities.isInt(value.toString())) {
							JOptionPane.showMessageDialog(null, "Invalid Input:\nYour input could not be determined as an Integer.");
							tmContent.setValueAt(null, e.getLastRow(), 1);
						}
					}
					else if(cbDataType.getSelectedItem().toString() == "Double") {
						if(!MyUtilities.isDouble(value.toString())) {
							JOptionPane.showMessageDialog(null, "Invalid Input:\nYour input could not be determined as a Double.");
							tmContent.setValueAt(null, e.getLastRow(), 1);
						}
					}
				}
			}
		});
		
		//handle connection table entry
		tmConnect.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if(e.getType()==TableModelEvent.UPDATE && e.getColumn()>0) {
					Object value = tmConnect.getValueAt(e.getLastRow(), e.getColumn());
					if(value != null && !MyUtilities.isDouble(value.toString())) {
						JOptionPane.showMessageDialog(null, "Invalid Input:\nYour input could not be determined as a number.");
						tmConnect.setValueAt(null, e.getLastRow(), e.getColumn());
					}
				}
			}
		});
		
		//handle submit button
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				success[0] = initializeGraph(data);
				if(success[0]) dispose();
				else JOptionPane.showMessageDialog(null, "Something went wrong.\nMost likely an entry was not properly entered. Make sure to press enter after modifying a cell to ensure the change is recorded.");
			}
		});
	}
	
	private void initializeConnectTable() {
		//reset table model
		tmConnect.setColumnCount(0);
		tmConnect.setRowCount(0);
		
		//add columns
		tmConnect.addColumn("");
		for(int i=0; i<(Integer)spinnerSize.getValue(); i++) {
			tmConnect.addColumn("Node " + (i+1));
		}
		
		//add rows
		Object[] rowContent = new Object[(Integer)spinnerSize.getValue() + 1];
		rowContent[0] = "";
		for(int i=1; i<(Integer)spinnerSize.getValue()+1; i++) {
			rowContent[i] = null;
		}
		for(int i=0; i<(Integer)spinnerSize.getValue(); i++) {
			rowContent[0] = "Node " + (i+1);
			tmConnect.addRow(rowContent);
		}
	}
	
	private void initializeContentTable() {
		tmContent.addColumn("Array Index");
		tmContent.addColumn("Data");
	}
	
	private void updateConnectTable(int value) {
		//reset table model
		tmConnect.setColumnCount(0);
		tmConnect.setRowCount(0);
		
		int row = tmConnect.getRowCount();
		if(value-row > 0) {
			//add columns
			tmConnect.addColumn("");
			for(int i=row; i<value; i++) {
				tmConnect.addColumn("Node " + (i+1));
			}
			
			//add rows
			Object[] rowContent = new Object[(Integer)spinnerSize.getValue() + 1];
			rowContent[0] = "";
			for(int i=row+1; i<value+1; i++) {
				rowContent[i] = null;
			}
			for(int i=row; i<value; i++) {
				rowContent[0] = "Node " + (i+1);
				tmConnect.addRow(rowContent);
			}
		}
		else {
			tmConnect.setRowCount(value);
			tmConnect.setColumnCount(value);
		}
	}
	
	private void updateContentTable(int value) {
		int row = tmContent.getRowCount();
		if(value-row > 0) {
			for(int i=row; i<value; i++) {
				tmContent.addRow(new Object[] {"Node "+(i+1), null});
			}
		}
		else {
			tmContent.setRowCount(value);
		}
	}
	
	private void resetContentTable() {
		for(int i=0; i<tmContent.getRowCount(); i++)
			tmContent.setValueAt(null, i, 1);
	}
	
	private boolean initializeGraph(MessageService back) {
		try {
			int size = (Integer)spinnerSize.getValue() + 1;
			Object[][] data = new Object[size][size];
			if(cbDataType.getSelectedItem().toString() != "None") {
				if(cbDataType.getSelectedItem().toString() == "String") {
					for(int i=0; i<tmContent.getRowCount(); i++) {
						data[0][i+1] = tmContent.getValueAt(i, 1).toString();
					}
				}
				else if(cbDataType.getSelectedItem().toString() == "Integer") {
					for(int i=0; i<tmContent.getRowCount(); i++) {
						data[0][i+1] = Integer.parseInt(tmContent.getValueAt(i, 1).toString());
					}
				}
				else if(cbDataType.getSelectedItem().toString() == "Double") {
					for(int i=0; i<tmContent.getRowCount(); i++) {
						data[0][i+1] = Double.parseDouble(tmContent.getValueAt(i, 1).toString());
					}
				}
			}
			
			for(int i=1; i<size; i++) {
				for(int j=1; j<size; j++) {
					try {
						data[i][j] = Integer.parseInt(tmConnect.getValueAt(i-1, j).toString());
					} catch(Exception e) {
						data[i][j] = 0;
					}
				}
			}
			
			back.setDataGraph(data);
			
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
