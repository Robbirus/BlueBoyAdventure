package main;

import entity.Entity;

public class EventHandler {

    private final GamePanel gp;
    private EventRect[][][] eventRect;
    private Entity eventMaster;

    private int previousEventX;
    private int previousEventY;
    private boolean canTouchEvent = true;
    private int tempMap;
    private int tempCol;
    private int tempRow;

    public EventHandler(GamePanel gp){
        this.gp = gp;

        eventMaster = new Entity(gp);

        eventRect = new EventRect[gp.getMaxMap()][gp.getMaxWorldCol()][gp.getMaxWorldRow()];

        int map = 0;
        int col = 0;
        int row = 0;
        while(map < gp.getMaxMap() && col < gp.getMaxWorldCol() && row < gp.getMaxWorldRow()) {

            eventRect[map][col][row] = new EventRect();
            eventRect[map][col][row].x = 23;
            eventRect[map][col][row].y = 23;
            eventRect[map][col][row].width = 2;
            eventRect[map][col][row].height = 2;
            eventRect[map][col][row].setEventRectDefaultX(eventRect[map][col][row].x);
            eventRect[map][col][row].setEventRectDefaultY(eventRect[map][col][row].y);

            col++;
            if(col == gp.getMaxWorldCol()){
                col = 0;
                row++;

                if(row == gp.getMaxWorldRow()){
                    row = 0;
                    map++;
                }
            }
        }

        setDialogue();
    }

    private void setDialogue(){

        eventMaster.setDialogue(0, 0, "Tu es tombé dans un trou !");

        eventMaster.setDialogue(1, 0, "Tu bois l'eau. Ta Vie et ta Mana sont restaurés. \nLe mal resurgit... (Partie Sauvegardée)");
        eventMaster.setDialogue(1, 1, "Hmm... Ce qu'elle est bonne l'eau !");

        eventMaster.setDialogue(2, 0, "Ta vitesse a augmenté !");

        eventMaster.setDialogue(3, 0, "Transfert Terminé");
    }

