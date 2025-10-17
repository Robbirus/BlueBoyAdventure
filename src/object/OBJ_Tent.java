package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Tent extends Entity {

    private GamePanel gp;
    public final static String OBJ_NAME = "Tente";

    public OBJ_Tent(GamePanel gp){
        super(gp);
        this.gp = gp;

        type = TYPE_CONSUMABLE;
        setName(OBJ_NAME);
        down1 = setup("res/objects/tent", gp.getTileSize(), gp.getTileSize());
        setDescription("[Tente]\nUtile pour passer la nuit.");
        setPrice(300);
        setStackable(true);
    }

    @Override
    public boolean use(Entity entity){

        gp.gameState = GamePanel.SLEEP_STATE;
        gp.playSE(14);
        gp.getPlayer().setLife(gp.getPlayer().getMaxLife());
        gp.getPlayer().setMana(gp.getPlayer().getMaxMana());
        gp.getPlayer().getSleepingImage(down1);

        // Utilisation infini => false, utilisation fini => true;
        return true;
    }
}
