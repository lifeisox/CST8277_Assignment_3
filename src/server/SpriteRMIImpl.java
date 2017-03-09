/* File Name: SpriteRMIImpl.java
 * Author: Byung Seon Kim
 * Date: 6 March 2017
 * Description: the concrete class implement SpriteRMIInterface. The class implements the methods 
 *  of the remote object which is invoked by the clients.
 */
package server;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * the concrete class implement SpriteRMIInterface. The class implements the methods of 
 * 	the remote object which is invoked by the clients.
 * @author Byung Seon Kim
 *
 */
public class SpriteRMIImpl implements SpriteRMIInterface {
	/** default thread sleep time */
	private static final long SLEEP = 40L;
	/** default panel width */
	private static final int SCREEN_WIDTH = 400;
	/** default panel height */
	private static final int SCREEN_HEIGHT = 400;
	/** distance of movement in defined unit time */
	private static final int MAX_SPEED = 5;
	/** save current sprite color */
	private static Integer currentColor = 0;
	/** random object to define the value of dx, dy */
	private Random random;
	/** the SessionFactory object using the properties and mappings in this configuration */
	private SessionFactory sessionFactory;
	
	/**
	 * default constructor: run modules for hibernate programming
	 * @throws java.rmi.RemoteException java.rmi.RemoteException
	 */
	public SpriteRMIImpl() throws java.rmi.RemoteException {
		super();
		random = new Random();
		try {
			// An instance of Configuration allows the application to specify properties and mapping documents to be used 
			// when creating a SessionFactory.
			Configuration config = new Configuration()
					.addAnnotatedClass( SpriteEntity.class ).configure( "hibernate.cfg.xml" );
			// Builder for standard ServiceRegistry instances.	
			StandardServiceRegistryBuilder standardRegistryBuilder = new StandardServiceRegistryBuilder()
					.applySettings( config.getProperties() );
			// Build the StandardServiceRegistry
			ServiceRegistry standardRegistry = standardRegistryBuilder.build();
			// Create a SessionFactory using the properties and mappings in this configuration.
			sessionFactory = config.buildSessionFactory( standardRegistry );
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * return thread sleep value
	 * @throws java.rmi.RemoteException java.rmi.RemoteException
	 */
	@Override
	public long getSleep() throws java.rmi.RemoteException {
		return SLEEP;
	}
	
	/**
	 * return panel width
	 * @throws java.rmi.RemoteException java.rmi.RemoteException
	 */
	@Override
	public int getScreenWidth() throws java.rmi.RemoteException {
		return SCREEN_WIDTH;
	}

	/**
	 * return panel height
	 * @throws java.rmi.RemoteException java.rmi.RemoteException
	 */
	@Override
	public int getScreenHeight() throws java.rmi.RemoteException {
		return SCREEN_HEIGHT;
	}
	
	/**
	 * create a sprite in which user clicks. define a sprite's color in sequential
	 * and horizontal and vertical migration in random.
	 */
	@Override
	public void createSprite( int x, int y ) throws java.rmi.RemoteException {
		Color[] color = { Color.RED, Color.BLUE, Color.GREEN };
		SpriteEntity sprite = new SpriteEntity();
		
		// I don't want horizontal migration or vertical migration, so I changed algorithm for I get a number without zero.
	    int dx = (random.nextInt( MAX_SPEED ) + 1) * (random.nextInt( 2 ) == 1 ? 1 : -1); // dx should be -5, -4, -3, -2, -1, 1, 2, 3, 4, 5 not 0
	    int dy = (random.nextInt( MAX_SPEED ) + 1) * (random.nextInt( 2 ) == 1 ? 1 : -1); // dx should be -5, -4, -3, -2, -1, 1, 2, 3, 4, 5 not 0

		sprite.setX( x );
		sprite.setY( y );
		sprite.setDx( dx );
		sprite.setDy( dy );
		sprite.setColor( color[currentColor++] );
		if ( currentColor > 2 ) { currentColor = 0; } // rotate sprite color	
		
	    // get current session from SessionFactory
	    Session session = sessionFactory.getCurrentSession();
	    try {
		    session.beginTransaction(); // begin transaction
			session.save( sprite );
			session.getTransaction().commit(); // commit transaction
	    } catch ( HibernateException ex ) {
			ex.printStackTrace();
		} finally {
			session.close();
		}

		new Thread( new Runnable() {

			@Override
			public void run() {
				while( true ) {
					sprite.move( SCREEN_WIDTH, SCREEN_HEIGHT ); // move sprite
					// save sprite to database
					Session session = sessionFactory.getCurrentSession();
					try {
						session.beginTransaction();
						session.update( sprite );
						session.getTransaction().commit();
					} catch ( HibernateException ex ) {
						ex.printStackTrace();
					} finally {
						session.close();
					}	
				    try {
				    	Thread.sleep( 40 );
				    } catch ( InterruptedException ex ) {
				    	ex.printStackTrace();
				    }
				}
			}
			
		}).start();
	}
	
	/**
	 * get all sprites from database
	 * @return array of sprite
	 */
	@Override
	public List<?> getSprites() throws java.rmi.RemoteException {
		List<?> sprites = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			sprites = session.createQuery("from SpriteEntity").list();
			session.getTransaction().commit();
		} catch ( HibernateException ex ) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return sprites;
	}
}
