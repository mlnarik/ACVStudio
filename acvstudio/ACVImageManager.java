/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ACVImageManager.java
 *
 * Created on 28.3.2011, 22:34:53
 */

package acvstudio;

import acvstudio.elements.ACVFile;
import acvstudio.elements.Image;
import acvstudio.elements.exceptions.FrameException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Sinnister
 */
public class ACVImageManager extends javax.swing.JFrame {

    /** Creates new form ACVImageManager */
    public ACVImageManager()
	{
        initComponents();

		imageList.setModel(listModel);
		fileChooser.setMultiSelectionEnabled(true);

		addWindowListener( new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				file = null;
			}
		} );
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        previewImage = new acvstudio.ImageDisplay();
        jPanel1 = new javax.swing.JPanel();
        bOK = new javax.swing.JButton();
        bAddImage = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        imageList = new javax.swing.JList();
        bRemoveImage = new javax.swing.JButton();
        bMoveUp = new javax.swing.JButton();
        bMoveDown = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(acvstudio.ACVStudioApp.class).getContext().getResourceMap(ACVImageManager.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jSplitPane1.setResizeWeight(1.0);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        previewImage.setMinimumSize(new java.awt.Dimension(300, 300));
        previewImage.setName("previewImage"); // NOI18N

        javax.swing.GroupLayout previewImageLayout = new javax.swing.GroupLayout(previewImage);
        previewImage.setLayout(previewImageLayout);
        previewImageLayout.setHorizontalGroup(
            previewImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        previewImageLayout.setVerticalGroup(
            previewImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(previewImage);

        jPanel1.setMinimumSize(new java.awt.Dimension(164, 300));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(164, 300));

        bOK.setIcon(resourceMap.getIcon("bOK.icon")); // NOI18N
        bOK.setText(resourceMap.getString("bOK.text")); // NOI18N
        bOK.setToolTipText(resourceMap.getString("bOK.toolTipText")); // NOI18N
        bOK.setName("bOK"); // NOI18N
        bOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOKActionPerformed(evt);
            }
        });

        bAddImage.setIcon(resourceMap.getIcon("bAddImage.icon")); // NOI18N
        bAddImage.setText(resourceMap.getString("bAddImage.text")); // NOI18N
        bAddImage.setToolTipText(resourceMap.getString("bAddImage.toolTipText")); // NOI18N
        bAddImage.setName("bAddImage"); // NOI18N
        bAddImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddImageActionPerformed(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        imageList.setBackground(resourceMap.getColor("imageList.background")); // NOI18N
        imageList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        imageList.setName("imageList"); // NOI18N
        imageList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                imageListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(imageList);

        bRemoveImage.setIcon(resourceMap.getIcon("bRemoveImage.icon")); // NOI18N
        bRemoveImage.setText(resourceMap.getString("bRemoveImage.text")); // NOI18N
        bRemoveImage.setToolTipText(resourceMap.getString("bRemoveImage.toolTipText")); // NOI18N
        bRemoveImage.setName("bRemoveImage"); // NOI18N
        bRemoveImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRemoveImageActionPerformed(evt);
            }
        });

        bMoveUp.setIcon(resourceMap.getIcon("bMoveUp.icon")); // NOI18N
        bMoveUp.setText(resourceMap.getString("bMoveUp.text")); // NOI18N
        bMoveUp.setToolTipText(resourceMap.getString("bMoveUp.toolTipText")); // NOI18N
        bMoveUp.setName("bMoveUp"); // NOI18N
        bMoveUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bMoveUpActionPerformed(evt);
            }
        });

        bMoveDown.setIcon(resourceMap.getIcon("bMoveDown.icon")); // NOI18N
        bMoveDown.setText(resourceMap.getString("bMoveDown.text")); // NOI18N
        bMoveDown.setToolTipText(resourceMap.getString("bMoveDown.toolTipText")); // NOI18N
        bMoveDown.setName("bMoveDown"); // NOI18N
        bMoveDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bMoveDownActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(bOK, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bAddImage, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bRemoveImage, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bMoveUp, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bMoveDown, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bOK)
                        .addComponent(bAddImage))
                    .addComponent(bRemoveImage)
                    .addComponent(bMoveUp)
                    .addComponent(bMoveDown)))
        );

        jSplitPane1.setRightComponent(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void imageListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_imageListValueChanged
		try
		{
			int index = imageList.getSelectedIndex();
			Image image = file.getScreenById(index);
			image.getProperties().removeEffects();
			previewImage.loadImage(image, false);
		}
		catch (FrameException e)
		{
			
		}
	}//GEN-LAST:event_imageListValueChanged

	private void bAddImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddImageActionPerformed

		if (file == null) return;
		
		FileFilter filter = new FileNameExtensionFilter("JPEG files", "jpg");
		fileChooser.addChoosableFileFilter(filter);
		filter = new FileNameExtensionFilter("PNG files", "png");
		fileChooser.addChoosableFileFilter(filter);

		int ret = fileChooser.showDialog(null, "Add images");

		if (ret == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				int index = imageList.getSelectedIndex();
				/*
				if (index == -1 && imageList.getMaxSelectionIndex() > 0)
				{
					JOptionPane.showMessageDialog(this, "Select image first. New image will be added after this one.", "Select image", JOptionPane.ERROR_MESSAGE);
				}*/
				File[] imageFiles = fileChooser.getSelectedFiles();

				for (File imageFile: imageFiles)
				{
					BufferedImage image = ImageIO.read(imageFile);
					file.addImage(index, image);
					index += 1;
				}
				refreshList();
				imageList.setSelectedIndex(index);
			}
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(this, "Selected file is not a valid ACV file", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}//GEN-LAST:event_bAddImageActionPerformed

	private void bRemoveImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRemoveImageActionPerformed

		int index = imageList.getSelectedIndex();

		if (index == -1) return;
		try
		{
			file.removeScreen(index);
		}
		catch (FrameException e)
		{
			System.err.println("Unexpected exception bRemoveImageActionPerformed@ACVImageManager " + e.toString());
		}
		refreshList();
		imageList.setSelectedIndex(index);
	}//GEN-LAST:event_bRemoveImageActionPerformed

	private void bOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOKActionPerformed
		//setVisible(false);
		this.processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}//GEN-LAST:event_bOKActionPerformed

	private void bMoveUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMoveUpActionPerformed
		int index = imageList.getSelectedIndex();

		if (index < 1) return;

		file.exchangeScreens(index, index-1);

		refreshList();

		imageList.setSelectedIndex(index-1);
	}//GEN-LAST:event_bMoveUpActionPerformed

	private void bMoveDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMoveDownActionPerformed
		int index = imageList.getSelectedIndex();

		if (index > file.getSizeOfComic()-2) return;

		file.exchangeScreens(index, index+1);

		refreshList();

		imageList.setSelectedIndex(index+1);
	}//GEN-LAST:event_bMoveDownActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ACVImageManager().setVisible(true);
            }
        });
    }

	public void loadList(ACVFile file)
	{
		if (file == null) return;

		this.file = file;
		previewImage.loadImage(null, false);

		refreshList();
	}

	private void refreshList()
	{
		listModel.clear();
		int size = file.getSizeOfComic();
		for (int i = 1; i<=size; i++)
			listModel.addElement("Image " + Integer.toString(i));
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAddImage;
    private javax.swing.JButton bMoveDown;
    private javax.swing.JButton bMoveUp;
    private javax.swing.JButton bOK;
    private javax.swing.JButton bRemoveImage;
    private javax.swing.JList imageList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private acvstudio.ImageDisplay previewImage;
    // End of variables declaration//GEN-END:variables
	private ACVFile file;
	private JFileChooser fileChooser = new JFileChooser();
	DefaultListModel listModel = new DefaultListModel();


}
