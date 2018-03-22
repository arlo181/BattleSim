/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;


import battlesimulator.Geography.Battlefield;
import battlesimulator.Military.Army;
import battlesimulator.Military.Army.ArmyStats;
import battlesimulator.SimulationEngine;
import com.thebuzzmedia.imgscalr.Scalr;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.JFrame;



/**
 *
 * @author Arlo
 */
public class ViewSimFrame extends javax.swing.JFrame
{

    final double TENTH_SCALE = 0.1;
    final double QUARTER_SCALE = 0.25;
    final double HALF_SCALE = 0.5;
    final double FULL_SCALE = 1.0;
    final double DOUBLE_SCALE = 2.0;
    final double ONE_AND_HALF_SCALE = 1.5;

    final int MAX_SCALE = 5;

    SimulationEngine engine;
    int drawPointX;
    int drawPointY;
    int clickPointX;
    int clickPointY;
    int drawPointAtClickX;
    int drawPointAtClickY;
    boolean dragging = false;
    HashMap<String, ArmyStatsPanel> statsPanels;
    int notches;
    double scale;

    /**
     * Creates new form ViewSimFrame
     */
    public ViewSimFrame(SimulationEngine engine)
    {
        initComponents();
        this.engine = engine;
        this.drawPointX = 0;
        this.drawPointY = 0;
        this.statsPanels = new HashMap<>();
        this.scale = FULL_SCALE;
        this.notches = 3;

        for (Army army : this.engine.getArmies())
        {
            ArmyStatsPanel panel = new ArmyStatsPanel(army.getColor());

            this.statsPanels.put(army.getAllegiance(), panel);
            this.StatsPanel.add(panel);
        }

    }

    public void updateSlider(int sceneNumber, int maxScenes)
    {
        this.SimSlider.setMaximum(maxScenes);
        this.SimSlider.setValue(sceneNumber);
    }

    public void nextScene()
    {
        int value = this.SimSlider.getValue();
        if (value < this.SimSlider.getMaximum())
        {
            this.SimSlider.setValue(value + 1);
        }
    }

    public void previousScene()
    {
        int value = this.SimSlider.getValue();
        if (value > 0)
        {
            this.SimSlider.setValue(value - 1);
        }
    }

    public void updateMaxScenes(int maxScenes)
    {
        this.SimSlider.setMaximum(maxScenes);
    }

    public void setBattlefield(Battlefield battlefield)
    {

        battlefieldPanel.setBounds(0, 0, battlefield.getPixelsWidth() + 40, battlefield.getPixelsHeight() + 40);

        int width = battlefield.getPixelsWidth() + 100;
        int height = battlefield.getPixelsHeight() + 100;
        Dimension dim = new Dimension(width, height);
        battlefieldPanel.setMinimumSize(dim);
        battlefieldPanel.setMaximumSize(dim);
        battlefieldPanel.setPreferredSize(dim);
    }
public void clearScreen()
{
    this.battlefieldPanel.removeAll();
    this.battlefieldPanel.repaint();

}
    public void drawScene(int sceneNumber)
    {
        if (sceneNumber < SimulationEngine.MAX_LOOPS)
        {
            BufferedImage imageToDraw = this.engine.paintBattlefield(sceneNumber);

            int newWidth = (int) (imageToDraw.getWidth() * this.scale);
            int newHeight = (int) (imageToDraw.getHeight() * this.scale);

            imageToDraw = Scalr.resize(imageToDraw, Scalr.Method.BALANCED, newWidth, newHeight);
//TODO: shift the scene to match the relative location of the mouse during the scroll...
            battlefieldPanel.getGraphics().drawImage(imageToDraw, this.drawPointX, this.drawPointY, null);
            for (ArmyStats stats : this.engine.getArmyStatsForTick(sceneNumber))
            {
                this.statsPanels.get(stats.getAllegiance()).setStats(stats);
            }
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

        StatsScroolPane = new javax.swing.JScrollPane();
        StatsPanel = new javax.swing.JPanel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        battlefieldPanel = new javax.swing.JPanel();
        ControlPanel = new javax.swing.JPanel();
        SimSlider = new javax.swing.JSlider();
        sceneLabel = new javax.swing.JLabel();
        playPauseButton = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        btnBack = new javax.swing.JButton();
        btnStep = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnuSaveRecording = new javax.swing.JMenuItem();
        mnuReturnToTitle = new javax.swing.JMenuItem();
        mnuDoneWithSim = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setExtendedState(this.getExtendedState()|JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new java.awt.Dimension(200, 200));
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosed(java.awt.event.WindowEvent evt)
            {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                formWindowClosing(evt);
            }
        });

