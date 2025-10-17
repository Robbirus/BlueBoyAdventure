package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Sword_Normal extends Entity {

    public final static String OBJ_NAME = "Epée Normal";

    public OBJ_Sword_Normal(GamePanel gp) {
        super(gp);

        type = TYPE_SWORD;
        setName(OBJ_NAME);
        down1 = setup("res/objects/sword_normal", gp.getTileSize(), gp.getTileSize());
        setAttackValue(1);
        setDescription("[" + getName() + "]\nUne vieille épée\nCrits: 6%");
        getAttackArea().width = 36;
        getAttackArea().height = 36;
        setCritsValue(4);
        setChanceOfCrits(6.0);
        setPrice(20);
        setKnockBackPower(2);
        setMotion1_duration(5);
        setMotion2_duration(25);
    }
}
