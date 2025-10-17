package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Entity {

    private final GamePanel gamePanel;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    public BufferedImage guardUp, guardDown, guardLeft, guardRight;
    public BufferedImage image, image2, image3;
    private Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    private Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    private int solidAreaDefaultX;
    private int solidAreaDefaultY;
    private boolean collision = false;
    private String[][] dialogues = new String[20][20];
    private Entity attacker;
    private Entity linkedEntity;

    // STATE
    private int worldX;
    private int worldY;
    private String direction = "down";
    private int spriteNum = 1;
    private int dialogueSet = 0;
    private int dialogueIndex = 0;
    private boolean collisionOn = false;
    private boolean invincible = false;
    private boolean attacking = false;
    private boolean alive = true;
    private boolean dead = false;
    private boolean hpBarOn = false;
    private boolean onPath = false;
    private boolean knockBack = false;
    private String knockBackDirection;
    private boolean guarding = false;
    private boolean transparent = false;
    private boolean offBalance = false;
    protected Entity loot;
    private boolean opened = false;

    // COUNTER
    private int spriteCounter = 0;
    private int actionLockCounter = 0;
    private int invincibleCounter = 0;
    private int shotAvailableCounter = 0;
    private int dyingCounter = 0;
    private int hpBarCounter = 0;
    private int knockBackCounter = 0;
    private int guardCounter = 0;
    private int offBalanceCounter = 0;


    // CHARACTER ATTRIBUTES
    private String name;
    private int defaultSpeed;
    private int speed;
    private int mobility;
    private int maxLife;

    private int life;
    private int maxMana;
    private int mana;
    private int ammo;
    private int level;
    private int strength;
    private int dexterity;
    private int attack;
    private int defense;
    private int exp;
    private int nextLevelExp;
    private int coin;
    private int crits;
    private int motion1_duration;
    private int motion2_duration;
    private Entity currentWeapon;
    private Entity currentShield;
    private Entity currentLight;
    private Entity currentBoots;
    private Projectile projectile;

    // ITEM ATTRIBUTES
    private ArrayList<Entity> inventory = new ArrayList<>();
    private final int maxInventorySize = 20;
    private int value;
    private int attackValue;
    private int defenseValue;
    private String description = "";
    private int useCost;
    private int price;
    private int knockBackPower = 0;
    private boolean stackable = false;
    private int amount = 1;
    private int lightRadius;
    private int critsValue;
    private double chanceOfCrits;

    // TYPES
    public int type; // 0 = player, 1 = npc, 2 = monster
    public final static int TYPE_PLAYER = 0;
    public final static int TYPE_NPC = 1;
    public final static int TYPE_MONSTER = 2;
    public final static int TYPE_SWORD = 3;
    public final static int TYPE_AXE = 4;
    public final static int TYPE_SHIELD = 5;
    public final static int TYPE_CONSUMABLE = 6;
    public final static int TYPE_PICKUP_ONLY = 7;
    public final static int TYPE_BOOTS = 8;
    public final static int TYPE_OBSTACLE = 9;
    public final static int TYPE_LIGHT = 10;
    public final static int TYPE_PICKAXE = 11;


    public Entity(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    public int getLeftX(){
        return worldX + solidArea.x;
    }

    public int getRightX(){
        return worldX + solidArea.x + solidArea.width;
    }

    public int getTopY(){
        return worldY + solidArea.y;
    }

    public int getBottomY(){
        return worldY + solidArea.y + solidArea.height;
    }

    public int getCol(){
        return (worldX + solidArea.x) / gamePanel.getTileSize();
    }

    public int getRow(){
        return (worldY + solidArea.y) / gamePanel.getTileSize();
    }

    public int getCenterX(){
        return worldX + left1.getWidth()/2;
    }

    public int getCenterY(){
        return  worldY + up1.getHeight()/2;
    }

    public int getXdistance(Entity target) {
        return Math.abs(getCenterX() - target.getCenterX());
    }

    public int getYdistance(Entity target){
        return Math.abs(getCenterY() - target.getCenterY());
    }

    public int getTileDistance(Entity target){
        return (getXdistance(target) + getYdistance(target)) / gamePanel.getTileSize();
    }

    public int getGoalCol(Entity target){
        return (target.worldX + target.solidArea.x) / gamePanel.getTileSize();
    }

    public int getGoalRow(Entity target){
        return (target.worldY + target.solidArea.y) / gamePanel.getTileSize();
    }

    public void resetCounter(){
        spriteCounter = 0;
        actionLockCounter = 0;
        invincibleCounter = 0;
        shotAvailableCounter = 0;
        dyingCounter = 0;
        hpBarCounter = 0;
        knockBackCounter = 0;
        guardCounter = 0;
        offBalanceCounter = 0;
    }

    public void setLoot(Entity loot){}

    public void setAction(){}

    public void move(String direction){}

    public void damageReaction(){}

    public void speak(){
    }

    public void facePlayer(){

        switch (gamePanel.getPlayer().getDirection()){
            case "up": direction = "down"; break;
            case "down": direction = "up"; break;
            case "left": direction = "right"; break;
            case "right": direction = "left"; break;
        }
    }

    public void startDialogue(Entity entity, int dialogueSet){

        gamePanel.gameState = GamePanel.DIALOGUE_STATE;
        gamePanel.getUi().setNpc(entity);
        this.dialogueSet = dialogueSet;
    }

    public void interact(){}

    public boolean use(Entity entity){ return false;}

    public void checkDrop(){}

    public void dropItem(Entity droppedItem){

        for(int i = 0; i < gamePanel.getObj()[1].length; i++){
            if(gamePanel.getObj(gamePanel.getCurrentMap(),i) == null){
                gamePanel.setObj(gamePanel.getCurrentMap(), i, droppedItem);
                gamePanel.getObj(gamePanel.getCurrentMap(), i).setWorldX(worldX); // The dead Monster's worldX
                gamePanel.getObj(gamePanel.getCurrentMap(), i).setWorldY(worldY);
                break;
            }
        }

    }

    public Color getParticleColor(){ return null; }

    public int getParticleSize(){ return 0; }

    public int getParticleSpeed(){ return 0;}

    public int getParticleMaxLife(){ return 0; }

    public void generateParticle(Entity generator, Entity target){

        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLife = generator.getParticleMaxLife();

        Particle topLeft = new Particle(gamePanel, target, color, size, speed, maxLife, -2, -1);
        Particle topRight = new Particle(gamePanel, target, color, size, speed, maxLife, 2, -1);
        Particle bottomLeft = new Particle(gamePanel, target, color, size, speed, maxLife, -2, 1);
        Particle bottomRight = new Particle(gamePanel, target, color, size, speed, maxLife, 2, 1);

        gamePanel.getParticlesList().add(topLeft);
        gamePanel.getParticlesList().add(topRight);
        gamePanel.getParticlesList().add(bottomLeft);
        gamePanel.getParticlesList().add(bottomRight);

    }

    public void checkCollision(){

        collisionOn = false;
        gamePanel.getCollisionChecker().checkTile(this);
        gamePanel.getCollisionChecker().checkObject(this, false);
        gamePanel.getCollisionChecker().checkEntity(this, gamePanel.getNpc());
        gamePanel.getCollisionChecker().checkEntity(this, gamePanel.getMonster());
        gamePanel.getCollisionChecker().checkEntity(this, gamePanel.getInteractiveTile());
        boolean contactPlayer = gamePanel.getCollisionChecker().checkPlayer(this);

        if(this.type == TYPE_MONSTER && contactPlayer){
            damagePlayer(attack);

        }
    }

    public void update() {

        if (knockBack) {

            checkCollision();

            if (collisionOn) {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;

            } else if (!collisionOn) {
                switch (knockBackDirection) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }

            knockBackCounter++;
            if (knockBackCounter == 10) {
                knockBackCounter = 0;
                knockBack = false;
                speed = defaultSpeed;
            }

        } else if(attacking){
            attacking();

        }else {
            setAction();
            checkCollision();

            // SI LA COLLISION EST FALSE, LE JOUEUR PEUT BOUGER
            if(!collisionOn){

                switch(direction){
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            spriteCounter++;
            if(spriteCounter > 24){
                if(spriteNum == 1){
                    spriteNum = 2;
                }
                else if(spriteNum == 2){
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }

        }


        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if(shotAvailableCounter < 30){
            shotAvailableCounter++;
        }

        if(offBalance){
            offBalanceCounter++;

            if(offBalanceCounter > 60){
                offBalance = false;
                offBalanceCounter = 0;
            }
        }
    }

    public void checkAttack(int rate, int straight, int horizontal){

        boolean targetInRange = false;
        int xDis = getXdistance(gamePanel.getPlayer());
        int yDis = getYdistance(gamePanel.getPlayer());


        switch (direction){
            case "up":
                if(gamePanel.getPlayer().getCenterY() < getCenterY() && yDis < straight && xDis < horizontal){
                    targetInRange = true;
                }
            break;
            case "down":
                if(gamePanel.getPlayer().getCenterY() > getCenterY() && yDis < straight && xDis < horizontal){
                    targetInRange = true;
                }
            break;
            case "left":
                if(gamePanel.getPlayer().getCenterX() < getCenterX() && xDis < straight && yDis < horizontal){
                    targetInRange = true;
                }
            break;
            case "right":
                if(gamePanel.getPlayer().getCenterX() > getCenterX() && xDis < straight && yDis < horizontal){
                    targetInRange = true;
                }
            break;
        }

        if(targetInRange){
            // Check if it initiates an attack
            int i = new Random().nextInt(rate);
            if(i == 0){
                attacking = true;
                spriteNum = 1;
                spriteCounter = 0;
                shotAvailableCounter = 0;
            }
        }

    }

    public void checkShoot(int rate, int shotInterval){

        int i = new Random().nextInt(rate);
        if(i == 0 && !projectile.isAlive() && shotAvailableCounter == shotInterval){

            projectile.set(worldX, worldY, direction, true, this);

            // CHECK VACANCY
            for(int j = 0; j < gamePanel.getProjectile()[1].length; j++){
                if(gamePanel.getProjectile(gamePanel.getCurrentMap(), j) == null){
                    gamePanel.setProjectile(gamePanel.getCurrentMap(), j, projectile);
                    break;
                }
            }

            shotAvailableCounter = 0;
        }

    }

    public void checkStartChasing(Entity target, int distance, int rate){

        if(getTileDistance(target) < distance){
            int i = new Random().nextInt(rate);
            if(i == 0){
                onPath = true;
            }
        }
    }

    public void checkStopChasing(Entity target, int distance, int rate){

        if(getTileDistance(target) > distance){
            int i = new Random().nextInt(rate);
            if(i == 0){
                onPath = false;
            }
        }
    }

    /**
     * 120 = 2 secondes
     * 60 = 1 secondes
     * @param interval
     */
    public void randomiseDirection(int interval){

        actionLockCounter++;

        if (actionLockCounter == interval) {

            Random random = new Random();
            int i = random.nextInt(100) + 1; // Pick up a number from 1 to 100

            if (i <= 25) { direction = "up"; }
            if (i > 25 && i <= 50) { direction = "down"; }
            if (i > 50 && i <= 75) { direction = "left"; }
            if (i > 75) { direction = "right"; }

            actionLockCounter = 0;
        }
    }

    public String getOppositeDirection(String direction){

        String oppositeDirection = "";

        switch (direction){
            case "up" : oppositeDirection = "down"; break;
            case "down" : oppositeDirection = "up"; break;
            case "left" : oppositeDirection = "right"; break;
            case "right" : oppositeDirection = "left"; break;
        }

        return oppositeDirection;
    }

    public void attacking(){

        spriteCounter++;

        if(spriteCounter <= motion1_duration){
            spriteNum = 1;
        }

        if(spriteCounter > motion1_duration && spriteCounter <= motion2_duration){ // <= hitting window
            spriteNum = 2;

            // SAVE THE CURRENT worldX, worldY, solidArea
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // ADJUST PLAYER'S worldX/Y FOR THE attackArea
            switch (direction){
                case "up": worldY -= attackArea.height; break;

                case "down": worldY += attackArea.height; break;

                case "left": worldX -= attackArea.width; break;

                case "right": worldX += attackArea.width; break;
            }

            // attackArea becomes solidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            if(type == TYPE_MONSTER){
                if(gamePanel.getCollisionChecker().checkPlayer(this)){
                    damagePlayer(attack);
                }

            } else { // PLAYER
                // Check monster collision with the updated worldX, worldY and solidArea
                int monsterIndex = gamePanel.getCollisionChecker().checkEntity(this, gamePanel.getMonster());
                gamePanel.getPlayer().damageMonster(monsterIndex, this, attack, currentWeapon.knockBackPower);

                int iTileIndex = gamePanel.getCollisionChecker().checkEntity(this, gamePanel.getInteractiveTile());
                gamePanel.getPlayer().damageInteractiveTile(iTileIndex);

                int projectileIndex = gamePanel.getCollisionChecker().checkEntity(this, gamePanel.getProjectile());
                gamePanel.getPlayer().damageProjectile(projectileIndex);

            }

            // After checking collision, restore the original data
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;

        }

        if(spriteCounter > motion2_duration){
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void damagePlayer(int attack){

        if(!gamePanel.getPlayer().isInvincible()){

            int damage = attack - gamePanel.getPlayer().getDefense();

            // Get an opposite direction of this attacker
            String canGuardDirection = getOppositeDirection(direction);

            if(gamePanel.getPlayer().isGuarding() && gamePanel.getPlayer().getDirection().equals(canGuardDirection)){

                // PARRY
                if(gamePanel.getPlayer().getGuardCounter() < 20){ // Parry Time Window
                    damage = 0;
                    gamePanel.playSE(16);
                    setKnockBack(this, gamePanel.getPlayer(), knockBackPower);
                    offBalance = true;
                    spriteCounter =- 60;

                } else {
                    // NORMAL GUARD
                    damage /= 3;
                    gamePanel.playSE(15);

                }

            } else {
                // Not Guarding
                gamePanel.playSE(6);
                if(damage < 1){
                    damage = 1;
                }
            }

            if(damage != 0){
                gamePanel.getPlayer().setTransparent(true);
                setKnockBack(gamePanel.getPlayer(), this, knockBackPower);
            }

            gamePanel.getPlayer().setLife(gamePanel.getPlayer().getLife() - damage);
            gamePanel.getPlayer().setInvincible(true);
        }
    }

    public void setKnockBack(Entity target, Entity attacker, int knockBackPower){

        this.attacker = attacker;
        target.knockBackDirection = attacker.direction;
        target.speed += knockBackPower;
        target.knockBack = true;
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        int screenX = worldX - gamePanel.getPlayer().getWorldX() + gamePanel.getPlayer().getScreenX();
        int screenY = worldY - gamePanel.getPlayer().getWorldY() + gamePanel.getPlayer().getScreenY();

        if(worldX + gamePanel.getTileSize()*5 > gamePanel.getPlayer().getWorldX() - gamePanel.getPlayer().getScreenX() &&
           worldX - gamePanel.getTileSize() < gamePanel.getPlayer().getWorldX() + gamePanel.getPlayer().getScreenX() &&
           worldY + gamePanel.getTileSize()*5 > gamePanel.getPlayer().getWorldY() - gamePanel.getPlayer().getScreenY() &&
           worldY - gamePanel.getTileSize() < gamePanel.getPlayer().getWorldY() + gamePanel.getPlayer().getScreenY()) {

            int tempScreenX = screenX;
            int tempScreenY = screenY;

            switch (direction) {
                case "up":
                    if (!attacking) {
                        if (spriteNum == 1) {
                            image = up1;
                        }
                        if (spriteNum == 2) {
                            image = up2;
                        }
                    }

                    if (attacking) {
                        tempScreenY = screenY - up1.getHeight();
                        if (spriteNum == 1) {
                            image = attackUp1;
                        }
                        if (spriteNum == 2) {
                            image = attackUp2;
                        }
                    }
                    break;
                case "down":
                    if (!attacking) {
                        if (spriteNum == 1) {
                            image = down1;
                        }
                        if (spriteNum == 2) {
                            image = down2;
                        }
                    }

                    if (attacking) {
                        if (spriteNum == 1) {
                            image = attackDown1;
                        }
                        if (spriteNum == 2) {
                            image = attackDown2;
                        }
                    }

                    break;
                case "left":
                    if (!attacking) {
                        if (spriteNum == 1) {
                            image = left1;
                        }
                        if (spriteNum == 2) {
                            image = left2;
                        }
                    }

                    if (attacking) {
                        tempScreenX = screenX - left1.getWidth();
                        if (spriteNum == 1) {
                            image = attackLeft1;
                        }
                        if (spriteNum == 2) {
                            image = attackLeft2;
                        }
                    }
                    break;
                case "right":
                    if (!attacking) {
                        if (spriteNum == 1) {
                            image = right1;
                        }
                        if (spriteNum == 2) {
                            image = right2;
                        }
                    }

                    if (attacking) {
                        if (spriteNum == 1) {
                            image = attackRight1;
                        }
                        if (spriteNum == 2) {
                            image = attackRight2;
                        }
                    }
                    break;
            }

            // MONSTER HP BAR
            if (type == 2 && hpBarOn && !dead) {

                // oneScale correspond à une unité de vie
                // on divise 48 par la vie max de l'entité
                // oneScale = 24 si maxLife = 2
                double oneScale = (double) gamePanel.getTileSize() / maxLife;
                int x = life;
                if (life < 0) {
                    x = 0;
                }
                double hpBarValue = oneScale * x;

                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX - 1, screenY - 16, gamePanel.getTileSize() + 2, 12);

                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);

                hpBarCounter++;

                if (hpBarCounter > 600) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if (invincible) {
                hpBarOn = true;
                hpBarCounter = 0;
                //Réduit l'opacité à 40%
                changeAlpha(g2, 0.4F);
            }

            if (dead) {
                dyingAnimation(g2);
            }

            g2.drawImage(image, tempScreenX, tempScreenY, null);

            // Met l'opacité à 100%
            changeAlpha(g2, 1F);
        }
    }

    public void dyingAnimation(Graphics2D g2){

        dyingCounter++;

        // Compteur de Frames
        int i = 5;

        if(dyingCounter <= i){ changeAlpha(g2, 0f); }

        if(dyingCounter > i && dyingCounter <= i*2) { changeAlpha(g2, 1f); }

        if(dyingCounter > i*2 && dyingCounter <= i*3) { changeAlpha(g2, 0f); }

        if(dyingCounter > i*3 && dyingCounter <= i*4) { changeAlpha(g2, 1f); }

        if(dyingCounter > i*4 && dyingCounter <= i*5) { changeAlpha(g2, 0f); }

        if(dyingCounter > i*5 && dyingCounter <= i*6) { changeAlpha(g2, 1f); }

        if(dyingCounter > i*6 && dyingCounter <= i*7) { changeAlpha(g2, 0f); }

        if(dyingCounter > i*7 && dyingCounter <= i*8) { changeAlpha(g2, 1f); }

        if(dyingCounter > i*8){
            alive = false;
        }
    }

    public void changeAlpha(Graphics2D g2, float alphaValue){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    public BufferedImage setup (String imagePath, int width, int height){

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {

            image = ImageIO.read(new FileInputStream(imagePath + ".png"));
            image = uTool.scaleImage(image, width, height);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void searchPath(int goalCol, int goalRow){

        int startCol = (worldX + solidArea.x) / gamePanel.getTileSize();
        int startRow = (worldY + solidArea.y) / gamePanel.getTileSize();

        gamePanel.getPathFinder().setNodes(startCol, startRow, goalCol, goalRow);

        if(gamePanel.getPathFinder().search()){

            // Next worldX & worldY
            int nextX = gamePanel.getPathFinder().getPathList().get(0).getCol() * gamePanel.getTileSize();
            int nextY = gamePanel.getPathFinder().getPathList().get(0).getRow() * gamePanel.getTileSize();

            // Entity's solidArea position
            int enLeftX = worldX + solidArea.x;
            int enRightX = worldX + solidArea.x + solidArea.width;
            int enTopY = worldY + solidArea.y;
            int enBottomY = worldY + solidArea.y + solidArea.height;

            if(enTopY > nextY && enLeftX >= nextX && enRightX < nextX + gamePanel.getTileSize()){
                direction = "up";

            } else if(enTopY < nextY && enLeftX >= nextX && enRightX < nextX + gamePanel.getTileSize()){
                direction = "down";

            } else if(enTopY >= nextY && enBottomY < nextY + gamePanel.getTileSize()){
                // left or right
                if(enLeftX > nextX){
                    direction ="left";
                }
                if(enLeftX < nextX){
                    direction = "right";
                }

            } else if(enTopY > nextY && enLeftX > nextX){
                // up or left
                direction = "up";
                checkCollision();
                if(collisionOn){
                    direction = "left";
                }

            }
        }
    }

    public int getDetected(Entity user, Entity[][] target, String targetName){

        int index = 999;

        // Check The surrounding object
        int nextWorldX = user.getLeftX();
        int nextWorldY = user.getTopY();

        switch (user.direction){
            case "up": nextWorldY = user.getTopY()-user.speed; break;
            case "down": nextWorldY = user.getBottomY()+user.speed; break;
            case "left": nextWorldX = user.getLeftX()-user.speed; break;
            case "right": nextWorldX = user.getRightX()+user.speed; break;
        }

        int col = nextWorldX / gamePanel.getTileSize();
        int row = nextWorldY / gamePanel.getTileSize();

        for(int i = 0; i < target[1].length; i++){
            if(target[gamePanel.getCurrentMap()][i] != null){
                if(target[gamePanel.getCurrentMap()][i].getCol() == col &&
                        target[gamePanel.getCurrentMap()][i].getRow() == row &&
                        target[gamePanel.getCurrentMap()][i].name.equals(targetName)){

                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    public boolean isOnPath() {
        return onPath;
    }

    public void setOnPath(boolean onPath) {
        this.onPath = onPath;
    }

    public boolean isStackable() {
        return stackable;
    }

    public void setStackable(boolean stackable) {
        this.stackable = stackable;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Entity getCurrentLight() {
        return currentLight;
    }

    public void setCurrentLight(Entity currentLight) {
        this.currentLight = currentLight;
    }

    public int getLightRadius() {
        return lightRadius;
    }

    public void setLightRadius(int lightRadius) {
        this.lightRadius = lightRadius;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public void setCrits(int crits) {
        this.crits = crits;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public Entity getAttacker() {
        return attacker;
    }

    public void setAttacker(Entity attacker) {
        this.attacker = attacker;
    }

    public String getKnockBackDirection() {
        return knockBackDirection;
    }

    public void setKnockBackDirection(String knockBackDirection) {
        this.knockBackDirection = knockBackDirection;
    }

    public int getMotion1_duration() {
        return motion1_duration;
    }

    public void setMotion1_duration(int motion1_duration) {
        this.motion1_duration = motion1_duration;
    }

    public int getMotion2_duration() {
        return motion2_duration;
    }

    public void setMotion2_duration(int motion2_duration) {
        this.motion2_duration = motion2_duration;
    }

    public int getWorldX() {
        return worldX;
    }

    public void setWorldX(int worldX) {
        this.worldX = worldX;
    }

    public int getWorldY() {
        return worldY;
    }

    public void setWorldY(int worldY) {
        this.worldY = worldY;
    }

    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public Rectangle getSolidArea() {
        return solidArea;
    }

    public void setSolidArea(Rectangle solidArea) {
        this.solidArea = solidArea;
    }

    public Rectangle getAttackArea() {
        return attackArea;
    }

    public void setAttackArea(Rectangle attackArea) {
        this.attackArea = attackArea;
    }

    public boolean isGuarding(){ return this.guarding; }

    public void setGuarding(boolean guarding){ this.guarding = guarding; }

    public boolean isTransparent(){ return this.transparent; }

    public void setTransparent(boolean transparent){ this.transparent = transparent; }

    public boolean isKnockBack() {
        return knockBack;
    }

    public void setKnockBack(boolean knockBack) {
        this.knockBack = knockBack;
    }

    public int getKnockBackCounter() {
        return knockBackCounter;
    }

    public void setKnockBackCounter(int knockBackCounter) {
        this.knockBackCounter = knockBackCounter;
    }

    public Entity getCurrentBoots() {
        return currentBoots;
    }

    public void setCurrentBoots(Entity currentBoots) {
        this.currentBoots = currentBoots;
    }

    public int getSpriteCounter() {
        return spriteCounter;
    }

    public void setSpriteCounter(int spriteCounter) {
        this.spriteCounter = spriteCounter;
    }

    public int getActionLockCounter() {
        return actionLockCounter;
    }

    public void setActionLockCounter(int actionLockCounter) {
        this.actionLockCounter = actionLockCounter;
    }

    public int getInvincibleCounter() {
        return invincibleCounter;
    }

    public void setInvincibleCounter(int invincibleCounter) {
        this.invincibleCounter = invincibleCounter;
    }

    public int getShotAvailableCounter() {
        return shotAvailableCounter;
    }

    public void setShotAvailableCounter(int shotAvailableCounter) {
        this.shotAvailableCounter = shotAvailableCounter;
    }

    public int getDyingCounter() {
        return dyingCounter;
    }

    public void setDyingCounter(int dyingCounter) {
        this.dyingCounter = dyingCounter;
    }

    public int getHpBarCounter() {
        return hpBarCounter;
    }

    public void setHpBarCounter(int hpBarCounter) {
        this.hpBarCounter = hpBarCounter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDefaultSpeed() {
        return defaultSpeed;
    }

    public void setDefaultSpeed(int defaultSpeed) {
        this.defaultSpeed = defaultSpeed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getKnockBackPower() {
        return knockBackPower;
    }

    public void setKnockBackPower(int knockBackPower) {
        this.knockBackPower = knockBackPower;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public int getDialogueIndex() {
        return dialogueIndex;
    }

    public void setDialogueIndex(int dialogueIndex) {
        this.dialogueIndex = dialogueIndex;
    }

    public boolean isOffBalance() {
        return offBalance;
    }

    public void setOffBalance(boolean offBalance) {
        this.offBalance = offBalance;
    }

    public int getGuardCounter() {
        return guardCounter;
    }

    public void setGuardCounter(int guardCounter) {
        this.guardCounter = guardCounter;
    }

    public int getOffBalanceCounter() {
        return offBalanceCounter;
    }

    public void setOffBalanceCounter(int offBalanceCounter) {
        this.offBalanceCounter = offBalanceCounter;
    }

    public int getCrits() {
        return crits;
    }

    public int getMaxInventorySize() {
        return maxInventorySize;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getAttackValue() {
        return attackValue;
    }

    public void setAttackValue(int attackValue) {
        this.attackValue = attackValue;
    }

    public int getDefenseValue() {
        return defenseValue;
    }

    public void setDefenseValue(int defenseValue) {
        this.defenseValue = defenseValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUseCost() {
        return useCost;
    }

    public void setUseCost(int useCost) {
        this.useCost = useCost;
    }

    public int getCritsValue() {
        return critsValue;
    }

    public void setCritsValue(int critsValue) {
        this.critsValue = critsValue;
    }

    public double getChanceOfCrits() {
        return chanceOfCrits;
    }

    public void setChanceOfCrits(double chanceOfCrits) {
        this.chanceOfCrits = chanceOfCrits;
    }

    public int getSolidAreaDefaultX() {
        return solidAreaDefaultX;
    }

    public void setSolidAreaDefaultX(int solidAreaDefaultX) {
        this.solidAreaDefaultX = solidAreaDefaultX;
    }

    public int getSolidAreaDefaultY() {
        return solidAreaDefaultY;
    }

    public void setSolidAreaDefaultY(int solidAreaDefaultY) {
        this.solidAreaDefaultY = solidAreaDefaultY;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getSpriteNum() {
        return spriteNum;
    }

    public void setSpriteNum(int spriteNum) {
        this.spriteNum = spriteNum;
    }

    public boolean isCollisionOn() {
        return collisionOn;
    }

    public void setCollisionOn(boolean collisionOn) {
        this.collisionOn = collisionOn;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isHpBarOn() {
        return hpBarOn;
    }

    public void setHpBarOn(boolean hpBarOn) {
        this.hpBarOn = hpBarOn;
    }

    public int getMobility() {
        return mobility;
    }

    public void setMobility(int mobility) {
        this.mobility = mobility;
    }

    public int getMaxLife() {
        return maxLife;
    }

    public void setMaxLife(int maxLife) {
        this.maxLife = maxLife;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getNextLevelExp() {
        return nextLevelExp;
    }

    public void setNextLevelExp(int nextLevelExp) {
        this.nextLevelExp = nextLevelExp;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public Entity getCurrentWeapon() {
        return currentWeapon;
    }

    public void setCurrentWeapon(Entity currentWeapon) {
        this.currentWeapon = currentWeapon;
    }

    public Entity getCurrentShield() {
        return currentShield;
    }

    public void setCurrentShield(Entity currentShield) {
        this.currentShield = currentShield;
    }

    public Projectile getProjectile() {
        return projectile;
    }

    public void setProjectile(Projectile projectile) {
        this.projectile = projectile;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public ArrayList<Entity> getInventory() {
        return inventory;
    }

    public void setInventory(ArrayList<Entity> inventory) {
        this.inventory = inventory;
    }

    public void setDialogue(int dialogueSet, int dialogueIndex, String newDialogue){
        this.dialogues[dialogueSet][dialogueIndex] = newDialogue;
    }

    public String getDialogue(int dialogueSet, int dialogueIndex){
        return this.dialogues[dialogueSet][dialogueIndex];
    }

    public int getDialogueSet() {
        return dialogueSet;
    }

    public void setDialogueSet(int dialogueSet) {
        this.dialogueSet = dialogueSet;
    }

    public Entity getLoot() {
        return loot;
    }

    public Entity getLinkedEntity() {
        return linkedEntity;
    }

    public void setLinkedEntity(Entity linkedEntity) {
        this.linkedEntity = linkedEntity;
    }
}