    public void checkEvent(){

        // Check if the player character is more than 1 title away from the last event
        int xDistance = Math.abs(gp.getPlayer().getWorldX() - previousEventX);
        int yDistance = Math.abs(gp.getPlayer().getWorldY() - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if(distance > gp.getTileSize()){
            canTouchEvent = true;
        }

        if(canTouchEvent){

            if(isHit(0,34,36, "any")){ warp(GamePanel.DIALOGUE_STATE); }

            else if(isHit(0,27,16, "right")){ damagePit(GamePanel.DIALOGUE_STATE); }

            else if(isHit(0,23,12, "up")){ healingPool(GamePanel.DIALOGUE_STATE); }

            else if(isHit(0, 10, 39, "any")){ teleport(1, 12, 13, GamePanel.INDOOR); } // To the merchant's House

            else if(isHit(1, 12, 13, "any")){ teleport(0, 10, 39, GamePanel.OUTSIDE); } // To outside

            else if(isHit(1,12,9,"up")){ speak(gp.getNpc(1, 0));}

            else if(isHit(0, 12, 9, "any")){ teleport(2, 9, 41, GamePanel.DUNGEON); } // To the dungeon

            else if(isHit(2, 9, 41, "any")){ teleport(0, 12, 9, GamePanel.OUTSIDE); } // To outside

            else if(isHit(2, 8, 7, "any")){ teleport(3, 26, 41, GamePanel.DUNGEON); } // To B2

            else if(isHit(3, 26, 41, "any")){ teleport(2, 8, 7, GamePanel.DUNGEON); } // To B1
        }
    }

    public boolean isHit(int map, int col, int row, String reqDirection){

        boolean hit = false;

        if(map == gp.getCurrentMap()) {

            gp.getPlayer().getSolidArea().x = gp.getPlayer().getWorldX() + gp.getPlayer().getSolidArea().x;
            gp.getPlayer().getSolidArea().y = gp.getPlayer().getWorldY() + gp.getPlayer().getSolidArea().y;
            eventRect[map][col][row].x = col * gp.getTileSize() + eventRect[map][col][row].x;
            eventRect[map][col][row].y = row * gp.getTileSize() + eventRect[map][col][row].y;

            if (gp.getPlayer().getSolidArea().intersects(eventRect[map][col][row]) && !eventRect[map][col][row].isEventDone()) {
                if (gp.getPlayer().getDirection().contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                    hit = true;

                    previousEventX = gp.getPlayer().getWorldX();
                    previousEventY = gp.getPlayer().getWorldY();
                }
            }

            gp.getPlayer().getSolidArea().x = gp.getPlayer().getSolidAreaDefaultX();
            gp.getPlayer().getSolidArea().y = gp.getPlayer().getSolidAreaDefaultY();
            eventRect[map][col][row].x = eventRect[map][col][row].getEventRectDefaultX();
            eventRect[map][col][row].y = eventRect[map][col][row].getEventRectDefaultY();
        }
        return hit;
    }

    public void warp(int gameState){

        gp.gameState = gameState;
        eventMaster.startDialogue(eventMaster, 3);
        gp.getPlayer().setWorldX(gp.getTileSize() * 23);
        gp.getPlayer().setWorldY(gp.getTileSize() * 7);
    }

    public void speedBoost(int gameState){

        gp.gameState = gameState;
        eventMaster.startDialogue(eventMaster, 2);
        if(gp.getPlayer().getSpeed() == 6) {
            gp.getPlayer().setSpeed(7);
            gp.getPlayer().getProjectile().setSpeed(8);

        }else {
            gp.getPlayer().setSpeed(5);
            gp.getPlayer().getProjectile().setSpeed(7);
        }

        //eventRect[col][row].eventDone = true;
        canTouchEvent = false;

    }

    public void damagePit(int gameState){

        gp.gameState = gameState;
        gp.playSE(6);
        eventMaster.startDialogue(eventMaster, 0);
        gp.getPlayer().setLife(gp.getPlayer().getLife() - 1);
        canTouchEvent = false;
    }

    public void healingPool(int gameState){

        if(gp.getKeyHandler().isEnterPressed()) {
            gp.gameState = gameState;
            gp.getPlayer().setAttackCanceled(true);
            gp.playSE(2);
            eventMaster.startDialogue(eventMaster, 7);
            gp.getPlayer().setLife(gp.getPlayer().getMaxLife());
            gp.getPlayer().setMana(gp.getPlayer().getMaxMana());
            gp.getAssetSetter().setMonster();
            gp.getSaveLoad().save();
        }
    }

    public void teleport(int map, int col, int row, int area){

        gp.gameState = GamePanel.TRANSITION_STATE;
        gp.nextArea = area;
        tempMap = map;
        tempCol = col;
        tempRow = row;

        canTouchEvent = false;
        gp.playSE(13);

    }

    public void speak(Entity entity){

        if(gp.getKeyHandler().isEnterPressed()){
            gp.gameState = GamePanel.DIALOGUE_STATE;
            gp.getPlayer().setAttackCanceled(true);
            entity.speak();
        }
    }


    public int getPreviousEventX() {
        return previousEventX;
    }

    public void setPreviousEventX(int previousEventX) {
        this.previousEventX = previousEventX;
    }

    public int getPreviousEventY() {
        return previousEventY;
    }

    public void setPreviousEventY(int previousEventY) {
        this.previousEventY = previousEventY;
    }

    public boolean isCanTouchEvent() {
        return canTouchEvent;
    }

    public void setCanTouchEvent(boolean canTouchEvent) {
        this.canTouchEvent = canTouchEvent;
    }

    public int getTempMap() {
        return tempMap;
    }

    public void setTempMap(int tempMap) {
        this.tempMap = tempMap;
    }

    public int getTempCol() {
        return tempCol;
    }

    public void setTempCol(int tempCol) {
        this.tempCol = tempCol;
    }

    public int getTempRow() {
        return tempRow;
    }

    public void setTempRow(int tempRow) {
        this.tempRow = tempRow;
    }
}
