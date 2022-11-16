package src.GUI;

import src.Constant;
import src.Highway.Highway;
import src.Vehicles.Vehicle;

        import javax.swing.*;
        import javax.swing.Timer;
        import java.awt.*;
        import java.awt.event.ActionEvent;
        import java.awt.event.ActionListener;
        import java.awt.geom.Line2D;
        import java.awt.geom.Point2D;
        import java.awt.geom.Rectangle2D;

public class HighwayGUI extends JFrame{
    HPanel panel;
    int updateTimeInMilisec = 1;

    public HighwayGUI(){
        this.setTitle("Highway");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        panel = new HPanel();

        this.getContentPane().add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public class HPanel extends JPanel implements ActionListener{
        Highway highway;
        Timer timer;
        JButton pauseButton;
        JButton resumeButton;

        HPanel(){

            creatingButtons();
            this.setPreferredSize(new Dimension(Constant.PANEL_WIDTH,Constant.PANEL_HEIGHT));
            this.setBackground(Color.white);

            //Start timer
            timer = new Timer(updateTimeInMilisec,this);
            timer.start();
        }

        private void creatingButtons(){

            Image pauseTemp = new ImageIcon("pauseButton.jpg").getImage().getScaledInstance(25,25,Image.SCALE_DEFAULT);
            Image resumeTemp = new ImageIcon("resumeButton.jpg").getImage().getScaledInstance(25,25,Image.SCALE_DEFAULT);

            ImageIcon pause = new ImageIcon(pauseTemp);
            ImageIcon resume = new ImageIcon(resumeTemp);

            pauseButton = new JButton("Pause", pause);
            pauseButton.addActionListener(this);
            resumeButton = new JButton("Resume", resume);
            resumeButton.addActionListener(this);

            FlowLayout layout = new FlowLayout();
            this.setLayout(layout);
            this.add(pauseButton);
            this.add(resumeButton);
        }
        @Override
        public void paintComponent(Graphics graphics){
            super.paintComponent(graphics);
            Graphics2D g2D = (Graphics2D)  graphics;

            //Drawing a highway
            float heightOfLane = (float) Constant.PANEL_HEIGHT / highway.getNumberOfLanes();
            paintLanesOfTraffic(graphics, heightOfLane);

            //Drawing the vehicles
            for (Vehicle vehicle : highway.getVehicles()) {

                float width = vehicle.getParams().length;
                float height = heightOfLane / 2;

                Rectangle2D.Float rect1 = new Rectangle2D.Float(vehicle.getParams().position, heightOfLane * (vehicle.getParams().laneOfTraffic - 1) + heightOfLane / 4, width, height);
                if(vehicle.isOnRoad) {
                    //Painting a car
                    g2D.setPaint(vehicle.getParams().color);
                    g2D.fill(rect1);
                    //Drawing a model information
                    g2D.setPaint(Color.WHITE);
                    g2D.drawString(vehicle.getParams().model, vehicle.getParams().position + (width - vehicle.getParams().model.length()) / 4, heightOfLane * (vehicle.getParams().laneOfTraffic - 1) + heightOfLane / 2);
                }
            }
        }

        private void paintLanesOfTraffic(Graphics graphics, float heightOfLane) {
            Graphics2D g2D = (Graphics2D) graphics;
            //Drawing a highway
            float yPoint = 0.f;
            for (int i = 0; i < highway.getNumberOfLanes(); i++) {
                Point2D.Float linep1 = new Point2D.Float(0.f, yPoint);
                Point2D.Float linep2 = new Point2D.Float(Constant.PANEL_WIDTH, yPoint);
                g2D.setPaint(Color.BLACK);
                g2D.draw(new Line2D.Float(linep1, linep2));
                yPoint = yPoint + heightOfLane;
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == pauseButton){
                timer.stop();
            }else if(e.getSource() == resumeButton){
                timer.start();
            }else {
                highway.update(updateTimeInMilisec);
                repaint();
            }

        }

        public static void createAndShowGui(Highway highway) {
            HighwayGUI gui = new HighwayGUI();
            gui.panel.highway = highway;
        }
    }
}
