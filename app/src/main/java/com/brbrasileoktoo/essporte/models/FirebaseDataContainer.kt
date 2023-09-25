package com.brbrasileoktoo.essporte.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FirebaseDataContainer(
    val letIn: Long,
    val whereLet: String
): Parcelable