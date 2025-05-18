package hu.oliverparoczai.arfolyamok.ui.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import hu.oliverparoczai.arfolyamok.MainActivity
import hu.oliverparoczai.arfolyamok.R
import hu.oliverparoczai.arfolyamok.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    private fun checkUserCredentials(email: String, password: String): Boolean {
        if(email != "" && password != "") {
            return true;
        }else{
            Toast.makeText(
                context,
                getString(R.string.authentication_nodata),
                Toast.LENGTH_SHORT,
            ).show()
            return false;
        }
    }

    private fun onLoginSuccess() {
        if (activity is MainActivity) {
            (activity as MainActivity).onLoginSuccess()
        }
    }


    interface LoginCallback {
        fun onLoginResult(success: Boolean)
    }

    private fun defaultLoginCallback(success: Boolean) {
        // Do nothing by default
    }

    private fun registerUser(
        email: String,
        password: String,
        callback: LoginCallback = object : LoginCallback {
            override fun onLoginResult(success: Boolean) {
                defaultLoginCallback(success)
            }
        }
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Register in success
                    val user = auth.currentUser
                    Toast.makeText(
                        context,
                        getString(R.string.registration_successful),
                        Toast.LENGTH_SHORT,
                    ).show()
                    callback.onLoginResult(true);
                } else {
                    // Register in failure
                    Toast.makeText(
                        context,
                        getString(R.string.registration_failed),
                        Toast.LENGTH_SHORT,
                    ).show()
                    callback.onLoginResult(false);
                }
            }
    }

    private fun loginUser(
        email: String,
        password: String,
        showSuccessToastMsg: Boolean,
        callback: LoginCallback = object : LoginCallback {
            override fun onLoginResult(success: Boolean) {
                defaultLoginCallback(success)
            }
        }
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    val user = auth.currentUser
                    if(showSuccessToastMsg){
                        Toast.makeText(
                            context,
                            getString(R.string.authentication_successful),
                            Toast.LENGTH_SHORT,
                            ).show();
                    }
                    callback.onLoginResult(true);
                    onLoginSuccess();
                } else {
                    // Sign in failure
                    Toast.makeText(
                        context,
                        getString(R.string.authentication_failed),
                        Toast.LENGTH_SHORT,
                    ).show();
                    callback.onLoginResult(false);
                }
            }
    }

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
            if(checkUserCredentials(email, password)){
                loginUser(email, password, true);
            }
        };

        binding.registerButton.setOnClickListener {
            val email = binding.emailField.text.toString()
            val password = binding.passwordField.text.toString();
            if (checkUserCredentials(email, password)) {
                registerUser(email, password, object : LoginCallback {
                    override fun onLoginResult(success: Boolean) {
                        if (success) {
                            Handler(Looper.getMainLooper()).postDelayed({
                                loginUser(email, password, false);
                            }, 200)
                        }
                    };
                });
            };
        };

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}