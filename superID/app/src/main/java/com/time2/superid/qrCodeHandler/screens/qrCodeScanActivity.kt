package com.time2.superid.qrCodeHandler.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Size
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.time2.superid.qrCodeHandler.qrCodeManager
import kotlinx.coroutines.launch
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.remember
import com.time2.superid.passwordHandler.screens.SinglePasswordActivity
import com.time2.superid.ui.theme.SuperIDTheme

class qrCodeScanActivity : ComponentActivity() {
    private lateinit var qrCodeManager: qrCodeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializa o qrCodeManager
        qrCodeManager = qrCodeManager()

        // Permissão da Camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        }

        setContent {
            SuperIDTheme {
                qrCodeScanView(
                    onScanQrCode = { loginToken ->
                        lifecycleScope.launch {
                            try {
                                val docId = intent.getStringExtra("docId") ?: throw Exception("ID do documento não fornecido")
                                val partnerSite = qrCodeManager.processLoginToken(loginToken, docId)
                                // Navega para a tela de sucesso, passando o partnerSite
                                startActivity(
                                    Intent(this@qrCodeScanActivity, SinglePasswordActivity::class.java).apply {
                                        putExtra("partnerSite", partnerSite)
                                        putExtra("docId", docId)
                                    }
                                )
                                finish()
                            } catch (e: Exception) {
                                // TODO: Exibir mensagem de erro (ex.: Snackbar com e.message)
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun qrCodeScanView(
    onScanQrCode: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Exibe o scanner de QR Code diretamente
    QrCodeScanner(
        onQrCodeScanned = onScanQrCode,
        modifier = modifier
            .fillMaxSize()
            .padding(start = 21.dp, top = 60.dp, end = 21.dp, bottom = 31.dp)
    )
}

@OptIn(ExperimentalGetImage::class)
@Composable
fun QrCodeScanner(
    onQrCodeScanned: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }

    AndroidView(
        factory = { previewView },
        update = { view -> previewView.scaleType = PreviewView.ScaleType.FIT_CENTER },
        modifier = modifier
    )

    LaunchedEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // View da camera
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            // Análise de imagem para escaneamento
            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(previewView.width, previewView.height))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                        val options = BarcodeScannerOptions.Builder()
                            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                            .build()
                        val scanner = BarcodeScanning.getClient(options)
                        val inputImage = InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)
                        scanner.process(inputImage)
                            .addOnSuccessListener { barcodes ->
                                for (barcode in barcodes) {
                                    val value = barcode.rawValue
                                    if (value != null) {
                                        onQrCodeScanned(value)
                                    }
                                }
                            }
                            .addOnFailureListener { /* TO DO */ }
                            .addOnCompleteListener { imageProxy.close() }
                    }
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                // /* TO DO */
            }
        }, ContextCompat.getMainExecutor(context))
    }
}