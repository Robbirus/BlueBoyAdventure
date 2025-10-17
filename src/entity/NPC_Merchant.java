package entity;

import java.awt.Rectangle;

import main.GamePanel;
import object.*;

public class NPC_Merchant extends Entity{

    private final GamePanel gamePanel = getGamePanel();

    public NPC_Merchant(GamePanel gp) {
        super(gp);

        setDirection("down");
        setSpeed(1);

        setSolidArea(new Rectangle());
        getSolidArea().x = 8;
        getSolidArea().y = 16;
        setSolidAreaDefaultX(getSolidArea().x);
        setSolidAreaDefaultY(getSolidArea().y);
        getSolidArea().width = 32;
        getSolidArea().height = 32;

        getImage();
        setDialogue();
        setItems();
    }

    public void getImage(){

        up1 = setup("res/npc/merchant_down_1", gamePanel.getTileSize(), gamePanel.getTileSize());
        up2 = setup("res/npc/merchant_down_2", gamePanel.getTileSize(), gamePanel.getTileSize());
        down1 = setup("res/npc/merchant_down_1", gamePanel.getTileSize(), gamePanel.getTileSize());
        down2 = setup("res/npc/merchant_down_2", gamePanel.getTileSize(), gamePanel.getTileSize());
        left1 = setup("res/npc/merchant_down_1", gamePanel.getTileSize(), gamePanel.getTileSize());
        left2 = setup("res/npc/merchant_down_2", gamePanel.getTileSize(), gamePanel.getTileSize());
        right1 = setup("res/npc/merchant_down_1", gamePanel.getTileSize(), gamePanel.getTileSize());
        right2 = setup("res/npc/merchant_down_2", gamePanel.getTileSize(), gamePanel.getTileSize());
    }

    public void setDialogue(){

        setDialogue(0, 0,"Salut étranger !\nAlors qu'est ce que t'achètes ?");

        setDialogue(1, 0,"Reviens quand tu veux l'ami !");

        setDialogue(2, 0,"Désolé mon ami, je ne fais pas crédit !\nReviens quand tu sera un peu plus riche.");

        setDialogue(3, 0,"Cela ne rentrera pas l'ami.\nVide d'abord tes poches !");

        setDialogue(4, 0,"Tu ne peux pas me vendre ça !\n(Objet équipé)");

    }

    public void setItems(){

        getInventory().add(new OBJ_Potion_Red(gamePanel));
        getInventory().add(new OBJ_Potion_Blue(gamePanel));
        getInventory().add(new OBJ_Key(gamePanel));
        getInventory().add(new OBJ_Sword_Normal(gamePanel));
        getInventory().add(new OBJ_Speed_Boots(gamePanel));
        getInventory().add(new OBJ_Axe(gamePanel));
        getInventory().add(new OBJ_Shield_Wood(gamePanel));
        getInventory().add(new OBJ_Shield_Blue(gamePanel));
    }

    @Override
    public void speak(){

        facePlayer();
        gamePanel.gameState = GamePanel.TRADE_STATE;
        gamePanel.getUi().setNpc(this);
    }
}
