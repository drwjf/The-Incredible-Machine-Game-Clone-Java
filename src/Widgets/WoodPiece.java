//
// WoodPiece.java
//
// Created on 01/11/08
// Copyright (c) 2008 GroupFour
//
package Widgets;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

import net.phys2d.raw.shapes.Box;
import net.phys2d.math.*;
import net.phys2d.raw.*;

/**
 * This WoodPiece acts as an object that is just there,
 * bounces objects off as normal and acts like an obstacle.
 */

public class WoodPiece implements Widget
{
	private static final int width = 100;
	private static final int height = 10;
	private Vector2f[] position;
	private boolean locked;
	private boolean activated;
	private boolean resetted;
	private Direction dir;
	private Body[] b;
	private ImageIcon imgicon;
	private BufferedImage imgshown;
	
	/**
	 * The no constructor WoodPiece, this no constructor WoodPiece 
	 * creates a WoodPiece with initial position of 0,0 (top left)
	 * which is not locked, not resetted, not activated, and also
	 * has not been detonated yet
	 */
	public WoodPiece()
	{
		b = new Body[1];
		b[0] = new StaticBody(new Box(width, height));
		b[0].setRestitution(0.8f);
		position = new Vector2f[4];
		for (int i = 0; i < 4; i++)
		{
			position[i] = new Vector2f();
		}
		imgicon = new ImageIcon(getClass().getClassLoader().getResource("resources/img/woodn.gif"));
		initializePiece();
		setPosition(new Vector2f((float)0.0, (float)0.0));
		locked = false;
		resetted = false;
		dir = Direction.WEST;
	}

	/*
	* Private method for our use, it initializes the bomb so that the image
	* is the actual image that it's supposed to be displaying
	*/
	private void initializePiece()
	{
		try
		{
			imgshown = ImageIO.read(getClass().getClassLoader().getResource("resources/img/woodn.gif"));
		}
		catch(IOException e)
		{
			System.err.println("Error loading img");
			imgshown = null;
		}
		/*if (dir == Direction.EAST || dir == Direction.WEST)
		{
			rotateHorizontal();
		}
		dir = Direction.NORTH;*/
		activated = false;
	}

	/**
	 * Set the position of the WoodPiece.
	 *
	 * @param f  The x,y coordinates of the top left 
	 *           x-y coordinates of the Widget within the container.
	 */
	public void setPosition(Vector2f f)
	{
		if (this.dir == Direction.WEST || this.dir == Direction.EAST)
			b[0].setPosition(f.getX() + width/2, f.getY() + height/2);
		else
			b[0].setPosition(f.getX() + height/2, f.getY() + width/2);
		/*position[0].set((ROVector2f) f);
		position[1].set(f.getX() + width, f.getY());
		position[2].set(f.getX() + width, f.getY() + height);
		position[3].set(f.getX(), f.getY() + height);
		*/
	}

	/**
	 * Set the top-left x-coordinate of the WoodPiece.
	 * it is equivalent to setPosition(new Vector2f(x, getPositionY()));
	 *
	 * @param x  The x coordinate within the container.
	 */
	public void setPositionX(float x)
	{
		/*b[0].setPosition(x + 50, b[0].getPosition().getY());
		position[0].set(x, position[0].getY());
		position[1].set(x + width, position[1].getY());
		position[2].set(x + width, position[2].getY());
		position[3].set(x, position[3].getY());*/
		this.setPosition( new Vector2f(x, this.getPositionY()) );
	}

	/**
	 * Set the top-left y-coordinate of the WoodPiece.
	 *
	 * @param y  The y coordinate within the container.
	 */
	public void setPositionY(float y)
	{
		/*b[0].setPosition(b[0].getPosition().getX(), y + 5);
		position[0].set(position[0].getX(), y);
		position[1].set(position[1].getX(), y);
		position[2].set(position[2].getX(), y + height);
		position[3].set(position[3].getX(), y + height);*/
		this.setPosition( new Vector2f(this.getPositionX(), y) );

	}

