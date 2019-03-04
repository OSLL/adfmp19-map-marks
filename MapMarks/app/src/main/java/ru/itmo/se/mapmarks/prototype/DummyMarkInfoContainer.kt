package ru.itmo.se.mapmarks.prototype

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ru.itmo.se.mapmarks.data.category.Category
import ru.itmo.se.mapmarks.data.mark.Mark
import ru.itmo.se.mapmarks.data.storage.ListMarkInfoContainer
import kotlin.random.Random

class DummyMarkInfoContainer: ListMarkInfoContainer() {
    init {
        val categories = mutableListOf(
            Category("Покемоны", "Pokemon Go", Color.argb(255, 190, 0, 0)),
            Category("Развлечения", "Типа жизнь", Color.argb(255, 0, 190, 0)),
            Category("Экономия", "Копим на вишневую семерку", Color.argb(255, 0, 0, 190))
        )

        val marks = mutableListOf(
            Mark("Редкие покемоны", "Зимой в ночное время можно поймать много редких покемонов", categories[0], MarkerOptions().position(randomLatLng())),
            Mark("Электрические покемоны", "Зимой в ночное время можно поймать много электрических покемонов", categories[0], MarkerOptions().position(randomLatLng())),
            Mark("Редкие земляные покемоны", "Зимой в ночное время можно поймать много редких земляных покемонов", categories[0], MarkerOptions().position(randomLatLng())),
            Mark("Реальные покемоны", "Зимой в ночное время можно поймать много реальных покемонов", categories[0], MarkerOptions().position(randomLatLng())),
            Mark("Стальные покемоны", "Зимой в ночное время можно поймать много стальных покемонов", categories[0], MarkerOptions().position(randomLatLng())),
            Mark("Темные покемоны", "Зимой в ночное время можно поймать много темных покемонов", categories[0], MarkerOptions().position(randomLatLng())),
            Mark("Огненные покемоны", "Зимой в ночное время можно поймать много огненных покемонов", categories[0], MarkerOptions().position(randomLatLng())),
            Mark("Психические покемоны", "Зимой в ночное время можно поймать много психических покемонов", categories[0], MarkerOptions().position(randomLatLng())),
            Mark("Водные покемоны", "Зимой в ночное время можно поймать много водных покемонов", categories[0], MarkerOptions().position(randomLatLng())),
            Mark("Травяные покемоны", "Зимой в ночное время можно поймать много травяных покемонов", categories[0], MarkerOptions().position(randomLatLng())),
            Mark("Покемоны-призраки", "Зимой в ночное время можно поймать много покемонов-призраков", categories[0], MarkerOptions().position(randomLatLng())),
            Mark("Покемоны-феи", "Зимой в ночное время можно поймать много покемонов-фей", categories[0], MarkerOptions().position(randomLatLng())),
            Mark("Обычные развлечения", "Самые обычные развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            Mark("Необычные развлечения", "Самые необычные развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            Mark("Экзотические развлечения", "Самые экзотические развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            Mark("Утренние развлечения", "Самые утренние развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            Mark("Дневные развлечения", "Самые дневные развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            Mark("Вечерние развлечения", "Самые вечерние развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            Mark("Ночные развлечения", "Самые ночные развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            Mark("Жаркие развлечения", "Самые жаркие развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            Mark("Зимние развлечения", "Самые зимные развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            Mark("Твои развлечения", "Самые твои развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            Mark("Весенние развлечения", "Самые весенние развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            Mark("Летние развлечения", "Самые летние развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            Mark("Осенние развлечения", "Самые осенние развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            Mark("Монетка на счастье", "Самые обычные развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            Mark("Круглосуточный ларек", "Самые необычные развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            Mark("Недоеденный обед", "Остается на ужин", categories[2], MarkerOptions().position(randomLatLng())),
            Mark("Дешевый обед", "Самый дешевый обед", categories[2], MarkerOptions().position(randomLatLng())),
            Mark("Недорогой обед", "Самый недорогой обед", categories[2], MarkerOptions().position(randomLatLng())),
            Mark("Доступный обед", "Самый доступный обед", categories[2], MarkerOptions().position(randomLatLng())),
            Mark("BUSINESS LUNCH", "Самый примитивный обед", categories[2], MarkerOptions().position(randomLatLng()))
        )

        marks.shuffle()
        categories.forEach{ addCategory(it) }
        marks.forEach { addMark(it) }
    }

    companion object {
        val INSTANCE = DummyMarkInfoContainer()

        fun newMarkWithRandomLocation(name: String, description: String, category: Category): Mark {
            return Mark(name, description, category, MarkerOptions().position(randomLatLng()))
        }

        private fun randomLatLng(): LatLng {
            val latitude = Random.nextDouble(-85.0, 85.0)
            val longitude = Random.nextDouble(-180.0, 180.0)
            return LatLng(latitude, longitude)
        }
    }
}