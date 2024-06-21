package com.capstonebangkit.birdwatch.view.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonebangkit.birdwatch.helper.BookmarkAdapter
import com.capstonebangkit.birdwatch.data.remote.response.BookmarkResponseItem
import com.capstonebangkit.birdwatch.databinding.FragmentHomeBinding
import com.capstonebangkit.birdwatch.view.detailbookmark.DetailBookmarkActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var bookmarkAdapter: BookmarkAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
        observeViewModel()

        homeViewModel.getBookmarks()
    }

    private fun setupRecyclerView() {
        binding.rvBookmarks.layoutManager = LinearLayoutManager(requireContext())
        bookmarkAdapter = BookmarkAdapter(emptyList()) { bookmark ->
            val intent = Intent(requireContext(), DetailBookmarkActivity::class.java).apply {
                putExtra(DetailBookmarkActivity.EXTRA_BOOKMARK, bookmark as Parcelable)
            }
            startActivityForResult(intent, REQUEST_CODE_DETAIL)
        }
        binding.rvBookmarks.adapter = bookmarkAdapter
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterBookmarks(newText ?: "")
                return true
            }
        })
    }

    private fun filterBookmarks(query: String) {
        val filteredList = homeViewModel.bookmarks.value?.filter {
            it?.jenisBurung?.contains(query, ignoreCase = true) == true
                    || it?.genus?.contains(query, ignoreCase = true) == true
                    || it?.famili?.contains(query, ignoreCase = true) == true
        } ?: emptyList()
        bookmarkAdapter.updateList(filteredList)
    }

    private fun observeViewModel() {
        homeViewModel.bookmarks.observe(viewLifecycleOwner) { bookmarks ->
            bookmarkAdapter.updateList(bookmarks)
        }

        homeViewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            showToast(message)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DETAIL && resultCode == Activity.RESULT_OK) {
            val deletedBookmarkId = data?.getStringExtra(DetailBookmarkActivity.EXTRA_BOOKMARK_ID)
            deletedBookmarkId?.let { id ->
                homeViewModel.removeBookmarkById(id)
            }
        }
    }

    companion object {
        const val REQUEST_CODE_DETAIL = 100
    }
}
