/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package acvstudio.elements;

/**
 *
 * @author Sinnister
 */
public class ImageName
{
	private String namePattern;
	private String indexPattern;
	private int startAt;
	private int length;

	public ImageName(String np, String ip, int start, int len)
	{
		namePattern = np;
		indexPattern = ip;
		startAt = start;
		length = len;
	}

	public String getName(int no)
	{
		String number = new Integer(startAt+no).toString();
		String ip = indexPattern.substring(0, indexPattern.length()-number.length()) + number;
		String result = namePattern.replaceFirst("@index", ip) + ".jpg";
		return result;
	}

	public String getIndexPattern() {
		return indexPattern;
	}

	public int getLength() {
		return length;
	}

	public String getNamePattern() {
		return namePattern;
	}

	public int getStartAt() {
		return startAt;
	}


}
