package com.example.bookinglist.presenter.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bookinglist.MainActivity
import com.example.bookinglist.R
import com.example.bookinglist.data.infra.ListOfBooksApi
import com.example.bookinglist.data.infra.retroFit
import com.example.bookinglist.data.model.BooksModel
import com.example.bookinglist.data.model.ListBookModel
import com.example.bookinglist.databinding.ActivityBookInfoBinding
import com.example.bookinglist.databinding.ActivityBookListBinding
import com.example.bookinglist.databinding.ItemBookListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookInfoBinding

    //TODO: Fazer button para voltar para a tela anterior


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id: String = intent.getStringExtra("id").toString()

        retroFit().create(ListOfBooksApi::class.java)
            .getBooksById(id)
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
                            binding.titleBookText.text = it.results.first().title
                            binding.authorNameText.text = it.results.first().authors.first().name
                            binding.authorBirthText.text = it.results.first().authors.first().birth_year.toString()
                            binding.authorDeathText.text = it.results.first().authors.first().death_year.toString()
                            binding.subject.text = it.results.first().subjects.first().toString()

                        }
                    }
                }

            }
            )
    }

}