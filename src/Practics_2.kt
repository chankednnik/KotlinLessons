import kotlin.math.max
import kotlin.math.min

// --- БАЗОВЫЙ КЛАСС И НАСЛЕДНИКИ СОБЫТИЙ ---

abstract class GameEvent(val name: String, val description: String) {
    // Переопределяемый метод применения события
    abstract fun apply(player: Player)
}

// 1. Событие с фиксированным эффектом
class FixedEvent(name: String, description: String, private val readinessChange: Int, private val energyChange: Int, private val budgetChange: Int) : GameEvent(name, description) {
    override fun apply(player: Player) {
        player.readiness = max(0, player.readiness + readinessChange)
        player.energy = max(0, player.energy + energyChange)
        player.budget = max(0, player.budget + budgetChange)
        println(">> [СОБЫТИЕ] $name: $description")
        println("   Эффект: Готовность ${formatChange(readinessChange)}, Энергия ${formatChange(energyChange)}, Бюджет ${formatChange(budgetChange)}")
    }
    private fun formatChange(valInt: Int) = if (valInt >= 0) "+$valInt" else "$valInt"
}

// 2. Условное событие (зависит от текущих ресурсов)
class ConditionalEvent(name: String, description: String) : GameEvent(name, description) {
    override fun apply(player: Player) {
        println(">> [СОБЫТИЕ] $name: $description")
        if (player.energy < 3) {
            // Если мало энергии, стресс от события
            player.readiness = max(0, player.readiness - 10)
            println("   Вы истощены! Эффект: Готовность -10 (паника)")
        } else if (player.budget < 20) {
            // Если мало денег, но есть силы — помогают соседи
            player.readiness += 10
            player.energy -= 2
            println("   Соседняя деревня помогла! Эффект: Готовность +10, Энергия -2")
        } else {
            // Штатный эффект при нормальных ресурсах
            player.budget -= 15
            player.readiness += 5
            println("   Пришлось заплатить. Эффект: Готовность +5, Бюджет -15")
        }
    }
}

// --- КЛАСС ИГРОКА ---

class Player {
    var budget: Int = 100
    var energy: Int = 10
    var readiness: Int = 0
    var consecutiveHireDays: Int = 0 // Для особого правила

    fun isDead(): Boolean = budget <= 0 || energy <= 0
    fun isVictorious(): Boolean = readiness >= 100

    fun changeStats(readinessChange: Int, energyChange: Int, budgetChange: Int) {
        readiness = max(0, readiness + readinessChange)
        energy = max(0, energy + energyChange)
        budget = max(0, budget + budgetChange)
    }
}

// --- ОСНОВНОЙ КЛАСС ИГРЫ ---

class Game(private val theme: String) {
    private val player = Player()
    private var currentDay = 1
    private val maxDays = 7
    private val history = mutableListOf<DayRecord>()

    // Коллекция событий
    private val eventPool = listOf(
        FixedEvent("Дождь", "Грязь замедлила работу.", -5, 0, 0),
        FixedEvent("Находка", "Крестьяне нашли старый склад с инструментами.", 10, 0, 0),
        FixedEvent("Эпидемия", "Болезнь скосила часть работников.", 0, -2, 0),
        FixedEvent("Торговец", "Приезжий купец дал скидку на материалы.", 5, 0, 10),
        ConditionalEvent("Налет бандитов", "Бандиты требуют дань. Откупиться или отбиваться?"),
        ConditionalEvent("Проверка старейшин", "Старейшины проверяют вашу готовность."),
        ConditionalEvent("Голод", "Запасы еды подходят к концу."),
        FixedEvent("Удачная охота", "Охотники принесли много дичи.", 0, 3, 0)
    )

    data class DayRecord(
        val day: Int,
        val action: String,
        val eventName: String,
        val readinessChange: Int,
        val energyChange: Int,
        val budgetChange: Int
    )

    fun start() {
        println("=== ИГРА: 7 ДНЕЙ ДО $theme ===")
        println("Цель: набрать 100 очков готовности за 7 дней.")
        println("Ресурсы: Бюджет: 100, Энергия: 10, Готовность: 0")
        println("Внимание: Бездумное повторение одного действия приведет к краху!\n")

        while (currentDay <= maxDays) {
            if (player.isDead()) {
                loseGame("Вы истощены. Ресурсы на нуле.")
                return
            }
            if (player.isVictorious()) {
                winGame()
                return
            }

            println("--- ДЕНЬ $currentDay ---")
            println("Статус -> Бюджет: ${player.budget} | Энергия: ${player.energy} | Готовность: ${player.readiness}")

            val actionName = chooseAction()
            processAction(actionName)

            if (player.isDead()) {
                loseGame("Вы не выдержали нагрузки. Игра окончена.")
                return
            }

            triggerEvent(actionName)
            currentDay++
        }

        if (player.isVictorious()) winGame() else loseGame("Время вышло! Вы не успели подготовиться.")
    }

