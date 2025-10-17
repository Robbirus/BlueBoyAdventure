package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Key extends Entity {

    private final GamePanel gp;
    public final static String OBJ_NAME = "Clé";

    public OBJ_Key(GamePanel gp){
        super(gp);
        this.gp = gp;

        type = TYPE_CONSUMABLE;
        setName(OBJ_NAME);
        down1 = setup("res/objects/key", gp.getTileSize(), gp.getTileSize());
        setDescription("[" + getName() + "]\nCela devrait ouvrir\nquelque chose...");
        setPrice(100);
        setStackable(true);
        setDialogue();

    }

    private void setDialogue(){

        setDialogue(0, 0, "Tu utilise la " + getName() + "\nEt ouvre la porte.");

        setDialogue(1, 0, "Cela ne marchera pas.");
    }

    @Override
    public boolean use(Entity entity){

        int objIndex = getDetected(entity, gp.getObj(), "Porte");

        if(objIndex != 999){
            startDialogue(this, 0);
            gp.playSE(3);
            gp.setObj(gp.getCurrentMap(), objIndex, null);
            return true;

        } else {
            startDialogue(this, 1);
            return false;
        }
    }
}
