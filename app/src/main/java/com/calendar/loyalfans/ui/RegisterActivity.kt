package com.calendar.loyalfans.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.request.RegistrationRequest
import com.calendar.loyalfans.utils.Common
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.buttonFacebookLogin
import kotlinx.android.synthetic.main.activity_login.etPassword
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : BaseActivity() {

    private val RC_SIGN_IN: Int = 1010
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setUpFB()
        setUpGmail()
    }

    fun onLogin(view: View) {
        onBackPressed()
    }

    fun onRegister(view: View) {
        if (checkValidation()) {
            val registrationRequest =
                RegistrationRequest(etEmail.text.toString(),
                    etUserName.text.toString(),
                    etPassword.text.toString(),
                    imei,
                    "1",
                    firebaseToken,
                    "2")
            registrationAPICallAndUpdateUI(registrationRequest)
        }
    }

    private fun checkValidation(): Boolean {
        if (etUserName.text.isEmpty()) {
            Common.showToast(this, getString(R.string.username_validation))
            return false
        } else if (etEmail.text.isEmpty() || !Common.checkEmail(etEmail.text.toString())) {
            Common.showToast(this, getString(R.string.valid_email))
            return false
        } else if (etPassword.text.isEmpty()) {
            Common.showToast(this, getString(R.string.password_validation))
            return false
        }
        return true
    }

    private fun setUpGmail() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()
        googleSignInClient.revokeAccess()
    }

    private fun setUpFB() {
        callbackManager = CallbackManager.Factory.create()
        auth = FirebaseAuth.getInstance()
        buttonFacebookLogin.setPermissions("email", "public_profile")
        buttonFacebookLogin.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.d("", "")
                }

                override fun onError(error: FacebookException) {
                    Log.d("", "")
                }
            })
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.d("", "")
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null && user.email != null) {
                        val registrationRequest =
                            RegistrationRequest(user.email.toString(),
                                user.displayName.toString(),
                                "",
                                imei, "1", firebaseToken, "1", user.uid.toString(), "")
                        registrationAPICallAndUpdateUI(registrationRequest)
                    } else {
                        Common.showToast(baseContext, "Unable to login.")
                    }
                } else {
                    Common.showToast(this@RegisterActivity, "Unable to login")
                }
            }
    }

    private fun registrationAPICallAndUpdateUI(registrationRequest: RegistrationRequest) {
        googleSignInClient.revokeAccess()
        LoginManager.getInstance().logOut();
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.registerNewUser(
            registrationRequest, true
        )
            .observe(this, {
                if (it.status) {
                    spHelper.saveLoginData(it)
                    val intent = Intent(this@RegisterActivity, MainActivity::
                    class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            })
    }


    fun onGoogleLogin(view: View) {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    if (currentUser != null && currentUser.email != null) {
                        val registrationRequest =
                            RegistrationRequest(currentUser.email.toString(),
                                currentUser.displayName.toString(),
                                "",
                                imei, "1", firebaseToken, "0", "", currentUser.uid.toString())
                        registrationAPICallAndUpdateUI(registrationRequest)
                    } else {
                        Common.showToast(baseContext, "Unable to login.")
                    }
                } else {
                    Common.showToast(baseContext, "Authentication failed.")
                }
            }
    }

    fun onFacebookLoginClick(view: View) {
        buttonFacebookLogin.performClick()
    }


}