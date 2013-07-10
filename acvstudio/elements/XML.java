/*
 * Class for comic element
 */

package acvstudio.elements;

import acvstudio.elements.enums.Orientation;
import acvstudio.elements.enums.ReadDirection;
import acvstudio.elements.enums.ScaleMode;
import acvstudio.elements.enums.Transition;
import acvstudio.elements.exceptions.FrameException;
import java.awt.Color;

import java.util.ArrayList;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



public class XML
{
	
 
	private ScaleMode scaleMode = ScaleMode.BEST;
	private Orientation orientation = Orientation.LANDSCAPE;
	private ReadDirection readDirection = ReadDirection.LEFTTORIGHT;
	private String version = "Not Defined";
	private String minVersion = "Not Defined";
	private boolean paid = false;

	private CommonProperties comicProperties = new CommonProperties();

	private ImageName imageName = new ImageName("screen@index","000",0,999);
	private ImageName thumbnailName = new ImageName("tn_screen@index","000",0,999);

	private TreeMap<Integer,Screen>  screens  = new TreeMap<Integer,Screen>();	
	private ArrayList<Message> messages = new ArrayList<Message>();

	public XML() {}

	public XML(Document doc)
	{
		Element comic = (Element) doc.getElementsByTagName("comic").item(0);

		version = comic.getAttribute("version");
		minVersion = comic.getAttribute("minViewerVersion");

		if(comic.getAttribute("paid").equals("true")) paid = true;
		else                                          paid = false;
		
		if(comic.getAttribute("scaleMode").equals("width"))       scaleMode = ScaleMode.WIDTH;
		else if(comic.getAttribute("scaleMode").equals("height")) scaleMode = ScaleMode.HEIGHT;
		else if(comic.getAttribute("scaleMode").equals("none"))   scaleMode = ScaleMode.NONE;
		else                                                      scaleMode = ScaleMode.BEST;

		if(comic.getAttribute("orientation").equals("portrait")) orientation = Orientation.PORTRAIT;
		if(comic.getAttribute("orientation").equals("1"))        orientation = Orientation.PORTRAIT;
		else                                                     orientation = Orientation.LANDSCAPE;

		if (comic.getAttribute("direction").equals("ltr")) readDirection = ReadDirection.LEFTTORIGHT;
		else                                               readDirection = ReadDirection.RIGHTTOLEFT;

		//Properties
		comicProperties.parse(comic);

		//All screens
		NodeList listScreens = doc.getElementsByTagName("screen");
		for (int i = 0; i < listScreens.getLength(); i++)
		{
			Element elScreen = (Element) listScreens.item(i);
			screens.put(new Integer(elScreen.getAttribute("index")), new Screen(elScreen));
		}

		NodeList listImages = doc.getElementsByTagName("image");
		if (listScreens.getLength() == 0)
		{
			for (int i = 0; i < listImages.getLength(); i++)
			{
				Element elImage = (Element) listImages.item(i);
				screens.put(new Integer(elImage.getAttribute("index")), new Screen(elImage));
			}

			if (listImages.getLength() > 0 && ((Element)listImages.item(0)).getParentNode().getNodeName().equals("images"))
			{
				correctTransitions();
			}
		}
		else if (listScreens.getLength() > 0 && ((Element)listScreens.item(0)).getParentNode().getNodeName().equals("images"))
			correctTransitions();


		NodeList lImages = doc.getElementsByTagName("images");
		if (lImages.getLength() == 1)
		{
			Element images = (Element) lImages.item(0);
			imageName = new ImageName(images.getAttribute("namePattern"),
									  images.getAttribute("indexPattern"),									  
									  new Integer(images.getAttribute("startAt")),
									  new Integer(images.getAttribute("length")));
		}

		NodeList lThumbnails = doc.getElementsByTagName("thumbnails");
		if (lThumbnails.getLength() == 1)
		{
			Element images = (Element) lThumbnails.item(0);
			thumbnailName = new ImageName(images.getAttribute("namePattern"),
										  images.getAttribute("indexPattern"),
										  new Integer(images.getAttribute("startAt")),
										  new Integer(images.getAttribute("length")));
		}
	}

