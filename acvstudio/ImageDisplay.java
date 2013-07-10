/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ImageDisplay.java
 *
 * Created on 26.11.2010, 15:07:08
 */

package acvstudio;

import acvstudio.elements.Image;
import acvstudio.elements.ImageProperties;
import acvstudio.elements.enums.ReadDirection;
import acvstudio.elements.enums.ScaleMode;
import acvstudio.elements.enums.Transition;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.Timer;

/**
 *
 * @author Sinnister
 */
public class ImageDisplay extends javax.swing.JPanel implements ActionListener {

	private BufferedImage image;		 //Current or final image currently projected onto the screen
	protected BufferedImage imageScreen; //Image of whole screen used for cropping operation while making animations
	protected ImageProperties properties;

	

	private int vibration = 0;
	private boolean smoothTransition = false;
	private float transition = 0;	
	private float transitionDuration = 1.5f;

	private float zoom = 1.0f;


	private Transition pastTransition = Transition.NONE;
	private double[] pastArea;

	private final float transitionFrequency = 0.05f;
	private final int startX = 0;
	private final int startY = 1;
	private final int endX = 2;
	private final int endY = 3;

	private Timer timer;


    /** Creates new form ImageDisplay */
    public ImageDisplay()
	{
        initComponents();
    }
	
	public void loadImage(Image img, boolean backwards)
	{
		if (img == null) 
		{
			image = null;
			imageScreen = null;
			updateUI();
			return;
		}

		transition = 0;
		smoothTransition = false;
		timer = new Timer((int) (transitionFrequency*1000), this);		
		

		if(img.getProperties().getTransitionDuration() > 0 || (backwards && properties.getTransitionDuration() > 0 && imageScreen.equals(img.getImage())))
		{
			if (properties != null)
			{
				smoothTransition = true;
				if (backwards && properties.getTransitionDuration() > 0)
					transition = transitionDuration = properties.getTransitionDuration() + 0.2f;
				else
					transition = transitionDuration = img.getProperties().getTransitionDuration() + 0.2f;
				double[] nextArea = properties.getRelativeArea();
				pastArea = new double[4];
				pastArea[startX] = nextArea[startX];
				pastArea[startY] = nextArea[startY];
				pastArea[endX]   = nextArea[endX];
				pastArea[endY]   = nextArea[endY];
				img.getProperties().getProperties().setTransition(Transition.NONE);
			}
			else
			{
				pastArea = new double[] {0,0,1,1};
			}

		}


		imageScreen = img.getImage();

		//Change of transitions when backward step
		Transition tr = Transition.NONE;
		if (properties != null) tr = img.getProperties().getProperties().getTransition();
		if (backwards)
			img.getProperties().getProperties().setTransition(pastTransition);
		if (tr == Transition.PUSHDOWN)   pastTransition = Transition.PUSHUP;
		else if(tr == Transition.PUSHUP) pastTransition = Transition.PUSHDOWN;
		else pastTransition = tr;

		//Update properties
		properties = img.getProperties();

		//Change push directions if return to previous frame
		if (backwards) properties.changeReadDirection();

		//Setting duration for transitions
		if (properties.getProperties().getTransition() != Transition.NONE)
		{
			if (properties.getProperties().getTransition() == Transition.FADE)
				transition = transitionDuration = 1.5f;
			else
				transition = transitionDuration = 0.4f;			
		}

		//Cropping image of a frame when there is no transition
		if (properties.getProperties().getTransition() != Transition.NONE || transition == 0)
		{
			int siWidth = imageScreen.getWidth();
			int siHeight = imageScreen.getHeight();
			double[] relativeArea = properties.getRelativeArea();

			int fiStartX = (int) (siWidth  * relativeArea[0]);
			int fiEndX   = fiStartX + (int) (siWidth  * relativeArea[2]);
			int fiStartY = (int) (siHeight * relativeArea[1]);
			int fiEndY   = fiStartY + (int) (siHeight * relativeArea[3]);

			BufferedImage frameImage = new BufferedImage(fiEndX - fiStartX, fiEndY - fiStartY, BufferedImage.TYPE_INT_RGB);
			Graphics gr = frameImage.getGraphics();
			gr.drawImage(imageScreen, 0, 0, fiEndX - fiStartX, fiEndY - fiStartY, fiStartX, fiStartY, fiEndX, fiEndY, null);
			gr.dispose();

			image = frameImage;
			//imageScreen = null; //No need for image of screen anymore
		}

		//Setting vibration flag (5 phases)
		if (properties.getProperties().getVibration())
			vibration = 5;

		timer.start();
		this.updateUI();
	}

