package main;

import entity.Entity;
import object.*;

public class EntityGenerator {

    private GamePanel gamePanel;

    public EntityGenerator(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    public Entity getObject(String itemName){

        Entity obj = null;

        switch (itemName){
            case OBJ_Axe.OBJ_NAME: obj = new OBJ_Axe(gamePanel); break;
            case OBJ_Boots.OBJ_NAME: obj = new OBJ_Boots(gamePanel); break;
            case OBJ_Chest.OBJ_NAME: obj = new OBJ_Chest(gamePanel); break;
            case OBJ_Coin_Bronze.OBJ_NAME: obj = new OBJ_Coin_Bronze(gamePanel); break;
            case OBJ_Door.OBJ_NAME: obj = new OBJ_Door(gamePanel); break;
            case OBJ_IronDoor.OBJ_NAME: obj = new OBJ_IronDoor(gamePanel); break;
            case OBJ_Fireball.OBJ_NAME: obj = new OBJ_Fireball(gamePanel); break;
            case OBJ_Heart.OBJ_NAME: obj = new OBJ_Heart(gamePanel); break;
            case OBJ_Key.OBJ_NAME: obj = new OBJ_Key(gamePanel); break;
            case OBJ_Lantern.OBJ_NAME: obj = new OBJ_Lantern(gamePanel); break;
            case OBJ_ManaCrystal.OBJ_NAME: obj = new OBJ_ManaCrystal(gamePanel); break;
            case OBJ_Pickaxe.OBJ_NAME: obj = new OBJ_Pickaxe(gamePanel); break;
            case OBJ_Potion_Blue.OBJ_NAME: obj = new OBJ_Potion_Blue(gamePanel); break;
            case OBJ_Potion_Red.OBJ_NAME: obj = new OBJ_Potion_Red(gamePanel); break;
            case OBJ_Rock.OBJ_NAME: obj = new OBJ_Rock(gamePanel); break;
            case OBJ_Shield_Blue.OBJ_NAME: obj = new OBJ_Shield_Blue(gamePanel); break;
            case OBJ_Shield_Wood.OBJ_NAME: obj = new OBJ_Shield_Wood(gamePanel); break;
            case OBJ_Speed_Boots.OBJ_NAME: obj = new OBJ_Speed_Boots(gamePanel); break;
            case OBJ_Sword_Normal.OBJ_NAME: obj = new OBJ_Sword_Normal(gamePanel); break;
            case OBJ_Tent.OBJ_NAME: obj = new OBJ_Tent(gamePanel); break;
            case OBJ_Ultimate_Sword.OBJ_NAME: obj = new OBJ_Ultimate_Sword(gamePanel); break;
        }

        return obj;
    }


}
