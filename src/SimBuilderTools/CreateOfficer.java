/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SimBuilderTools;

import battlesimulator.Military.Officer;

/**
 *
 * @author Arlo
 */
public class CreateOfficer extends AbstractCreationPanel<Officer> {

    /**
     * Creates new form CreateOfficer
     */
    public CreateOfficer() {
        super();
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public void Save() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void Load() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean Validate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Officer buildObject() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setObject(Officer object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
