package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Heart extends Entity {

    GamePanel gp;
    public final static String OBJ_NAME = "Coeur";

    public OBJ_Heart(GamePanel gp){
        super(gp);

        this.gp = gp;

        type = TYPE_PICKUP_ONLY;
        setName(OBJ_NAME);
        setValue(2);
        down1 = setup("res/objects/heart_full", gp.getTileSize(), gp.getTileSize());
        image = setup("res/objects/heart_full", gp.getTileSize(), gp.getTileSize());
        image2 = setup("res/objects/heart_half", gp.getTileSize(), gp.getTileSize());
        image3 = setup("res/objects/heart_blank", gp.getTileSize(), gp.getTileSize());
    }

    @Override
    public boolean use(Entity entity){

        gp.playSE(2);
        gp.getUi().addMessage("Vie +" + getValue());
        entity.setLife(entity.getLife() + getValue());
        return true;
    }
}
