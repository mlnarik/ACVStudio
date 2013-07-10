/*
 * 
 */

package acvstudio.elements;

import acvstudio.elements.enums.Transition;
import java.awt.Color;
import org.w3c.dom.Element;

public final class CommonProperties
{

	private String title = "";
	private Color backgroundColor = Color.BLACK;
	private Transition transition = Transition.NONE;
	private boolean vibration = false;


	private boolean defTitle = false;
	private boolean defColor = false;
	private boolean defTransition = false;
	private boolean defVibration = false;

	public CommonProperties() {}

	public CommonProperties(CommonProperties prop)
	{
		modify(prop);
		
	}

	private CommonProperties inherit(CommonProperties prop)
	{
	
		CommonProperties result = new CommonProperties(this);

		if (!result.isDefTitle())      result.setTitle(prop.getTitle());
		if (!result.isDefColor())      result.setBackgroundColor(prop.getBackgroundColor());
		if (!result.isDefTransition()) result.setTransition(prop.getTransition());
		if (!result.isDefVibration())  result.setVibration(prop.getVibration());

		return result;
	}

	public CommonProperties evaluate(CommonProperties screenProp)
	{
		return screenProp.inherit(this);
	}

	public CommonProperties evaluate(CommonProperties screenProp, CommonProperties frameProp)
	{
		return frameProp.inherit(screenProp.inherit(this));
	}

	void parse(Element el)
	{
		if (!el.getAttribute("title").equals("")) setTitle(el.getAttribute("title"));

		if(el.getAttribute("transition").equals("none"))          setTransition(Transition.NONE);
		else if(el.getAttribute("transition").equals("fade"))     setTransition(Transition.FADE);
		else if(el.getAttribute("transition").equals("push"))     setTransition(Transition.PUSH);
		else if(el.getAttribute("transition").equals("pushUp"))   setTransition(Transition.PUSHUP);
		else if(el.getAttribute("transition").equals("pushDown")) setTransition(Transition.PUSHDOWN);

		String color = el.getAttribute("bgcolor");
		if (!color.equals(""))
		{
			int color_r = Integer.valueOf(color.substring(1, 3), 16);
			int color_g = Integer.valueOf(color.substring(3, 5), 16);
			int color_b = Integer.valueOf(color.substring(5, 7), 16);
			setBackgroundColor(new Color(color_r, color_g, color_b));
		}
		
		if (el.getAttribute("vibrate").equals("yes"))
			setVibration(true);
		else if (el.getAttribute("vibrate").equals("no")) setVibration(false);
	}

	public void modify(CommonProperties prop)
	{

		title = prop.getTitle(); defTitle = prop.isDefTitle();

		backgroundColor = prop.getBackgroundColor(); defColor = prop.isDefColor();

		transition = prop.getTransition(); defTransition = prop.isDefTransition();

		vibration = prop.getVibration(); defVibration = prop.isDefVibration();
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		defColor = true;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		defTitle = true;
	}

	public Transition getTransition() {
		return transition;
	}

	public void setTransition(Transition transition) {
		this.transition = transition;
		defTransition = true;
	}

	public boolean getVibration() {
		return vibration;
	}

	public void setVibration(boolean vibration) {
		this.vibration = vibration;
		defVibration = true;
	}

	public boolean isDefColor() {
		return defColor;
	}

	public boolean isDefTitle() {
		return defTitle;
	}

	public boolean isDefTransition() {
		return defTransition;
	}

	public boolean isDefVibration() {
		return defVibration;
	}




}
