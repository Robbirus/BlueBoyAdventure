package main;

import ai.PathFinder;
import data.SaveLoad;
import entity.Entity;
import entity.Player;
import environment.EnvironmentManager;
import tile.TileManager;
import tile.Map;
import tile_interative.InteractiveTile;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    private final int originalTileSize = 16; //16x16 tile
    private final int scale = 3;

    private final int tileSize = originalTileSize * scale; // 48x48 tile
    private final int maxScreenCol = 20; // Largeur
    private final int maxScreenRow = 12; // Hauteur
    private final int screenWidth = tileSize * maxScreenCol;
    private final int screenHeight = tileSize * maxScreenRow;

    // WORLD SETTINGS
    private int maxWorldCol;
    private int maxWorldRow;
    private final int worldWidth = tileSize * maxWorldCol;
    private final int worldHeight = tileSize * maxWorldRow;
    private final int maxMap = 10; // Nbr de map max
    private int currentMap = 0; // Map actuel

    // FOR FULL SCREEN
    private int screenWidth2 = screenWidth;
    private int screenHeight2 = screenHeight;
    private BufferedImage tempScreen;
    private Graphics2D g2;
    private boolean fullScreenOn = false;

    //FPS
    private final static int FPS_SET = 60;
    private final static int UPS_SET = 100;

    // SYSTEM
    private TileManager tileManager = new TileManager(this);
    private KeyHandler keyHandler = new KeyHandler(this);
    private final Sound music = new Sound();
    private final Sound soundEffect = new Sound();
    private CollisionChecker collisionChecker = new CollisionChecker(this);
    private AssetSetter assetSetter = new AssetSetter(this);

    private UI ui = new UI(this);
    private final EventHandler eventHandler = new EventHandler(this);
    private final Config config = new Config(this);
    private PathFinder pathFinder = new PathFinder(this);
    private final EnvironmentManager eManager = new EnvironmentManager(this);
    private Map map = new Map(this);
    private SaveLoad saveLoad = new SaveLoad(this);
    private EntityGenerator entityGenerator = new EntityGenerator(this);
    private Thread gameThread;

    // ENTITY AND OBJECT
    private Player player = new Player(this, keyHandler);
    private Entity[][] obj = new Entity[maxMap][20];
    private Entity[][] npc = new Entity[maxMap][10];
    private Entity[][] monster = new Entity[maxMap][20];
    private InteractiveTile[][] interactiveTile = new InteractiveTile[maxMap][50];
    private Entity[][] projectile = new Entity[maxMap][20];
    private ArrayList<Entity> particlesList = new ArrayList<>();
    private final ArrayList<Entity> entityList = new ArrayList<>();

    // GAME STATE
    public int gameState;
    public final static int TITLE_STATE = 0;
    public final static int PLAY_STATE = 1;
    public final static int PAUSE_STATE = 2;
    public final static int DIALOGUE_STATE = 3;
    public final static int CHARACTER_STATE = 4;
    public final static int OPTION_STATE = 5;
    public final static int GAME_OVER_STATE = 6;
    public final static int TRANSITION_STATE = 7;
    public final static int TRADE_STATE = 8;
    public final static int SLEEP_STATE = 9;
    public final static int MAP_STATE = 10;

    // AREA
    public int currentArea;
    public int nextArea;
    public final static int OUTSIDE = 50;
    public final static int INDOOR = 51;
    public final static int DUNGEON = 52;


    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // 720p
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }


    public void setupGame() {

        assetSetter.setObject();
        assetSetter.setNpc();
        assetSetter.setMonster();
        assetSetter.setInteractiveTile();
        eManager.setup();

        playMusic(29);
        gameState = TITLE_STATE;
        currentArea = OUTSIDE;

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();

        if (fullScreenOn) {
            setFullScreen();
        }
    }

    public void resetGame(boolean restart) {

        currentArea = OUTSIDE;

        player.setDefaultPositions();
        player.restoreStatus();
        player.resetCounter();
        assetSetter.setNpc();
        assetSetter.setMonster();

        if (restart) {
            player.setDefaultValues();
            assetSetter.setObject();
            assetSetter.setMonster();
            eManager.getLighting().resetDay();
        }
    }

    public void setFullScreen() {

        // GET LOCAL SCREEN DEVICE
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);

        // GET FULL SCREEN WIDTH AND HEIGHT
        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();

    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (gameThread != null) {

            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                drawToTempScreen();
                drawToScreen();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }

