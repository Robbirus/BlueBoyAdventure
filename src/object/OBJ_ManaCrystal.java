package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_ManaCrystal extends Entity {

    private GamePanel gp;
    public final static String OBJ_NAME = "Mana Crystal";

    public OBJ_ManaCrystal(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = TYPE_PICKUP_ONLY;
        setName(OBJ_NAME);
        setValue(1);
        down1 = setup("res/objects/manacrystal_full", gp.getTileSize(), gp.getTileSize());
        image = setup("res/objects/manacrystal_full", gp.getTileSize(), gp.getTileSize());
        image2 = setup("res/objects/manacrystal_blank", gp.getTileSize(), gp.getTileSize());
    }

    @Override
    public boolean use(Entity entity){

        gp.playSE(2);
        gp.getUi().addMessage("Mana +" + getValue());
        entity.setMana(entity.getMana() + getValue());
        return true;
    }
}
