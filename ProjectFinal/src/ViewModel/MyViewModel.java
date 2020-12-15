package ViewModel;

import Model.IModel;
import algorithms.search.AState;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

public class MyViewModel extends Observable implements Observer {

    private IModel model;
    private Maze myMaze;
    private int rowChar;
    private int colChar;
    private Solution sol ;

    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);
        this.myMaze = null;
        this.sol = null;
    }

    public void update(Observable o, Object arg) {
        if(o instanceof IModel) {
            if(arg == "maze created"){
                this.myMaze = model.getMaze();
                this.model.CommunicateWithServer_SolveSearchProblem();
            }
            if(arg == "solution created")
                this.sol = model.getSolution();
            else if(arg == "position updated"|| arg == "succeeded")
            {
                this.rowChar = model.getRowChar();
                this.colChar = model.getColChar();
            }

            else if(arg == "file loaded")
            {
                this.myMaze = model.getMaze();
                this.rowChar = model.getRowChar();
                this.colChar = model.getColChar();
                this.model.CommunicateWithServer_SolveSearchProblem();
            }
            setChanged();
            notifyObservers(arg);
        }
    }
    public void saveMaze()
    {
        this.model.SaveMaze();
    }

    public void generateMaze(int row,int col) {
        this.model.CommunicateWithServer_MazeGenerating(row,col);
    }

    public void solveMaze() { model.CommunicateWithServer_SolveSearchProblem();}

    public void moveCharacter(KeyEvent keyEvent) {

        int direction=-1;
        switch (keyEvent.getCode()){
            case NUMPAD1:
                direction=1;
               break;
           case NUMPAD2: //Down
                direction=2;
                break;
            case NUMPAD3:
                direction=3;
               break;
            case NUMPAD4: //Left
                direction=4;
                break;

            case NUMPAD6:
               direction=6;
               break;
           case NUMPAD7: //Up
                direction=7;
                break;
            case NUMPAD8:
               direction=8;
                break;
            case NUMPAD9:
                direction=9;
               break;

       }

        this.model.updateCharacterLocation(direction);
    }

    public void close(){
        this.model.close();
    }
    public void open(){
        model.openMaze();
    }
    public Solution getSolution() {  return sol;
    }
    public Maze getMaze() { return myMaze; }
    public int getRowChar() { return this.model.getRowChar(); }
    public int getColChar() { return this.model.getColChar(); }
    public void setResizeEvent(){
    }

}
