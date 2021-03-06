/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SimBuilderTools;

import UnitFormations.GridFormation;
import battlesimulator.Military.Archer;
import battlesimulator.Military.Army;
import battlesimulator.Military.Cavalry;
import battlesimulator.Military.Mage;
import battlesimulator.Military.Officer;
import battlesimulator.Military.Soldier;
import battlesimulator.Military.Unit;
import java.awt.Color;

/**
 *
 * @author Arlo
 */
public class CreateUnit extends AbstractCreationPanel<Unit>
{

    CreateOfficer createOfficer;
    Unit unit;
    Army army;
    int unitScore;

    /**
     * Creates new form CreateUnit
     */
    public CreateUnit(Army army)
    {
        super();
        initComponents();
        this.army = army;
        unitScore = 0;
    }

    public void setArmy(Army army)
    {
        this.army = army;
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
        fldNumSoldiers = new javax.swing.JTextField();
        fldNumCavalry = new javax.swing.JTextField();
        fldNumArchers = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        fldOfficerName = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        fldUnitName = new javax.swing.JTextField();
        btnDelete = new javax.swing.JButton();
        fldNumMages = new javax.swing.JTextField();
        lblWizards = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblScore = new javax.swing.JLabel();

        jLabel1.setText("Randomly Generate:");

        jLabel2.setText("footmen");

        jLabel3.setText("cavalry");

        jLabel4.setText("archers");
        jLabel4.setToolTipText("");

        fldNumSoldiers.setText("0");
        fldNumSoldiers.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                fldNumSoldiersMouseReleased(evt);
            }
        });
        fldNumSoldiers.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                fldNumSoldiersKeyReleased(evt);
            }
        });

        fldNumCavalry.setText("0");
        fldNumCavalry.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                fldNumCavalryKeyReleased(evt);
            }
        });

        fldNumArchers.setText("0");
        fldNumArchers.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                fldNumArchersKeyReleased(evt);
            }
        });

        jLabel5.setText("Officer Name");

        fldOfficerName.setText("officerName");

        jLabel6.setText("Unit Name");

        fldUnitName.setText("unitName");
        fldUnitName.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(java.awt.event.FocusEvent evt)
            {
                fldUnitNameFocusLost(evt);
            }
        });
        fldUnitName.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                fldUnitNameActionPerformed(evt);
            }
        });
        fldUnitName.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                fldUnitNameKeyReleased(evt);
            }
        });

        btnDelete.setText("Delete Unit");
        btnDelete.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDeleteActionPerformed(evt);
            }
        });

        fldNumMages.setText("0");
        fldNumMages.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(java.awt.event.KeyEvent evt)
            {
                fldNumMagesKeyReleased(evt);
            }
        });

        lblWizards.setText("(buggy) wizards");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("Unit Score:");

        lblScore.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(fldUnitName)
                                    .addComponent(fldOfficerName, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(fldNumCavalry, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                                    .addComponent(fldNumSoldiers, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fldNumArchers)
                                    .addComponent(fldNumMages, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel4)
                                    .addComponent(lblWizards)
                                    .addComponent(jLabel3))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
                        .addComponent(btnDelete))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(52, 52, 52)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblScore)
                        .addGap(0, 138, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(fldUnitName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(fldOfficerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(lblScore)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(fldNumSoldiers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(fldNumCavalry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(fldNumArchers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fldNumMages, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblWizards))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void fldUnitNameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_fldUnitNameActionPerformed
    {//GEN-HEADEREND:event_fldUnitNameActionPerformed

    }//GEN-LAST:event_fldUnitNameActionPerformed

    private void fldUnitNameKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_fldUnitNameKeyReleased
    {//GEN-HEADEREND:event_fldUnitNameKeyReleased
        this.unit.setName(this.fldUnitName.getName());
        this.notifyListeners();
    }//GEN-LAST:event_fldUnitNameKeyReleased

    private void fldUnitNameFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_fldUnitNameFocusLost
    {//GEN-HEADEREND:event_fldUnitNameFocusLost

    }//GEN-LAST:event_fldUnitNameFocusLost

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDeleteActionPerformed
    {//GEN-HEADEREND:event_btnDeleteActionPerformed
        this.isDeleted = true;
        this.notifyListeners();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void fldNumSoldiersMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_fldNumSoldiersMouseReleased
    {//GEN-HEADEREND:event_fldNumSoldiersMouseReleased
   
    }//GEN-LAST:event_fldNumSoldiersMouseReleased

    private void fldNumCavalryKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_fldNumCavalryKeyReleased
    {//GEN-HEADEREND:event_fldNumCavalryKeyReleased
        updateUnitScore();
    }//GEN-LAST:event_fldNumCavalryKeyReleased

    private void fldNumArchersKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_fldNumArchersKeyReleased
    {//GEN-HEADEREND:event_fldNumArchersKeyReleased
        updateUnitScore();
    }//GEN-LAST:event_fldNumArchersKeyReleased

    private void fldNumMagesKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_fldNumMagesKeyReleased
    {//GEN-HEADEREND:event_fldNumMagesKeyReleased
        updateUnitScore();
    }//GEN-LAST:event_fldNumMagesKeyReleased

    private void fldNumSoldiersKeyReleased(java.awt.event.KeyEvent evt)//GEN-FIRST:event_fldNumSoldiersKeyReleased
    {//GEN-HEADEREND:event_fldNumSoldiersKeyReleased
         updateUnitScore();
    }//GEN-LAST:event_fldNumSoldiersKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JTextField fldNumArchers;
    private javax.swing.JTextField fldNumCavalry;
    private javax.swing.JTextField fldNumMages;
    private javax.swing.JTextField fldNumSoldiers;
    private javax.swing.JTextField fldOfficerName;
    private javax.swing.JTextField fldUnitName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel lblScore;
    private javax.swing.JLabel lblWizards;
    // End of variables declaration//GEN-END:variables

    @Override
    public void Save()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void Load()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean Validate()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Unit buildObject()
    {
        String allegiance = this.unit.getAllegiance();
        Color color = this.unit.getColor();
        Officer officer = new Officer(allegiance, this.fldOfficerName.getText());
        this.unit.getDefaultFormation().clearPositions();

        Unit newUnit = new Unit(officer, this.unit.getDefaultFormation(), color, this.unit.getAllegiance(), this.fldUnitName.getText());
        newUnit.setOfficer(officer);
        int numSoldiers = Integer.parseInt(this.fldNumSoldiers.getText());
        int numArchers = Integer.parseInt(this.fldNumArchers.getText());
        int numCavalry = Integer.parseInt(this.fldNumCavalry.getText());
        int numWizards = Integer.parseInt(this.fldNumMages.getText());

        int totalNum = numSoldiers + numArchers + numCavalry + numWizards;
        int spacing = (numCavalry >= (totalNum / 3)) ? Cavalry.getPersonalSpace() : Soldier.SOLDIER_PERSONAL_SPACE_RADIUS;
        int numInRow = (int) Math.sqrt(totalNum);
        this.unit = newUnit;
        this.unit.setDefaultFormation(new GridFormation("defaultGrid", this.army.getGeneralPoint(), 0, totalNum, spacing, numInRow));
        this.unit.getDefaultFormation().getOfficersPosition().setObjectInPosition(officer);
        newUnit.clearSoldiers();
        for (int index = 0; index < numSoldiers; index++)
        {
            Soldier soldier = new Soldier(allegiance, "Soldier" + index);
            newUnit.addSoldier(soldier);
        }

        for (int index = 0; index < numArchers; index++)
        {
            Archer soldier = new Archer(allegiance, "Archer" + index);
            newUnit.addSoldier(soldier);
        }

        for (int index = 0; index < numCavalry; index++)
        {
            Cavalry soldier = new Cavalry(allegiance, "Cavalry" + index);
            newUnit.addSoldier(soldier);
        }

        for (int index = 0; index < numWizards; index++)
        {
            Mage mage = new Mage(allegiance, "Wizard" + index);
            newUnit.addSoldier(mage);
        }

        newUnit.finalizeUnitMetrics(0);

        return this.unit;
    }

    @Override
    public void setObject(Unit object)
    {
        this.unit = object;
        this.fldOfficerName.setText(this.unit.getOfficer().getName());
        this.fldUnitName.setText(this.unit.getName());

        int numSoldiers = 0;
        int numArchers = 0;
        int numCavalry = 0;
        int numWizards = 0;

        for (Soldier soldier : this.unit.getSoldiers())
        {
            if (Archer.class.isAssignableFrom(soldier.getClass()))
            {
                numArchers++;
            } else if (Cavalry.class.isAssignableFrom(soldier.getClass()))
            {
                numCavalry++;
            } else if (Mage.class.isAssignableFrom(soldier.getClass()))
            {
                numWizards++;
            } else
            {
                numSoldiers++;
            }
        }

        this.fldNumArchers.setText("" + numArchers);
        this.fldNumCavalry.setText("" + numCavalry);
        this.fldNumSoldiers.setText("" + numSoldiers);
        this.fldNumMages.setText("" + numWizards);

        updateUnitScore();
    }

    public void updateUnitScore()
    {
        int numSoldiers = 0;
        int numArchers = 0;
        int numCavalry = 0;
        int numWizards = 0;
        try
        {
            numSoldiers = Integer.parseInt(this.fldNumSoldiers.getText());
            numArchers = Integer.parseInt(this.fldNumArchers.getText());
            numCavalry = Integer.parseInt(this.fldNumCavalry.getText());
            numWizards = Integer.parseInt(this.fldNumMages.getText());

        } catch (NumberFormatException ex)
        {
            System.out.println("Bad Number");
        }

        this.unitScore = Officer.OFFICER_CLASS_SCORE +
                numSoldiers * Soldier.SOLDIER_CLASS_SCORE
                + numArchers * Archer.ARCHER_CLASS_SCORE
                + numCavalry * Cavalry.CAVALRY_CLASS_SCORE
                + numWizards * Mage.MAGE_CLASS_SCORE;
        this.lblScore.setText("" + this.unitScore);
        this.invalidate();
        this.notifyListeners();
    }

    public int getUnitScore()
    {
        return unitScore;
    }
    
    

    public String getUnitName()
    {
        if(this.unit != null)
        {
        return this.unit.getName();
        }
        else
        {
            return this.getCurrentUnitName();
        }
    }

    public String getCurrentUnitName()
    {
        return this.fldUnitName.getText();
    }
}
