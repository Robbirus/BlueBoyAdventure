package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Shield_Blue extends Entity {

    public final static String OBJ_NAME = "Bouclier en fer";

    public OBJ_Shield_Blue(GamePanel gp) {
        super(gp);

        type = TYPE_SHIELD;
        setName(OBJ_NAME);
        down1 = setup("res/objects/shield_blue", gp.getTileSize(), gp.getTileSize());
        setDefenseValue(2);
        setDescription("[" + getName() + "]\nRésiste à tout !\nSauf à la rouille...");
        setPrice(250);
    }
}
