package monster;

import main.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Rock;

import java.util.Random;

public class MON_Red_Slime extends Monster {

    private final GamePanel gamePanel = getGamePanel();

    public MON_Red_Slime(GamePanel gamePanel) {
        super(gamePanel);

        setName("Red Slime");
        setDefaultSpeed(2);
        setSpeed(getDefaultSpeed());
        setMaxLife(8);
        setLife(getMaxLife());
        setAttack(7);
        setDefense(1);
        setExp(5);
        setProjectile(new OBJ_Rock(gamePanel));
        setKnockBackPower(2);

        getSolidArea().x = 3;
        getSolidArea().y = 18;
        getSolidArea().width = 42;
        getSolidArea().height = 30;
        setSolidAreaDefaultX(getSolidArea().x);
        setSolidAreaDefaultY(getSolidArea().y);

        getImage();
    }

    @Override
    public void getImage() {
        up1 = setup("res/monster/redslime_down_1", gamePanel.getTileSize(), gamePanel.getTileSize());
        up2 = setup("res/monster/redslime_down_2", gamePanel.getTileSize(), gamePanel.getTileSize());
        down1 = setup("res/monster/redslime_down_1", gamePanel.getTileSize(), gamePanel.getTileSize());
        down2 = setup("res/monster/redslime_down_2", gamePanel.getTileSize(), gamePanel.getTileSize());
        left1 = setup("res/monster/redslime_down_1", gamePanel.getTileSize(), gamePanel.getTileSize());
        left2 = setup("res/monster/redslime_down_2", gamePanel.getTileSize(), gamePanel.getTileSize());
        right1 = setup("res/monster/redslime_down_1", gamePanel.getTileSize(), gamePanel.getTileSize());
        right2 = setup("res/monster/redslime_down_2", gamePanel.getTileSize(), gamePanel.getTileSize());

    }

    @Override
    public void getAttackImage() {}


    @Override
    public void setAction() {
        if (isOnPath()) {

            // Check if it stops chasing
            checkStopChasing(gamePanel.getPlayer(), 10, 100);

            // Search the direction to go
            searchPath(getGoalCol(gamePanel.getPlayer()), getGoalRow(gamePanel.getPlayer()));

            // Check if it shoots a projectile
            checkShoot(200, 30);

        } else {

            // Check if it starts chasing
            checkStartChasing(gamePanel.getPlayer(), 3, 100);

            // Get a random direction
            randomiseDirection(120);
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
        if (i >= 50 && i < 80) {
            dropItem(new OBJ_Heart(gamePanel));
        }
        if (i >= 80 && i < 100) {
            dropItem(new OBJ_ManaCrystal(gamePanel));
        }
    }
}
