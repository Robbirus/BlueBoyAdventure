package main;

import java.io.*;
import java.nio.file.Path;

public class Config {

    private final GamePanel gp;

    public Config(GamePanel gp){
        this.gp = gp;
    }

    public void saveConfig(){

        Path saveData = Path.of("config.txt");

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(saveData.toFile()));

            // Full Screen
            if(gp.isFullScreenOn()){
                bw.write("On");
            }
            if(!gp.isFullScreenOn()){
                bw.write("Off");
            }
            bw.newLine();

            // Music Volume
            bw.write(String.valueOf(gp.getMusic().getVolumeScale()));
            bw.newLine();

            // SE Volume
            bw.write(String.valueOf(gp.getSoundEffect().getVolumeScale()));
            bw.newLine();

            bw.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadConfig(){

        Path saveData = Path.of("config.txt");

        try {
            BufferedReader br = new BufferedReader(new FileReader(saveData.toFile()));

            String str = br.readLine();

            // Full screen
            if(str.equals("On")){
                gp.setFullScreenOn(true);
            }
            if(str.equals("Off")){
                gp.setFullScreenOn(false);
            }

            // Music Volume
            str = br.readLine();
            gp.getMusic().setVolumeScale(Integer.parseInt(str));

            // SE Volume
            str = br.readLine();
            gp.getSoundEffect().setVolumeScale(Integer.parseInt(str));

            br.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
