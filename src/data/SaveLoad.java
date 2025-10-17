package data;

import entity.Entity;
import main.GamePanel;
import object.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;

public class SaveLoad {

    private final GamePanel gamePanel;

    public SaveLoad(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    public void save(){

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File("save.dat")));

            DataStorage dataStorage = new DataStorage();

            // PLAYER STATS
            dataStorage.setLevel(gamePanel.getPlayer().getLevel());
            dataStorage.setMaxLife(gamePanel.getPlayer().getMaxLife());
            dataStorage.setLife(gamePanel.getPlayer().getLife());
            dataStorage.setMaxMana(gamePanel.getPlayer().getMaxMana());
            dataStorage.setMana(gamePanel.getPlayer().getMana());
            dataStorage.setStrength(gamePanel.getPlayer().getStrength());
            dataStorage.setDexterity(gamePanel.getPlayer().getDexterity());
            dataStorage.setExp(gamePanel.getPlayer().getExp());
            dataStorage.setNextLevelExp(gamePanel.getPlayer().getNextLevelExp());
            dataStorage.setCoin(gamePanel.getPlayer().getCoin());

            // PLAYER INVENTORY
            for(int i = 0; i < gamePanel.getPlayer().getInventory().size(); i++){
                dataStorage.getItemNames().add(gamePanel.getPlayer().getInventory().get(i).getName());
                dataStorage.getItemAmounts().add(gamePanel.getPlayer().getInventory().get(i).getAmount());
            }

            // PLAYER EQUIPMENT
            dataStorage.setCurrentWeaponSlot(gamePanel.getPlayer().getCurrentWeaponSlot());
            dataStorage.setCurrentShieldSlot(gamePanel.getPlayer().getCurrentShieldSlot());
            dataStorage.setCurrentBootSlot(gamePanel.getPlayer().getCurrentBootSlot());

            // OBJECTS ON MAP
            dataStorage.setMapObjectNames(new String[gamePanel.getMaxMap()][gamePanel.getObj()[1].length]);
            dataStorage.setMapObjectWorldX(new int[gamePanel.getMaxMap()][gamePanel.getObj()[1].length]);
            dataStorage.setMapObjectWorldY(new int[gamePanel.getMaxMap()][gamePanel.getObj()[1].length]);
            dataStorage.setMapObjectLootNames(new String[gamePanel.getMaxMap()][gamePanel.getObj()[1].length]);
            dataStorage.setMapObjectOpened(new boolean[gamePanel.getMaxMap()][gamePanel.getObj()[1].length]);

            for(int mapNum = 0; mapNum < gamePanel.getMaxMap(); mapNum++){

                for(int i = 0; i < gamePanel.getObj()[1].length; i++){

                    if(gamePanel.getObj(mapNum, i) == null){
                        dataStorage.setMapObjectNames(mapNum, i, "N/A");

                    } else {
                        dataStorage.setMapObjectNames(mapNum, i, gamePanel.getObj(mapNum, i).getName());
                        dataStorage.setMapObjectWorldX(mapNum, i, gamePanel.getObj(mapNum, i).getWorldX());
                        dataStorage.setMapObjectWorldY(mapNum, i, gamePanel.getObj(mapNum, i).getWorldY());

                        if(gamePanel.getObj(mapNum, i).getLoot() != null){
                            dataStorage.setMapObjectLootNames(mapNum, i, gamePanel.getObj(mapNum, i).getLoot().getName());

                        }
                        dataStorage.setMapObjectOpened(mapNum, i, gamePanel.getObj(mapNum, i).isOpened());
                    }
                }
            }

            // Write the DataStorage object
            objectOutputStream.writeObject(dataStorage);

        } catch (Exception ex){
            System.out.println("Save Exception !");
            ex.printStackTrace();

        }
    }

    public void load(){

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File("save.dat")));

            // Read the DataStorage object
            DataStorage dataStorage = (DataStorage) objectInputStream.readObject();

            // PLAYER STATS
            gamePanel.getPlayer().setLevel(dataStorage.getLevel());
            gamePanel.getPlayer().setMaxLife(dataStorage.getMaxLife());
            gamePanel.getPlayer().setLife(dataStorage.getLife());
            gamePanel.getPlayer().setMaxMana(dataStorage.getMaxMana());
            gamePanel.getPlayer().setMana(dataStorage.getMana());
            gamePanel.getPlayer().setStrength(dataStorage.getStrength());
            gamePanel.getPlayer().setDexterity(dataStorage.getDexterity());
            gamePanel.getPlayer().setExp(dataStorage.getExp());
            gamePanel.getPlayer().setNextLevelExp(dataStorage.getNextLevelExp());
            gamePanel.getPlayer().setCoin(dataStorage.getCoin());

            // PLAYER INVENTORY
            gamePanel.getPlayer().getInventory().clear();
            for(int i = 0; i < dataStorage.getItemNames().size(); i++){
                gamePanel.getPlayer().getInventory().add(gamePanel.getEntityGenerator().getObject(dataStorage.getItemNames().get(i)));
                gamePanel.getPlayer().getInventory().get(i).setAmount(dataStorage.getItemAmounts().get(i));

            }

            // PLAYER EQUIPMENT
            gamePanel.getPlayer().setCurrentWeapon(gamePanel.getPlayer().getInventory().get(dataStorage.getCurrentWeaponSlot()));
            gamePanel.getPlayer().setCurrentShield(gamePanel.getPlayer().getInventory().get(dataStorage.getCurrentShieldSlot()));
            gamePanel.getPlayer().setCurrentBoots(gamePanel.getPlayer().getInventory().get(dataStorage.getCurrentBootSlot()));
            gamePanel.getPlayer().getAttack();
            gamePanel.getPlayer().getDefense();
            gamePanel.getPlayer().getPlayerMobility();
            gamePanel.getPlayer().getAttackImage();

            // OBJECTS ON MAP
            for(int mapNum = 0; mapNum < gamePanel.getMaxMap(); mapNum++){

                for(int i = 0; i < gamePanel.getObj()[1].length; i++){

                    if(dataStorage.getMapObjectNames(mapNum, i).equals("N/A")){
                        gamePanel.setObj(mapNum, i, null);

                    } else {
                        gamePanel.setObj(mapNum, i, gamePanel.getEntityGenerator().getObject(dataStorage.getMapObjectNames(mapNum, i)));
                        gamePanel.getObj(mapNum, i).setWorldX(dataStorage.getMapObjectWorldX(mapNum, i));
                        gamePanel.getObj(mapNum, i).setWorldY(dataStorage.getMapObjectWorldY(mapNum, i));


                        if(dataStorage.getMapObjectLootNames(mapNum, i) != null){
                            gamePanel.getObj(mapNum, i).setLoot(gamePanel.getEntityGenerator().getObject(dataStorage.getMapObjectLootNames(mapNum, i)));

                        }

                        gamePanel.getObj(mapNum, i).setOpened(dataStorage.isMapObjectOpened(mapNum, i));

                        if(gamePanel.getObj(mapNum, i).isOpened()){
                            gamePanel.getObj(mapNum, i).down1 = gamePanel.getObj(mapNum, i).image2;
                        }
                    }
                }
            }

        } catch (Exception ex){
            System.out.println("Load Exception !");
            ex.printStackTrace();
        }

    }
}
