package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Pickaxe extends Entity {

    public final static String OBJ_NAME = "Pioche";
    public OBJ_Pickaxe(GamePanel gamePanel) {
        super(gamePanel);

        type = TYPE_PICKAXE;
        setName(OBJ_NAME);
        down1 = setup("res/objects/pickaxe", gamePanel.getTileSize(), gamePanel.getTileSize());
        setAttackValue(2);
        setDescription("[Pioche]\nRocks And Stones !");
        getAttackArea().width = 30;
        getAttackArea().height = 30;
        setPrice(75);
        setKnockBackPower(10);
        setMotion1_duration(10);
        setMotion2_duration(20);
    }
}
