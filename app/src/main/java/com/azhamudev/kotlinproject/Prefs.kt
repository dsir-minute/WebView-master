import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context)
{
    private val APP_PREF_STR1 = "strPref1"

    private val preferences: SharedPreferences = context.getSharedPreferences("raxyPrefs", Context.MODE_PRIVATE)

    var strPref1: String?
        get() {
            return preferences.getString(APP_PREF_STR1, "yes")
        }
        set(value) = preferences.edit().putString(APP_PREF_STR1, value).apply()
}
