
package acvstudio.elements;

import org.w3c.dom.Element;


public class Frame
{
	private double[] relativeArea = new double[4];
	private float transitionDuration = 0;
	private boolean autoplay = false;
	private float durationAutoplay = 0;
	
	private final int startX = 0;
	private final int startY = 1;
	private final int endX = 2;
	private final int endY = 3;

	private CommonProperties frameProperties = new CommonProperties();

	public Frame(Element frame)
	{
		
		String[] stringData = (frame.getAttribute("relativeArea").split(" ",4));
		for (int i=0; i<4; i++)
			relativeArea[i] = new Double(stringData[i]);

		if(frame.getAttribute("autoplay").equals("true"))       autoplay = true;
		if(!frame.getAttribute("duration").isEmpty())           durationAutoplay = new Float(frame.getAttribute("duration"));
		if(!frame.getAttribute("transitionDuration").isEmpty()) transitionDuration = new Float(frame.getAttribute("transitionDuration"));

		frameProperties.parse(frame);
		
	}

	public Frame(double sX, double sY, double eX, double eY)
	{
		relativeArea[startX] = sX;
		relativeArea[startY] = sY;
		relativeArea[endX]   = eX;
		relativeArea[endY]   = eY;
	}

	public boolean getAutoplay() {
		return autoplay;
	}

	public void setAutoplay(boolean autoplay) {
		this.autoplay = autoplay;
	}

	public float getDurationAutoplay() {
		return durationAutoplay;
	}

	public void setDurationAutoplay(float durationAutoplay) {
		this.durationAutoplay = durationAutoplay;
	}

	public float getTransitionDuration() {
		return transitionDuration;
	}

	public void setTransitionDuration(float transitionDuration) {
		this.transitionDuration = transitionDuration;
	}

	public CommonProperties getProperties() {
		return frameProperties;
	}

	public double[] getRelativeArea() {
		return relativeArea;
	}

	public void setRelativeArea(double[] relativeArea) {
		this.relativeArea = relativeArea;
	}

	
}
