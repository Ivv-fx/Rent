package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ThemeError
import com.example.ui.theme.ThemeErrorLight
import com.example.ui.theme.ThemeSecondary
import com.example.ui.theme.ThemeSecondaryLight
import com.example.ui.theme.ThemeSurfaceVariant
import com.example.ui.theme.ThemeTextSecondary
import com.example.ui.theme.ThemePrimaryLight
import com.example.ui.theme.ThemePrimary
import com.example.ui.theme.ThemeSuccess
import com.example.ui.theme.ThemeSuccessLight

@Composable
fun AvailabilityBadge(
    status: String,
    modifier: Modifier = Modifier
)  {
    val (bgColor, textColor, icon) = when  {
        status.contains("Alert", ignoreCase = true) || status.contains("Move-out", ignoreCase = true) ->  {
            Triple(ThemeErrorLight, ThemeError, Icons.Default.NotificationsActive)
        }
        status.contains("Now", ignoreCase = true) ->  {
            Triple(ThemeSuccessLight, ThemeSuccess, Icons.Default.CheckCircle)
        }
        status.contains("Only 1", ignoreCase = true) ->  {
            Triple(ThemeSecondaryLight, ThemeSecondary, Icons.Default.Star)
        }
        else ->  {
            Triple(ThemeSurfaceVariant, ThemeTextSecondary, Icons.Default.CheckCircle)
        }
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    )  {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = status,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PriceTierPill(
    basePrice: Int,
    utilitiesPrice: Int,
    studentDiscountPercent: Int,
    modifier: Modifier = Modifier
)  {
    val isBudget = basePrice <= 650
    val tierLabel = if (isBudget) "Student Budget Tier" else if (basePrice <= 1100) "Mid-Range Tier" else "Premium Tier"
    val tierColor = if (isBudget) ThemeSuccess else if (basePrice <= 1100) ThemePrimary else ThemeSecondary

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(tierColor.copy(alpha = 0.12f))
            .border(1.dp, tierColor.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    )  {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(tierColor)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = tierLabel,
            color = tierColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
        if (studentDiscountPercent > 0)  {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "• studentDiscountPercent% .edu off",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun VerificationBadgesRow(
    isLandlordVerified: Boolean,
    isBackgroundChecked: Boolean,
    modifier: Modifier = Modifier
)  {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    )  {
        if (isLandlordVerified)  {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ThemePrimaryLight)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            )  {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Verified Landlord",
                    tint = ThemePrimary,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "Verified Landlord",
                    color = ThemePrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (isBackgroundChecked)  {
            Spacer(modifier = Modifier.width(6.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ThemeSuccessLight)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            )  {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Background Checked",
                    tint = ThemeSuccess,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "Background Checked",
                    color = ThemeSuccess,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
