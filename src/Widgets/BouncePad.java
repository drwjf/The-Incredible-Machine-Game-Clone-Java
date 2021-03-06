/* Jason Cote
 * 0568943
 *
 * CIS*3750 Assignment #2
 * Class: BouncePad
 */
package Widgets;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.ImageIcon;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

import net.phys2d.raw.*;
import net.phys2d.raw.shapes.*;
import net.phys2d.math.*;
import net.phys2d.raw.*;

/** WidgetX is a simple widget class that consists of a single Box body.
 *  Upon collision the colour of the Box will change, however the widget
 *  must be in an active state in order to do so.  This can be achieved
 *  by calling activateWidget().
 *
 *  The Phys2D javadocs by Kevin Glass were used frequently to ensure
 *  this widget is designed to be compatible with the Phys2D engine.
 *  Other citations can be found in the description for draw() and 
 *  getBoundary().
 */
public class BouncePad implements Widget
{
	
	private String       widget_name;
	private String       widget_desc;
	private ActionType   widget_type;
	private boolean      widget_lock;
	private boolean      widget_active;
	private Direction    widget_dir;
	private BufferedImage img;
	
	/* Bodies and joints that form this widget */
	private Body         legl, legr;
	private Body         pad;
	private FixedJoint   lj, rj;
	
	/* Position variables and sizefor the bodies */
	private float        topleft_x;
	private float        topleft_y;
	private float        ll_x, ll_y;
	private float        pad_x, pad_y;
	private float        rl_x, rl_y;
	private float        width, height;
	
	private float        bounce_force = -123f;
	
	/** Create a new instance of WidgetX with a name "new_WidgetX"
	 */
	public BouncePad()
	{
		this("BouncePad");
	}
	
	/** Create a new instance of WidgetX providing it with a name.
	 */
	public BouncePad(String name)
	{
		/* initialize properties */
		this.widget_name = name;
		this.widget_desc = "A BouncePad widget (bounces objects that land on it).";
		this.widget_type = ActionType.IMPACT;
		this.widget_active = false;
		this.widget_lock = false;
		this.widget_dir = null;
		
		/* setup bodies and body properties */
		ll_x = 3f; ll_y = 28f;
		rl_x = 3f; rl_y = 28f;
		pad_x = 54f; pad_y = 4f;
		width = ll_x + rl_x + pad_x;
		height = ll_y;
		
		legl = new StaticBody( "legl", new Box(ll_x, ll_y) );
		legr = new StaticBody( "legr", new Box(rl_x, rl_y) );		
		pad  = new StaticBody( "pad",  new Box(pad_x, pad_y) );
		
		img = null;
		/* load img for the BouncePad */
		try {
			//img = ImageIO.read(new File("src/Widgets/img/BouncePad.gif")); }
		img = ImageIO.read(getClass().getClassLoader().getResource("resources/img/BouncePad.gif")); 
		}
		catch (Exception e) {
			System.err.println("Error: could not load BouncePad buffered img"); }
			
		/* set intial position to 0,0 */
		setPosition(new Vector2f(0f, 0f));
	}

	/* ------------------------------------------------- */
	
	
	/**
	 * Set the position of the widget.
	 * @param f  The x,y coordinates within the
	 * container.
	 */
	public void setPosition(Vector2f f)
	{
		float x,y;
		
		/* set the Widget's top-left point */
	 	topleft_x = f.getX();
	 	topleft_y = f.getY();
	 	
	 	/* set the center position for drawing the left leg body */
	 	x = topleft_x + (ll_x / 2);
	 	y = topleft_y + (ll_y / 2);
	 	legl.setPosition( x, y ); /* <--- */
	 	
	 	/* set the center position for drawing the pad surface */
	 	x = topleft_x + ll_x + (pad_x/2);
	 	y = topleft_y + 2;
	 	pad.setPosition( x, y ); /* <--- */
	 	
	 	/* set the center position for drawing the right leg body */
	 	x = topleft_x + ll_x + pad_x + (rl_x / 2);
	 	y = topleft_y + (rl_y / 2);
	 	legr.setPosition( x, y ); /* <--- */
	}
	/* ------------------------------------------------- */
	
	
	/**
	 * Set the x-coordinate of the widget.
	 * @param x  The x coordinate within the container.
	 */
	public void setPositionX(float x)
	{	 	
	 	setPosition(new Vector2f(x, topleft_y));
	}
	/* ------------------------------------------------- */
	
	
	/**
	 * Set the y-coordinate of the widget.
	 * @param y  The y coordinate within the container.
	 */
	public void setPositionY(float y)
	{
		setPosition(new Vector2f(topleft_x, y));
	}
	/* ------------------------------------------------- */
	
	
	/**
	 * Get the position of the widget.
	 * @return   The x,y coordinates within the container.
	 */
	public Vector2f getPosition() {
		return( new Vector2f(topleft_x, topleft_y) );
	}
	/* ------------------------------------------------- */
	
	
	/**
	 * Get the x-coordinate of the widget.
	 * @return   The x coordinate within the container.
	 */
	public float getPositionX()
	{
		return topleft_x;
	}
	/* ------------------------------------------------- */
	
	
	/**
	 * Get the x-coordinate of the widget.
	 * @return   The x coordinate within the container.
	 */
	public float getPositionY()
	{
		return topleft_y;
	}
	/* ------------------------------------------------- */
	