    private fun chooseAction(): String {
        var validChoice = false
        var choice = ""

        while (!validChoice) {
            println("Выберите действие:")
            println("1. Строить укрепления (+15 готовности, -3 энергии)")
            println("2. Нанимать наемников (+20 готовности, -25 бюджета)")
            println("3. Устроить пир (+4 энергии, день пропадает)")
            print("Ваш выбор (1-3): ")

            when (readlnOrNull()?.trim()) {
                "1" -> {
                    if (player.energy < 3) println("Недостаточно энергии!")
                    else { choice = "Строить"; validChoice = true }
                }
                "2" -> {
                    if (player.budget < 25) println("Недостаточно бюджета!")
                    else { choice = "Нанимать"; validChoice = true }
                }
                "3" -> { choice = "Отдыхать"; validChoice = true }
                else -> println("Неверный ввод. Попробуйте снова.")
            }
        }
        return choice
    }

    private fun processAction(action: String) {
        var rChange = 0; var eChange = 0; var bChange = 0

        when (action) {
            "Строить" -> {
                rChange = 15; eChange = -3
                player.consecutiveHireDays = 0
                println("Вы весь день таскали бревна.")
            }
            "Нанимать" -> {
                rChange = 20; bChange = -25
                player.consecutiveHireDays++
                println("Вы наняли отряд наемников.")

                // ОСОБОЕ ПРАВИЛО
                if (player.consecutiveHireDays == 3) {
                    println("!!! ОСОБОЕ ПРАВИЛО: Боги разгневаны вашей жадностью! -10 энергии.")
                    eChange -= 10
                }
            }
            "Отдыхать" -> {
                eChange = 4
                player.consecutiveHireDays = 0
                println("Вы устроили пир и набрались сил.")
            }
        }

        player.changeStats(rChange, eChange, bChange)
    }

    private fun triggerEvent(action: String) {
        // Фильтрация событий: не даем выпасть одному и тому же событию дважды подряд (опционально)
        val event = eventPool.random()

        // Запоминаем статы до события для истории
        val preR = player.readiness; val preE = player.energy; val preB = player.budget

        event.apply(player)

        // Запись в историю
        val record = DayRecord(
            currentDay, action, event.name,
            player.readiness - preR, player.energy - preE, player.budget - preB
        )
        history.add(record)
    }

    private fun showHistory() {
        println("\n=== ИСТОРИЯ ПРОХОЖДЕНИЯ ===")
        history.forEach {
            println("День ${it.day} | Действие: ${it.action} | Событие: ${it.eventName}")
            println("   Изменения: Готовность ${if(it.readinessChange >= 0) "+" else ""}${it.readinessChange}, Энергия ${if(it.energyChange >= 0) "+" else ""}${it.energyChange}, Бюджет ${if(it.budgetChange >= 0) "+" else ""}${it.budgetChange}")
        }

        // Подсчет лучшего и худшего дня по изменению готовности
        val bestDay = history.maxByOrNull { it.readinessChange }
        val worstDay = history.minByOrNull { it.readinessChange }

        val totalEnergySpent = history.sumOf { if (it.energyChange < 0) -it.energyChange else 0 }
        val totalBudgetSpent = history.sumOf { if (it.budgetChange < 0) -it.budgetChange else 0 }

        println("\n--- СТАТИСТИКА ---")
        println("Лучший день по готовности: День ${bestDay?.day} (+${bestDay?.readinessChange})")
        println("Худший день по готовности: День ${worstDay?.day} (${worstDay?.readinessChange})")
        println("Всего потрачено энергии: $totalEnergySpent")
        println("Всего потрачено бюджета: $totalBudgetSpent")
    }

    private fun winGame() {
        println("\n🎉 ПОБЕДА! Вы набрали ${player.readiness} очков готовности.")
        println("Враги увидели ваши укрепления и отступили!")
        showHistory()
    }

    private fun loseGame(reason: String) {
        println("\n💀 ПОРАЖЕНИЕ. $reason")
        showHistory()
    }
}

fun main() {
    val game = Game("НАШЕСТВИЯ ВАРВАРОВ")
    game.start()
}