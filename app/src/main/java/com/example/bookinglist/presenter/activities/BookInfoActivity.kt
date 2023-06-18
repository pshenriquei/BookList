package com.example.bookinglist.presenter.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.bookinglist.MainActivity
import com.example.bookinglist.R
import com.example.bookinglist.data.infra.ListOfBooksApi
import com.example.bookinglist.data.infra.retroFit
import com.example.bookinglist.data.model.ListBookModel
import com.example.bookinglist.databinding.ActivityBookInfoBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookInfoActivity : AppCompatActivity() {

    lateinit var binding: ActivityBookInfoBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var btnBack: ImageButton
    private var isBackButtonEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBar = findViewById(R.id.progressBar2)
        progressBar.visibility = View.GONE

        loadData()

        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.activity_book_info)
        }
        btnBack = supportActionBar!!.customView.findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            if (isBackButtonEnabled) {
                progressBar.isVisible = true
                isBackButtonEnabled = false
                btnBack.isEnabled = false

                lifecycleScope.launch {
                    val intent = Intent(this@BookInfoActivity, MainActivity::class.java)
                    delay(2000)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        isBackButtonEnabled = true
        btnBack.isEnabled = true
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            delay(2000)
            progressBar.visibility = View.GONE
        }
    }

    fun loadData() {
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
                            binding.authorNameText.text =
                                "Author: ${it.results.first().authors.first().name}"
                            binding.authorBirthText.text =
                                "Author's birth year: ${it.results.first().authors.first().birth_year}"
                            binding.authorDeathText.text =
                                "Author's death year: ${it.results.first().authors.first().death_year}"
                            binding.subject.text = "Subject: ${it.results.first().subjects.first()}"

                        }
                    }
                }
            }
            )
    }
}