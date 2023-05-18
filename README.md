# Elevator System Documentation

The Elevator System is a simulation of multiple elevators operating in a building. It provides functionality for handling elevator movements, floor requests, and passenger interactions. This documentation provides an overview of the system architecture, usage instructions, and details about the LOOK Disk Scheduling Algorithm used for elevator movement optimization.

## Architecture
The Elevator System consists of the following key components:

* **Elevator**: Represents an elevator with a unique identifier, current floor, direction, maximum capacity, and maximum weight. It handles passenger boarding, disembarking, and movement between floors.

* **Floor**: Represents a floor in the building. It maintains a list of people waiting on the floor, handles spawning new people, and selects passengers to board the elevator based on their destination floors.

* **Building**: The central orchestrator of the elevator system. It manages the elevators, floors, and facilitates communication between them. It handles elevator requests from floors, determines the best elevator to serve a request using the LOOK Disk Scheduling Algorithm, and advances the simulation by stepping the elevators and floors forward in time.

## Implementation

In the elevator system code, the LOOK algorithm is applied in the Building.findBestElevator(startFloor, direction) method. This method is responsible for selecting the most suitable elevator to respond to a floor request.

1. If there are any idle elevators (elevators that are not currently moving), the algorithm selects the nearest idle elevator to the requested floor.
2. If there are no idle elevators, the algorithm checks for elevators that are already in motion and have additional capacity to accommodate more passengers. It considers only those elevators that are moving in the same direction as the requested floor. Among these elevators, it selects the nearest one to the requested floor.
3. If there are nosuitable elevators found in the previous step, the algorithm falls back to selecting the nearest elevator overall, regardless of its direction or capacity.

The LOOK algorithm, in conjunction with additional criteria such as elevator capacity and weight restrictions, helps optimize the elevator selection process and minimizes the waiting time for passengers.

## Advantages

The LOOK Disk Scheduling Algorithm offers several advantages in the elevator system:

* Improved Efficiency: By scanning pending requests in the direction of travel and changing direction only when necessary, the algorithm reduces unnecessary elevator movement, resulting in improved efficiency and reduced seek time.

* Optimal Resource Utilization: The algorithm considers both idle and moving elevators to select the most suitable one for a floor request. This optimal resource utilization ensures that elevators are efficiently utilized across the building.

* Fairness: By selecting the nearest elevator overall when no suitable options are found in the current direction, the algorithm ensures fairness in serving different floors throughout the building.