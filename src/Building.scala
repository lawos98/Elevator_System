package pl.app

import scala.collection.immutable.{HashMap, HashSet}

object Building {
  val elevatorList: List[Elevator] = List.tabulate(ElevatorConfig.numberOfElevators)(id => new Elevator(id,0,Idle, ElevatorConfig.elevatorCapacity, ElevatorConfig.elevatorMaxWeight))
  val floorList: List[Floor] = List.tabulate(ElevatorConfig.numberOfFloors)(number => Floor(number))

  def getFloor(floorNumber: Int): Floor = floorList.find(_.number == floorNumber).getOrElse(throw new IllegalArgumentException("Invalid floor number."))

  def isFloorExist(floorNumber: Int): Boolean = floorList.exists(_.number == floorNumber)

  def requestElevator(floorNum: Int, direction: State): Unit = {
    val elevator = findBestElevator(floorNum,direction)
    elevator.addRequest(floorNum)
  }

  private def findNearestElevator(list:List[Elevator], floorNumber:Int): Elevator = {
    list.minBy(elevator => Math.abs(elevator.currentFloor - floorNumber))
  }

  private def findBestElevator(startFloor: Int, direction: State): Elevator = {
    val availableElevators = elevatorList.filter(_.direction == Idle)
    if(availableElevators.nonEmpty) return findNearestElevator(availableElevators, startFloor)

    val inWayElevators = if(direction == Up) elevatorList.filter(elevator => elevator.direction == Up && elevator.currentFloor < startFloor && elevator.checkConditionToTakeAdditionalPeople())
    else elevatorList.filter(elevator => elevator.direction == Down && elevator.currentFloor > startFloor && elevator.checkConditionToTakeAdditionalPeople())

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