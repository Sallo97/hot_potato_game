package org.example.frontend

import org.example.backend.game.Game
import org.example.backend.player.BarnumPlayer
import org.example.backend.player.BenthamitePlayer
import org.example.backend.player.CoalitionalPlayer
import org.example.backend.player.DirectAltruistPlayer
import org.example.backend.player.GulliblePlayer
import org.example.backend.player.InputPlayer
import org.example.backend.player.MyopicPlayer
import org.example.backend.player.Player
import org.example.backend.player.RationalPlayer
import org.example.backend.player.StochasticPlayer
import org.example.backend.potato.Potato
import kotlin.math.absoluteValue

const val invalidInputMsg = "Invalid input, retrying..."

/**
 * @return a Game Object with parameters coming from user's input
 */
fun createGameFromStdin() : Game {
    println("Creating game...")
    val potato = createHotPotatoFromStdin()
    val isHomogeneous = gameTypeFromStdin()

    val numOfPlayers = absIntFromStdin("number of players")
    val set = createSetOfPlayersFromStdin(numOfPlayers, isHomogeneous)

    val game = Game(potato, set)
    return game
}

/**
 * @param [question] the question asked the user for which it needs to respond.
 * @return true if the user choose "y", false if it chooses "n".
 */
fun choiceFromStdin(question: String) : Boolean {
    while(true) {
        print("$question Press Y/y for Yes, N/n for No: ")
        val choice = readln().lowercase()
        if (choice == "n" || choice == "y") {
            return choice == "y"
        }
        println(invalidInputMsg)
    }
}

/**
 * @param [name] the name of the argument requested to Stdin.
 * @return a correct input from stdin parsed as an absolute integer
 */
fun absIntFromStdin(name: String) : Int {
    var input: Int?
    while(true) {
        print("Input $name (will take the absolute value): ")
        input = readln().toIntOrNull()
        if(input != null) {
            return input.absoluteValue
        }
        println(invalidInputMsg)
    }
}

/**
 * @return a string describing all the available types of player.
 */
val getTypesOfPlayerAvailable by lazy {
    val str = "1 - Rational: never takes the hot potato\n" +
            "2 - Gullible: always takes the hot potato\n" +
            "3 - Barnum: is aware of the possibility of irrational actors among the population, thus it could consider taking the hot potato depending on them.\n" +
            "4 - Myopic: is not able to see the end to the game if its length surpasses its reasoning capability. If it sees the game as infinite, it will act rationally, otherwise will accept the good.\n" +
            "5 - Stochastic: interprets the decision of taking the hot potato as a static stochastic process, independent of other players.\n" +
            "6 - Direct Altruist: concerned with the well-being of another player. For sake of simplicity we assume the beneficiary is always another player that did not get the hot potato previously.\n" +
            "7 - Benthamite: focused on maximizing the total payoff of the game, rather than its own. Still, if it knows that other players could take the risk of accepting the potato, it could deny the proposal.\n" +
            "8 - Coalitional: concerned in forming a coalition with other players of the same type in order to get a better payoff.\n" +
            "9 - Input: the decision is provided by the user from Stdin"
    str
}

/**
 * @param [name] the name of the argument requested to Stdin.
 * @param [range] the range of integers in which the value must be inbetween (included)
 * @return a correct input from stdin parsed as an absolute integer.
 */
fun absIntWithinRangeFromStdin(name: String, range: IntRange) : Int {
    while(true){
        val input = absIntFromStdin(name)
        if(input in range) {
            return input
        } else {
            println(invalidInputMsg)
        }
    }
}
/**
 * @param [name] the name of the argument requested to Stdin.
 * @return a correct input from stdin parsed as an absolute double.
 */
private fun absDoubleFromStdin(name:String) : Double {
    while(true) {
        print("Input $name (will take the absolute value): ")
        val input = readln().toDoubleOrNull()
        if(input != null) {
            return input.absoluteValue
        } else {
            println(invalidInputMsg)
        }
    }
}

/**
 * @param [name] the name of the argument requested to Stdin.
 * @param [range] the range of integers in which the value must be inbetween (included)
 * @return a correct input from stdin parsed as an absolute double
 */
private fun absDoubleWithinRangeFromStdin(name: String, range: IntRange) : Double {
    var input: Double?
    while(true) {
        input = absDoubleFromStdin(name)
        if(input in range.start.toDouble()..range.endInclusive.toDouble()) {
            return input.absoluteValue
        }
        println(invalidInputMsg)
    }
}

