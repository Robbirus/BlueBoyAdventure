package entity;

import main.GamePanel;

import java.awt.Rectangle;
import java.util.Random;

public class NPC_OldMan extends Entity{

    private final GamePanel gamePanel = getGamePanel();

    public NPC_OldMan(GamePanel gp){
        super(gp);

        setDirection("down");
        setSpeed(2);

        setSolidArea(new Rectangle());
        getSolidArea().x = 8;
        getSolidArea().y = 16;
        setSolidAreaDefaultX(getSolidArea().x);
        setSolidAreaDefaultY(getSolidArea().y);
        getSolidArea().width = 30;
        getSolidArea().height = 30;

        setDialogueSet(-1);

        getOldManImage();
        setDialogue();
    }

    public void getOldManImage(){

        up1 = setup("res/npc/oldman_up_1", gamePanel.getTileSize(), gamePanel.getTileSize());
        up2 = setup("res/npc/oldman_up_2", gamePanel.getTileSize(), gamePanel.getTileSize());
        down1 = setup("res/npc/oldman_down_1", gamePanel.getTileSize(), gamePanel.getTileSize());
        down2 = setup("res/npc/oldman_down_2", gamePanel.getTileSize(), gamePanel.getTileSize());
        left1 = setup("res/npc/oldman_left_1", gamePanel.getTileSize(), gamePanel.getTileSize());
        left2 = setup("res/npc/oldman_left_2", gamePanel.getTileSize(), gamePanel.getTileSize());
        right1 = setup("res/npc/oldman_right_1", gamePanel.getTileSize(), gamePanel.getTileSize());
        right2 = setup("res/npc/oldman_right_2", gamePanel.getTileSize(), gamePanel.getTileSize());
    }

    public void setDialogue(){

        setDialogue(0, 0, "Salutation, voyageur.");
        setDialogue(0, 1, "Vous êtes donc venu sur cette ile pour trouver le\ntrésor ?");
        setDialogue(0, 2, "J'étais moi aussi un aventurier... \nMais je me suis pris une flèche dans le genou.");
        setDialogue(0, 3, "Bref ! Bonne chance.");

        setDialogue(1, 0, "Si tu fatigues, repose toi à la source.");
        setDialogue(1, 1, "Cependant, les monstres réapparaissent si tu te \nreposes près de la sources.\nAucune idée de pourquoi c'est comme ça.");
        setDialogue(1, 2, "Dans tous les cas, prends garde.");

        setDialogue(2, 0, "Je me demande comment ouvrir cette porte...");
    }

    @Override
    public void setAction(){

        if(isOnPath()){

            // int goalCol = 12;
            // int goalRow = 9;

            int goalCol = (gamePanel.getPlayer().getWorldX() + gamePanel.getPlayer().getSolidArea().x)/gamePanel.getTileSize();
            int goalRow = (gamePanel.getPlayer().getWorldY() + gamePanel.getPlayer().getSolidArea().y)/gamePanel.getTileSize();

            searchPath(goalCol, goalRow);

        } else {

            setActionLockCounter(getActionLockCounter() + 1);

            if(getActionLockCounter() == 120) {
                Random random = new Random();
                int i = random.nextInt(100) + 1; // Pick up a number from 1 to 100

                if (i <= 25) {
                    setDirection("up");
                }
                if (i > 25 && i <= 50) {
                    setDirection("down");
                }
                if (i > 50 && i <= 75) {
                    setDirection("left");
                }
                if (i > 75) {
                    setDirection("right");
                }

                setActionLockCounter(0);
            }
        }
    }

    @Override
    public void speak(){

        // Do this character specific stuff
        facePlayer();
        startDialogue(this, getDialogueSet());

        setDialogueSet(getDialogueSet() + 1);

        System.out.println(getDialogueSet());

        // Si on atteint un index en dehors du tableau, on reset le dialogueSet a 0
        if(getDialogue(getDialogueSet(), 0) == null){

            // Si la vie du jouer est en desous du tier de sa vie max, le dialogue joué est alors
            // le conseil de soin au lac
            if(gamePanel.getPlayer().getLife() < gamePanel.getPlayer().getMaxLife()/3){
                setDialogueSet(1);

            } else {

                setDialogueSet(0);
            }

        }

//        setOnPath(true);
    }
}
