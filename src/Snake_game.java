import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

//Each circle in snake body
class snake_node{
    int x, y, radius, x1, y1;
    snake_node(){

        radius = 10;
    }
}
//JFRAME
public class Snake_game extends JFrame {
    snake panel = null;
    JMenuBar menu = null;
    JMenu controls = null;
    JMenuItem play = null;
    JMenuItem restart = null;
    boolean firstplay = true;
    Snake_game(){
        this.setResizable(false);
        menu = new JMenuBar();
        play = new JMenuItem("Play");
        controls = new JMenu("Menu");
        restart = new JMenuItem("Restart");
        restart.setMnemonic(KeyEvent.VK_R);
        restart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
        restart.setMnemonic(KeyEvent.VK_I);
        play.setMnemonic(KeyEvent.VK_P);
        play.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
        controls.add(play);
        controls.add(restart);
        menu.add(controls);
        this.setJMenuBar(menu);
        //GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
        panel = new snake();
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(800, 595));
        panel.dim = this.getSize();
        this.add(panel, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        play.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent eee){play();}});
        restart.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ee){restart();}});

        this.addKeyListener(new KeyAdapter(){public void keyPressed(KeyEvent e){panel.keyPress(e);}});
        this.setVisible(true);
        this.pack();
    }
    //function to start thread for starting game
    public void play(){
        if(firstplay){
            panel.t.start();
            firstplay = false;
            play.setEnabled(false);
        }
    }
    //function for restarting game, it will resize the snake to its initial position and initial length
    public void restart(){
        int pos = 0;
        panel.vector.clear();
        snake_node[] loda = new snake_node[5];
        for(int i =0; i < 5; i++){
            loda[i] = new snake_node();
            loda[i].x = 200;
            loda[i].y = 150-pos;
            loda[i].y1 = 150-pos;
            loda[i].x1 = 200;
            panel.vector.add(loda[i]);
            pos = pos+10;
        }
        panel.EE = false;
        panel.dir = 'D';
        panel.repaint();
    }

    public static void main(String[] args){
        new Snake_game();// object of the game class
    }
}

//a panel where all the drawings take place and it is embedded into JFrame
class snake extends JPanel implements Runnable{
    Vector<snake_node> vector = null;   //vector which will hold all the beads of snake body
    snake_node[] snode = null;
    int centx = 0, centy = 0;
    int inc = 10, pos = 0;
    char dir = 'D';
    Dimension dim = null;
    Thread t = null;
    boolean b = true, EE = false;
    snake(){
        vector = new Vector();
        snode = new snake_node[5];
        for(int i =0; i < 5; i++){
            snode[i] = new snake_node();
            snode[i].x = 200;
            snode[i].y = 150-pos;
            snode[i].y1 = 150-pos;
            snode[i].x1 = 200;
            vector.add(snode[i]);
            pos = pos+10;
        }
        t = new Thread(this);
        this.setLayout(null);
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        dim = this.getSize();
        this.addKeyListener(new KeyAdapter(){public void keyTyped(KeyEvent e){keyPress(e);}});
        this.setVisible(true);
    }
    int time = 70;
    boolean gamepause = true;
    //keyevents
    void keyPress(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_DOWN){
            if(dir != 'U')
                dir = 'D';
        }
        else if(e.getKeyCode() == KeyEvent.VK_UP){
            if(dir != 'D')
                dir = 'U';
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(dir != 'R')
                dir = 'L';
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(dir != 'L')
                dir = 'R';
        }
        else if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(0);
        else if(e.getKeyCode() == KeyEvent.VK_SPACE){
            if(gamepause){
                t.suspend();
                gamepause = false;
            }
            else{
                t.resume();
                gamepause = true;
            }
        }
        else if(e.getKeyCode() == KeyEvent.VK_S){
            time--;
        }
        else if(e.getKeyCode() == KeyEvent.VK_D){
            time++;
        }

    }
    //generating random position of food
    int foodx = 20+(int)(Math.random()*56)*10;
    int foody = 20+(int)(Math.random()*52)*10;
    int score = 0;
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        score = vector.size()-5;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int red = 30+(int)(Math.random()*220);
        int green = 30+(int)(Math.random()*220);
        int blue = 30+(int)(Math.random()*220);
        g2.setColor(Color.red);
        g2.drawRect(600, 0, 0, 0);
        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", 1, 20));
        g2.drawString("Score: "+score,650, 20);
        g2.fillRect(10, 10, dim.width-220, dim.height-65);
        g2.setColor(Color.black);
        g2.fillRect(10, 10, dim.width-220, dim.height-65);
        g2.setColor(new Color(blue));
        g2.fillOval(foodx, foody, 10, 10);
        g2.setColor(Color.green);
        //if game over then it will be display on screen
        if(EE){
            g2.setColor(Color.white);
            g2.setFont(new Font("Arial", 1, 120));
            g2.drawString("Game Over", 60, 260);

            //t.stop();
        }
        else{
            g2.fillOval(vector.get(0).x, vector.get(0).y, vector.get(0).radius, vector.get(0).radius);
            for(int i=1; i < vector.size(); i++){
                g2.fillOval(vector.get(i).x, vector.get(i).y, vector.get(i).radius, vector.get(i).radius);
                vector.get(i).x1 = vector.get(i).x;
                vector.get(i).y1 = vector.get(i).y;
                vector.get(i).x = vector.get(i-1).x1;
                vector.get(i).y = vector.get(i-1).y1;
            }
        }
    }
    //see the collision of wall's
    void checkOutOfBounds(int x, int y){
        if(x < -5 || x > dim.width-20 || y < -5 || y > dim.height-65){
            EE = true;
            try{Thread.sleep(1000);}catch(Exception e){}
            repaint();
        }
        else if(x==foodx && y==foody){
            snake_node snode = new snake_node();
            snode.x = vector.get(vector.size()-1).x1;
            snode.y = vector.get(vector.size()-1).y1;
            vector.add(snode);
            foodx = 20+(int)(Math.random()*56)*10;
            foody = 20+(int)(Math.random()*52)*10;
        }
        for(int i=1; i < vector.size(); i++){
            if(x == vector.get(i).x && y == vector.get(i).y){
                EE = true;
                try{Thread.sleep(1000);}catch(Exception e){}
                repaint();
            }
        }
    }

    public void run(){

        while (true){
            switch(dir){
                case 'L':
                    vector.get(0).x = (vector.get(0).x -inc);
                    vector.get(0).x1 = vector.get(0).x;
                    checkOutOfBounds(vector.get(0).x, vector.get(0).y);
                    repaint();
                    break;
                case 'R':
                    vector.get(0).x = (vector.get(0).x +inc);
                    vector.get(0).x1 = vector.get(0).x;
                    checkOutOfBounds(vector.get(0).x, vector.get(0).y);
                    repaint();
                    break;
                case 'U':
                    vector.get(0).y = (vector.get(0).y -inc);
                    vector.get(0).y1 = vector.get(0).y;
                    checkOutOfBounds(vector.get(0).x, vector.get(0).y);
                    repaint();
                    break;
                case 'D':
                    vector.get(0).y = (vector.get(0).y +inc);
                    vector.get(0).y1 = vector.get(0).y;
                    checkOutOfBounds(vector.get(0).x, vector.get(0).y);
                    repaint();
                    break;
            }
            try{
                Thread.sleep(time);
            }
            catch(Exception e){}
        }
    }
}