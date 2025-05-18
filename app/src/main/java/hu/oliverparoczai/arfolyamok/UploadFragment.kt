package hu.oliverparoczai.arfolyamok

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import hu.oliverparoczai.arfolyamok.databinding.FragmentUploadBinding
import hu.oliverparoczai.arfolyamok.model.CurrencyRate
import hu.oliverparoczai.arfolyamok.model.FirebaseHelper
import android.widget.Toast

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class UploadFragment : Fragment() {

    private var _binding: FragmentUploadBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUploadBinding.inflate(inflater, container, false)

        binding.goBackBtn.setOnClickListener{
            activity?.finish()
        }

        binding.uploadCurrencyBtn.setOnClickListener{
            val firebaseHelper = FirebaseHelper()
            val currencyCode = binding.currencyCode.text.toString().trim()
            val currencyRateString = binding.currencyRate.text.toString().trim()

            if (currencyCode.isNotEmpty() && currencyRateString.isNotEmpty()) {
                try {
                    // Convert the currency rate to Double
                    val currencyRate = currencyRateString.toDouble()

                    // Create a CurrencyRate object
                    val currencyRateObject = CurrencyRate(
                        currencyCode = currencyCode,
                        rate = currencyRate,
                        timestamp = System.currentTimeMillis()
                    )

                    // Add the currency rate to Firebase
                    firebaseHelper.addCurrencyRate(currencyRateObject)

                    // Show a success Toast message
                    Toast.makeText(context, "Currency rate added successfully!", Toast.LENGTH_SHORT).show()

                } catch (e: NumberFormatException) {
                    // Handle the case where the currency rate is not a valid number
                    Toast.makeText(context, "Invalid currency rate: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Handle the case where the currency code or rate is empty
                Toast.makeText(context, "Currency code or rate cannot be empty.", Toast.LENGTH_SHORT).show()
            }


        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}