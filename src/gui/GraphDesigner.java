/*
 * Grady Landers
 * Master's Project - Code name Rubber Duck
 * graph creator gui for front-end
 */

package gui;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import backend.MessageService;
import backend.MyUtilities;

import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

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
	private JTable tblConnect;
	private MyTableModel tmConnect;
	private JButton btnSubmit;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmSave;
	private JMenuItem mntmLoad;

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
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		
		mntmLoad = new JMenuItem("Load");
		mnFile.add(mntmLoad);
		initializeComponents(success[1]);
		initializeActions(success, data);
	}
	
	private void initializeComponents(boolean weighted) {
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
		
		tmConnect = new MyTableModel();
		tblConnect = new JTable();
		tblConnect.setRowSelectionAllowed(false);
		spConnections.setViewportView(tblConnect);
		
		tmContent = new DefaultTableModel();
		initializeContentTable();
		JTable tblContent = new JTable(tmContent);
		spContent.setViewportView(tblContent);
		getContentPane().setLayout(groupLayout);
	}
	
	private void initializeActions(boolean[] success, MessageService data) {
		//FILE MENU
		//save
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = "No valid graph has been entered.";
				
				if(!validData()) {
					JOptionPane.showMessageDialog(null, message);
					return;
				}
				
				String path;
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileNameExtensionFilter("Rubber Duck Graph (.rdg)", "rdg"));
				fc.setCurrentDirectory(null);
				int val = fc.showSaveDialog(null);
				if(val == JFileChooser.APPROVE_OPTION) {
					path = fc.getSelectedFile().toString();
					if(!path.endsWith(".rdg"))
						path += ".rdg";
					
					if(saveData(path)) message = "Data saved successfully.";
					else message = "There was a problem saving the data.\\nCheck the console for more info.";
					
					JOptionPane.showMessageDialog(null, message);
				}
			}
		});
		
		//load
		mntmLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(validData()) {
					if(JOptionPane.showConfirmDialog(null, "There is a valid graph present. Are you sure you want to overwrite it?") == JOptionPane.CANCEL_OPTION)
						return;
				}
				
				String path;
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileNameExtensionFilter("Rubber Duck Graph (.rdg)", "rdg"));
				fc.setCurrentDirectory(null);
				int val = fc.showOpenDialog(null);
				if(val == JFileChooser.APPROVE_OPTION) {
					path = fc.getSelectedFile().toString();
					
					if(!loadData(path, success[1])) JOptionPane.showMessageDialog(null, "There was a problem loading the data.\nCheck the console for more info.");
				}
			}
		});
		
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
				initializeConnectTable(success[1]);
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
					if(value != null && !MyUtilities.isInt(value.toString())) {
						JOptionPane.showMessageDialog(null, "Invalid Input:\nYour input could not be determined as an Integer.");
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
	
	private void initializeConnectTable(boolean weighted) {
		//make a new table model out of an Object[][]
		int n = (Integer)spinnerSize.getValue();
		if(n == 0) return;
		String[] columns = new String[n+1];
		Object[][] data = new Object[n][n+1];
		
		//add columns
		columns[0] = "";
		for(int i=1; i<=n; i++) {
			columns[i] = "Node "+i;
		}
		
		//add rows
		for(int i=0; i<n; i++) {
			data[i][0] = "Node "+(i+1);
			for(int j=1; j<=n; j++) {
				if(weighted) data[i][j] = 0;
				else data[i][j] = Boolean.FALSE;
			}
		}
		
		//reset
		tmConnect = new MyTableModel(data, columns);
		tblConnect.setModel(tmConnect);
	}
	
	private void updateGraphModel(Object[][] data, boolean weighted) {
		String[] columns = new String[data.length+1];
		columns[0] = "";
		for(int i=1; i<data.length+1; i++) {
			columns[i] = "Node "+i;
		}
		
		for(int i=0; i<data.length; i++) {
			data[i][0] = "Node "+(i+1);
			for(int j=1; j<data[i].length && !weighted; j++) {
				data[i][j] = Integer.parseInt(data[i][j].toString()) > 0;
			}
		}
		
		tmConnect = new MyTableModel(data, columns);
		tblConnect.setModel(tmConnect);
	}
	
	private void initializeContentTable() {
		tmContent.addColumn("Array Index");
		tmContent.addColumn("Data");
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
	
	private void updateContentTable(Object[] content) {
		tmContent.setRowCount(0);
		for(int i=0; i<content.length; i++) {
			tmContent.addRow(new Object[] {"Node "+(i+1), content[i].toString()});
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
						if((Boolean)tmConnect.getValueAt(i-1, j)) data[i][j] = 1;
						else data[i][j] = 0;
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
	
	//save graph to a file
	private boolean saveData(String path) {
		try {
			//initialize file writer
			FileWriter fw = new FileWriter(new File(path));
			
			//write data structure
			int size = (Integer)spinnerSize.getValue();
			
			fw.append(cbDataType.getSelectedItem().toString() + "\n");
			if(cbDataType.getSelectedItem().toString() != "None") {
				for(int i=0; i<size; i++) {
					fw.append(tmContent.getValueAt(i, 1).toString());
					if(i < size-1) fw.append(",");
				}
				fw.append("\n");
			}
			
			for(int i=0; i<size; i++) {
				for(int j=1; j<size+1; j++) {
					try {
						fw.append(Integer.parseInt(tmConnect.getValueAt(i, j).toString()) + "");
					}
					catch(Exception e) {
						if((Boolean)tmConnect.getValueAt(i, j)) fw.append("1");
						else fw.append("0");
					}
					if(j < size) fw.append(",");
				}
				if(i < size-1) fw.append("\n");
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
	
	//load graph from a file
	private boolean loadData(String path, boolean weighted) {
		try {
			//initialize file reader
			BufferedReader fr = new BufferedReader(new FileReader(path));
			
			//read data structure
			cbDataType.setSelectedItem(fr.readLine());
			if(!cbDataType.getSelectedItem().toString().contentEquals("None")) {
				String[] temp = fr.readLine().split(",");
				updateContentTable(temp);
			}
			
			Object[] temp = fr.readLine().split(",");
			if(temp.length > 0) {
				Object[][] graph = new Object[temp.length][];
				
				for(int i=0; i<graph.length && temp!=null; i++) {
					graph[i] = new Object[temp.length+1];
					for(int j=0; j<temp.length; j++) {
						graph[i][j+1] = temp[j];
					}
					
					try { temp = fr.readLine().split(","); }
					catch(Exception e) { temp = null; }
				}
				
				spinnerSize.setValue(graph.length);
				updateGraphModel(graph, weighted);
			}
			else spinnerSize.setValue(0);  
			
			fr.close();
			
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//check that the graph is valid
	private boolean validData() {
		if((Integer)spinnerSize.getValue() < 1) return false;
		
		if(!cbDataType.getSelectedItem().toString().contentEquals("None")) {
			for(int i=0; i<tmContent.getRowCount(); i++) {
				if(tmContent.getValueAt(i, 1) == null) return false;
			}
		}
		
		return true;
	}
	
	//modified from https://kodejava.org/how-do-i-render-boolean-value-as-checkbox-in-jtable-component/
	class MyTableModel extends AbstractTableModel {
        //default serial version uid
		private static final long serialVersionUID = 1L;
		String[] columns;
        Object[][] data;
        
        public MyTableModel(Object[][] d, String[] c) {
        	data = d;
        	columns = c;
        }
        
        public MyTableModel() {
        	data = new Object[0][0];
        	columns = new String[0];
        }

        public int getRowCount() {
            return data.length;
        }

        public int getColumnCount() {
            return columns.length;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }
        
        public boolean isCellEditable(int row, int column) {
        	return column != 0;
        }
        
        public void toggleValue(int r, int c) {
        	if(data[r][c] instanceof Boolean) {
        		data[r][c] = !(Boolean)data[r][c];
        	}
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }
        
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return data[0][columnIndex].getClass();
        }
        
        public void setValueAt(Object o, int row, int col) {
        	data[row][col] = o;
        }
    }
}