	public XML(XML xml)
	{
		this.comicProperties = new CommonProperties(xml.comicProperties);
		this.imageName = new ImageName(xml.imageName.getNamePattern(), xml.imageName.getIndexPattern(),
				xml.imageName.getStartAt(), xml.imageName.getLength());
		this.messages = new ArrayList<Message>(xml.messages);
		this.minVersion = xml.minVersion;
		this.orientation = xml.orientation;
		this.paid = xml.paid;
		this.readDirection = xml.readDirection;
		this.scaleMode = xml.scaleMode;
		this.screens = new TreeMap<Integer, Screen>(xml.screens);
		this.thumbnailName = new ImageName(xml.thumbnailName.getNamePattern(), xml.thumbnailName.getIndexPattern(),
				xml.thumbnailName.getStartAt(), xml.thumbnailName.getLength());
		this.version = xml.version;
	}
	private void correctTransitions()
	{
		TreeMap<Integer,Screen> newScreens = new TreeMap<Integer,Screen>(screens);
		for(int index: screens.descendingKeySet())
		{
			Transition tr = screens.get(index).getProperties().getTransition();
			if (screens.containsKey(index+1)) newScreens.get(index+1).getProperties().setTransition(tr);
			else
			{
				newScreens.put(index+1, new Screen());
				newScreens.get(index+1).getProperties().setTransition(tr);
			}
			newScreens.get(index).getProperties().setTransition(Transition.NONE);
		}
		screens = newScreens;
	}

	public ImageProperties getScreen(int no)
	{
		CommonProperties prop;

		if (screens.containsKey(no)) 
			prop = comicProperties.evaluate(screens.get(no).getProperties());
		else // When screen element for given index is not found
			prop = new CommonProperties(comicProperties);
		
		String in = imageName.getName(no);
		
		return new ImageProperties(scaleMode, orientation, readDirection, version, prop, in); 
	}

	public String getImageName(int no)
	{
		return imageName.getName(no);
	}

	public ImageProperties getFrame(int screenNo, int frameNo)
	{
		CommonProperties prop;
		Screen selectedScreen;
		Frame selectedFrame;

		String in = imageName.getName(screenNo);

		if (screens.containsKey(screenNo))
		{
			selectedScreen = screens.get(screenNo);
			try
			{
				selectedFrame = selectedScreen.getFrame(frameNo);

				prop = comicProperties.evaluate(selectedScreen.getProperties(),selectedFrame.getProperties());

				return new ImageProperties(scaleMode, orientation, readDirection, version, prop, in,
				selectedFrame.getTransitionDuration(), selectedFrame.getAutoplay(),
				selectedFrame.getDurationAutoplay(), selectedFrame.getRelativeArea());
			}
			catch (FrameException e)
			{
				prop = comicProperties.evaluate(selectedScreen.getProperties());
				return new ImageProperties(scaleMode, orientation, readDirection, version, prop, in);
			}
		}
		else // When screen element for given index is not found
			return new ImageProperties(scaleMode, orientation, readDirection, version, comicProperties, in);

	}

	public ImageProperties getComicProperties()
	{
		return new ImageProperties(scaleMode, orientation, readDirection, version, comicProperties, null);
	}

	public ImageProperties getScreenProperties(int no) throws FrameException
	{
		if (!screens.containsKey(no)) throw new FrameException("Screen not found getScreenProperties@XML");

		return new ImageProperties(null, null, null, null, screens.get(no).getProperties(), null);
	}

	public ImageProperties getFrameProperties(int screenNo, int frameNo) throws FrameException
	{
		if (!screens.containsKey(screenNo)) throw new FrameException("Screen not found getFrameProperties@XML");

		Frame selectedFrame = screens.get(screenNo).getFrame(frameNo);

		return new ImageProperties(null, null, null, null, selectedFrame.getProperties(), null,
		selectedFrame.getTransitionDuration(), selectedFrame.getAutoplay(),
		selectedFrame.getDurationAutoplay(), selectedFrame.getRelativeArea());
		
	}

	public int getSizeOfScreen(int no)
	{
		if (screens.containsKey(no))
		{
			return screens.get(no).getNumberOfFrames();
		}
		else return 0;
	}

