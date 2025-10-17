package entity;

import main.GamePanel;

public class Projectile extends Entity{

    private Entity user;
    private final GamePanel gamePanel = getGamePanel();

    public Projectile(GamePanel gp) {
        super(gp);
    }

    public void set(int worldX, int worldY, String direction, boolean alive, Entity user){

        this.setWorldX(worldX);
        this.setWorldY(worldY);
        this.setDirection(direction);
        this.setAlive(alive);
        this.user = user;
        this.setLife(this.getMaxLife());
    }

    @Override
    public void update(){

        if(user == gamePanel.getPlayer()){
            int monsterIndex = gamePanel.getCollisionChecker().checkEntity(this, gamePanel.getMonster());
            if(monsterIndex != 999) {
                gamePanel.getPlayer().damageMonster(monsterIndex, this, getAttack(), getKnockBackPower());
                generateParticle(user.getProjectile(), gamePanel.getMonster(gamePanel.getCurrentMap(), monsterIndex));
                setAlive(false);
            }
        }
        if(user != gamePanel.getPlayer()){
            boolean contactPlayer = gamePanel.getCollisionChecker().checkPlayer(this);
            if(!gamePanel.getPlayer().isInvincible() && contactPlayer){
                damagePlayer(getAttack());
                generateParticle(user.getProjectile(), gamePanel.getPlayer());
                setAlive(false);
            }
        }

        switch (getDirection()){
            case "up": setWorldY(getWorldY() - getSpeed()); break;
            case "down": setWorldY(getWorldY() + getSpeed()); break;
            case "left": setWorldX(getWorldX() - getSpeed()); break;
            case "right": setWorldX(getWorldX() + getSpeed()); break;
        }

        setLife(getLife() - 1);
        if(getLife() <= 0){
            setAlive(false);
        }

        setSpriteCounter(getSpriteCounter() + 1);
        if(getSpriteCounter() > 12){
            if(getSpriteNum() == 1){
                setSpriteNum(2);

            } else if(getSpriteNum() == 2){
                setSpriteNum(1);
            }
            setSpriteCounter(0);
        }
    }
    public boolean hasResource(Entity user){
        return false;

    }

    public void subtractResource(Entity user){}
}
