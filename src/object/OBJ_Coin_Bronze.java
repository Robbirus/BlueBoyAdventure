package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Coin_Bronze extends Entity {

    private GamePanel gp;
    public final static String OBJ_NAME = "Pièce de Bronze";

    public OBJ_Coin_Bronze(GamePanel gp) {
        super(gp);

        this.gp = gp;

        setValue(1);
        type = TYPE_PICKUP_ONLY;
        setName(OBJ_NAME);
        down1 = setup("res/objects/coin_bronze", gp.getTileSize(), gp.getTileSize());
    }

    @Override
    public boolean use(Entity entity){

        gp.playSE(1);
        gp.getUi().addMessage("Pièce +" + getValue());
        gp.getPlayer().setCoin(gp.getPlayer().getCoin() + getValue());
        return true;
    }
}
