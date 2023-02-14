package com.example.conexionapi

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.conexionapi.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var rvMain: RecyclerView

    private var lista = mutableListOf<Holidays>()
    private lateinit var miAdapter: HolidaysAdapter


    private lateinit var searchView: androidx.appcompat.widget.SearchView

    private lateinit var listaCopia: MutableList<Holidays>

    private lateinit var context: Context

    private lateinit var pokemonService: HolidayService
    private var pageNumber = 0
    private var totalPages = 0
    private var allHolidays = mutableListOf<Holidays>()

    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinner = binding.numPag
        binding.tvNo.text = "Cargando"

        cargaSpinner()



    }

    fun spinnerListener(){
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.rvMain.visibility = View.INVISIBLE
                binding.tvNo.visibility = View.INVISIBLE
                binding.tvNo.text = "Cargando..."

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://calendarific.com/api/v2/holidays")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val holidaysService = retrofit.create(HolidayService::class.java)
                val selectedItem = spinner.getItemAtPosition(position) as Int
                holidaysService.getHolidays("1a14837dd82dc020d405b6cd2d9f99888dd05e75&country=ES&year=2023", selectedItem)
                    .enqueue(object: Callback<HolidayService>{
                        override fun onResponse(
                            call: Call<HolidayService>,
                            response: Response<HolidayService>
                        ) {
                            allHolidays.clear()
                            miAdapter.setList(allHolidays)
                            val layoutManager = LinearLayoutManager(applicationContext)
                            binding.rvMain.layoutManager = layoutManager
                            binding.rvMain.adapter = miAdapter
                            val holidays = response.body()?.getHolidays()
                            allHolidays.addAll(holidays!!)
                        }

                        override fun onFailure(call: Call<HolidayService>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    }
            })

        }
    }
}