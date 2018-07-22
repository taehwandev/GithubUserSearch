package tech.thdev.githubusersearch

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import kotlinx.android.synthetic.main.activity_main.*
import tech.thdev.githubusersearch.util.inject
import tech.thdev.githubusersearch.util.loadFragment
import tech.thdev.githubusersearch.view.common.viewmodel.SearchQueryViewModel
import tech.thdev.githubusersearch.view.search.SearchFragment


class MainActivity : AppCompatActivity() {

    private val searchFragment: Fragment by lazy {
        SearchFragment.getInstance("Search!!!!!")
    }

    private val likeFragment: Fragment by lazy {
        SearchFragment.getInstance("Like!!!!!!!!")
    }

    private val searchQueryViewModel: SearchQueryViewModel by lazy(LazyThreadSafetyMode.NONE) {
        SearchQueryViewModel::class.java.inject(this) {
            SearchQueryViewModel()
        }
    }

    private val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_search -> {
                        searchFragment.loadFragment()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_liked -> {
                        likeFragment.loadFragment()
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        searchFragment.loadFragment()
    }

    private fun Fragment.loadFragment() {
        loadFragment(R.id.container, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)

        // Associate searchable configuration with the SearchView
        (getSystemService(Context.SEARCH_SERVICE) as SearchManager).run {
            (menu?.findItem(R.id.menu_search)?.actionView as SearchView).run {
                queryHint = getString(R.string.search_hint)
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(query: String?): Boolean {
                        searchQueryViewModel.setSearchQuery(query)
                        return false
                    }
                })
            }
        }
        return true
    }
}
