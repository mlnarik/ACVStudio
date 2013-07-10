/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ACVEditFrame.java
 *
 * Created on 25.2.2011, 17:34:28
 */

package acvstudio;

import acvstudio.elements.ACVFile;
import acvstudio.elements.CommonProperties;
import acvstudio.elements.Image;
import acvstudio.elements.ImageProperties;
import acvstudio.elements.enums.Orientation;
import acvstudio.elements.enums.ReadDirection;
import acvstudio.elements.enums.ScaleMode;
import acvstudio.elements.enums.Transition;
import acvstudio.elements.exceptions.FrameException;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.text.NumberFormat;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.ParseException;

import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Sinnister
 */
public class ACVEditFrame extends javax.swing.JFrame {


	// <editor-fold defaultstate="collapsed" desc="All components for properties">

	private class JLabelData extends JLabel
	{

		public JLabelData(String text, boolean center)
		{
			super(text);
			if (center) this.setHorizontalAlignment(SwingConstants.CENTER);
		}

		public JLabelData(String text)
		{
			super(text);
		}
	}

	private class JTextData extends JTextField implements ActionListener
	{
		private String currentText;
		public JTextData(String name)
		{
			super();

			dataComponents.put(name, this);
			addActionListener(this);

		}
		
		public void actionPerformed(ActionEvent e) 
		{
			triggerDataChange();
		}


	}

	private class JFloatData extends JFormattedTextField implements ActionListener
	{
	
		public JFloatData(String name)
		{
			super(numberFormat);

			
			dataComponents.put(name, this);
			addActionListener(this);/*
			getDocument().addDocumentListener(new DocumentListener()
			{
				public void changedUpdate(DocumentEvent e) {  }
				public void removeUpdate(DocumentEvent e) {  }
				public void insertUpdate(DocumentEvent e) { triggerDataChange(); }
			});*/
		}




		public void actionPerformed(ActionEvent e)
		{
			triggerDataChange();
		}

	}

	private class JColorData extends JLabel implements MouseListener
	{
		private Color color = Color.GRAY;

		public JColorData(String name)
		{
			setOpaque(true);
			//setForeground(new Color(0,0,0));
			setBackground(color);

			setSize(20, 10);
			setBorder(BorderFactory.createMatteBorder(20,3,0,0, getBackground()));

			dataComponents.put(name, this);
			addMouseListener(this);
		}

		public void setColor(Color c)
		{
			color = c;
			setBorder(BorderFactory.createMatteBorder(20,3,0,0, c));
		}

		public Color getColor()
		{
			return color;
		}



		public void mouseClicked(MouseEvent e)
		{
			JColorChooser cc = new JColorChooser(color);
			Color newColor = JColorChooser.showDialog(this, "Choose background color", getBackground());
			if (newColor != null) setColor(newColor);
			
			triggerDataChange();
		}
		public void mouseExited(MouseEvent e) { }
		public void mouseEntered(MouseEvent e) { }
		public void mouseReleased(MouseEvent e) { }
		public void mousePressed(MouseEvent e) { }
	}

	private class JBooleanData extends JCheckBox implements ActionListener
	{

		//private String name;

		public JBooleanData(String name)
		{
			super();

			//this.name = name;
			dataComponents.put(name, this);
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e)
		{
			triggerDataChange();
		}
	}

	private class JEnumData extends JComboBox implements ActionListener
	{
		public JEnumData(String name, String[] values)
		{
			super(values);

			dataComponents.put(name, this);
			addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			triggerDataChange();
		}
	}
	// </editor-fold>

