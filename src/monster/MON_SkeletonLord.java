package monster;

import main.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

import java.util.Random;

public class MON_SkeletonLord extends Monster{

    private final GamePanel gamePanel = getGamePanel();
    public static final String MON_NAME = "Skeleton Lord";

    public MON_SkeletonLord(GamePanel gamePanel) {
        super(gamePanel);

        setName(MON_NAME);
        setDefaultSpeed(1);
        setSpeed(getDefaultSpeed());
        setMaxLife(50);
        setLife(getMaxLife());
        setAttack(10);
        setDefense(2);
        setExp(50);
        setKnockBackPower(5);

        int size = gamePanel.getTileSize()*5;
        getSolidArea().x = 48;
        getSolidArea().y = 48;
        getSolidArea().width = size -44*2;
        getSolidArea().height = size - 48;
        setSolidAreaDefaultX(getSolidArea().x);
        setSolidAreaDefaultY(getSolidArea().y);
        getAttackArea().width = 170;
        getAttackArea().height = 170;
        setMotion1_duration(25);
        setMotion2_duration(50);

        getImage();
        getAttackImage();
    }

    @Override
    public void getImage() {

        int sizeCoef = 5;

        up1 = setup("res/monster/skeletonlord_up_1", gamePanel.getTileSize()*sizeCoef, gamePanel.getTileSize()*sizeCoef);
        up2 = setup("res/monster/skeletonlord_up_2", gamePanel.getTileSize()*sizeCoef, gamePanel.getTileSize()*sizeCoef);
        down1 = setup("res/monster/skeletonlord_down_1", gamePanel.getTileSize()*sizeCoef, gamePanel.getTileSize()*sizeCoef);
        down2 = setup("res/monster/skeletonlord_down_2", gamePanel.getTileSize()*sizeCoef, gamePanel.getTileSize()*sizeCoef);
        left1 = setup("res/monster/skeletonlord_left_1", gamePanel.getTileSize()*sizeCoef, gamePanel.getTileSize()*sizeCoef);
        left2 = setup("res/monster/skeletonlord_left_2", gamePanel.getTileSize()*sizeCoef, gamePanel.getTileSize()*sizeCoef);
        right1 = setup("res/monster/skeletonlord_right_1", gamePanel.getTileSize()*sizeCoef, gamePanel.getTileSize()*sizeCoef);
        right2 = setup("res/monster/skeletonlord_right_2", gamePanel.getTileSize()*sizeCoef, gamePanel.getTileSize()*sizeCoef);
    }

    @Override
    public void getAttackImage(){

        int sizeCoef = 5;

        attackUp1 = setup("res/monster/skeletonlord_attack_up_1", gamePanel.getTileSize()*sizeCoef, gamePanel.getTileSize()*sizeCoef * 2);
        attackUp2 = setup("res/monster/skeletonlord_attack_up_2", gamePanel.getTileSize()*sizeCoef, gamePanel.getTileSize()*sizeCoef * 2);
        attackDown1 = setup("res/monster/skeletonlord_attack_down_1", gamePanel.getTileSize()*sizeCoef, gamePanel.getTileSize()*sizeCoef * 2);
        attackDown2 = setup("res/monster/skeletonlord_attack_down_2", gamePanel.getTileSize()*sizeCoef, gamePanel.getTileSize()*sizeCoef * 2);
        attackLeft1 = setup("res/monster/skeletonlord_attack_left_1", gamePanel.getTileSize()*sizeCoef * 2, gamePanel.getTileSize()*sizeCoef);
        attackLeft2 = setup("res/monster/skeletonlord_attack_left_2", gamePanel.getTileSize()*sizeCoef * 2, gamePanel.getTileSize()*sizeCoef);
        attackRight1 = setup("res/monster/skeletonlord_attack_right_1", gamePanel.getTileSize()*sizeCoef * 2, gamePanel.getTileSize()*sizeCoef);
        attackRight2 = setup("res/monster/skeletonlord_attack_right_2", gamePanel.getTileSize()*sizeCoef * 2, gamePanel.getTileSize()*sizeCoef);
    }

    @Override
    public void setAction() {

        if (isOnPath()) {

        } else {

            // Get a random direction
            randomiseDirection(120);

        }

        // Check if it attacks
        if(!isAttacking()){
            checkAttack(60, gamePanel.getTileSize() * 10, gamePanel.getTileSize()*5);
        }
    }

    @Override
    public void damageReaction() {

        setActionLockCounter(0);
//      direction = gp.player.direction;
        setOnPath(true);
    }

    @Override
    public void checkDrop() {

        // CAST A DIE
        int i = new Random().nextInt(100) + 1;

        // SET THE MONSTER DROP
        if (i < 50) {
            dropItem(new OBJ_Coin_Bronze(gamePanel));
        }
        if (i >= 50 && i < 75) {
            dropItem(new OBJ_Heart(gamePanel));
        }
        if (i >= 75 && i < 100) {
            dropItem(new OBJ_ManaCrystal(gamePanel));
        }
    }
}