	/**
      * Draws the widget.
      * A tutorial by Marty Hall (http://www.apl.jhu.edu/~hall/java/Java2D-Tutorial.html)
      * was used to help me understand Java 2D graphics
      * and using the Graphics2D class to draw/fill shapes.
	 * @param graphics  The Graphics2D object that draws things.
	 */
	public void draw(Graphics2D graphics)
	{
		/*
		Body[] bod = this.getBodiesForSimulation();
		for (int i=0; i<bod.length; i++)
		{
			if (bod[i].getShape() instanceof Box) {
				Vector2f[] pts = ((Box)bod[i].getShape()).getPoints(bod[i].getPosition(), bod[i].getRotation());
				Vector2f v1 = pts[0], v2 = pts[1], v3 = pts[2], v4 = pts[3];
				
				graphics.setColor(Color.black);
          		graphics.draw( new Line2D.Double( (int) v1.x,(int) v1.y,(int) v2.x,(int) v2.y) );
          		graphics.draw( new Line2D.Double( (int) v2.x,(int) v2.y,(int) v3.x,(int) v3.y) );
          		graphics.draw( new Line2D.Double( (int) v3.x,(int) v3.y,(int) v4.x,(int) v4.y) );
          		graphics.draw( new Line2D.Double( (int) v4.x,(int) v4.y,(int) v1.x,(int) v1.y) );
			}
		}*/
		AffineTransform xform = new AffineTransform();

		graphics.drawImage( img, new AffineTransformOp(xform, AffineTransformOp.TYPE_BILINEAR),
		             (int)this.getPosition().getX(), (int)this.getPosition().getY()
		           );
		
	}
	/* ------------------------------------------------- */
	

	/**
	 * Activates the widget.
	 */
	public void activateWidget()
	{
		this.widget_active = true;
	}
	/* ------------------------------------------------- */
	
	
	/**
	 * Resets the widget to start state.
	 */
	public void resetWidget()
	{
		/* Do nothing */
	}
	/* ------------------------------------------------- */
	

	/**
	 * Rotates the Widget 90 Degrees in a
	 * clockwise direction.
	 */
	public void rotateClockwise()
	{

	}
	/* ------------------------------------------------- */
	
	
	/**
	 * Rotates the Widget 90 Degrees in a
	 * counter-clockwise direction.
	 */
	public void rotateCounterClockwise()
	{

	}
	/* ------------------------------------------------- */
	

	/**
	 * Manually set the direction of which
	 * the Widget is facing.
	 *
	 * @param d Direction to rotate the Widget
	 */
	public void setDirection(Direction d)
	{
		this.widget_dir = d;
	}
	/* ------------------------------------------------- */
	
	
	/**
	 * Gets the direction in which the Widget is currently facing
	 * @return The direction that the widget is facing.
	 */
	public Direction getDirection()
	{
		return this.widget_dir;
	}
	/* ------------------------------------------------- */
	

	/**
	 * Determines if the widget is active.
	 * @return true is widget is active, false if not.
	 */
	public boolean isActive()
	{
		return this.widget_active;
	}
	/* ------------------------------------------------- */
	

	/**
	 * Sets the name of the widget.
	 * @param name The name being assigned to the widget.
	 */
	public void setName(String name)
	{
		this.widget_name = name;
	}
	/* ------------------------------------------------- */
	

	/**
	 * Gets the name of the widget.
	 * @return The name of the widget.
	 */
	public String getName()
	{
		return this.widget_name;
	}
	/* ------------------------------------------------- */
	

