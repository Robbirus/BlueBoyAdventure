package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Door extends Entity {

    private final GamePanel gamePanel;
    public final static String OBJ_NAME = "Porte";

    public OBJ_Door(GamePanel gp){
        super(gp);
        this.gamePanel = gp;

        type = TYPE_OBSTACLE;
        setName(OBJ_NAME);
        down1 = setup("res/objects/door", gp.getTileSize(), gp.getTileSize());
        setCollision(true);

        getSolidArea().x = 0;
        getSolidArea().y = 16;
        getSolidArea().width = 48;
        getSolidArea().height = 32;
        setSolidAreaDefaultX(getSolidArea().x);
        setSolidAreaDefaultY(getSolidArea().y);
        setDialogue();
    }

    private void setDialogue(){

        setDialogue(0, 0, "Tu essayes de d'ouvrir la porte.\nIl te faut une clé.");
    }

    @Override
    public void interact(){

        startDialogue(this, 0);
    }
}
