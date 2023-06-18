import com.example.bookinglist.data.model.AuthorsModel
import com.example.bookinglist.data.model.BooksModel
import com.example.bookinglist.data.model.ListBookModel
import org.junit.Assert.assertEquals
import org.junit.Test

class ListBookModelTest {

    @Test
    fun testListBookModel() {
        val author1 = AuthorsModel("John Doe", 1980, 0)
        val author2 = AuthorsModel("Jane Smith", 1990, 0)
        val book1 = BooksModel(1, "Book 1", listOf(author1), listOf("Fiction"))
        val book2 = BooksModel(2, "Book 2", listOf(author2), listOf("Adventure"))
        val listBookModel = ListBookModel(listOf(book1, book2))

        assertEquals(2, listBookModel.results.size)
        assertEquals(book1, listBookModel.results[0])
        assertEquals(book2, listBookModel.results[1])
    }
}