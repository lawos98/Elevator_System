package pl.app

import scala.collection.immutable.{HashSet, Queue}
import scala.collection.mutable
import scala.util.Random

case class Floor(number: Int) {
  private var waitingList = List[Person]()
  private var peopleOnFloorList = List[Person]()

  private def addPersonToFloor(person: Person): Unit = {
    peopleOnFloorList :+= person
  }

  def getPeopleNotTaken(direction: State): List[Person] = {
    waitingList.filter(person => direction match {
      case Up => person.destinationFloor > number
      case Down => person.destinationFloor < number
    })
  }

  def selectPassengers(elevator: Elevator): List[Person] = {
    val (selected , waiting , _ , _ ) = waitingList.foldLeft((List.empty[Person], List.empty[Person], elevator.currentCapacity, elevator.currentWeight)) {
      case ((selected, waiting, currCapacity, currWeight), person) =>
        val newCapacity = currCapacity + 1
        val newWeight = currWeight + person.weight
        if (newCapacity <= elevator.maxCapacity && newWeight <= elevator.maxWeight) {
          (selected :+ person, waiting, newCapacity, newWeight)
        } else {
          (selected, waiting :+ person, currCapacity, currWeight)
        }
    }
    waitingList = waiting
    selected
  }

  def spawnPerson():Unit = {
    val person = Person.generateRandomPerson()
    addPersonToFloor(person)
    ColorText.printBlue(s"Person ${person} spawned on floor $number")
  }

  def step(): Unit = {
    peopleOnFloorList.foreach(person => {
      if (Random.nextDouble() < ElevatorConfig.chanceForPersonToUseElevator) {
        peopleOnFloorList = peopleOnFloorList.filter(_ != person)
        var changedPerson = person.changeDestinationFloor()
        while (changedPerson.destinationFloor == number) changedPerson=changedPerson.changeDestinationFloor()
        waitingList :+= changedPerson
        ColorText.printBlue(s"Person ${person} changed destination floor to ${changedPerson.destinationFloor}")
        Building.requestElevator(number,defineDirection(number,changedPerson.destinationFloor))
      }
    })
    waitingList.foreach(_.addTimeToEnterElevator())
  }

  private def defineDirection(startFloor: Int, destinationFloor: Int): State = {
    if (startFloor < destinationFloor) Up
    else if (startFloor > destinationFloor) Down
    else Idle
  }
}