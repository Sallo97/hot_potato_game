package gameTests

import backend.game.GameType
import backend.player.BarnumPlayer
import backend.player.Player
import backend.potato.Potato
import kotlin.random.Random
import kotlin.test.Test

class BarnumTest: GameTest() {
    val potato = Potato(lifetime = 5, baseGain = 5.0, baseLoss = 10.0)

    @Test
    fun testBarnum1() {
        // All players have prob = 1, meaning they behave as rational players.
        val players = mutableSetOf<Player>().apply {
            for (i in 1..10) {
                val player = BarnumPlayer(i, prob = 1.0)
                this.add(player)
            }
        }

        doExactGameTest(potato,
            players,
            0,
            0,
            0.0)
    }

    @Test
    fun testBarnum2() {
        // All players have prob = 0, meaning they behave as gullible players.
        val players = mutableSetOf<Player>().apply {
            for (i in 1..10) {
                val player = BarnumPlayer(i, prob = 0.0)
                this.add(player)
            }
        }

        doExactGameTest(potato,
            players,
            4,
            4,
            5.0)
    }

    @Test
    fun testBarnum3() {
        // All players have random prob except one with prob = 1
        val players = mutableSetOf<Player>().apply {
            for (i in 1..4) {
                val randProb = Random.nextDouble(0.0, 1.0 + Double.MIN_VALUE)
                val player = BarnumPlayer(i, randProb)
                this.add(player)
            }
            val player = BarnumPlayer(5, 1.0)
            this.add(player)
        }

        doGEGameTest(potato,
            players,
            GameType.HOMOGENEOUS,
            1,
            1,
        -10,
            true)
    }
}