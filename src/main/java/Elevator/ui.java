package Elevator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;


public class ui {
    public static JLabel[] labels = new JLabel[20];
    public static JComboBox[] up = new JComboBox[20];
    public static JComboBox[] down = new JComboBox[20];
    public static JButton[] forOne = new JButton[20];
    public static JButton[] forTwo = new JButton[20];
    public static JButton[] forThree = new JButton[20];
    public static JButton[] forFour = new JButton[20];
    public static JButton[] forFive = new JButton[20];
    public static TextArea logs = new TextArea();
    public static elevator one;
    public static elevator two;
    public static elevator three;
    public static elevator four;
    public static elevator five;

    public static ArrayList<elevator> elevators = new ArrayList<elevator>();
    public static ArrayList[][] queue = new ArrayList[20][2];
    public static boolean queLock[][] = new boolean[20][2];

    //    private static String helpMessage2 = """
//            <html><h2>Instruction</h2><ul><li>Button on the left indicates floor occupants want to go，needed will show" +
//            "green</li><li>Red button indicates current position of elevator，initial is“－”，up and down as “UP”and“DOWN”</li><li>Blue indicates load off occupants " +
//            "will wait a little time</li><li>First row of buttons shows status of elevator, Click will stop elevator for emergency,Click again will release it</li></ul><h2>Matters" +
//            "need attention</h2><ul><li>Elevator going up will pick up all the occupants going up</li><li>Elevator" +
//            " going down will pick up all the occupants going down</li></ul><h2>Algorithm</h2><h3>Occupants</h3><ul><li>When ride needed" +
//            " check all elevator status,closest and moving elevator have higher priority</li><li>If there is no moving elevator" +
//            " closest elevator will move</li></ul><h3>Elevator</h3><ul><li>Initial status of elevator will stay still response to up and down </li><li>Elevator self check" +
//            "change current status</li><li>Elevator will check if there is occupants going off at every stopped floor</li><li>Elevator will check if " +
//            "there is occupants going in at every stopped floor</li><li>When elevator empty but there was needed occupants elevator will change  " +
//            " status to moving</li></ul></html>
//            """;
    private static String helpMessage = "<html><h2>Instruction</h2><ul><li>Button on the left indicates floor occupants want to go，needed will show" +
            "green</li><li>Red button indicates current position of elevator，initial is“－”，up and down as “UP”and“DOWN”</li><li>Blue indicates load off occupants " +
            "will wait a little time</li><li>First row of buttons shows status of elevator, Click will stop elevator for emergency,Click again will release it</li></ul><h2>Matters" +
            "need attention</h2><ul><li>Elevator going up will pick up all the occupants going up</li><li>Elevator" +
            " going down will pick up all the occupants going down</li></ul><h2>Algorithm</h2><h3>Occupants</h3><ul><li>When ride needed" +
            " check all elevator status,closest and moving elevator have higher priority</li><li>If there is no moving elevator" +
            " closest elevator will move</li></ul><h3>Elevator</h3><ul><li>Initial status of elevator will stay still response to up and down </li><li>Elevator self check" +
            "change current status</li><li>Elevator will check if there is occupants going off at every stopped floor</li><li>Elevator will check if " +
            "there is occupants going in at every stopped floor</li><li>When elevator empty but there was needed occupants elevator will change  " +
            " status to moving</li></ul></html>";


