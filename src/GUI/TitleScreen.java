/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import SimBuilderTools.GenerateSimulation;
import SimBuilderTools.QuickArmyBuildParser;
import Tournament.TourneyManager;
import Tournament.TourneyScreen;
import battlesimulator.BattleSimulator;
import battlesimulator.SimulationEngine;
import battlesimulator.SimulationSeed;
import java.awt.Cursor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

/**
 *
 * @author Arlo
 */
public class TitleScreen extends javax.swing.JFrame
{

    SimulationSeed seed;
    GenerateSimulation genSim;

    /**
     * Creates new form TitleScreen
     */
    public TitleScreen()
    {
        initComponents();
        btnStartSim.setEnabled(seed != null);
        btnEditLoaded.setEnabled(seed != null);
        genSim = new GenerateSimulation(this);
        genSim.setVisible(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void setSeed(SimulationSeed seed)
    {
        this.seed = seed;
        this.setCursor(Cursor.DEFAULT_CURSOR);
        btnCreateNew.setEnabled(true);
        btnLoadLast.setEnabled(true);
        btnLoadSim.setEnabled(true);
        btnStartSim.setEnabled(seed != null);
        btnEditLoaded.setEnabled(seed != null);
        btnSave.setEnabled(seed != null);
        lblLoadedSim.setText("custom Sim");
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
        btnLoadLast = new javax.swing.JButton();
        btnLoadSim = new javax.swing.JButton();
        btnCreateNew = new javax.swing.JButton();
        btnStartSim = new javax.swing.JButton();
        lblLoadedSim = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnEditLoaded = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        checkRecordOnly = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        fldScale = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btnQuickStart = new javax.swing.JButton();
        btnTournament = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Welcome to Battle Sim");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        btnLoadLast.setText("Use Default Sim");
        btnLoadLast.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnLoadLastActionPerformed(evt);
            }
        });

