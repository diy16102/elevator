1. Overview of project requirements
1.1 Project purpose
Although we may take this for granted when using elevators, most modern elevators are driven by software control systems. In the simplest case of a single elevator system, the elevator controller needs to remember the floor requested by the elevator passengers, and stop and open the door when the requested floor is reached. Outside, if someone calls an elevator from the corridor, the control system needs to decide when the elevator should stop on that floor-whether it has finished transporting all passengers or stopped in the middle. In a multi-elevator group, if more than one elevator is currently in use, the control system must select which elevator should respond to the call button.

1.2 Basic needs
Simulate the dispatch of 5 elevators in a 20-story building
The elevator has the most basic buttons
Can display the current status of the elevator
2. Overview of scheduling algorithm
2.1 Overview of passenger behavior
Passengers can press the up or down button of the current floor to request elevators on any floor of the 20th floor
Passengers can press the floor selection button in the elevator to specify where the elevator is going. Due to ui design issues, this function is required to be completed when pressing the request button
Passengers can press the emergency button in the elevator to force the current elevator to stop operation
2.2 Overview of elevator behavior
The initial state of the elevator is stationary and parked on the first floor
The elevator changes its behavior by repeatedly checking its own state variables
The moving elevator will self-check the disembarking queue every time it reaches a floor to determine whether the current floor needs to open the door to disembark passengers
When the moving elevator arrives at a floor, it is necessary to check whether there are passengers in the current floor passenger waiting queue and determine whether the current floor is to carry passengers. If there are no passengers in the elevator on that floor and no other request is answered, then it is loaded. Passengers in the direction of the current floor with more people continue to travel
2.3 Scheduling
The passenger presses the request button to respond to the process
There is going to this floor in the up and down direction and the highest/low request of the elevator is greater than that of the floor: Will wait for the elevator to arrive at the floor to carry the passenger
There is no elevator heading towards this floor in the up and down direction or there is but the highest/low request of the elevator fails to reach the floor: a search will be carried out for the static elevator queue: the selection of the static elevator will give priority to the position and choose the closest to the floor To respond to the request, start the elevator and insert the instruction to stop in the building into the elevator.

                                                                                                                         



















2.3.1 The operating procedure of the moving elevator when it arrives at a certain floor
The elevator retrieves whether there is this floor in its own stop queue
There is this floor: Stop and eject all passengers in the queue from the queue, and eject the floor where the queue is stopped
No such floor: go to the next step
The elevator retrieves the request queue of the current floor
The elevator's current stop queue is empty:
There is no request for the current floor, the elevator sets its own state variable to static
There is a request on the current floor. The elevator chooses a direction with more people to carry passengers, pops them out of the request queue, sets state variables, and then starts traveling in that direction
The elevator's current stop queue is not empty
There is no request on the current floor, the elevator continues to move
There is a request on the current floor. The elevator carries passengers in the corresponding direction, ejects these passengers from the request queue, and continues to travel.

![image](https://user-images.githubusercontent.com/60899705/142128154-e54a3381-79f7-477c-98b9-ff99051917b7.png)





int name; // elevator name
int currentState; // current state variable
int emerState; // emergency state variable
int currentMaxFloor; // The highest floor currently available
int maxUp; // The highest floor the elevator will go to
int minDown; // The lowest floor the current elevator is going to
Queue<Integer> upStopList; // elevator down stop queue
Queue<Integer> downStopList; // elevator ascending stop queue
JButton buttonList; // Button control queue in ui
method

int getCurrentState(); // Get currentState
void setCurrentState(); // set currentState
int getCurrentFloor(); // Get currentFloor
void setCurrentFloor(); // set currentFloor
void popUp(); // Pop up the first element of upStopList
void popDown(); // Pop up the first element of downStopList
void addUp(int pos); // Add the location floor to upStopList
void addDown(int pos); // Add the location floor to downStopList
int upMax(); // get maxUp
void setUpMax(); // set maxUp
int downMin(); // Get minDown
void setDownMin(); // set minDown
void run(); // Start the elevator thread
