package elearning;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;


public class EEditor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	

	
			try{
				for (LookAndFeelInfo info:UIManager.getInstalledLookAndFeels()){
					if("Nimbus".equals(info.getName())){
						UIManager.setLookAndFeel(info.getClassName());
						break;
					}else{
						UIManager.getCrossPlatformLookAndFeelClassName();
					}
				}
			} 
			catch (UnsupportedLookAndFeelException e) {
				// handle exception
			}
			catch (ClassNotFoundException e) {
				// handle exception
			}
			catch (InstantiationException e) {
				// handle exception
			}
			catch (IllegalAccessException e) {
				// handle exception
			}
			
			EEFrame frame = new EEFrame();
			Thread t1=new Thread(frame.Words());
			t1.start();
			frame.setSize(500, 600);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			frame.setVisible(true);
			
		
		
		}
			
		
		
			
}
