package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Axe extends Entity {

    public final static String OBJ_NAME = "Hache";

    public OBJ_Axe(GamePanel gamePanel) {
        super(gamePanel);

        type = TYPE_AXE;
        setName(OBJ_NAME);
        down1 = setup("res/objects/axe", gamePanel.getTileSize(), gamePanel.getTileSize());
        setAttackValue(2);
        setDescription("[" + getName() + "]\nUne hache de Bucheron !\nCrits: 15%");
        getAttackArea().width = 30;
        getAttackArea().height = 30;
        setCritsValue(3);
        setChanceOfCrits(15.0);
        setPrice(75);
        setKnockBackPower(10);
        setMotion1_duration(20);
        setMotion2_duration(40);
    }
}
