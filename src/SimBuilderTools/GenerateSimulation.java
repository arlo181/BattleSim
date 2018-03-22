/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SimBuilderTools;

import GUI.TitleScreen;

import battlesimulator.Geography.Battlefield;
import battlesimulator.Military.Army;
import battlesimulator.SimulationSeed;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author Arlo
 */
public class GenerateSimulation extends javax.swing.JFrame implements CreatorListener
{

    SimulationSeed seed;
    TitleScreen titleScreen;
    ArrayList<CreateArmy> armyList = new ArrayList<>();

    public void clearAll()
    {

        armyList.clear();
        //this.battlefieldPanel = new CreateBattlefield();

        this.armiesTabbedPane.removeAll();
    }

    @Override
    public void updateGUI()
    {
        Battlefield field = this.battlefieldPanel.buildObject();
        this.seed.setBattlefield(field);

        for (int index = 0; index < this.armiesTabbedPane.getTabCount(); index++)
        {
            CreateArmy armyOnTab = (CreateArmy) this.armiesTabbedPane.getComponentAt(index);
            this.armiesTabbedPane.setTitleAt(index, armyOnTab.getArmyName());
            armyOnTab.setStartingPositions(field.getStartingPositions());

            if (armyOnTab.isDeleted())
            {
                this.armyList.remove(armyOnTab);
                this.armiesTabbedPane.remove(index);
            }

        }

        this.revalidate();
    }

    public enum GenerationStage
    {

        BATTLEFIELD,
        ARMIES,
        ARMY,
        UNIT
    }

    /**
     * Creates new form GenerateSimulation
     */
    public GenerateSimulation(TitleScreen titleScreen)
    {
        initComponents();
        this.seed = new SimulationSeed();
        this.battlefieldPanel.addListener(this);
        this.titleScreen = titleScreen;

    }

    public void setSeed(SimulationSeed seed)
    {
        this.seed = seed;
        this.battlefieldPanel.setObject(seed.getBattlefield());
        this.armyList.clear();

        for (Army army : seed.getArmies())
        {
            CreateArmy createArmy = new CreateArmy();
            // createArmy.setStartingPositions(this.battlefieldPanel.);
            createArmy.setObject(army);
            createArmy.setStartingPositions(this.seed.getBattlefield().getStartingPositions());
            this.armiesTabbedPane.addTab(army.getAllegiance(), createArmy);
            this.armyList.add(createArmy);
            createArmy.addListener(this);

        }

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

        jScrollPane2 = new javax.swing.JScrollPane();
        ConfigurationPanel = new javax.swing.JPanel();
        battlefieldPanel = new SimBuilderTools.CreateBattlefield();
        armiesTabbedPane = new javax.swing.JTabbedPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        ControlPanel = new javax.swing.JPanel();
        btnPrevious = new javax.swing.JButton();
        btnAddArmy = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        ConfigurationPanel.setBorder(new javax.swing.border.MatteBorder(null));

        armiesTabbedPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Armies");

        jLabel2.setText("Battlefield");

        javax.swing.GroupLayout ConfigurationPanelLayout = new javax.swing.GroupLayout(ConfigurationPanel);
        ConfigurationPanel.setLayout(ConfigurationPanelLayout);
        ConfigurationPanelLayout.setHorizontalGroup(
            ConfigurationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ConfigurationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ConfigurationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(battlefieldPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ConfigurationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ConfigurationPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(1068, Short.MAX_VALUE))
                    .addGroup(ConfigurationPanelLayout.createSequentialGroup()
                        .addComponent(armiesTabbedPane)
                        .addContainerGap(1057, Short.MAX_VALUE))))
        );
        ConfigurationPanelLayout.setVerticalGroup(
            ConfigurationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ConfigurationPanelLayout.createSequentialGroup()
                .addGroup(ConfigurationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(ConfigurationPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ConfigurationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(armiesTabbedPane)
                    .addComponent(battlefieldPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(ConfigurationPanel);

        getContentPane().add(jScrollPane2);

        ControlPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnPrevious.setText("Finish");
        btnPrevious.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPreviousActionPerformed(evt);
            }
        });

        btnAddArmy.setText("AddArmy");
        btnAddArmy.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnAddArmyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ControlPanelLayout = new javax.swing.GroupLayout(ControlPanel);
        ControlPanel.setLayout(ControlPanelLayout);
        ControlPanelLayout.setHorizontalGroup(
            ControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPrevious)
                .addGap(268, 268, 268)
                .addComponent(btnAddArmy)
                .addContainerGap(1169, Short.MAX_VALUE))
        );
        ControlPanelLayout.setVerticalGroup(
            ControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ControlPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(ControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrevious)
                    .addComponent(btnAddArmy))
                .addContainerGap())
        );

        getContentPane().add(ControlPanel);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddArmyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddArmyActionPerformed
        int armyNameSuffix = 0;
        String armyName = "army" + armyNameSuffix;
        boolean uniqueName = false;
        while (!uniqueName)
        {
            uniqueName = true;
            armyName = "army" + armyNameSuffix;
            for (CreateArmy createArmy : this.armyList)
            {
                uniqueName &= (!createArmy.getArmyName().equals(armyName));
            }

            armyNameSuffix++;
        }
        CreateArmy createArmy = new CreateArmy(armyName);
        createArmy.addListener(this);
        this.armiesTabbedPane.addTab(createArmy.getArmyName(), createArmy);
        this.armyList.add(createArmy);
        createArmy.setStartingPositions(this.battlefieldPanel.buildObject().getStartingPositions());


    }//GEN-LAST:event_btnAddArmyActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        // TODO add your handling code here:
        ArrayList<Army> armies = new ArrayList<>();

        for (CreateArmy createArmy : this.armyList)
        {
            armies.add(createArmy.buildObject());
        }
        this.seed.setArmies(armies);

        Battlefield field = this.battlefieldPanel.buildObject();
        for (Army army : this.seed.getArmies())
        {
            field.assignArmyStartingPoint(army.getStartingPositionName(), army);
        }

        this.seed.setBattlefield(field);
        this.titleScreen.setSeed(this.seed);
        this.setVisible(false);
        this.titleScreen.setVisible(true);

        this.ConfigurationPanel.repaint();
    }//GEN-LAST:event_btnPreviousActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(GenerateSimulation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(GenerateSimulation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(GenerateSimulation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(GenerateSimulation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ConfigurationPanel;
    private javax.swing.JPanel ControlPanel;
    private javax.swing.JTabbedPane armiesTabbedPane;
    private SimBuilderTools.CreateBattlefield battlefieldPanel;
    private javax.swing.JButton btnAddArmy;
    private javax.swing.JButton btnPrevious;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
