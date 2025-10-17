package tile_interative;

import main.GamePanel;

public class IT_MetalPlate extends InteractiveTile {

    private final GamePanel gamePanel;
    public final static String IT_NAME = "Metal Plate";

    public IT_MetalPlate(GamePanel gamePanel, int col, int row) {
        super(gamePanel, col, row);
        this.gamePanel = gamePanel;

        this.setWorldX(gamePanel.getTileSize() * col);
        this.setWorldY(gamePanel.getTileSize() * row);

        setName(IT_NAME);
        down1 = setup("res/tiles_interactive/metalplate", gamePanel.getTileSize(), gamePanel.getTileSize());

        getSolidArea().x = 0;
        getSolidArea().y = 0;
        getSolidArea().width = 0;
        getSolidArea().height = 0;
        setSolidAreaDefaultX(getSolidArea().x);
        setSolidAreaDefaultY(getSolidArea().y);
    }
}