	/**
	 * Get the top-left position of this WoodPiece.
	 *
	 * @return   The x,y coordinates within the container.
	 */
	public Vector2f getPosition()
	{
		ROVector2f pos = b[0].getPosition();
		if (this.dir == Direction.EAST || this.dir == Direction.WEST)
			return new Vector2f( pos.getX()-width/2 , pos.getY()-height/2 );
		else
			return new Vector2f( pos.getX()-height/2 , pos.getY()-width/2 );
	}

	/**
	 * Get the top-left x-coordinate of this WoodPiece.
	 *
	 * @return   The x coordinate within the container.
	 */
	public float getPositionX()
	{
		return this.getPosition().getX();
	}

	/**
	 * Get the top-left y-coordinate of this WoodPiece.
	 *
	 * @return   The y coordinate within the container.
	 */
	public float getPositionY()
	{
		return this.getPosition().getY();
	}

	/**
	 * Draws the widget.
	 * @param g  The graphic to draw onto.
	 */
	public void draw (Graphics2D g)
	{
		/*Color old = g.getColor();
		
		Vector2f[] points = ((Box)b[0].getShape()).getPoints(b[0].getPosition(),b[0].getRotation());
		
		int xs[] = { 0, 0, 0, 0 };
		int ys[] = { 0, 0, 0, 0 };
		for( int i=0; i<4; i++ )
		{
			xs[i] = (int) points[i].getX();
			ys[i] = (int) points[i].getY();
		}

		g.fill( new Polygon(xs, ys, 4) );
						
		g.setColor(old);
	*/
		if (imgshown != null)
		{
		    // we read in image properly in constructor, use it
		    
		    // rotation transform
		    AffineTransform xform = new AffineTransform();
		    xform.setToRotation(b[0].getRotation());
		    
		    g.drawImage(
		                imgshown,
		                new AffineTransformOp(xform, AffineTransformOp.TYPE_BILINEAR),
		                (int)this.getPosition().getX() + (this.dir == Direction.NORTH || this.dir == Direction.SOUTH? height: 0),
		                (int)this.getPosition().getY()
		    );
		    
		}
	}


	/**
	 * Activates the widget.
	 * refuses to become activated if the widget has not been reset yet.
	 * Must be called by the engine for every widget when the gameplay start.
	 */
	public void activateWidget ()
	{
		if (resetted)
		{
			activated = true;
		}
	}

	/**
	 * Resets the widget to start state.
	 * Should reinitialize everything to with all bodies associated with this.
	 * Should reinitialize its own internal state information as well.
	 * Ensure that the caller invokes this once before allowing
	 * activateWidget() to do something.
	 * Should probably call each body's set(Shape s, float mass) in here.
	 */
	public void resetWidget()
	{
		resetted = true;
		initializePiece();
	}

	/**
	 * Rotates the Woodpiece clockwise using its middle as the rotation axis 
	 *
	 */
	public void rotateClockwise()
	{
		if (dir == Direction.NORTH)
		{
			dir = Direction.EAST;
			rotateVertical();
			setImage(dir);
		}
		else if (dir == Direction.EAST)
		{
			dir = Direction.SOUTH;
			rotateHorizontal();
			setImage(dir);
		}
		else if (dir == Direction.SOUTH)
		{
			dir = Direction.WEST;
			rotateVertical();
			setImage(dir);
		}
		else
		{
			dir = Direction.NORTH;
			rotateHorizontal();
			setImage(dir);
		}
	}

	/**
	 * Rotates the Woodpiece clockwise using its middle as the rotation axis 
	 *
	 */
	public void rotateCounterClockwise ()
	{
		if (dir == Direction.NORTH)
		{
			dir = Direction.WEST;
			rotateVertical();
			setImage(dir);
		}
		else if (dir == Direction.EAST)
		{
			dir = Direction.NORTH;
			rotateHorizontal();
			setImage(dir);
		}
		else if (dir == Direction.SOUTH)
		{
			dir = Direction.EAST;
			rotateVertical();
			setImage(dir);
		}
		else
		{
			dir = Direction.SOUTH;
			rotateHorizontal();
			setImage(dir);
		}
	}

