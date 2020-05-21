package me.kaungmyatmin.videochat

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_dial.*

class DialDialog(
    val onConnectClicked: (userName: String, roomName: String) -> Unit,
    val onCancelClicked: () -> Unit
) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.dialog_dial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnOk.setOnClickListener {
            val name = etName.text.toString()
            val room = etRoom.text.toString()
            if (name.isNullOrBlank()) {
                Toast.makeText(context, "Name is required to identify you.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (room.isNullOrBlank()) {
                Toast.makeText(context, "Room Id is required to connect", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            onConnectClicked(name, room)
            dismiss()
        }

        btnCancel.setOnClickListener {
            onCancelClicked()
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            it.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.let {
                it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


            }
        }
    }
}