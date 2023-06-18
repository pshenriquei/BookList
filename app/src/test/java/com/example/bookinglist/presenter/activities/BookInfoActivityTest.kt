import com.example.bookinglist.data.infra.ListOfBooksApi
import com.example.bookinglist.data.infra.retroFit
import com.example.bookinglist.data.model.AuthorsModel
import com.example.bookinglist.data.model.BooksModel
import com.example.bookinglist.data.model.ListBookModel
import com.example.bookinglist.presenter.activities.BookInfoActivity
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class BookInfoActivityTest {

    private lateinit var bookInfoActivity: BookInfoActivity

    @Mock
    private lateinit var mockListOfBooksApi: ListOfBooksApi

    @Mock
    private lateinit var mockCall: Call<ListBookModel>

    @Mock
    private lateinit var mockResponse: Response<ListBookModel>

    @Before
    fun setup() {
        bookInfoActivity = spy(BookInfoActivity())
        bookInfoActivity.intent = mock()
        whenever(bookInfoActivity.intent.getStringExtra(any())).thenReturn("123")

        whenever(retroFit().create(ListOfBooksApi::class.java)).thenReturn(mockListOfBooksApi)
        whenever(mockListOfBooksApi.getBooksById(any())).thenReturn(mockCall)
    }

    @Test
    fun loadData_shouldUpdateUIWithBookData() {
        val bookModel = BooksModel(
            id = 1,
            title = "Book Title",
            authors = listOf(AuthorsModel("Author Name", 1980, 2020)),
            subjects = listOf("Subject")
        )
        val listBookModel = ListBookModel(results = listOf(bookModel))

        whenever(mockCall.enqueue(any())).thenAnswer {
            val callback = it.arguments[0] as Callback<ListBookModel>
            callback.onResponse(mockCall, mockResponse.apply {
                whenever(isSuccessful).thenReturn(true)
                whenever(body()).thenReturn(listBookModel)
            })
        }

        bookInfoActivity.loadData()

        verify(bookInfoActivity.binding.titleBookText).text = "Title: ${bookModel.title}"
        verify(bookInfoActivity.binding.authorNameText).text = "Author: ${bookModel.authors.first().name}"
        verify(bookInfoActivity.binding.authorBirthText).text = "Author's birth year: ${bookModel.authors.first().birth_year}"
        verify(bookInfoActivity.binding.authorDeathText).text = "Author's death year: ${bookModel.authors.first().death_year}"
        verify(bookInfoActivity.binding.subject).text = "Subject: ${bookModel.subjects.first()}"
    }
}