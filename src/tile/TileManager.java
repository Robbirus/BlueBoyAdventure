package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class TileManager {

    private GamePanel gp;
    public Tile[] tile;
    public int[][][] mapTileNum;
    private boolean drawPath = true;

    private ArrayList<String> fileNames = new ArrayList<>();
    private ArrayList<String> collisionStatus = new ArrayList<>();

    public TileManager(GamePanel gp){

        this.gp = gp;

        // READ TILE DATA FILE
        InputStream inputStream = getClass().getResourceAsStream("/maps/tiledata.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        // GETTING TILE NAMES AND COLLISION INFO FROM THE FILE
        String line;

        try{
            while((line = bufferedReader.readLine()) != null){
                fileNames.add(line);
                collisionStatus.add(bufferedReader.readLine());

            }
            bufferedReader.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        // INITIALIZE THE TILE ARRAY BASED ON THE fileNames size
        tile = new Tile[fileNames.size()];
        getTileImage();

        // GET THE maxWorldCol & Row
        inputStream = getClass().getResourceAsStream("/maps/worldmap.txt");
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        try{
            String line2 = bufferedReader.readLine();
            String[] maxTile = line2.split(" ");

            gp.setMaxWorldCol(maxTile.length);
            gp.setMaxWorldRow(maxTile.length);

            mapTileNum = new int[gp.getMaxMap()][gp.getMaxWorldCol()][gp.getMaxWorldRow()];

            bufferedReader.close();

        } catch (IOException e){
            e.printStackTrace();
        }

        loadMap("res/maps/worldmap.txt", 0);
        loadMap("res/maps/indoor01.txt", 1);
        loadMap("res/maps/dungeon01.txt", 2);
        loadMap("res/maps/dungeon02.txt", 3);
    }

    public void getTileImage(){

       for(int i = 0; i < fileNames.size(); i++){

           String fileName;
           boolean collision;

           // Get a file name
           fileName = fileNames.get(i);

           // Get a collision status
           if(collisionStatus.get(i).equals("true")){
               collision = true;

           } else {
               collision = false;

           }

           setup(i, fileName, collision);

       }

    }

    public void setup(int index, String imageName, boolean collision){

        UtilityTool uTool = new UtilityTool();

        try{
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(new FileInputStream("res/tiles/" + imageName));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.getTileSize(), gp.getTileSize());
            tile[index].setCollision(collision);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath, int map){

        try{

            InputStream is = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while(col < gp.getMaxWorldCol() && row < gp.getMaxWorldRow()){

                String line = br.readLine();

                while(col < gp.getMaxWorldCol()){

                    String[] numbers = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[map][col][row] = num;
                    col++;
                }
                if(col == gp.getMaxWorldCol()){
                    col = 0;
                    row++;
                }
            }
            br.close();

        }catch (Exception e){

        }
    }

    public void draw(Graphics2D g2){

        int worldCol = 0;
        int worldRow = 0;

        while(worldCol < gp.getMaxWorldCol() && worldRow < gp.getMaxWorldRow()){

            int tileNum = mapTileNum[gp.getCurrentMap()][worldCol][worldRow];

            int worldX = worldCol * gp.getTileSize();
            int worldY = worldRow * gp.getTileSize();
            int screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX();
            int screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();


            if(worldX + gp.getTileSize() > gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() &&
                worldX - gp.getTileSize() < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX() &&
                worldY + gp.getTileSize() > gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY() &&
                worldY  - gp.getTileSize() < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()){

                g2.drawImage(tile[tileNum].image, screenX, screenY, null);

            }

            worldCol++;

            if(worldCol == gp.getMaxWorldCol()){
                worldCol = 0;
                worldRow++;
            }
        }

        if(isDrawPath()){
            g2.setColor(new Color(255,0,0,70));

            for(int i = 0; i < gp.getPathFinder().getPathList().size(); i++){

                int worldX = gp.getPathFinder().getPathList().get(i).getCol() * gp.getTileSize();
                int worldY = gp.getPathFinder().getPathList().get(i).getRow() * gp.getTileSize();
                int screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX();
                int screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();

                g2.fillRect(screenX, screenY, gp.getTileSize(), gp.getTileSize());

            }

        }
    }

    public boolean isDrawPath() {
        return drawPath;
    }

    public void setDrawPath(boolean drawPath) {
        this.drawPath = drawPath;
    }
}
