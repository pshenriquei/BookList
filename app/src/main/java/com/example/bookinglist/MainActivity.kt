package com.example.bookinglist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookinglist.data.infra.ListOfBooksApi
import com.example.bookinglist.data.infra.retroFit
import com.example.bookinglist.databinding.ItemBookListBinding
import com.example.bookinglist.data.model.AuthorsModel
import com.example.bookinglist.data.model.BooksModel
import com.example.bookinglist.data.model.ListBookModel
import com.example.bookinglist.databinding.ActivityBookListBinding
import com.example.bookinglist.presenter.activities.BookInfoActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: BookListAdapter
    private var bookList: ArrayList<BooksModel> = arrayListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityBookListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = BookListAdapter(bookList)

        recyclerView = findViewById<View>(R.id.recycler_books) as RecyclerView



        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)

        retroFit().create(ListOfBooksApi::class.java)
            .getBooks()
            .enqueue(object : Callback<ListBookModel> {
                override fun onFailure(call: Call<ListBookModel>, t: Throwable) {
                    Toast.makeText(applicationContext, "Server Error!", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<ListBookModel>,
                    response: Response<ListBookModel>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            adapter.books.clear()
                            adapter.books.addAll(it.results)
                            adapter.notifyDataSetChanged()

                        }
                    }
                }

            }
            )
    }

    private inner class BookListAdapter(val books: ArrayList<BooksModel>) :
        RecyclerView.Adapter<ListBooksHolder>() {

        private lateinit var binding: ItemBookListBinding
        private lateinit var context: Context

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListBooksHolder {
            context = parent.context
            binding =
                ItemBookListBinding.inflate(LayoutInflater.from(context), parent, false)
            return ListBooksHolder(binding)

        }

        override fun getItemCount(): Int = books.size

        override fun onBindViewHolder(holder: ListBooksHolder, position: Int) {
            val book = books[position]
            holder.bind(book,context)

        }
    }

    private inner class ListBooksHolder(binding: ItemBookListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val textBookTitle = binding.titleBookText
        private val textAuthorName = binding.authorNameText
        private val button = binding.button

        fun bind(bookInfo: BooksModel, ctx: Context) {

            textBookTitle.text = "Title: ${bookInfo.title}"
            textAuthorName.text = "Author: ${bookInfo.authors.first().name}"

            button.setOnClickListener {
                val intent = Intent(ctx, BookInfoActivity::class.java)
                intent.putExtra("id",bookInfo.id.toString())
                startActivity(intent)
            }

        }

    }
}
