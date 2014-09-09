//
// TennisBall.java
//
// Created on 01/11/08
// Copyright (c) 2008 GroupFour
//
package Widgets;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

import net.phys2d.raw.shapes.Circle;
import net.phys2d.math.*;
import net.phys2d.raw.*;

/**
 * This TennisBall acts as an object that is just there,
 * bounces objects off as normal and acts like an obstacle.
 */

public class TennisBall implements Widget
{
	private static final float RADIUS = 10f;
	private static final int MASS = 5;
	private Vector2f[] position;
	private boolean locked;
	private boolean activated;
	private boolean resetted;
	private Direction dir;
	private Body[] b;
	private ImageIcon imgicon;
	private BufferedImage imgshown;
	
	/**
	 * The no constructor TennisBall, this no constructor TennisBall 
	 * creates a TennisBall with initial position of 0,0 (top left)
	 * which is not locked, not resetted, not activated
	 */
	public TennisBall()
	{
		b = new Body[1];
		b[0] = new Body(new Circle(RADIUS), MASS);
		b[0].setRestitution(0.95f);
		position = new Vector2f[4];
		for (int i = 0; i < 4; i++)
		{
			position[i] = new Vector2f();
		}
		imgicon = new ImageIcon(getClass().getClassLoader().getResource("resources/img/tennisball.gif"));
		initializeBall();
		setPosition(new Vector2f((float)0.0, (float)0.0));
		locked = false;
		resetted = false;
		dir = Direction.NORTH;
	}

	/*
	* Private method for our use, it initializes the ball so that the image
	* is the actual image that it's supposed to be displaying
	*/
	private void initializeBall()
	{
		try
		{
			imgshown = ImageIO.read(getClass().getClassLoader().getResource("resources/img/tennisball.gif"));
		}
		catch(IOException e)
		{
			System.err.println("Error loading img resources/img/tennisball.gif");
			imgshown = null;
		}
		b[0].setForce(0.0f, 0.0f);
		b[0].setRotation(0);
		activated = false;
	}

	/**
	 * Set the position of the TennisBall.
	 *
	 * @param f  The x,y coordinates of the top left 
	 *           x-y coordinates of the Widget within the container.
	 */
	public void setPosition(Vector2f f)
	{
		b[0].setPosition(f.getX() + RADIUS, f.getY() + RADIUS);
		position[0].set((ROVector2f) f);
		position[1].set(f.getX() + RADIUS * 2, f.getY());
		position[2].set(f.getX() + RADIUS * 2, f.getY() + RADIUS * 2);
		position[3].set(f.getX(), f.getY() + RADIUS * 2);
	}

	/**
	 * Set the top-left x-coordinate of the TennisBall.
	 * it is equivalent to setPosition(new Vector2f(x, getPositionY()));
	 *
	 * @param x  The x coordinate within the container.
	 */
	public void setPositionX(float x)
	{
		b[0].setPosition(x + RADIUS, b[0].getPosition().getY());
		position[0].set(x, position[0].getY());
		position[1].set(x + RADIUS * 2, position[1].getY());
		position[2].set(x + RADIUS * 2, position[2].getY());
		position[3].set(x, position[3].getY());
	}

	/**
	 * Set the top-left y-coordinate of the TennisBall.
	 *
	 * @param y  The y coordinate within the container.
	 */
	public void setPositionY(float y)
	{
		b[0].setPosition(b[0].getPosition().getX(), y + RADIUS);
		position[0].set(position[0].getX(), y);
		position[1].set(position[1].getX(), y);
		position[2].set(position[2].getX(), y + RADIUS * 2);
		position[3].set(position[3].getX(), y + RADIUS * 2);

	}

	/**
	 * Get the top-left position of this TennisBall.
	 *
	 * @return   The x,y coordinates within the container.
	 */
	public Vector2f getPosition()
	{
		return position[0];
	}

	/**
	 * Get the top-left x-coordinate of this TennisBall.
	 *
	 * @return   The x coordinate within the container.
	 */
	public float getPositionX()
	{
		return position[0].getX();
	}