	/*
	* For personal class use, this rotates the rectangle vertically
	*/
	private void rotateVertical()
	{
		b[0].setShape(new Box(height, width));
		position[0].set(position[0].getX() + 45, position[0].getY() - 45);
		position[1].set(position[1].getX() - 45, position[1].getY() - 45);
		position[2].set(position[2].getX() - 45, position[2].getY() + 45);
		position[3].set(position[3].getX() + 45, position[3].getY() + 45);

		b[0].setPosition(position[0].getX() + 5, position[0].getY() + 50);
	}

	/*
	* For personal class use, this rotates the rectangle horizontally
	*/
	private void rotateHorizontal()
	{
		b[0].setShape(new Box(height, width));
		position[0].set(position[0].getX() - 45, position[0].getY() + 45);
		position[1].set(position[1].getX() + 45, position[1].getY() + 45);
		position[2].set(position[2].getX() + 45, position[2].getY() - 45);
		position[3].set(position[3].getX() - 45, position[3].getY() - 45);
		b[0].setPosition(position[0].getX() + 50, position[0].getY() + 5);
	}

	/*
	* For personal class use, sets the image shown to be the direction shown
	* so that when drawing the widget it will be the proper image, and direction
	*/
	private void setImage(Direction d)
	{
		if (d == Direction.NORTH)
		{
			try
			{
				imgshown = ImageIO.read(getClass().getClassLoader().getResource("resources/img/woodn.gif"));
			}
			catch(IOException e)
			{
				System.err.println("Error loading img");
				imgshown = null;
			}
		}
		else if (d == Direction.EAST)
		{
			try
			{
				imgshown = ImageIO.read(getClass().getClassLoader().getResource("resources/img/woode.gif"));
			}
			catch(IOException e)
			{
				System.err.println("Error loading img");
				imgshown = null;
			}
		}
		else if (d == Direction.SOUTH)
		{
			try
			{
				imgshown = ImageIO.read(getClass().getClassLoader().getResource("resources/img/woods.gif"));
			}
			catch(IOException e)
			{
				System.err.println("Error loading img");
				imgshown = null;
			}
		}
		else if (d == Direction.WEST)
		{
			try
			{
				imgshown = ImageIO.read(getClass().getClassLoader().getResource("resources/img/woodw.gif"));
			}
			catch(IOException e)
			{
				System.err.println("Error loading img");
				imgshown = null;
			}
		}
	}

	/**
	 * sets the direction of the WoodPiece widget
	 *
	 * @param d Direction to rotate the Widget
	 */
	public void setDirection(Direction d)
	{
		/*
		if (d == Direction.NORTH)
		{
			if (dir == Direction.EAST || dir == Direction.WEST)
			{
				rotateHorizontal();
			}
		}
		else if (d == Direction.EAST)
		{
			if (dir == Direction.NORTH || dir == Direction.SOUTH)
			{
				rotateVertical();
			}
		}
		else if (d == Direction.SOUTH)
		{
			if (dir == Direction.EAST || dir == Direction.WEST)
			{
				rotateHorizontal();
			}
		}
		else if (d == Direction.WEST)
		{
			if (dir == Direction.NORTH || dir == Direction.SOUTH)
			{
				rotateVertical();
			}
		}
		dir = d;
		setImage(dir);*/
		Vector2f old_pos = this.getPosition();
		if (d == Direction.NORTH || d == Direction.SOUTH)
		{
			b[0].setRotation((float)(Math.PI / 2.0));
		}
		else if (d == Direction.EAST || d == Direction.WEST)
		{
			b[0].setRotation(0f);
		}
		
		dir = d;
		/* update position now with updated direction being set */
		this.setPosition( old_pos );
	}

