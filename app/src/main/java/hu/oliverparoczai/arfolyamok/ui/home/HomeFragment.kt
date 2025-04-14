package hu.oliverparoczai.arfolyamok.ui.home

import android.graphics.Color
import android.graphics.Paint
import android.graphics.fonts.FontFamily
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import hu.oliverparoczai.arfolyamok.R
import hu.oliverparoczai.arfolyamok.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.textHome.text = getString(R.string.welcome_user)
        //val textView: TextView = binding.textHome
        //homeViewModel.text.observe(viewLifecycleOwner) {
        //    textView.text = it
        //}

        addChart()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addChart(){
        val candleStickChart: CandleStickChart = binding.chart
        candleStickChart.isHighlightPerDragEnabled = true

        candleStickChart.setDrawBorders(true)

        //candleStickChart.setBorderColor(resources.getColor(R.color.colorLightGray))

        val yAxis = candleStickChart.axisLeft
        val rightAxis = candleStickChart.axisRight
        yAxis.setDrawGridLines(true)
        rightAxis.setDrawGridLines(true)
        candleStickChart.requestDisallowInterceptTouchEvent(true)

        val xAxis = candleStickChart.xAxis

        xAxis.setDrawGridLines(true) // disable x axis grid lines
        xAxis.setDrawLabels(true)
        rightAxis.textColor = Color.WHITE
        yAxis.setDrawLabels(false)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setAvoidFirstLastClipping(true)

        val l = candleStickChart.legend
        l.isEnabled = false

        val yValsCandleStick: ArrayList<CandleEntry> = ArrayList()

        yValsCandleStick.add (
            CandleEntry(
                0f,
                225.0.toFloat(),
                219.84.toFloat(),
                224.94.toFloat(),
                221.07.toFloat()
            ))
        yValsCandleStick.add(
            CandleEntry(
                1f,
                228.35.toFloat(),
                222.57.toFloat(),
                223.52.toFloat(),
                226.41.toFloat()
            )
        )
        yValsCandleStick.add(
            CandleEntry(
                2f,
                226.84.toFloat(),
                222.52.toFloat(),
                225.75.toFloat(),
                223.84.toFloat()
            )
        )
        yValsCandleStick.add(
            CandleEntry(
                3f,
                222.95.toFloat(),
                217.27.toFloat(),
                222.15.toFloat(),
                217.88.toFloat()
            )
        )

        yValsCandleStick.add (
            CandleEntry(
                4f,
                225.0.toFloat(),
                219.84.toFloat(),
                224.94.toFloat(),
                221.07.toFloat()
            ))
        yValsCandleStick.add(
            CandleEntry(
                5f,
                228.35.toFloat(),
                222.57.toFloat(),
                223.52.toFloat(),
                226.41.toFloat()
            )
        )
        yValsCandleStick.add(
            CandleEntry(
                6f,
                226.84.toFloat(),
                222.52.toFloat(),
                225.75.toFloat(),
                223.84.toFloat()
            )
        )
        yValsCandleStick.add(
            CandleEntry(
                7f,
                222.95.toFloat(),
                217.27.toFloat(),
                222.15.toFloat(),
                217.88.toFloat()
            )
        )


        val set1 = CandleDataSet(yValsCandleStick, "DataSet 1")
        set1.color = Color.rgb(80, 80, 80)
        set1.shadowColor = Color.LTGRAY
        set1.shadowWidth = 0.8f
        set1.decreasingColor = Color.rgb(246,0,0) //red
        set1.decreasingPaintStyle = Paint.Style.FILL
        set1.increasingColor = Color.rgb(107,228,0) //green
        set1.increasingPaintStyle = Paint.Style.FILL
        set1.neutralColor = Color.LTGRAY
        set1.setDrawValues(false)


// create a data object with the datasets
        val data = CandleData(set1)


// set data
        candleStickChart.data = data
        val desc = Description()
        desc.text = ""
        candleStickChart.description = desc
        candleStickChart.invalidate()
    }

}