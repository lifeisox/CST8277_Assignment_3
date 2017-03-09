/* File Name: SpriteClient.java
 * Author: Byung Seon Kim
 * Date: 6 March 2017
 * Description: The presentation layer (this class) will need to display the Sprites as they exist 
 * 	as persistent object on the server.
 */

package client;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import server.SpriteEntity;
import server.SpriteRMIInterface;
import server.SpriteServer;

/**
 * this class will need to display the Sprites as they exist as persistent object on the server.
 * @author Byung Seon Kim
 */
public class SpriteClient extends JPanel {
	/** Eclipse generated Serial Version ID */
	private static final long serialVersionUID = -7421235242316942282L;
	/** Remote object to access service's methods */
	private SpriteRMIInterface remoteObj;

	/**
	 * Constructor for initializing instance variables
	 * @param serverName server host name
	 * @param portNumber server port number
	 * @throws NotBoundException java.rmi.NotBoundException;
	 * @throws MalformedURLException java.net.MalformedURLException
	 * @throws RemoteException java.rmi.RemoteException
	 */
    public SpriteClient ( String serverName, int portNumber ) throws NotBoundException, MalformedURLException, RemoteException {
    	
    	// The RMI registry allows the clients look up the remote objects by name
		remoteObj = (SpriteRMIInterface) Naming.lookup(
    			"rmi://" + serverName + ":" + portNumber + "/" + SpriteServer.RMI_SERVICE_NAME );
		
    	// Adds the specified mouse listener to receive mouse events from this component.
    	this.addMouseListener( new Mouse() ); 
		this.setFocusable( true );
		// set panel size
		this.setPreferredSize(new Dimension( remoteObj.getScreenWidth(), remoteObj.getScreenHeight() ));
    	JFrame jFrame = new JFrame( "Bouncing Sprites using RMI & Hibernate" );
    	jFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    	jFrame.add( this );
    	jFrame.setVisible( true );
    	jFrame.setResizable( false );
    	jFrame.pack(); // adjust jFrame size for panel size
    }
    
    /**
     * Calls the UI delegate's paint method, if the UI delegate is non-null. 
     * We pass the delegate a copy of the Graphics object to protect the rest of the paint code 
     * from irrevocable changes (for example, Graphics.translate).
     * @param g the Graphics object to protect
     */
	@Override
	protected void paintComponent( Graphics g ) {
		super.paintComponent(g);
		try {
			List<?> sprites = remoteObj.getSprites();
			if ( sprites != null ) {
				for ( Iterator<?> iterator = sprites.iterator(); iterator.hasNext(); ){
					SpriteEntity sprite = (SpriteEntity) iterator.next(); 
					sprite.draw(g);
				}
			}
		} catch ( RemoteException ex ) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Inner class for implementing when mouse buttons are pressed
	 */
	private class Mouse extends MouseAdapter {
		@Override
	    public void mousePressed( final MouseEvent event ) {
			try {
				remoteObj.createSprite( event.getX(), event.getY() );
			} catch ( RemoteException ex ) {
				ex.printStackTrace();
			}
	    }
	}
	
	/**
	 * The start() method is to start animation
	 * @throws RemoteException java.rmi.RemoteException
	 * @throws InterruptedException InterruptedException
	 */
    public void start() throws RemoteException, InterruptedException {
    	while (true){ // never returns due to infinite loop
    		repaint();  
    		Thread.sleep( remoteObj.getSleep() );  // use the sleep value from server
    	}
    }

    /** 
     * Entry point of the program, if there are no arguments, use default host and port number
     * @param args command line parameters
     */
    public static void main( String[] args ) {
    	String serverName = "";
    	int portNumber = SpriteServer.DEFAULT_PORT;
    	
    	switch ( args.length ) {
        	case 0: // if no argument, use local host 
        		try { 
        			serverName = Inet4Address.getLocalHost().getHostName();
        		} catch ( UnknownHostException ex ) {
        			ex.printStackTrace();
        		}
        		break;
        	case 1: 
        		serverName = args[0];
        		break;
        	case 2:
        		serverName = args[0];
        		portNumber = Integer.parseInt(args[1]);
        		break;
        	default:
        		System.out.println("usage: SpriteClient [ServerName] [PortNumber]");
        	break;
        }  
    	
    	try { 
    		new SpriteClient( serverName, portNumber ).start();
    	} catch ( NotBoundException | MalformedURLException | RemoteException | InterruptedException ex ) {
    		ex.printStackTrace();
    	}
    }
}
