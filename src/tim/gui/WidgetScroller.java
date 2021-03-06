//
//  WidgetScroller.java
//  
//
//  Created by Jason Cote on 23/09/08.
//  Copyright 2008 Group Four. All rights reserved.
//
package tim.gui;

import Widgets.*;
import Widgets.Widget;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.HashSet;

/**
 Panel responsible for displaying widgets that are available to the user,
 as well as the buttons to scroll through the stored widgets in the panel.
 */
public class WidgetScroller extends JPanel implements ActionListener {

	private ArrayList<WidgetItem> widgets = new ArrayList<WidgetItem>(5);
	private int current_page = 1;
	private int num_pages = 1;
	
	// Visual components
	private JButton left, right;
	private WidgetPane display;
	
	
	/** Default class constructor.
	 */
	public WidgetScroller() {
		super();
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		// Construct the left scroll button
		left = new JButton(new ImageIcon(getClass().getClassLoader().getResource("resources/images/left_button.gif")));
		left.setPreferredSize(new Dimension(26,80));
		left.setMaximumSize(new Dimension(26,80));
		left.addActionListener(this);
		
		// Construct the panel to draw the Widgets
		display = this.new WidgetPane();
		display.setPreferredSize(new Dimension(528,80));
		display.setMaximumSize(new Dimension(528,80));
		display.setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		// Construct the right scroll button
		right = new JButton(new ImageIcon(getClass().getClassLoader().getResource("resources/images/right_button.gif")));
		right.setPreferredSize(new Dimension(26,80));
		right.setMaximumSize(new Dimension(26,80));
		right.addActionListener(this);
		
		// Add each element to this WidgetScroller
		this.add(left);
		this.add(display);
		this.add(right);
	}

	
	/** Add a widget into this WidgetScroller.
	 *  @param type The type or "name" of the Widget to be added.
	 *  @param qty  The quantity of this Widget to add. -1 for unlimited.
	 *  @return true upon success, false is qty is invalid.
	 */
	public boolean addWidget(String type, int qty)
	{
		boolean new_type = true;
		if (qty <= 0 && qty != -1) return false;
		
		/* if a widget of this type is already stored in the scroller,
		   we just have to increase the quantity.  */
		for(WidgetItem w : widgets)
		{
			if (w.name.equals(type))
			{
				if (qty == -1)
					w.qty = -1;
				else if (w.qty != -1)
					w.qty += qty;
				new_type = false;
				break;
			}
		}
		
		/* otherwise, we're creating a new WidgetItem for our list  */
		if (new_type)
		{
			WidgetItem w = new WidgetItem(type, qty);
			this.widgets.add(w);
		
			this.updatePageCount();
		}
		this.display.repaint();
		return true;
	}
	
	/** Events handled here are page left and page right buttons being
	 *  pressed.
	 */
    public void actionPerformed(ActionEvent event) {
            if(event.getSource() == left)
                this.prevPage();
            else if(event.getSource() == right)
                this.nextPage();
            this.display.repaint();
    }
	
	/** Get the current number of a given widget type that are
	 *  stored in the scroller.
	 *  @param type The type of widget of interest.
	 *  @return int the number of widgets currently stored of this type.
	 */
	public int getWidgetCount(String type)
	{
		/* search this list if we're storing this type of widget */
		for(WidgetItem w : widgets)
		{
			if( w.name.equals(type) )
				return w.qty; /* return it's quantity */
		}
		return 0; /* if not, return 0 (that's how many we have :/) */
	}
	
	
	/** Empty the widget scroller.
	 */
	public void clearWidgets() {
		while( false != removeWidget( 1, 1 ));
		
		this.num_pages = 1;
		this.current_page = 1;
		this.display.repaint();
	}
	
	
	/** Get the i'th widget of the page'th page.
	 *  Pages are numbered starting from 1, and each page may store up to
	 *  five widgets, numbered 1 to 5.
	 *  @param page The page index that has the desired widget.
	 *  @param i The position of the widget on the page (1<=i<=5).
	 *  @return The Widget that was requested, or null.
	 */
	private String getWidget(int page, int i)
	{
		int index = (page-1)*5 + i-1;
		
		if (index >= 0 && index <= this.widgets.size()-1)
			return this.widgets.get(index).name;
		else
			return null;
	}
	
	
	/** Get the widget "name" currently displayed in the x, y coordinates
	 *  of the scroller.
	 *  @param x the x co-ordinate value.
	 *  @param y the y co-ordinate value.
	 *  @return the widget's String name.
	 */ 
	public String getWidgetAt(float x, float y)
	{
		if (y >= 15) {
			if (x >= 5 && x < 105)
				return getWidget(getPage(), 1);
			else if (x >= 105 && x < 205)
				return getWidget(getPage(), 2);
			else if (x >= 205 && x < 305)
				return getWidget(getPage(), 3);
			else if (x >= 305 && x < 405)
				return getWidget(getPage(), 4);
			else if (x >= 405 && x < 505)
				return getWidget(getPage(), 5);
			else
				return null;
		}
		else
			return null;
	}
	
	
	/** Decrease the quantity stored for the specified widget type.
	 *  Note: this method will handle removing the item once the quantity
	 *  decreases down to zero. Unlimited quantities are not decreased.
	 *  @param type The widget "name" or type to decrease.
	 *  @return true on success, false otherwise.
	 */	
	public boolean decreaseWidget(String type)
	{	
		boolean ret = false;
		/* search if we have a widget of this type stored.  */
		for(int i=0; i<widgets.size(); i++)
		{
            WidgetItem w = widgets.get(i);
			if (w.name.equals(type))
			{
				if (w.qty == -1)  /* unlimited qty */
					return true;  /* return now, ie. do nothing */
				w.qty -= 1;
				//System.out.println("widget \""+w.name+"\" now at: "+w.qty);
				if (w.qty == 0)
					widgets.remove(i);
				ret = true;
			}
		}
		this.updatePageCount();
		
		/* otherwise not stored, failed!  */
		this.display.repaint();
		return ret;
	}

    
	
