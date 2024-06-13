package com.capstonebangkit.birdwatch.view.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstonebangkit.birdwatch.R
import com.capstonebangkit.birdwatch.data.remote.retrofit.ApiConfig
import com.capstonebangkit.birdwatch.data.remote.response.PredictResponse
import com.capstonebangkit.birdwatch.view.ui.bird.BirdAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var birdAdapter: BirdAdapter
    private lateinit var searchBar: EditText
    private var filteredList: MutableList<PredictResponse> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        searchBar = view.findViewById(R.id.search)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        birdAdapter = BirdAdapter(filteredList)
        recyclerView.adapter = birdAdapter

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    birdAdapter.setList(emptyList())
                } else {
                    searchBirds(s.toString())
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }

    private fun searchBirds(query: String) {
        val apiService = ApiConfig.getApiService()
        apiService.searchBirds(query).enqueue(object : Callback<List<PredictResponse>> {
            override fun onResponse(
                call: Call<List<PredictResponse>>,
                response: Response<List<PredictResponse>>
            ) {
                if (response.isSuccessful) {
                    val birds = response.body()
                    birds?.let {
                        birdAdapter.setList(it)
                    }
                } else {
                    Toast.makeText(activity, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<PredictResponse>>, t: Throwable) {
                Toast.makeText(activity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
