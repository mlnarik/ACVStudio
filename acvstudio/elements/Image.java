/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package acvstudio.elements;

import java.awt.image.BufferedImage;

/**
 *
 * @author Sinnister
 */
public class Image
{
	private BufferedImage image;
	private ImageProperties properties;

	public Image(BufferedImage img, ImageProperties prop)
	{
		image = img;
		properties = prop;
	}

	public BufferedImage getImage() {
		return image;
	}

	public ImageProperties getProperties() {
		return properties;
	}

	
}
