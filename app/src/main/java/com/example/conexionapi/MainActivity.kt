package com.example.conexionapi

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.conexionapi.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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


    private lateinit var listaCopia: MutableList<Holidays>

    private lateinit var context: Context

    private lateinit var holidaysService: HolidayService
    private var pageNumber = 0
    private var totalPages = 0
    private var allHolidays = mutableListOf<Holidays>()

    private lateinit var spinner: Spinner
    private var isLoading = false
    private var isLastPage = false

    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinner = binding.numPag
        binding.tvNo.text = "Cargando"

        cargaSpinner()

        rvMain = findViewById(R.id.rvMain)
        totalPages = 1
        miAdapter = HolidaysAdapter(allHolidays)
        layoutManager = LinearLayoutManager(applicationContext)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://calendarific.com/api/v2/holidays/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val holidaysService = retrofit.create(HolidayService::class.java)
        binding.tvNo.visibility = View.VISIBLE

        holidaysService.getHolidays("1a14837dd82dc020d405b6cd2d9f99888dd05e75&country=ES&year=2023", totalPages)
            .enqueue(object: Callback<HolidaysResponse>{
                override fun onResponse(
                    call: Call<HolidaysResponse>,
                    response: Response<HolidaysResponse>
                ) {
                    val holidays = response.body()?.holidays
                    allHolidays.addAll(holidays!!)

                    if (response.isSuccessful){
                        binding.tvNo.visibility = View.INVISIBLE
                        miAdapter.setList(allHolidays)
                        binding.rvMain.layoutManager = layoutManager
                        binding.rvMain.adapter = miAdapter
                    }
                }

                override fun onFailure(call: Call<HolidaysResponse>, t: Throwable) {
                    binding.tvNo.visibility = View.VISIBLE
                    binding.tvNo.text = "No hay vacaciones ea"
                }
            })

        //spinnerListener()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setUpScrollListener(){
        binding.rvMain.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val totalItemCount = binding.rvMain.computeVerticalScrollRange()
            val visibleItemCount = binding.rvMain.computeVerticalScrollExtent()
            val pastVisibleItems = binding.rvMain.computeVerticalScrollOffset()

            if(pastVisibleItems + visibleItemCount >= totalItemCount * 0.60){
                addNextN()
            }
        }
    }

    fun addNextN(){
        if(pageNumber < 20){

            CoroutineScope(Dispatchers.IO).launch {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://calendarific.com/api/v2/holidays/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val call = retrofit.create(HolidayService::class.java)
                    .getHolidays("1a14837dd82dc020d405b6cd2d9f99888dd05e75&country=ES&year=2023", totalPages)
                    .enqueue(object: Callback<HolidaysResponse>{
                        override fun onResponse(
                            call: Call<HolidaysResponse>,
                            response: Response<HolidaysResponse>
                        ) {
                            runOnUiThread{
                                if(call.isExecuted){
                                    val holidays = response.body()?.holidays
                                    allHolidays.addAll(holidays!!)
                                    miAdapter.notifyDataSetChanged()
                                }else{
                                    println("error")
                                }
                            }
                        }

                        override fun onFailure(call: Call<HolidaysResponse>, t: Throwable) {
                            binding.tvNo.visibility = View.VISIBLE
                            binding.tvNo.text = "No hay vacaciones ea"
                        }
                    })
            }

        }
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
                    .baseUrl("https://calendarific.com/api/v2/holidays/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val holidaysService = retrofit.create(HolidayService::class.java)
                val selectedItem = spinner.getItemAtPosition(position) as Int

                holidaysService.getHolidays(
                    "1a14837dd82dc020d405b6cd2d9f99888dd05e75&country=ES&year=2023",
                    totalPages
                )
                    .enqueue(object : Callback<HolidaysResponse> {
                        override fun onResponse(
                            call: Call<HolidaysResponse>,
                            response: Response<HolidaysResponse>
                        ) {
                            allHolidays.clear()
                            miAdapter.setList(allHolidays)
                            val layoutManager = LinearLayoutManager(applicationContext)
                            binding.rvMain.layoutManager = layoutManager
                            binding.rvMain.adapter = miAdapter
                            val holidays = response.body()?.holidays
                            allHolidays.addAll(holidays!!)

                            if (response.isSuccessful) {
                                binding.rvMain.visibility = View.VISIBLE
                                binding.tvNo.visibility = View.INVISIBLE
                                miAdapter.setList(allHolidays)
                                val layoutManager = LinearLayoutManager(applicationContext)
                                binding.rvMain.layoutManager = layoutManager
                                binding.rvMain.adapter = miAdapter
                            }
                        }

                        override fun onFailure(call: Call<HolidaysResponse>, t: Throwable) {
                            binding.tvNo.visibility = View.VISIBLE
                            binding.tvNo.text = "No hay vacaciones ea"
                        }
                    })
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        }

    private fun cargaSpinner(){
        val numbers = ArrayList<Int>()
        for(i in 1..20){
            numbers.add(i)
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, numbers)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun cargarTodasHolidays(){
        while (pageNumber <= totalPages){
            holidaysService.getHolidays(
                "1a14837dd82dc020d405b6cd2d9f99888dd05e75&country=ES&year=2023",
                totalPages
            )
                .enqueue(object : Callback<HolidaysResponse> {
                    override fun onResponse(
                        call: Call<HolidaysResponse>,
                        response: Response<HolidaysResponse>
                    ) {

                        val holidays = response.body()?.holidays
                        allHolidays.addAll(holidays!!)
                        pageNumber++
                    }

                    override fun onFailure(call: Call<HolidaysResponse>, t: Throwable) {

                    }
                })
        }
    }
    }