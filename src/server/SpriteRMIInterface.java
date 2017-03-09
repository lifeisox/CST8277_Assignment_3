/* File Name: SprintRMIInterface.java
 * Author: Byung Seon Kim
 * Date: 6 March 2017
 * Description: The class is to define Remote interface which includes determination of 
 * 	types of objects used as parameters and types of objects used as return values.
 */
package server;

import java.util.List;

/**
 * The class is to define Remote interface which includes determination of 
 * 	types of objects used as parameters and types of objects used as return values.
 * @author Byung Seon Kim
 */
public interface SpriteRMIInterface extends java.rmi.Remote {
	/** return delay time in millisecond */
	public long getSleep() throws java.rmi.RemoteException;
	/** return screen width */
	public int getScreenWidth() throws java.rmi.RemoteException;
	/** return screen height */
	public int getScreenHeight() throws java.rmi.RemoteException;
	/** create a sprite and add to sprite's list */
	public void createSprite( int x, int y ) throws java.rmi.RemoteException;
	/** return sprite's array list */
	public List<?> getSprites() throws java.rmi.RemoteException;
}
