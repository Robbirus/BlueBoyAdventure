package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Red extends Entity {

    private GamePanel gp;
    public final static String OBJ_NAME = "Potion Rouge";

    public OBJ_Potion_Red(GamePanel gp) {
        super(gp);

        this.gp = gp;
        setValue(5);

        type = TYPE_CONSUMABLE;
        setName(OBJ_NAME);
        down1 = setup("res/objects/potion_red", gp.getTileSize(), gp.getTileSize());
        setDescription("[" + getName() + "]\nSoigne " + getValue() + " pv");
        setPrice(25);
        setStackable(true);

        setDialogue();
    }

    private void setDialogue(){

        setDialogue(0, 0, "Tu as bu la " + getName() + "!\nTa vie a été restauré de " + getValue() + ".");
    }

    @Override
    public boolean use(Entity entity){

        startDialogue(this, 0);
        entity.setLife(entity.getLife() + getValue());
        gp.playSE(2);
        return true;

    }
}
