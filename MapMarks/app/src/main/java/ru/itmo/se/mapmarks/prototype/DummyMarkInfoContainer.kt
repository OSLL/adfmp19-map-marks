package ru.itmo.se.mapmarks.prototype

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import ru.itmo.se.mapmarks.data.category.Category
import ru.itmo.se.mapmarks.data.mark.Mark
import ru.itmo.se.mapmarks.data.mark.point.PointMark
import ru.itmo.se.mapmarks.data.mark.polygon.PolygonMark
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
            PointMark("Редкие покемоны", "Зимой в ночное время можно поймать много редких покемонов", categories[0], MarkerOptions().position(randomLatLng())),
            PointMark("Электрические покемоны", "Зимой в ночное время можно поймать много электрических покемонов", categories[0], MarkerOptions().position(randomLatLng())),
            PointMark("Редкие земляные покемоны", "Зимой в ночное время можно поймать много редких земляных покемонов", categories[0], MarkerOptions().position(randomLatLng())),
            PointMark("Реальные покемоны", "Зимой в ночное время можно поймать много реальных покемонов", categories[0], MarkerOptions().position(randomLatLng())),
            PointMark("Стальные покемоны", "Зимой в ночное время можно поймать много стальных покемонов", categories[0], MarkerOptions().position(randomLatLng())),
            PointMark("Темные покемоны", "Зимой в ночное время можно поймать много темных покемонов", categories[0], MarkerOptions().position(randomLatLng())),
            PointMark("Огненные покемоны", "Зимой в ночное время можно поймать много огненных покемонов", categories[0], MarkerOptions().position(randomLatLng())),
            PointMark("Психические покемоны", "Зимой в ночное время можно поймать много психических покемонов", categories[0], MarkerOptions().position(randomLatLng())),
            PointMark("Водные покемоны", "Зимой в ночное время можно поймать много водных покемонов", categories[0], MarkerOptions().position(randomLatLng())),
            PointMark("Травяные покемоны", "Зимой в ночное время можно поймать много травяных покемонов", categories[0], MarkerOptions().position(randomLatLng())),
            PointMark("Покемоны-призраки", "Зимой в ночное время можно поймать много покемонов-призраков", categories[0], MarkerOptions().position(randomLatLng())),
            PointMark("Покемоны-феи", "Зимой в ночное время можно поймать много покемонов-фей", categories[0], MarkerOptions().position(randomLatLng())),
            PointMark("Обычные развлечения", "Самые обычные развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            PointMark("Необычные развлечения", "Самые необычные развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            PointMark("Экзотические развлечения", "Самые экзотические развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            PointMark("Утренние развлечения", "Самые утренние развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            PointMark("Дневные развлечения", "Самые дневные развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            PointMark("Вечерние развлечения", "Самые вечерние развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            PointMark("Ночные развлечения", "Самые ночные развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            PointMark("Жаркие развлечения", "Самые жаркие развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            PointMark("Зимние развлечения", "Самые зимные развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            PointMark("Твои развлечения", "Самые твои развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            PointMark("Весенние развлечения", "Самые весенние развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            PointMark("Летние развлечения", "Самые летние развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            PointMark("Осенние развлечения", "Самые осенние развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            PointMark("Монетка на счастье", "Самые обычные развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            PointMark("Круглосуточный ларек", "Самые необычные развлечения", categories[1], MarkerOptions().position(randomLatLng())),
            PointMark("Недоеденный обед", "Остается на ужин", categories[2], MarkerOptions().position(randomLatLng())),
            PointMark("Дешевый обед", "Самый дешевый обед", categories[2], MarkerOptions().position(randomLatLng())),
            PointMark("Недорогой обед", "Самый недорогой обед", categories[2], MarkerOptions().position(randomLatLng())),
            PointMark("Доступный обед", "Самый доступный обед", categories[2], MarkerOptions().position(randomLatLng())),
            PointMark("BUSINESS LUNCH", "Самый примитивный обед", categories[2], MarkerOptions().position(randomLatLng())),
            newPolygonTriangleMarkWithRandomLocation("Грибное место", "Отличный поганочки", categories[2]),
            newPolygonTriangleMarkWithRandomLocation("Рыбное место", "Тут отлично порыбачили в прошлые выходные", categories[2]),
            newPolygonTriangleMarkWithRandomLocation("Пастбище пикачу", "За 2 дня поимал в этой округе 100 пикачу", categories[0])
        )

        marks.shuffle()
        categories.forEach{ addCategory(it) }
        marks.forEach { addMark(it) }
    }

    companion object {
        val INSTANCE = DummyMarkInfoContainer()

        fun newPointMarkWithRandomLocation(name: String, description: String, category: Category): PointMark {
            return PointMark(name, description, category, MarkerOptions().position(randomLatLng()))
        }

        fun newPolygonTriangleMarkWithRandomLocation(name: String, description: String, category: Category): PolygonMark {
            val base = randomLatLng()
            val options = PolygonOptions().add(
                base, LatLng(base.latitude + 10, base.longitude + 10),
                LatLng(base.latitude, base.longitude + 10), base
            )
            return PolygonMark(name, description, category, options)
        }

        private fun randomLatLng(): LatLng {
            val latitude = Random.nextDouble(-85.0, 85.0)
            val longitude = Random.nextDouble(-180.0, 180.0)
            return LatLng(latitude, longitude)
        }
    }
}