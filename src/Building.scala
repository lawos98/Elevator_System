package pl.app

import scala.collection.immutable.{HashMap, HashSet}

object Building {
  private val elevatorList: List[Elevator] = List.tabulate(ElevatorConfig.numberOfElevators)(id => new Elevator(id,0,Idle, ElevatorConfig.elevatorCapacity, ElevatorConfig.elevatorMaxWeight))
  private val floorList: List[Floor] = List.tabulate(ElevatorConfig.numberOfFloors)(number => Floor(number))

  def getFloorList: List[Floor] = floorList
  def getFloor(floorNumber: Int): Floor = floorList.find(_.number == floorNumber).getOrElse(throw new IllegalArgumentException("Invalid floor number."))
  
  def requestElevator(floorNum: Int, state: State): Unit = {
    val elevator = findBestElevator(floorNum,state)
    elevator.addRequest(floorNum)
  }

  private def findNearestElevator(list:List[Elevator], floorNumber:Int): Elevator = {
    list.minBy(elevator => Math.abs(elevator.currentFloor - floorNumber))
  }

  private def findBestElevator(startFloor: Int, state: State): Elevator = {
    val availableElevators = elevatorList.filter(_.state == Idle)
    if(availableElevators.nonEmpty) return findNearestElevator(availableElevators, startFloor)

    val inWayElevators = if(state == Up) elevatorList.filter(elevator => elevator.state == Up && elevator.currentFloor < startFloor && elevator.checkConditionToTakeAdditionalPeople())
    else elevatorList.filter(elevator => elevator.state == Down && elevator.currentFloor > startFloor && elevator.checkConditionToTakeAdditionalPeople())

    if(inWayElevators.nonEmpty) return findNearestElevator(inWayElevators, startFloor)

    findNearestElevator(elevatorList,startFloor)
  }

  def step():Unit = {
    elevatorList.foreach(_.step())
    floorList.foreach(_.step())
    floorList.head.spawnPerson()
    Statistics.printStatistics()
  }

}