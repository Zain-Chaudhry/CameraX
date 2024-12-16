package com.example.camerax.presentation

import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.camerax.domain.repository.CameraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val cameraRepository: CameraRepository
) : ViewModel() {
    private val _isRecording = MutableStateFlow(false)
    val isRecording = _isRecording.asStateFlow()

    private val _isFlashOn = MutableStateFlow(false)
    val isFlashOn = _isFlashOn.asStateFlow()


    fun onTakePhoto(
        controller: LifecycleCameraController
    ) {
       viewModelScope.launch {

           cameraRepository.takePhoto(controller)

       }
    }
    fun onRecordVideo(
        controller: LifecycleCameraController
    ) {
        _isRecording.update { !isRecording.value }
        viewModelScope.launch {

            cameraRepository.recordVideo(controller)

        }
    }
    fun onToggleFlash(
        controller: LifecycleCameraController
    ) {
        _isFlashOn.update { !isFlashOn.value }
        controller.enableTorch(_isFlashOn.value)

    }


}