	public Document createXML()
	{
		Document doc = null;

		try
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.newDocument();

			java.text.DecimalFormat format = new java.text.DecimalFormat("#.##########");
			java.text.DecimalFormatSymbols symbols = new java.text.DecimalFormatSymbols();
			symbols.setDecimalSeparator('.');
			format.setGroupingUsed(false);
			format.setDecimalFormatSymbols(symbols);

			Element root = doc.createElement("comic");

			if (!version.equals("Not Defined"))    root.setAttribute("version", version);
			if (!minVersion.equals("Not Defined")) root.setAttribute("minViewerVersion", minVersion);

			if (paid == false) root.setAttribute("paid", "false");
			else               root.setAttribute("paid", "true");

			root.setAttribute("title", comicProperties.getTitle());

			if (comicProperties.isDefColor()) root.setAttribute("bgcolor", calculateHex(comicProperties.getBackgroundColor()));

			if (scaleMode == ScaleMode.BEST)        root.setAttribute("scaleMode", "best");
			else if (scaleMode == ScaleMode.NONE)   root.setAttribute("scaleMode", "none");
			else if (scaleMode == ScaleMode.HEIGHT) root.setAttribute("scaleMode", "height");
			else if (scaleMode == ScaleMode.WIDTH)  root.setAttribute("scaleMode", "width");

			if (comicProperties.isDefTransition())
			{
				if (comicProperties.getTransition() == Transition.NONE)          root.setAttribute("transition", "none");
				else if (comicProperties.getTransition() == Transition.FADE)     root.setAttribute("transition", "fade");
				else if (comicProperties.getTransition() == Transition.PUSH)     root.setAttribute("transition", "push");
				else if (comicProperties.getTransition() == Transition.PUSHDOWN) root.setAttribute("transition", "pushdown");
				else if (comicProperties.getTransition() == Transition.PUSHUP)   root.setAttribute("transition", "pushup");
			}

			if (orientation == Orientation.LANDSCAPE) root.setAttribute("orientation", "2");
			else root.setAttribute("orientation", "1");

			doc.appendChild(root);

			//Images
			Element images = doc.createElement("images");
			images.setAttribute("namePattern", imageName.getNamePattern());
			images.setAttribute("indexPattern", imageName.getIndexPattern());
			images.setAttribute("startAt", Integer.toString(imageName.getStartAt()));
			images.setAttribute("length", Integer.toString(imageName.getLength()));

			root.appendChild(images);

			//Thumbnails
			Element thumbnails = doc.createElement("thumbnails");
			thumbnails.setAttribute("namePattern", thumbnailName.getNamePattern());
			thumbnails.setAttribute("indexPattern", thumbnailName.getIndexPattern());
			thumbnails.setAttribute("startAt", Integer.toString(thumbnailName.getStartAt()));
			thumbnails.setAttribute("length", Integer.toString(thumbnailName.getLength()));

			root.appendChild(thumbnails);

			//Screens
			
			for(Integer screenIndex : screens.keySet())
			{
				Screen s = screens.get(screenIndex);
				CommonProperties sp = s.getProperties();
				Element screen = doc.createElement("screen");
				screen.setAttribute("index", screenIndex.toString());
				if (sp.isDefTitle()) screen.setAttribute("title", sp.getTitle());
				if (sp.isDefTransition())
				{
					if (sp.getTransition() == Transition.NONE)          screen.setAttribute("transition", "none");
					else if (sp.getTransition() == Transition.FADE)     screen.setAttribute("transition", "fade");
					else if (sp.getTransition() == Transition.PUSH)     screen.setAttribute("transition", "push");
					else if (sp.getTransition() == Transition.PUSHDOWN) screen.setAttribute("transition", "pushdown");
					else if (sp.getTransition() == Transition.PUSHUP)   screen.setAttribute("transition", "pushup");
				}
				if (sp.isDefColor())
				{
					screen.setAttribute("bgcolor", calculateHex(sp.getBackgroundColor()));
				}
				if(sp.isDefVibration())
				{
					if (sp.getVibration() == true) screen.setAttribute("vibrate", "true");
					else                           screen.setAttribute("vibrate", "false");
				}

				
				root.appendChild(screen);

				//Frames
				try
				{
					int frameIndex = 0;
					while (true)
					{
						Frame f = s.getFrame(frameIndex);
						Element frame = doc.createElement("frame");
						CommonProperties fp = f.getProperties();

						if (fp.isDefTitle()) frame.setAttribute("title", fp.getTitle());
						if (fp.isDefTransition())
						{
							if (fp.getTransition() == Transition.NONE)          frame.setAttribute("transition", "none");
							else if (fp.getTransition() == Transition.FADE)     frame.setAttribute("transition", "fade");
							else if (fp.getTransition() == Transition.PUSH)     frame.setAttribute("transition", "push");
							else if (fp.getTransition() == Transition.PUSHDOWN) frame.setAttribute("transition", "pushdown");
							else if (fp.getTransition() == Transition.PUSHUP)   frame.setAttribute("transition", "pushup");
						}
						if (fp.isDefColor())
						{
							frame.setAttribute("bgcolor", calculateHex(fp.getBackgroundColor()));
						}
						if(fp.isDefVibration())
						{
							if (fp.getVibration() == true) frame.setAttribute("vibrate", "true");
							else                           frame.setAttribute("vibrate", "false");
						}

						if (f.getAutoplay()) frame.setAttribute("autoplay", "true");
						if (f.getDurationAutoplay() > 0) frame.setAttribute("duration", Float.toString(f.getDurationAutoplay()));
						if (f.getTransitionDuration() > 0) frame.setAttribute("transitionDuration", Float.toString(f.getTransitionDuration()));

						double[] rA = f.getRelativeArea();


						frame.setAttribute("relativeArea", format.format(rA[0]) + " " + format.format(rA[1]) + " " + format.format(rA[2]) + " " + format.format(rA[3]));


						screen.appendChild(frame);
						frameIndex++;
					}
				}
				catch (FrameException e) {}

				screenIndex++;
			}
		}
		catch (ParserConfigurationException e)
		{
			System.err.println("Unexpected exception createXML@XML.java");
		}

