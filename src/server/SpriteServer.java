/* File Name: SpriteServer.java
 * Author: Byung Seon Kim
 * Date: 6 March 2017
 * Description: start RMI server
 */

package server;

import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import server.SpriteRMIImpl;
import server.SpriteRMIInterface;

/**
 * start RMI server.
 * @author Byung Seon Kim
 */
public class SpriteServer {
	/** the default port number for server */
	public static final int DEFAULT_PORT = 8082;
	/** RMI service name */
	public static final String RMI_SERVICE_NAME = "SpriteService";

	/**
	 * Constructor for initializing instance variables
	 * @param serverName server name
	 * @param portNumber server port number
	 * @throws NotBoundException java.rmi.NotBoundException
	 * @throws MalformedURLException java.net.MalformedURLException
	 * @throws RemoteException java.rmi.RemoteException
	 */
    public SpriteServer ( String serverName, int portNumber ) throws NotBoundException, MalformedURLException, RemoteException {
    	SpriteRMIInterface remoteObj = new SpriteRMIImpl();
    	// LocateRegistry is used to obtain a reference to a bootstrap remote object registry on a particular host 
    	// (including the local host), or to create a remote object registry that accepts calls on a specific port.
    	// -------------------------------------------------------------------------------------------------
    	// Creates and exports a Registry instance on the local host that accepts requests on the specified port.
        LocateRegistry.createRegistry( portNumber );
        System.out.println( "Registry created..." );
        // Used for exporting a remote object with JRMP and obtaining a stub that communicates to the remote object. 
        // Stubs are either generated at runtime using dynamic proxy objects, or they are generated statically at build time, 
        // typically using the rmic tool.
        // -------------------------------------------------------------------------------------------------
        // There are six ways to export remote objects:
        // 1. Subclassing UnicastRemoteObject and calling the UnicastRemoteObject() constructor.
        // 2. Subclassing UnicastRemoteObject and calling the UnicastRemoteObject(port) constructor.
        // 3. Subclassing UnicastRemoteObject and calling the UnicastRemoteObject(port, csf, ssf) constructor.
        // 4. Calling the exportObject(Remote) method. Deprecated.
        // 5. Calling the exportObject(Remote, port) method. ---------- USED
        // 6. Calling the exportObject(Remote, port, csf, ssf) method.
        UnicastRemoteObject.exportObject( remoteObj, portNumber );
        System.out.println( "Exported Remote Object..." );
        // The Naming class provides methods for storing and obtaining references to remote objects in a remote object registry. 
        // rebind: Rebinds the specified name to a new remote object.
        Naming.rebind( "rmi://" + serverName + ":" + portNumber + "/" + RMI_SERVICE_NAME, remoteObj );
        System.out.println( "Rebinded the name (" + RMI_SERVICE_NAME + ") to a remote object..." );
    }

    /** 
     * Entry point of the program, if there are no arguments, use default host and port number
     * @param args command line parameters
     */
    public static void main( String[] args ) {
    	String serverName = "";
    	int portNumber = DEFAULT_PORT;
    	
    	switch ( args.length ) {
        	case 0: // if no argument, use local host 
        		try { 
        			// Inet4Address class represents an Internet Protocol version 4 (IPv4) address.
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
        		System.out.println("usage: SpriteServer [ServerName] [PortNumber]");
        	break;
        }  
    	
    	try { 
    		new SpriteServer( serverName, portNumber );
    	} catch ( NotBoundException | MalformedURLException | RemoteException ex ) {
    		ex.printStackTrace();
    	}
    }
}
