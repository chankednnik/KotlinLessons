//Класс как структура определяет то какими свойствами и методами должны обладать все его потомки
//Метод внутри класса; Функция отдельно
class Car1 {
    //Переменные внутри класса это его свойства
    var wheel: Int = 4
    var stearingWheel = true
    var brand = "Lada"
}
//Пример класса с конструктором
class Car(val brand: String, val wheel: Int = 4, val power: Double) {
    //Метод - функция внутри класса
    //Блок init выполняется один раз при создании экземпляра класса
    init {
        //Поле newCar будет доступно только внутри блока init
        //и обратиться к нему из объекта класса будет невозможно
        val new = true
    }
    fun ride() {
        println("Ride ${brand}")
    }
    override fun toString(): String {
        return "brand: ${brand}, wheel: $wheel, power: $power"
    }
}

//Класс с демонстрацией принципа инкапсуляции
class Person(public var name: String, private var age: Int) {
    //Метод гатер для доступа к значениям свойства
    fun getAge(): Int {return age}
    //Метод сатер для изменения значения свойства
    fun setAge(age: Int) {
        //this - ключевое слово для ЯВНОГО обращения к свойству класса
        this.age = age
    }

    //Более простой способ объясвления сетера и гетера в котлин
    var lastName: String = "Дерден"
        private set
}

//class Animal(val name: String, val age: Int, var type: String) {
//    open fun sound() {}
//}
//
//class Dog(name: String, age: Int, type: String) : Animal(name, age, type) {
//    override fun sound() {
//        println("Dog sound")
//    }
//}
fun main() {
    //Экземпляр класса - это его реализация в виде переменной
//    val car1 = Car()
//    val car2 = Car()
//    car2.brand = "Moskvich"
//    car2.wheel = 5
//    println("Эта машина бренда ${car1.brand}")
//    println("Вторая машина ${car2.brand}")

    val newCar1 = Car("Volga", power = 4.4)
    val newCar2 = Car("Ё-мобиль", power = 0.0)
    println("Эта машина бренда ${newCar1.brand}, колес у нее ${newCar1.wheel} колес")
    println("Вторая машина ${newCar2.brand}")
    println(newCar1.toString())

    //РФБОТА С ИНКАПСУЛЯЦИЕЙ
    val person = Person("Рассказчик", 30)
    person.name = "Тайлер"
    person.setAge(29)
    println(person.getAge())
    println(person.lastName)

//    var cat = Animal("Гав", 30, "Кошка")


}