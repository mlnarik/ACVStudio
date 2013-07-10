/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package acvstudio.elements;

import acvstudio.elements.enums.Orientation;
import acvstudio.elements.enums.ReadDirection;
import acvstudio.elements.enums.ScaleMode;
import acvstudio.elements.enums.Transition;

/**
 *
 * @author Sinnister
 */
public class ImageProperties
{
	private ScaleMode scaleMode;
	private Orientation orientation;
	private ReadDirection readDirection;
	private String version;

	private float transitionDuration = 0;
	private boolean autoplay = false;
	private float durationAutoplay = 0;

	private double[] relativeArea = new double[4];

	private CommonProperties properties = new CommonProperties();

	private String imageName;

	public ImageProperties()
	{
		double[] rA = new double[] {0,0,1,1};
		save(null, null, null, null, new CommonProperties(), null, 0, false, 0, rA);
	}

	public ImageProperties(String in)
	{
		double[] rA = new double[] {0,0,1,1};
		save(ScaleMode.NONE, Orientation.LANDSCAPE, ReadDirection.LEFTTORIGHT, "2", new CommonProperties(), in, 0, false, 0, rA);
	}

	public ImageProperties(ScaleMode sm, Orientation o, ReadDirection rd, String v,
			CommonProperties ip, String in)
	{
		double[] rA = new double[] {0,0,1,1};
		save(sm, o, rd, v, ip, in, 0, false, 0, rA);
	}

	public ImageProperties(ScaleMode sm, Orientation o, ReadDirection rd, String v,
			CommonProperties ip, String in, float td, boolean ap, float da, double[] rA)
	{
		save(sm, o, rd, v, ip, in, td, ap, da, rA);
	}

	private void save(ScaleMode sm, Orientation o, ReadDirection rd, String v,
			CommonProperties ip, String in, float td, boolean ap, float da, double[] rA)
	{
		scaleMode = sm;
		orientation = o;
		readDirection = rd;
		version = v;

		properties = new CommonProperties(ip);

		transitionDuration = td;
		autoplay = ap;
		durationAutoplay = da;

		imageName = in;

		if(rA != null) relativeArea = rA;

	}

	public void removeEffects()
	{
		transitionDuration = 0;
		autoplay = false;
		durationAutoplay = 0;
		properties.setTransition(Transition.NONE);
		properties.setVibration(false);
		scaleMode = ScaleMode.BEST;
	}

	public void removeScaling()
	{
		scaleMode = ScaleMode.NONE;
	}

	public void changeReadDirection ()
	{
		if (readDirection == ReadDirection.LEFTTORIGHT) readDirection = ReadDirection.RIGHTTOLEFT;
		else readDirection = ReadDirection.LEFTTORIGHT;
	}

	public String getImageName() {
		return imageName;
	}

	public boolean isAutoplay() {
		return autoplay;
	}

	public float getDurationAutoplay() {
		return durationAutoplay;
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public CommonProperties getProperties() {
		return properties;
	}

	public ReadDirection getReadDirection() {
		return readDirection;
	}

	public ScaleMode getScaleMode() {
		return scaleMode;
	}

	public float getTransitionDuration() {
		return transitionDuration;
	}

	public String getVersion() {
		return version;
	}

	public double[] getRelativeArea() {
		return relativeArea;
	}



}
