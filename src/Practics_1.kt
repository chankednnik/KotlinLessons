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
    fun start() {
        println("ИГРА 7 ДНЕЙ ДО $theme")
        println("Цель: набрать >= 100 очков готовности")
        println("У тебя есть 1500 рублей бюджета, 100 очков настроения, 0 очков готовности")
        println("Бездумное выполнение одного действия приведет к ошибкам!")


    }
}
fun main() {
}

