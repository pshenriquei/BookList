import androidx.test.core.app.ActivityScenario
import com.example.bookinglist.MainActivity
import com.example.bookinglist.data.infra.ListOfBooksApi
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

@ExperimentalCoroutinesApi
class MainActivityTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var mockListOfBooksApi: ListOfBooksApi

    private lateinit var mockWebServer: MockWebServer
    private lateinit var retrofit: Retrofit
    private lateinit var activityScenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        mockWebServer = MockWebServer()
        mockWebServer.start()

        retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mockListOfBooksApi = retrofit.create(ListOfBooksApi::class.java)

        activityScenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
        mockWebServer.shutdown()
        activityScenario.close()
    }

    @Test
    fun loadData_shouldUpdateAdapterWithData() {
        val responseJson = """
            {
                "results": [
                    {
                        "id": 1,
                        "title": "Book 1",
                        "authors": [
                            {
                                "name": "Author 1",
                                "birth_year": 1980,
                                "death_year": 2020
                            }
                        ],
                        "subjects": ["Subject 1"]
                    },
                    {
                        "id": 2,
                        "title": "Book 2",
                        "authors": [
                            {
                                "name": "Author 2",
                                "birth_year": 1990,
                                "death_year": 2030
                            }
                        ],
                        "subjects": ["Subject 2"]
                    }
                ]
            }
        """.trimIndent()
        val mockResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(responseJson)
        mockWebServer.enqueue(mockResponse)

        whenever(retrofit.create(ListOfBooksApi::class.java)).thenReturn(mockListOfBooksApi)
        whenever(mockListOfBooksApi.getBooks()).thenReturn(mock())

        activityScenario.onActivity { activity ->
            activity.loadData()
        }

        activityScenario.onActivity { activity ->
            val adapter = activity.adapter
            val books = adapter.books

            assert(books.size == 2)
            assert(books[0].title == "Book 1")
            assert(books[1].title == "Book 2")
        }
    }
}