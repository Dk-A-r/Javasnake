import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;


public class SnakeGame extends JPanel implements ActionListener, KeyListener {


    private class Tile {
        int x;
        int y;

        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
// snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    // food
    Tile food;

    Random random;

    //game logic
    Timer gameLoop;
    int velocityX = 1; //init velocity
    int velocityY = 0;
    boolean gameOver = false;

    
    int boardWidth;
    int boardHeight;
    
    //constants
    private static final int TILE_SIZE = 25;
    private static final Color SNAKE_COLOR = new Color(50, 205, 50);
    private static final Color FOOD_COLOR = new Color(220, 20, 60);

    SnakeGame(int boardWidth, int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();
        int velocityX;
        int velocityY;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        /*
        for (int i = 0; i < boardWidth/TILE_SIZE; i++){
            g.drawLine(i*TILE_SIZE, 0, i*TILE_SIZE, boardHeight);
            g.drawLine(0, i*TILE_SIZE, boardWidth, i * TILE_SIZE);
        }
        */

        //snake Head
        g.setColor(SNAKE_COLOR);
        //g.fillRect(snakeHead.x*TILE_SIZE, snakeHead.y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
        g.fill3DRect(snakeHead.x*TILE_SIZE, snakeHead.y*TILE_SIZE, TILE_SIZE, TILE_SIZE, true);

        //Snake Body
        for (int i = 0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            //g.fillRect(snakePart.x * TILE_SIZE, snakePart.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            g.fill3DRect(snakePart.x * TILE_SIZE, snakePart.y * TILE_SIZE, TILE_SIZE, TILE_SIZE, true);
        }

        //score
        g.setFont(new Font("Times New Roman", Font.BOLD, 24));
        if (gameOver) {
            g.setColor(Color.red);
            g.setFont(new Font("Times New Roman", Font.BOLD, 70));
            g.drawString("Конец игры: " + String.valueOf(snakeBody.size()),  100, 300);
        } else {
            g.drawString("Счет: " + String.valueOf(snakeBody.size()), TILE_SIZE-16, TILE_SIZE);
        }

        //food
        g.setColor(FOOD_COLOR);
        //g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        g.fill3DRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE, true);
    }

    public void placeFood() {
        food.x = random.nextInt(boardWidth/TILE_SIZE); //600/25 = 24
        food.y = random.nextInt(boardHeight/TILE_SIZE);
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        //eat food
        if(collision(snakeHead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        //snake Body
        for (int i = snakeBody.size() - 1; i >=0; i--){
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                snakePart.x = snakeBody.get(i - 1).x;
                snakePart.y = snakeBody.get(i - 1).y;
            }
        }

        //snake Head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game over
        for (int i = 0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }
        if (snakeHead.x < 0 || snakeHead.x > 24 ||
                snakeHead.y < 0 || snakeHead.y > 24) {
                gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