//        double drawInterval = 1000000000.0 / FPS_SET;
//        double delta = 0;
//        long lastTime = System.nanoTime();
//        long currentTime;
//        long timer = 0;
//        int drawCount = 0;
//
//        //Boucle de jeu
//        while(gameThread != null){
//
//            currentTime = System.nanoTime();
//
//            delta += (currentTime - lastTime)/drawInterval;
//            timer += (currentTime - lastTime);
//            lastTime = currentTime;
//
//            if(delta >= 1) {
//                //1 UPDATE: Mettre a jour les informations comme la position du personnage
//                update();
//
//                //2 DRAW: Afficher l'écran avec les information a jour
//                drawToTempScreen(); // Draw everything to the Buffered Image
//                drawToScreen(); // Draw the Buffered image to the screen
//
//                delta--;
//                drawCount++;
//            }
//
//            if(timer >= 1000000000){
//                System.out.println("FPS: " + drawCount);
//                drawCount = 0;
//                timer = 0;
//            }
//        }
    }

    public void update(){

        if(gameState == PLAY_STATE) {
            // Player
            player.update();

            // NPC
            for(int i = 0; i< npc[1].length; i++){
                if(npc[currentMap][i] != null){
                    npc[currentMap][i].update();
                }
            }

            // MONSTER
            for(int i = 0; i< monster[1].length; i++){
                if(monster[currentMap][i] != null){

                    if(monster[currentMap][i].isAlive() && !monster[currentMap][i].isDead()){
                        monster[currentMap][i].update();
                    }
                    if(!monster[currentMap][i].isAlive()){
                        monster[currentMap][i].checkDrop();
                        monster[currentMap][i] = null;
                    }
                }
            }

            // PROJECTILES
            for(int i = 0; i < projectile[1].length; i++){
                if(projectile[currentMap][i] != null){

                    if(projectile[currentMap][i].isAlive()){
                        projectile[currentMap][i].update();
                    }
                    if(!projectile[currentMap][i].isAlive()){
                        projectile[currentMap][i] = null;
                    }
                }
            }

            // PARTICLES
            for(int i = 0; i< particlesList.size(); i++){
                if(particlesList.get(i) != null){

                    if(particlesList.get(i).isAlive() && !particlesList.get(i).isDead()){
                        particlesList.get(i).update();
                    }
                    if(!particlesList.get(i).isAlive()){
                        particlesList.remove(i);
                    }
                }
            }

            for(int i = 0; i < interactiveTile[1].length; i++){
                if(interactiveTile[currentMap][i] != null){
                    interactiveTile[currentMap][i].update();
                }
            }

            eManager.update();
        }

        if(gameState == PAUSE_STATE){
            // Nothing
        }

    }

    public void drawToTempScreen(){

        // DEBUG
        long drawStart = 0;
        if(keyHandler.isDebugPressed()) {
            drawStart = System.nanoTime();
        }

        // TITLE SCREEN
        if(gameState == TITLE_STATE){
            ui.draw(g2);

        }
        // MAP SCREEN
        else if(gameState == MAP_STATE){
            map.drawFullMapScreen(g2);
        }
        // OTHER
        else {

            // TILE
            tileManager.draw(g2);

            // INTERACTIVE TILES
            for(int i = 0; i < interactiveTile[1].length; i++){
                if(interactiveTile[currentMap][i] != null){
                    interactiveTile[currentMap][i].draw(g2);
                }
            }

            // ADD ENTITIES TO THE LIST
            entityList.add(player);

            for(int i = 0; i < npc[1].length; i++){
                if(npc[currentMap][i] != null){
                    entityList.add(npc[currentMap][i]);
                }
            }

            for(int i = 0; i < obj[1].length; i++){
                if(obj[currentMap][i] != null){
                    entityList.add(obj[currentMap][i]);
                }
            }

            for(int i = 0; i < monster[1].length; i++) {
                if (monster[currentMap][i] != null) {
                    entityList.add(monster[currentMap][i]);
                }
            }

            for(int i = 0; i < projectile[1].length; i++) {
                if (projectile[currentMap][i] != null) {
                    entityList.add(projectile[currentMap][i]);
                }
            }

            for(int i = 0; i < particlesList.size(); i++) {
                if (particlesList.get(i) != null) {
                    entityList.add(particlesList.get(i));
                }
            }

            // SORT
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {

                    int result = Integer.compare(e1.getWorldY(), e2.getWorldY());
                    return result;
                }
            });

            // DRAW ENTITIES
            for(int i = 0; i < entityList.size(); i++){
                entityList.get(i).draw(g2);
            }

            // EMPTY ENTITY LIST
            entityList.clear();

            // ENVIRONMENT
            eManager.draw(g2);

            // MINI MAP
            map.drawMiniMap(g2);

            // UI
            ui.draw(g2);
        }

        // DEBUG
        if(keyHandler.isDebugPressed()) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.white);
            int x = 10;
            int y = 400;
            int lineHeight = 20;

            g2.drawString("WorldX: " + player.getWorldX(), x, y); y+= lineHeight;
            g2.drawString("WorldY: " + player.getWorldY(), x, y); y+= lineHeight;
            g2.drawString("Col: " + (player.getWorldX() + player.getSolidArea().x) / tileSize, x, y); y+= lineHeight;
            g2.drawString("Row: " + (player.getWorldY() + player.getSolidArea().y) / tileSize, x, y); y+= lineHeight;
            g2.drawString("God Mode: " + keyHandler.isGodModeOn(), x, y);

            g2.drawString("Draw Time: " + passed, x, y);
        }
    }

    public void drawToScreen(){

        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();

    }

    public void playMusic(int i){

        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic(){

        music.stop();
    }

    public void playSE(int i){

        soundEffect.setFile(i);
        soundEffect.play();
    }

    public void stopSE(){

        soundEffect.stop();
    }

    public Config getConfig() {
        return config;
    }

    public Sound getMusic() {
        return music;
    }

    public Sound getSoundEffect() {
        return soundEffect;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    /**
     *
     * @return the maximum number of maps
     */
    public int getMaxMap() {
        return maxMap;
    }

    /**
     *
     * @return the current map
     */
    public int getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(int currentMap) {
        this.currentMap = currentMap;
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    public EnvironmentManager getEnvironmentManager() {
        return eManager;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map){
        this.map = map;
    }

    public int getMaxScreenCol() {
        return maxScreenCol;
    }

    public int getMaxScreenRow() {
        return maxScreenRow;
    }

    public int getMaxWorldCol() {
        return maxWorldCol;
    }

    public int getMaxWorldRow() {
        return maxWorldRow;
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public int getWorldHeight() {
        return worldHeight;
    }

    public boolean isFullScreenOn() {
        return fullScreenOn;
    }

    public void setFullScreenOn(boolean fullScreenOn) {
        this.fullScreenOn = fullScreenOn;
    }

    public PathFinder getPathFinder() {
        return pathFinder;
    }

    public void setPathFinder(PathFinder pathFinder) {
        this.pathFinder = pathFinder;
    }

    public TileManager getTileManager() {
        return tileManager;
    }

    public void setTileManager(TileManager tileManager) {
        this.tileManager = tileManager;
    }

    public KeyHandler getKeyHandler() {
        return keyHandler;
    }

    public void setKeyHandler(KeyHandler keyHandler) {
        this.keyHandler = keyHandler;
    }

    public CollisionChecker getCollisionChecker() {
        return collisionChecker;
    }

    public void setCollisionChecker(CollisionChecker collisionChecker) {
        this.collisionChecker = collisionChecker;
    }

    public AssetSetter getAssetSetter() {
        return assetSetter;
    }

    public void setAssetSetter(AssetSetter assetSetter) {
        this.assetSetter = assetSetter;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public UI getUi() {
        return ui;
    }

    public void setUi(UI ui) {
        this.ui = ui;
    }

    public ArrayList<Entity> getParticlesList() {
        return particlesList;
    }

    public void setParticlesList(ArrayList<Entity> particlesList) {
        this.particlesList = particlesList;
    }

    public ArrayList<Entity> getEntityList() {
        return entityList;
    }

    public SaveLoad getSaveLoad() {
        return saveLoad;
    }

    public void setNpc(Entity[][] npc){
        this.npc = npc;
    }

    public Entity[][] getNpc(){
        return this.npc;
    }

    public void setNpc(int maxMap, int index, Entity newNpc){
        this.npc[maxMap][index] = newNpc;
    }

    public Entity getNpc(int maxMap, int index){
        return this.npc[maxMap][index];
    }

    public void setObj(Entity[][] obj){
        this.obj = obj;
    }

    public Entity[][] getObj(){
        return this.obj;
    }

    public void setObj(int maxMap, int index, Entity newObj){
        this.obj[maxMap][index] = newObj;
    }

    public Entity getObj(int maxMap, int index){
        return this.obj[maxMap][index];
    }

    public void setMonster(Entity[][] monster){
        this.monster = monster;
    }

    public Entity[][] getMonster(){
        return this.monster;
    }

    public void setMonster(int maxMap, int index, Entity newMonster){
        this.monster[maxMap][index] = newMonster;
    }

    public Entity getMonster(int maxMap, int index){
        return this.monster[maxMap][index];
    }

    public void setInteractiveTile(InteractiveTile[][] interactiveTile){
        this.interactiveTile = interactiveTile;
    }

    public InteractiveTile[][] getInteractiveTile(){
        return this.interactiveTile;
    }

    public void setInteractiveTile(int maxMap, int index, InteractiveTile interactiveTile){
        this.interactiveTile[maxMap][index] = interactiveTile;
    }

    public InteractiveTile getInteractiveTile(int maxMap, int index){
        return this.interactiveTile[maxMap][index];
    }

    public void setProjectile(Entity[][] projectile){
        this.projectile = projectile;
    }

    public Entity[][] getProjectile(){
        return this.projectile;
    }

    public void setProjectile(int maxMap, int index, Entity projectile){
        this.projectile[maxMap][index] = projectile;
    }

    public Entity getProjectile(int maxMap, int index){
        return this.projectile[maxMap][index];
    }

    public EntityGenerator getEntityGenerator() {
        return entityGenerator;
    }

    public void setEntityGenerator(EntityGenerator entityGenerator) {
        this.entityGenerator = entityGenerator;
    }

    public void setMaxWorldCol(int maxWorldCol) {
        this.maxWorldCol = maxWorldCol;
    }

    public void setMaxWorldRow(int maxWorldRow) {
        this.maxWorldRow = maxWorldRow;
    }

    public void changeArea(){

        if(nextArea != currentArea){
            stopMusic();

            if(nextArea == OUTSIDE){
                playMusic(0);
            }
            if(nextArea == INDOOR){
                playMusic(18);
            }
            if(nextArea == DUNGEON){
                playMusic(19);
            }

            assetSetter.setNpc();
        }

        currentArea = nextArea;
        assetSetter.setMonster();
    }
}

