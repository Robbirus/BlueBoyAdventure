package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Speed_Boots extends Entity {

    public final static String OBJ_NAME = "Super Bottes";

    public OBJ_Speed_Boots(GamePanel gp){
        super(gp);

        type = TYPE_BOOTS;
        setName(OBJ_NAME);
        down1 = setup("res/objects/speed_boots", gp.getTileSize(), gp.getTileSize());
        setDescription("[" + getName() + "]\nPermet au porteur de \ncourir vite");
        setSpeed(2);
        setPrice(50);
    }

}
