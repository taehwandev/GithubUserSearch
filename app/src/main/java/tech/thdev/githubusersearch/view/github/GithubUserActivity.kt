package tech.thdev.githubusersearch.view.github

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
import android.view.Menu
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_fab_sort.*
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.data.source.search.GithubSearchRepository
import tech.thdev.githubusersearch.db.GithubRoomDatabase
import tech.thdev.githubusersearch.network.RetrofitFactory
import tech.thdev.githubusersearch.util.animationStart
import tech.thdev.githubusersearch.util.inject
import tech.thdev.githubusersearch.util.loadFragment
import tech.thdev.githubusersearch.view.github.viewmodel.FilterStatusViewModel
import tech.thdev.githubusersearch.view.github.viewmodel.SearchQueryViewModel


class GithubUserActivity : AppCompatActivity() {

    private val githubSearchRepository: GithubSearchRepository
        get() = GithubSearchRepository.getInstance(
                githubApi = RetrofitFactory.githubApi,
                githubRoomDatabase = GithubRoomDatabase.getInstance(application))

    private val searchFragment: Fragment by lazy {
        GithubUserFragment.getInstance(GithubUserFragment.VIEW_TYPE_SEARCH, githubSearchRepository)
    }

    private val likeFragment: Fragment by lazy {
        GithubUserFragment.getInstance(GithubUserFragment.VIEW_TYPE_LIKED, githubSearchRepository)
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

    private lateinit var fabOpenAnimation: Animation
    private lateinit var fabCloseAnimation: Animation

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

        fabOpenAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_open)
        fabCloseAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_close)

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

        view_fab_container.run {
            this.isClickable = false
            this.isFocusable = false
            setOnTouchListener { _, _ ->
                false
            }
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
                setOnTouchListener { _, _ ->
                    if (isClickable) {
                        hideFloatingSubMenu()
                        true
                    } else {
                        false
                    }
                }
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
                        searchQueryViewModel.setSearchQuery(query, true)
                        return false
                    }

                    override fun onQueryTextChange(query: String?): Boolean {
                        searchQueryViewModel.setSearchQuery(query)
                        return false
                    }
                })
                setOnCloseListener {
                    searchQueryViewModel.setSearchQuery("")
                    false
                }
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()

        githubSearchRepository.clear()
    }
}
