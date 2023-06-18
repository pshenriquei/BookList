import com.example.bookinglist.data.model.AuthorsModel
import com.example.bookinglist.data.model.BooksModel
import org.junit.Assert.assertEquals
import org.junit.Test

class BooksModelTest {

    @Test
    fun testBooksModel() {
        val author = AuthorsModel("John Doe", 1980, 0)
        val book = BooksModel(1, "Sample Book", listOf(author), listOf("Fiction", "Adventure"))

        assertEquals(1, book.id)
        assertEquals("Sample Book", book.title)
        assertEquals(1, book.authors.size)
        assertEquals(author, book.authors[0])
        assertEquals(2, book.subjects.size)
        assertEquals("Fiction", book.subjects[0])
        assertEquals("Adventure", book.subjects[1])
    }
}