        StatsScroolPane.setViewportView(StatsPanel);

        getContentPane().add(StatsScroolPane, java.awt.BorderLayout.PAGE_START);

        battlefieldPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        battlefieldPanel.setMinimumSize(new java.awt.Dimension(100, 100));
        battlefieldPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
        {
            public void mouseDragged(java.awt.event.MouseEvent evt)
            {
                battlefieldPanelMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt)
            {
                battlefieldPanelMouseMoved(evt);
            }
        });
        battlefieldPanel.addMouseWheelListener(new java.awt.event.MouseWheelListener()
        {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt)
            {
                battlefieldPanelMouseWheelMoved(evt);
            }
        });
        battlefieldPanel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                battlefieldPanelMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                battlefieldPanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                battlefieldPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout battlefieldPanelLayout = new javax.swing.GroupLayout(battlefieldPanel);
        battlefieldPanel.setLayout(battlefieldPanelLayout);
        battlefieldPanelLayout.setHorizontalGroup(
            battlefieldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 762, Short.MAX_VALUE)
        );
        battlefieldPanelLayout.setVerticalGroup(
            battlefieldPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 230, Short.MAX_VALUE)
        );

        jLayeredPane1.add(battlefieldPanel);
        battlefieldPanel.setBounds(0, 1, 762, 230);

        getContentPane().add(jLayeredPane1, java.awt.BorderLayout.CENTER);

        ControlPanel.setMaximumSize(new java.awt.Dimension(751, 89));
        ControlPanel.setMinimumSize(new java.awt.Dimension(751, 89));
        ControlPanel.setLayout(new java.awt.BorderLayout());

        SimSlider.setMaximum(1);
        SimSlider.setValue(0);
        SimSlider.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                SimSliderMouseReleased(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt)
            {
                SimSliderMousePressed(evt);
            }
        });
        SimSlider.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                SimSliderStateChanged(evt);
            }
        });
        ControlPanel.add(SimSlider, java.awt.BorderLayout.NORTH);

        sceneLabel.setText("0000/0000");
        sceneLabel.setMaximumSize(new java.awt.Dimension(52, 10));
        sceneLabel.setPreferredSize(new java.awt.Dimension(60, 14));
        ControlPanel.add(sceneLabel, java.awt.BorderLayout.LINE_START);

        playPauseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/1495345483_pause-circle-outline.png"))); // NOI18N
        playPauseButton.setToolTipText("");
        playPauseButton.setBorder(null);
        playPauseButton.setBorderPainted(false);
        playPauseButton.setContentAreaFilled(false);
        playPauseButton.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/pause-icon.png"))); // NOI18N
        playPauseButton.setFocusPainted(false);
        playPauseButton.setRolloverEnabled(false);
        playPauseButton.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/1495346093_play-circle-outline.png"))); // NOI18N
        playPauseButton.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                playPauseButtonStateChanged(evt);
            }
        });
        playPauseButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                playPauseButtonActionPerformed(evt);
            }
        });
        ControlPanel.add(playPauseButton, java.awt.BorderLayout.CENTER);

        jPanel1.setMaximumSize(new java.awt.Dimension(50, 50));
        jPanel1.setMinimumSize(new java.awt.Dimension(50, 50));
        jPanel1.setPreferredSize(new java.awt.Dimension(90, 10));
        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        btnBack.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnBack.setText("<<");
        btnBack.setMaximumSize(new java.awt.Dimension(25, 25));
        btnBack.setMinimumSize(new java.awt.Dimension(25, 25));
        btnBack.setPreferredSize(new java.awt.Dimension(25, 25));
        btnBack.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnBackActionPerformed(evt);
            }
        });
        jPanel1.add(btnBack);

        btnStep.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        btnStep.setText(">>");
        btnStep.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnStepActionPerformed(evt);
            }
        });
        jPanel1.add(btnStep);

        ControlPanel.add(jPanel1, java.awt.BorderLayout.LINE_END);

        getContentPane().add(ControlPanel, java.awt.BorderLayout.SOUTH);

        jMenu1.setText("File");

        mnuSaveRecording.setText("Save Recording");
        mnuSaveRecording.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mnuSaveRecordingActionPerformed(evt);
            }
        });
        jMenu1.add(mnuSaveRecording);

        mnuReturnToTitle.setText("Return To Title");
        mnuReturnToTitle.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mnuReturnToTitleActionPerformed(evt);
            }
        });
        jMenu1.add(mnuReturnToTitle);

        mnuDoneWithSim.setText("Done With Sim");
        mnuDoneWithSim.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mnuDoneWithSimActionPerformed(evt);
            }
        });
        jMenu1.add(mnuDoneWithSim);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        System.exit(0);
    }//GEN-LAST:event_formWindowClosed

    private void SimSliderMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SimSliderMousePressed
        this.engine.pauseAnimation(true);
    }//GEN-LAST:event_SimSliderMousePressed

    private void SimSliderMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SimSliderMouseReleased
        this.engine.pauseAnimation(this.playPauseButton.isSelected()); //todo:  or the state of the pause button, when I add one
    }//GEN-LAST:event_SimSliderMouseReleased

    private void SimSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_SimSliderStateChanged
        this.drawScene(this.SimSlider.getValue());
        this.engine.setSceneNumber(this.SimSlider.getValue());
        this.sceneLabel.setText(this.SimSlider.getValue() + "/" + this.SimSlider.getMaximum());
        this.btnStep.setEnabled(this.SimSlider.getValue() != this.SimSlider.getMaximum());
        this.btnBack.setEnabled(this.SimSlider.getValue() > 0);
    }//GEN-LAST:event_SimSliderStateChanged

    private void playPauseButtonStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_playPauseButtonStateChanged
        this.engine.pauseAnimation(this.playPauseButton.isSelected());
    }//GEN-LAST:event_playPauseButtonStateChanged

    private void playPauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playPauseButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_playPauseButtonActionPerformed

    private void battlefieldPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_battlefieldPanelMouseClicked
    }//GEN-LAST:event_battlefieldPanelMouseClicked

    private void battlefieldPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_battlefieldPanelMousePressed
        this.clickPointX = evt.getX();
        this.clickPointY = evt.getY();
        this.drawPointAtClickX = this.drawPointX;
        this.drawPointAtClickY = this.drawPointY;
        this.dragging = true;
    }//GEN-LAST:event_battlefieldPanelMousePressed

    private void battlefieldPanelMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_battlefieldPanelMouseMoved
    }//GEN-LAST:event_battlefieldPanelMouseMoved

    private void battlefieldPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_battlefieldPanelMouseReleased
        this.dragging = false;
        this.battlefieldPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        //this.battlefieldPanel.repaint();
        this.battlefieldPanel.getGraphics().clearRect(0, 0, this.battlefieldPanel.getWidth(), this.battlefieldPanel.getHeight());
        this.battlefieldPanel.invalidate();
        this.drawScene(this.SimSlider.getValue());
    }//GEN-LAST:event_battlefieldPanelMouseReleased

    private void battlefieldPanelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_battlefieldPanelMouseDragged
        if (dragging)
        {
            this.battlefieldPanel.setCursor(new Cursor(Cursor.MOVE_CURSOR));
            int diffX = evt.getX() - clickPointX;
            int diffY = evt.getY() - clickPointY;

            this.drawPointX = drawPointAtClickX + diffX;
            this.drawPointY = drawPointAtClickY + diffY;

            this.drawScene(this.SimSlider.getValue());
        }
    }//GEN-LAST:event_battlefieldPanelMouseDragged

    private void btnStepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStepActionPerformed
        // TODO add your handling code here:
        this.nextScene();
    }//GEN-LAST:event_btnStepActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        this.previousScene();
    }//GEN-LAST:event_btnBackActionPerformed

    private void battlefieldPanelMouseWheelMoved(java.awt.event.MouseWheelEvent evt)//GEN-FIRST:event_battlefieldPanelMouseWheelMoved
    {//GEN-HEADEREND:event_battlefieldPanelMouseWheelMoved
        // TODO add your handling code here:
        int scrollNotches = evt.getWheelRotation();
        if (scrollNotches < 0)
        {
            this.notches++;

        } else
        {
            this.notches--;
        }

        if (this.notches > MAX_SCALE)
        {
            this.notches = MAX_SCALE;
        }
        if (this.notches < 0)
        {
            this.notches = 0;
        }

        switch (this.notches)
        {
            case 0:
                this.scale = TENTH_SCALE;
                break;
            case 1:
                this.scale = QUARTER_SCALE;
                break;
            case 2:
                this.scale = HALF_SCALE;
                break;
            case 3:
                this.scale = FULL_SCALE;
                break;
            case 4:
                this.scale = ONE_AND_HALF_SCALE;
                break;
            case 5:
                this.scale = DOUBLE_SCALE;
                break;

        }
        System.out.println("Notches: " + this.notches + " scroll: " + scrollNotches);
        this.drawPointX = 0;
        this.drawPointY = 0;
this.clearScreen();
        this.drawScene(this.SimSlider.getValue());
                


    }//GEN-LAST:event_battlefieldPanelMouseWheelMoved

    private void mnuSaveRecordingActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mnuSaveRecordingActionPerformed
    {//GEN-HEADEREND:event_mnuSaveRecordingActionPerformed

          this.engine.paintSceneToFileInBatches();
  
    }//GEN-LAST:event_mnuSaveRecordingActionPerformed

    private void mnuReturnToTitleActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mnuReturnToTitleActionPerformed
    {//GEN-HEADEREND:event_mnuReturnToTitleActionPerformed
            
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new TitleScreen().setVisible(true);
            }
        });
        
         this.engine.killSimulation();
    }//GEN-LAST:event_mnuReturnToTitleActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
       this.engine.killSimulation();
    }//GEN-LAST:event_formWindowClosing

    private void mnuDoneWithSimActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mnuDoneWithSimActionPerformed
    {//GEN-HEADEREND:event_mnuDoneWithSimActionPerformed
        this.engine.killSimulation();
    }//GEN-LAST:event_mnuDoneWithSimActionPerformed
 
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ControlPanel;
    private javax.swing.JSlider SimSlider;
    private javax.swing.JPanel StatsPanel;
    private javax.swing.JScrollPane StatsScroolPane;
    private javax.swing.JPanel battlefieldPanel;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnStep;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JMenuItem mnuDoneWithSim;
    private javax.swing.JMenuItem mnuReturnToTitle;
    private javax.swing.JMenuItem mnuSaveRecording;
    private javax.swing.JToggleButton playPauseButton;
    private javax.swing.JLabel sceneLabel;
    // End of variables declaration//GEN-END:variables
}