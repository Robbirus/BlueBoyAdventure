package environment;

import main.GamePanel;

import java.awt.Graphics2D;

public class EnvironmentManager {

    private GamePanel gp;
    private Lighting lighting;

    public EnvironmentManager(GamePanel gp){
        this.gp = gp;
    }

    public void setup(){

        this.lighting = new Lighting(gp);

    }

    public void update(){

        lighting.update();
    }

    public void draw(Graphics2D g2){

        this.lighting.draw(g2);
    }

    public Lighting getLighting() {
        return lighting;
    }
}
