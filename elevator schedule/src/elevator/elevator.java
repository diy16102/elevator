package elevator;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class elevator extends Thread{
    /**
     * Elevator's attributes:
     * currentState: to show the three state(stop -> -1, on - > 0, up -> 1)
     * currentFloor: to show the current floor
     * currentMaxFloor: to show the max floor the elevator will stop
     * stopList: to store the floors which the elevator will stop
     */
    private int name;
    private int currentState;
    private int emerState;
    private int currentFloor;
    private int currentMaxFloor;
    private int maxUp;
    private int minDown;
    private Comparator<Integer> cmpUp = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
    };
    private Comparator<Integer> cmpDown = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }
    };
    private Queue<Integer> upStopList = new PriorityQueue<Integer>(15, cmpUp);
    private Queue<Integer> downStopList = new PriorityQueue<Integer>(15, cmpDown);
    private JButton[] buttonList;

    elevator(int name, int dir, JButton[] buttonList){
        this.name = name;
        maxUp = 0;
        minDown = 19;
        currentState = dir;
        currentFloor = 0;
        currentMaxFloor = 0;
        emerState = -1;
        this.buttonList = buttonList;
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        if(currentState == -2){
            emerState = this.currentState;
        }
        if(currentState == 2){
            currentState = emerState;
            emerState = -1;
        }
        this.currentState = currentState;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public void popUp() {
        upStopList.poll();
    }

    public void addUp(Integer pos){
        upStopList.add(pos);
    }

    public void popDown(Integer pos){
        downStopList.poll();
    }

    public void addDown(Integer pos){
        downStopList.add(pos);
    }

    public int upMax(){return maxUp;}

    public void setMaxUp(int maxUp){this.maxUp = maxUp;}

    public int downMin(){return minDown;}

    public void setMinDown(int minDown){this.minDown = minDown;}
