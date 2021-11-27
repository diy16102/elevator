package Elevator;

import javax.swing.*;
import java.awt.*;
import java.util.*;



public class elevator extends Thread{

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

    public void run() {
        while(true){
            // 上升状态 Rising state
            while (currentState == 1){
                boolean blueFlag = false;
                for (int i = 1; i < 20; i++){
                    buttonList[i].setText("Up");
                }
                // 下客  Drop off
                if (!upStopList.isEmpty() && currentFloor  == upStopList.peek()) {
                    while (currentFloor  == upStopList.peek()) {
                        Integer a = upStopList.poll();
                        ui.logs.append("Elevator" + name + ": number" + (currentFloor + 1) + " floor" + " occupants out\n");
                        if(upStopList.isEmpty())
                            break;
                    }
                    buttonList[currentFloor].setBackground(Color.BLUE);
                    blueFlag = true;
                }
                // 载上当前上升的人 pickup people who are currently rising
                while (!ui.queLock[currentFloor][0]);
                ui.queLock[currentFloor][0] = false;
                if (!ui.queue[currentFloor][0].isEmpty()) {
                    for (int i = 0; i < ui.queue[currentFloor][0].size(); i++) {
                        if ((int) ui.queue[currentFloor][0].get(i) - 1 > maxUp) {
                            maxUp = (int) ui.queue[currentFloor][0].get(i) - 1;
                        }
                        addUp((Integer) ui.queue[currentFloor][0].get(i) - 1);
                        ui.logs.append("Elevator" + name + ": number" + (currentFloor + 1) + " floor occupants to " + ui.queue[currentFloor][0].get(i)
                                + " floor\n");
                    }
                    buttonList[currentFloor].setBackground(Color.BLUE);
                    blueFlag = true;
                }
                ui.queue[currentFloor][0].clear();
                ui.queLock[currentFloor][0] = true;
                // 电梯走空 载上向下的人 The elevator goes empty, carrying people up or down
                while (!ui.queLock[currentFloor][1]);
                ui.queLock[currentFloor][1] = false;
                if (upStopList.isEmpty() && !ui.queue[currentFloor][1].isEmpty()){
                    for (int i = 0; i < ui.queue[currentFloor][1].size();i++){
                        if ((int)ui.queue[currentFloor][1].get(i) - 1 < minDown){
                            minDown = (int)ui.queue[currentFloor][1].get(i) - 1;
                        }
                        addDown((Integer) ui.queue[currentFloor][1].get(i) - 1);
                        ui.logs.append("Elevator" + name + ": number" + (currentFloor + 1) + " floor occupants to " + ui.queue[currentFloor][1].get(i)
                                + " floor\n");
                    }
                    if (!downStopList.isEmpty()){
                        ui.queue[currentFloor][1].clear();
                        setCurrentState(-1);
                        blueFlag = true;
                        ui.queLock[currentFloor][1] = true;
                        ui.logs.append("Elevator" + name + " :Going down\n");
                        break;
                    }
                }
                ui.queLock[currentFloor][1] = true;

                if (blueFlag){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    buttonList[currentFloor].setBackground(Color.RED);
                }
                // 电梯空了 到顶了 The elevator is empty, it's at the top of the building
                if (upStopList.isEmpty() || currentFloor == 19){
                    setCurrentState(0);
                    maxUp = 0;
                    minDown = 19;
                    buttonList[currentFloor].setBackground(Color.RED);
                    ui.logs.append("Elevator" + name + ": Stop\n");
                    break;
                }
                buttonList[currentFloor].setBackground(Color.WHITE);
                currentFloor++;
                buttonList[currentFloor].setBackground(Color.RED);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 下降状态 down status
            while(currentState == -1){
                boolean blueFlag = false;
                for (int i = 1; i < 20; i++){
                    buttonList[i].setText("Down");
                }
                // 下客 drop off people
                if (!downStopList.isEmpty() && currentFloor  == downStopList.peek()) {
                    System.out.println(downStopList.peek());
                    while (currentFloor  == downStopList.peek()) {
                        Integer a = downStopList.poll();
                        ui.logs.append("Elevator" + name + ": number" + (currentFloor + 1) + " floor" + " occupants out\n");
                        if(downStopList.isEmpty())
                            break;
                    }
                    buttonList[currentFloor].setBackground(Color.BLUE);
                    blueFlag = true;
                }
                // 载上当前下降的人 pickup people who are currently going down
                while (!ui.queLock[currentFloor][1]);
                ui.queLock[currentFloor][1] = false;
                if (!ui.queue[currentFloor][1].isEmpty()) {
                    for (int i = 0; i < ui.queue[currentFloor][1].size(); i++) {
                        if ((int) ui.queue[currentFloor][1].get(i) - 1 < minDown) {
                            minDown = (int) ui.queue[currentFloor][1].get(i) - 1;
                        }
                        addDown((Integer) ui.queue[currentFloor][1].get(i) - 1);
                        ui.logs.append("Elevator" + name + ": number" + (currentFloor + 1) + " floor occupants to " + ui.queue[currentFloor][1].get(i)
                                + " floor\n");
                    }
                    buttonList[currentFloor].setBackground(Color.BLUE);
                    blueFlag = true;
                }
                ui.queue[currentFloor][1].clear();
                ui.queLock[currentFloor][1] = true;

                // 电梯走空 载上向上的人 The elevator goes empty and carries doing up people.
                while (!ui.queLock[currentFloor][0]);
                ui.queLock[currentFloor][0] = false;
                if (downStopList.isEmpty() && !ui.queue[currentFloor][0].isEmpty()){
                    for (int i = 0; i < ui.queue[currentFloor][0].size();i++){
                        if ((int)ui.queue[currentFloor][0].get(i) - 1 > maxUp){
                            maxUp = (int)ui.queue[currentFloor][0].get(i) - 1;
                        }
                        addUp((Integer) ui.queue[currentFloor][0].get(i) - 1);
                        ui.logs.append("Elevator" + name + ": number" + (currentFloor + 1) + " floor occupants to " + ui.queue[currentFloor][0].get(i)
                                + " floor\n");
                    }
                    if (!upStopList.isEmpty()){
                        ui.queue[currentFloor][0].clear();
                        setCurrentState(1);
                        blueFlag = true;
                        ui.queLock[currentFloor][0] = true;
                        ui.logs.append("Elevator" + name + " :Going up\n");
                        break;
                    }
                }
                ui.queLock[currentFloor][0] = true;
                if (blueFlag){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    buttonList[currentFloor].setBackground(Color.RED);
                }
                // 电梯走空 到底 The elevator goes empty to the bottom
                if (downStopList.isEmpty() || currentFloor == 0){
                    buttonList[currentFloor].setBackground(Color.RED);
                    setCurrentState(0);
                    maxUp = 0;
                    minDown = 19;
                    ui.logs.append("Elevator" + name + ": Stop\n");
                    break;
                }
                buttonList[currentFloor].setBackground(Color.WHITE);
                currentFloor--;
                buttonList[currentFloor].setBackground(Color.RED);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 停滞状态  Stagnant state
            while(currentState == 0){
                for (int i = 1; i < 20; i++){
                    buttonList[i].setText("-");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 防止线程阻塞  Prevent thread blocking
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
