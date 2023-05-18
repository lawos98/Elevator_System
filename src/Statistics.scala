package pl.app

object Statistics {
  private var weightMoveDown: List[Int] = Nil
  private var weightMoveUp: List[Int] = Nil
  private var waitingTimesForElevator: List[Int] = Nil
  private var waitingTimesInElevator: List[Int] = Nil

  def addMoveDown(weight: Int):Unit = {
    weightMoveDown = weightMoveDown :+ weight
  }

  def addMoveUp(weight: Int):Unit = {
    weightMoveUp = weightMoveUp :+ weight
  }

  def addWaitingTimeForElevator(time: Int):Unit = {
    waitingTimesForElevator = waitingTimesForElevator :+ time
  }

  def addWaitingTimeInElevator(time: Int):Unit = {
    waitingTimesInElevator = waitingTimesInElevator :+ time
  }

  private def getWaitingTimeForElevator():Int = {
    if(waitingTimesForElevator.isEmpty) 0
    else waitingTimesForElevator.sum/waitingTimesForElevator.length
  }

  private def getWaitingTimeInElevator():Int = {
    if(waitingTimesInElevator.isEmpty) 0
    else waitingTimesInElevator.sum/waitingTimesInElevator.length
  }

  def printStatistics():Unit = {
    ColorText.printYellow("===============================================")
    ColorText.printYellow("Moves down: " + weightMoveDown.length)
    ColorText.printYellow("Moves up: " + weightMoveUp.length)
    ColorText.printYellow("Average waiting time for elevator: " + getWaitingTimeForElevator())
    ColorText.printYellow("Average waiting time in elevator: " + getWaitingTimeInElevator())
    ColorText.printYellow("Total Energy Used:" + ElevatorConfig.energyUpFactor*weightMoveUp.sum + ElevatorConfig.energyDownFactor*weightMoveDown.sum)
    ColorText.printYellow("===============================================")
  }
}
