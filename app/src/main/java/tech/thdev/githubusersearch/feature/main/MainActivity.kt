package tech.thdev.githubusersearch.feature.main

import android.annotation.SuppressLint
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tech.thdev.githubusersearch.R
import tech.thdev.githubusersearch.databinding.ActivityMainBinding
import tech.thdev.githubusersearch.domain.model.GitHubSortType
import tech.thdev.githubusersearch.util.animationStart

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val searchQueryViewModel by viewModels<SearchQueryViewModel>()

    private val filterStatusViewModel by viewModels<FilterStatusViewModel>()

    private val fabOpenAnimation: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.fab_open)
    }
    private val fabCloseAnimation: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.fab_close)
    }

    private var isShowFloatingMenu: Boolean = false

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val navigation: BottomNavigationView = binding.navigation

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        setSupportActionBar(binding.toolbar)
        navigation.setupWithNavController(navController)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                filterStatusViewModel.filterUiState
                    .collect {
                        hideFloatingSubMenu(filterIcon = it.icon)
                    }
            }
        }

        initView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
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
            filterStatusViewModel.selectSortType(GitHubSortType.FILTER_SORT_DEFAULT)
        }
        binding.includeFabSort.fabSortName.setOnClickListener {
            filterStatusViewModel.selectSortType(GitHubSortType.FILTER_SORT_NAME)
        }
        binding.includeFabSort.fabSortDateOfRegistration.setOnClickListener {
            filterStatusViewModel.selectSortType(GitHubSortType.FILTER_SORT_DATE_OF_REGISTRATION)
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

    @SuppressLint("ClickableViewAccessibility")
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
                        searchQueryViewModel.setSearchQuery(query)
                        return false
                    }

                    override fun onQueryTextChange(query: String?): Boolean {
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
