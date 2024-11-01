package com.example.shapegenerator.View

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.shapegenerator.CanvaActivity
import com.example.shapegenerator.R

class SelectionDialogFragment : DialogFragment() {

    var scale = 0
    var sides = 0
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_with_seekbar, null)

        // Configuración del primer SeekBar
        val seekBar1 = dialogView.findViewById<SeekBar>(R.id.seek_bar_1)
        val seekBarValue1 = dialogView.findViewById<TextView>(R.id.seekbar_value_1)
        seekBar1.setProgress(5)

        seekBar1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekBarValue1.text = "N de caras : $progress"
                sides = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        // Configuración del segundo SeekBar
        val seekBar2 = dialogView.findViewById<SeekBar>(R.id.seek_bar_2)
        val seekBarValue2 = dialogView.findViewById<TextView>(R.id.seekbar_value_2)
        seekBar2.setProgress(250)

        seekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekBarValue2.text = "Escala : $progress"
                scale = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        builder.setTitle("Diseñar poligono personalizado")
            .setView(dialogView)
            .setPositiveButton("Continuar") { dialog, _ ->
                val intent = Intent(requireContext(), CanvaActivity::class.java)
                intent.putExtra("id", 1)
                intent.putExtra("seekBarValue1", scale)
                intent.putExtra("seekBarValue2", sides)
                startActivity(intent)
                dialog.dismiss() // Opcional: Cierra el diálogo después de la acción
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
        // Configura el fondo transparente y el efecto de atenuación
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    companion object {
        fun newInstance(): SelectionDialogFragment {
            return SelectionDialogFragment()
        }
    }
}
