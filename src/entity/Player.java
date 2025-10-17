package entity;

import main.EntityGenerator;
import main.GamePanel;
import main.KeyHandler;
import object.*;

import java.awt.AlphaComposite;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    private final GamePanel gamePanel = getGamePanel();
    private final KeyHandler keyH;

    private final int screenX;
    private final int screenY;
    private int standCounter = 0;
    private boolean attackCanceled = false;
    private boolean lightUpdated = false;


    public Player(GamePanel gp, KeyHandler keyH) {

        super(gp);
        this.keyH = keyH;

        screenX = gp.getScreenWidth() / 2 - (gp.getTileSize() / 2);
        screenY = gp.getScreenHeight() / 2 - (gp.getTileSize() / 2);

        // SOLID AREA
        setSolidArea(new Rectangle());
        getSolidArea().x = 8;
        getSolidArea().y = 16;
        setSolidAreaDefaultX(getSolidArea().x);
        setSolidAreaDefaultY(getSolidArea().y);

        // HITBOX
        getSolidArea().width = 30;
        getSolidArea().height = 30;

        setDefaultValues();
    }

    public void setDefaultValues() {

        setWorldX(gamePanel.getTileSize() * 23);
        setWorldY(gamePanel.getTileSize() * 21);

        gamePanel.setCurrentMap(0);
        setDirection("down");

        // PLAYER STATUS
        setLevel(0);
        setMaxLife(6);
        setLife(getMaxLife());
        setMaxMana(4);
        setMana(getMaxMana());
        setAmmo(10);
        setDefaultSpeed(3);
        setMobility(getDefaultSpeed());
        setStrength(1); // The more strength he has, the more damage he gives.
        setDexterity(1); // The more dexterity he has, the less damage he receives.
        setExp(0);
        setNextLevelExp(7);
        setCoin(500);
        setCurrentWeapon(new OBJ_Sword_Normal(gamePanel));
        setCurrentShield(new OBJ_Shield_Wood(gamePanel));
        setCurrentLight(null);
        setCurrentBoots(new OBJ_Boots(gamePanel));
        setProjectile(new OBJ_Fireball(gamePanel));
        setAttack(getAttack()); // The total attack value is decided by strength and weapon
        setDefense(getDefense()); // The total defense value is decided by dexterity and shield
        setSpeed(getPlayerMobility());
        setCrits(getCritique()); // The total attack crits value is decided by strength and Weapon Crits

        getImage();
        getAttackImage();
        getGuardImage();
        setItems();
        setDialogue();
    }

    public void setDefaultPositions(){

        gamePanel.setCurrentMap(0);

        setWorldX(gamePanel.getTileSize() * 23);
        setWorldY(gamePanel.getTileSize() * 21);
        setDirection("down");
    }

    private void setDialogue(){

        setDialogue(0, 0, "Tu es niveau " + getLevel() + " maintenant !\n"
                + "Tu te sens plus fort !");

    }

    public void restoreStatus(){

        setLife(getMaxLife());
        setMana(getMaxMana());
        setSpeed(getDefaultSpeed());
        setInvincible(false);
        setTransparent(false);
        setAttacking(false);
        setGuarding(false);
        setKnockBack(false);
        setLightUpdated(true);
    }

    /**
     * Initialise l'inventaire du joueur
     */
    public void setItems(){

        getInventory().clear();
        getInventory().add(getCurrentWeapon());
        getInventory().add(getCurrentShield());
        getInventory().add(getCurrentBoots());
        getInventory().add(new OBJ_Axe(gamePanel));
        getInventory().add(new OBJ_Ultimate_Sword(gamePanel));
        getInventory().add(new OBJ_Key(gamePanel));
        getInventory().add(new OBJ_Key(gamePanel));
    }

    @Override
    public int getAttack(){
        setAttackArea(getCurrentWeapon().getAttackArea());
        setMotion1_duration(getCurrentWeapon().getMotion1_duration());
        setMotion2_duration(getCurrentWeapon().getMotion2_duration());
        return getStrength() * getCurrentWeapon().getAttackValue();
    }

    public int getCritique(){
        return getStrength() * getCurrentWeapon().getCritsValue();
    }

    @Override
    public int getDefense(){
        return getDexterity() * getCurrentShield().getDefenseValue();
    }

    public int getCurrentWeaponSlot(){
        int currentWeaponSlot = 0;
        for(int i = 0; i < getInventory().size(); i++){
            if(getInventory().get(i) == getCurrentWeapon()){
                currentWeaponSlot = i;
            }
        }
        return currentWeaponSlot;
    }

    public int getCurrentShieldSlot(){
        int currentShieldSlot = 0;
        for(int i = 0; i < getInventory().size(); i++){
            if(getInventory().get(i) == getCurrentShield()){
                currentShieldSlot = i;
            }
        }
        return currentShieldSlot;
    }

    public int getCurrentBootSlot(){
        int currentBootSlot = 0;
        for(int i = 0; i < getInventory().size(); i++){
            if(getInventory().get(i) == getCurrentBoots()){
                currentBootSlot = i;
            }
        }
        return currentBootSlot;
    }

    public int getPlayerMobility() {
        return getMobility() * getCurrentBoots().getSpeed();
    }

    public void getImage() {

        up1 = setup("res/player/boy_up_1", gamePanel.getTileSize(), gamePanel.getTileSize());
        up2 = setup("res/player/boy_up_2", gamePanel.getTileSize(), gamePanel.getTileSize());
        down1 = setup("res/player/boy_down_1", gamePanel.getTileSize(), gamePanel.getTileSize());
        down2 = setup("res/player/boy_down_2", gamePanel.getTileSize(), gamePanel.getTileSize());
        left1 = setup("res/player/boy_left_1", gamePanel.getTileSize(), gamePanel.getTileSize());
        left2 = setup("res/player/boy_left_2", gamePanel.getTileSize(), gamePanel.getTileSize());
        right1 = setup("res/player/boy_right_1", gamePanel.getTileSize(), gamePanel.getTileSize());
        right2 = setup("res/player/boy_right_2", gamePanel.getTileSize(), gamePanel.getTileSize());
    }

    public void getSleepingImage(BufferedImage image){

        up1 = image;
        up2 = image;
        down1 = image;
        down2 = image;
        left1 = image;
        left2 = image;
        right1 = image;
        right2 = image;
    }

    public void getAttackImage() {

        if(getCurrentWeapon().type == TYPE_SWORD) {
            attackUp1 = setup("res/player/boy_attack_up_1", gamePanel.getTileSize(), gamePanel.getTileSize() * 2);
            attackUp2 = setup("res/player/boy_attack_up_2", gamePanel.getTileSize(), gamePanel.getTileSize() * 2);
            attackDown1 = setup("res/player/boy_attack_down_1", gamePanel.getTileSize(), gamePanel.getTileSize() * 2);
            attackDown2 = setup("res/player/boy_attack_down_2", gamePanel.getTileSize(), gamePanel.getTileSize() * 2);
            attackLeft1 = setup("res/player/boy_attack_left_1", gamePanel.getTileSize() * 2, gamePanel.getTileSize());
            attackLeft2 = setup("res/player/boy_attack_left_2", gamePanel.getTileSize() * 2, gamePanel.getTileSize());
            attackRight1 = setup("res/player/boy_attack_right_1", gamePanel.getTileSize() * 2, gamePanel.getTileSize());
            attackRight2 = setup("res/player/boy_attack_right_2", gamePanel.getTileSize() * 2, gamePanel.getTileSize());
        }

        if(getCurrentWeapon().type == TYPE_AXE){
            attackUp1 = setup("res/player/boy_axe_up_1", gamePanel.getTileSize(), gamePanel.getTileSize() * 2);
            attackUp2 = setup("res/player/boy_axe_up_2", gamePanel.getTileSize(), gamePanel.getTileSize() * 2);
            attackDown1 = setup("res/player/boy_axe_down_1", gamePanel.getTileSize(), gamePanel.getTileSize() * 2);
            attackDown2 = setup("res/player/boy_axe_down_2", gamePanel.getTileSize(), gamePanel.getTileSize() * 2);
            attackLeft1 = setup("res/player/boy_axe_left_1", gamePanel.getTileSize() * 2, gamePanel.getTileSize());
            attackLeft2 = setup("res/player/boy_axe_left_2", gamePanel.getTileSize() * 2, gamePanel.getTileSize());
            attackRight1 = setup("res/player/boy_axe_right_1", gamePanel.getTileSize() * 2, gamePanel.getTileSize());
            attackRight2 = setup("res/player/boy_axe_right_2", gamePanel.getTileSize() * 2, gamePanel.getTileSize());
        }

        if(getCurrentWeapon().type == TYPE_PICKAXE){
            attackUp1 = setup("res/player/boy_pick_up_1", gamePanel.getTileSize(), gamePanel.getTileSize() * 2);
            attackUp2 = setup("res/player/boy_pick_up_2", gamePanel.getTileSize(), gamePanel.getTileSize() * 2);
            attackDown1 = setup("res/player/boy_pick_down_1", gamePanel.getTileSize(), gamePanel.getTileSize() * 2);
            attackDown2 = setup("res/player/boy_pick_down_2", gamePanel.getTileSize(), gamePanel.getTileSize() * 2);
            attackLeft1 = setup("res/player/boy_pick_left_1", gamePanel.getTileSize() * 2, gamePanel.getTileSize());
            attackLeft2 = setup("res/player/boy_pick_left_2", gamePanel.getTileSize() * 2, gamePanel.getTileSize());
            attackRight1 = setup("res/player/boy_pick_right_1", gamePanel.getTileSize() * 2, gamePanel.getTileSize());
            attackRight2 = setup("res/player/boy_pick_right_2", gamePanel.getTileSize() * 2, gamePanel.getTileSize());
        }
    }

    public void getGuardImage(){

        guardUp = setup("res/player/boy_guard_up", gamePanel.getTileSize(), gamePanel.getTileSize());
        guardDown = setup("res/player/boy_guard_down", gamePanel.getTileSize(), gamePanel.getTileSize());
        guardLeft = setup("res/player/boy_guard_left", gamePanel.getTileSize(), gamePanel.getTileSize());
        guardRight = setup("res/player/boy_guard_right", gamePanel.getTileSize(), gamePanel.getTileSize());
    }

    @Override
    public void update() {

        if (isKnockBack()) {

            setCollisionOn(false);
            gamePanel.getCollisionChecker().checkTile(this);
            gamePanel.getCollisionChecker().checkObject(this, true);
            gamePanel.getCollisionChecker().checkEntity(this, gamePanel.getNpc());
            gamePanel.getCollisionChecker().checkEntity(this, gamePanel.getMonster());
            gamePanel.getCollisionChecker().checkEntity(this, gamePanel.getInteractiveTile());

            if (isCollisionOn()) {
                setKnockBackCounter(0);
                setKnockBack(false);
                setSpeed(getDefaultSpeed());

            } else if (!isCollisionOn()) {
                switch (getKnockBackDirection()) {
                    case "up":
                        setWorldY(getWorldY() - getSpeed());
                        break;
                    case "down":
                        setWorldY(getWorldY() + getSpeed());
                        break;
                    case "left":
                        setWorldX(getWorldX() - getSpeed());
                        break;
                    case "right":
                        setWorldX(getWorldX() + getSpeed());
                        break;
                }
            }

            setKnockBackCounter(getKnockBackCounter() + 1);
            if (getKnockBackCounter() == 10) {
                setKnockBackCounter(0);
                setKnockBack(false);
                setSpeed(getDefaultSpeed());
            }

        } else if(isAttacking()){
            attacking();

        } else if(keyH.isSpacePressed()){
            setGuarding(true);
            setGuardCounter(getGuardCounter() + 1);

        }else if (keyH.isUpPressed() || keyH.isDownPressed() || keyH.isLeftPressed() || keyH.isRightPressed() || keyH.isEnterPressed()) {

            if (keyH.isUpPressed()) {
                setDirection("up");
            }

            if (keyH.isDownPressed()) {
                setDirection("down");
            }

            if (keyH.isLeftPressed()) {
                setDirection("left");
            }

            if (keyH.isRightPressed()) {
                setDirection("right");
            }

            // VERIFIE LA COLLISION AVEC UNE TUILE
            setCollisionOn(false);
            getGamePanel().getCollisionChecker().checkTile(this);

            // CHECK OBJECT COLLISION
            int objIndex = gamePanel.getCollisionChecker().checkObject(this, true);
            pickUpObject(objIndex);

            // CHECK NPC COLLISION
            int npcIndex = gamePanel.getCollisionChecker().checkEntity(this, gamePanel.getNpc());
            interactNPC(npcIndex);

            // CHECK MONSTER COLLISION
            int monsterIndex = gamePanel.getCollisionChecker().checkEntity(this, gamePanel.getMonster());
            contactMonster(monsterIndex);

            // CHECK INTERACTIVE TILE COLLISION
            gamePanel.getCollisionChecker().checkEntity(this, gamePanel.getInteractiveTile());

            // CHECK EVENT
            gamePanel.getEventHandler().checkEvent();

            // SI LA COLLISION EST FALSE,
            // ET QUE LA TOUCHE ENTRER N'EST PAS TOUCHER,
            // LE JOUEUR PEUT BOUGER
            if ((!isCollisionOn()) && (!keyH.isEnterPressed())) {

                switch (getDirection()) {
                    case "up": setWorldY(getWorldY() - getSpeed());
                    break;

                    case "down": setWorldY(getWorldY() + getSpeed());
                    break;

                    case "left": setWorldX(getWorldX() - getSpeed());
                    break;

                    case "right": setWorldX(getWorldX() + getSpeed());
                    break;
                }
            }

            if(keyH.isEnterPressed() && !attackCanceled){
                gamePanel.playSE(7);
                setAttacking(true);
                setSpriteCounter(0);
            }

            attackCanceled = false;
            gamePanel.getKeyHandler().setEnterPressed(false);
            setGuarding(false);
            setGuardCounter(0);

            setSpriteCounter(getSpriteCounter() + 1);
            if (getSpriteCounter() > 12) {
                if (getSpriteNum() == 1) {
                    setSpriteNum(2);
                } else if (getSpriteNum() == 2) {
                    setSpriteNum(1);
                }
                setSpriteCounter(0);
            }

        } else {
            standCounter++;
            if (standCounter == 20) {
                setSpriteNum(1);
                standCounter = 0;
            }
            setGuarding(false);
            setGuardCounter(0);
        }

        if(gamePanel.getKeyHandler().isShotKeyPressed() && !getProjectile().isAlive()
                && getShotAvailableCounter() == 30 && getProjectile().hasResource(this)){

            // SET DEFAULT COORDINATES, DIRECTION AND USER
            getProjectile().set(getWorldX(), getWorldY(), getDirection(), true, this);

            // SUBTRACT THE COST (MANA, AMMO, ETC...)
            getProjectile().subtractResource(this);

            // CHECK VACANCY
            for(int i = 0; i < gamePanel.getProjectile()[1].length; i++){
                if(gamePanel.getProjectile(gamePanel.getCurrentMap(), i) == null){
                    gamePanel.setProjectile(gamePanel.getCurrentMap(), i, getProjectile());
                    break;
                }
            }

            setShotAvailableCounter(0);

            gamePanel.playSE(10);
        }

        // This needs to be outside of key if statement !
        if (isInvincible()) {
            setInvincibleCounter(getInvincibleCounter() + 1);
            if (getInvincibleCounter() > 60) {
                setInvincible(false);
                setTransparent(false);
                setInvincibleCounter(0);
            }
        }

        if(getShotAvailableCounter() < 30){
            setShotAvailableCounter(getShotAvailableCounter() + 1);
        }

        if(getLife() > getMaxLife()){
            setLife(getMaxLife());
        }
        if(getMana() > getMaxMana()){
            setMana(getMaxMana());
        }

        if(!keyH.isGodModeOn()) {

            if (getLife() <= 0) {
                gamePanel.gameState = GamePanel.GAME_OVER_STATE;
                gamePanel.getUi().commandNum = -1;
                gamePanel.stopMusic();
                gamePanel.playSE(12);
            }
        }
    }

    public void pickUpObject(int index) {

        if (index != 999) {

            //PICK ONLY ITEMS
            if(gamePanel.getObj(gamePanel.getCurrentMap(), index).type == TYPE_PICKUP_ONLY){ //FIXED

                gamePanel.getObj(gamePanel.getCurrentMap(), index).use(this); //FIXED
                gamePanel.setObj(gamePanel.getCurrentMap(), index, null); //FIXED

            }
            // OBSTACLE
            else if(gamePanel.getObj(gamePanel.getCurrentMap(), index).type == TYPE_OBSTACLE){
                if(keyH.isEnterPressed()){
                    attackCanceled = true;
                    gamePanel.getObj(gamePanel.getCurrentMap(), index).interact();
                }
            }
            //INVENTORY ITEMS
            else {
                String text;

                if(canObtainItem(gamePanel.getObj(gamePanel.getCurrentMap(), index))){
                    gamePanel.playSE(1);

                    switch (gamePanel.getObj(gamePanel.getCurrentMap(), index).getName()){ //FIXED
                        case "Clé", "Hache", "Potion Rouge", "Potion Bleu", "Lanterne", "Tente":
                            text = "Obtenue une " + gamePanel.getObj(gamePanel.getCurrentMap(), index).getName() + " !"; //FIXED
                            break;
                        case "Bottes":
                            text = "Obtenue des " + gamePanel.getObj(gamePanel.getCurrentMap(), index).getName() + " !"; //FIXED
                            break;
                        default:
                            text = "Obtenue un " + gamePanel.getObj(gamePanel.getCurrentMap(), index).getName() + "!";
                            break;
                    }

                } else {
                    text = "Tu ne peux porter plus !";
                }
                gamePanel.getUi().addMessage(text);
                gamePanel.setObj(gamePanel.getCurrentMap(), index, null); //FIXED DON'T FORGET THIS

            }
        }
    }

    public void interactNPC(int i) {

        if (i != 999) {
            if (gamePanel.getKeyHandler().isEnterPressed()) {

                attackCanceled = true;
                gamePanel.getNpc(gamePanel.getCurrentMap(), i).speak();

            }

            gamePanel.getNpc(gamePanel.getCurrentMap(), i).move(getDirection());
        }
    }

    public void contactMonster(int i){

        if( i != 999){

            if(!isInvincible() && !gamePanel.getMonster(gamePanel.getCurrentMap(), i).isDead()) { //FIXED
                gamePanel.playSE(6);

                int damage = gamePanel.getMonster(gamePanel.getCurrentMap(), i).getAttack() - getDefense(); //FIXED
                if(damage < 1){
                    damage = 1;
                }
                setLife(getLife() - damage);
                setInvincible(true);
                setTransparent(true);
            }
        }
    }

    public void damageMonster(int i, Entity attacker, int attack, int knockBackPower){

        double randomCrits = Math.random() * 100.0;

        if(i != 999){

            if(!gamePanel.getMonster(gamePanel.getCurrentMap(), i).isInvincible()){ //FIXED

                gamePanel.playSE(5);

                if(knockBackPower > 0) {
                    setKnockBack(gamePanel.getMonster(gamePanel.getCurrentMap(), i), attacker, knockBackPower);
                }

                if(gamePanel.getMonster(gamePanel.getCurrentMap(), i).isOffBalance()){
                    attack *= 5;
                }

                int damage = attack - gamePanel.getMonster(gamePanel.getCurrentMap(), i).getDefense(); //FIXED
                if(randomCrits < getCurrentWeapon().getChanceOfCrits()){
                    damage = getCritique() - gamePanel.getMonster(gamePanel.getCurrentMap(), i).getDefense(); //FIXED
                }

                if(damage < 0){
                    damage = 0;
                }
                gamePanel.getMonster(gamePanel.getCurrentMap(), i).setLife(gamePanel.getMonster(gamePanel.getCurrentMap(), i).getLife() - damage); //FIXED
                gamePanel.getUi().addMessage(damage + " damage !");

                gamePanel.getMonster(gamePanel.getCurrentMap(), i).setInvincible(true); //FIXED
                gamePanel.getMonster(gamePanel.getCurrentMap(), i).damageReaction(); //FIXED

                if(gamePanel.getMonster(gamePanel.getCurrentMap(), i).getLife() <= 0){ //FIXED
                    gamePanel.getMonster(gamePanel.getCurrentMap(), i).setDead(true); //FIXED
                    gamePanel.getUi().addMessage("Tu as tué le " + gamePanel.getMonster(gamePanel.getCurrentMap(), i).getName() + "!"); //FIXED
                    gamePanel.getUi().addMessage("Exp " + gamePanel.getMonster(gamePanel.getCurrentMap(), i).getExp()); //FIXED
                    setExp(getExp() + gamePanel.getMonster(gamePanel.getCurrentMap(), i).getExp()); ;
                    checkLevelUp();
                }
            }
        }
    }

    public void damageInteractiveTile(int i){

        if(i != 999 && gamePanel.getInteractiveTile(gamePanel.getCurrentMap(), i).isDestructible() //FIXED
                && gamePanel.getInteractiveTile(gamePanel.getCurrentMap(), i).isCorrectItem(this) && !gamePanel.getInteractiveTile(gamePanel.getCurrentMap(), i).isInvincible()){ //FIXED

            gamePanel.getInteractiveTile(gamePanel.getCurrentMap(), i).playSE(); //FIXED
            gamePanel.getInteractiveTile(gamePanel.getCurrentMap(), i).setLife(gamePanel.getInteractiveTile(gamePanel.getCurrentMap(), i).getLife() - 1); //FIXED
            gamePanel.getInteractiveTile(gamePanel.getCurrentMap(), i).setInvincible(true); //FIXED

            // Generate Particles
            generateParticle(gamePanel.getInteractiveTile(gamePanel.getCurrentMap(), i), gamePanel.getInteractiveTile(gamePanel.getCurrentMap(), i)); //FIXED
            generateParticle(gamePanel.getInteractiveTile(gamePanel.getCurrentMap(), i), gamePanel.getInteractiveTile(gamePanel.getCurrentMap(), i)); //FIXED

            if(gamePanel.getInteractiveTile(gamePanel.getCurrentMap(), i).getLife() == 0) { //FIXED
//                gamePanel.getInteractiveTile(gamePanel.getCurrentMap(), i).checkDrop();
                gamePanel.setInteractiveTile(gamePanel.getCurrentMap(), i, gamePanel.getInteractiveTile(gamePanel.getCurrentMap(), i).getDestroyedForm()); //FIXED

            }
        }
    }

    public void damageProjectile(int projectileIndex){

        if(projectileIndex != 999){
            Entity projectile = gamePanel.getProjectile(gamePanel.getCurrentMap(), projectileIndex);
            projectile.setAlive(false);
            generateParticle(projectile, projectile);
        }

    }

    private void checkLevelUp() {

        if(getExp() >= getNextLevelExp()){

            setLevel(getLevel() + 1);
            setExp(getExp() - getNextLevelExp());
            if(getLevel() < 15){
                setNextLevelExp(2 * getLevel() + 7);

            } else if (getLevel() > 16 && getLevel() < 30){
                setNextLevelExp(5 * getLevel() - 38);

            } else {
                setNextLevelExp(9 * getLevel() - 158);

            }

            setMaxLife(getMaxLife() + 2);
            setLife(getMaxLife());
            setMaxMana(getMaxMana() + 1);
            setMana(getMaxMana());

            setStrength(getStrength() + 1);
            setDexterity(getDexterity() + 1);
            setAttack(getAttack());
            setDefense(getDefense());
            setCrits(getCritique());

            gamePanel.playSE(8);
            gamePanel.gameState = GamePanel.DIALOGUE_STATE;
            setDialogue();
            startDialogue(this, 0);
        }
    }

    public void selectItem(){

        int itemIndex = gamePanel.getUi().getItemIndexOnSlot(gamePanel.getUi().playerSlotCol, gamePanel.getUi().playerSlotRow);

        if(itemIndex < getInventory().size()){

            Entity selectedItem = getInventory().get(itemIndex);

            if(selectedItem.type == TYPE_SWORD || selectedItem.type == TYPE_AXE || selectedItem.type == TYPE_PICKAXE){

                setCurrentWeapon(selectedItem);
                setAttack(getAttack());
                getAttackImage();
            }
            if(selectedItem.type == TYPE_SHIELD){

                setCurrentShield(selectedItem);
                setDefense(getDefense());
            }
            if(selectedItem.type == TYPE_BOOTS){

                setCurrentBoots(selectedItem);
                setSpeed(getMobility());
            }
            if(selectedItem.type == TYPE_LIGHT){

                if(getCurrentLight() == selectedItem){
                    setCurrentLight(null);

                } else {
                    setCurrentLight(selectedItem);
                }
                lightUpdated = true;
            }
            if(selectedItem.type == TYPE_CONSUMABLE){

                if(selectedItem.use(this)){
                    if(selectedItem.getAmount() > 1){
                        int amount = selectedItem.getAmount();
                        amount--;
                        selectedItem.setAmount(amount);

                    } else {
                        getInventory().remove(itemIndex);

                    }
                }
            }
        }
    }

    public int searchItemInInventory(String itemName){

        int itemIndex = 999;

        for(int i = 0; i < getInventory().size(); i++){
            if(getInventory().get(i).getName().equals(itemName)){
                itemIndex = i;
                break;
            }
        }
        return itemIndex;
    }

    public boolean canObtainItem(Entity item){

        boolean canObtain = false;

        Entity newItem = gamePanel.getEntityGenerator().getObject(item.getName());

        // CHECK IF STACKABLE
        if(newItem.isStackable()){

            int index = searchItemInInventory(newItem.getName());

            if(index != 999){
                int amount = getInventory().get(index).getAmount();
                amount++;
                getInventory().get(index).setAmount(amount);
                canObtain = true;

            } else { // New item so need to check vacancy
                if(getInventory().size() != getMaxInventorySize()){
                    getInventory().add(newItem);
                    canObtain = true;
                }
            }

        } else { // NOT STACKABLE so check vacancy
            if(getInventory().size() != getMaxInventorySize()){
                getInventory().add(newItem);
                canObtain = true;
            }
        }

        return canObtain;
    }

    @Override
    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch (getDirection()) {
            case "up":
                if(!isAttacking()) {
                    if(getSpriteNum() == 1) image = up1;
                    if(getSpriteNum() == 2) image = up2;
                }

                if(isAttacking()) {
                    tempScreenY = screenY - gamePanel.getTileSize();
                    if(getSpriteNum() == 1) image = attackUp1;
                    if(getSpriteNum() == 2) image = attackUp2;
                }
                if(isGuarding()){
                    image = guardUp;
                }
            break;
            case "down":
                if(!isAttacking()) {
                    if(getSpriteNum() == 1) image = down1;
                    if(getSpriteNum() == 2) image = down2;
                }
                if(isAttacking()) {
                    if(getSpriteNum() == 1) image = attackDown1;
                    if(getSpriteNum() == 2) image = attackDown2;
                }
                if(isGuarding()){
                    image = guardDown;
                }
            break;
            case  "left":
                if(!isAttacking()) {
                    if(getSpriteNum() == 1) image = left1;
                    if(getSpriteNum() == 2) image = left2;
                }
                if(isAttacking()) {
                    tempScreenX = screenX - gamePanel.getTileSize();
                    if(getSpriteNum() == 1) image = attackLeft1;
                    if(getSpriteNum() == 2) image = attackLeft2;
                }
                if(isGuarding()){
                    image = guardLeft;
                }
            break;
            case "right":
                if(!isAttacking()) {
                    if(getSpriteNum() == 1) image = right1;
                    if(getSpriteNum() == 2) image = right2;
                }
                if(isAttacking()) {
                    if(getSpriteNum() == 1) image = attackRight1;
                    if(getSpriteNum() == 2) image = attackRight2;
                }
                if(isGuarding()){
                    image = guardRight;
                }
            break;
        }

        if(isTransparent()) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        }

        g2.drawImage(image, tempScreenX, tempScreenY, null);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public boolean isLightUpdated() {
        return lightUpdated;
    }

    public void setLightUpdated(boolean lightUpdated) {
        this.lightUpdated = lightUpdated;
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    public int getStandCounter() {
        return standCounter;
    }

    public void setStandCounter(int standCounter) {
        this.standCounter = standCounter;
    }

    public boolean isAttackCanceled() {
        return attackCanceled;
    }

    public void setAttackCanceled(boolean attackCanceled) {
        this.attackCanceled = attackCanceled;
    }
}
