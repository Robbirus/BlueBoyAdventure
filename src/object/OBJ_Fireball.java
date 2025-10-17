package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

import java.awt.*;

public class OBJ_Fireball extends Projectile {

    private GamePanel gp;
    public final static String OBJ_NAME = "Boule de feu";

    public OBJ_Fireball(GamePanel gp) {
        super(gp);
        this.gp = gp;

        setName(OBJ_NAME);
        setSpeed(5);
        setMaxLife(80);
        setLife(getMaxLife());
        setAttack(2);
        setKnockBackPower(5);
        setUseCost(1);
        setAlive(false);
        getImage();
    }

    public void getImage(){
        up1 = setup("res/projectile/fireball_up_1", gp.getTileSize(), gp.getTileSize());
        up2 = setup("res/projectile/fireball_up_2", gp.getTileSize(), gp.getTileSize());
        down1 = setup("res/projectile/fireball_down_1", gp.getTileSize(), gp.getTileSize());
        down2 = setup("res/projectile/fireball_down_2", gp.getTileSize(), gp.getTileSize());
        left1 = setup("res/projectile/fireball_left_1", gp.getTileSize(), gp.getTileSize());
        left2 = setup("res/projectile/fireball_left_2", gp.getTileSize(), gp.getTileSize());
        right1 = setup("res/projectile/fireball_right_1", gp.getTileSize(), gp.getTileSize());
        right2 = setup("res/projectile/fireball_right_2", gp.getTileSize(), gp.getTileSize());
    }

    @Override
    public boolean hasResource(Entity user){

        boolean hasResource = false;
        if(user.getMana() >= getUseCost()){
            hasResource = true;
        }
        return hasResource;
    }

    @Override
    public void subtractResource(Entity user){

        user.setMana(user.getMana() - getUseCost());
    }

    @Override
    public Color getParticleColor() {
        Color color = new Color(240, 50, 0);
        return color;
    }

    @Override
    public int getParticleSize() {
        int size = 10; // 10 pixels
        return size;
    }

    @Override
    public int getParticleSpeed() {
        int speed = 1;
        return speed;
    }

    @Override
    public int getParticleMaxLife() {
        int maxLife = 20;
        return maxLife;
    }
}
