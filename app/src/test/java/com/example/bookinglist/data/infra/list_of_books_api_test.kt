import com.example.bookinglist.data.infra.ListOfBooksApi
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListOfBooksApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: ListOfBooksApi

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ListOfBooksApi::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testGetBooks() {
        val responseBody = """
            {
                "results": [
                    {
                        "id": 1,
                        "title": "Book 1"
                    },
                    {
                        "id": 2,
                        "title": "Book 2"
                    }
                ]
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(responseBody))

        val response = api.getBooks().execute()

        assertEquals(true, response.isSuccessful)
        assertEquals(2, response.body()?.results?.size)
        assertEquals(1, response.body()?.results?.get(0)?.id)
        assertEquals("Book 1", response.body()?.results?.get(0)?.title)
    }

    @Test
    fun testGetBooksById() {
        val responseBody = """
            {
                "results": [
                    {
                        "id": 1,
                        "title": "Book 1"
                    }
                ]
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(responseBody))

        val response = api.getBooksById("1").execute()

        assertEquals(true, response.isSuccessful)
        assertEquals(1, response.body()?.results?.size)
        assertEquals(1, response.body()?.results?.get(0)?.id)
        assertEquals("Book 1", response.body()?.results?.get(0)?.title)
    }
}