	public void dispose()
	{
		timer.stop();
		timer = null;
		image.flush();
		imageScreen.flush();
		image = null;
		imageScreen = null;
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        transition -= transitionFrequency;
		if (transition <= 0) {
            transition = 0;
			if (timer != null)
			{
				timer.stop();
				timer = null;
			}
        }
        repaint();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setName("Form"); // NOI18N
        setRequestFocusEnabled(false);
        setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 465, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 357, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void paintComponent(Graphics g)
	{
		drawComic(g, true);
	}
	protected void drawComic(Graphics g, boolean center)
	{
		//Image needs to be loaded first
		if (image != null) 
		{

			//Enviroment variables
			int imageWidth = image.getWidth();
			int imageHeight = image.getHeight();
			int windowWidth = getWidth();
			int windowHeight = getHeight();
			double imageRatioAspect = (double) imageWidth / (double) imageHeight;

			//Images
			Graphics2D bg = (Graphics2D) g;
			BufferedImage sceneImage = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics2D img = sceneImage.createGraphics();

			//Drawing background
			img.setColor(properties.getProperties().getBackgroundColor());
			img.fillRect(0, 0, windowWidth, windowHeight);

			//Transition - Move and FADE
			if(properties.getProperties().getTransition() == Transition.FADE)
			{
				bg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (1-transition/transitionDuration)));
			}
			else if(properties.getProperties().getTransition() == Transition.NONE && smoothTransition)
			{
				double[] targetArea = properties.getRelativeArea();

				int pastWidth  = (int) (imageScreen.getWidth()*pastArea[endX]);
				int pastHeight = (int) (imageScreen.getHeight()*pastArea[endY]);
				int targetWidth  = (int) (imageScreen.getWidth()*targetArea[endX]);
				int targetHeight = (int) (imageScreen.getHeight()*targetArea[endY]);
				int pastX = (int) (imageScreen.getWidth()*pastArea[startX]);
				int pastY = (int) (imageScreen.getHeight()*pastArea[startY]);
				int targetX = (int) (imageScreen.getWidth()*targetArea[startX]);
				int targetY = (int) (imageScreen.getHeight()*targetArea[startY]);
				
				double transitionRatio = (1-transition/transitionDuration);

				int iStartX = (int) (pastX + (targetX-pastX)*transitionRatio);
				int iWidth  = (int) (pastWidth + (targetWidth-pastWidth)*transitionRatio);
				int iStartY = (int) (pastY + (targetY-pastY)*transitionRatio);
				int iHeight = (int) (pastHeight + (targetHeight-pastHeight)*transitionRatio);

				BufferedImage frameImage = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_INT_RGB);
				Graphics gr = frameImage.getGraphics();
				gr.drawImage(imageScreen, 0, 0, iWidth, iHeight, iStartX, iStartY, iStartX+iWidth, iStartY+iHeight, null);

				imageWidth = iWidth;
				imageHeight = iHeight;
				imageRatioAspect = (double) imageWidth / (double) imageHeight;

				
				image = frameImage;
				gr.dispose();

				if (transition == 0)
				{
					smoothTransition = false;
					//imageScreen = null;
				}
			}



