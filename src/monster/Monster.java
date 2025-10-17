package monster;

import entity.Entity;
import main.GamePanel;

public abstract class Monster extends Entity {

    private final GamePanel gamePanel;

    public Monster(GamePanel gamePanel) {
        super(gamePanel);

        this.gamePanel = gamePanel;

        type = TYPE_MONSTER;
    }

    public abstract void getImage();

    public abstract void setAction();

    public abstract void damageReaction();

    public abstract void checkDrop();

    public abstract void getAttackImage();

    public GamePanel getGamePanel(){
        return this.gamePanel;
    }
}