    public static void init() {
        // response wait que lock initialization
        for (int i = 0; i < 20; i++) {
            queLock[i][0] = true;
            queLock[i][1] = true;
        }
        // floor number initialization
        for (int i = 0; i < 20; i++) {
            labels[i] = new JLabel(String.valueOf(i + 1));
            labels[i].setBackground(Color.WHITE);
            labels[i].setOpaque(true);
        }
        // response que initialization
        for (int i = 0; i < 20; i++) {
            // up wait que
            queue[i][0] = new ArrayList<Integer>();
            // down wait que
            queue[i][1] = new ArrayList<Integer>();
        }
        // up button initialization
        for (int i = 0; i < 20; i++) {
            up[i] = new JComboBox();
            up[i].addItem("-");
            for (int k = i + 2; k <= 20; k++) {
                up[i].addItem(String.valueOf(k));
            }
            final int finalI = i;
            up[i].addItemListener(e -> {
                if (ItemEvent.SELECTED == e.getStateChange() && !up[finalI].getSelectedItem().toString().equals("-")) {
                    queue[finalI][0].add(Integer.parseInt(up[finalI].getSelectedItem().toString()));
                    labels[finalI].setBackground(Color.GREEN);
                    up[finalI].setSelectedIndex(0);
                    logs.append("Level " + (finalI + 1) + " floor passager need to go Level "
                            + queue[finalI][0] + " floor \n");
                }
            });
        }
        // down selection button initialization
        for (int i = 0; i < 20; i++) {
            down[i] = new JComboBox();
            down[i].addItem("-");
            for (int k = i; k > 0; k--) {
                down[i].addItem(String.valueOf(k));
            }
            final int finalI = i;
            down[i].addItemListener(e -> {
                if (ItemEvent.SELECTED == e.getStateChange() && !down[finalI].getSelectedItem().toString().equals("-")) {
                    queue[finalI][1].add(Integer.parseInt(down[finalI].getSelectedItem().toString()));
                    labels[finalI].setBackground(Color.GREEN);
                    down[finalI].setSelectedIndex(0);
                    logs.append("Level " + (finalI + 1) + "floor passager need to go Level "
                            + queue[finalI][1] + " floor\n");
                }
            });
        }
        // initialize elevator 1
        for (int i = 0; i < 20; i++) {
            forOne[i] = new JButton("-");
            forOne[i].setOpaque(true);
            forOne[i].setBackground(Color.WHITE);
        }
        forOne[0].setBackground(Color.RED);
        forOne[0].setText("Normal");
        forOne[0].addActionListener(e -> {
            if (forOne[0].getText().equals("Normal")) {
                forOne[0].setText("Unusual");
                forOne[0].setBackground(Color.ORANGE);
                one.setCurrentState(-2);
                logs.append("Elevator 1 malfunction stop operation!\n");
            } else {
                forOne[0].setText("Normal");
                forOne[0].setBackground(Color.WHITE);
                one.setCurrentState(2);
                logs.append("Elevator 1 back to operation!\n");
            }
        });
        // initialize elevator 2
        for (int i = 0; i < 20; i++) {
            forTwo[i] = new JButton("-");
            forTwo[i].setOpaque(true);
            forTwo[i].setBackground(Color.WHITE);
        }
        forTwo[0].setBackground(Color.RED);
        forTwo[0].setText("Normal");
        forTwo[0].addActionListener(e -> {
            if (forTwo[0].getText().equals("Normal")) {
                forTwo[0].setText("Unusual");
                forTwo[0].setBackground(Color.ORANGE);
                two.setCurrentState(-2);
                logs.append("Elevator 2 malfunction stop operation!\n");
            } else {
                forTwo[0].setText("Normal");
                forTwo[0].setBackground(Color.WHITE);
                two.setCurrentState(2);
                logs.append("Elevator 2 back to operation!\n");
            }
        });
        // initialize elevator 3
        for (int i = 0; i < 20; i++) {
            forThree[i] = new JButton("-");
            forThree[i].setOpaque(true);
            forThree[i].setBackground(Color.WHITE);
        }
        forThree[0].setBackground(Color.RED);
        forThree[0].setText("Normal");
        forThree[0].addActionListener(e -> {
            if (forThree[0].getText().equals("Normal")) {
                forThree[0].setText("Unusual");
                forThree[0].setBackground(Color.ORANGE);
                three.setCurrentState(-2);
                logs.append("Elevator 3 malfunction stop operation!\n");
            } else {
                forThree[0].setText("Normal");
                forThree[0].setBackground(Color.WHITE);
                three.setCurrentState(2);
                logs.append("Elevator 3 back to operation!\n");
            }
        });
        // initialize elevator  4
        for (int i = 0; i < 20; i++) {
            forFour[i] = new JButton("-");
            forFour[i].setOpaque(true);
            forFour[i].setBackground(Color.WHITE);
        }
        forFour[0].setBackground(Color.RED);
        forFour[0].setText("Normal");
        forFour[0].addActionListener(e -> {
            if (forFour[0].getText().equals("Normal")) {
                forFour[0].setText("Unusual");
                forFour[0].setBackground(Color.ORANGE);
                four.setCurrentState(-2);
                logs.append("Elevator 4 malfunction stop operation!\n");
            } else {
                forFour[0].setText("Normal");
                forFour[0].setBackground(Color.WHITE);
                four.setCurrentState(2);
                logs.append("Elevator 4 back to operation!\n");
            }
        });
        // initialize elevator 5
        for (int i = 0; i < 20; i++) {
            forFive[i] = new JButton("-");
            forFive[i].setOpaque(true);
            forFive[i].setBackground(Color.WHITE);
        }
        forFive[0].setBackground(Color.RED);
        forFive[0].setText("Normal");
        forFive[0].addActionListener(e -> {
            if (forFive[0].getText().equals("Normal")) {
                forFive[0].setText("Unusual");
                forFive[0].setBackground(Color.ORANGE);
                five.setCurrentState(-2);
                logs.append("Elevator 5 malfunction stop operation!\n");
            } else {
                forFive[0].setText("Normal");
                forFive[0].setBackground(Color.WHITE);
                five.setCurrentState(2);
                logs.append("Elevator 5 back to operation!\n");
            }
        });

        JFrame frame = new JFrame("Elevator");
        frame.setLayout(new GridLayout(1, 2));
        GridLayout grid = new GridLayout(21, 8);
        Container c = new Container();
        c.setLayout(grid);
        // label 
        c.add(new JLabel("Floor"));
        c.add(new JLabel("UP"));
        c.add(new JLabel("DOWN"));
        c.add(new JLabel("ELe1"));
        c.add(new JLabel("Ele2"));
        c.add(new JLabel("Ele3"));
        c.add(new JLabel("Ele4"));
        c.add(new JLabel("Ele5"));
        // button
        for (int i = 20; i > 0; i--) {
            c.add(labels[i - 1]);
            c.add(up[i - 1]);
            c.add(down[i - 1]);
            for (int k = 0; k < 5; k++) {
                c.add(forOne[i - 1]);
                c.add(forTwo[i - 1]);
                c.add(forThree[i - 1]);
                c.add(forFour[i - 1]);
                c.add(forFive[i - 1]);
            }
        }

        logs.setEditable(false);
        logs.setFont(new Font("黑体", Font.BOLD, 32));
        frame.add(c);
        JScrollPane pane = new JScrollPane(logs);
        frame.add(pane);

        frame.setSize(new Dimension(2000, 1500));
        frame.setVisible(true);

        // intialize elevator
        one = new elevator(1, 0, forOne);
        elevators.add(one);
        two = new elevator(2, 0, forTwo);
        elevators.add(two);
        three = new elevator(3, 0, forThree);
        elevators.add(three);
        four = new elevator(4, 0, forFour);
        elevators.add(four);
        five = new elevator(5, 0, forFive);
        elevators.add(five);

        // help box
        JOptionPane.showMessageDialog(null,
                helpMessage,
                "Instruction",
                JOptionPane.DEFAULT_OPTION);
    }

