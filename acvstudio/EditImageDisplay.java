/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package acvstudio;

import acvstudio.elements.Image;
import acvstudio.elements.ImageProperties;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author Sinnister
 */
public class EditImageDisplay extends ImageDisplay
{
	private int startX = 0;
	private int startY = 0;
	private int endX = 0;
	private int endY = 0;

	private boolean mousePressed = false;

	private int imageWidth = 0;
	private int imageHeight = 0;
	private float zoom;


	public EditImageDisplay()
	{
		super();

		this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		
		addMouseListener( new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				mousePressed = true;
				startX = e.getX();
				startY = e.getY();
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				if (mousePressed)
				{
					endX = e.getX();
					endY = e.getY();
					if (imageWidth < endX) endX = imageWidth;
					if (imageHeight < endY) endY = imageHeight;
					if (startX == endX) endX += 10;
					if (startY == endY) endY += 10;
					mousePressed = false;
					updateUI();
				}
			}


		});

		addMouseMotionListener(new MouseMotionListener()
		{
			public void mouseMoved(MouseEvent e) { }
			public void mouseDragged(MouseEvent e)
			{
					endX = e.getX();
					endY = e.getY();
					if (imageWidth < endX) endX = imageWidth;
					if (imageHeight < endY) endY = imageHeight;
					updateUI();
			}
		});
	}

	@Override
    public void paintComponent(Graphics g)
	{
		if (imageWidth == 0) return;

		drawComic(g, false);

		Graphics2D bg = (Graphics2D) g;
		bg.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 3, 1 }, 0));
		int sX, sY, eX, eY;
		if (startX>endX)
		{
			sX = endX;
			eX = startX;
		}
		else
		{
			sX = startX;
			eX = endX;
		}

		if (startY>endY)
		{
			sY = endY;
			eY = startY;
		}
		else
		{
			sY = startY;
			eY = endY;
		}
		bg.setColor(Color.BLACK);
		bg.drawRect(sX, sY, eX-sX, eY-sY);
		bg.setColor(Color.WHITE);
		bg.drawRect(sX+1, sY+1, eX-sX-2, eY-sY-2);
	}
	
	public void loadImage(Image img, ImageProperties frame)
	{

		
		if (img != null)
		{
			imageWidth = img.getImage().getWidth();
			imageHeight = img.getImage().getHeight();

			double[] relativeArea = frame.getRelativeArea();
			startX = (int) (imageWidth*relativeArea[0]);
			startY = (int) (imageHeight*relativeArea[1]);
			endX = startX + (int) (imageWidth*relativeArea[2]);
			endY = startY + (int) (imageHeight*relativeArea[3]);

			img.getProperties().getProperties().setBackgroundColor(this.getBackground());
		}
		else
		{
			imageWidth = 0;
			imageHeight = 0;
		}
		
		super.loadImage(img, false);

		zoom = 1;
		setZoom(1);
	}

	public double[] getRelativeArea()
	{
		int sX, sY, eX, eY;
		if (startX>endX)
		{
			sX = endX;
			eX = startX;
		}
		else
		{
			sX = startX;
			eX = endX;
		}

		if (startY>endY)
		{
			sY = endY;
			eY = startY;
		}
		else
		{
			sY = startY;
			eY = endY;
		}
		return new double[] {(double)sX/imageWidth, (double)sY/imageHeight, (double)(eX-sX)/imageWidth, (double)(eY-sY)/imageHeight};
	}

	@Override
	public void setZoom(float newZoom)
	{
		float change = newZoom/zoom;
		startX *= change;
		startY *= change;
		endX   *= change;
		endY   *= change;
		imageHeight *= change;
		imageWidth  *= change;

		setPreferredSize(new Dimension(imageWidth,imageHeight));
		setMinimumSize(new Dimension(imageWidth,imageHeight));

		zoom = newZoom;
		super.setZoom(newZoom);
		
	}


}
