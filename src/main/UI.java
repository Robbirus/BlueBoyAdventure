package main;


import entity.Entity;
import environment.Lighting;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UI {

    private final GamePanel gp;
    private Graphics2D g2;
    private final Font maruMonica;
    private BufferedImage heart_full;
    private BufferedImage heart_half;
    private BufferedImage heart_blank;
    private BufferedImage crystal_full;
    private BufferedImage crystal_blank;
    private BufferedImage coin;
    private boolean messageOn = false;
    private ArrayList<String> message = new ArrayList<>();
    private ArrayList<Integer> messageCounter = new ArrayList<>();
    private boolean gameFinished = false;
    private String currentDialogue = "";
    public int commandNum = 0; // 0: The first screen 1: The second screen 2: ...
    public int titleScreenState = 0;
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;
    public int npcSlotCol = 0;
    public int npcSlotRow = 0;
    private int subState = 0;
    private int counter;
    private Entity npc;
    private int charIndex = 0;
    private String combineText = "";

    public UI(GamePanel gp){
        this.gp = gp;

        try {
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);

        } catch (FontFormatException e) {
            throw new RuntimeException(e);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // CREATE HUD OBJECT
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;

        Entity mana = new OBJ_ManaCrystal(gp);
        crystal_full = mana.image;
        crystal_blank = mana.image2;

        Entity bronzeCoin = new OBJ_Coin_Bronze(gp);
        coin = bronzeCoin.down1;
    }


    public void addMessage(String text){

        message.add(text);
        messageCounter.add(0);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(maruMonica);
        g2.setColor(Color.white);

        // TITLE STATE
        if(gp.gameState == GamePanel.TITLE_STATE){
            drawTitleScreen();
        }

        // PLAY STATE
        if(gp.gameState == GamePanel.PLAY_STATE){
            drawPlayerLife();
            drawMessage();
        }
        // PAUSE STATE
        if(gp.gameState == GamePanel.PAUSE_STATE){
            drawPlayerLife();
            drawPauseScreen();
        }
        // DIALOGUE STATE
        if(gp.gameState == GamePanel.DIALOGUE_STATE){
            drawPlayerLife();
            drawDialogueScreen();
        }
        // CHARACTER STATE
        if(gp.gameState == GamePanel.CHARACTER_STATE){
            drawCharacterScreen();
            drawInventory(gp.getPlayer(), true);
        }
        // OPTIONS STATE
        if(gp.gameState == GamePanel.OPTION_STATE){
            drawOptionsScreen();
        }
        // GAME OVER STATE
        if(gp.gameState == GamePanel.GAME_OVER_STATE){
            drawGameOverScreen();
        }
        // TRANSITION STATE
        if(gp.gameState == GamePanel.TRANSITION_STATE){
            drawTransition();
        }
        // TRADE STATE
        if(gp.gameState == GamePanel.TRADE_STATE){
            drawTradeScreen();
        }
        // SLEEP STATE
        if(gp.gameState == GamePanel.SLEEP_STATE){
            drawSleepScreen();
        }
    }

    private void drawPlayerLife() {

        int x = gp.getTileSize() / 2;
        int y = gp.getTileSize() / 2;
        int i = 0;

        // DRAW MAX LIFE
        while(i < gp.getPlayer().getMaxLife() / 2){
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.getTileSize();
        }

        // RESET
        x = gp.getTileSize() / 2;
        y = gp.getTileSize() / 2;
        i = 0;

        // DRAW CURRENT LIFE
        while(i < gp.getPlayer().getLife()){
            g2.drawImage(heart_half, x, y, null);
            i++;
            if(i < gp.getPlayer().getLife()){
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.getTileSize();
        }

        // DRAW MAX MANA
        x = gp.getTileSize() / 2 - 5;
        y = (int)(gp.getTileSize() * 1.5);
        i = 0;
        while(i < gp.getPlayer().getMaxMana()){
            g2.drawImage(crystal_blank, x, y, null);
            i++;
            x += 35;
        }

        // DRAW MANA
        x = gp.getTileSize() / 2 - 5;
        y = (int)(gp.getTileSize() * 1.5);
        i = 0;
        while(i < gp.getPlayer().getMana()){
            g2.drawImage(crystal_full, x, y, null);
            i++;
            x += 35;
        }
    }

    public void drawMessage(){

        int messageX = gp.getTileSize();
        int messageY = gp.getTileSize() * 6;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

        for(int i = 0; i < message.size(); i++){

            if(message.get(i) != null){

                g2.setColor(Color.black);
                g2.drawString(message.get(i), messageX+2, messageY+2);
                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1; // messageCounter++
                messageCounter.set(i, counter); // set the counter to the array
                messageY += 50;

                if(messageCounter.get(i) > 180){ // 3 seconds
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }

    private void drawTitleScreen() {

        if(titleScreenState == 0) {
            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(0, 0, gp.getScreenWidth(), gp.getScreenHeight());

            // TITLE NAME
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
            String text = "Blue Boy Adventure";
            int x = getXforCenteredText(text);
            int y = gp.getTileSize() * 3;

            // SHADOW
            g2.setColor(Color.gray);
            g2.drawString(text, x + 5, y + 5);
            // MAIN COLOR
            g2.setColor(Color.white);
            g2.drawString(text, x, y);

            // BLUE BOY IMAGE
            x = gp.getScreenWidth() / 2 - (gp.getTileSize() * 2) / 2;
            y += gp.getTileSize() * 2;
            g2.drawImage(gp.getPlayer().down1, x, y, gp.getTileSize() * 2, gp.getTileSize() * 2, null);

            // MENU
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));

            text = "NOUVELLE PARTIE";
            x = getXforCenteredText(text);
            y += gp.getTileSize() * 4.5;
            g2.drawString(text, x, y);
            if (commandNum == 0) {
                g2.drawString(">", x - gp.getTileSize(), y);
            }

            text = "CHARGER";
            x = getXforCenteredText(text);
            y += gp.getTileSize();
            g2.drawString(text, x, y);
            if (commandNum == 1) {
                g2.drawString(">", x - gp.getTileSize(), y);
            }

            text = "QUITTER";
            x = getXforCenteredText(text);
            y += gp.getTileSize();
            g2.drawString(text, x, y);
            if (commandNum == 2) {
                g2.drawString(">", x - gp.getTileSize(), y);
            }

        } else if(titleScreenState == 1) {

            g2.setColor(new Color(0, 0, 0));
            g2.fillRect(0, 0, gp.getScreenWidth(), gp.getScreenHeight());

            // CLASS SELECTION SCREEN
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(42F));

            String text = "Choisis une classe !";
            int x = getXforCenteredText(text);
            int y = gp.getTileSize() * 3;
            g2.drawString(text, x, y);

            text = "Combatant";
            x = getXforCenteredText(text);
            y += gp.getTileSize() * 3;
            g2.drawString(text, x, y);
            if(commandNum == 0){
                g2.drawString(">", x-gp.getTileSize(), y);
            }

            text = "Voleur";
            x = getXforCenteredText(text);
            y += gp.getTileSize();
            g2.drawString(text, x, y);
            if(commandNum == 1){
                g2.drawString(">", x-gp.getTileSize(), y);
            }

            text = "Sorcier";
            x = getXforCenteredText(text);
            y += gp.getTileSize();
            g2.drawString(text, x, y);
            if(commandNum == 2){
                g2.drawString(">", x-gp.getTileSize(), y);
            }

            text = "Retour";
            x = getXforCenteredText(text);
            y += gp.getTileSize();
            g2.drawString(text, x, y);
            if(commandNum == 3){
                g2.drawString(">", x-gp.getTileSize(), y);
            }
        }
    }

    public void drawPauseScreen(){

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSE";
        int x = getXforCenteredText(text);

        int y = gp.getScreenHeight()/2;

        g2.drawString(text, x, y);
    }

    public void drawDialogueScreen(){

        // WINDOW
        int x = gp.getTileSize() * 2;
        int y = gp.getTileSize() / 2;
        int width = gp.getScreenWidth() - (gp.getTileSize() * 6);
        int height = gp.getTileSize() * 4;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F)); // maumonica
        x += gp.getTileSize();
        y += gp.getTileSize();

        if(npc.getDialogue(npc.getDialogueSet(), npc.getDialogueIndex()) != null){

            // Play the text as a whole
//            setCurrentDialogue(npc.getDialogue(npc.getDialogueSet(), npc.getDialogueIndex()));

            // Play the text letter by letter - HERE
            char[] characters = npc.getDialogue(npc.getDialogueSet(), npc.getDialogueIndex()).toCharArray();

            if(charIndex < characters.length){

                gp.playSE(17);
                String s = String.valueOf(characters[charIndex]);
                combineText = combineText + s;
                setCurrentDialogue(combineText);

                charIndex++;
            }
            // TO HERE

            if(gp.getKeyHandler().isEnterPressed()){

                charIndex = 0;
                combineText = "";

                if(gp.gameState == GamePanel.DIALOGUE_STATE){

                    npc.setDialogueIndex(npc.getDialogueIndex() + 1);
                    gp.getKeyHandler().setEnterPressed(false);
                }
            }

        } else { // If no text is in the array
            npc.setDialogueIndex(0);

            if(gp.gameState == GamePanel.DIALOGUE_STATE){
                gp.gameState = GamePanel.PLAY_STATE;

            }
        }

        for(String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    public void drawCharacterScreen(){

        // CREATE A FRAME
        final int frameX = gp.getTileSize() + 24;
        final int frameY = gp.getTileSize();
        final int frameWidth = gp.getTileSize() * 6;
        final int frameHeight = gp.getTileSize() * 10;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // TEXT
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int textX = frameX + 20;
        int textY = frameY + gp.getTileSize();
        final int lineHeight = 32;

        // NAMES
        g2.drawString("Niveau", textX, textY);
        textY += lineHeight;
        g2.drawString("Vie", textX, textY);
        textY += lineHeight;
        g2.drawString("Mana", textX, textY);
        textY += lineHeight;
        g2.drawString("Force", textX, textY);
        textY += lineHeight;
        g2.drawString("Dexterité", textX, textY);
        textY += lineHeight;
        g2.drawString("Attaque", textX, textY);
        textY += lineHeight;
        g2.drawString("Défense", textX, textY);
        textY += lineHeight;
        g2.drawString("EXP", textX, textY);
        textY += lineHeight;
        g2.drawString("Prochain Niveau", textX, textY);
        textY += lineHeight;
        g2.drawString("Pièce", textX, textY);
        textY += lineHeight + 20;
        g2.drawString("Arme", textX, textY);
        textY += lineHeight + 15;
        g2.drawString("Bouclier", textX, textY);
        textY += lineHeight;


        // VALUES
        int tailX = (frameX + frameWidth) - 30;
        // Reset textY
        textY = frameY + gp.getTileSize();
        String value;

        value = String.valueOf(gp.getPlayer().getLevel());
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.getPlayer().getLife() + "/" + gp.getPlayer().getMaxLife());
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.getPlayer().getMana() + "/" + gp.getPlayer().getMaxMana());
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.getPlayer().getStrength());
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.getPlayer().getDexterity());
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.getPlayer().getAttack());
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.getPlayer().getDefense());
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.getPlayer().getExp());
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.getPlayer().getNextLevelExp());
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.getPlayer().getCoin());
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        g2.drawImage(gp.getPlayer().getCurrentWeapon().down1, tailX - gp.getTileSize(), textY - 14, null);
        textY += gp.getTileSize();

        g2.drawImage(gp.getPlayer().getCurrentShield().down1, tailX - gp.getTileSize(), textY - 14, null);
    }

    public void drawInventory(Entity entity, boolean cursor){

        int frameX = 0;
        int frameY = 0;
        int frameWidth = 0;
        int frameHeight = 0;
        int slotCol = 0;
        int slotRow = 0;

        if(entity == gp.getPlayer()){

            frameX = gp.getTileSize() * 13;
            frameY = gp.getTileSize();
            frameWidth = gp.getTileSize() * 6;
            frameHeight = gp.getTileSize() * 5;
            slotCol = playerSlotCol;
            slotRow = playerSlotRow;

        } else {
            frameX = gp.getTileSize() * 2;
            frameY = gp.getTileSize();
            frameWidth = gp.getTileSize() * 6;
            frameHeight = gp.getTileSize() * 5;
            slotCol = npcSlotCol;
            slotRow = npcSlotRow;

        }

        //FRAME
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // SLOT
        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.getTileSize() + 3;

        // DRAW PLAYER'S ITEMS
        for(int i = 0; i < entity.getInventory().size(); i++){

            // EQUIP CURSOR
            if(entity.getInventory().get(i) == entity.getCurrentWeapon() || entity.getInventory().get(i) == entity.getCurrentShield() ||
                    entity.getInventory().get(i) == entity.getCurrentBoots() || entity.getInventory().get(i) == entity.getCurrentLight()){

                g2.setColor(new Color(240, 190, 90));
                g2.fillRoundRect(slotX, slotY, gp.getTileSize(), gp.getTileSize(), 10, 10);

            }

            g2.drawImage(entity.getInventory().get(i).down1, slotX, slotY, null);

            // DISPLAY AMOUNT
            if(entity == gp.getPlayer() && entity.getInventory().get(i).getAmount() > 1){
                g2.setFont(g2.getFont().deriveFont(32f));
                int amountX;
                int amountY;

                String s = "" + entity.getInventory().get(i).getAmount();
                amountX = getXforAlignToRightText(s, slotX + 44);
                amountY = slotY + gp.getTileSize();

                // SHADOW
                g2.setColor(new Color(60, 60, 60));
                g2.drawString(s, amountX, amountY);
                // NUMBER
                g2.setColor(Color.WHITE);
                g2.drawString(s, amountX-3, amountY-3);

            }

            slotX += slotSize;

            if(i == 4 || i == 9 || i == 14){
                slotX = slotXstart;
                slotY += slotSize;
            }
        }

        // CURSOR
        if(cursor) {
            int cursorX = slotXstart + (slotSize * slotCol);
            int cursorY = slotYstart + (slotSize * slotRow);
            int cursorWidth = gp.getTileSize();
            int cursorHeight = gp.getTileSize();

            // DRAW CURSOR
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

            // DESCRIPTION FRAME
            int dframeX = frameX;
            int dframeY = frameY + frameHeight;
            int dframeWidth = frameWidth;
            int dframeHeight = gp.getTileSize() * 3;

            // DRAW DESCRIPTION TEXT
            int textX = dframeX + 20;
            int textY = dframeY + gp.getTileSize();
            g2.setFont(g2.getFont().deriveFont(28F));

            int itemIndex = getItemIndexOnSlot(slotCol, slotRow);

            if (itemIndex < entity.getInventory().size()) {

                drawSubWindow(dframeX, dframeY, dframeWidth, dframeHeight);

                for (String line : entity.getInventory().get(itemIndex).getDescription().split("\n")) {
                    g2.drawString(line, textX, textY);
                    textY += 32;
                }
            }
        }
    }

    public void drawGameOverScreen(){

        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.getScreenWidth(), gp.getScreenHeight());

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));

        // Shadow
        text = "Game Over";
        g2.setColor(Color.black);
        x = getXforCenteredText(text);
        y = gp.getTileSize() * 4;
        g2.drawString(text, x, y);

        // Main
        g2.setColor(Color.white);
        g2.drawString(text, x-4, y-4);

        // Retry
        g2.setFont(g2.getFont().deriveFont(50f));
        text = "Retry";
        x = getXforCenteredText(text);
        y += gp.getTileSize() * 4;
        g2.drawString(text, x, y);
        if(commandNum == 0){
            g2.drawString(">", x-40, y);
        }

        // Back to the title screen
        text = "Quit";
        x = getXforCenteredText(text);
        y += 55;
        g2.drawString(text, x, y);
        if(commandNum == 1){
            g2.drawString(">", x-40, y);
        }

    }

    public void drawOptionsScreen(){

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        // SUB WINDOW
        int frameX = gp.getTileSize() * 6;
        int frameY = gp.getTileSize();
        int frameWidth = gp.getTileSize() * 8;
        int frameHeight = gp.getTileSize() * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch (subState){
            case 0: options_top(frameX,  frameY); break;
            case 1: options_fullScreenNotification(frameX, frameY); break;
            case 2: options_control(frameX, frameY); break;
            case 3: options_endGameConfirmation(frameX, frameY); break;
        }

        gp.getKeyHandler().setEnterPressed(false);
    }

    public void options_top(int frameX, int frameY){

        int textX;
        int textY;

        // TITLE
        String text = "Options";
        textX =  getXforCenteredText(text);
        textY = frameY + gp.getTileSize();
        g2.drawString(text, textX, textY);

        // FULL SCREEN ON/OFF
        textX = frameX + gp.getTileSize();
        textY += gp.getTileSize() * 2;
        g2.drawString("Full Screen", textX, textY);
        if(commandNum == 0){
            g2.drawString(">", textX-25, textY);
            if(gp.getKeyHandler().isEnterPressed()){
                if(!gp.isFullScreenOn()){
                    gp.setFullScreenOn(true);

                } else if(gp.isFullScreenOn()){
                    gp.setFullScreenOn(false);

                }
                subState = 1;
            }
        }

        // MUSIC
        textY += gp.getTileSize();
        g2.drawString("Music", textX, textY);
        if(commandNum == 1){
            g2.drawString(">", textX-25, textY);
        }

        // SOUND EFFECT
        textY += gp.getTileSize();
        g2.drawString("Sound Effect", textX, textY);
        if(commandNum == 2){
            g2.drawString(">", textX-25, textY);
        }

        // CONTROLS
        textY += gp.getTileSize();
        g2.drawString("Controls", textX, textY);
        if(commandNum == 3){
            g2.drawString(">", textX-25, textY);
            if(gp.getKeyHandler().isEnterPressed()){
                subState = 2;
                commandNum = 0;
            }
        }

        // END GAME
        textY += gp.getTileSize();
        g2.drawString("End Game", textX, textY);
        if(commandNum == 4){
            g2.drawString(">", textX-25, textY);
            if(gp.getKeyHandler().isEnterPressed()){
                subState = 3;
                commandNum = 0;
            }
        }

        // BACK
        textY += gp.getTileSize() * 2;
        g2.drawString("BACK", textX, textY);
        if(commandNum == 5){
            g2.drawString(">", textX-25, textY);
            if(gp.getKeyHandler().isEnterPressed()){
                gp.gameState = GamePanel.PLAY_STATE;
                commandNum = 0;
            }
        }

        // FULL SCREEN CHECK BOX
        textX = (int) (frameX + gp.getTileSize() * 4.5);
        textY = frameY + gp.getTileSize() * 2 + 24;
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(textX, textY, 24, 24);
        if(gp.isFullScreenOn()){
            g2.fillRect(textX, textY, 24, 24);
        }

        // MUSIC VOLUME
        textY += gp.getTileSize();
        g2.drawRect(textX, textY, 120, 24); // 120/5 = 24
        int volumeWidth = 24 * gp.getMusic().getVolumeScale();
        g2.fillRect(textX, textY, volumeWidth, 24);

        // SE VOLUME
        textY += gp.getTileSize();
        g2.drawRect(textX, textY, 120, 24);
        volumeWidth = 24 * gp.getSoundEffect().getVolumeScale();
        g2.fillRect(textX, textY, volumeWidth, 24);

        gp.getConfig().saveConfig();

    }

    public void options_fullScreenNotification(int frameX, int frameY){

        int textX = frameX + gp.getTileSize();
        int textY = frameY + gp.getTileSize() * 3;

        currentDialogue = "The Change will take \neffect after restarting \nthe game.";

        for(String line : currentDialogue.split("\n")){
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        // BACK
        textY = frameY + gp.getTileSize() * 9;
        g2.drawString("Back", textX, textY);
        if(commandNum == 0){
            g2.drawString(">", textX-25, textY);
            if(gp.getKeyHandler().isEnterPressed()){
                subState = 0;
            }
        }
    }

    public void options_control(int frameX, int frameY){

        int textX;
        int textY;

        // TITLE
        String text = "Control";
        textX = getXforCenteredText(text);
        textY = frameY + gp.getTileSize();
        g2.drawString(text, textX, textY);

        textX = frameX + gp.getTileSize();
        textY += gp.getTileSize();
        g2.drawString("Move", textX, textY); textY += gp.getTileSize();
        g2.drawString("Confirm/Attack", textX, textY); textY += gp.getTileSize();
        g2.drawString("Shoot/Cast", textX, textY); textY += gp.getTileSize();
        g2.drawString("Character Screen", textX, textY); textY += gp.getTileSize();
        g2.drawString("Pause", textX, textY); textY += gp.getTileSize();
        g2.drawString("Map", textX, textY); textY += gp.getTileSize();
        g2.drawString("Options", textX, textY); textY += gp.getTileSize();

        textX = frameX + gp.getTileSize() * 6;
        textY = frameY + gp.getTileSize() * 2;
        g2.drawString("ZQSD", textX, textY); textY += gp.getTileSize();
        g2.drawString("ENTER", textX, textY); textY += gp.getTileSize();
        g2.drawString("F", textX, textY); textY += gp.getTileSize();
        g2.drawString("C", textX, textY); textY += gp.getTileSize();
        g2.drawString("P", textX, textY); textY += gp.getTileSize();
        g2.drawString("X", textX, textY); textY += gp.getTileSize();
        g2.drawString("ESC", textX, textY); textY += gp.getTileSize();

        // BACK
        textX = frameX + gp.getTileSize();
        textY = frameY + gp.getTileSize() * 9;
        g2.drawString("Back", textX, textY);
        if(commandNum == 0){
            g2.drawString(">", textX - 25, textY);
            if(gp.getKeyHandler().isEnterPressed()){
                subState = 0;
                commandNum = 3;
            }
        }
    }

    public void options_endGameConfirmation(int frameX, int frameY){

        int textX = frameX + gp.getTileSize();
        int textY = frameY + gp.getTileSize() * 3;

        currentDialogue = "Quitter le jeu et \nrevenir à l'écran titre ?";

        for(String line: currentDialogue.split("\n")){
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        // YES
        String text = "Oui";
        textX = getXforCenteredText(text);
        textY += gp.getTileSize() * 3;
        g2.drawString(text, textX, textY);
        if(commandNum == 0){
            g2.drawString(">", textX-25, textY);

            if(gp.getKeyHandler().isEnterPressed()){
                subState = 0;
                gp.gameState = GamePanel.TITLE_STATE;
                gp.resetGame(true);
            }
        }

        // NO
        text = "Non";
        textX = getXforCenteredText(text);
        textY += gp.getTileSize();
        g2.drawString(text, textX, textY);
        if(commandNum == 1){
            g2.drawString(">", textX-25, textY);

            if(gp.getKeyHandler().isEnterPressed()){
                subState = 0;
                commandNum = 4;
            }
        }
    }

    public void drawTransition(){

        counter++;
        g2.setColor(new Color(0,0,0,counter*5));
        g2.fillRect(0, 0, gp.getScreenWidth(), gp.getScreenHeight());

        if(counter == 50){ // The transition is done
            counter = 0;
            gp.gameState = GamePanel.PLAY_STATE;
            gp.setCurrentMap(gp.getEventHandler().getTempMap());
            gp.getPlayer().setWorldX(gp.getTileSize() * gp.getEventHandler().getTempCol());
            gp.getPlayer().setWorldY(gp.getTileSize() * gp.getEventHandler().getTempRow());
            gp.getEventHandler().setPreviousEventX(gp.getPlayer().getWorldX());
            gp.getEventHandler().setPreviousEventY(gp.getPlayer().getWorldY());
            gp.changeArea();
        }

    }

    public void drawTradeScreen(){

        switch (subState){
            case 0: trade_select(); break;
            case 1: trade_buy(); break;
            case 2: trade_sell(); break;
        }

        gp.getKeyHandler().setEnterPressed(false);
    }

    public void trade_select(){

        npc.setDialogueSet(0);
        drawDialogueScreen();

        //DRAW WINDOW
        int x = gp.getTileSize() * 15;
        int y = gp.getTileSize() * 4;
        int width = (int)(gp.getTileSize() * 3.5);
        int height = (int)(gp.getTileSize() * 3.5);
        drawSubWindow(x, y, width, height);

        // DRAW TEXTS
        x += gp.getTileSize();
        y += gp.getTileSize();
        g2.drawString("Acheter", x, y);
        if(commandNum == 0){
            g2.drawString(">", x-24, y);
            if(gp.getKeyHandler().isEnterPressed()){
                subState = 1;
            }
        }
        y += gp.getTileSize();
        g2.drawString("Vendre", x, y);
        if(commandNum == 1){
            g2.drawString(">", x-24, y);
            if(gp.getKeyHandler().isEnterPressed()){
                subState = 2;
            }
        }
        y += gp.getTileSize();
        g2.drawString("Partir", x, y);
        if(commandNum == 2){
            g2.drawString(">", x-24, y);
            if(gp.getKeyHandler().isEnterPressed()){
                commandNum = 0;
                npc.startDialogue(npc, 1);
            }
        }
    }

    public void trade_buy(){

        // DRAW PLAYER INVENTORY
        drawInventory(gp.getPlayer(), false);
        // DRAW NPC INVENTORY
        drawInventory(npc, true);

        // DRAW HINT WINDOW
        int x = gp.getTileSize() * 2;
        int y = gp.getTileSize() * 9;
        int width = gp.getTileSize() * 6;
        int height = gp.getTileSize() * 2;
        drawSubWindow(x, y, width, height);
        g2.drawString("[ESC] Back", x+24, y+60);

        // DRAW PLAYER COIN WINDOW
        x = gp.getTileSize() * 13;
        y = gp.getTileSize() * 9;
        width = gp.getTileSize() * 6;
        height = gp.getTileSize() * 2;
        drawSubWindow(x, y, width, height);
        g2.drawString("Your Coin: " + gp.getPlayer().getCoin(), x+24, y+60);

        // DRAW PRICE WINDOW
        int itemIndex = getItemIndexOnSlot(npcSlotCol, npcSlotRow);
        if(itemIndex < npc.getInventory().size()){

            x= (int)(gp.getTileSize() * 5.5);
            y= (int)(gp.getTileSize() * 5.5);
            width = (int)(gp.getTileSize() * 2.5);
            height = gp.getTileSize();
            drawSubWindow(x, y, width, height);
            g2.drawImage(coin, x+10, y+8, 32, 32, null);

            int price = npc.getInventory().get(itemIndex).getPrice();
            String text = ""+price;
            x = getXforAlignToRightText(text, gp.getTileSize() * 8-20);
            g2.drawString(text, x, y+34);

            // BUY AN ITEM
            if(gp.getKeyHandler().isEnterPressed()){
                if(npc.getInventory().get(itemIndex).getPrice() > gp.getPlayer().getCoin()){
                    subState = 0;
                    npc.startDialogue(npc, 2);

                } else {
                    if(gp.getPlayer().canObtainItem(npc.getInventory().get(itemIndex))){
                        gp.getPlayer().setCoin(gp.getPlayer().getCoin() - npc.getInventory().get(itemIndex).getPrice());

                    } else {
                        subState = 0;
                        npc.startDialogue(npc, 3);

                    }
                }
            }
        }
    }

    public void trade_sell(){

        // DRAW PLAYER INVENTORY
        drawInventory(gp.getPlayer(), true);

        int x;
        int y;
        int width;
        int height;

        // DRAW HINT WINDOW
        x = gp.getTileSize() * 2;
        y = gp.getTileSize() * 9;
        width = gp.getTileSize() * 6;
        height = gp.getTileSize() * 2;
        drawSubWindow(x, y, width, height);
        g2.drawString("[ESC] Back", x+24, y+60);

        // DRAW PLAYER COIN WINDOW
        x = gp.getTileSize() * 13;
        y = gp.getTileSize() * 9;
        width = gp.getTileSize() * 6;
        height = gp.getTileSize() * 2;
        drawSubWindow(x, y, width, height);
        g2.drawString("Your Coin: " + gp.getPlayer().getCoin(), x+24, y+60);

        // DRAW PRICE WINDOW
        int itemIndex = getItemIndexOnSlot(playerSlotCol, playerSlotRow);
        if(itemIndex < gp.getPlayer().getInventory().size()){

            x= (int)(gp.getTileSize() * 15.5);
            y= (int)(gp.getTileSize() * 5.5);
            width = (int)(gp.getTileSize() * 2.5);
            height = gp.getTileSize();
            drawSubWindow(x, y, width, height);
            g2.drawImage(coin, x+10, y+8, 32, 32, null);

            int price = gp.getPlayer().getInventory().get(itemIndex).getPrice();
            String text = ""+price;
            x = getXforAlignToRightText(text, gp.getTileSize() * 18-20);
            g2.drawString(text, x, y+34);

            // SELL AN ITEM
            if(gp.getKeyHandler().isEnterPressed()){

                if(gp.getPlayer().getInventory().get(itemIndex) == gp.getPlayer().getCurrentWeapon() ||
                        gp.getPlayer().getInventory().get(itemIndex) == gp.getPlayer().getCurrentShield()||
                            gp.getPlayer().getInventory().get(itemIndex) == gp.getPlayer().getCurrentBoots()){
                    commandNum = 0;
                    npc.startDialogue(npc, 4);

                } else {
                    if(gp.getPlayer().getInventory().get(itemIndex).getAmount() > 1){
                        int amount = gp.getPlayer().getInventory().get(itemIndex).getAmount();
                        amount--;
                        gp.getPlayer().getInventory().get(itemIndex).setAmount(amount);

                    } else {
                        gp.getPlayer().getInventory().remove(itemIndex);

                    }
                    gp.getPlayer().setCoin(gp.getPlayer().getCoin() + price);
                }
            }
        }
    }

    public void drawSleepScreen(){

        this.counter++;

        if(counter < 120){ // 2 secondes
            float filterAlpha = gp.getEnvironmentManager().getLighting().getFilterAlpha();
            filterAlpha += 0.01f;
            gp.getEnvironmentManager().getLighting().setFilterAlpha(filterAlpha);

            if(filterAlpha > 1f){
                gp.getEnvironmentManager().getLighting().setFilterAlpha(1f);

            }
        }

        if(counter >= 120){ // 2 secondes
            float filterAlpha = gp.getEnvironmentManager().getLighting().getFilterAlpha();
            filterAlpha -= 0.01f;
            gp.getEnvironmentManager().getLighting().setFilterAlpha(filterAlpha);

            if(filterAlpha <= 0f){
                gp.getEnvironmentManager().getLighting().setFilterAlpha(0f);
                counter = 0;
                gp.getEnvironmentManager().getLighting().setDayState(Lighting.DAY);
                gp.getEnvironmentManager().getLighting().setDayCounter(0);
                gp.gameState = GamePanel.PLAY_STATE;
                gp.getPlayer().getImage();

            }
        }
    }

    public int getItemIndexOnSlot(int slotCol, int slotRow){
        int itemIndex = slotCol + (slotRow * 5);
        return itemIndex;
    }

    public void drawSubWindow(int x, int y, int width, int height){

        // SUB WINDOW
        Color c = new Color(0 ,0 ,0,220);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x +5, y +5, width - 10, height - 10, 25, 25);
    }

    public int getXforCenteredText(String text){

        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.getScreenWidth()/2 - length/2;
        return x;
    }

    public int getXforAlignToRightText(String text, int tailX){

        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }

    public int getSubState() {
        return subState;
    }

    public void setSubState(int subState) {
        this.subState = subState;
    }

    public Font getMaruMonica() {
        return maruMonica;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }

    public String getCurrentDialogue() {
        return currentDialogue;
    }

    public void setCurrentDialogue(String currentDialogue) {
        this.currentDialogue = currentDialogue;
    }

    public Entity getNpc() {
        return npc;
    }

    public void setNpc(Entity npc) {
        this.npc = npc;
    }
}
