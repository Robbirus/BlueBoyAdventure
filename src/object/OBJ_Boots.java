package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Boots extends Entity {

    public final static String OBJ_NAME = "Bottes";

    public OBJ_Boots(GamePanel gp){
        super(gp);

        type = TYPE_BOOTS;
        setName(OBJ_NAME);
        down1 = setup("res/objects/boots", gp.getTileSize(), gp.getTileSize());
        setDescription("[" + getName() + "]\nLent mais sur");
        setSpeed(1);
        setPrice(50);
    }
}
