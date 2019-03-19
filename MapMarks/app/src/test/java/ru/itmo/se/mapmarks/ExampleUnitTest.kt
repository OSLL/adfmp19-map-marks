package ru.itmo.se.mapmarks

import org.junit.Test

import org.junit.Assert.*
import ru.itmo.se.mapmarks.data.category.Category
import ru.itmo.se.mapmarks.data.storage.MarkInfoContainer
import ru.itmo.se.mapmarks.prototype.DummyMarkInfoContainer
import java.io.IOException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    val container: MarkInfoContainer = DummyMarkInfoContainer.INSTANCE
    val defCategory = Category("Покемоны", "Pokemon Go", 44)
    val defPoint = DummyMarkInfoContainer.newPointMarkWithRandomLocation(
        "Психические покемоны",
        "Зимой в ночное время можно поймать много психических покемонов",
        defCategory
    )

    val defPolygon = DummyMarkInfoContainer.newPolygonTriangleMarkWithRandomLocation(
        "Пастбище пикачу",
        "За 2 дня поимал в этой округе 100 пикачу",
        defCategory
    )


    @Test
    fun getCategoryByNameTest() {
        assertEquals(container.getCategoryByName("Покемоны").description, "Pokemon Go")
    }

    @Test
    fun getMarkByNameTest() {
        assertEquals(container.getMarkByName("Недоеденный обед").description, "Остается на ужин")
        assertEquals(container.getMarkByName("Грибное место").description, "Отличный поганочки")
    }

    @Test
    fun addCategotyTest() {
        container.addCategory(Category("category", "description", 0))
        assertEquals(container.getCategoryByName("category").description, "description")
    }

    @Test
    fun addPointTest() {
        container.addMark(
            DummyMarkInfoContainer.newPointMarkWithRandomLocation(
                "newPoint",
                "newPointDescripsion",
                defCategory
            )
        )
        assertEquals(container.getMarkByName("newPoint").description, "newPointDescripsion")
    }

    @Test
    fun addPolygonTest() {
        container.addMark(
            DummyMarkInfoContainer.newPointMarkWithRandomLocation(
                "newPolygon",
                "newPolygonDescripsion",
                defCategory
            )
        )
        assertEquals(container.getMarkByName("newPolygon").description, "newPolygonDescripsion")
    }

    @Test
    fun containsCategoryTest() {
        assertEquals(container.containsCategory(defCategory), true)
    }

    @Test
    fun containsMark() {
        assertEquals(container.containsMark(defPoint), true)
        assertEquals(container.containsMark(defPolygon), true)
    }

    @Test
    fun containsMarkByName() {
        assertEquals(container.containsMark("Дешевый обед"), true)
        assertEquals(container.containsMark("Рыбное место"), true)
    }

    @Test
    fun contaonsCategoryByName() {
        assertEquals(container.containsCategory("Экономия"), true)
    }

    @Test
    fun updateCategory() {
        container.addCategory(Category("newCategory", "newDescription", 0))
        assertEquals(container.getCategoryByName("newCategory").description, "newDescription")
        assertEquals(container.getCategoryByName("newCategory").color, 0)
        container.updateCategory(Category("newCategory", "updateDescription", 1))
        assertEquals(container.getCategoryByName("newCategory").description, "updateDescription")
        assertEquals(container.getCategoryByName("newCategory").color, 1)
    }

    @Test
    fun updateMark() {
        container.addMark(
            DummyMarkInfoContainer.newPointMarkWithRandomLocation(
                "newPointForUpdate",
                "description",
                defCategory
            )
        )
        assertEquals(container.getMarkByName("newPointForUpdate").description, "description")
        assertEquals(container.getMarkByName("newPointForUpdate").category, defCategory)
        val newCategory = Category("categoryForTestMarkUpate", "description", 44)
        container.updateMark(
            DummyMarkInfoContainer.newPointMarkWithRandomLocation(
                "newPointForUpdate",
                "description",
                defCategory
            ),
            DummyMarkInfoContainer.newPointMarkWithRandomLocation(
                "updatePoint",
                "updateDescription",
                newCategory
            )
        )
        assertEquals(container.containsMark("newPointForUpdate"), false)
        assertEquals(container.containsMark("updatePoint"), true)
        assertEquals(container.getMarkByName("updatePoint").description, "updateDescription")
        assertEquals(container.getMarkByName("updatePoint").category, newCategory)
    }

    @Test(expected = IOException::class)
    fun addExistCategoryTest() {
        container.addCategory(defCategory)
    }

    @Test(expected = IOException::class)
    fun addExistMarkTest() {
        container.addMark(defPoint)
        container.addMark(defPolygon)
    }
}
