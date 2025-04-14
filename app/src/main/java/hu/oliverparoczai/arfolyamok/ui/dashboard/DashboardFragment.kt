package hu.oliverparoczai.arfolyamok.ui.dashboard

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import hu.oliverparoczai.arfolyamok.databinding.FragmentDashboardBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import java.util.concurrent.TimeUnit

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    fun downloadXml(url: String): String? {
        // Configure the OkHttpClient with timeouts
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS) // Set connection timeout
            .readTimeout(30, TimeUnit.SECONDS)    // Set read timeout
            .writeTimeout(15, TimeUnit.SECONDS)   // Set write timeout
            .build()

        val request = Request.Builder().url(url).build()
        return try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    response.body?.string()
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            // Handle exceptions (e.g., timeout, network issues)
            e.printStackTrace() // Log the exception or handle it as needed
            null
        }
    }

    @Root(name = "items")
    data class Items(
        @field:ElementList(inline = true)
        var itemList: List<Item> = emptyList()
    )

    @Root(name = "item")
    data class Item(
        @field:Element(name = "name")
        var name: String = "",
        @field:Element(name = "value")
        var value: Int = 0
    )

    fun parseXml(xml: String): List<Item> {
        val serializer: Serializer = Persister()
        val items = serializer.read(Items::class.java, xml)
        return items.itemList
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }


        val url = "http://api.napiarfolyam.hu/?valuta=eur&bank=mnb&datum=20250401&datumend=20250414"
        GlobalScope.launch(Dispatchers.IO) {
            val xml = downloadXml(url)
            if (xml != null) {
                val items = parseXml(xml)
                withContext(Dispatchers.Main) {
                    setupChart(binding.chart, items)
                }
            }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupChart(chart: LineChart, items: List<Item>){
        chart.isHighlightPerDragEnabled = true

        chart.setDrawBorders(true)

        //candleStickChart.setBorderColor(resources.getColor(R.color.colorLightGray))

        val yAxis = chart.axisLeft
        val rightAxis = chart.axisRight
        yAxis.setDrawGridLines(true)
        rightAxis.setDrawGridLines(true)
        chart.requestDisallowInterceptTouchEvent(true)

        val xAxis = chart.xAxis

        xAxis.setDrawGridLines(true) // disable x axis grid lines
        xAxis.setDrawLabels(true)
        rightAxis.textColor = Color.WHITE
        yAxis.setDrawLabels(false)
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setAvoidFirstLastClipping(true)

        val l = chart.legend
        l.isEnabled = false

        val chartItems: ArrayList<Entry> = ArrayList()

        chartItems.add (
            Entry(
                0f,
                0f
            )
        )

        chartItems.add (
            Entry(
                1f,
                1f
            )
        )

        chartItems.add (
            Entry(
                2f,
                0f
            )
        )


        val set1 = LineDataSet(chartItems, "DataSet 1")
        set1.color = Color.rgb(80, 80, 80)
        set1.setDrawValues(false)


        // create a data object with the datasets
        val data = LineData(set1)


        // set data
        chart.data = data
        val desc = Description()
        desc.text = ""
        chart.description = desc
        chart.invalidate()
    }
}