        btnLoadSim.setText("Load Sim");
        btnLoadSim.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnLoadSimActionPerformed(evt);
            }
        });

        btnCreateNew.setText("Create New Sim");
        btnCreateNew.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCreateNewActionPerformed(evt);
            }
        });

        btnStartSim.setText("Start");
        btnStartSim.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnStartSimActionPerformed(evt);
            }
        });

        lblLoadedSim.setText("none loaded");

        jLabel2.setText("Simulation:");

        btnEditLoaded.setText("Edit Loaded Sim");
        btnEditLoaded.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnEditLoadedActionPerformed(evt);
            }
        });

        btnSave.setText("Save Loaded Sim");
        btnSave.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSaveActionPerformed(evt);
            }
        });

        checkRecordOnly.setText("Record to Image file only");

        jLabel3.setText("ScaleForImages");

        fldScale.setText("100");

        jLabel4.setText("%");

        btnQuickStart.setText("Quick Start");
        btnQuickStart.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnQuickStartActionPerformed(evt);
            }
        });

        btnTournament.setText("Tournament!");
        btnTournament.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnTournamentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(126, 126, 126)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnStartSim, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(lblLoadedSim))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(checkRecordOnly)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fldScale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel4))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(btnLoadLast)
                                            .addComponent(btnQuickStart))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(btnEditLoaded)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnLoadSim)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnCreateNew))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnSave)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                                                .addComponent(btnTournament)))))))
                        .addGap(20, 20, 20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLoadedSim)
                    .addComponent(jLabel2))
                .addGap(21, 21, 21)
                .addComponent(btnStartSim, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkRecordOnly)
                    .addComponent(jLabel3)
                    .addComponent(fldScale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLoadLast)
                    .addComponent(btnLoadSim)
                    .addComponent(btnCreateNew))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEditLoaded)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSave)
                        .addContainerGap(30, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnQuickStart)
                            .addComponent(btnTournament))
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void beginLoading()
    {
        lblLoadedSim.setText("Loading...");
        this.setCursor(Cursor.WAIT_CURSOR);
        btnCreateNew.setEnabled(false);
        btnLoadLast.setEnabled(false);
        btnLoadSim.setEnabled(false);
        btnStartSim.setEnabled(false);
        btnEditLoaded.setEnabled(false);
        btnSave.setEnabled(false);
    }

    public void beginSaving()
    {
        lblLoadedSim.setText("Saving...");
        this.setCursor(Cursor.WAIT_CURSOR);
        btnCreateNew.setEnabled(false);
        btnLoadLast.setEnabled(false);
        btnLoadSim.setEnabled(false);
        btnStartSim.setEnabled(false);
        btnEditLoaded.setEnabled(false);
        btnSave.setEnabled(false);
    }

    public void loadingFinished(boolean success, String simName)
    {
        if (success)
        {
            lblLoadedSim.setText(simName);
        } else
        {
            lblLoadedSim.setText("Load Failed.");
        }

        this.setCursor(Cursor.DEFAULT_CURSOR);
        btnCreateNew.setEnabled(true);
        btnLoadLast.setEnabled(true);
        btnLoadSim.setEnabled(true);
        btnStartSim.setEnabled(seed != null);
        btnEditLoaded.setEnabled(seed != null);
        btnSave.setEnabled(seed != null);
    }

    public void savingFinished(boolean success, String simName)
    {

        this.setCursor(Cursor.DEFAULT_CURSOR);
        btnCreateNew.setEnabled(true);
        btnLoadLast.setEnabled(true);
        btnLoadSim.setEnabled(true);
        btnStartSim.setEnabled(seed != null);
        btnEditLoaded.setEnabled(seed != null);
        btnSave.setEnabled(seed != null);
    }

    private void btnLoadSimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadSimActionPerformed
        // TODO add your handling code here:

        loadSimulation();


    }//GEN-LAST:event_btnLoadSimActionPerformed

    private void loadSimulation()
    {

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "\\SavedData\\Seeds"));
        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            final File file = fileChooser.getSelectedFile();
            this.beginLoading();
            SwingWorker<String, Void> worker = new SwingWorker()
            {
                @Override
                protected String doInBackground() throws Exception
                {
                    String simName = null;

                    //This is where a real application would open the file.
                    try
                    {
                        FileInputStream fileIn = new FileInputStream(file);
                        ObjectInputStream in = new ObjectInputStream(fileIn);

                        seed = (SimulationSeed) in.readObject();

                        in.close();
                        fileIn.close();
                        simName = file.getName();
                    } catch (IOException i)
                    {
                        i.printStackTrace();

                    } catch (ClassNotFoundException c)
                    {
                        c.printStackTrace();

                    } catch (Exception c)
                    {
                        c.printStackTrace();

                    } finally
                    {
                    }
                    return simName;
                }

                @Override
                protected void done()
                {
                    try
                    {
                        //get the data fetched above, in doInBackground()
                        String simName = (String) get();

                        loadingFinished((simName != null), simName);

                    } catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            };

            worker.execute();
        }
    }

    public void saveSimulation()
    {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "\\SavedData\\Seeds"));
        int returnVal = fileChooser.showSaveDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            final File file = fileChooser.getSelectedFile();
            this.beginSaving();
            SwingWorker<String, Void> worker = new SwingWorker()
            {
                @Override
                protected String doInBackground() throws Exception
                {
                    String simName = null;

                    //This is where a real application would open the file.
                    try
                    {
                        FileOutputStream fileOut = new FileOutputStream(file);
                        ObjectOutputStream out = new ObjectOutputStream(fileOut);
                        out.writeObject(seed);

                        out.close();
                        fileOut.close();
                        simName = file.getName();
                    } catch (IOException i)
                    {
                        i.printStackTrace();

                    } catch (Exception c)
                    {
                        c.printStackTrace();

                    } finally
                    {
                    }
                    return simName;
                }

                @Override
                protected void done()
                {
                    try
                    {
                        //get the data fetched above, in doInBackground()
                        String simName = (String) get();

                        loadingFinished((simName != null), simName);

                    } catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            };

            worker.execute();
        }
    }

    public void returnToTitleScreen()
    {
        this.setVisible(true);
    }

    private void btnStartSimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartSimActionPerformed
        // TODO add your handling code here:

        boolean recordOnly = this.checkRecordOnly.isSelected();
        
        this.beginLoading();
        this.setCursor(Cursor.WAIT_CURSOR);
        SwingWorker worker = new SwingWorker()
        {
            @Override
            protected Object doInBackground() throws Exception
            {
                if (seed != null)
                {
                    try
                    {
                        int scale = Integer.parseInt(fldScale.getText());
                        SimulationEngine engine = new SimulationEngine(seed, 10, recordOnly, false, true, scale); //test
                        engine.startSimulation();
                    } catch (Exception ex)
                    {
                        ex.printStackTrace();
                        System.out.println("EXCEPTION!!!");
                        System.exit(1);
                    }
                } else
                {
                }
                return null;
            }

            @Override
            protected void done()
            {
                setCursor(Cursor.DEFAULT_CURSOR);
            }
        };

        worker.execute();

    }//GEN-LAST:event_btnStartSimActionPerformed

    private void btnCreateNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateNewActionPerformed
        enterSimGenerations();
    }//GEN-LAST:event_btnCreateNewActionPerformed

    private void btnEditLoadedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditLoadedActionPerformed
        enterSimGenerations(this.seed);
    }//GEN-LAST:event_btnEditLoadedActionPerformed

    private void btnLoadLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadLastActionPerformed
        // TODO add your handling code here:

        this.beginLoading();
        SwingWorker<String, Void> worker = new SwingWorker()
        {
            @Override
            protected String doInBackground() throws Exception
            {
                String simName = null;

                //This is where a real application would open the file.
                try
                {
                    seed = BattleSimulator.generateTestSimulationSeedMultipleOfficers(1000);
                    simName = "Default Sim";
                } catch (Exception i)
                {
                    i.printStackTrace();

                }
                return simName;
            }

            @Override
            protected void done()
            {
                try
                {
                    //get the data fetched above, in doInBackground()
                    String simName = (String) get();

                    loadingFinished((simName != null), simName);

                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        };

        worker.execute();
    }//GEN-LAST:event_btnLoadLastActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        saveSimulation();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnQuickStartActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnQuickStartActionPerformed
    {//GEN-HEADEREND:event_btnQuickStartActionPerformed
        QuickArmyBuildParser parser = new QuickArmyBuildParser();

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "\\SavedData\\QuickStart\\SeedFiles\\"));
        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            final File file = fileChooser.getSelectedFile();

            this.seed = parser.buildSeedFromFile(file.getAbsolutePath());
            loadingFinished(true, file.getName());
        }
    }//GEN-LAST:event_btnQuickStartActionPerformed

    private void btnTournamentActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnTournamentActionPerformed
    {//GEN-HEADEREND:event_btnTournamentActionPerformed
this.lblLoadedSim.setText ("TOURNAMENT!!!");
       TourneyScreen screen = new TourneyScreen();
       screen.setVisible(true);

    }//GEN-LAST:event_btnTournamentActionPerformed

    public void enterSimGenerations()
    {
        this.genSim.clearAll();
        this.genSim.setVisible(true);
        this.setVisible(true);
    }

    public void enterSimGenerations(SimulationSeed seed)
    {
        this.genSim.clearAll();
        this.enterSimGenerations();
        this.genSim.setSeed(seed);
    }

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
           ex.printStackTrace();
            java.util.logging.Logger.getLogger(TitleScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex)
        {
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(TitleScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new TitleScreen().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCreateNew;
    private javax.swing.JButton btnEditLoaded;
    private javax.swing.JButton btnLoadLast;
    private javax.swing.JButton btnLoadSim;
    private javax.swing.JButton btnQuickStart;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnStartSim;
    private javax.swing.JButton btnTournament;
    private javax.swing.JCheckBox checkRecordOnly;
    private javax.swing.JTextField fldScale;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lblLoadedSim;
    // End of variables declaration//GEN-END:variables
}