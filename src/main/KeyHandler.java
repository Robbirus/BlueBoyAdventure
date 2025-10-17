package main;

import tile.TileManager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    private final GamePanel gp;
    private TileManager tileManager;
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean enterPressed;
    private boolean shotKeyPressed;
    private boolean spacePressed;

    // DEBUG
    private boolean debugPressed = false;
    private boolean godModeOn = false;

    public KeyHandler(GamePanel gp) {

        this.gp = gp;
        this.tileManager = new TileManager(gp);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        // TITLE STATE
        if(gp.gameState == GamePanel.TITLE_STATE){
            titleState(code);

        }
        // PLAY STATE
        else if (gp.gameState == GamePanel.PLAY_STATE) {
            playState(code);

        }
        // PAUSE STATE
        else if (gp.gameState == GamePanel.PAUSE_STATE) {
            pauseState(code);

        }
        // DIALOGUE STATE
        else if (gp.gameState == GamePanel.DIALOGUE_STATE) {
            dialogueState(code);

        }
        // CHARACTER STATE
        else if(gp.gameState == GamePanel.CHARACTER_STATE){
            characterState(code);

        }
        // OPTIONS STATE
        else if(gp.gameState == GamePanel.OPTION_STATE){
            optionState(code);

        }
        // GAME OVER STATE
        else if(gp.gameState == GamePanel.GAME_OVER_STATE){
            gameOverState(code);

        }
        // TRADE STATE
        else if(gp.gameState == GamePanel.TRADE_STATE){
            tradeState(code);

        }
        // MAP STATE
        else if(gp.gameState == GamePanel.MAP_STATE){
            mapState(code);

        }
    }

    public void titleState(int code) {
        if (gp.getUi().titleScreenState == 0) {

            if (code == KeyEvent.VK_Z) {
                gp.getUi().commandNum--;
                if (gp.getUi().commandNum < 0) {
                    gp.getUi().commandNum = 2;
                }
            }

            if (code == KeyEvent.VK_S) {
                gp.getUi().commandNum++;
                if (gp.getUi().commandNum > 2) {
                    gp.getUi().commandNum = 0;
                }
            }

            if (code == KeyEvent.VK_ENTER) {
                // NEW GAME
                if (gp.getUi().commandNum == 0) {
                    gp.getUi().titleScreenState = 1;
                }

                // LOAD GAME
                if (gp.getUi().commandNum == 1) {
                    gp.getSaveLoad().load();
                    gp.gameState = GamePanel.PLAY_STATE;
                    gp.playSE(0);
                }

                // EXIT GAME
                if (gp.getUi().commandNum == 2) {
                    System.exit(0);
                }
            }

        } else if (gp.getUi().titleScreenState == 1) {

            if (code == KeyEvent.VK_Z) {
                gp.getUi().commandNum--;
                if (gp.getUi().commandNum < 0) {
                    gp.getUi().commandNum = 3;
                }
            }

            if (code == KeyEvent.VK_S) {
                gp.getUi().commandNum++;
                if (gp.getUi().commandNum > 3) {
                    gp.getUi().commandNum = 0;
                }
            }

            if (code == KeyEvent.VK_ENTER) {
                if (gp.getUi().commandNum == 0) {
                    System.out.println("Do some Fighter specific stuff !");
                    gp.gameState = GamePanel.PLAY_STATE;
                    gp.stopMusic();
                    gp.playMusic(0);
                }

                if (gp.getUi().commandNum == 1) {
                    System.out.println("Do some Thief specific stuff !");
                    gp.getPlayer().setSpeed(6);
                    gp.getPlayer().getProjectile().setSpeed(9);
                    gp.gameState = GamePanel.PLAY_STATE;
                    gp.stopMusic();
                    gp.playMusic(0);
                }

                if (gp.getUi().commandNum == 2) {
                    System.out.println("Do some Wizard specific stuff !");
                    gp.gameState = GamePanel.PLAY_STATE;
                    gp.getPlayer().setSpeed(3);
                    gp.getPlayer().setMaxMana(6);
                    gp.getPlayer().setMana(gp.getPlayer().getMaxMana());
                    gp.stopMusic();
                    gp.playMusic(0);
                }

                if (gp.getUi().commandNum == 3) {
                    gp.getUi().titleScreenState = 0;
                    //gp.stopMusic();
                }
            }
        }
    }

    public void playState(int code){

        //Flèche Haute
        if (code == KeyEvent.VK_Z) {
            upPressed = true;
        }
        //Flèche Bas
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        //Flèche Gauche
        if (code == KeyEvent.VK_Q) {
            leftPressed = true;
        }
        //Flèche Droite
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        //Touche PAUSE P
        if (code == KeyEvent.VK_P) {
            gp.gameState = GamePanel.PAUSE_STATE;
        }
        // Touche PAUSE C
        if(code == KeyEvent.VK_C){
            gp.gameState = GamePanel.CHARACTER_STATE;
        }
        //Touche ENTER
        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        //Touche F Projectile
        if (code == KeyEvent.VK_F) {
            shotKeyPressed = true;
        }
        //Touche ESC
        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = GamePanel.OPTION_STATE;
        }
        //Touche M
        if (code == KeyEvent.VK_M) {
            gp.gameState = GamePanel.MAP_STATE;
        }
        //Touche X
        if (code == KeyEvent.VK_X) {
            if(!gp.getMap().isMiniMapOn()){
                gp.getMap().setMiniMapOn(true);

            } else {
                gp.getMap().setMiniMapOn(false);
            }
        }
        //Touche SPACE
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = true;
        }

        // DEBUG
        if (code == KeyEvent.VK_T) {
            if (!debugPressed) {
                debugPressed = true;

            } else {
                debugPressed = false;
            }
        }

        // DEBUG
        if (code == KeyEvent.VK_R) {
            switch (gp.getCurrentMap()){
                case 0: gp.getTileManager().loadMap("res/maps/worldV3.txt", 0);
                case 1: gp.getTileManager().loadMap("res/maps/interior01.txt", 1);

            }
            gp.getUi().addMessage("Map reloaded");
        }

        // GODMODE
        if (code == KeyEvent.VK_G) {
            if (!godModeOn) {
                godModeOn = true;

            } else {
                godModeOn = false;
            }
        }

    }

    public void pauseState(int code){
        //Touche PAUSE P
        if (code == KeyEvent.VK_P) {
            gp.gameState = GamePanel.PLAY_STATE;

        }
    }

    public void dialogueState(int code){

        if(code == KeyEvent.VK_ENTER){
            enterPressed = true;

        }
    }

    public void characterState(int code){

        if(code == KeyEvent.VK_C){
            gp.gameState = GamePanel.PLAY_STATE;
        }
        if(code == KeyEvent.VK_ENTER){
            gp.getPlayer().selectItem();
        }
        playerInventory(code);
    }

    private void optionState(int code) {

        if(code == KeyEvent.VK_ESCAPE){
            gp.gameState = GamePanel.PLAY_STATE;
        }
        if(code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }

        int maxCommandNum = 0;
        switch (gp.getUi().getSubState()){
            case 0: maxCommandNum = 5; break;
            case 3: maxCommandNum = 1;

        }

        if(code == KeyEvent.VK_Z){
            gp.getUi().commandNum--;
            gp.playSE(9);
            if(gp.getUi().commandNum < 0){
                gp.getUi().commandNum = maxCommandNum;
            }
        }
        if(code == KeyEvent.VK_S){
            gp.getUi().commandNum++;
            gp.playSE(9);
            if(gp.getUi().commandNum > maxCommandNum){
                gp.getUi().commandNum = 0;
            }
        }
        // VOLUME SOUNDS BUTTONS
        if(code == KeyEvent.VK_Q){
            if(gp.getUi().getSubState() == 0) {
                if (gp.getUi().commandNum == 1 && gp.getMusic().getVolumeScale() > 0) {
                    gp.getMusic().setVolumeScale(gp.getMusic().getVolumeScale() - 1);
                    gp.getMusic().checkVolume();
                    gp.playSE(9);
                }

                // SE VOLUME
                if (gp.getUi().commandNum == 2 && gp.getSoundEffect().getVolumeScale() > 0) {
                    gp.getSoundEffect().setVolumeScale(gp.getSoundEffect().getVolumeScale() - 1);
                    gp.playSE(9);
                }
            }

        }
        if(code == KeyEvent.VK_D){
            if(gp.getUi().getSubState() == 0) {
                if (gp.getUi().commandNum == 1 && gp.getMusic().getVolumeScale() < 5) {
                    gp.getMusic().setVolumeScale(gp.getMusic().getVolumeScale() + 1);
                    gp.getMusic().checkVolume();
                    gp.playSE(9);
                }

                // SE VOLUME
                if (gp.getUi().commandNum == 2 && gp.getSoundEffect().getVolumeScale() < 5) {
                    gp.getSoundEffect().setVolumeScale(gp.getSoundEffect().getVolumeScale() + 1);
                    gp.playSE(9);
                }
            }
        }
    }

    public void gameOverState(int code){

        if(code == KeyEvent.VK_Z){
            gp.getUi().commandNum--;
            if(gp.getUi().commandNum < 0){
                gp.getUi().commandNum = 1;

            }
            gp.playSE(9);
        }
        if(code == KeyEvent.VK_S){
            gp.getUi().commandNum++;
            if(gp.getUi().commandNum > 1){
                gp.getUi().commandNum = 0;

            }
            gp.playSE(9);
        }
        if(code == KeyEvent.VK_ENTER){
            if(gp.getUi().commandNum == 0){
                gp.gameState = GamePanel.PLAY_STATE;
                gp.resetGame(false);  // RETRY
                gp.playMusic(0);

            } else if(gp.getUi().commandNum == 1){
                gp.gameState = GamePanel.TITLE_STATE;
                gp.resetGame(true); // RESTART

            }
        }
    }

    public void tradeState(int code){

        if(code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }

        if(gp.getUi().getSubState() == 0){
            if(code == KeyEvent.VK_Z){
                gp.getUi().commandNum--;
                if(gp.getUi().commandNum < 0){
                    gp.getUi().commandNum = 2;

                }
                gp.playSE(9);
            }

            if(code == KeyEvent.VK_S){
                gp.getUi().commandNum++;
                if(gp.getUi().commandNum > 2){
                    gp.getUi().commandNum = 0;

                }
                gp.playSE(9);
            }
        }
        if(gp.getUi().getSubState() == 1){
            npcInventory(code);
            if(code == KeyEvent.VK_ESCAPE){
                gp.getUi().setSubState(0);
            }
        }
        if(gp.getUi().getSubState() == 2){
            playerInventory(code);
            if(code == KeyEvent.VK_ESCAPE){
                gp.getUi().setSubState(0);
            }
        }
    }

    public void mapState(int code){

        if(code == KeyEvent.VK_M){
            gp.gameState = GamePanel.PLAY_STATE;
        }
    }

    public void playerInventory(int code){
        if(code == KeyEvent.VK_Z){
            if(gp.getUi().playerSlotRow != 0) {
                gp.getUi().playerSlotRow--;
                gp.playSE(9);
            }
        }
        if(code == KeyEvent.VK_Q){
            if(gp.getUi().playerSlotCol != 0){
                gp.getUi().playerSlotCol--;
                gp.playSE(9);
            }
        }
        if(code == KeyEvent.VK_S){
            if(gp.getUi().playerSlotRow != 3) {
                gp.getUi().playerSlotRow++;
                gp.playSE(9);
            }
        }
        if(code == KeyEvent.VK_D){
            if(gp.getUi().playerSlotCol != 4) {
                gp.getUi().playerSlotCol++;
                gp.playSE(9);
            }
        }

    }

    public void npcInventory(int code){
        if(code == KeyEvent.VK_Z){
            if(gp.getUi().npcSlotRow != 0) {
                gp.getUi().npcSlotRow--;
                gp.playSE(9);
            }
        }
        if(code == KeyEvent.VK_Q){
            if(gp.getUi().npcSlotCol != 0){
                gp.getUi().npcSlotCol--;
                gp.playSE(9);
            }
        }
        if(code == KeyEvent.VK_S){
            if(gp.getUi().npcSlotRow != 3) {
                gp.getUi().npcSlotRow++;
                gp.playSE(9);
            }
        }
        if(code == KeyEvent.VK_D){
            if(gp.getUi().npcSlotCol != 4) {
                gp.getUi().npcSlotCol++;
                gp.playSE(9);
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        //Flèche Haute
        if (code == KeyEvent.VK_Z) {
            upPressed = false;
        }
        //Flèche Bas
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        //Flèche Gauche
        if (code == KeyEvent.VK_Q) {
            leftPressed = false;
        }
        //Flèche Droite
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
        // Touche F
        if(code == KeyEvent.VK_F){
            shotKeyPressed = false;
        }
        // Touche ENTER
        if(code == KeyEvent.VK_ENTER){
            enterPressed = false;
        }
        // Touche SPACE
        if(code == KeyEvent.VK_SPACE){
            spacePressed = false;
        }
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public void setDownPressed(boolean downPressed) {
        this.downPressed = downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }

    public boolean isEnterPressed() {
        return enterPressed;
    }

    public void setEnterPressed(boolean enterPressed) {
        this.enterPressed = enterPressed;
    }

    public boolean isShotKeyPressed() {
        return shotKeyPressed;
    }

    public void setShotKeyPressed(boolean shotKeyPressed) {
        this.shotKeyPressed = shotKeyPressed;
    }

    public boolean isDebugPressed() {
        return debugPressed;
    }

    public void setDebugPressed(boolean debugPressed) {
        this.debugPressed = debugPressed;
    }

    public boolean isSpacePressed() {
        return spacePressed;
    }

    public void setSpacePressed(boolean spacePressed) {
        this.spacePressed = spacePressed;
    }

    public boolean isGodModeOn() {
        return godModeOn;
    }

    public void setGodModeOn(boolean godModeOn) {
        this.godModeOn = godModeOn;
    }
}
