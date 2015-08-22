
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;	

public class ScreenLock extends JPanel  {
	
	private static String PASSWORD = "changeme";
	private static String MESSAGE = "--- Do not close this laptop! ---";
	
    private JFrame f = new JFrame("Screen Locked");
    private JLabel l = new JLabel("Workstation Locked");
    private JButton b = new JButton("UNLOCK");
    private JPasswordField t = new JPasswordField();
    private JLabel m = new JLabel();
    
	private ActionListener buttonListener;
    
	public static void main(String[] args) {
		if (args.length > 0) PASSWORD = args[0];
		if (args.length > 1) MESSAGE = args[1];
		
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ScreenLock().display();
            }
        });
    }
	
	public ScreenLock() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(l);
        this.add(t);
		this.add(b);
		this.add(m);
		this.add(Box.createVerticalGlue());
		
		this.buttonListener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (new String(t.getPassword()).equals(PASSWORD)) {
					f.dispose();
					System.exit(0);
				}
			}
		};
		
		this.setBackground(Color.black);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
		l.setPreferredSize(new Dimension(500,120));
		l.setMaximumSize(new Dimension(500,120));
		l.setFont(t.getFont().deriveFont(Font.BOLD, 48f));
		l.setForeground(Color.white);
		m.setAlignmentX(Component.CENTER_ALIGNMENT);
		m.setFont(t.getFont().deriveFont(Font.BOLD, 32f));
		m.setForeground(Color.white);
		m.setText(MESSAGE);
		t.setAlignmentX(Component.CENTER_ALIGNMENT);
		t.setPreferredSize(new Dimension(500,60));
		t.setMaximumSize(new Dimension(500,60));
		t.setFont(t.getFont().deriveFont(Font.BOLD, 24f));
		t.setBackground(Color.black);
		t.setForeground(Color.white);
		t.setHorizontalAlignment(SwingConstants.CENTER);
		b.setAlignmentX(Component.CENTER_ALIGNMENT);
		b.setFont(t.getFont().deriveFont(Font.BOLD, 16f));
		b.setBackground(Color.black);
		b.setForeground(Color.white);
		
		b.addActionListener(buttonListener);
		
        KeyListener keyListener = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				captureKeys(arg0);
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				captureKeys(arg0);
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				captureKeys(arg0);
			}
        };
        this.addKeyListener(keyListener);
        t.addKeyListener(keyListener);
        this.setFocusable(true);
        t.setFocusTraversalKeysEnabled(false);
        
    }
	
	private void captureKeys(KeyEvent e) {
		if (e.isAltDown() || e.isControlDown() || e.isAltGraphDown() || e.isMetaDown())
			e.consume();
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			buttonListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
	}

	private void display() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice dev = env.getDefaultScreenDevice();
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        f.setResizable(false);
        f.setUndecorated(true);
        f.setAlwaysOnTop(true);
        f.setFocusTraversalKeysEnabled(false);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.add(this);
        f.pack();
        dev.setFullScreenWindow(f);
        f.repaint();
        AltTabStopper.create(f, t);
    }

}
