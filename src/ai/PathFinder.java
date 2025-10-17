package ai;

import entity.Entity;
import main.GamePanel;

import java.util.ArrayList;

public class PathFinder {

    private final GamePanel gamePanel;
    private Node[][] node;
    private ArrayList<Node> openList = new ArrayList<>();
    private ArrayList<Node> pathList = new ArrayList<>();
    private Node startNode;
    private Node goalNode;
    private Node currentNode;
    private boolean goalReached = false;
    private int step = 0;

    public PathFinder(GamePanel gamePanel){

        this.gamePanel = gamePanel;
        instantiateNodes();
    }

    public void instantiateNodes(){

        this.node = new Node[this.gamePanel.getMaxWorldCol()][this.gamePanel.getMaxWorldRow()];

        int col = 0;
        int row = 0;

        while(col < this.gamePanel.getMaxWorldCol() && row < this.gamePanel.getMaxWorldRow()){

            this.node[col][row] = new Node(col, row);

            col++;
            if(col == this.gamePanel.getMaxWorldCol()){
                col = 0;
                row++;
            }
        }
    }

    public void resetNodes(){

        int col = 0;
        int row = 0;

        while(col < this.gamePanel.getMaxWorldCol() && row < this.gamePanel.getMaxWorldRow()){

            // Reset open, checked and solid state
            this.node[col][row].setOpen(false);
            this.node[col][row].setChecked(false);
            this.node[col][row].setSolid(false);

            col++;
            if(col == this.gamePanel.getMaxWorldCol()){
                col = 0;
                row++;
            }
        }

        // Reset other settings
        this.openList.clear();
        this.pathList.clear();
        this.goalReached = false;
        this.step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow){

        this.resetNodes();

        // Set Start and Goal Node
        this.startNode = this.node[startCol][startRow];
        this.currentNode = this.startNode;
        this.goalNode = this.node[goalCol][goalRow];
        this.openList.add(this.currentNode);

        int col = 0;
        int row = 0;

        while(col < this.gamePanel.getMaxWorldCol() && row < this.gamePanel.getMaxWorldRow()){

            // SET SOLID NODE
            // CHECK TITLES
            int tileNum = this.gamePanel.getTileManager().mapTileNum[this.gamePanel.getCurrentMap()][col][row];
            if(this.gamePanel.getTileManager().tile[tileNum].isCollision()){
                this.node[col][row].setSolid(true);
            }
            // SET COST
            this.getCost(this.node[col][row]);

            col++;
            if(col == this.gamePanel.getMaxWorldCol()){
                col = 0;
                row++;
            }
        }

        // CHECK INTERACTIVE TILES
        for(int i = 0; i < this.gamePanel.getInteractiveTile()[1].length; i++){
            if(this.gamePanel.getInteractiveTile(this.gamePanel.getCurrentMap(), i) != null && this.gamePanel.getInteractiveTile(this.gamePanel.getCurrentMap(), i).isDestructible()){
                int itCol = this.gamePanel.getInteractiveTile(this.gamePanel.getCurrentMap(), i).getWorldX()/this.gamePanel.getTileSize();
                int itRow = this.gamePanel.getInteractiveTile(this.gamePanel.getCurrentMap(), i).getWorldY()/this.gamePanel.getTileSize();
                this.node[itCol][itRow].setSolid(true);
            }
        }
    }

    public void getCost(Node node){

        // G cost
        int xDistance = Math.abs(node.getCol() - this.startNode.getCol());
        int yDistance = Math.abs(node.getRow() - this.startNode.getRow());
        node.setgCost(xDistance + yDistance);
        // H Cost
        xDistance = Math.abs(node.getCol() - this.goalNode.getCol());
        yDistance = Math.abs(node.getRow() - this.goalNode.getRow());
        node.sethCost(xDistance + yDistance);
        // F Cost
        node.setfCost(node.getgCost() + node.gethCost());

    }

    public boolean search(){

        while(!this.goalReached && this.step < 500){

            int col = this.currentNode.getCol();
            int row = this.currentNode.getRow();

//          System.out.println("col " + col + " : row " + row);

            // Check the current node
            this.currentNode.setChecked(true);
            this.openList.remove(this.currentNode);

            // Open the UP node
            if(row - 1 >= 0){
                openNode(this.node[col][row-1]);
            }
            // Open the LEFT node
            if(col - 1 >= 0){
                openNode(this.node[col-1][row]);
            }
            // Open the DOWN node
            if(row + 1 < this.gamePanel.getMaxWorldRow()){
                openNode(this.node[col][row+1]);
            }
            // Open the RIGHT node
            if(col + 1 < this.gamePanel.getMaxWorldCol()){
                openNode(this.node[col+1][row]);
            }

            // FIND THE BEST NODE
            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for(int i = 0; i < this.openList.size(); i++){

                // Check if this node's F cost is better
                if(this.openList.get(i).getfCost() < bestNodefCost){
                    bestNodeIndex = i;
                    bestNodefCost = this.openList.get(i).getfCost();
                }
                // If F cost is equal, check the G cost
                else if(this.openList.get(i).getfCost() == bestNodefCost){
                    if(this.openList.get(i).getgCost() < this.openList.get(bestNodeIndex).getgCost()){
                        bestNodeIndex = i;
                    }
                }
            }

            // If there is no node in the openList, end of loop
            ///
            if(this.openList.isEmpty()){
                break;
            }

            // After the loop, openList[bestNodeIndex] is the next step (= currentNode)
            this.currentNode = this.openList.get(bestNodeIndex);

            if(this.currentNode == this.goalNode){
                this.goalReached = true;
                trackThePath();
            }
            this.step++;
        }

        return this.goalReached;
    }

    public void openNode(Node node){

        if(!node.isOpen() && !node.isChecked() && !node.isSolid()){

            node.setOpen(true);
            node.setParent(this.currentNode);
            this.openList.add(node);
        }
    }

    public void trackThePath(){

        Node current = this.goalNode;

        while (current != this.startNode){

            this.pathList.add(0, current);
            current = current.getParent();
        }
    }


    public ArrayList<Node> getPathList() {
        return pathList;
    }

    public Node getStartNode() {
        return startNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public Node getGoalNode() {
        return goalNode;
    }

    public void setGoalNode(Node goalNode) {
        this.goalNode = goalNode;
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    public boolean isGoalReached() {
        return goalReached;
    }

    public void setGoalReached(boolean goalReached) {
        this.goalReached = goalReached;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
