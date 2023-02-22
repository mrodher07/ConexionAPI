package com.example.conexionapi

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
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

    private var lista = mutableListOf<DATA>()
    private lateinit var miAdapter: HolidaysAdapter


    private lateinit var listaCopia: MutableList<DATA>

    private lateinit var context: Context

    private lateinit var holidaysService: HolidayService
    private var pageNumber = 0
    private var totalPages = 0
    private var allHolidays = mutableListOf<Holidays>()
    private var allInmuebles = mutableListOf<SamirResponse>()

    private lateinit var spinner: Spinner
    private var isLoading = false
    private var isLastPage = false

    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvNo.text = "Cargando"

        rvMain = findViewById(R.id.rvMain)
        totalPages = 1
        miAdapter = HolidaysAdapter(allInmuebles)
        rvMain.adapter = miAdapter
        layoutManager = LinearLayoutManager(applicationContext)

        /*val retrofit = Retrofit.Builder()
            .baseUrl("https://calendarific.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val holidaysService = retrofit.create(HolidayService::class.java)
        binding.tvNo.visibility = View.VISIBLE
        System.out.println("Ha entrado")
        holidaysService.getHolidays("1a14837dd82dc020d405b6cd2d9f99888dd05e75", "ES", "2023")
            .enqueue(object: Callback<DATA>{
                override fun onResponse(
                    call: Call<DATA>,
                    response: Response<DATA>
                ) {
                    val datos = response.body()?.response
                    val respuesta = datos?.holidays
                    System.out.println(response)
                    if (respuesta != null) {
                        System.out.println("aas")
                        for (a in respuesta){
                            allHolidays.add(a)
                        }
                    }

                    if (response.isSuccessful){
                        binding.tvNo.visibility = View.INVISIBLE
                        miAdapter.setList(allHolidays)
                        binding.rvMain.layoutManager = layoutManager
                        binding.rvMain.adapter = miAdapter
                    }
                }
                override fun onFailure(call: Call<DATA>, t: Throwable) {
                    binding.tvNo.visibility = View.VISIBLE
                    binding.tvNo.text = "No hay vacaciones ea"
                }
            })*/
        inmuebles()
        listenersBotones()
    }

    fun inmuebles() {

        val tvNo = findViewById<TextView>(R.id.tvNo)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.10.30.161:8080/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val myApi = retrofit.create(SamirService::class.java)

        tvNo.visibility = View.VISIBLE

        myApi.getInmuebles().enqueue(object : Callback<List<SamirResponse>> {
            override fun onResponse(
                call: Call<List<SamirResponse>>,
                response: Response<List<SamirResponse>>
            ) {
                val inmueble = response.body()
                allInmuebles.addAll(inmueble!!)

                if (response.isSuccessful) {
                    tvNo.visibility = View.INVISIBLE
                    miAdapter.setList(allInmuebles)
                    rvMain.adapter = miAdapter
                }
            }

            override fun onFailure(call: Call<List<SamirResponse>>, t: Throwable) {
                tvNo.visibility = View.VISIBLE
                tvNo.text = "No hay na"
                println(call.toString())
                println(t.toString())
            }
        })
    }

    fun listenersBotones() {

        var btnPOST = findViewById<Button>(R.id.btnPOST)
        var btnDELETE = findViewById<Button>(R.id.btnDELETE)
        var btnPUT = findViewById<Button>(R.id.btnPUT)
        btnPOST.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {

                val retrofit = Retrofit.Builder()
                    .baseUrl("http://10.10.30.161:8080/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val myApi = retrofit.create(SamirService::class.java)

                val myData = SamirResponse(
                    "Mi casa", 30f, "Casa mu buena, increible", 10, 5, "Adra",
                    "Ayuntamiento", "2022-01-27", 2, 3, 40
                )

                myApi.postMyData(myData).enqueue(object : Callback<SamirResponse> {

                    override fun onFailure(call: Call<SamirResponse>, t: Throwable) {

                    }

                    override fun onResponse(
                        call: Call<SamirResponse>,
                        response: Response<SamirResponse>
                    ) {
                        if (response.isSuccessful) {
                            val myResponse = response.body()
                            Log.i("POST", "realizado con id: " + myResponse?.idInmueble.toString())
                        } else {
                            System.err.println(response.errorBody()?.string())
                        }
                    }
                })
            }
        }

        btnDELETE.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Ingresa un número")

// Configura el cuadro de texto de entrada
            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_NUMBER
            builder.setView(input)

// Agrega el botón "Aceptar"
            builder.setPositiveButton("Aceptar") { dialog, which ->
                val number = input.text.toString().toInt()


                val retrofit = Retrofit.Builder()
                    .baseUrl("http://10.10.30.161:8080/api/inmuebles/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val myApi = retrofit.create(SamirService::class.java)

                myApi.deleteInmueble(input.text.toString()).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        Log.i("delete", "delete completado")
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.i("delete", "delete error")
                    }

                })

            }

            builder.setNegativeButton("Cancelar") { dialog, which ->
                dialog.cancel()
            }

            builder.show()


        }

        btnPUT.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {

                val retrofit = Retrofit.Builder()
                    .baseUrl("http://10.10.30.161:8080/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val myApi = retrofit.create(SamirService::class.java)

                val myData = SamirResponse(
                    "Casa 4 plantas", 30f, "Casa mu buena, increible, pero mejor ", 10, 5, "Adra",
                    "Paseo", "2022-01-27", 2, 3, 43
                )

                myApi.postMyData(myData).enqueue(object : Callback<SamirResponse> {

                    override fun onFailure(call: Call<SamirResponse>, t: Throwable) {

                    }

                    override fun onResponse(
                        call: Call<SamirResponse>,
                        response: Response<SamirResponse>
                    ) {
                        if (response.isSuccessful) {
                            val myResponse = response.body()
                            Log.i("POST", "realizado con id: " + myResponse?.idInmueble.toString())
                        } else {
                            System.err.println(response.errorBody()?.string())
                        }
                    }
                })
            }
        }

        /*@RequiresApi(Build.VERSION_CODES.M)
    private fun setUpScrollListener(){
        binding.rvMain.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val totalItemCount = binding.rvMain.computeVerticalScrollRange()
            val visibleItemCount = binding.rvMain.computeVerticalScrollExtent()
            val pastVisibleItems = binding.rvMain.computeVerticalScrollOffset()

            if(pastVisibleItems + visibleItemCount >= totalItemCount * 0.60){
                addNextN()
            }
        }
    }*/

        /*fun addNextN(){
        if(pageNumber < 20){

            CoroutineScope(Dispatchers.IO).launch {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://calendarific.com/api/v2/holidays/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val call = retrofit.create(HolidayService::class.java)
                    .getHolidays("1a14837dd82dc020d405b6cd2d9f99888dd05e75", "ES", "2023")
                    .enqueue(object: Callback<DATA>{
                        override fun onResponse(
                            call: Call<DATA>,
                            response: Response<DATA>
                        ) {
                            runOnUiThread{
                                if(call.isExecuted){
                                    val datos = response.body()?.response
                                    val respuesta = datos?.holidays
                                    System.out.println(response)
                                    if (respuesta != null) {
                                        var i = 0;
                                        for (a in respuesta){
                                            allHolidays.add(a)
                                        }
                                    }
                                    miAdapter.notifyDataSetChanged()
                                }else{
                                    println("error")
                                }
                            }
                        }

                        override fun onFailure(call: Call<DATA>, t: Throwable) {
                            binding.tvNo.visibility = View.VISIBLE
                            binding.tvNo.text = "No hay vacaciones ea"
                        }
                    })
            }

        }
    }*/

        /*fun spinnerListener(){
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

                holidaysService.getHolidays("1a14837dd82dc020d405b6cd2d9f99888dd05e75", "ES", "2023")
                    .enqueue(object : Callback<DATA> {
                        override fun onResponse(
                            call: Call<DATA>,
                            response: Response<DATA>
                        ) {
                            allHolidays.clear()
                            miAdapter.setList(allHolidays)
                            val layoutManager = LinearLayoutManager(applicationContext)
                            binding.rvMain.layoutManager = layoutManager
                            binding.rvMain.adapter = miAdapter
                            val datos = response.body()?.response
                            val respuesta = datos?.holidays
                            System.out.println(response)
                            if (respuesta != null) {
                                for (a in respuesta){
                                    allHolidays.add(a)
                                }
                            }

                            if (response.isSuccessful) {
                                binding.rvMain.visibility = View.VISIBLE
                                binding.tvNo.visibility = View.INVISIBLE
                                miAdapter.setList(allHolidays)
                                val layoutManager = LinearLayoutManager(applicationContext)
                                binding.rvMain.layoutManager = layoutManager
                                binding.rvMain.adapter = miAdapter
                            }
                        }

                        override fun onFailure(call: Call<DATA>, t: Throwable) {
                            binding.tvNo.visibility = View.VISIBLE
                            binding.tvNo.text = "No hay vacaciones ea"
                        }
                    })
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        }*/


        /*private fun cargarTodasHolidays(){
        while (pageNumber <= totalPages){
            holidaysService.getHolidays("1a14837dd82dc020d405b6cd2d9f99888dd05e75", "ES", "2023")
                .enqueue(object : Callback<DATA> {
                    override fun onResponse(
                        call: Call<DATA>,
                        response: Response<DATA>
                    ) {

                        val datos = response.body()?.response
                        val respuesta = datos?.holidays
                        System.out.println(response)
                        if (respuesta != null) {

                            for (a in respuesta){
                                allHolidays.add(a)
                            }
                        }
                        pageNumber++
                    }

                    override fun onFailure(call: Call<DATA>, t: Throwable) {

                    }
                })
        }
    }*/
    }
}