/* File Name: SpriteEntity.java
 * Author: Byung Seon Kim
 * Date: 6 March 2017
 * Description: The class is a Hibernate Entity for Assignment 3. Sprite objects are to be stored in the database.
 */
package server;
import java.awt.Color;
import java.awt.Graphics;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The class is a Hibernate Entity for Assignment 3. Sprite objects are to be stored in the database.
 * @author Byung Seon Kim
 */
@Entity
@Table(name="sprite")  // optionally we can specify a table name other than the class name
public class SpriteEntity implements java.io.Serializable {
	/** Eclipse generated Serial Version ID */
	private static final long serialVersionUID = 5574590873668466737L;
	/** the radius size of the sprite */
	private static final int RADIUS = 5;
	/* These instance variables are not annotated, so Hibernate will use property access */
	/** the id of the sprite. It should be auto-increment integer. */
	private int id;
	/** the x axis of the left corner of the sprite */
	private int x;	
	/** the y axis of the upper corner of the sprite */
	private int y; 
	/** the x movement of the sprite */
	private int dx;
	/** the y movement of the sprite */
	private int dy; 
	/** the color of the sprite */
	private Color color;	

	/**
	 * The getter of the primary key which is auto-increment id
	 * @return return the primary key
	 */
	@Id 	// the getter of the primary key is annotated, so Hibernate will use property access
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto generate number
	@Column(name = "id", updatable = false, nullable = false) // name: database field name
	public int getId() { return id; }
	
	/**
	 * The setter of the primary key
	 * @param id the primary key to assign
	 */
	public void setId( int id ) { this.id = id; }

	/**
	 * Get x axis which is the very left of the sprite
	 * @return the x axis which is the very left of the sprite
	 */
	@Column(name = "x", nullable = false, columnDefinition = "int default 0")
	public int getX() { return x; }

	/**
	 * Set x axis which is the very left corner of the sprite
	 * @param x the x axis to set
	 */
	public void setX( int x ) { this.x = x; }

	/**
	 * Get y axis which is the very top of the sprite
	 * @return the y axis which is the very top of the sprite
	 */
	@Column(nullable = false, columnDefinition = "int default 0")
	public int getY() { return y; }

	/**
	 * Set y axis which is the very top of the sprite
	 * @param y the y axis to set
	 */
	public void setY(int y) { this.y = y; }

	/**
	 * Get the x movement of the sprite
	 * @return the x movement of the sprite
	 */
	@Column(nullable = false, columnDefinition = "int default 0")
	public int getDx() { return dx; }

	/**
	 * Set the x movement of the sprite
	 * @param dx the x movement to set
	 */
	public void setDx( int dx ) { this.dx = dx; }

	/**
	 * Get the y movement of the sprite
	 * @return the y movement of the sprite
	 */
	@Column(nullable = false, columnDefinition = "int default 0")
	public int getDy() { return dy; }

	/**
	 * Set the y movement of the sprite
	 * @param dy the y movement to set
	 */
	public void setDy( int dy ) { this.dy = dy; }

	/**
	 * Get the color of the sprite, Convert a String with format red|green|blue|alpha to a Color object
	 * @return the color of the sprite
	 */
	@Column(nullable = false)
	@Convert(converter = ColorConverter.class)
	public Color getColor() { return color; }

	/**
	 * Set the color of the sprite, Convert Color object to a String with format red|green|blue|alpha
	 * @param color the color to set
	 */
	@Convert(converter = ColorConverter.class)
	public void setColor(Color color) { this.color = color; }
	
	/**
     * Draw a sprite 
     * @param g the Graphics object to protect
     */
    public void draw( Graphics g ) {
        g.setColor( this.getColor() );
	    g.fillOval( this.getX(), this.getY(), RADIUS * 2, RADIUS * 2 );
    }
    
    /**
     * Move the sprite. If the sprite meet the boundaries of screen width and height, change direction
     */
    public void move( int panelWidth, int panelHeight ) {
        // check for bounce and make the ball bounce if necessary
    	// bounce off the left wall
        if (x < 0 && dx < 0) {  
            x = 0;
            dx = -dx;
        }
        //bounce off the top wall
        if (y < 0 && dy < 0){
            y = 0;
            dy = -dy;
        }
        // bounce off the right wall
        if (x > panelWidth - (RADIUS * 2) && dx > 0){
            //bounce off the right wall
        	x = panelWidth - RADIUS * 2;
        	dx = - dx;
        }   
        // bounce off the bottom wall
        if (y > panelHeight - (RADIUS * 2) && dy > 0){
            //bounce off the bottom wall
        	y = panelHeight - RADIUS * 2;
        	dy = -dy;
        }

        //make the ball move
        x += dx;
        y += dy;
    }
}
