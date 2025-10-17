package environment;

import main.GamePanel;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Lighting {

    private final GamePanel gp;
    private BufferedImage darknessFilter;
    private int dayCounter;
    private float filterAlpha = 0f;

    // DAY STATE
    public final static int DAY = 0;
    public final static int DUSK = 1;
    public final static int NIGHT = 2;
    public final static int DAWN = 3;
    public int dayState = DAY;

    public Lighting(GamePanel gp){
        this.gp = gp;
        setLightSource();
    }

    public void setLightSource(){

        // Create a buffered image
        this.darknessFilter = new BufferedImage(gp.getScreenWidth(), gp.getScreenHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) darknessFilter.getGraphics();

        if(gp.getPlayer().getCurrentLight() == null){
            g2.setColor(new Color(0, 0, 0.1f, 0.97f));

        } else {

            // Get the center x and y of the light circle
            int centerX = gp.getPlayer().getScreenX() + (gp.getTileSize())/2;
            int centerY = gp.getPlayer().getScreenY() + (gp.getTileSize())/2;

            // Create a gradation effect within the light circle
            Color[] colors = new Color[12];
            float[] fractions = new float[12];

            colors[0] = new Color(0, 0, 0.1f, 0.1f);
            colors[1] = new Color(0, 0, 0.1f, 0.42f);
            colors[2] = new Color(0, 0, 0.1f, 0.52f);
            colors[3] = new Color(0, 0, 0.1f, 0.61f);
            colors[4] = new Color(0, 0, 0.1f, 0.69f);
            colors[5] = new Color(0, 0, 0.1f, 0.76f);
            colors[6] = new Color(0, 0, 0.1f, 0.82f);
            colors[7] = new Color(0, 0, 0.1f, 0.87f);
            colors[8] = new Color(0, 0, 0.1f, 0.91f);
            colors[9] = new Color(0, 0, 0.1f, 0.92f);
            colors[10] = new Color(0, 0, 0.1f, 0.93f);
            colors[11] = new Color(0, 0, 0.1f, 0.94f);

            fractions[0] = 0f;
            fractions[1] = 0.4f;
            fractions[2] = 0.5f;
            fractions[3] = 0.6f;
            fractions[4] = 0.65f;
            fractions[5] = 0.7f;
            fractions[6] = 0.75f;
            fractions[7] = 0.8f;
            fractions[8] = 0.85f;
            fractions[9] = 0.9f;
            fractions[10] = 0.95f;
            fractions[11] = 1f;

            // Create a gradation paint settings for the light circle
            RadialGradientPaint gPaint = new RadialGradientPaint(centerX, centerY, gp.getPlayer().getCurrentLight().getLightRadius(), fractions, colors);

            // Set the gPaint data on g2
            g2.setPaint(gPaint);

        }

        g2.fillRect(0, 0, gp.getScreenWidth(), gp.getScreenHeight());

        g2.dispose();

    }

    public void resetDay(){
        dayState = DAY;
        filterAlpha = 0f;
    }

    public void update() {

        if (gp.getPlayer().isLightUpdated()) {
            setLightSource();
            gp.getPlayer().setLightUpdated(false);
        }

        // CHECK THE STATE OF THE DAY
        switch (dayState) {
        case DAY:
            dayCounter++;

            if (dayCounter > 18000) { // <= 36 000 = 10 minutes / 600 = 9.6 secondes / 39600 = 11 minutes
                dayState = DUSK;
                dayCounter = 0;
            }
            break;
        case DUSK:
            filterAlpha += 0.0006f; // 0.001f x 1000 = 1f, 1000/60 0f -> 1f = 16,6 secondes /  0.0003f x 3600 = 1f, 3600/60 0f -> 1f = 60 secondes

            if (filterAlpha > 1f) {
                filterAlpha = 1f;
                dayState = NIGHT;
            }
            break;
        case NIGHT:
            dayCounter++;

            if (dayCounter > 18000) {
                dayState = DAWN;
                dayCounter = 0;
            }
            break;
        case DAWN:
            filterAlpha -= 0.0006f;

            if (filterAlpha < 0f) {
                filterAlpha = 0f;
                dayState = DAY;
            }
            break;
        }
    }

    public void draw(Graphics2D g2){

        if(gp.currentArea == GamePanel.OUTSIDE) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, filterAlpha));
        }
        if(gp.currentArea == GamePanel.OUTSIDE || gp.currentArea == GamePanel.DUNGEON){
            g2.drawImage(darknessFilter, 0, 0, null);
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        //DEBUG
        String situation = "";
        switch (dayState){
        case DAY: situation = "Day"; break;
        case DUSK: situation = "Dusk"; break;
        case NIGHT: situation = "Night"; break;
        case DAWN: situation = "Dawn"; break;
        }

        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(50f));
        g2.drawString(situation, 800, 500);

    }

    public void setFilterAlpha(float filterAlpha) {
        this.filterAlpha = filterAlpha;
    }

    public float getFilterAlpha() {
        return filterAlpha;
    }

    public void setDayState(int dayState) {
        this.dayState = dayState;
    }

    public void setDayCounter(int dayCounter) {
        this.dayCounter = dayCounter;
    }
}
