package tile_interative;

import entity.Entity;
import main.GamePanel;

import java.awt.Graphics2D;

public class InteractiveTile extends Entity {

    private final GamePanel gp;
    private boolean destructible = false;

    public InteractiveTile(GamePanel gp, int col, int row) {
        super(gp);
        this.gp = gp;
    }

    public boolean isCorrectItem(Entity entity){
        boolean isCorrectItem = false;
        return isCorrectItem;
    }

    public void playSE(){}

    public InteractiveTile getDestroyedForm(){
        InteractiveTile tile = null;
        return tile;
    }

    @Override
    public void update(){

        if(isInvincible()){
            setInvincibleCounter(getInvincibleCounter() + 1);

            if(getInvincibleCounter() > 20){
                setInvincible(false);
                setInvincibleCounter(0);
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {

        int screenX = getWorldX() - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX();
        int screenY = getWorldY() - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();

        g2.drawImage(down1, screenX, screenY, null);
    }

    public boolean isDestructible() {
        return destructible;
    }

    public void setDestructible(boolean destructible) {
        this.destructible = destructible;
    }
}
