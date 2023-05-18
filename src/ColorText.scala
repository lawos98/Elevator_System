package pl.app

object ColorText{
  private val ANSI_RESET = "\u001B[0m"
  private val ANSI_MAGENTA = "\u001B[35m"
  private val ANSI_YELLOW = "\u001B[33m"
  private val ANSI_BLUE = "\u001B[36m"

  def printMagenta(text: String): Unit = {
    println(ANSI_MAGENTA + text + ANSI_RESET)
  }
  
  def printYellow(text: String): Unit = {
    println(ANSI_YELLOW + text + ANSI_RESET)
  }

  def printBlue(text: String): Unit = {
    println(ANSI_BLUE + text + ANSI_RESET)
  }
}
