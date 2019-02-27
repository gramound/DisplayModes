package com.broadcom.android.tv.example.displaymodes


import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.util.Log
import android.view.Display
import androidx.fragment.app.FragmentActivity
import androidx.leanback.app.GuidedStepSupportFragment
import androidx.leanback.widget.GuidanceStylist
import androidx.leanback.widget.GuidedAction
import java.text.DecimalFormat
import java.util.*

/**
 * Display Mode Selection using [GuidedStepSupportFragment].
 */
class DisplayModesGuidedStepFragment : GuidedStepSupportFragment() {

    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        val title = getString(R.string.guided_step_first_title)
        val breadcrumb = getString(R.string.guided_step_first_breadcrumb)
        val description = getString(R.string.guided_step_first_description)
        val icon = Objects.requireNonNull<FragmentActivity>(activity).getDrawable(R.drawable.lb_ic_thumb_up)
        return GuidanceStylist.Guidance(title, description, breadcrumb, icon)
    }

    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        val context = activity!!
        val displayManager = context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val display = displayManager.getDisplay(Display.DEFAULT_DISPLAY)
        val modes = display.supportedModes
        val activeMode = display.mode
        Log.d(TAG, "activeMode:$activeMode")
        val decimalFormat = DecimalFormat("#.##")
        for (mode in modes) {
            actions.add(GuidedAction.Builder(context)
                    .id(mode.modeId.toLong())
                    .title(mode.physicalWidth.toString() + "x" + mode.physicalHeight
                            + "@" + decimalFormat.format(mode.refreshRate.toDouble()))
                    .hasNext(false)
                    .checkSetId(GuidedAction.DEFAULT_CHECK_SET_ID)
                    .checked(mode.modeId == activeMode.modeId)
                    .build())
        }
    }

    override fun onGuidedActionClicked(action: GuidedAction?) {
        Log.d(TAG, "action:" + action!!.toString())
        val window = activity!!.window
        val params = window.attributes
        val modeId = action.id.toInt()
        if (modeId != params.preferredDisplayModeId) {
            params.preferredDisplayModeId = modeId
            window.attributes = params
        }
    }

    companion object {
        private const val TAG = "DisplayModes"
    }
}
