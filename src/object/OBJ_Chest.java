package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Chest extends Entity {

    private final GamePanel gp;
    public final static String OBJ_NAME = "Coffre";

    public OBJ_Chest(GamePanel gp){
        super(gp);
        this.gp = gp;

        type = TYPE_OBSTACLE;
        setName(OBJ_NAME);
        image = setup("res/objects/chest", gp.getTileSize(), gp.getTileSize());
        image2 = setup("res/objects/chest_opened", gp.getTileSize(), gp.getTileSize());
        down1 = image;
        setCollision(true);

        getSolidArea().x = 4;
        getSolidArea().y = 16;
        getSolidArea().width = 40;
        getSolidArea().height = 32;
        setSolidAreaDefaultX(getSolidArea().x);
        setSolidAreaDefaultY(getSolidArea().y);
    }

    private void setDialogue(){

        setDialogue(0, 0, "Tu ouvres le coffre et trouve: " + loot.getName() + " !\n... Tu ne peux porter plus !");
        setDialogue(1, 0, "\nTu obtiens: " + loot.getName() + " !");
        setDialogue(2, 0, "... C'est vide.");

    }

    @Override
    public void setLoot(Entity loot){
        this.loot = loot;
        setDialogue();
    }

    @Override
    public void interact(){

        if(!isOpened()){
            gp.playSE(3);


            if(!gp.getPlayer().canObtainItem(loot)){
                startDialogue(this, 0);

            } else {
                startDialogue(this, 1);
                down1 = image2;
                setOpened(true);
            }

        } else {
            startDialogue(this, 2);
        }

    }
}
