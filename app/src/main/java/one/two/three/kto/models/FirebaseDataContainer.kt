package one.two.three.kto.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FirebaseDataContainer(
    val allow: Boolean,
    val link: String
): Parcelable