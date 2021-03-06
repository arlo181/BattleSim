/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SimBuilderTools;

import battlesimulator.Geography.Battlefield;
import battlesimulator.Geography.StartingPosition;
import battlesimulator.Geography.Stone;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Arlo
 */
public class CreateBattlefield extends AbstractCreationPanel<Battlefield>
{

    Battlefield battlefield;

    /**
     * Creates new form CreateBattlefield
     */
    public CreateBattlefield()
    {
        super();
        initComponents();

    }

    /**
     * This creates the Battlefield object based on the values entered in the
     * GUI.
     *
     * @return
     */
    @Override
    public Battlefield buildObject()
    {
        int numRows = ((Number) fldNumRows.getValue()).intValue();
        String name = fieldName.getText();
        int numCols = ((Number) fldNumCols.getValue()).intValue();
        int cellSize = ((Number) fldCellSize.getValue()).intValue();
        int numStones = Integer.parseInt(fldNumStones.getText());

        Battlefield field = new Battlefield(numCols, numRows, cellSize, name);

        for (int positionIndex = 0; positionIndex < this.positonsPanel.getComponentCount(); positionIndex++)
        {
            Component comp = this.positonsPanel.getComponent(positionIndex);
            if (CreateStartingPosition.class.isAssignableFrom(comp.getClass()))
            {
                CreateStartingPosition setStartingPosition = (CreateStartingPosition) comp;
                Dimension dimension = setStartingPosition.getDimensions().getDimension();
                Point point = setStartingPosition.getCoordinates().getPoint();
                Rectangle rectangle = new Rectangle(point, dimension);
                StartingPosition pos = new StartingPosition(rectangle);
                field.addStartingPosition(setStartingPosition.getFieldName(), pos);
            }
        }
        
        for(int stoneIndex = 0; stoneIndex < numStones; stoneIndex ++)
        {
            Point randomPoint = field.getRandomPoint();
            Stone stone = new Stone(randomPoint);
            field.addNeutralObject(stone);
        }

        return field;
    }

    public void clearStartingPositions()
    {
        this.positonsPanel.removeAll();
    }

    public void addStartingPosition()
    {
    }

    public void addStartingPosition(CreateStartingPosition position)
    {
    }

    @Override
    public void setObject(Battlefield battlefield)
    {
        this.battlefield = battlefield;
        //todo:  fill in the controls, based on the battlefield passed in.
        fieldName.setText(battlefield.getName());
        fldNumRows.setValue(battlefield.getNumCellsHeight());
        fldNumCols.setValue(battlefield.getNumCellsWidth());
        fldCellSize.setValue(battlefield.getSizeOfCells());
        HashMap<String, StartingPosition> startingPositions = battlefield.getStartingPositions();
        fldNumPositions.setText("" + startingPositions.size());
        this.positonsPanel.removeAll();
        //todo: iterate over positions, fill them in
        for (Map.Entry<String, StartingPosition> startingPosition : startingPositions.entrySet())
        {
            CreateStartingPosition positionToAdd = new CreateStartingPosition(this);
            positionToAdd.setPosition(startingPosition.getValue(), startingPosition.getKey());
            this.positonsPanel.add(positionToAdd);
        }
        this.positonsPanel.repaint();
    }

    public Battlefield getBattlefield()
    {
        return battlefield;
    }

