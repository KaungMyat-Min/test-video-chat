package me.kaungmyatmin.videochat

import android.content.Intent
import android.os.Bundle

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.twilio.video.*
import me.kaungmyatmin.videochat.Constants.Companion.AUDIO_CODEC_NAMES
import me.kaungmyatmin.videochat.Constants.Companion.PREF_AUDIO_CODEC
import me.kaungmyatmin.videochat.Constants.Companion.PREF_AUDIO_CODEC_DEFAULT
import me.kaungmyatmin.videochat.Constants.Companion.PREF_SENDER_MAX_AUDIO_BITRATE
import me.kaungmyatmin.videochat.Constants.Companion.PREF_SENDER_MAX_AUDIO_BITRATE_DEFAULT
import me.kaungmyatmin.videochat.Constants.Companion.PREF_SENDER_MAX_VIDEO_BITRATE
import me.kaungmyatmin.videochat.Constants.Companion.PREF_SENDER_MAX_VIDEO_BITRATE_DEFAULT
import me.kaungmyatmin.videochat.Constants.Companion.PREF_VIDEO_CODEC
import me.kaungmyatmin.videochat.Constants.Companion.PREF_VIDEO_CODEC_DEFAULT
import me.kaungmyatmin.videochat.Constants.Companion.VIDEO_CODEC_NAMES
import tvi.webrtc.MediaCodecVideoDecoder
import tvi.webrtc.MediaCodecVideoEncoder

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val settingsFragment = SettingsFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, settingsFragment)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private val sharedPreferences by lazy {
            requireContext().getSharedPreferences(
                "default_pref",
                android.content.Context.MODE_PRIVATE
            )
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.settings)
            setHasOptionsMenu(true)
            setupCodecListPreference(
                AudioCodec::class.java,
                PREF_AUDIO_CODEC,
                PREF_AUDIO_CODEC_DEFAULT,
                findPreference(PREF_AUDIO_CODEC) as ListPreference?
            )
            setupCodecListPreference(
                VideoCodec::class.java,
                PREF_VIDEO_CODEC,
                PREF_VIDEO_CODEC_DEFAULT,
                findPreference(PREF_VIDEO_CODEC) as ListPreference?
            )
            setupSenderBandwidthPreferences(
                PREF_SENDER_MAX_AUDIO_BITRATE,
                PREF_SENDER_MAX_AUDIO_BITRATE_DEFAULT,
                findPreference(PREF_SENDER_MAX_AUDIO_BITRATE) as EditTextPreference?
            )
            setupSenderBandwidthPreferences(
                PREF_SENDER_MAX_VIDEO_BITRATE,
                PREF_SENDER_MAX_VIDEO_BITRATE_DEFAULT,
                findPreference(PREF_SENDER_MAX_VIDEO_BITRATE) as EditTextPreference?
            )
        }


        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return when (item.itemId) {
                android.R.id.home -> {
                    startActivity(Intent(activity, SettingsActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }

        private fun setupCodecListPreference(
            codecClass: Class<*>,
            key: String,
            defaultValue: String,
            listPreference: ListPreference?
        ) {
            // Set codec entries
            val codecEntries = if (codecClass == AudioCodec::class.java)
                AUDIO_CODEC_NAMES.toMutableList()
            else
                VIDEO_CODEC_NAMES.toMutableList()

            // Remove H264 if not supported
            if (!MediaCodecVideoDecoder.isH264HwSupported() ||
                !MediaCodecVideoEncoder.isH264HwSupported()
            ) {
                codecEntries.remove(H264Codec.NAME)
            }

            // Bind value
            val prefValue = sharedPreferences.getString(key, defaultValue)
            val codecStrings = codecEntries.toTypedArray()

            listPreference?.apply {
                entries = codecStrings
                entryValues = codecStrings
                value = prefValue
                summary = prefValue
                onPreferenceChangeListener =
                    Preference.OnPreferenceChangeListener { preference, newValue ->
                        preference.summary = newValue.toString()
                        true
                    }
            }

        }

        private fun setupSenderBandwidthPreferences(
            key: String,
            defaultValue: String,
            editTextPreference: EditTextPreference?
        ) {
            val value = sharedPreferences.getString(key, defaultValue)

            // Set layout with input type number for edit text
            editTextPreference?.apply {
                dialogLayoutResource = R.layout.preference_dialog_number_edittext
                summary = value
                onPreferenceChangeListener =
                    Preference.OnPreferenceChangeListener { preference, newValue ->
                        preference.summary = newValue.toString()
                        true
                    }
            }

        }

        companion object {
            fun newInstance(): SettingsFragment {
                return SettingsFragment()
            }
        }
    }
}