package pl.app

import scala.collection.immutable.HashSet

class Elevator(val id: Int, var currentFloor: Int, var state:State, val maxCapacity: Int, val maxWeight: Int) {
  private var passengerList: List[Person] = Nil
  private var requestList: List[Int] = Nil

  def addRequest(floor: Int): Unit = {
    requestList = requestList :+ floor
  }

  private def takeOutPeople(): Unit = {
    val passengersToTakeOut = passengerList.filter(_.destinationFloor == currentFloor)
    passengersToTakeOut.foreach(person => {
      ColorText.printBlue(s"Person ${person.id} left elevator $id on floor $currentFloor")
      Statistics.addWaitingTimeInElevator(person.getTimeInElevator)
    })
    passengerList = passengerList.filter(_.destinationFloor != currentFloor)
    requestList = requestList.filter(_ != currentFloor)
  }

  private def takeInPeople():Unit = {
    val newPassengers = Building.getFloor(currentFloor).selectPassengers(this)
    newPassengers.foreach(person => {
      ColorText.printBlue(s"Person ${person.id} entered elevator $id on floor $currentFloor")
      Statistics.addWaitingTimeForElevator(person.getTimeForElevator)
    })
    passengerList ++= newPassengers
    if(Building.getFloor(currentFloor).getPeopleNotTaken(state).nonEmpty) Building.requestElevator(currentFloor,state)
  }

  def step(): Unit = {
    ColorText.printMagenta(s"Elevator $id is on floor $currentFloor and has direction $state")
    if(passengerList.exists(_.destinationFloor == currentFloor) || checkConditionToTakeAdditionalPeople()){
      takeOutPeople()
      takeInPeople()
    }

    val floorsAboveExist = Building.getFloorList.exists { case (floor) =>
      floor.number > currentFloor && (requestList.exists(_>currentFloor) || passengerList.exists(_.destinationFloor > currentFloor))
    }

    val floorsBelowExist = Building.getFloorList.exists { case (floor) =>
      floor.number < currentFloor && (requestList.exists(_<currentFloor) || passengerList.exists(_.destinationFloor < currentFloor))
    }

    passengerList.foreach(_.addTimeInElevator())

    state = (floorsAboveExist, floorsBelowExist, state) match {
      case (false, false, _) => Idle
      case (false, true, Up) => Down
      case (true, false, Down) => Up
      case (true, false, Idle) => Up
      case (false, true, Idle) => Down
      case _ => state
    }
    if (state == Up) {
      currentFloor += 1
      Statistics.addMoveUp(currentWeight)
    }
    else if (state == Down) {
      currentFloor -= 1
      Statistics.addMoveDown(currentWeight)
    }
  }

  def currentWeight: Int = {
    passengerList.foldLeft(0)(_ + _.weight)
  }

  def currentCapacity: Int = {
    passengerList.length
  }
  def checkConditionToTakeAdditionalPeople():Boolean = {
    currentCapacity/maxCapacity.toDouble < ElevatorConfig.maxPercentageOfPeople && currentWeight/maxWeight.toDouble < ElevatorConfig.maxPercentageOfWeight
  }
}