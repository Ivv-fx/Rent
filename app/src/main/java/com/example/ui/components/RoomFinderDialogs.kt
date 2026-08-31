package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PriceCheck
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.models.ListingEntity
import com.example.ui.theme.ThemeError
import com.example.ui.theme.ThemeSecondary
import com.example.ui.theme.ThemeSurfaceVariant
import com.example.ui.theme.ThemeTextSecondary
import com.example.ui.theme.ThemeTextPrimary
import com.example.ui.theme.ThemePrimaryDark
import com.example.ui.theme.ThemePrimaryLight
import com.example.ui.theme.ThemePrimary
import com.example.ui.theme.ThemeSuccess
import com.example.ui.theme.ThemeSuccessLight

@Composable
fun ReportMoveOutDialog(
    listing: ListingEntity,
    onDismiss: () -> Unit,
    onSubmitNotice: (moveOutDate: String, reason: String) -> Unit
)  {
    var moveOutDate by remember  { mutableStateOf("Oct 31st, 2026") }
    var reason by remember  { mutableStateOf("Graduating / Starting new job in tech corridor") }
    var broadcastAlert by remember  { mutableStateOf(true) }
    var isSubmitted by remember  { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss)  {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .testTag("report_move_out_dialog"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        )  {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            )  {
                if (isSubmitted)  {
                    // Success View
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    )  {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(ThemeSuccessLight),
                            contentAlignment = Alignment.Center
                        )  {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = ThemeSuccess,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Move-Out Notice Sent!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Owner ${listing.landlordName} has been officially notified. An automated vacancy alert has also been broadcast to 30+ students looking for rooms near ${listing.universityNearby}.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = ThemePrimary)
                        )  {
                            Text("Done", fontWeight = FontWeight.Bold)
                        }
                    }
                } else  {
                    // Form View
                    Row(verticalAlignment = Alignment.CenterVertically)  {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(ThemeSecondary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        )  {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = ThemeSecondary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column  {
                            Text(
                                text = "Notice of Room Move-Out",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = listing.title,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Inform the property owner that you plan to vacate the room. This triggers automated vacancy alerts for student seekers looking in ${listing.neighborhood}.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = moveOutDate,
                        onValueChange =  { moveOutDate = it },
                        label =  { Text("Planned Departure Date") },
                        leadingIcon =  {
                            Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = ThemePrimary)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("move_out_date_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = reason,
                        onValueChange =  { reason = it },
                        label =  { Text("Reason for Moving / Next Plans") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("move_out_reason_input"),
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Broadcast toggle
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(10.dp)
                    )  {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        )  {
                            Column(modifier = Modifier.weight(1f))  {
                                Text(
                                    text = "Auto-Broadcast Vacancy Alert",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Notify students searching near ${listing.universityNearby}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = broadcastAlert,
                                onCheckedChange =  { broadcastAlert = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = ThemePrimary)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    )  {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        )  {
                            Text("Cancel")
                        }
                        Button(
                            onClick =  {
                                onSubmitNotice(moveOutDate, reason)
                                isSubmitted = true
                            },
                            modifier = Modifier
                                .weight(1.5f)
                                .testTag("submit_move_out_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = ThemeError)
                        )  {
                            Text("Send Notice", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentCheckoutDialog(
    listing: ListingEntity,
    onDismiss: () -> Unit,
    onPaymentSuccess: (amount: Double, type: String, method: String) -> Unit
)  {
    var paymentType by remember  { mutableStateOf("Monthly Rent") }
    var selectedMethod by remember  { mutableStateOf("Chase Student Checking (•••• 4129)") }
    var applyStudentDiscount by remember  { mutableStateOf(true) }
    var isProcessing by remember  { mutableStateOf(false) }
    var isCompleted by remember  { mutableStateOf(false) }
    var transactionId by remember  { mutableStateOf("") }

    val baseAmount = when (paymentType)  {
        "Monthly Rent" -> listing.basePrice.toDouble()
        "Security Deposit Escrow" -> listing.depositPrice.toDouble()
        else -> listing.utilitiesPrice.toDouble()
    }

    val finalAmount = if (paymentType == "Monthly Rent" && applyStudentDiscount)  {
        baseAmount * (1.0 - listing.studentDiscountPercent / 100.0)
    } else  {
        baseAmount
    }

    Dialog(onDismissRequest = onDismiss)  {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .testTag("payment_checkout_dialog"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        )  {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            )  {
                if (isCompleted)  {
                    // Payment Receipt
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    )  {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(ThemeSuccessLight),
                            contentAlignment = Alignment.Center
                        )  {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = ThemeSuccess,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Payment Completed!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Ref: transactionId",
                            fontSize = 12.sp,
                            color = ThemePrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        )  {
                            Column(modifier = Modifier.padding(12.dp))  {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                )  {
                                    Text("Payment Item", fontSize = 12.sp, color = ThemeTextSecondary)
                                    Text(paymentType, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                )  {
                                    Text("Amount Paid", fontSize = 12.sp, color = ThemeTextSecondary)
                                    Text("₹${String.format("%.2f", finalAmount)}", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = ThemePrimary)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                )  {
                                    Text("Property", fontSize = 12.sp, color = ThemeTextSecondary)
                                    Text(listing.title, fontSize = 11.sp, maxLines = 1, fontWeight = FontWeight.Medium)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = ThemePrimary)
                        )  {
                            Text("View in Payment Ledger", fontWeight = FontWeight.Bold)
                        }
                    }
                } else  {
                    // Checkout selection
                    Row(verticalAlignment = Alignment.CenterVertically)  {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(ThemePrimaryLight),
                            contentAlignment = Alignment.Center
                        )  {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Secure",
                                tint = ThemePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column  {
                            Text(
                                text = "Secure In-App Checkout",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "256-bit Encrypted Escrow Protection",
                                fontSize = 11.sp,
                                color = ThemeSuccess,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Payment Type selector
                    Text(
                        text = "Select Payment Type",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    listOf(
                        "Monthly Rent" to "₹${listing.basePrice}",
                        "Security Deposit Escrow" to "₹${listing.depositPrice}",
                        "Utilities Split" to "₹${listing.utilitiesPrice}"
                    ).forEach  { (type, price) ->
                        val isSelected = paymentType == type
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    if (isSelected) 2.dp else 1.dp,
                                    if (isSelected) ThemePrimary else MaterialTheme.colorScheme.outline,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable  { paymentType = type },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) ThemePrimaryLight.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
                            )
                        )  {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            )  {
                                Text(
                                    text = type,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                Text(
                                    text = price,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ThemePrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (paymentType == "Monthly Rent" && listing.studentDiscountPercent > 0)  {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        )  {
                            Checkbox(
                                checked = applyStudentDiscount,
                                onCheckedChange =  { applyStudentDiscount = it }
                            )
                            Text(
                                text = "Apply ${listing.studentDiscountPercent}% Verified Student Discount",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Payment Method options
                    Text(
                        text = "Payment Method",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    )  {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        )  {
                            Icon(
                                imageVector = Icons.Default.AccountBalance,
                                contentDescription = null,
                                tint = ThemePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column  {
                                Text(selectedMethod, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                Text("Verified Student Checking", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Total Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    )  {
                        Text(
                            text = "Total Due:",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "₹${String.format("%.2f", finalAmount)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ThemePrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    )  {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        )  {
                            Text("Cancel")
                        }
                        Button(
                            onClick =  {
                                isProcessing = true
                                val ref = "UR-TX-${(100000..999999).random()}"
                                transactionId = ref
                                onPaymentSuccess(finalAmount, paymentType, selectedMethod)
                                isCompleted = true
                                isProcessing = false
                            },
                            modifier = Modifier
                                .weight(1.6f)
                                .testTag("confirm_pay_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = ThemePrimary)
                        )  {
                            Text("Pay ₹${String.format("%.2f", finalAmount)}", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScheduleTourDialog(
    listing: ListingEntity,
    onDismiss: () -> Unit,
    onConfirmTour: (date: String, type: String) -> Unit
)  {
    var tourType by remember  { mutableStateOf("Live 360 Video Walkthrough") }
    var selectedDate by remember  { mutableStateOf("Tomorrow @ 4:00 PM") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title =  {
            Row(verticalAlignment = Alignment.CenterVertically)  {
                Icon(Icons.Default.Videocam, contentDescription = null, tint = ThemePrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Schedule Room Tour", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        text =  {
            Column  {
                Text(
                    text = "Request a walkthrough with landlord ${listing.landlordName}.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                listOf("Live 360 Video Walkthrough", "In-Person Campus Tour").forEach  { type ->
                    val isSelected = tourType == type
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) ThemePrimaryLight else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable  { tourType = type }
                    )  {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        )  {
                            Icon(
                                imageVector = if (type.contains("Video")) Icons.Default.Videocam else Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (isSelected) ThemePrimary else ThemeTextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = type,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) ThemePrimaryDark else ThemeTextPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = selectedDate,
                    onValueChange =  { selectedDate = it },
                    label =  { Text("Preferred Time Slot") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton =  {
            Button(
                onClick =  {
                    onConfirmTour(selectedDate, tourType)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = ThemePrimary)
            )  {
                Text("Book Tour", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton =  {
            TextButton(onClick = onDismiss)  {
                Text("Cancel")
            }
        }
    )
}