		return doc;
	}


	public void changeComic(ImageProperties ip)
	{
		comicProperties.modify(ip.getProperties());
		scaleMode = ip.getScaleMode();
		orientation = ip.getOrientation();
		readDirection = ip.getReadDirection();
		version = ip.getVersion();

	}


	public void changeScreen(ImageProperties ip, int screenNo)
	{

		if (!screens.containsKey(screenNo))/* && (ip.getProperties().isDefTitle() || ip.getProperties().isDefColor() ||
				ip.getProperties().isDefTransition() || ip.getProperties().isDefVibration()))*/
		{
			screens.put(screenNo, new Screen());
		}

		screens.get(screenNo).getProperties().modify(ip.getProperties());
	}

	public void changeFrame(ImageProperties ip, int screenNo, int frameNo) throws FrameException
	{
		if (!screens.containsKey(screenNo))
		{
			screens.put(screenNo, new Screen());
		}
		
		Frame frame;
		try
		{
			frame = screens.get(screenNo).getFrame(frameNo);
		}
		catch (FrameException e)
		{
			frame = new Frame(0, 0, 1, 1);
			screens.get(screenNo).putNewFrame(frame);
		}

		frame.getProperties().modify(ip.getProperties());
		frame.setTransitionDuration(ip.getTransitionDuration());
		frame.setAutoplay(ip.isAutoplay());
		frame.setDurationAutoplay(ip.getDurationAutoplay());
		frame.setRelativeArea(ip.getRelativeArea());

	}

	public void removeFrame(int screenNo, int frameNo) throws FrameException
	{
		if (!screens.containsKey(screenNo)) throw new FrameException("Screen not found removeFrame@XML");

		screens.get(screenNo).removeFrame(frameNo);
	}

	public void removeScreen(int no)
	{
		
		int screenNo = no;
		while (true)
		{

			if (!screens.containsKey(screenNo+1))
			{
				screens.remove(screenNo);
				break;
			}

			screens.put(screenNo, screens.get(screenNo+1));

			screenNo += 1;
		}
	}

	public void addScreen(int afterNo, Screen screen)
	{

		if (afterNo == -1)
		{
			screens.put(0, screen);
		}
		else
		{
			for (int screenNo = screens.lastKey(); screenNo > afterNo; screenNo--)
			{
				if (screenNo > afterNo && screens.containsKey(screenNo)) screens.put(screenNo+1, screens.get(screenNo));
			}

			screens.put(afterNo+1, screen);
		}
	}

	public void addFrame(int screenNo, int frameNo, Frame frame)
	{
		if (!screens.containsKey(screenNo))
		{
			screens.put(screenNo, new Screen());
		}
		
		screens.get(screenNo).addFrame(frameNo+1, frame);
	}

	public void exchangeScreens(int first, int second)
	{
		boolean hasFirst = screens.containsKey(first);
		boolean hasSecond = screens.containsKey(second);

		Screen f = null;
		Screen s = null;
		if (hasFirst) f = screens.get(first);
		if (hasSecond) s = screens.get(second);

		if (hasSecond) screens.put(first, s);
		else screens.put(first, new Screen());
		if (hasFirst) screens.put(second, f);
		else screens.put(second, new Screen());

	}
/*
	public void switchFrames(int screenNo1, int frameNo1, int screenNo2, int frameNo2) throws FrameException
	{
		Frame f1 = screens.get(screenNo1).getFrame(frameNo1);
		Frame f2 = screens.get(screenNo2).getFrame(frameNo2);

		screens.get(screenNo1).putFrame(frameNo1, f2);
		screens.get(screenNo2).putFrame(frameNo2, f1);
	}*/

	private String calculateHex(Color c)
	{
		String hex = "000000" + Integer.toHexString( c.getRGB() & 0x00ffffff );
		return "#" + hex.substring(hex.length()-6);
	}
}