    /** Creates new form ACVEditFrame */
    public ACVEditFrame()
	// <editor-fold defaultstate="collapsed" desc="Long code..">
	{
        initComponents();

		addWindowListener( new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				file = null;
			}
		} );

		manager.addWindowListener( new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				refreshPreviews();
				manager.setVisible(false);
			}
		} );

		framePanel.setLayout(new BoxLayout(framePanel, BoxLayout.X_AXIS));
		JScrollPane scrollPane = new JScrollPane(framePanel);
		scrollPane.setMinimumSize(new Dimension(350, 150));
		scrollPane.setPreferredSize(new Dimension(350, 150));
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getHorizontalScrollBar().setUnitIncrement(54);
		rightSplitPane.setTopComponent(scrollPane);

		numberFormat.setMaximumFractionDigits(2);
		numberFormat.setMinimumFractionDigits(2);
		
		JScrollPane SpreviewDisplay = new JScrollPane(previewDisplay);
		previewDisplay.addMouseListener( new MouseAdapter()
		{
		
			@Override
			public void mouseReleased(MouseEvent e)
			{
				triggerDataChange();
			}


		});
		SpreviewDisplay.getVerticalScrollBar().setUnitIncrement(16);
		SpreviewDisplay.getHorizontalScrollBar().setUnitIncrement(16);
		mainSplitPane.setLeftComponent(SpreviewDisplay);

		JPanel inheritPropPanel = new JPanel();
		inheritPropPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.insets = new Insets(2,2,2,2);

		//Line 0
		c.gridy = 0;

		c.gridx = 1;
		inheritPropPanel.add(new JLabelData("Comic properties", true), c);

		c.gridx = 2;
		inheritPropPanel.add(new JLabelData("Image properties", true), c);

		c.gridx = 3;
		inheritPropPanel.add(new JLabelData("Frame properties", true), c);



		//Line 1
		c.gridy = 1;

		c.gridx = 0;		
		inheritPropPanel.add(new JLabelData("Title"), c);

		c.gridx = 1;
		inheritPropPanel.add(new JTextData("cTitle"), c);

		c.gridx = 2;
		inheritPropPanel.add(new JTextData("sTitle"), c);

		c.gridx = 3;
		inheritPropPanel.add(new JLabelData("-",true), c);


		//Line 2
		c.gridy = 2;

		c.gridx = 0;
		inheritPropPanel.add(new JLabelData("Transition"), c);

		c.gridx = 1;
		inheritPropPanel.add(new JEnumData("cTransition", new String[] {"","Fade","Push (to side)","Push up","Push down","None"}), c);

		c.gridx = 2;
		inheritPropPanel.add(new JEnumData("sTransition", new String[] {"","Fade","Push (to side)","Push up","Push down","None"}), c);

		c.gridx = 3;
		inheritPropPanel.add(new JLabelData("-", true), c);


		//Line 3
		c.gridy = 3;

		c.gridx = 0;
		inheritPropPanel.add(new JLabelData("Color"), c);

		c.gridx = 1;
		inheritPropPanel.add(new JColorData("cColor"), c);

		c.gridx = 2;
		inheritPropPanel.add(new JColorData("sColor"), c);

		c.gridx = 3;
		inheritPropPanel.add(new JColorData("fColor"), c);


		//Line 4
		c.gridy = 4;

		c.gridx = 0;
		inheritPropPanel.add(new JLabelData("Vibration"), c);

		c.gridx = 1;
		inheritPropPanel.add(new JLabelData("-",true), c);

		c.gridx = 2;
		inheritPropPanel.add(new JBooleanData("sVibration"), c);

		c.gridx = 3;
		inheritPropPanel.add(new JBooleanData("fVibration"), c);

		


		JPanel extendedPropPanel = new JPanel();
		extendedPropPanel.setLayout(new GridBagLayout());

		//Line 0
		c.gridy = 0;

		c.gridx = 1;
		extendedPropPanel.add(new JLabelData("Comic properties", true), c);

		c.gridx = 3;
		extendedPropPanel.add(new JLabelData("Frame properties", true), c);

		//Line 1
		c.gridy = 1;

		c.gridx = 0;
		extendedPropPanel.add(new JLabelData("Scale Mode"), c);

		c.gridx = 1;
		extendedPropPanel.add(new JEnumData("cScaleMode", new String[] {"No scaling","Best","Height","Width"}), c);

		c.gridx = 2;
		extendedPropPanel.add(new JLabelData("Transition duration"), c);

		c.gridx = 3;
		extendedPropPanel.add(new JFloatData("fTransitionDuration"), c);


		//Line 2
		c.gridy = 2;

		c.gridx = 0;
		extendedPropPanel.add(new JLabelData("Orientation"), c);

		c.gridx = 1;
		extendedPropPanel.add(new JEnumData("cOrientation", new String[] {"Landscape","Portrait"}), c);

		c.gridx = 2;
		extendedPropPanel.add(new JLabelData("Autoplay"), c);

		c.gridx = 3;
		extendedPropPanel.add(new JBooleanData("fAutoplay"), c);


		//Line 3
		c.gridy = 3;

		c.gridx = 0;
		extendedPropPanel.add(new JLabelData("Read Direction"), c);

		c.gridx = 1;
		extendedPropPanel.add(new JEnumData("cDirection", new String[] {"Left to right","Right to left"}), c);

		c.gridx = 2;
		extendedPropPanel.add(new JLabelData("Autoplay duration"), c);

		c.gridx = 3;
		extendedPropPanel.add(new JFloatData("fAutoplayDuration"), c);




		JPanel propertiesPanel = new JPanel();
		propertiesPanel.setLayout(new GridBagLayout());

		c.gridx = 0;
		c.gridy = 0;
		propertiesPanel.add(inheritPropPanel, c);

		c.gridx = 0;
		c.gridy = 1;
		propertiesPanel.add(extendedPropPanel,c);

		c.gridx = 0;
		c.gridy = 2;
		c.weighty = 1;
		propertiesPanel.add(new JLabel(), c);

		propertiesPanel.setMinimumSize(new Dimension(350, 200));

		rightSplitPane.setBottomComponent(propertiesPanel);



	}
	// </editor-fold>


	private void setValue(String name, Object value)
	{
		Component c = dataComponents.get(name);

		if (c == null) System.err.println("Selected component (" + name + ") does not exist setValue@ACVEditFrame");

		if (c instanceof JTextData)
		{
			((JTextField)c).setText((String) value);
		}
		if (c instanceof JFloatData)
		{
			((JFormattedTextField)c).setText(numberFormat.format((Float)value));
		}
		else if (c instanceof JColorData)
		{
			((JColorData)c).setColor((Color) value);
		}
		else if (c instanceof JEnumData)
		{

			String item = "";

			if (!(value instanceof Boolean))
			{
				if (value instanceof Transition)
				{
					Transition tr = (Transition) value;
					if (tr == Transition.FADE) item = "Fade";
					else if(tr == Transition.PUSH) item = "Push (to side)";
					else if(tr == Transition.PUSHDOWN) item = "Push down";
					else if(tr == Transition.PUSHUP) item = "Push up";
					else item = "None";
				}
				else if (value instanceof ScaleMode)
				{
					ScaleMode sm = (ScaleMode) value;
					if (sm == ScaleMode.BEST) item = "Best";
					else if (sm == ScaleMode.HEIGHT) item = "Height";
					else if (sm == ScaleMode.WIDTH) item = "Width";
					else item = "No scaling";
				}
				else if (value instanceof Orientation)
				{
					Orientation or = (Orientation) value;
					if (or == Orientation.LANDSCAPE) item = "Landscape";
					else item = "Portrait";
				}
				else if (value instanceof ReadDirection)
				{
					ReadDirection rd = (ReadDirection) value;
					if (rd == ReadDirection.LEFTTORIGHT) item = "Left to right";
					else item = "Right to left";
				}
			}
			((JComboBox)c).setSelectedItem(item);
		}
		else if (c instanceof JCheckBox)
		{
			((JCheckBox)c).setSelected((Boolean) value);
		}
	}

	private Object getValue(String name)
	{
		if (name == null) return null;
		
		Component c = dataComponents.get(name);

		if (c == null) System.err.println("Selected component (" + name + ") does not exist getValue@ACVEditFrame");

		if (c instanceof JTextData)
		{
			return ((JTextField)c).getText();
		}
		if (c instanceof JFloatData)
		{
			try
			{
				//String s = ((JFormattedTextField)c).getText();
				Number n = numberFormat.parse(((JFormattedTextField)c).getText());
				return n.floatValue();
			}
			catch (ParseException e)
			{
				return null;
			}
			
		}
		else if (c instanceof JColorData)
		{
			return ((JColorData)c).getColor();
		}
		else if (c instanceof JEnumData)
		{
			String item = (String)((JEnumData)c).getSelectedItem();

			if (item.equals("Fade")) return Transition.FADE;
			else if (item.equals("Push (to side)")) return Transition.PUSH;
			else if (item.equals("Push down")) return Transition.PUSHDOWN;
			else if (item.equals("Push up")) return Transition.PUSHUP;
			else if (item.equals("None")) return Transition.NONE;

			else if (item.equals("Best")) return ScaleMode.BEST;
			else if (item.equals("Height")) return ScaleMode.HEIGHT;
			else if (item.equals("Width")) return ScaleMode.WIDTH;
			else if (item.equals("No scaling")) return ScaleMode.NONE;

			else if (item.equals("Landscape")) return Orientation.LANDSCAPE;
			else if (item.equals("Portrait")) return Orientation.PORTRAIT;

			else if (item.equals("Left to right")) return ReadDirection.LEFTTORIGHT;
			else if (item.equals("Right to left")) return ReadDirection.RIGHTTOLEFT;
			else return null;

		}
		else if (c instanceof JCheckBox)
		{
			return ((JCheckBox)c).isSelected();
		}
		else
		{
			System.err.println("Selected component (" + name + ") is unknown type getValue@ACVEditFrame");
			return null;
		}
	}



	private CommonProperties calculateCP(String compTitle, String compColor, String compTransition, String compVibration)
	{
		CommonProperties cp = new CommonProperties();

		String title = (String) getValue(compTitle);
		if ((title != null) && (!title.equals(""))) cp.setTitle(title);

		Color color = (Color) getValue(compColor);
		if (color != Color.GRAY) cp.setBackgroundColor(color);

		Transition transition = (Transition) getValue(compTransition);
		if (transition != null) cp.setTransition(transition);

		Boolean vibration = (Boolean) (getValue(compVibration));
		if (vibration != null && vibration) cp.setVibration(true);

		return cp;
	}

	private void triggerDataChange()
	{
		if (!isFrameReady || frameNumber == -1) return;

		CommonProperties comicCP = calculateCP("cTitle", "cColor", "cTransition", null);

		CommonProperties screenCP = calculateCP("sTitle", "sColor", "sTransition", "sVibration");

		CommonProperties frameCP = calculateCP(null, "fColor", null, "fVibration");

		
		ImageProperties comicIP = new ImageProperties((ScaleMode) getValue("cScaleMode"), (Orientation) getValue("cOrientation"),
				(ReadDirection) getValue("cDirection"), "2", comicCP, null);

		ImageProperties screenIP = new ImageProperties(null, null, null, null, screenCP, null);

		double [] ra = previewDisplay.getRelativeArea();

		ImageProperties frameIP = new ImageProperties(null, null, null, null, frameCP, null, (Float) getValue("fTransitionDuration"),
				(Boolean) getValue("fAutoplay"), (Float) getValue("fAutoplayDuration"), previewDisplay.getRelativeArea());

		try
		{
			file.changeComic(comicIP);
			file.changeScreen(screenIP, frameNumber);
			file.changeFrame(frameIP, frameNumber);

			refreshPreviews();
		}
		catch (FrameException e)
		{
			System.err.println("Unexpected error triggerFrameChange@ACVEditFrame " + e.toString());
		}
	}

	private void refreshPreviews()
	{
		if (file == null) return;

		MouseListener selectFrame = new MouseAdapter()
		{

			@Override
			public void mouseClicked(MouseEvent evt)
			{
				triggerDataChange();

				isFrameReady = false;
				frameNumber = Preview.currentNumber - 1;
				zoom = 1;


				ImageProperties comic = file.getComicProperties();
				ImageProperties screen;
				ImageProperties frame;
				try
				{
					screen = file.getScreenProperties(frameNumber);
				}
				catch (FrameException ex)
				{
					screen = new ImageProperties();
				}
				try
				{
					frame = file.getFrameProperties(frameNumber);
				}
				catch (FrameException ex)
				{
					frame = new ImageProperties();
				}




				setValue("cTitle", comic.getProperties().getTitle());
				setValue("sTitle", screen.getProperties().getTitle());

				if (comic.getProperties().isDefColor()) setValue("cColor", comic.getProperties().getBackgroundColor());
				else setValue("cColor", Color.GRAY);
				if (screen.getProperties().isDefColor()) setValue("sColor", screen.getProperties().getBackgroundColor());
				else setValue("sColor", Color.GRAY);
				if (frame.getProperties().isDefColor()) setValue("fColor", frame.getProperties().getBackgroundColor());
				else setValue("fColor", Color.GRAY);

				if (comic.getProperties().isDefTransition()) setValue("cTransition", comic.getProperties().getTransition());
				else setValue("cTransition", false);
				if (screen.getProperties().isDefTransition()) setValue("sTransition", screen.getProperties().getTransition());
				else setValue("sTransition", false);

				setValue("sVibration", screen.getProperties().getVibration());
				setValue("fVibration", frame.getProperties().getVibration());


				setValue("cScaleMode", comic.getScaleMode());
				setValue("cOrientation", comic.getOrientation());
				setValue("cDirection", comic.getReadDirection());

				setValue("fTransitionDuration", frame.getTransitionDuration());
				setValue("fAutoplay", frame.isAutoplay());
				setValue("fAutoplayDuration", frame.getDurationAutoplay());

				isFrameReady = true;

				try
				{
					Image img = file.getScreen(frameNumber);
					img.getProperties().removeEffects();
					img.getProperties().removeScaling();
					previewDisplay.loadImage(img, frame);
			
				}
				catch (FrameException ex)
				{
					System.err.println("Unexpected exception mouseClicked@ACVEditFrame: " + ex.toString());
				}
			}
		};

		framePanel.removeAll();

		Integer frame = 0;
		try
		{
			while (true)
			{
				Preview prev = new Preview();

				Image prop = file.getFrame(frame);
				prop.getProperties().removeEffects();
				prev.set(prop, frame+1);
				prev.addMouseListener(selectFrame);


				framePanel.add(prev);
				frame++;
			}

		}
		catch (FrameException e) {}

		framePanel.updateUI();
	}



	public void loadFile(ACVFile f)
	{
		frameNumber = -1;
		Preview.currentNumber = -1;

		previewDisplay.loadImage(null, null);
		previewDisplay.updateUI();

		if (f == null) return;

		file = f;	

		refreshPreviews();
	}


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        mainSplitPane = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        rightSplitPane = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        bAddFrame = new javax.swing.JButton();
        bRemoveFrame = new javax.swing.JButton();
        bManageImages = new javax.swing.JButton();
        bZoomIn = new javax.swing.JButton();
        bZoomOut = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        miNewFile = new javax.swing.JMenuItem();
        miSave = new javax.swing.JMenuItem();
        miSaveAs = new javax.swing.JMenuItem();
        miExit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(acvstudio.ACVStudioApp.class).getContext().getResourceMap(ACVEditFrame.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        getContentPane().setLayout(new java.awt.GridLayout(1, 1));

        mainPanel.setAlignmentX(1.0F);
        mainPanel.setAlignmentY(1.0F);
        mainPanel.setName("mainPanel"); // NOI18N

        mainSplitPane.setBorder(null);
        mainSplitPane.setResizeWeight(1.0);
        mainSplitPane.setName("mainSplitPane"); // NOI18N
        mainSplitPane.setOpaque(false);

        jPanel1.setName("jPanel1"); // NOI18N

        rightSplitPane.setBorder(null);
        rightSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        rightSplitPane.setEnabled(false);
        rightSplitPane.setName("rightSplitPane"); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new java.awt.GridLayout(9, 6));
        rightSplitPane.setBottomComponent(jPanel2);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        bAddFrame.setIcon(resourceMap.getIcon("bAddFrame.icon")); // NOI18N
        bAddFrame.setText(resourceMap.getString("bAddFrame.text")); // NOI18N
        bAddFrame.setToolTipText(resourceMap.getString("bAddFrame.toolTipText")); // NOI18N
        bAddFrame.setFocusable(false);
        bAddFrame.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        bAddFrame.setName("bAddFrame"); // NOI18N
        bAddFrame.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bAddFrame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddFrameActionPerformed(evt);
            }
        });
        jToolBar1.add(bAddFrame);

        bRemoveFrame.setIcon(resourceMap.getIcon("bRemoveFrame.icon")); // NOI18N
        bRemoveFrame.setText(resourceMap.getString("bRemoveFrame.text")); // NOI18N
        bRemoveFrame.setToolTipText(resourceMap.getString("bRemoveFrame.toolTipText")); // NOI18N
        bRemoveFrame.setFocusable(false);
        bRemoveFrame.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        bRemoveFrame.setName("bRemoveFrame"); // NOI18N
        bRemoveFrame.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bRemoveFrame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRemoveFrameActionPerformed(evt);
            }
        });
        jToolBar1.add(bRemoveFrame);

        bManageImages.setIcon(resourceMap.getIcon("bManageImages.icon")); // NOI18N
        bManageImages.setText(resourceMap.getString("bManageImages.text")); // NOI18N
        bManageImages.setToolTipText(resourceMap.getString("bManageImages.toolTipText")); // NOI18N
        bManageImages.setFocusable(false);
        bManageImages.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        bManageImages.setName("bManageImages"); // NOI18N
        bManageImages.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bManageImages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bManageImagesActionPerformed(evt);
            }
        });
        jToolBar1.add(bManageImages);

        bZoomIn.setIcon(resourceMap.getIcon("bZoomIn.icon")); // NOI18N
        bZoomIn.setText(resourceMap.getString("bZoomIn.text")); // NOI18N
        bZoomIn.setFocusable(false);
        bZoomIn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        bZoomIn.setName("bZoomIn"); // NOI18N
        bZoomIn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bZoomIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bZoomInActionPerformed(evt);
            }
        });
        jToolBar1.add(bZoomIn);

        bZoomOut.setIcon(resourceMap.getIcon("bZoomOut.icon")); // NOI18N
        bZoomOut.setText(resourceMap.getString("bZoomOut.text")); // NOI18N
        bZoomOut.setFocusable(false);
        bZoomOut.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        bZoomOut.setName("bZoomOut"); // NOI18N
        bZoomOut.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        bZoomOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bZoomOutActionPerformed(evt);
            }
        });
        jToolBar1.add(bZoomOut);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rightSplitPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rightSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE))
        );

        mainSplitPane.setRightComponent(jPanel1);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 951, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainSplitPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
        );

        getContentPane().add(mainPanel);

        jMenuBar1.setName("jMenuBar1"); // NOI18N

        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N

        miNewFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        miNewFile.setText(resourceMap.getString("miNewFile.text")); // NOI18N
        miNewFile.setName("miNewFile"); // NOI18N
        miNewFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miNewFileActionPerformed(evt);
            }
        });
        jMenu1.add(miNewFile);

        miSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        miSave.setText(resourceMap.getString("miSave.text")); // NOI18N
        miSave.setName("miSave"); // NOI18N
        miSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSaveActionPerformed(evt);
            }
        });
        jMenu1.add(miSave);

        miSaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        miSaveAs.setText(resourceMap.getString("miSaveAs.text")); // NOI18N
        miSaveAs.setName("miSaveAs"); // NOI18N
        miSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSaveAsActionPerformed(evt);
            }
        });
        jMenu1.add(miSaveAs);

        miExit.setText(resourceMap.getString("miExit.text")); // NOI18N
        miExit.setName("miExit"); // NOI18N
        miExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miExitActionPerformed(evt);
            }
        });
        jMenu1.add(miExit);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void miSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSaveAsActionPerformed
		
		FileFilter filter = new FileNameExtensionFilter("ACV files", "acv");
		filechooser.addChoosableFileFilter(filter);

		int ret = filechooser.showSaveDialog(this);

		if (ret == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				triggerDataChange();
				String filename = filechooser.getSelectedFile().toString();
				if (filechooser.getSelectedFile().getName().indexOf('.') == -1) filename += ".acv";
				file.save(filename);
				
			}
			catch (Exception e)
			{
				
				JOptionPane.showMessageDialog(mainPanel, "Selected file cannot be saved", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}

	}//GEN-LAST:event_miSaveAsActionPerformed

	private void bRemoveFrameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRemoveFrameActionPerformed
		
		if (frameNumber == -1)
		{
			JOptionPane.showMessageDialog(mainPanel, "Select frame first.",
						"Select frame", JOptionPane.WARNING_MESSAGE);

			return;
		}

		try
		{
			file.removeFrame(frameNumber);
			refreshPreviews();
		}
		catch (FrameException e)
		{
			if (e.getMessage().equals("Last Frame"))
			{
				JOptionPane.showMessageDialog(mainPanel, "This is last frame of image. Images can't exist without frames. Remove the image in Image Manager instead.",
						"Last frame of image", JOptionPane.WARNING_MESSAGE);
			}
			else
				System.err.println("Unexpected exception bRemoveFrameActionPerformed@ACVEditFrame " + e.toString());
		}
	}//GEN-LAST:event_bRemoveFrameActionPerformed

	private void bManageImagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bManageImagesActionPerformed
		if (file != null)
		{
			manager.loadList(file);
			manager.setVisible(true);
		}
	}//GEN-LAST:event_bManageImagesActionPerformed

	private void bAddFrameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddFrameActionPerformed

		if (frameNumber == -1)
		{
			JOptionPane.showMessageDialog(mainPanel, "Select frame first. New frame will follow after selected one.",
						"Select frame", JOptionPane.WARNING_MESSAGE);
			return;
		}

		try
		{
			file.addFrame(frameNumber);
		}
		catch (FrameException e)
		{
			System.err.println("Unexpected exception bAddFrameActionPerformed@ACVEditFrame " + e.toString());
		}
		refreshPreviews();
	}//GEN-LAST:event_bAddFrameActionPerformed

	private void miNewFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miNewFileActionPerformed
		file = new ACVFile();
		loadFile(file);
		JOptionPane.showMessageDialog(mainPanel, "Add your first images to comic.",
						"Add images", JOptionPane.INFORMATION_MESSAGE);
		manager.loadList(file);
		manager.setVisible(true);
	}//GEN-LAST:event_miNewFileActionPerformed

	private void miExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miExitActionPerformed
		this.processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}//GEN-LAST:event_miExitActionPerformed

	private void miSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSaveActionPerformed

		triggerDataChange();
		try
		{
			file.save();
		}
		catch (IOException e)
		{
			if (e.getMessage().equals("New File"))
				miSaveAsActionPerformed(evt);
			else
				JOptionPane.showMessageDialog(mainPanel, "Access Denied.",
						"Save File", JOptionPane.ERROR_MESSAGE);
		}
	}//GEN-LAST:event_miSaveActionPerformed

	private void bZoomInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bZoomInActionPerformed
		if (zoom == 0.5f) zoom = 1;
		else if (zoom == 1) zoom = 2;
		else if (zoom == 2) zoom = 4;
		else if (zoom == 4) zoom = 8;
		previewDisplay.setZoom(zoom);
	}//GEN-LAST:event_bZoomInActionPerformed

	private void bZoomOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bZoomOutActionPerformed
		if (zoom == 8) zoom = 4;
		else if (zoom == 4) zoom = 2;
		else if (zoom == 2) zoom = 1;
		else if (zoom == 1) zoom = 0.5f;
		previewDisplay.setZoom(zoom);
	}//GEN-LAST:event_bZoomOutActionPerformed

    /**
    * @param args the command line arguments
    */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAddFrame;
    private javax.swing.JButton bManageImages;
    private javax.swing.JButton bRemoveFrame;
    private javax.swing.JButton bZoomIn;
    private javax.swing.JButton bZoomOut;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JSplitPane mainSplitPane;
    private javax.swing.JMenuItem miExit;
    private javax.swing.JMenuItem miNewFile;
    private javax.swing.JMenuItem miSave;
    private javax.swing.JMenuItem miSaveAs;
    private javax.swing.JSplitPane rightSplitPane;
    // End of variables declaration//GEN-END:variables
	private int frameNumber = -1;
	private ACVFile file;
	private EditImageDisplay previewDisplay = new EditImageDisplay();
	private JFileChooser filechooser = new JFileChooser();
	private NumberFormat numberFormat = NumberFormat.getNumberInstance();
	private HashMap<String, Component> dataComponents = new HashMap<String, Component>();
	private boolean isFrameReady = false;
	private JPanel framePanel = new JPanel();
	private ACVImageManager manager = new ACVImageManager();
	private float zoom = 1;



}
