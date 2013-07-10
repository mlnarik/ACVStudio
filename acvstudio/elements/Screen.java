/*
 * 
 */

package acvstudio.elements;

import acvstudio.elements.exceptions.FrameException;
import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Screen
{

	private CommonProperties screenProperties = new CommonProperties();

	private ArrayList<Frame> frames   = new ArrayList<Frame>();

	public Screen()	{}

	public Screen(Element screen)
	{
		screenProperties.parse(screen);

		//All frames
		NodeList listFrames = screen.getElementsByTagName("frame");
		for (int i = 0; i < listFrames.getLength(); i++)
		{
			Element elFrame = (Element) listFrames.item(i);
			frames.add(new Frame(elFrame));
		}
	}

	public CommonProperties getProperties()
	{
		return screenProperties;
	}

	public Frame getFrame(int no) throws FrameException
	{
		if (frames.size() <= no) throw new FrameException("Frame not found getFrame@Screen");
		return frames.get(no);
	}

	public int getNumberOfFrames()
	{
		if (frames.size() == 0) return 1;
		return frames.size();
	}

	public void putNewFrame(Frame frame)
	{
		frames.add(frame);
	}

	public void putFrame(int no, Frame frame)
	{
		frames.set(no, frame);
	}

	public void addFrame(int afterNo, Frame frame)
	{
		if (frames.isEmpty() && afterNo == 1) frames.add(new Frame(0, 0, 1, 1));
		frames.add(afterNo, frame);
	}

	public void removeFrame(int no) throws FrameException
	{
		if (frames.size() < no) throw new FrameException("Frame not found removeFrame@Screen");
		if (!frames.isEmpty()) frames.remove(no);

	}
}