	/**
	 * Get the top-left y-coordinate of this TennisBall.
	 *
	 * @return   The y coordinate within the container.
	 */
	public float getPositionY()
	{
		return position[0].getY();
	}

	/**
	 * Draws the widget.
	 * uses an img to draw the widget.
	 * @param g  The graphic to draw onto.
	 */
	public void draw (Graphics2D g)
	{
		if (imgshown != null)
		{
		    /* we read in image properly in constructor, use it */
		    /* rotation transform */
		    
		    g.drawImage(imgshown,
		                new AffineTransformOp(AffineTransform.getRotateInstance(b[0].getRotation(), RADIUS, RADIUS),
				AffineTransformOp.TYPE_BILINEAR),
		                (int)(b[0].getPosition().getX() - RADIUS),
		                (int)(b[0].getPosition().getY() - RADIUS));
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
		initializeBall();
	}

	/**
	 * Rotates the TennisBall clockwise using its middle as the rotation axis 
	 * But it realy doesn't do anything since this is a directionless widget and it does
	 * not rotate
	 *
	 */
	public void rotateClockwise()
	{

	}

	/**
	 * Rotates the TennisBall clockwise using its middle as the rotation axis 
	 * But it realy doesn't do anything since this is a directionless widget and it does
	 * not rotate
	 *
	 */
	public void rotateCounterClockwise ()
	{

	}

	/**
	 * sets the direction of the TennisBall widget
	 * but it really doesn't do anything since the tenniball is a directionless widget
	 *
	 * @param d Direction to rotate the Widget
	 */
	public void setDirection(Direction d)
	{

	}

	/**
	 * Returns the Direction where the TennisBall is facing
	 * This will always return Direction.NORTH since this is a directionless widget
	 *
	 * @return the direction the TennisBall is facing
	 */
	public Direction getDirection ()
	{
		return dir;
	}

	/**
	 * Determines if the TennisBall is active.
	 *
	 * @return whether or not the widget is active.
	 */
	public boolean isActive()
	{
		return activated;
	}

	/**
	 * Gets the name of the widget. Which is "Tennis Ball"
	 *
	 * @return The name of this widget.
	 */
	public String getName ()
	{
		return "Tennis Ball";
	}

	/**
	 * Gets the type of the TennisBall
	 * Type of TennisBall is ActionType.BOUNCE.
	 *
	 * @return always returns to be of ActionType.BOUNCE since this is the category
	 *         the TennisBall
	 */
	public ActionType getType()
	{
		return ActionType.BOUNCE;
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
	 * Tell if the TennisBall can connect to joints or not.
	 *
	 * @return always returns false since this TennisBall is not connectable
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
	 * Whether or not the TennisBall is locked and cannot be moved.
	 *
	 * @return If the widget is locked (true) or not (false).
	 */
	public boolean isLocked()
	{
		return locked;
	}

	/**
	 * Sets the lock state of the TennisBall.
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
	 * @return returns null since this TennisBall does not have joints
	 */
	public Vector2f attachJoint (Vector2f point)
	{
		return null;
	}

	/**
	 * This will return the body of what the TennisBall uses
	 *
	 * @return An array of bodies.
	 */
	public Body[] getBodiesForSimulation ()
	{
		return b;
	}

	/**
	 * returns nothing since this TennisBall does not have any joints
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
	 * Gets the four boundary vertices of the TennisBall,
	 *
	 * @return An array of Vector2f which will be the four corners x,y coordinates.
	 */
	public Vector2f[] getBoundary()
	{
		return position;
	}

	/**
	 * Retrieves the description of the widget.
	 *
	 * @return The small blurb describing what the widget does.
	 */
	public String getDescription ()
	{
		return "This tennis ball has low mass and can be pretty bouncy";
	}

	/**
	 * This returns the image that i made, which is 20x20
	 * This is an image of a tennis ball (or looks like one anyway)
	 *
	 * @return The image which is linked with the widget.
	 */
	public ImageIcon getIcon ()
	{
		return imgicon;
	}

}
