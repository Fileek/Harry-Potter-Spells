package com.epam.harrypotterspells.feature.main

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.forEach
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.epam.harrypotterspells.R
import com.epam.harrypotterspells.databinding.ActivityMainBinding
import com.epam.harrypotterspells.mvibase.MVIView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.subjects.PublishSubject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MVIView<MainIntent, MainViewState> {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = checkNotNull(_binding) { "Binding is not initialized" }
    private var switch: SwitchCompat? = null
    private var searchView: SearchView? = null

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
            val isQueryEmpty = searchView?.query.toString() == "" || searchView?.query == null
            val isSpellsFragment = destination.label == spellsFragmentLabel
            searchView?.isIconified = isQueryEmpty
            menu.forEach { it.isVisible = isSpellsFragment }
        }
    }

    private fun setSwitch() {
        switch = binding.toolbar.menu.findItem(R.id.switch_view).actionView as? SwitchCompat
        switch?.setTextColor(Color.BLACK)
        switch?.setOnCheckedChangeListener { _, checked ->
            intentsSubject.onNext(
                if (checked) MainIntent.SwitchSourceIntent.ToRemoteIntent
                else MainIntent.SwitchSourceIntent.ToLocalIntent
            )
        }
    }

    private fun setSearchView() {
        searchView = binding.toolbar.menu.findItem(R.id.search_view).actionView as? SearchView
        searchView?.run {
            setOnQueryTextListener(getOnQueryTextListener())
        }
    }

    private fun getOnQueryTextListener() = object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(query: String): Boolean {
            intentsSubject.onNext(MainIntent.SearchByQueryIntent(query))
            return false
        }

        override fun onQueryTextSubmit(p0: String?): Boolean {
            return false
        }
    }

    private fun bindViewModel() {
        viewModel.processIntents(getIntents())
        disposables += viewModel.getStates()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::render)
    }

    override fun getIntents(): Observable<MainIntent> = intentsSubject.serialize()

    override fun render(state: MainViewState) {
        switch?.isChecked = state.isRemote
        switch?.text = if (state.isRemote) remote else local
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        disposables.dispose()
    }
}