	/** Remove the i'th widget of the page'th page.
	 *  Pages are numbered starting from 1, and each page may store up to
	 *  five widgets, numbered 1 to 5.
	 *  @param page The page index that has the desired widget.
	 *  @param i The position of the widget on the page (1<=i<=5).
	 *  @return true on success, false otherwise.
	 */
	private boolean removeWidget(int page, int i) {
		int index = (page-1)*5 + i-1;
		
		if (index >= 0 && index <= this.widgets.size()-1)
		{
			this.widgets.remove(index);
			this.updatePageCount();
			
			this.display.repaint();
			return true;
		}
		else
			return false;
	}
	
	
	/** Get the number of pages current utilized by this scroller
	 *  @return int number of pages.
	 */
	public int getNumPages() {
		return num_pages;
	}
	
	
	/** Get the number of widgets types stored in the scroller
	 *  @return int number of widgets of different types.
	 */
	public int getNumWidgets() {
		return widgets.size();
	}
	
	
	/** Get the current page the scroller is positioned on.
	 *  @return int the current page number.
	 */
	public int getPage()
	{
		return this.current_page;
	}
	
	
	/** Step to the next page of the Scroller.
	 *  @return true if there is a next page, false if not.
	 */
	public boolean nextPage() {
		if (current_page+1 > num_pages) return false;
		
		current_page++;
		this.display.repaint();
		return true;
	}
	
	
	/** Step to the previous page of the Scroller.
	 *  @return true if there is a previous page, false if not.
	 */
	public boolean prevPage() {
		if (current_page-1 < 1) return false;
		
		current_page--;
		this.display.repaint();
		return true;
	}
	
	
	/** Updates the page count after widgets have been
	    removed or added. */
	private void updatePageCount() {
		this.num_pages = (int)(this.widgets.size() / 5);
		if (this.widgets.size() % 5 > 0) this.num_pages++;
		if (this.num_pages == 0) num_pages = 1;
	}
	
	
	/** A simple subclass of JPanel used to display the icons
	 *  for the widgets stored within the WidgetScroller.
	 */
	private class WidgetPane extends JPanel {
		private int width;
		private int height;
		
		public WidgetPane() {
			super();
		}
		
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D)g;
			height = super.getHeight();
			width = super.getWidth();
			
			// paint the background
			g2.setColor(Color.gray);
			g2.fill( new Rectangle2D.Double( 0, 0, width, height ) );
		
			// draw each widget, and indicate quantity, pages, etc.
			for (int i=0; i<5; i++)
			{
				String type = getWidget( getPage(), i+1 );
				
				if (type != null) 
				{
					ImageIcon w_img = WidgetFactory.getIcon(type);
					if (w_img != null) w_img.paintIcon( this, g, i*100+15, 15 );
					//else paint default missing img??
					
					g2.setColor(Color.yellow);
					int cur_qty;
					cur_qty = getWidgetCount(type);
					if (cur_qty == -1)
						g2.drawString( "99", i*100+28, 70 );
					else
						g2.drawString( ""+cur_qty, i*100+48, 70 );
				}
				else {
					//TODO: remove this println statment
					//System.out.println("no widget icon at p."+getPage() +" i." +(i+1) +" :(");
					break;
				}
				
			}			
			g2.setColor(Color.black);
			g2.drawString( widgets.size() +" widgets stored.", 4, 12);
			g2.drawString( "Page "+ getPage() +"/"+ getNumPages(), 456, 12 );
		}
	}
	
	private class WidgetItem {
		public String  name;
		public int     qty;
		
		public WidgetItem(String name, int qty)
		{
			this.name = name;
			this.qty = qty;
		}
	}
	
}
