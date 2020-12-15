package algorithms.search;

public abstract class AState {
    private double cost;
    AState prev;
    private int stepscounter;

    public int getStepscounter() {
        return stepscounter;
    }

    public void setStepscounter(int stepscounter) {
        this.stepscounter = stepscounter;
    }

    AState(){
        stepscounter=0;
        this.prev = null;
        this.cost = 0;
    }

    public double getCost() {
        return this.cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setPrev(AState prev) {
        this.prev = prev;
    }
    public AState getPrev() {
        return prev;
    }
    public abstract String toString();
}