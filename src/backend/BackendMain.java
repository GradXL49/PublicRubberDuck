/*
 * Grady Landers
 * Master's Project - Code name Rubber Duck
 * back-end main
 * 09-21-2021
 */

package backend;

//imports
import java.awt.EventQueue;
import javax.swing.UIManager;
import gui.MainWindow;

public class BackendMain {

	public static void main(String[] args) {
		//initialize message service
		MessageService messages = new MessageService();
		
		//launch main window
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					MainWindow window = new MainWindow(messages);
					window.frmRubberDuck.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
