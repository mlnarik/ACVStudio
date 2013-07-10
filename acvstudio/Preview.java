/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Preview.java
 *
 * Created on 8.3.2011, 17:36:53
 */

package acvstudio;

import acvstudio.elements.Image;
import java.awt.Color;
import javax.swing.BorderFactory;

/**
 *
 * @author Sinnister
 */
public final class Preview extends javax.swing.JPanel {

    /** Creates new form Preview */
    public Preview() {
        initComponents();

		//this.deselect();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        iD = new acvstudio.ImageDisplay();
        number = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(300, 300));
        setMinimumSize(new java.awt.Dimension(50, 50));
        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(160, 130));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        iD.setName("iD"); // NOI18N

        javax.swing.GroupLayout iDLayout = new javax.swing.GroupLayout(iD);
        iD.setLayout(iDLayout);
        iDLayout.setHorizontalGroup(
            iDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 115, Short.MAX_VALUE)
        );
        iDLayout.setVerticalGroup(
            iDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 107, Short.MAX_VALUE)
        );

        number.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(acvstudio.ACVStudioApp.class).getContext().getResourceMap(Preview.class);
        number.setText(resourceMap.getString("number.text")); // NOI18N
        number.setName("number"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(number, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(iD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(iD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(number))
                .addContainerGap(23, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

	private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
		requestFocusInWindow();

		if (selectedPreview != this && selectedPreview != null) selectedPreview.deselect();
		select();
		selectedPreview = this;
		currentNumber = numberId;
	}//GEN-LAST:event_formMouseClicked

	public void deselect()
	{
		iD.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
	}

	public void select()
	{
		iD.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.YELLOW));

	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private acvstudio.ImageDisplay iD;
    private javax.swing.JLabel number;
    // End of variables declaration//GEN-END:variables
	private int numberId;
	
	public static int currentNumber = 0;
	public static Preview selectedPreview;

	public void set(Image img, Integer no)
	{
		iD.loadImage(img, false);
		number.setText(no.toString());
		numberId = no;

		if (no == currentNumber)
		{
			select();
			selectedPreview = this;
		}
		else deselect();
	}

	public int getNumber()
	{
		return numberId;
	}
}
