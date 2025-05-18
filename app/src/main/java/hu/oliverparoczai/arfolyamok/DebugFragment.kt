package hu.oliverparoczai.arfolyamok

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import hu.oliverparoczai.arfolyamok.databinding.FragmentDebugBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class DebugFragment : Fragment() {

    private var _binding: FragmentDebugBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDebugBinding.inflate(inflater, container, false)


        binding.goBackBtn2.setOnClickListener{
            activity?.finish()
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