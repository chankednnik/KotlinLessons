import kotlin.math.max

class Player_NEW() {
    var budget: Int = 1500
    var mood: Int = 100
    var readiness: Int = 0
    fun isDead(): Boolean = budget <= 0 || mood <= 10
    fun isVictorious(): Boolean = readiness >= 100

    fun changeStatus(readinessChange: Int, energyChange: Int, budgetChange: Int) {
        readiness = max(0, readiness + readinessChange)
        mood = max(0, mood + energyChange)
        budget = max(0, budget + budgetChange)
    }
}
class Game_NEW(private var theme: String) {
    private val player = Player_NEW()
    private var currentDay = 1
    private val maxDay = 7

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

