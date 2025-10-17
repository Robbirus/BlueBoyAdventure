package entity;

import main.GamePanel;

import java.awt.*;

public class Particle extends Entity{

    private Entity generator;
    private final GamePanel gamePanel = getGamePanel();
    private Color color;
    private int size;
    private int xd;
    private int yd;


    public Particle(GamePanel gp, Entity generator, Color color, int size, int speed, int maxLife, int xd, int yd) {
        super(gp);

        this.generator = generator;
        this.color = color;
        this.size = size;
        this.setSpeed(speed);
        this.setMaxLife(maxLife);
        this.xd = xd;
        this.yd = yd;

        setLife(maxLife);
        int offset = (gp.getTileSize()/2) - (size/2);
        setWorldX(generator.getWorldX() + offset);
        setWorldY(generator.getWorldY() + offset);
    }

    @Override
    public void update() {

        setLife(getLife() - 1);

        if(getLife() < getMaxLife()/3){
            yd++;
        }

        setWorldX(getWorldX() + xd * getSpeed());
        setWorldY(getWorldY() + xd * getSpeed());

        if(getLife() == 0){
            setAlive(false);
        }
    }

    @Override
    public void draw(Graphics2D g2) {

        int screenX = getWorldX() - gamePanel.getPlayer().getWorldX() + gamePanel.getPlayer().getScreenX();
        int screenY = getWorldY() - gamePanel.getPlayer().getWorldY() + gamePanel.getPlayer().getScreenY();

        g2.setColor(color);
        g2.fillRect(screenX, screenY, size, size);
    }
}
