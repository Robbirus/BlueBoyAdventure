package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Shield_Wood extends Entity {

    public final static String OBJ_NAME = "Bouclier en Bois";

    public OBJ_Shield_Wood(GamePanel gp) {
        super(gp);

        type = TYPE_SHIELD;
        setName(OBJ_NAME);
        down1 = setup("res/objects/shield_wood", gp.getTileSize(), gp.getTileSize());
        setDefenseValue(1);
        setDescription("[" + getName() + "]\nUn simple bouclier");
        setPrice(50);
    }
}
