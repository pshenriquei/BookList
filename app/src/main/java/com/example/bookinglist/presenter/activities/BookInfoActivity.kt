package com.example.bookinglist.presenter.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.bookinglist.MainActivity
import com.example.bookinglist.R
import com.example.bookinglist.data.infra.ListOfBooksApi
import com.example.bookinglist.data.infra.retroFit
import com.example.bookinglist.data.model.ListBookModel
import com.example.bookinglist.databinding.ActivityBookInfoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()
        Toolbar()

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
                            binding.titleBookText.text = "Title: ${it.results.first().title}"
                            binding.authorNameText.text = "Author: ${it.results.first().authors.first().name}"
                            binding.authorBirthText.text = "Author's birth year: ${it.results.first().authors.first().birth_year}"
                            binding.authorDeathText.text = "Author's death year: ${it.results.first().authors.first().death_year}"
                            binding.subject.text = "Subject: ${it.results.first().subjects.first()}"

                        }
                    }
                }

            }
            )
    }

    private fun Toolbar(){
        val toolbarBookInfo = binding.toolbarBookInfo

        val backIcon = getDrawable(R.drawable.ic_back)
        backIcon?.setTint(ContextCompat.getColor(this, R.color.black))
        toolbarBookInfo.navigationIcon = backIcon
        toolbarBookInfo.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }
    }

}