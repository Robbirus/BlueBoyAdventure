package tile_interative;

import entity.Entity;
import main.GamePanel;
import java.awt.Color;

public class IT_DryTree extends InteractiveTile{

    private final GamePanel gp;

    public IT_DryTree(GamePanel gp, int col, int row) {
        super(gp, col, row);
        this.gp = gp;

        this.setWorldX(gp.getTileSize() * col);
        this.setWorldY(gp.getTileSize() * row);

        down1 = setup("res/tiles_interactive/drytree", gp.getTileSize(), gp.getTileSize());
        setDestructible(true);
        setLife(1);
    }

    @Override
    public boolean isCorrectItem(Entity entity){
        boolean isCorrectItem = false;

        if(entity.getCurrentWeapon().type == TYPE_AXE){
            isCorrectItem = true;
        }

        return isCorrectItem;
    }

    @Override
    public void playSE(){
        gp.playSE(11);
    }

    @Override
    public InteractiveTile getDestroyedForm(){
        InteractiveTile tile = new IT_Trunk(gp, getWorldX()/ gp.getTileSize(), getWorldY()/gp.getTileSize());
        return tile;
    }

    @Override
    public Color getParticleColor(){
        Color color = new Color(65, 50, 30);
        return color;
    }

    @Override
    public int getParticleSize(){
        int size = 6; // 6 pixels
        return size;
    }

    @Override
    public int getParticleSpeed(){
        int speed = 1;
        return speed;
    }

    @Override
    public int getParticleMaxLife(){
        int maxLife = 20;
        return maxLife;
    }
}
