package gameTests

import backend.player.Player
import backend.player.StochasticPlayer
import backend.potato.Potato
import kotlin.test.Test

class StochasticTest : GameTest() {
    val potato = Potato(10, 100.0, 200.0)


    @Test
    fun testStochastic1() {
        // All stochastic players have equal weight which is 0 (thus all will accept the potato)
        val weight = 0.0
        val players = mutableSetOf<Player>().apply {
            for(i in 1..5) {
                val player = StochasticPlayer(i, weight)
                this.add(player)
            }
        }
        doExactGameTest(potato,
            players,
            4,
            4,
            100.0)
    }

    @Test
    fun testStochastic2() {
        // All stochastic players have equal weight which is 1 (thus all will deny the potato)
        val weight = 1.0
        val players = mutableSetOf<Player>().apply {
            for(i in 1..5) {
                val player = StochasticPlayer(i, weight)
                this.add(player)
            }
        }
        doExactGameTest(potato,
            players,
            0,
            0,
            0.0)
    }
}