/**
 * @return true if the user asked for a Homogenous game, false otherwise.
 */
private fun gameTypeFromStdin() : Boolean{
    println("Select type of game:")
    println("1 - Homogeneous: game where players are all of the same type")
    println("2 - Mixed: game where players can be of different types")

    val choice = absIntWithinRangeFromStdin("type of game", 1..2)
    val result = choice == 1
    return result
}


/**
 * @return a set of [numOfPlayers] players. If [isHomogeneous] is passed, then the set will have only player of the same
 * type, otherwise each player could have different type.
 */
private fun createSetOfPlayersFromStdin(numOfPlayers: Int, isHomogeneous: Boolean = false) : Set<Player> {
    // println("Creating set of players...")

    var type: Int? = if(isHomogeneous) {
        getPlayerTypeFromStdin()
    } else {
        null
    }

    val result:MutableSet<Player> = mutableSetOf()
    for (id in 1..numOfPlayers) {
        if(!isHomogeneous) {
            type = getPlayerTypeFromStdin()
        }
        val player = createPlayerOfTypeFromStdin(type!!, id)
        result.add(player)
    }
    return result
}

/**
 * @param [id] the unique identifier of the player.
 * @return a player with arguments determined by the user from stdin.
 */
fun createPlayerFromStdin (id:Int = 0) : Player {
    val type = getPlayerTypeFromStdin()
    val player = createPlayerOfTypeFromStdin(type, id)
    return player
}

/**
 * @return player object of specified [type] and [id] with parameters coming from user's input.
 */
private fun createPlayerOfTypeFromStdin(type: Int, id:Int = 0) : Player {
    val player: Player = when(type) {
        1 -> {
            RationalPlayer(id)
        }
        2 -> {
            GulliblePlayer(id)
        }
        3 -> {
            val message = "the probability that other alters are Barnum players"
            val prob = absDoubleWithinRangeFromStdin(message, IntRange(0, 1))

            BarnumPlayer(id, prob)
        }
        4 -> {
            val threshold = absIntFromStdin("threshold")

            MyopicPlayer(id, threshold)
        }
        5 -> {
            val message = "weight, an hyperparameter between 0 and 1 which determines" +
                    " how much weight the probability have."
            val weight = absDoubleWithinRangeFromStdin(message, IntRange(0, 1))

            StochasticPlayer(id, weight)
        }
        6 -> {
            val altruismMessage = "altruism, value between [0,1] representing how much the player is willing to help the beneficiary."
            val altruism = absDoubleWithinRangeFromStdin(altruismMessage, IntRange(0, 1))

            val helpAlterMessage = "the belief of the current player that an alter will help the same beneficiary. Is a value\n" +
                    " * in [0,1]."
            val helpAlterBelief = absDoubleWithinRangeFromStdin(helpAlterMessage, IntRange(0, 1))

            DirectAltruistPlayer(id, altruism, helpAlterBelief)
        }
        7 -> {
            val alterMessage = "the belief of the current player that an alter will accept the potato at the current turn. Is a value in [0,1]"
            val alterAcceptBelief = absDoubleWithinRangeFromStdin(alterMessage, IntRange(0, 1))

            BenthamitePlayer(id, alterAcceptBelief)
        }
        8 -> {
            val riskMessage = "how much the current players is willing to risk accepting the potato for the proposed coalition. Is a value between 0 and 1."
            val acceptanceToRisk = absDoubleWithinRangeFromStdin(riskMessage, IntRange(0, 1))

            CoalitionalPlayer(id, acceptanceToRisk)
        }
        9 -> {
            InputPlayer(id)
        }

        else -> {
            error("Invalid type for player")
        }
    }
    // println("Created the player: $player")
    return player
}

/**
 * @return the player's type as an integer
 */
private fun getPlayerTypeFromStdin() : Int {
    val typeOfPlayerMsg = "Select which type of player do you want between:\n$getTypesOfPlayerAvailable"
    println(typeOfPlayerMsg)

    val type: Int = absIntWithinRangeFromStdin("player type", 1..typeOfPlayerMsg.lines().size)
    return type
}

/**
 * @return a Potato object with parameters coming from user's input.
 */
private fun createHotPotatoFromStdin() : Potato{
    println("Creating the hot potato...")
    val lifetime = absIntFromStdin("lifetime")
    val gain = absDoubleFromStdin("gain")
    val loss = absDoubleFromStdin("loss")
    val potato = Potato(lifetime, gain, loss)
    println("Created the potato: $potato")
    return potato
}
