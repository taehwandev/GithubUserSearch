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
import android.view.Menu
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_fab_sort.*
import tech.thdev.githubusersearch.util.animationStart
import tech.thdev.githubusersearch.util.inject
import tech.thdev.githubusersearch.util.loadFragment
import tech.thdev.githubusersearch.util.rotationAnimationStart
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
        initView()
    }

    private fun initView() {
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

        }
        fab_sort_name.setOnClickListener {

        }
        fab_sort_score.setOnClickListener {

        }
    }

    private fun showFloatingSubMenu() {
        isShowFloatingMenu = true
        fab_sort.startRotationAnimation(R.drawable.ic_close_white_24dp, 90.0F, true, R.color.colorFabBackgroundColor)
        view_sort_default.startOpenAnimation()
        view_sort_name.startOpenAnimation()
        view_sort_score.startOpenAnimation()
    }

    private fun hideFloatingSubMenu() {
        isShowFloatingMenu = false
        fab_sort.startRotationAnimation(R.drawable.ic_sort, 0F, false, R.color.colorTransparent)

        view_sort_default.startCloseAnimation()
        view_sort_name.startCloseAnimation()
        view_sort_score.startCloseAnimation()
    }

    private fun FloatingActionButton.startRotationAnimation(
            @DrawableRes drawableRes: Int, rotation: Float,
            isClickable: Boolean, @ColorRes colorRes: Int) {
        this.setImageResource(drawableRes)
        rotationAnimationStart(rotation, 300, 10.0F) {
            view_fab_container.run {
                this.isClickable = isClickable
                this.isFocusable = isClickable
                setBackgroundResource(colorRes)
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
