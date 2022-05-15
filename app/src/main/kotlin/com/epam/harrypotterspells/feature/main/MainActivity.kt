package com.epam.harrypotterspells.feature.main

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.epam.harrypotterspells.R
import com.epam.harrypotterspells.databinding.ActivityMainBinding
import com.epam.harrypotterspells.mvibase.MVIView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.subjects.PublishSubject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MVIView<MainIntent, MainViewState> {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = checkNotNull(_binding) { "Binding is not initialized" }

    private var navController: NavController? = null

    private val local by lazy { getString(R.string.local) }
    private val remote by lazy { getString(R.string.remote) }
    private val spellsFragmentLabel by lazy { getString(R.string.spells_label) }

    private val viewModel: MainViewModel by viewModels()
    private val disposables = CompositeDisposable()
    private val intentsSubject = PublishSubject.create<MainIntent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBinding()
        setContentView(binding.root)
        setToolbar()
        bindViewModel()
    }

    private fun setBinding() {
        _binding = ActivityMainBinding.inflate(layoutInflater)
    }

    private fun setToolbar() {
        setNavController()
        setOnDestinationChangedListener()
        setSwitch()
        setSearchView()
    }

    private fun setNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController.also {
            binding.toolbar.setupWithNavController(it, AppBarConfiguration(it.graph))
        }
    }

    private fun setOnDestinationChangedListener() = binding.toolbar.run {
        navController?.addOnDestinationChangedListener { _, destination, _ ->
            val isSpellsFragment = destination.label == spellsFragmentLabel
            menu.forEach { it.isVisible = isSpellsFragment }
        }
    }

    private fun setSwitch() {
        val switch = binding.toolbar.menu[SWITCH_INDEX].actionView as? SwitchCompat
        switch?.setTextColor(Color.BLACK)
        switch?.setOnCheckedChangeListener { _, checked ->
            intentsSubject.onNext(
                if (checked) MainIntent.SwitchSourceIntent.ToRemoteIntent
                else MainIntent.SwitchSourceIntent.ToLocalIntent
            )
        }
    }

    private fun setSearchView() {
        val searchView = binding.toolbar.menu[SEARCH_VIEW_INDEX].actionView as? SearchView
        searchView?.run {
            setOnSearchClickListener(getOnSearchClickListener())
            setOnQueryTextListener(getOnQueryTextListener())
            setOnCloseListener(getOnCloseListener())
        }
    }

    private fun getOnSearchClickListener() = View.OnClickListener {
        intentsSubject.onNext(MainIntent.SearchIntent.OpenIntent)
    }

    private fun getOnQueryTextListener() = object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(query: String): Boolean {
            intentsSubject.onNext(MainIntent.SearchIntent.QueryIntent(query))
            return false
        }

        override fun onQueryTextSubmit(p0: String?): Boolean {
            return false
        }
    }

    private fun getOnCloseListener() = SearchView.OnCloseListener {
        intentsSubject.onNext(MainIntent.SearchIntent.CloseIntent)
        false
    }

    private fun bindViewModel() {
        viewModel.processIntents(getIntents())
        disposables += viewModel.getStates().subscribe(::render)
    }

    override fun getIntents(): Observable<MainIntent> = intentsSubject.serialize()

    override fun render(state: MainViewState) {
        renderSwitch(state.isRemote)
        renderSearchView(state.isSearchClosed)
    }

    private fun renderSwitch(isRemote: Boolean) {
        val switch = binding.toolbar.menu[SWITCH_INDEX].actionView as? SwitchCompat
        switch?.isChecked = isRemote
        switch?.text = if (isRemote) remote else local
    }

    private fun renderSearchView(isSearchClosed: Boolean) {
        val searchView = binding.toolbar.menu[SEARCH_VIEW_INDEX].actionView as? SearchView
        searchView?.isIconified = isSearchClosed
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        disposables.dispose()
    }

    private companion object {
        private const val SWITCH_INDEX = 0
        private const val SEARCH_VIEW_INDEX = 1
    }
}
