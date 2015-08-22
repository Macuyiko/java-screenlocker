import java.awt.Robot;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class AltTabStopper implements Runnable {
	private boolean working = true;
	private JComponent focus = null;
	private JFrame frame = null;
	
	public AltTabStopper (JFrame frame, JComponent focus) {
		this.frame = frame;
		this.focus = focus;
	}

	public static AltTabStopper create(JFrame frame, JComponent focus) {
		AltTabStopper stopper = new AltTabStopper(frame, focus);
		new Thread(stopper, "Alt-Tab Stopper").start();
		return stopper;
	}

	public void stop() {
		working = false;
	}

	public void run() {
	         try {
	             Robot robot = new Robot();
	             while (working) {
	                  robot.keyRelease(KeyEvent.VK_SHIFT);
	                  robot.keyRelease(KeyEvent.VK_ALT);
	                  robot.keyRelease(KeyEvent.VK_WINDOWS);
	                  robot.keyRelease(KeyEvent.VK_CONTROL);
	                  robot.keyRelease(KeyEvent.VK_SPACE);
	                  robot.keyRelease(KeyEvent.VK_TAB);
	                  robot.keyPress(KeyEvent.VK_ESCAPE);
	                  java.awt.EventQueue.invokeLater(new Runnable() {
	                	  public void run() { 
	                		  if (focus != null && !focus.isFocusOwner()) {
	                			  if (frame != null) {
	                				  int state = frame.getExtendedState();
	                			   	  state &= ~JFrame.ICONIFIED;
	                			   	  frame.setExtendedState(state);
	                			   	  frame.toFront();
	                			   	  frame.requestFocus();
	                			  }
	                			  focus.requestFocus();
			                  }
			                  if (frame != null && frame.getState() != JFrame.NORMAL) {
			                	  frame.setState(JFrame.ICONIFIED);
			                	  frame.setState(JFrame.NORMAL);
			                  }
	                	  }
	                  });
	                  try { Thread.sleep(10); } catch(Exception e) {}
	             }
	         } catch (Exception e) { e.printStackTrace(); System.exit(-1); }
     }
}