			//Scaling image
			if (properties.getScaleMode() != ScaleMode.NONE)
			{
				if(properties.getScaleMode() == ScaleMode.BEST)
				{
					if (((double) imageHeight/(double) windowHeight) > ((double) imageWidth/(double) windowWidth))
					{
						imageHeight = windowHeight;
						imageWidth = (int) (imageHeight * imageRatioAspect);
					}
					else
					{
						imageWidth = windowWidth;
						imageHeight = (int) (imageWidth / imageRatioAspect);
					}
				}
				else if(properties.getScaleMode() == ScaleMode.WIDTH)
				{
					imageWidth = windowWidth;
					imageHeight = (int) (imageWidth / imageRatioAspect);
				}
				else if(properties.getScaleMode() == ScaleMode.HEIGHT)
				{
					imageHeight = windowHeight;
					imageWidth = (int) (imageHeight * imageRatioAspect);
				}

				img.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
				img.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				img.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			}

			if (zoom != 1f)
			{
				imageHeight *= zoom;
				imageWidth *= zoom;
			}

			//Drawing image
			if (vibration > 0) // Vibration
			{
				if (vibration == 5) img.drawImage(image, (windowWidth-imageWidth)/2 - 30, (windowHeight-imageHeight)/2 - 30, imageWidth, imageHeight, null);
				else if(vibration == 4) img.drawImage(image, (windowWidth - imageWidth) / 2 - 20, (windowHeight - imageHeight) / 2 - 40, imageWidth, imageHeight, null);
				else if(vibration == 3) img.drawImage(image, (windowWidth - imageWidth) / 2 - 40, (windowHeight - imageHeight) / 2 - 20, imageWidth, imageHeight, null);
				else if(vibration == 2) img.drawImage(image, (windowWidth - imageWidth) / 2 - 30, (windowHeight - imageHeight) / 2 - 30, imageWidth, imageHeight, null);
				else if(vibration == 1) img.drawImage(image, (windowWidth - imageWidth) / 2 - 40, (windowHeight - imageHeight) / 2 - 40, imageWidth, imageHeight, null);
				vibration -= 1;
			}
			else if (center) // Drawing without vibration but centered
				img.drawImage(image, (windowWidth - imageWidth) / 2, (windowHeight - imageHeight) / 2, imageWidth, imageHeight, null);
			else  //Drawing not centered
				img.drawImage(image, 0, 0, imageWidth, imageHeight, null);

			//Push transitions
			if(properties.getProperties().getTransition() == Transition.PUSHUP)
			{
				int translY = (int) -(windowHeight*(transitionFrequency/transitionDuration));
				
				if (transition != transitionDuration)
					bg.copyArea(0, 0, windowWidth, (int)(windowHeight*(transition/transitionDuration))-translY, 0, translY);

				bg.drawImage(sceneImage, 0,(int)(windowHeight*(transition/transitionDuration)), null);

			}
			else if(properties.getProperties().getTransition() == Transition.PUSHDOWN)
			{
				int sY = (int) (windowHeight*(1-transition/transitionDuration));
				int translY = (int)(windowHeight*(transitionFrequency/transitionDuration));

				if (transition != transitionDuration)
					bg.copyArea(0, sY-translY, windowWidth, windowHeight-sY, 0, translY);

				bg.drawImage(sceneImage, 0,(int)-(windowHeight*(transition/transitionDuration)), null);
			}
			else if(properties.getProperties().getTransition() == Transition.PUSH)
			{
				int sX = (int) (windowWidth*(1-transition/transitionDuration));
				int	tX = (int) (windowWidth*(transitionFrequency/transitionDuration));

				if (properties.getReadDirection() == ReadDirection.LEFTTORIGHT)
				{

					if (transition != transitionDuration)
						bg.copyArea(0, 0, windowWidth-sX+tX, windowHeight, -tX, 0);

					bg.drawImage(sceneImage, -sX+windowWidth, 0, null);
				}
				else
				{
					if (transition != transitionDuration)
						bg.copyArea(sX-tX, 0, windowWidth, windowHeight, tX, 0);

					bg.drawImage(sceneImage, sX-windowWidth, 0, null);
				}

			}
			else
				bg.drawImage(sceneImage, 0, 0, null);
			


			//Unsetting variables
			img.dispose();
		}
    }

	public void setZoom(float zoom)
	{
		this.zoom = zoom;
		updateUI();
	}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
