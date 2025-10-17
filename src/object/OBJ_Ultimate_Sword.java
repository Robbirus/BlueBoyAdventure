package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Ultimate_Sword extends Entity {

    public final static String OBJ_NAME = "Excalibur";

    public OBJ_Ultimate_Sword(GamePanel gp) {
        super(gp);

        type = TYPE_SWORD;
        setName(OBJ_NAME);
        down1 = setup("res/objects/ultimate_sword", gp.getTileSize(), gp.getTileSize());
        setAttackValue(10);
        setDescription("[" + getName() + "]\nForgée par les dieux !\nCrits: 47%");
        getAttackArea().width = 36;
        getAttackArea().height = 36;
        setCritsValue(15);
        setChanceOfCrits(47.0);
        setPrice(1000);
        setKnockBackPower(20);
        setMotion1_duration(2);
        setMotion2_duration(10);
    }
}
