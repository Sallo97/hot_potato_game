package org.example.backend.gameStatus

import org.example.backend.player.Player

/**
 * Contains useful information regarding the status of a SHPG
 * @property [activePopulation] the non-ordered subset of players which at the current turn never got the good.
 * @property [chain] the ordered set of players which at the current turn already got the good.
 * @property [turn] the current turn of the game.
 * @property [numOfPlayers] the total number of players in the game.
 * @property [totalPayoff] sum of the payoff of all players at the current turn.
 * @property [gameEnded] tells if the game ended or not.
 *
 */
data class GameStatus(
    val activePopulation: MutableSet<Player>,
) {
    val chain: MutableList<Player> = mutableListOf()
    var turn: Int = 0
    val numOfPlayers: Int = activePopulation.size
    var totalPayoff: Double = 0.0
    var gameEnded = false

    /**
     * updates the game status after a turn has been played.
     */
    fun updateStatus(p: Player) {
        turn++
        chain.add(p)
        activePopulation.remove(p)
    }

    fun chainToString() :String {
        var str = "Chain of players that partake in the game:"

        if (chain.isEmpty()) {
            str += "∅"
        } else {
            for (p in chain) {
                str += "$p "
            }
        }

        return str
    }
}