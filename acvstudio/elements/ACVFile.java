/*
 * File ACV
 */

package acvstudio.elements;

import acvstudio.elements.enums.Transition;
import acvstudio.elements.exceptions.FrameException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class ACVFile
{
	private String pathToFile;
	private XML settings;
	private TreeMap<String,BufferedImage> images = new TreeMap<String,BufferedImage>();

	private class FrameAddress
	{
		public int frame;
		public int screen;

		public FrameAddress(int no) throws FrameException
		{
			screen = 0;
			frame = 0;

			if (no < 0) throw new FrameException("Frame does not exist FrameAddress@ACVFile");

			while (no > 0)
			{
				int frames = getSizeOfScreen(screen);
				if (frames == 0) frames = 1;//throw new FrameException("Frame does not exist");
				if (frames > no)
				{
					frame = no;
					no = 0;
				}
				else
				{
					screen += 1;
					no -= frames;
				}
			}
		}
	}

	public ACVFile(File file) throws IOException
	{
		 
		try
		{
			ZipFile acvFile = new ZipFile(file);
			ZipEntry xmlFile = acvFile.getEntry("comic.xml");
			if (xmlFile != null)
			{

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(acvFile.getInputStream(xmlFile));

				//Parse XML document
				settings = new XML(doc);
			}
			else
			{
				settings = new XML();
			}
			
			//All images
			Enumeration entries = acvFile.entries();

			while(entries.hasMoreElements())
			{
				ZipEntry entry = (ZipEntry)entries.nextElement();

				if (entry.getName().equals("comic.xml") || entry.isDirectory()) continue;				
			
				images.put(entry.getName(),ImageIO.read(acvFile.getInputStream(entry)));
			}

			pathToFile = acvFile.getName();
			acvFile.close();

		}
		catch (Exception e)
		{
			System.err.println("Nejde načíst soubor nebo není platný ACV soubor. Chyba: " + e.toString());
			throw new IOException("File is not valid ACV File ACVFile@ACVFile");
		}
	}

	public ACVFile(ACVFile file)
	{
		this.settings = new XML(settings);
		this.images = new TreeMap<String,BufferedImage>(file.images);
	}

	public ACVFile()
	{
		settings = new XML();
	}

	public void save() throws IOException
	{
		if (pathToFile == null)
			throw new IOException("New File");
		else
			save(pathToFile);
	}

	public void save(String path) throws IOException
	{

		Document doc = settings.createXML();


		try
		{
			ZipOutputStream newAcvFile = new ZipOutputStream(new FileOutputStream(path));
			ZipEntry comic = new ZipEntry("comic.xml");
			newAcvFile.putNextEntry(comic);


			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(newAcvFile);
			transformer.transform(source, result);

			for(String imageName : images.keySet())
			{
				ZipEntry image = new ZipEntry(imageName);
				newAcvFile.putNextEntry(image);
				ImageIO.write(images.get(imageName), "jpg", newAcvFile);
			}

			newAcvFile.close();
			pathToFile = path;

		}
		catch (TransformerException e)
		{
			System.err.println("Error save@ACVFile: " + e.toString());
		}
	}

	public Image getScreen(int no) throws FrameException
	{

		FrameAddress fa = new FrameAddress(no);

		ImageProperties ip = settings.getScreen(fa.screen);
		if (! images.containsKey(ip.getImageName())) throw new FrameException("Screen image not found");
		return new Image(images.get(ip.getImageName()), ip);

	}
	public Image getScreenById(int no) throws FrameException
	{

		ImageProperties ip = settings.getScreen(no);
		if (! images.containsKey(ip.getImageName())) throw new FrameException("Screen image not found");
		return new Image(images.get(ip.getImageName()), ip);

	}

	public Image getFrame(int screenNo, int frameNo) throws FrameException
	{

		ImageProperties ip = settings.getFrame(screenNo, frameNo);
		if (frameNo != 0) ip.getProperties().setTransition(Transition.NONE);
		if (! images.containsKey(ip.getImageName())) throw new FrameException("Screen image not found");

		BufferedImage screenImage = images.get(ip.getImageName());

		return new Image(screenImage, ip);

	}

	public Image getFrame(int no) throws FrameException
	{

		FrameAddress fa = new FrameAddress(no);

		return getFrame(fa.screen, fa.frame);
	}

	public ImageProperties getComicProperties()
	{
		return settings.getComicProperties();
	}

	public ImageProperties getScreenProperties(int no) throws FrameException
	{
		FrameAddress fa = new FrameAddress(no);

		return settings.getScreenProperties(fa.screen);
	}
	
	public ImageProperties getFrameProperties(int no) throws FrameException
	{

		FrameAddress fa = new FrameAddress(no);

		return settings.getFrameProperties(fa.screen, fa.frame);
	}



	public void changeComic(ImageProperties ip)
	{
		settings.changeComic(ip);
	}

	public void changeScreen(ImageProperties ip, int no) throws FrameException
	{
		FrameAddress fa = new FrameAddress(no);

		settings.changeScreen(ip, fa.screen);
	}

	public void changeFrame(ImageProperties ip, int no) throws FrameException
	{
		FrameAddress fa = new FrameAddress(no);

		settings.changeFrame(ip, fa.screen, fa.frame);
	}

	public int getSizeOfScreen(int no)
	{
		return settings.getSizeOfScreen(no);
	}

	public int getSizeOfComic()
	{
		int size = 0;
		while (images.containsKey(settings.getImageName(size)))
			size += 1;
		return size;
	}

	public void removeFrame(int no) throws FrameException
	{
		FrameAddress fa = new FrameAddress(no);

		if (settings.getSizeOfScreen(fa.screen) <= 1)
		{
			throw new FrameException("Last Frame");
		}
		else
		{
			settings.removeFrame(fa.screen, fa.frame);
		}
	}

	public void removeScreen(int no) throws FrameException
	{
		FrameAddress fa = new FrameAddress(no);

		String imageName = settings.getImageName(fa.screen);

		if (!images.containsKey(imageName)) throw new FrameException("Image not found removeScreen@ACVFile");

		

		settings.removeScreen(fa.screen);

		//Shifting

		int imageNo = fa.screen;
		while (true)
		{

			String newKey = settings.getImageName(imageNo);
			String oldKey = settings.getImageName(imageNo+1);

			if (!images.containsKey(oldKey))
			{
				images.remove(newKey);
				break;
			}

			images.put(newKey, images.get(oldKey));

			imageNo += 1;
		}
	}

	public void addImage(int afterNo, BufferedImage image)
	{
		
		for (int imageNo = getSizeOfComic()-1; imageNo > afterNo; imageNo--)
		{
			String newKey = settings.getImageName(imageNo+1);
			String oldKey = settings.getImageName(imageNo);

			images.put(newKey, images.get(oldKey));
		}

		images.put(settings.getImageName(afterNo+1), image);

		settings.addScreen(afterNo, new Screen());
	}

	public void addFrame(int no) throws FrameException
	{
		FrameAddress fa = new FrameAddress(no);

		settings.addFrame(fa.screen, fa.frame, new Frame(0,0,1,1));

	}

	public void exchangeScreens(int first, int second)
	{

		String firstKey = settings.getImageName(first);
		String secondKey = settings.getImageName(second);

		BufferedImage f = images.get(firstKey);
		BufferedImage s = images.get(secondKey);

		images.put(firstKey, s);
		images.put(secondKey, f);

		settings.exchangeScreens(first, second);

	}

	public boolean containsFrame(int no)
	{
		try
		{
			FrameAddress fa = new FrameAddress(no);
			
			if (! images.containsKey(settings.getImageName(fa.screen)) ) return false;
		}
		catch (FrameException e)
		{
			return false;
		}

		return true;
	}

}
