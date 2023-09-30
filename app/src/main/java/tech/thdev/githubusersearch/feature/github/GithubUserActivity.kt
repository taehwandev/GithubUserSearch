package tech.thdev.githubusersearch.feature.github

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.data.GitHubSearchRepositoryImpl
import tech.thdev.githubusersearch.database.GitHubRoomDatabase
import tech.thdev.githubusersearch.databinding.ActivityMainBinding
import tech.thdev.githubusersearch.network.RetrofitFactory
import tech.thdev.githubusersearch.util.animationStart
import tech.thdev.githubusersearch.util.loadFragment
import tech.thdev.githubusersearch.feature.github.viewmodel.FilterStatusViewModel
import tech.thdev.githubusersearch.feature.github.viewmodel.SearchQueryViewModel


class GithubUserActivity : AppCompatActivity() {

    private val gitHubSearchRepository by lazy {
        GitHubSearchRepositoryImpl(
            githubApi = RetrofitFactory.githubApi,
            githubUserDao = GitHubRoomDatabase.getInstance(application).githubUserDao(),
        )
    }

    private val searchFragment: Fragment by lazy {
        GithubUserFragment.getInstance(GithubUserFragment.VIEW_TYPE_SEARCH, gitHubSearchRepository)
    }

    private val likeFragment: Fragment by lazy {
        GithubUserFragment.getInstance(GithubUserFragment.VIEW_TYPE_LIKED, gitHubSearchRepository)
    }

    private val searchQueryViewModel by viewModels<SearchQueryViewModel>()

    private val filterStatusViewModel by viewModels<FilterStatusViewModel>()

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

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        fabOpenAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_open)
        fabCloseAnimation = AnimationUtils.loadAnimation(this, R.anim.fab_close)

        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        searchFragment.loadFragment()
        filterStatusViewModel.onUpdateFilterIcon = ::hideFloatingSubMenu
        initView()
    }

    private fun initView() {
        setSupportActionBar(binding.toolbar)

        binding.includeFabSort.fabSort.setOnClickListener {
            if (isShowFloatingMenu) {
                hideFloatingSubMenu()
            } else {
                showFloatingSubMenu()
            }
        }

        binding.includeFabSort.viewFabContainer.run {
            this.isClickable = false
            this.isFocusable = false
            setOnTouchListener { _, _ ->
                false
            }
        }

        binding.includeFabSort.fabSortDefault.setOnClickListener {
            filterStatusViewModel.selectFilter(FilterStatusViewModel.FilterType.FILTER_SORT_DEFAULT)
        }
        binding.includeFabSort.fabSortName.setOnClickListener {
            filterStatusViewModel.selectFilter(FilterStatusViewModel.FilterType.FILTER_SORT_NAME)
        }
        binding.includeFabSort.fabSortDateOfRegistration.setOnClickListener {
            filterStatusViewModel.selectFilter(FilterStatusViewModel.FilterType.FILTER_SORT_DATE_OF_REGISTRATION)
        }
    }

    private fun showFloatingSubMenu() {
        isShowFloatingMenu = true

        binding.includeFabSort.run {
            fabSort.changeButton(R.drawable.ic_close_white_24dp, true, R.color.colorFabBackgroundColor)
            viewSortDefault.startOpenAnimation()
            viewSortName.startOpenAnimation()
            viewSortDateOfRegistration.startOpenAnimation()
        }
    }

    private fun hideFloatingSubMenu(filterIcon: Int = R.drawable.ic_sort_numbers) {
        isShowFloatingMenu = false
        binding.includeFabSort.run {
            fabSort.changeButton(filterIcon, false, R.color.colorTransparent)

            viewSortDefault.startCloseAnimation()
            viewSortName.startCloseAnimation()
            viewSortDateOfRegistration.startCloseAnimation()
        }
    }

    private fun FloatingActionButton.changeButton(
        @DrawableRes drawableRes: Int, isClickable: Boolean, @ColorRes colorRes: Int
    ) {
        this.setImageResource(drawableRes)
        binding.includeFabSort.viewFabContainer.run {
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
}
