with open("app/src/main/java/com/example/ui/components/MapPreviewCard.kt", "r") as f:
    content = f.read()

import_str = """import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import android.content.Intent
import android.net.Uri
import com.example.data.models.ListingEntity
import com.example.ui.theme.ThemePrimary
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState"""

content = content.replace(content.split("fun MapPreviewCard")[0], import_str + "\n\n")

target_canvas = """            // Map graphic canvas representation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE2E8F0))
            )  {
                // Procedural stylized street map canvas
                Canvas(modifier = Modifier.fillMaxWidth().height(140.dp))  {
                    val w = size.width
                    val h = size.height
                    // Background map land
                    drawRect(color = Color(0xFFF1F5F9))
                    // Green Park patch
                    drawRect(
                        color = Color(0xFFDCFCE7),
                        topLeft = Offset(w * 0.65f, 10f),
                        size = Size(w * 0.3f, h * 0.45f)
                    )
                    // Roads
                    drawLine(
                        color = Color.White,
                        start = Offset(w * 0.2f, 0f),
                        end = Offset(w * 0.4f, h),
                        strokeWidth = 12f
                    )
                    drawLine(
                        color = Color.White,
                        start = Offset(0f, h * 0.6f),
                        end = Offset(w, h * 0.4f),
                        strokeWidth = 14f
                    )
                    // Location pin marker
                    drawCircle(
                        color = ThemePrimary.copy(alpha = 0.2f),
                        radius = 40f,
                        center = Offset(w * 0.4f, h * 0.5f)
                    )
                    drawCircle(
                        color = ThemePrimary,
                        radius = 12f,
                        center = Offset(w * 0.4f, h * 0.5f)
                    )
                }
            }"""

replacement_map = """            // Real Google Maps integration
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE2E8F0))
            )  {
                // Note: We use a placeholder LatLng here. 
                // In a production app, the ListingEntity should have lat/lng properties derived from Google Maps Geocoding API.
                val location = LatLng(18.5204, 73.8567) // Default Pune
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(location, 14f)
                }
                
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = com.google.maps.android.compose.MapUiSettings(
                        zoomControlsEnabled = false,
                        scrollGesturesEnabled = false,
                        zoomGesturesEnabled = false,
                        tiltGesturesEnabled = false,
                        rotationGesturesEnabled = false
                    )
                ) {
                    Marker(
                        state = MarkerState(position = location),
                        title = listing.title,
                        snippet = listing.address
                    )
                }
            }"""

content = content.replace(target_canvas, replacement_map)

with open("app/src/main/java/com/example/ui/components/MapPreviewCard.kt", "w") as f:
    f.write(content)