    public void removeStartingPostion(CreateStartingPosition setStartingPosition)
    {
        this.positonsPanel.remove(setStartingPosition);
        this.battlefield.removeStartingPosition(setStartingPosition.getPositionName());
        this.positonsPanel.repaint();
        this.notifyListeners();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        scrollStartingPositions = new javax.swing.JScrollPane();
        positonsPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        fieldName = new javax.swing.JTextField();
        fldCellSize = new javax.swing.JFormattedTextField();
        fldNumRows = new javax.swing.JFormattedTextField();
        fldNumCols = new javax.swing.JFormattedTextField();
        btnAdd = new javax.swing.JButton();
        fldNumPositions = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        fldNumStones = new javax.swing.JTextField();

        jLabel1.setText("Cell Size");

        jLabel2.setText("Num Rows");

        jLabel3.setText("Num Columns");

        jLabel4.setText("Starting Positions");

        scrollStartingPositions.setMaximumSize(new java.awt.Dimension(32767, 400));

        positonsPanel.setMaximumSize(new java.awt.Dimension(90, 10000));
        positonsPanel.setMinimumSize(new java.awt.Dimension(90, 100));
        positonsPanel.setName(""); // NOI18N
        positonsPanel.setPreferredSize(new java.awt.Dimension(90, 400));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout();
        flowLayout1.setAlignOnBaseline(true);
        positonsPanel.setLayout(flowLayout1);
        scrollStartingPositions.setViewportView(positonsPanel);

        jLabel5.setText("Starting Positions");

        jLabel6.setText("Name");

        fieldName.setText("battlefield");
        fieldName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                fieldNameKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                fieldNameKeyReleased(evt);
            }
        });

        fldCellSize.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#"))));
        fldCellSize.setToolTipText("");
        fldCellSize.setValue(new Integer(25));

        fldNumRows.setText("100");
        fldNumRows.setValue(new Integer(100));

        fldNumCols.setText("100");
        fldNumCols.setValue(new Integer(100));

        btnAdd.setText("Add");
        btnAdd.setToolTipText("");
        btnAdd.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAddActionPerformed(evt);
            }
        });

        fldNumPositions.setText("0");

        jLabel7.setText("Stones");

        fldNumStones.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fieldName, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(fldNumRows, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                            .addComponent(fldCellSize)
                            .addComponent(fldNumPositions)
                            .addComponent(fldNumCols))
                        .addGap(60, 60, 60)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fldNumStones, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scrollStartingPositions, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(btnAdd)))
                .addGap(13, 13, 13))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(fieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(fldCellSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(fldNumStones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(fldNumRows, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fldNumCols, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(fldNumPositions))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(btnAdd))
                .addGap(4, 4, 4)
                .addComponent(scrollStartingPositions, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed

            this.battlefield = this.buildObject();
        
        int posNameSuffix = 0;
        String positionName = "position " + posNameSuffix;
        boolean uniqueName = false;
        HashMap<String, StartingPosition> startingPositions = battlefield.getStartingPositions();
        while (!uniqueName)
        {
            uniqueName = true;
            positionName = "position " + posNameSuffix;
            for (Map.Entry<String, StartingPosition> startingPosition : startingPositions.entrySet())
            {
                {
                    uniqueName &= (!startingPosition.getKey().equals(positionName));
                }

                posNameSuffix++;
            }
        }
        CreateStartingPosition positionToAdd = new CreateStartingPosition(this, positionName, this.battlefield.getRandomPoint());

        this.positonsPanel.add(positionToAdd);
        this.positonsPanel.setSize(this.positonsPanel.getWidth(), (startingPositions.size()+1)*275);
        positionToAdd.repaint();
        this.positonsPanel.repaint();
        this.repaint();
        this.positonsPanel.invalidate();
       
this.invalidate();
 this.scrollStartingPositions.invalidate();
        this.notifyListeners();

    }//GEN-LAST:event_btnAddActionPerformed

    private void fieldNameKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_fieldNameKeyPressed
    {//GEN-HEADEREND:event_fieldNameKeyPressed

    }//GEN-LAST:event_fieldNameKeyPressed

    private void fieldNameKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_fieldNameKeyReleased
    {//GEN-HEADEREND:event_fieldNameKeyReleased

    }//GEN-LAST:event_fieldNameKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JTextField fieldName;
    private javax.swing.JFormattedTextField fldCellSize;
    private javax.swing.JFormattedTextField fldNumCols;
    private javax.swing.JLabel fldNumPositions;
    private javax.swing.JFormattedTextField fldNumRows;
    private javax.swing.JTextField fldNumStones;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel positonsPanel;
    private javax.swing.JScrollPane scrollStartingPositions;
    // End of variables declaration//GEN-END:variables

    @Override
    public void Save()
    {
        if (Validate())
        {
            this.battlefield = buildObject();
            try
            {
                FileOutputStream fileOut
                        = new FileOutputStream("SavedData\\Battlefields\\" + this.battlefield.getName()); //TODO: magic string
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(this.battlefield);
                out.close();
                fileOut.close();

            } catch (IOException i)
            {
                i.printStackTrace();
            }
        }
    }

    @Override
    public void Load()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean Validate()
    {
        return !this.fieldName.getText().isEmpty();
    }
}
