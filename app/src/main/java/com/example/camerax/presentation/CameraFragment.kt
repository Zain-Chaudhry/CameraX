package com.example.camerax.presentation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.camerax.MainActivity
import com.example.camerax.R
import com.example.camerax.databinding.FragmentCameraBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private val cameraViewModel: CameraViewModel by viewModels()
    private lateinit var cameraController: LifecycleCameraController

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        initialization of camera controller
        cameraController = LifecycleCameraController(requireContext())

        cameraController.setEnabledUseCases(
            CameraController.IMAGE_CAPTURE or
                    CameraController.VIDEO_CAPTURE

        )
        cameraController.bindToLifecycle(viewLifecycleOwner)



//        set controller to preview
        binding.previewView.controller = cameraController


//        set button listeners
        binding.captureButton.setOnClickListener {
            if ((activity as MainActivity).arePermissionsGranted()) {
                cameraViewModel.onTakePhoto(cameraController)
            }
            Toast.makeText(requireContext(), "Photo captured", Toast.LENGTH_SHORT).show()
        }
        binding.rotateButton.setOnClickListener {
            rotateCamera()
        }
        binding.flashButton.setOnClickListener {
            cameraViewModel.onToggleFlash(cameraController)
            viewLifecycleOwner.lifecycleScope.launch {
                cameraViewModel.isFlashOn.collect { isFlashOn ->
                    binding.flashButton.setImageResource(
                        if (isFlashOn) R.drawable.flash else R.drawable.flash_off
                    )
                    val flashText = if (isFlashOn) "Flash On" else "Flash Off"
                    Toast.makeText(requireContext(), flashText, Toast.LENGTH_SHORT).show()
                }
            }

        }
        binding.recordButton.setOnClickListener {
            if ((activity as MainActivity).arePermissionsGranted()) {
                cameraViewModel.onRecordVideo(cameraController)
            }
            viewLifecycleOwner.lifecycleScope.launch {
                cameraViewModel.isRecording.collect { isRecording ->
                    if (isRecording) {
                        binding.recordButton.setImageResource(R.drawable.stop_record)

                    } else {
                        binding.recordButton.setImageResource(R.drawable.record)
                    }
                }
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.Q)
    private fun rotateCamera() {
        cameraController.cameraSelector =
            if (cameraController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}