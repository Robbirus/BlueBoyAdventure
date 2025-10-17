package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Lantern extends Entity{

    public final static String OBJ_NAME = "Lanterne";

    public OBJ_Lantern(GamePanel gp){
        super(gp);

        type = TYPE_LIGHT;
        setName(OBJ_NAME);
        down1 = setup("res/objects/lantern", gp.getTileSize(), gp.getTileSize());
        setDescription("[Lanterne]\nIllumine ton chemin");
        setPrice(200);
        setLightRadius(250);
    }
}