	/**
	 * Returns the Direction where the WoodPiece is facing
	 *
	 * @return the direction the WoodPiece is facing
	 */
	public Direction getDirection ()
	{
		return dir;
	}

	/**
	 * Determines if the WoodPiece is active.
	 *
	 * @return whether or not the widget is active.
	 */
	public boolean isActive()
	{
		return activated;
	}

	/**
	 * Gets the name of the widget. Which is "Wood Piece"
	 *
	 * @return The name of this widget.
	 */
	public String getName ()
	{
		return "Wood Piece";
	}

	/**
	 * Gets the type of the WoodPiece
	 * Type of WoodPiece is ActionType.STATIC.
	 *
	 * @return always returns to be of ActionType.STATIC since this is the category
	 *         the WoodPiece
	 */
	public ActionType getType()
	{
		return ActionType.STATIC;
	}

	/**
	 * What happens with the widget when two bodies touch.
	 * (this doesn't do anything special since its a static and it was made for an static object)
	 * Ensure that the widget carefully handles
	 * collision events between its own bodies.
	 * doesn't do anything special, lets the phys2d handle the basic collisions
	 *
	 * @param e The collision event that was triggered by a collision.
	 */
	public void reactToTouchingBody(CollisionEvent e)
	{
	}

	/**
	 * Tell if the WoodPiece can connect to joints or not.
	 *
	 * @return always returns false since this WoodPiece is not connectable
	 */
	public boolean isConnectable()
	{
		return false;
	}

	/**
	 * Sets the points where joints will connect to this widget.
	 * If the widget is not connectable, do nothing.
	 *
	 * @param points The points within the widget that designate
	 * where joints connect to.
	 */
	public void setConnectionPoints(Vector2f[] points)
	{
	}

	/**
	 * Whether or not the WoodPiece is locked and cannot be moved.
	 *
	 * @return If the widget is locked (true) or not (false).
	 */
	public boolean isLocked()
	{
		return locked;
	}

	/**
	 * Sets the lock state of the WoodPiece.
	 * Lock state returned by isLocked should reflect the change.
	 *
	 * @param locked If the widget is to be locked (true) or not (false).
	 */
	public void setLock(boolean locked)
	{
		this.locked = locked;
	}

	/**
	 * This widget is not connectable so it will do nothing and return null
	 *
	 * @param point The x,y coordinates where the joint is attempting to connected to.
	 *
	 * @return returns null since this WoodPiece does not have joints
	 */
	public Vector2f attachJoint (Vector2f point)
	{
		return null;
	}

	/**
	 * This will return the body of what the WoodPiece uses
	 *
	 * @return An array of bodies.
	 */
	public Body[] getBodiesForSimulation ()
	{
		return b;
	}

	/**
	 * returns nothing since this WoodPiece does not have any joints
	 *
	 * @return this is going to return null since this widget doesn't have joints
	 */
	public Joint[] getJointsForSimulation ()
	{
		return null;
	}

	/**
	 * does nothing since theres no joint connecting to this widget
	 *
	 * @param anchor_point The x,y coordinates of the connected Joint.
	 */
	public void receiveImpulse(Vector2f anchor_point)
	{
	}

	/**
	 * Gets the four boundary vertices of the WoodPiece,
	 *
	 * @return An array of Vector2f which will be the four corners x,y coordinates.
	 */
	public Vector2f[] getBoundary()
	{
		Vector2f[] points = ((Box)b[0].getShape()).getPoints(b[0].getPosition(),b[0].getRotation());
		return points;
	}

	/**
	 * Retrieves the description of the widget.
	 *
	 * @return The small blurb describing what the widget does.
	 */
	public String getDescription ()
	{
		return "This wood piece widget is just a obstacle piece in the game";
	}

	/**
	 * This returns the image that i made, which is 40x50
	 * depending if the bomb activated or not it will have an undetonated bomb
	 * or a detonated bomb
	 *
	 * @return The image which is linked with the widget.
	 */
	public ImageIcon getIcon ()
	{
		return imgicon;
	}

}
