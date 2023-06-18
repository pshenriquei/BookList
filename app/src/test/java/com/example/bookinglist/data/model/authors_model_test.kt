import com.example.bookinglist.data.model.AuthorsModel
import org.junit.Assert.assertEquals
import org.junit.Test

class AuthorsModelTest {

    @Test
    fun testAuthorsModel() {
        val author = AuthorsModel("John Doe", 1980, 0)

        assertEquals("John Doe", author.name)
        assertEquals(1980, author.birth_year)
        assertEquals(0, author.death_year)
    }
}