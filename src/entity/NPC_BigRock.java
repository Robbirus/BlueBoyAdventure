package entity;

import main.GamePanel;
import object.OBJ_IronDoor;
import tile_interative.IT_MetalPlate;
import tile_interative.InteractiveTile;

import java.awt.Rectangle;
import java.util.ArrayList;

public class NPC_BigRock extends Entity {

    private final GamePanel gamePanel = getGamePanel();
    public final static String NPC_NAME = "Big Rock";

    public NPC_BigRock(GamePanel gamePanel){
        super(gamePanel);

        setName(NPC_NAME);
        setDirection("down");
        setSpeed(4);

        setSolidArea(new Rectangle());
        getSolidArea().x = 2;
        getSolidArea().y = 6;
        setSolidAreaDefaultX(getSolidArea().x);
        setSolidAreaDefaultY(getSolidArea().y);
        getSolidArea().width = 44;
        getSolidArea().height = 40;

        setDialogueSet(-1);

        getImage();
        setDialogue();
    }

    public void getImage(){

        up1 = setup("res/npc/bigrock", gamePanel.getTileSize(), gamePanel.getTileSize());
        up2 = setup("res/npc/bigrock", gamePanel.getTileSize(), gamePanel.getTileSize());
        down1 = setup("res/npc/bigrock", gamePanel.getTileSize(), gamePanel.getTileSize());
        down2 = setup("res/npc/bigrock", gamePanel.getTileSize(), gamePanel.getTileSize());
        left1 = setup("res/npc/bigrock", gamePanel.getTileSize(), gamePanel.getTileSize());
        left2 = setup("res/npc/bigrock", gamePanel.getTileSize(), gamePanel.getTileSize());
        right1 = setup("res/npc/bigrock", gamePanel.getTileSize(), gamePanel.getTileSize());
        right2 = setup("res/npc/bigrock", gamePanel.getTileSize(), gamePanel.getTileSize());
    }

    public void setDialogue(){

        setDialogue(0, 0, "C'est une grande pierre.");
    }

    @Override
    public void setAction(){}

    @Override
    public void update(){}

    @Override
    public void speak(){

        // Do this character specific stuff
        facePlayer();
        startDialogue(this, getDialogueSet());

        setDialogueSet(getDialogueSet() + 1);

        // Si on atteint un index en dehors du tableau, on reset le dialogueSet a 0
        if(getDialogue(getDialogueSet(), 0) == null){
            setDialogueSet(getDialogueSet() - 1);
        }

    }

    @Override
    public void move(String direction) {

        this.setDirection(direction);

        checkCollision();

        if(!isCollisionOn()){

            switch (direction){
                case "up": setWorldY(getWorldY() - getSpeed()); break;
                case "down": setWorldY(getWorldY() + getSpeed()); break;
                case "left": setWorldX(getWorldX() - getSpeed()); break;
                case "right": setWorldX(getWorldX() + getSpeed()); break;
            }
        }

        detectPlate();
    }

    public void detectPlate(){

        ArrayList<InteractiveTile> plateList = new ArrayList<>();
        ArrayList<Entity> rockList = new ArrayList<>();

        // Create a plate list
        for(int i = 0; i < gamePanel.getInteractiveTile()[1].length; i++){

            if(gamePanel.getInteractiveTile(gamePanel.getCurrentMap(), i) != null &&
                    gamePanel.getInteractiveTile(gamePanel.getCurrentMap(), i).getName() != null &&
                    gamePanel.getInteractiveTile(gamePanel.getCurrentMap(), i).getName().equals(IT_MetalPlate.IT_NAME)){

                plateList.add(gamePanel.getInteractiveTile(gamePanel.getCurrentMap(), i));

            }
        }

        // Create a rock list
        for(int i = 0; i < gamePanel.getNpc()[1].length; i++){

            if(gamePanel.getNpc(gamePanel.getCurrentMap(), i) != null &&
                    gamePanel.getNpc(gamePanel.getCurrentMap(), i).getName().equals(NPC_BigRock.NPC_NAME)){

                rockList.add(gamePanel.getNpc(gamePanel.getCurrentMap(), i));

            }
        }

        int count = 0;

        // Scan the plate list
        for(int i = 0; i < plateList.size(); i++){

            int xDistance = Math.abs(getWorldX() - plateList.get(i).getWorldX());
            int yDistance = Math.abs(getWorldY() - plateList.get(i).getWorldY());
            int distance = Math.max(xDistance, yDistance);

            if(distance < 8){

                if(getLinkedEntity() == null){
                    setLinkedEntity(plateList.get(i));
                    gamePanel.playSE(3);
                }

            } else {
                if(getLinkedEntity() == plateList.get(i)) {
                    setLinkedEntity(null);
                }
            }
        }

        // Scan the rock list
        for(int i = 0; i < rockList.size(); i++){

            // Count the rock on the plate
            if(rockList.get(i).getLinkedEntity() != null){
                count++;
            }
        }

        // If all the rocks are on the plates, the iron door opens
        if(count == rockList.size()){

            for(int i = 0; i < gamePanel.getObj().length; i++){

                if(gamePanel.getObj(gamePanel.getCurrentMap(), i) != null && gamePanel.getObj(gamePanel.getCurrentMap(), i).getName().equals(OBJ_IronDoor.OBJ_NAME)){
                    gamePanel.setObj(gamePanel.getCurrentMap(), i, null);
                    gamePanel.playSE(21);
                }
            }
        }

    }
}