    static class lightManger extends Thread {
        lightManger() {
            start();
        }

        public void run() {
            while (true) {
                for (int i = 0; i < 20; i++) {
                    if (queue[i][0].isEmpty() && queue[i][1].isEmpty()) {
                        labels[i].setBackground(Color.WHITE);
                    }
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class elevatorManager extends Thread {
        elevatorManager() {
            start();
        }

        public void adjust(int index, int i) throws InterruptedException {
            // Closest un-moving elevator below request or current floor .
            if (elevators.get(index).getCurrentFloor() < i) {
                elevators.get(index).setCurrentState(1);
                elevators.get(index).addUp(i);
                elevators.get(index).setMaxUp(i);
                logs.append("Elevator" + (index + 1) + "Going Up\n");
                Thread.sleep(500);
                return;
            }
            // Closest un-moving elevator above request or current floor .
            if (elevators.get(index).getCurrentFloor() > i) {
                elevators.get(index).setCurrentState(-1);
                elevators.get(index).addDown(i);
                elevators.get(index).setMinDown(i);
                logs.append("Elevator" + (index + 1) + "Going Down\n");
                Thread.sleep(500);
                return;
            }
            // Closest elevator in the same floor of request or current floor.
            if (elevators.get(index).getCurrentFloor() == i) {
                elevators.get(index).setCurrentState(1);
                logs.append("Elevator" + (index + 1) + "Launch\n");
                Thread.sleep(500);
                return;
            }
        }

        public void run() {
            while (true) {
                for (int i = 0; i < 20; i++) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //  up que monitor. 
//                    System.out.println(queLock[i][0]);
                    while (!queLock[i][0]) ;
                    if (!queue[i][0].isEmpty()) {
                        int index = -1, distance = 1000000;
                        for (int k = 0; k < 5; k++) {
                            if (elevators.get(k).getCurrentState() == 0 && !queue[i][0].isEmpty()) {
                                if (Math.abs(elevators.get(k).getCurrentFloor() - i) < distance) {
                                    index = k;
                                    distance = Math.abs(elevators.get(k).getCurrentFloor() - i);
                                }
                            }
                            if (elevators.get(k).getCurrentFloor() >= i && elevators.get(k).getCurrentState() == -1
                                    && elevators.get(k).downMin() >= i) {
                                if (Math.abs(elevators.get(k).getCurrentFloor() - i) < distance) {
                                    index = -1;
                                    distance = Math.abs(elevators.get(k).getCurrentFloor() - i);
                                }
                            }
                            if (elevators.get(k).getCurrentFloor() <= i && elevators.get(k).getCurrentState() == 1
                                    && elevators.get(k).upMax() >= i) {
                                if (Math.abs(elevators.get(k).getCurrentFloor() - i) < distance) {
                                    index = -1;
                                    distance = Math.abs(elevators.get(k).getCurrentFloor() - i);
                                }
                            }
                        }
                        System.out.println(i + ":" + index);
                        if (index != -1 && !queue[i][0].isEmpty()) {
                            try {
                                adjust(index, i);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                for (int i = 0; i < 20; i++) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // down que monitor
//                    System.out.println(queLock[i][1]);
                    while (!queLock[i][1]) ;
                    if (!queue[i][1].isEmpty()) {
                        int index = -1, distance = 1000000;
                        for (int k = 0; k < 5; k++) {
                            if (elevators.get(k).getCurrentState() == 0 && !queue[i][1].isEmpty()) {
                                if (Math.abs(elevators.get(k).getCurrentFloor() - i) < distance) {
                                    index = k;
                                    distance = Math.abs(elevators.get(k).getCurrentFloor() - i);
                                }
                            }
                            if (elevators.get(k).getCurrentFloor() >= i && elevators.get(k).getCurrentState() == -1
                                    && elevators.get(k).downMin() <= i) {
                                if (Math.abs(elevators.get(k).getCurrentFloor() - i) < distance) {
                                    index = -1;
                                    distance = Math.abs(elevators.get(k).getCurrentFloor() - i);
                                }
                            }
                            if (elevators.get(k).getCurrentFloor() <= i && elevators.get(k).getCurrentState() == 1
                                    && elevators.get(k).upMax() <= i) {
                                if (Math.abs(elevators.get(k).getCurrentFloor() - i) < distance) {
                                    index = -1;
                                    distance = Math.abs(elevators.get(k).getCurrentFloor() - i);
                                }
                            }
                        }
//                        System.out.println(i + ":" + index);
                        if (index != -1 && !queue[i][1].isEmpty()) {
                            try {
                                adjust(index, i);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        init();
        //  initialize color of the elevator( color indicates status of the elevator)
        lightManger lightmanger = new lightManger();
        elevatorManager elevatormanager = new elevatorManager();
        one.start();
        two.start();
        three.start();
        four.start();
        five.start();
    }
}