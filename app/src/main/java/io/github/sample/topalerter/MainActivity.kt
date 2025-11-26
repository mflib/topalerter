package io.github.sample.topalerter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.mflib.topalerter.TopAlerter
import io.github.sample.topalerter.ui.theme.TopAlerterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TopAlerterDemoScreen()

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopAlerterDemoScreen() {
        var isDarkTheme by remember { mutableStateOf(false) }
        val states = remember {
            mutableStateListOf<Boolean>().apply { repeat(17) { add(false) } }
        }
        val show = { index: Int -> states[index] = true }
        TopAlerterTheme(darkTheme = isDarkTheme) {
            Box(modifier = Modifier.fillMaxSize()) {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("TopAlerter – All Features") },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                            ),
                            actions = {
                                IconButton(onClick = { isDarkTheme = !isDarkTheme }) {
                                    Icon(
                                        imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                        contentDescription = "Toggle Theme",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            })
                    }) { paddingValues ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "Tap any button to preview a real-world TopAlerter style",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(24.dp))
                        }
                        val demoTitles = listOf(
                            "Simple Alert (Theme-Aware)",
                            "With Icon",
                            "With Progress",
                            "With Action Buttons",
                            "Error Style (Red)",
                            "Warning Style (Orange)",
                            "Info Style (Blue)",
                            "Success Style (Green)",
                            "Long Message",
                            "No Auto-Dismiss",
                            "Custom Duration (8s)",
                            "Custom Typography",
                            "Dark Background",
                            "Gradient Background",
                            "Custom Shape",
                            "Clickable Card",
                            "Fully Custom Buttons"
                        )

                        items(demoTitles.size) { index ->
                            FilledTonalButton(
                                onClick = { show(index) }, modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(demoTitles[index], textAlign = TextAlign.Center)
                            }
                        }
                    }
                }

                // ================== ALL 17 DEMOS (Real-world use cases) ==================

                if (states.getOrNull(0) == true) {
                    TopAlerter(
                        visible = true,
                        title = "Welcome Back!",
                        message = "Your session is active",
                        onDismiss = { states[0] = false })
                }

                if (states.getOrNull(1) == true) {
                    TopAlerter(
                        visible = true,
                        title = "Download Complete",
                        message = "report.pdf is ready",
                        icon = Icons.Default.CheckCircle,
                        iconTint = Color(0xFF4CAF50),
                        onDismiss = { states[1] = false })
                }

                if (states.getOrNull(2) == true) {
                    TopAlerter(
                        visible = true,
                        title = "Uploading...",
                        message = "3 of 5 files completed",
                        showProgress = true,
                        onDismiss = { states[2] = false })
                }

                if (states.getOrNull(3) == true) {
                    TopAlerter(
                        visible = true,
                        title = "Unsaved Changes",
                        message = "Do you want to save before leaving?",
                        positiveButtonText = "Save",
                        positiveButtonOnClick = { /* save logic */ },
                        negativeButtonText = "Discard",
                        negativeButtonColors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        onDismiss = { states[3] = false })
                }

                if (states.getOrNull(4) == true) {
                    TopAlerter(
                        visible = true,
                        title = "Connection Failed",
                        message = "No internet connection",
                        icon = Icons.Default.Warning,
                        backgroundColor = Color(0xFFE53935),
                        titleColor = Color.White,
                        messageColor = Color.White.copy(0.9f),
                        iconTint = Color.White,
                        positiveButtonText = "Retry",
                        positiveButtonColors = ButtonDefaults.buttonColors(
                            containerColor = Color.White, contentColor = Color(0xFFE53935)
                        ),
                        onDismiss = { states[4] = false })
                }

                if (states.getOrNull(5) == true) {
                    TopAlerter(
                        visible = true,
                        title = "Low Battery",
                        message = "15% remaining – plug in soon",
                        icon = Icons.Default.BatteryAlert,
                        backgroundColor = Color(0xFFFF9800),
                        titleColor = Color.White,
                        iconTint = Color.White,
                        onDismiss = { states[5] = false })
                }

                if (states.getOrNull(6) == true) {
                    TopAlerter(
                        visible = true,
                        title = "Update Available",
                        message = "A new version is ready",
                        icon = Icons.Default.Info,
                        backgroundColor = Color(0xFF2196F3),
                        titleColor = Color.White,
                        iconTint = Color.White,
                        positiveButtonText = "Update Now",
                        onDismiss = { states[6] = false })
                }

                if (states.getOrNull(7) == true) {
                    TopAlerter(
                        visible = true,
                        title = "Success!",
                        message = "Your profile has been updated",
                        icon = Icons.Default.CheckCircle,
                        backgroundColor = Color(0xFF4CAF50),
                        titleColor = Color.White,
                        titleFontWeight = FontWeight.Bold,
                        messageColor = Color.White.copy(0.9f),
                        iconTint = Color.White,
                        positiveButtonText = "Done",
                        positiveButtonColors = ButtonDefaults.buttonColors(
                            containerColor = Color.White, contentColor = Color(0xFF4CAF50)
                        ),
                        onDismiss = { states[7] = false })
                }

                if (states.getOrNull(8) == true) {
                    TopAlerter(
                        visible = true,
                        title = "Privacy Update",
                        message = "We've improved our privacy policy to better protect your data. Tap to learn more.",
                        messageMaxLines = 5,
                        onDismiss = { states[8] = false })
                }

                if (states.getOrNull(9) == true) {
                    TopAlerter(
                        visible = true,
                        title = "Important Notice",
                        message = "This message will stay until dismissed",
                        autoDismiss = false,
                        backgroundColor = Color.Black.copy(alpha = 0.95f),
                        titleColor = Color.White,
                        onDismiss = { states[9] = false })
                }

                if (states.getOrNull(10) == true) {
                    TopAlerter(
                        visible = true,
                        title = "8 Second Alert",
                        message = "Long-lasting message example",
                        durationMillis = 8000L,
                        backgroundColor = Color(0xFF9C27B0),
                        titleColor = Color.White,
                        onDismiss = { states[10] = false })
                }

                if (states.getOrNull(11) == true) {
                    TopAlerter(
                        visible = true,
                        title = "Fancy Typography",
                        message = "Custom styles applied",
                        titleStyle = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold, fontSize = 22.sp
                        ),
                        titleColor = Color(0xFFE65100),
                        messageStyle = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
                        backgroundColor = Color(0xFFFFF3E0),
                        onDismiss = { states[11] = false })
                }

                if (states.getOrNull(12) == true) {
                    TopAlerter(
                        visible = true,
                        title = "Dark Mode Alert",
                        message = "Looks great at night",
                        icon = Icons.Default.DarkMode,
                        backgroundColor = Color.Black,
                        titleColor = Color.White,
                        messageColor = Color(0xFFDDDDDD),
                        iconTint = Color.Yellow,
                        onDismiss = { states[12] = false })
                }

                if (states.getOrNull(13) == true) {
                    TopAlerter(
                        visible = true,
                        title = "Gradient Magic",
                        message = "Full edge-to-edge gradient",
                        backgroundColor = Color.Transparent,
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF667eea), Color(0xFF764ba2))
                                )
                            )
                            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
                        titleColor = Color.White,
                        messageColor = Color.White.copy(0.95f),
                        onDismiss = { states[13] = false })
                }

                if (states.getOrNull(14) == true) {
                    TopAlerter(
                        visible = true,
                        title = "Custom Shape",
                        message = "Extra rounded bottom corners",
                        containerShape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp),
                        backgroundColor = Color(0xFFE91E63),
                        titleColor = Color.White,
                        onDismiss = { states[14] = false })
                }

                if (states.getOrNull(15) == true) {
                    TopAlerter(
                        visible = true,
                        title = "Tap Anywhere!",
                        message = "Entire card is clickable",
                        backgroundColor = Color(0xFF3F51B5),
                        titleColor = Color.White,
                        onClick = { states[15] = false },
                        onDismiss = { states[15] = false })
                }

                if (states.getOrNull(16) == true) {
                    TopAlerter(
                        visible = true,
                        title = "Rate This App",
                        message = "Enjoying TopAlerter?",
                        positiveButtonText = "Love it!",
                        positiveButtonColors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50), contentColor = Color.White
                        ),
                        positiveButtonShape = RoundedCornerShape(50),
                        positiveButtonTextStyle = TextStyle(fontWeight = FontWeight.Bold),
                        negativeButtonText = "Later",
                        negativeButtonColors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        negativeButtonShape = RoundedCornerShape(12.dp),
                        onDismiss = { states[16] = false })
                }
            }
        }
    }
}