	/**
	 * Gets the type of the widget.
	 * @return The ActionType that the widget is categorized as.
	 */
	public ActionType getType()
	{
		return this.widget_type;
	}
	/* ------------------------------------------------- */
	
	
	/**
	 * When this method is called, the widget will cycle through
	 * to the next colour that it changes to.  The cycle is orange,
	 * blue, green, and then starting over. 
	 * @param e The collision event that was triggered by a collision.
	 */
	public void reactToTouchingBody(CollisionEvent e)
	{
		Body other = null;
		ROVector2f vel = null;
		
		if (e.getBodyA() == pad)
			other = e.getBodyB();
		else if (e.getBodyB() == pad)
			other = e.getBodyA();
		
		if (other != null) {
			vel = other.getVelocity();
			other.addForce( new Vector2f(0f, bounce_force * vel.getY() * other.getMass()) );
		}
	}
	/* ------------------------------------------------- */
	
	
	/**
	 * If this widget can connect to joints or not.
	 * This is always false for this widget.
	 */
	public boolean isConnectable()
	{
		return false;
	}
	/* ------------------------------------------------- */
	

	/**
	 * Sets the points where joints will connect to this widget.
	 * @param points The points within the widget that designate
	 * where joints connect to.
	 * Should not be called for this widget as it is not connectable.
	 */
	public void setConnectionPoints(Vector2f[] points)
	{
	}
	/* ------------------------------------------------- */
	
	
	/**
	 * Whether or not the widget is locked and cannot be moved.
	 * @return If the widget is locked (true) or not (false).
	 */
	public boolean isLocked()
	{
		return this.widget_lock;
	}
	/* ------------------------------------------------- */
	
	
	/**
	 * Sets the lock state of the widget.
	 * @param locked If the widget is to be locked (true) or 
	 * not (false).
	 */
	public void setLock(boolean locked)
	{
		this.widget_lock = locked;
	}
	/* ------------------------------------------------- */
	

	/**
	 * The point that a joint is attempting to connect to the 
	 * widget. What is returned is the closest connection point.
	 * @param point The x,y coordinates where the joint is attempting to
	 * connected to.
	 * @return null - This widget is not connectable.
	 */
	public Vector2f attachJoint(Vector2f point)
	{
		return null;
	}
	/* ------------------------------------------------- */
	

	/**
	 * Returns an array of the Bodies this widget has, for use in Phys2D engine.
	 * @return An array of bodies.
	 */
	public Body[] getBodiesForSimulation()
	{
		Body[] bodies = new Body[3];
		bodies[0] = legl;
		bodies[1] = legr;
		bodies[2] = pad;
		return bodies;
	}
	/* ------------------------------------------------- */
	
	
	/**
	 * Returns an array of the Joints this widget has, for use in Phys2D engine.
	 * @return An empty array of joints, as this widget uses no Joints.
	 */
	public Joint[] getJointsForSimulation()
	{
		Joint[] joints = new Joint[0];
		//joints[0] = lj;
		//joints[1] = rj;
		return joints;
	}
	/* ------------------------------------------------- */
	
	
	/**
	 * This method does nothing for this widget, since it is not connectable.
	 * @param anchor_point The x,y coordinates of the connected Joint.
	 */
	public void receiveImpulse(Vector2f anchor_point)
	{
	}
	/* ------------------------------------------------- */
	
	
	/**
	 * Gets the four boundary vertices of the widget,
	 * which chould be used as a way to detect overlapping widgets.
	 * @return The four corners x,y coordinates.
	 */
	public Vector2f[] getBoundary()
	{
		Vector2f[] bounds = new Vector2f[4];
		bounds[0] = new Vector2f( topleft_x, topleft_y );
		bounds[1] = new Vector2f( topleft_x + width, topleft_y );
		bounds[2] = new Vector2f( topleft_x + width, topleft_y + height );
		bounds[3] = new Vector2f( topleft_x, topleft_y + height );

		/*
		System.out.println( "top left" + bounds[0].getX() + " " + bounds[0].getY() );
          System.out.println( "top right" + bounds[1].getX() + " " + bounds[1].getY() );
          System.out.println( "bottom right" + bounds[2].getX() + " " + bounds[2].getY() );
          System.out.println( "bottom left" + bounds[3].getX() + " " + bounds[3].getY() );
          */
          
		return bounds;
	}
	/* ------------------------------------------------- */
	 
	 
	 /**
	  * Retrieves the description of the widget.
	  * @return String that is the widget description.
	  */
	 public String getDescription()
	 {
	 	return this.widget_desc;
	 }
	 /* ------------------------------------------------- */
	 
	 
	 /**
	 * Retrieves the image associated with the widget.
	 * @return An empty ImageIcon, since this widget is drawn with Graphics2D
	 */
	public ImageIcon getIcon()
	{
		return new ImageIcon(getClass().getClassLoader().getResource("resources/img/BouncePad.gif"));
	}
	/* ------------------------------------------------- */
}
