package com.ferdidrgn.theatreticket.util

import com.ferdidrgn.theatreticket.util.helpers.PreferencesManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ClientPreferences : PreferencesManager() {

    companion object {
        lateinit var inst: ClientPreferences
    }

    var baseUrl: String?
        get() = getString(BASE_URL)
        set(value) {
            putString(BASE_URL, value)
        }

    var guestToken: String?
        get() = getString(GUEST_TOKEN, "")
        set(token) = putString(GUEST_TOKEN, token)

    var token: String?
        get() = getString(TOKEN, null)
        set(token) = putString(TOKEN, token)

    var FCMtoken: String?
        get() = getString(FCMTOKEN, null)
        set(token) = putString(FCMTOKEN, token)

    //for Onboarding
    var isFirstLaunch: Boolean?
        get() = getBooleanValue(IS_FIRST_LUNCH, true)
        set(isFirstLaunch) {
            putBoolean(IS_FIRST_LUNCH, isFirstLaunch ?: true)
        }

    var userID: String?
        get() = getString(USER_ID, null)
        set(value) = putString(USER_ID, value)

    var userFullName: String?
        get() = getString(USER_FULLNAME, null)
        set(token) = putString(USER_FULLNAME, token)

    var userFirstName: String?
        get() = getString(USER_FIRSTNAME, null)
        set(token) = putString(USER_FIRSTNAME, token)

    var userLastName: String?
        get() = getString(USER_LASTNAME, null)
        set(token) = putString(USER_LASTNAME, token)

    var userAge: String?
        get() = getString(USER_AGE, null)
        set(token) = putString(USER_AGE, token)

    var userPhone: String?
        get() = getString(PHONE_NUMBER, null)
        set(token) = putString(PHONE_NUMBER, token)

    var countryCode: String?
        get() = getString(COUNTRY_CODE, null)
        set(token) = putString(COUNTRY_CODE, token)

    var userEmail: String?
        get() = getString(USER_EMAIL, null)
        set(token) = putString(USER_EMAIL, token)

    var userPhotoUrl: String?
        get() = getString(USER_PHOTO_URL, null)
        set(token) = putString(USER_PHOTO_URL, token)

    var role: String?
        get() = getString(ROLE, null)
        set(token) = putString(ROLE, token)

    var isGoogleSignIn: Boolean?
        get() = getBooleanValue(IS_GOOGLE_SIGN_IN, false)
        set(isGoogleSignIn) {
            putBoolean(IS_GOOGLE_SIGN_IN, isGoogleSignIn ?: false)
        }

    var isPhoneNumberSignIn: Boolean?
        get() = getBooleanValue(IS_PHONE_NUMBER_SIGN_IN, false)
        set(isPhoneNumberSignIn) {
            putBoolean(IS_PHONE_NUMBER_SIGN_IN, isPhoneNumberSignIn ?: false)
        }

    var isBlankUserInfo: Boolean?
        get() = getBooleanValue(IS_BLANK_USER_INFO, true)
        set(isFillUserInfo) {
            putBoolean(IS_BLANK_USER_INFO, isFillUserInfo ?: true)
        }

    var connection: String?
        get() = getString(CONNECTION, null)
        set(token) = putString(CONNECTION, token)

    var language: String
        get() = getString(LANGUAGE, Languages.TURKISH.language).toString()
        set(token) = putString(LANGUAGE, token)

    var contextLanguage: String
        get() = getString(CONTEXT_LANGUAGE, ContextLanguages.TURKISH.language).toString()
        set(token) = putString(CONTEXT_LANGUAGE, token)

    var isDarkMode: Boolean?
        get() = getBooleanValue(IS_DARK_MODE, false)
        set(isDarkMode) {
            putBoolean(IS_DARK_MODE, isDarkMode ?: false)
        }

    var reviewStatus: Boolean
        get() = getBooleanValue(REVIEW_STATUS, false)
        set(value) {
            putBoolean(REVIEW_STATUS, value)
        }

    var reviewCounter: Int
        get() = getInt(REVIEW_COUNT)
        set(value) {
            putInt(REVIEW_COUNT, value ?: 0)
        }

    var shouldFirebaseAouthOn: Boolean?
        get() = getBooleanValue(SHOULD_FIREBASE_AOUTH_BE_ON_ANDROID)
        set(value) {
            putBoolean(SHOULD_FIREBASE_AOUTH_BE_ON_ANDROID, value ?: true)
        }

    var interest = ArrayList<String>()
    fun saveArrayList(list: ArrayList<String>, key: String?) {
        val gson = Gson()
        val json: String = gson.toJson(list)
        inst.putString(key, json)
        inst.apply { this@ClientPreferences.interest.addAll(list) }
    }

    fun getArrayList(key: String?): ArrayList<String?>? {
        val gson = Gson()
        val json: String? = inst.getString(key, null)
        val type: Type = object : TypeToken<ArrayList<String?>?>() {}.getType()
        return gson.fromJson(json, type)
    }

}