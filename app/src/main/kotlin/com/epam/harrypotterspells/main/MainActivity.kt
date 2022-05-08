package com.epam.harrypotterspells.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.epam.harrypotterspells.R
import com.epam.harrypotterspells.databinding.ActivityMainBinding
import com.epam.harrypotterspells.ext.TAG
import com.epam.harrypotterspells.mvibase.MVIView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.subjects.BehaviorSubject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MVIView<MainIntent, MainViewState> {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = checkNotNull(_binding) { "Binding is not initialized" }
    private val viewModel: MainViewModel by viewModels()
    private val intentsSubject = BehaviorSubject.create<MainIntent>()
    private val local by lazy { getString(R.string.local) }
    private val remote by lazy { getString(R.string.remote) }
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBinding()
        setContentView(binding.root)
        setToolbar()
        bindViewModel()
        setSwitchListener()
    }

    private fun setBinding() {
        _binding = ActivityMainBinding.inflate(layoutInflater)
    }

    private fun setToolbar() {
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun bindViewModel() {
        viewModel.processIntents(getIntents())
        disposables += viewModel.getStates().subscribe(::render)
    }

    private fun setSwitchListener() {
        binding.repositorySwitch.setOnCheckedChangeListener { _, checked ->
            Log.v(TAG, "listener $checked")
            intentsSubject.onNext(
                if (checked) MainIntent.SwitchToRemoteIntent
                else MainIntent.SwitchToLocalIntent
            )
        }
    }

    override fun render(state: MainViewState) {
        Log.v(TAG, "render $state")
        binding.repositorySwitch.text = if (state.isRemote) remote else local
    }

    override fun getIntents(): Observable<MainIntent> = intentsSubject.serialize()

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
        _binding = null
    }
}
