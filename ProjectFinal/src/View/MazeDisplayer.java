package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas {

    private Maze maze;
    private int row_player;
    private int col_player;
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNameSolution=new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameEnd=new SimpleStringProperty();
    private Solution sol = null;
    boolean toDraw = false;
    double zoomFactor =1;
    public void setDraw(boolean draw)
    {
        toDraw = draw;
    }
    public void setSolution(Solution s)
    {
        this.sol =s;
    }
    public void setZoomFactor(double zoom){this.zoomFactor=zoom;}
    public MazeDisplayer()
    {
        this.heightProperty().addListener(e->{draw();});
        this.widthProperty().addListener(e->{draw();});
        this.row_player=-1;
        this.col_player=-1;
    }
    public void drawSol(Solution sol)
    {
        ArrayList<AState> mazeSolutionSteps = sol.getSolutionPath();
        for(int i = 1 ; i < mazeSolutionSteps.size()-1;i++)
        {
            String step = mazeSolutionSteps.get(i).toString();
            int h = Integer.parseInt(step.substring(0,step.indexOf(',')));
            int w = Integer.parseInt(step.substring(step.indexOf(',')+1));
            drawSol(h,w);
        }
    }
    public void drawPlayer(int oRow,int oCol)
    {
        double canvasHeight = getHeight()*zoomFactor;
        double canvasWidth = getWidth()*zoomFactor;
        int row = maze.getRowLen();
        int col = maze.getColLen();
        double cellHeight = canvasHeight/row;
        double cellWidth = canvasWidth/col;

        GraphicsContext graphicsContext = getGraphicsContext2D();
        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no Image player....");
        }
            double h_player = getRow_player() * cellHeight;
            double w_player = getCol_player() * cellWidth;
            graphicsContext.clearRect(oCol*cellWidth,oRow*cellHeight,cellWidth,cellHeight);
            graphicsContext.drawImage(playerImage,w_player,h_player,cellWidth,cellHeight);

    }
    public void drawSol(int x, int y)
    {
        double canvasHeight = getHeight()*zoomFactor;
        double canvasWidth = getWidth()*zoomFactor;
        int row = maze.getRowLen();
        int col = maze.getColLen();
        double cellHeight = canvasHeight/row;
        double cellWidth = canvasWidth/col;
        double h = x * cellHeight;
        double w = y * cellWidth;
        GraphicsContext graphicsContext = getGraphicsContext2D();
        graphicsContext.setFill(Color.BLACK);
        Image solImage=null;

        try{
            solImage = new Image(new FileInputStream(getImageFileNameSolution()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        getGraphicsContext2D().drawImage(solImage,w,h,cellWidth,cellHeight);

    }
    public void draw() {
        if( maze!=null){
            double canvasHeight = getHeight()*zoomFactor;
            double canvasWidth = getWidth()*zoomFactor;
            int row = maze.getRowLen();
            int col = maze.getColLen();
            double cellHeight = canvasHeight/row;
            double cellWidth = canvasWidth/col;
            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0,0,getWidth(),getHeight());
            graphicsContext.setFill(Color.BLACK);
            double w,h;
            //Draw Maze
            Image wallImage = null;
            try {
                wallImage = new Image(new FileInputStream(getImageFileNameWall()));
            } catch (FileNotFoundException e) {
                System.out.println("There is no file....");
            }
            for(int i=0;i<row;i++)
            {
                for(int j=0;j<col;j++)
                {
                    if(maze.getMaze()[i][j] == 1) // Wall
                    {
                        h = i * cellHeight;
                        w = j * cellWidth;
                        if (wallImage == null){
                            graphicsContext.fillRect(w,h,cellWidth,cellHeight);
                        }else{
                            graphicsContext.drawImage(wallImage,w,h,cellWidth,cellHeight);
                        }
                    }
                }
            }
            double h_player = getRow_player() * cellHeight;
            double w_player = getCol_player() * cellWidth;
            Image playerImage = null;
            try {
                playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
            } catch (FileNotFoundException e) {
                System.out.println("There is no Image player....");
            }
            Image EndImage=null;
            try{
                EndImage = new Image(new FileInputStream(getImageFileNameEnd()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if(EndImage!=null) {
                graphicsContext.drawImage(EndImage, maze.getGoalPosition().getColumnIndex() * cellWidth, maze.getGoalPosition().getRowIndex() * cellHeight, cellWidth, cellHeight);
            }
            graphicsContext.drawImage(playerImage,w_player,h_player,cellWidth,cellHeight);
            if(toDraw)
                drawSol(this.sol);
        }
    }
    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }
    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }
    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }
    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }
    public String getImageFileNameSolution() {
        return imageFileNameSolution.get();
    }
    public void setImageFileNameSolution(String imageFileNameSolution) {
        this.imageFileNameSolution.set(imageFileNameSolution);
    }
    public int getRow_player() {
        return row_player;
    }
    public int getCol_player() {
        return col_player;
    }
    public void set_player_position(int row, int col){
        int oRow = this.row_player;
        int oCol = this.col_player;
        this.row_player = row;
        this.col_player = col;
        drawPlayer(oRow,oCol);
    }

    public String getImageFileNameEnd() {
        return imageFileNameEnd.get();
    }

    public StringProperty imageFileNameEndProperty() {
        return imageFileNameEnd;
    }

    public void setImageFileNameEnd(String imageFileNameEnd) {
        this.imageFileNameEnd.set(imageFileNameEnd);
    }

    public void drawMaze(Maze maze) {
        this.maze = maze;
        row_player = maze.getStartPosition().getRowIndex();
        col_player = maze.getStartPosition().getColumnIndex();
        draw();
    }
}
