import kotlin.math.max



abstract class gameEvent(val name: String, val description: String) {
    abstract fun apply(player: Player_NEW)
}

class fixedEvents(name: String, description: String, private val readinessChange: Int, private val moodChange: Int, private val budgetChange: Int): gameEvent(name, description) {
    override fun apply(player: Player_NEW) {
        player.readiness = max(0, player.readiness + readinessChange)
        player.mood = max(0, player.mood + moodChange)
        player.budget = max(0, player.budget + budgetChange)
        println("СОБЫТИЕ: $name -> $description")
        println("Как отражается -> Готовность ${formatChange(readinessChange)}, Настроение ${formatChange(moodChange)}, Бюджет ${formatChange(budgetChange)}")
    }
    private fun formatChange(valInt: Int) = if (valInt >= 0) "+${valInt}" else "$valInt"
}

class conditionalEvents(name: String, description: String, private val readinessChange: Int, private val moodChange: Int, private val budgetChange: Int): gameEvent(name, description) {
    override fun apply(player: Player_NEW) {
        println("СОБЫТИЕ: $name -> $description")
        if (player.mood < 10) {
            player.readiness = max(0, player.readiness - 5)
            println("Слишком маленькое настроение -> готовность упала на 5 ед.")
        } else if (player.budget < 1000) {
            player.mood += 10
            player.readiness += 10
            println("Денег стало мало, папа тебе помог. Эффект Готовность +10ед., Настроение +10ед.")
        }
        else {
            player.budget -= 500
            player.readiness += 10
            println("Уплата за готовность пришла")
        }
    }
}
class Player_NEW() {
    var budget: Int = 1500
    var mood: Int = 100
    var readiness: Int = 0
    fun isDead(): Boolean = budget <= 0 || mood <= 10
    fun isVictorious(): Boolean = readiness >= 100

    fun changeStatus(readinessChange: Int, moodChange: Int, budgetChange: Int) {
        readiness = max(0, readiness + readinessChange)
        mood = max(0, mood + moodChange)
        budget = max(0, budget + budgetChange)
    }
}
class Game_NEW(private var theme: String) {
    private val player = Player_NEW()
    private var currentDay = 1
    private val maxDay = 7
    private val history = mutableListOf<DayRecord>()

    private val eventPool = listOf(

    )

    data class DayRecord(
        val day: Int,
        val action: String,
        val eventName: String,
        val readinessChange: Int,
        val moodChange: Int,
        val budgetChange: Int
    )

    fun start() {
        println("ИГРА 7 ДНЕЙ ДО $theme")
        println("Цель: набрать >= 100 очков готовности")
        println("У тебя есть 1500 рублей бюджета, 100 очков настроения, 0 очков готовности")
        println("Бездумное выполнение одного действия приведет к ошибкам!")
        while (currentDay <= maxDay) {
            if (player.isDead()) {
                loseGame("Либо у тебя закончились деньги, либо настроение <= 10")
                return
            }
            if (player.isVictorious()) {
                winGame()
                return
            }

            println("День $currentDay")
            println("Состояние -> Бюджет - ${player.budget}, Настроение - ${player.mood}, Готовность - ${player.readiness}")

//            val actionName = chooseAction()
//            processAction(actionName)
            if (player.isDead()) {
                loseGame("Истощен, игра окончена")
                return
            }

//            triggerEvent(actionName)
            currentDay++
        }
        if (player.isVictorious()) winGame() else loseGame("Время вышло! Вы не успели подготовиться.")
        }
    private fun chooseAction(): String {

    }
    private fun showHistory() {
        println("История прохождения")
        history.forEach {
            println("День ${it.day} Действие ${it.action} Событие ${it.eventName}")
            println("Изменения --> Готовность ${if(it.readinessChange>=0) "+" else ""}${it.readinessChange}, Настроение ${if(it.moodChange>=0) "+" else ""}${it.moodChange}, Бюджет ${if(it.readinessChange>=0) "+" else ""}${it.readinessChange}")
        }

        val bestDay = history.maxByOrNull { it.readinessChange }
        val worstDay = history.minByOrNull { it.readinessChange }

        val totalMoodSpent = history.sumOf { if (it.moodChange < 0) -it.moodChange else 0 }
        val totalBudgetSpent = history.sumOf { if (it.budgetChange < 0) -it.budgetChange else 0 }

        println("Статистика........")
        println("Лучший день по готовности -> День ${bestDay?.day} (+${bestDay?.readinessChange})")
        println("Худший день по готовности -> День ${worstDay?.day} (${worstDay?.readinessChange})")
        println("Всего потрачено энергии -> $totalMoodSpent")
        println("Всего потрачено бюджета -> $totalBudgetSpent")

    }
    private fun winGame() {
        println("Это победа, вы смогли провести день рождения и собрали ${player.readiness} очков готовности")
        if (player.readiness == 100) {
            println("Ваш день рождения прошел нормально, все базово погуляли") }
        if (player.readiness > 100 && player.readiness <= 110) {
            println("Ваш день рождения прошел на среднем уровне, все гости остались довольными и подарили копеечку, что окупило половину днюхи")
        }
        if (player.readiness > 110) {
            println("Денб рождения прошел великолепно, все гости были в мясо и подарили столько денег, что все растраты окупились")
        }
//        showHistory()
    }
    private fun loseGame(reason: String) {
        println("ПОРАЖЕНИЕ. ПРИЧИНА -- $reason")
        //showHistory()
    }
}
fun main() {
    val game = Game_NEW("День родение")
    game.start()
}

