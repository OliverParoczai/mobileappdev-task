package hu.oliverparoczai.arfolyamok.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import hu.oliverparoczai.arfolyamok.R
import hu.oliverparoczai.arfolyamok.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val accountViewModel =
            ViewModelProvider(this).get(LoginViewModel::class.java)

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = Firebase.auth

        val currentUser = auth.currentUser
        //if (currentUser != null) {

        //}

        binding.loginButton.setOnClickListener {
            val email = binding.emailField.text.toString()
            val password = binding.passwordField.text.toString();
            if(email != "" && password != "") {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success
                            val user = auth.currentUser
                            Toast.makeText(
                                context,
                                getString(R.string.authentication_successful),
                                Toast.LENGTH_SHORT,
                            ).show()

                        } else {
                            // Sign in failure
                            Toast.makeText(
                                context,
                                getString(R.string.authentication_failed),
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }else{
                Toast.makeText(
                    context,
                    getString(R.string.authentication_nodata),
                    Toast.LENGTH_SHORT,
                ).show()
            }
        };

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}