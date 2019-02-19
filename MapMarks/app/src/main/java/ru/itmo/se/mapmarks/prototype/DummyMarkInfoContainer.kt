package ru.itmo.se.mapmarks.prototype

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ru.itmo.se.mapmarks.data.category.Category
import ru.itmo.se.mapmarks.data.mark.Mark
import ru.itmo.se.mapmarks.data.storage.CategoryOpsResultHandler
import ru.itmo.se.mapmarks.data.storage.MarkInfoContainer
import ru.itmo.se.mapmarks.data.storage.MarkOpsResultHandler
import kotlin.random.Random

class DummyMarkInfoContainer: MarkInfoContainer {

    private val categories: MutableList<Category> = mutableListOf(
        Category("Покемоны", "Pokemon Go", Color.argb(255, 190, 0, 0)),
        Category("Развлечения", "Типа жизнь", Color.argb(255, 0, 190, 0)),
        Category("Экономия", "Копим на вишневую семерку", Color.argb(255, 0, 0, 190))
    )

    private val marks: MutableList<Mark> = mutableListOf(
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

    init {
        marks.shuffle()
    }

    override val allCategories: Iterable<Category>
        get() = categories.toList()

    override val allMarks: Iterable<Mark>
        get() = marks.toList()

    override fun getCategoryOfMark(markData: Mark) = marks.find { it == markData }?.category

    override fun getCategoryByName(categoryName: String): Category {
        return allCategories.first { it.name == categoryName }
    }

    override fun getMarksForCategory(category: Category): Iterable<Mark> = marks.filter { it.category == category }

    override fun addCategory(category: Category): CategoryOpsResultHandler {
        val categoryExists = category in categories
        if (!categoryExists) {
            categories.add(category)
        }
        return CategoryOpsResultHandler(category, categoryExists)
    }

    override fun addMark(mark: Mark): MarkOpsResultHandler {
        val categoryNotExists = mark.category !in categories
        val markExists = mark in marks
        val markCannotBeAdded = categoryNotExists || markExists
        if (!markCannotBeAdded) {
            marks.add(0, mark)
        }
        return MarkOpsResultHandler(mark, markCannotBeAdded)
    }

    override fun containsCategory(categoryName: String) = categories.find { it.name == categoryName } != null

    override fun containsCategory(category: Category) = category in categories

    override fun containsMark(markName: String) = marks.find { it.name == markName } != null

    override fun containsMark(mark: Mark) = mark in marks

    override fun updateCategory(category: Category): CategoryOpsResultHandler {
        val categoryIndex = categories.indexOf(category)
        val categoryDoesNotExist = categoryIndex == -1
        if (!categoryDoesNotExist) {
            categories[categoryIndex] = category
        }
        return CategoryOpsResultHandler(category, categoryDoesNotExist)
    }

    override fun updateMark(mark: Mark): MarkOpsResultHandler {
        val markIndex = marks.indexOf(mark)
        val markDoesNotExists = markIndex == -1
        if (!markDoesNotExists) {
            marks[markIndex] = mark
        }
        return MarkOpsResultHandler(mark = mark, needCallback = markDoesNotExists)
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