package com.tsu.mobilecourse.UI.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.tsu.mobilecourse.UI.fragment.BioCardFragment
import com.tsu.mobilecourse.UI.fragment.NavigationFragment
import com.tsu.mobilecourse.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity(), BioCardFragment.OnButtonClickListener {

    private lateinit var binding: ActivityProfileBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initPieChart()

        val cardFields = listOf(
            mapOf(
                "blockImg" to com.tsu.mobilecourse.R.drawable.bio,
                "blockName" to "Личное био",
                "blockDescription" to "Расскажите о себе",
                "id" to "bio"
            ),
            mapOf(
                "blockImg" to com.tsu.mobilecourse.R.drawable.interests,
                "blockName" to "Интересы",
                "blockDescription" to "Расскажите о своих хобби",
                "id" to "interests"
            ),
            mapOf(
                "blockImg" to com.tsu.mobilecourse.R.drawable.info,
                "blockName" to "Информация",
                "blockDescription" to "Заполните анкету",
                "id" to "info"
            ),
            mapOf(
                "blockImg" to com.tsu.mobilecourse.R.drawable.test,
                "blockName" to "Тесты",
                "blockDescription" to "Пройдите тесты",
                "id" to "test"
            )
        )

        if (savedInstanceState == null) {
            for (card in cardFields) {
                val fragment = BioCardFragment()
                val bundle = Bundle().apply {
                    putInt("blockImg", card["blockImg"] as Int)
                    putString("blockName", card["blockName"] as String)
                    putString("blockDescription", card["blockDescription"] as String)
                    putString("id", card["id"] as String)
                }
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .add(binding.fragmentsContainer.id, fragment)
                    .commit()
            }
        }

        binding.moreTest.setOnClickListener {
            if(binding.additionalText.visibility == View.GONE){
                binding.additionalText.visibility = View.VISIBLE
            } else {
                binding.additionalText.visibility = View.GONE
            }
            val rotation = ObjectAnimator.ofFloat(binding.moreTest, "rotation", binding.moreTest.rotation, binding.moreTest.rotation + 180f)
            rotation.duration = 300
            rotation.start()
        }

        binding.moreBio.setOnClickListener{
            if(binding.additionalBio.visibility == View.GONE){
                binding.additionalBio.visibility = View.VISIBLE
            } else {
                binding.additionalBio.visibility = View.GONE
            }
            val rotation = ObjectAnimator.ofFloat(binding.moreBio, "rotation", binding.moreBio.rotation, binding.moreBio.rotation + 180f)
            rotation.duration = 300
            rotation.start()
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.navContainer.id, NavigationFragment())
                .commit()
        }
    }

    override fun onBioButtonClicked(buttonId: String?) {
        when (buttonId) {
            "bio" -> {
                binding.bioOverlay.overlay.visibility = View.VISIBLE
                binding.bioOverlay.close.setOnClickListener{
                    binding.bioOverlay.overlay.visibility = View.GONE
                }
            }
            "interests" -> {
                binding.interestsOverlay.overlay.visibility = View.VISIBLE
                binding.interestsOverlay.close.setOnClickListener{
                    binding.interestsOverlay.overlay.visibility = View.GONE
                }
                for (i in 0 until binding.interestsOverlay.itemsContainer.childCount){
                    val child = binding.interestsOverlay.itemsContainer.getChildAt(i)
                    if (child is TextView){
                        child.setOnClickListener {
                            it.isActivated = !it.isActivated
                        }
                    }
                }
            }
            "info" -> {
                binding.infoOverlay.overlay.visibility = View.VISIBLE
                binding.infoOverlay.close.setOnClickListener{
                    binding.infoOverlay.overlay.visibility = View.GONE
                }
                val languages = arrayOf("Русский", "Английский", "Немецкий", "Французский")
                binding.infoOverlay.languages.setOnClickListener {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Выберите язык")

                    builder.setItems(languages) { dialog, which ->
                        val selectedLanguage = languages[which]
                        binding.infoOverlay.languages.setText(selectedLanguage)
                    }
                    builder.show()
                }
                val education = arrayOf("Высшее", "Среднее профессиональное", "Среднее общее", "Основное общее")
                binding.infoOverlay.education.setOnClickListener {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Выберите язык")

                    builder.setItems(education) { dialog, which ->
                        val selectedEducation = education[which]
                        binding.infoOverlay.education.setText(selectedEducation)
                    }
                    builder.show()
                }
            }
            "test" -> {
                val intent = Intent(this, TestsActivity()::class.java)
                startActivity(intent)
            }
            else -> {

            }
        }
    }

    private fun initPieChart(){
        val pieChart: PieChart = binding.pieChart

        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(75f, ""))
        entries.add(PieEntry(25f, ""))

        val dataSet = PieDataSet(entries, "")

        dataSet.setColors(
            Color.parseColor("#AE86DF"),
            Color.parseColor("#DBC6FD")
        )

        dataSet.sliceSpace = 0f

        val pieData = PieData(dataSet)

        pieChart.data = pieData
        pieChart.invalidate()

        pieChart.isDrawHoleEnabled = true
        pieChart.holeRadius = 80f
        pieChart.transparentCircleRadius = 35f
        pieChart.setDrawCenterText(true)

        pieChart.centerText = "75%"

        pieChart.setCenterTextSize(20f)

        pieChart.setDescription(null)

        pieChart.legend.isEnabled = false
        pieChart.setDrawEntryLabels(false)
    }
}