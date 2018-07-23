package tech.thdev.githubusersearch

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_fab_sort.*
import tech.thdev.githubusersearch.util.animationStart
import tech.thdev.githubusersearch.util.inject
import tech.thdev.githubusersearch.util.loadFragment
import tech.thdev.githubusersearch.view.common.viewmodel.FilterStatusViewModel
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

    private val filterStatusViewModel: FilterStatusViewModel by lazy(LazyThreadSafetyMode.NONE) {
        FilterStatusViewModel::class.java.inject(this) {
            FilterStatusViewModel()
        }
    }

    private val fabOpenAnimation: Animation by lazy(LazyThreadSafetyMode.NONE) {
        AnimationUtils.loadAnimation(this, R.anim.fab_open)
    }

    private val fabCloseAnimation: Animation by lazy(LazyThreadSafetyMode.NONE) {
        AnimationUtils.loadAnimation(this, R.anim.fab_close)
    }

    private var isShowFloatingMenu: Boolean = false

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
        filterStatusViewModel.onUpdateFilterIcon = ::hideFloatingSubMenu
        initView()
    }

    private fun initView() {
        setSupportActionBar(toolbar)

        fab_sort.setOnClickListener {
            if (isShowFloatingMenu) {
                hideFloatingSubMenu()
            } else {
                showFloatingSubMenu()
            }
        }

        view_fab_container.setOnClickListener {
            hideFloatingSubMenu()
        }

        fab_sort_default.setOnClickListener {
            filterStatusViewModel.selectFilter(FilterStatusViewModel.FilterType.FILTER_SORT_DEFAULT)
        }
        fab_sort_name.setOnClickListener {
            filterStatusViewModel.selectFilter(FilterStatusViewModel.FilterType.FILTER_SORT_NAME)
        }
        fab_sort_date_of_registration.setOnClickListener {
            filterStatusViewModel.selectFilter(FilterStatusViewModel.FilterType.FILTER_SORT_DATE_OF_REGISTRATION)
        }
    }

    private fun showFloatingSubMenu() {
        isShowFloatingMenu = true
        fab_sort.changeButton(R.drawable.ic_close_white_24dp, true, R.color.colorFabBackgroundColor)
        view_sort_default.startOpenAnimation()
        view_sort_name.startOpenAnimation()
        view_sort_date_of_registration.startOpenAnimation()
    }

    private fun hideFloatingSubMenu(filterIcon: Int = R.drawable.ic_sort_numbers) {
        isShowFloatingMenu = false
        Log.e("TEMP", "filterIcon $filterIcon")
        fab_sort.changeButton(filterIcon, false, R.color.colorTransparent)

        view_sort_default.startCloseAnimation()
        view_sort_name.startCloseAnimation()
        view_sort_date_of_registration.startCloseAnimation()
    }

    private fun FloatingActionButton.changeButton(
            @DrawableRes drawableRes: Int, isClickable: Boolean, @ColorRes colorRes: Int) {
        this.setImageResource(drawableRes)
        view_fab_container.run {
            this.isClickable = isClickable
            this.isFocusable = isClickable
            setBackgroundResource(colorRes)
        }
    }

    private fun View.startOpenAnimation() {
        this.visibility = View.VISIBLE
        this.startAnimation(fabOpenAnimation)
    }

    /**
     * Close 애니메이션 시작
     */
    private fun View.startCloseAnimation() {
        this.animationStart(fabCloseAnimation, onEnd = {
            this.visibility = View.GONE
        })
    }

    private fun Fragment.loadFragment() {
        loadFragment(R.id.container, this)
    }

    override fun onBackPressed() {
        if (isShowFloatingMenu) {
            hideFloatingSubMenu()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)

        // Associate searchable configuration with the SearchView
        (getSystemService(Context.SEARCH_SERVICE) as SearchManager).run {
            (menu?.findItem(R.id.menu_search)?.actionView as SearchView).run {
                queryHint = getString(R.string.search_hint)
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
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
