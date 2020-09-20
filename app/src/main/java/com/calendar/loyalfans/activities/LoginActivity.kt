package com.calendar.loyalfans.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.calendar.loyalfans.R
import com.calendar.loyalfans.model.request.LoginRequest
import com.calendar.loyalfans.retrofit.BaseViewModel
import com.calendar.loyalfans.utils.Common
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity() {
    private val RC_SIGN_IN: Int = 1010
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setUpGmail()
        setUpFB()
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

    fun onLogin(view: View) {
        if (checkValidation()) {
            val loginRequest =
                LoginRequest(etEmailOrUserName.text.toString(), etPassword.text.toString(),
                    imei, "1", firebaseToken, "2")
            loginAPICallAndUpdateUI(loginRequest)
        }
    }

    fun onRegister(view: View) {
        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
    }

    fun onForgotPassword(view: View) {
        startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
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
                        val loginRequest =
                            LoginRequest(user.email.toString(), "",
                                imei, "1", firebaseToken, "1", user.uid.toString(), "")
                        loginAPICallAndUpdateUI(loginRequest)
                    } else {
                        Common.showToast(this@LoginActivity, "Unable to login")
                    }
                } else {
                    Common.showToast(this@LoginActivity, "Unable to login")
                }
            }
    }

    private fun loginAPICallAndUpdateUI(loginRequest: LoginRequest) {
        googleSignInClient.revokeAccess()
        LoginManager.getInstance().logOut();
        val baseViewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        baseViewModel.login(
            loginRequest, true
        )
            .observe(this, {
                if (it.status) {
                    spHelper.saveUserPassword(loginRequest.password)
                    spHelper.saveLoginData(it)
                    val intent = Intent(this@LoginActivity, MainActivity::
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
                        val loginRequest =
                            LoginRequest(currentUser.email.toString(), "",
                                imei, "1", firebaseToken, "0", "", currentUser.uid.toString())
                        loginAPICallAndUpdateUI(loginRequest)
                    } else {
                        Common.showToast(baseContext, "Unable to login")
                    }
                } else {
                    Common.showToast(baseContext, "Authentication failed.")
                }
            }
    }

    fun onFacebookLoginClick(view: View) {
        buttonFacebookLogin.performClick()
    }

    private fun checkValidation(): Boolean {
        if (etEmailOrUserName.text.isEmpty()) {
            Common.showToast(this, getString(R.string.valid_email_username))
            return false
        } else if (etPassword.text.isEmpty()) {
            Common.showToast(this, getString(R.string.password_validation))
            return false
        }
        return true
    }


}