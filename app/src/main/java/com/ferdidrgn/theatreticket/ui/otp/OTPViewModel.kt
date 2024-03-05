package com.ferdidrgn.theatreticket.ui.otp

import android.app.Activity
import android.os.CountDownTimer
import android.widget.EditText
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.base.BaseViewModel
import com.ferdidrgn.theatreticket.enums.Roles
import com.ferdidrgn.theatreticket.repository.UserFirebaseQueries
import com.ferdidrgn.theatreticket.tools.*
import com.ferdidrgn.theatreticket.tools.helpers.LiveEvent
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class OTPViewModel @Inject constructor(
    private val userFirebaseQueries: UserFirebaseQueries
) : BaseViewModel() {
    var firebaseAuth = FirebaseAuth.getInstance()
    val timeOutSecond = 60L
    var verificationCode = LiveEvent<String?>()
    lateinit var forceResendingToken: PhoneAuthProvider.ForceResendingToken

    val phoneNumber = MutableStateFlow("")
    val otp = MutableStateFlow("")

    val sendOtp = LiveEvent<Boolean?>()
    val sendButtonClickable = MutableStateFlow(true)
    val resendClick = LiveEvent<Boolean?>()
    val goToUserProfile = LiveEvent<Boolean?>()
    val timerFinished = MutableStateFlow(false)
    val timerText = MutableStateFlow("")

    var time = 100000 // 1.40 minute
    lateinit var timer: CountDownTimer

    fun checkPhoneNumber() {
        var isRequiredFieldsDone = true
        if (phoneNumber.value.removeWhiteSpace().length < 12)
            isRequiredFieldsDone = false

        if (isRequiredFieldsDone) {
            sendOtp.postValue(true)
            sendButtonClickable.value = false
        } else
            errorMessage.postValue(message(R.string.error_phone_little))
    }

    fun onSendCode() {
        checkPhoneNumber()
    }

    fun onResendCode() {
        resendClick.postValue(true)
    }

    fun startTime(activity: Activity, isResend: Boolean) {
        ioScope {
            timerFinished.value = false
        }
        timer = object : CountDownTimer(time.toLong(), 1000) {
            override fun onTick(duration: Long) {
                val Mmin: Long = duration / 1000 / 60
                val Ssec: Long = duration / 1000 % 60
                if (Ssec < 10) {
                    timerText.value = "0$Mmin:0$Ssec"
                } else timerText.value = "0$Mmin:$Ssec"
            }

            override fun onFinish() {
                ioScope {
                    timerFinished.emit(true)
                    sendOtp(activity, isResend)
                }
            }
        }
        timer.start()
    }

    fun sendOtp(activity: Activity, isResend: Boolean) {
        mainScope {
            showLoading()
            val builder = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber.value.removeWhiteSpace()) // Phone number to verify
                .setTimeout(timeOutSecond, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(activity) // Activity (for callback binding)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        signIn(credential)
                        hideLoading()
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        errorMessage.postValue(message(R.string.error_auth_failed) +  "onVerificationFailed: ${e.message}")
                        hideLoading()
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        super.onCodeSent(verificationId, token)
                        verificationCode.postValue(verificationId)
                        forceResendingToken = token
                        showToast(message(R.string.code_send_success))
                        hideLoading()
                    }
                }) // OnVerificationStateChangedCallbacks

            if (isResend) {
                PhoneAuthProvider.verifyPhoneNumber(
                    builder.setForceResendingToken(forceResendingToken).build()
                )
                hideLoading()
            } else {
                PhoneAuthProvider.verifyPhoneNumber(builder.build())
                hideLoading()
            }
            hideLoading()
        }
    }

    fun signIn(credential: PhoneAuthCredential, view: EditText? = null) {
        mainScope {
            showLoading()

            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        ClientPreferences.inst.apply {
                            userID = firebaseAuth.currentUser?.uid.toString()
                            userPhone = phoneNumber.value.removeWhiteSpace()
                            isGoogleSignIn = false
                            isPhoneNumberSignIn = true
                            role = Roles.User.role
                            token =
                                firebaseAuth.currentUser?.getIdToken(true)?.result?.token.toString()
                        }
                        goToUserProfile.postValue(true)
                        hideLoading()
                    } else {
                        errorMessage.value = message(R.string.error_auth_failed)
                        sendOtp.postValue(false)
                        sendButtonClickable.value = true
                        view?.showKeyboard()
                        hideLoading()
                    }
                }
        }
    }

    override fun onCleared() {
        if (::timer.isInitialized) {
            timer.cancel()
        }
        super.onCleared()
    }
}