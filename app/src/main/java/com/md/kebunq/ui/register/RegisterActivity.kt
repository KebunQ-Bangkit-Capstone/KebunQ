package com.md.kebunq.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.fragment.app.FragmentManager.TAG
import androidx.lifecycle.lifecycleScope
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.GraphRequest
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.md.kebunq.MainActivity
import com.md.kebunq.R
import com.md.kebunq.data.User
import com.md.kebunq.data.retrofit.ApiConfig
import com.md.kebunq.data.retrofit.ApiService
import com.md.kebunq.databinding.ActivityRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FacebookSdk.setClientToken(getString(R.string.facebook_client_token))
        FacebookSdk.sdkInitialize(applicationContext)

        callbackManager = CallbackManager.Factory.create()
        binding.btnFacebook.setOnClickListener{
            registerFacebook()

        }

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.btnRegister.setOnClickListener{
            registerUserNativeWithFirebase()
        }

        binding.signInButton.setOnClickListener {
            signIn()
        }
    }

//    private fun registerFacebook() {
//        LoginManager.getInstance().logInWithReadPermissions(
//            this,
//            listOf("public_profile", "email")
//        )
//
//        LoginManager.getInstance().registerCallback( callbackManager,
//            object : FacebookCallback<LoginResult> {
//                override fun onSuccess(result: LoginResult) {
//                    val accessToken = result.accessToken
//                    val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
//
//                    auth.signInWithCredential(credential)
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                val graphRequest = GraphRequest.newMeRequest(accessToken) { jsonObject, _ ->
//                                    val email = jsonObject?.getString("email")
//                                    startActivity(
//                                        Intent(
//                                            this@RegisterActivity,
//                                            MainActivity::class.java
//                                        )
//                                    )
//                                    finish()
//                                }
//                                val parameters = Bundle()
//                                parameters.putString("fields", "email")
//                                graphRequest.parameters = parameters
//                                graphRequest.executeAsync()
//                                Log.d(TAG, "signInWithCredential:success")
//                                val user = auth.currentUser
//                                updateUI(user)
//                            } else {
//                                Toast.makeText(this@RegisterActivity, "Register Gagal", Toast.LENGTH_SHORT).show()
//                                // If sign in fails, display a message to the user.
//                                Log.w(TAG, "signInWithCredential:failure", task.exception)
//                                Toast.makeText(baseContext, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show()
//                                updateUI(null)
//                            }
//                        }
//                }
//
//                override fun onCancel() {
//                    Toast.makeText(this@RegisterActivity, "Register Dibatalkan", Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onError(error: FacebookException) {
//                    Toast.makeText(this@RegisterActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        )
//    }

    private fun registerFacebook() {
        // Memulai proses login Facebook
        LoginManager.getInstance().logInWithReadPermissions(
            this,
            listOf("public_profile", "email")
        )

        // Mengatur callback
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    val accessToken = result.accessToken
                    val credential = FacebookAuthProvider.getCredential(accessToken.token)

                    // Masuk ke Firebase dengan kredensial Facebook
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "signInWithCredential:success")

                                // Mengambil informasi pengguna dari Facebook Graph API
                                val graphRequest = GraphRequest.newMeRequest(accessToken) { jsonObject, _ ->
                                    try {
                                        val email = jsonObject?.optString("email") ?: "Email not found"
                                        val name = jsonObject?.optString("name") ?: "Name not found"

                                        // Simpan data pengguna ke API jika diperlukan
                                        val user = auth.currentUser
                                        sendUserDataToApi(
                                            username = name,
                                            userId = user?.uid ?: "",
                                            userEmail = email
                                        )

                                        // Beralih ke MainActivity
                                        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                                        finish()
                                    } catch (e: Exception) {
                                        Log.e(TAG, "Error parsing Facebook user data", e)
                                        Toast.makeText(
                                            this@RegisterActivity,
                                            "Error retrieving Facebook user data",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                val parameters = Bundle()
                                parameters.putString("fields", "email,name")
                                graphRequest.parameters = parameters
                                graphRequest.executeAsync()
                            } else {
                                Log.w(TAG, "signInWithCredential:failure", task.exception)
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }

                override fun onCancel() {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Facebook login cancelled",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError(error: FacebookException) {
                    Log.e(TAG, "Facebook login error: ${error.message}", error)
                    Toast.makeText(
                        this@RegisterActivity,
                        "Error during Facebook login: ${error.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }


    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun registerUserNativeWithFirebase() {
        val email = binding.inputEmail.text.toString()
        val password = binding.inputPassword.text.toString()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val user = auth.currentUser
                    val firebaseUser = auth.currentUser
                    val userId = firebaseUser?.uid ?: ""
                    val userEmail = firebaseUser?.email ?: ""
                    Log.d(TAG, "createUserWithEmail:success")
                    val username = binding.inputUsername.text.toString()
                    sendUserDataToApi(username, userId, userEmail)
                    updateUI(user)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun sendUserDataToApi(username: String, userId: String?, userEmail: String?) {
        val user = User(userId.toString(), userEmail.toString(), username)
        val apiService = ApiConfig.getApiService()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.createUser(user)
                Log.d(TAG, "User data sent to API: $response")
            } catch (e: Exception) {
                Log.e(TAG, "Error sending user data to API", e)
            }
        }
    }


    private fun signIn() {
        val credentialManager = CredentialManager.create(this) //import from androidx.CredentialManager

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.your_web_client_id)) //from https://console.firebase.google.com/project/firebaseProjectName/authentication/providers
            .build()

        val request = GetCredentialRequest.Builder() //import from androidx.CredentialManager
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    //import from androidx.CredentialManager
                    request = request,
                    context = this@RegisterActivity,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) { //import from androidx.CredentialManager
                Log.d("Error", e.message.toString())
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract id to validate and authenticate on your server.
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user: FirebaseUser? = auth.currentUser
                    val userId = user?.uid ?: ""
                    val userEmail = user?.email ?: ""
                    val username = user?.displayName ?: ""
                    sendUserDataToApi(username, userId, userEmail)
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}