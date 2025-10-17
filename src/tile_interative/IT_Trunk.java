package tile_interative;

import main.GamePanel;

public class IT_Trunk extends InteractiveTile {

    private final GamePanel gp;

    public IT_Trunk(GamePanel gp, int col, int row){
        super(gp, col, row);
        this.gp = gp;

        this.setWorldX(gp.getTileSize() * col);
        this.setWorldY(gp.getTileSize() * row);

        down1 = setup("res/tiles_interactive/trunk", gp.getTileSize(), gp.getTileSize());

        getSolidArea().x = 0;
        getSolidArea().y = 0;
        getSolidArea().width = 0;
        getSolidArea().height = 0;
        setSolidAreaDefaultX(getSolidArea().x);
        setSolidAreaDefaultY(getSolidArea().y);
    }
}
