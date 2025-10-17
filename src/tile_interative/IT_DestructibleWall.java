package tile_interative;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

import java.awt.Color;
import java.util.Random;

public class IT_DestructibleWall extends InteractiveTile{

    private final GamePanel gamepanel;

    public IT_DestructibleWall(GamePanel gamepanel, int col, int row) {
        super(gamepanel, col, row);
        this.gamepanel = gamepanel;

        this.setWorldX(gamepanel.getTileSize() * col);
        this.setWorldY(gamepanel.getTileSize() * row);

        down1 = setup("res/tiles_interactive/destructiblewall", gamepanel.getTileSize(), gamepanel.getTileSize());
        setDestructible(true);
        setLife(3);
    }

    @Override
    public boolean isCorrectItem(Entity entity){
        boolean isCorrectItem = false;

        if(entity.getCurrentWeapon().type == TYPE_PICKAXE){
            isCorrectItem = true;
        }

        return isCorrectItem;
    }

    @Override
    public void playSE(){
        gamepanel.playSE(20);
    }

    @Override
    public InteractiveTile getDestroyedForm(){
        InteractiveTile tile = null;
        return tile;
    }

    @Override
    public Color getParticleColor(){
        Color color = new Color(65, 65, 65);
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

//    @Override
//    public void checkDrop() {
//
//        // CAST A DIE
//        int i = new Random().nextInt(100) + 1;
//
//        // SET THE MONSTER DROP
//        if (i < 50) {
//            dropItem(new OBJ_Coin_Bronze(this.gamepanel));
//        }
//        if (i >= 50 && i < 75) {
//            dropItem(new OBJ_Heart(this.gamepanel ));
//        }
//        if (i >= 75 && i < 100) {
//            dropItem(new OBJ_ManaCrystal(this.gamepanel));
//        }
//    }
}

