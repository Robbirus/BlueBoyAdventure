package tile;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Map extends TileManager{

    private final GamePanel gp;
    private BufferedImage[] worldMap;
    private boolean miniMapOn = false;

    public Map(GamePanel gp){
        super(gp);
        this.gp = gp;
        createWorldMap();
    }

    public void createWorldMap(){

        worldMap = new BufferedImage[gp.getMaxMap()];
        int worldMapWidth = gp.getTileSize() * gp.getMaxWorldCol();
        int worldMapHeight = gp.getTileSize() * gp.getMaxWorldRow();

        for(int i = 0; i < gp.getMaxMap(); i++){

            worldMap[i] = new BufferedImage(worldMapWidth, worldMapHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D)worldMap[i].createGraphics();

            int col = 0;
            int row = 0;

            while(col < gp.getMaxWorldCol() && row < gp.getMaxWorldRow()){

                int tileNum = mapTileNum[i][col][row];
                int x = gp.getTileSize() * col;
                int y = gp.getTileSize() * row;

                g2.drawImage(tile[tileNum].image, x, y, null);

                col++;
                if(col == gp.getMaxWorldCol()) {
                    col = 0;
                    row++;
                }
            }
            g2.dispose();
        }
    }

    public void drawFullMapScreen(Graphics2D g2){

        // Background Color
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, gp.getScreenWidth(), gp.getScreenHeight());

        // Draw Map
        int width = 500;
        int height = 500;
        int x = gp.getScreenWidth()/2 - width/2;
        int y = gp.getScreenHeight()/2 - height/2;
        g2.drawImage(worldMap[gp.getCurrentMap()], x, y, width, height, null);

        // Draw Player
        double scale = (double)(gp.getTileSize() * gp.getMaxWorldCol())/width;
        int playerX = (int)(x + gp.getPlayer().getWorldX()/scale);
        int playerY = (int)(y + gp.getPlayer().getWorldY()/scale);
        int playerSize = (int)(gp.getTileSize()/scale);
        g2.drawImage(gp.getPlayer().down1, playerX, playerY, playerSize, playerSize, null);

        // Hint
        g2.setFont(gp.getUi().getMaruMonica().deriveFont(32f));
        g2.setColor(Color.WHITE);
        g2.drawString("Appuyez sur M", 750, 520);
        g2.drawString("pour fermer", 750, 550);
    }

    public void drawMiniMap(Graphics2D g2){

        if(miniMapOn){

            // Draw Map
            int width = 200;
            int height = 200;
            int x = gp.getScreenWidth() - width - 50;
            int y = 50;

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            g2.drawImage(worldMap[gp.getCurrentMap()], x, y, width, height, null);

            // Draw Player
            double scale = (double)(gp.getTileSize() * gp.getMaxWorldCol())/width;
            int playerX = (int)(x + gp.getPlayer().getWorldX()/scale);
            int playerY = (int)(y + gp.getPlayer().getWorldY()/scale);
            int playerSize = gp.getTileSize()/2;
            g2.drawImage(gp.getPlayer().down1, playerX-4, playerY-4, playerSize, playerSize, null);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }

    public boolean isMiniMapOn() {
        return miniMapOn;
    }

    public void setMiniMapOn(boolean miniMapOn) {
        this.miniMapOn = miniMapOn;
    }
}
