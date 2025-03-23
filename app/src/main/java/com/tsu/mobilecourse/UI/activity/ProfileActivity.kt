package com.tsu.mobilecourse.UI.activity

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.tsu.mobilecourse.R
import com.tsu.mobilecourse.UI.fragment.BioCardFragment
import com.tsu.mobilecourse.UI.fragment.NavigationFragment
import com.tsu.mobilecourse.UI.viewmodel.ProfileUiState
import com.tsu.mobilecourse.UI.viewmodel.ProfileViewModel
import com.tsu.mobilecourse.data.db.TestEntity
import com.tsu.mobilecourse.data.model.ProfileModel
import com.tsu.mobilecourse.data.model.TestModel
import com.tsu.mobilecourse.data.repository.FakeProfileRepository
import com.tsu.mobilecourse.databinding.ActivityProfileBinding
import com.tsu.mobilecourse.databinding.ComponentTestBinding
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity(){

    private lateinit var binding: ActivityProfileBinding;
    private lateinit var componentBinding: ComponentTestBinding


    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModel.Factory(applicationContext)
    }

    private lateinit var pieChart: PieChart
    private lateinit var additionalBio: View
    private lateinit var fragmentsContainer: LinearLayout
    private lateinit var bioOverlay: ConstraintLayout
    private lateinit var interestsOverlay: ConstraintLayout
    private lateinit var infoOverlay: ConstraintLayout
    private lateinit var moreBioButton: ImageButton
    private lateinit var moreTestButton: ImageButton
    private lateinit var additionalText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        componentBinding = ComponentTestBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        initViews()
        setupListeners()
        observeViewModel()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.navContainer.id, NavigationFragment())
                .commit()
        }

    }

    private fun initViews() {
        pieChart = binding.pieChart
        additionalBio = binding.additionalBio
        fragmentsContainer = binding.fragmentsContainer
        bioOverlay = binding.bioOverlay.overlay
        interestsOverlay = binding.interestsOverlay.overlay
        infoOverlay = binding.infoOverlay.overlay
        moreBioButton = binding.moreBio
        moreTestButton = componentBinding.moreTest
        additionalText = componentBinding.additionalText
    }

    private fun setupListeners() {
        moreBioButton.setOnClickListener {
            viewModel.toggleAdditionalBio()
        }

        moreTestButton.setOnClickListener {
            viewModel.toggleAdditionalTest()
        }

        binding.bioOverlay.close.setOnClickListener {
            viewModel.toggleBioOverlay()
        }

        val bioSaveButton = binding.bioOverlay.saveBio
        bioSaveButton.setOnClickListener {
            val bioText = binding.bioOverlay.editTextText.text.toString()
            viewModel.updateBio(bioText)
        }

        binding.interestsOverlay.close.setOnClickListener {
            viewModel.toggleInterestsOverlay()
        }

        val interestsSaveButton = binding.interestsOverlay.saveInterests
        interestsSaveButton.setOnClickListener {
            viewModel.updateInterests()
        }

        val interestsContainer = binding.interestsOverlay.itemsContainer
        for (i in 0 until interestsContainer.childCount) {
            val interestItem = interestsContainer.getChildAt(i) as TextView
            interestItem.setOnClickListener {
                viewModel.toggleInterestSelection(interestItem.text.toString())
            }
        }

        binding.infoOverlay.close.setOnClickListener {
            viewModel.toggleInfoOverlay()
        }

        val infoSaveButton = binding.infoOverlay.saveInfo
        infoSaveButton.setOnClickListener {
            val country = binding.infoOverlay.country.text.toString()
            val city = binding.infoOverlay.city.text.toString()
            val worldview = binding.infoOverlay.worldview.text.toString()
            val education = binding.infoOverlay.education.text.toString()
            val languages = binding.infoOverlay.languages.text.toString()
                .split(",").map { it.trim() }.filter { it.isNotEmpty() }
            viewModel.updatePersonalInfo(country, city, worldview, education, languages)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        when (state) {
                            is ProfileUiState.Loading -> showLoading()
                            is ProfileUiState.Success -> {
                                hideLoading()
                                updateUI(state.profile, state.tests)
                            }
                            is ProfileUiState.Error -> showError(state.message)
                        }
                    }
                }

                launch {
                    viewModel.bioOverlayVisible.collect { isVisible ->
                        bioOverlay.visibility = if (isVisible) View.VISIBLE else View.GONE
                    }
                }

                launch {
                    viewModel.interestsOverlayVisible.collect { isVisible ->
                        interestsOverlay.visibility = if (isVisible) View.VISIBLE else View.GONE
                    }
                }

                launch {
                    viewModel.infoOverlayVisible.collect { isVisible ->
                        infoOverlay.visibility = if (isVisible) View.VISIBLE else View.GONE
                    }
                }

                launch {
                    viewModel.additionalBioVisible.collect { isVisible ->
                        additionalBio.visibility = if (isVisible) View.VISIBLE else View.GONE
                    }
                }

                launch {
                    viewModel.additionalTestVisible.collect { isVisible ->
                        additionalText.visibility = if (isVisible) View.VISIBLE else View.GONE
                    }
                }

                launch {
                    viewModel.selectedInterests.collect { selectedInterests ->
                        updateSelectedInterests(selectedInterests)
                    }
                }
            }
        }
    }

    private fun updateSelectedInterests(selectedInterests: List<String>) {
        val interestsContainer = binding.interestsOverlay.itemsContainer
        for (i in 0 until interestsContainer.childCount) {
            val interestItem = interestsContainer.getChildAt(i) as TextView
            interestItem.isActivated = selectedInterests.contains(interestItem.text.toString())
        }
    }

    private fun showLoading() {

    }

    private fun hideLoading() {

    }

    private fun showError(message: String) {

    }

    private fun updateUI(profile: ProfileModel, tests: List<TestEntity>) {
        binding.name.text = profile.name

        binding.cityAndAge.text = "${profile.age} лет" + if (profile.city.isNotBlank()) ", ${profile.city}" else ""

        tests.forEach { test ->
            val itemBinding = ComponentTestBinding.inflate(layoutInflater)
            itemBinding.testName.text = test.name
            itemBinding.shortText.text = test.shortDescription
            itemBinding.additionalText.text = test.result

            binding.testsContainer.addView(itemBinding.root)
        }

        setupPieChart(profile.completedProfile)

        updateBioFragments()
    }

    private fun setupPieChart(completionPercentage: Int) {
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(completionPercentage.toFloat(), ""))
        entries.add(PieEntry((100 - completionPercentage).toFloat(), ""))

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = listOf(Color.parseColor("#AE86DF"), Color.parseColor("#DBC6FD"))
        dataSet.setDrawValues(false)

        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.invalidate()
    }

    private fun updateBioFragments() {
        fragmentsContainer.removeAllViews()

        val infoFragment = BioCardFragment.newInstance(
            R.drawable.info,
            "Информация",
            "Заполните анкету"
        )

        val bioFragment = BioCardFragment.newInstance(
            R.drawable.bio,
            "О себе",
            "Расскажите о себе"
        )

        val interestsFragment = BioCardFragment.newInstance(
            R.drawable.interests,
            "Интересы",
            "Добавьте интересы"
        )

        val testsFragment = BioCardFragment.newInstance(
            R.drawable.test,
            "Тесты",
            "Пройдите тесты"
        )

        supportFragmentManager.beginTransaction()
            .add(fragmentsContainer.id, infoFragment)
            .add(fragmentsContainer.id, bioFragment)
            .add(fragmentsContainer.id, interestsFragment)
            .add(fragmentsContainer.id, testsFragment)
            .commit()

        infoFragment.setOnButtonClickListener {
            viewModel.toggleInfoOverlay()
        }

        bioFragment.setOnButtonClickListener {
            viewModel.toggleBioOverlay()
        }

        interestsFragment.setOnButtonClickListener {
            viewModel.toggleInterestsOverlay()
        }
    }
}