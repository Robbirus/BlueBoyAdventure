package monster;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

import java.util.Random;

public class MON_Orc extends Monster {

        private final GamePanel gamePanel = getGamePanel();

        public MON_Orc(GamePanel gamePanel) {
            super(gamePanel);

            setName("Orc");
            setDefaultSpeed(1);
            setSpeed(getDefaultSpeed());
            setMaxLife(10);
            setLife(getMaxLife());
            setAttack(8);
            setDefense(2);
            setExp(10);
            setKnockBackPower(5);

            getSolidArea().x = 4;
            getSolidArea().y = 4;
            getSolidArea().width = 40;
            getSolidArea().height = 44;
            setSolidAreaDefaultX(getSolidArea().x);
            setSolidAreaDefaultY(getSolidArea().y);
            getAttackArea().width = 48;
            getAttackArea().height = 48;
            setMotion1_duration(40);
            setMotion2_duration(85);

            getImage();
            getAttackImage();
        }

        @Override
        public void getImage() {

            up1 = setup("res/monster/orc_up_1", gamePanel.getTileSize(), gamePanel.getTileSize());
            up2 = setup("res/monster/orc_up_2", gamePanel.getTileSize(), gamePanel.getTileSize());
            down1 = setup("res/monster/orc_down_1", gamePanel.getTileSize(), gamePanel.getTileSize());
            down2 = setup("res/monster/orc_down_2", gamePanel.getTileSize(), gamePanel.getTileSize());
            left1 = setup("res/monster/orc_left_1", gamePanel.getTileSize(), gamePanel.getTileSize());
            left2 = setup("res/monster/orc_left_2", gamePanel.getTileSize(), gamePanel.getTileSize());
            right1 = setup("res/monster/orc_right_1", gamePanel.getTileSize(), gamePanel.getTileSize());
            right2 = setup("res/monster/orc_right_2", gamePanel.getTileSize(), gamePanel.getTileSize());
        }

        @Override
        public void getAttackImage(){

            attackUp1 = setup("res/monster/orc_attack_up_1", gamePanel.getTileSize(), gamePanel.getTileSize() * 2);
            attackUp2 = setup("res/monster/orc_attack_up_2", gamePanel.getTileSize(), gamePanel.getTileSize() * 2);
            attackDown1 = setup("res/monster/orc_attack_down_1", gamePanel.getTileSize(), gamePanel.getTileSize() * 2);
            attackDown2 = setup("res/monster/orc_attack_down_2", gamePanel.getTileSize(), gamePanel.getTileSize() * 2);
            attackLeft1 = setup("res/monster/orc_attack_left_1", gamePanel.getTileSize() * 2, gamePanel.getTileSize());
            attackLeft2 = setup("res/monster/orc_attack_left_2", gamePanel.getTileSize() * 2, gamePanel.getTileSize());
            attackRight1 = setup("res/monster/orc_attack_right_1", gamePanel.getTileSize() * 2, gamePanel.getTileSize());
            attackRight2 = setup("res/monster/orc_attack_right_2", gamePanel.getTileSize() * 2, gamePanel.getTileSize());
        }

        @Override
        public void setAction() {

            if (isOnPath()) {

                // Check if it stops chasing
                checkStopChasing(gamePanel.getPlayer(), 15, 100);

                // Search the direction to go
                searchPath(getGoalCol(gamePanel.getPlayer()), getGoalRow(gamePanel.getPlayer()));

            } else {

                // Check if it starts chasing
                checkStartChasing(gamePanel.getPlayer(), 5, 100);

                // Get a random direction
                randomiseDirection(120);

            }

            // Check if it attacks
            if(!isAttacking()){
                checkAttack(30, gamePanel.getTileSize() * 4, gamePanel.getTileSize());
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


