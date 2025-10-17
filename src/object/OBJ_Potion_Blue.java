package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Blue extends Entity {

    private GamePanel gp;
    public final static String OBJ_NAME = "Potion Bleu";

    public OBJ_Potion_Blue(GamePanel gp) {
        super(gp);

        this.gp = gp;
        setValue(5);

        type = TYPE_CONSUMABLE;
        setName(OBJ_NAME);
        down1 = setup("res/objects/potion_blue", gp.getTileSize(), gp.getTileSize());
        setDescription("[" + getName() + "]\nDonne " + getValue() + " mana");
        setPrice(25);
        setStackable(true);

        setDialogue();
    }

    private void setDialogue(){

        setDialogue(0, 0, "Tu as bu la " + getName() + "!\nTa mana a été restauré de " + getValue() + ".");
    }

    @Override
    public boolean use(Entity entity){

        startDialogue(this, 0);
        entity.setMana(entity.getMana() + getValue());
        gp.playSE(2);
        return true;

    }
}
