package com.hedvig.app.service.push.managers

import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.google.firebase.messaging.RemoteMessage
import com.hedvig.app.R
import com.hedvig.app.feature.referrals.ReferralsSuccessfulInviteActivity
import com.hedvig.app.service.push.PushNotificationService
import com.hedvig.app.service.push.setupNotificationChannel
import com.hedvig.app.util.interpolateTextKey
import com.hedvig.app.util.safeLet

object ReferralsNotificationManager {
    fun sendReferralNotification(context: Context, remoteMessage: RemoteMessage) {
        createChannel(context)

        val referralName =
            remoteMessage.data[PushNotificationService.DATA_MESSAGE_REFERRED_SUCCESS_NAME]
        val referralIncentive =
            remoteMessage.data[PushNotificationService.DATA_MESSAGE_REFERRED_SUCCESS_INCENTIVE_AMOUNT]
        val referralsIntent = safeLet(referralName, referralIncentive) { name, incentive ->
            ReferralsSuccessfulInviteActivity.newInstance(context, name, incentive)
        } ?: ReferralsSuccessfulInviteActivity.newInstance(context)

        val pendingIntent: PendingIntent? = TaskStackBuilder
            .create(context)
            .run {
                addNextIntentWithParentStack(referralsIntent)
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }

        val notificationBuilder = NotificationCompat
            .Builder(
                context,
                REFERRAL_CHANNEL_ID
            )
            .setSmallIcon(R.drawable.ic_hedvig_symbol_android)
            .setContentTitle(context.resources.getString(R.string.NOTIFICATION_REFERRAL_COMPLETED_TITLE))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setChannelId(REFERRAL_CHANNEL_ID)
            .setContentIntent(pendingIntent)

        val contentText = referralName?.let {
            interpolateTextKey(
                context.resources.getString(R.string.NOTIFICATION_REFERRAL_COMPLETED_CONTENT_WITH_NAME),
                "NAME" to it
            )
        } ?: run {
            context.resources.getString(R.string.NOTIFICATION_REFERRAL_COMPLETED_CONTENT)
        }
        notificationBuilder.setContentText(contentText)

        NotificationManagerCompat
            .from(context)
            .notify(REFERRAL_NOTIFICATION_ID, notificationBuilder.build())
    }

    fun createChannel(context: Context) {
        setupNotificationChannel(
            context,
            REFERRAL_CHANNEL_ID,
            context.resources.getString(R.string.NOTIFICATION_REFERRAL_CHANNEL_NAME),
            context.resources.getString(R.string.NOTIFICATION_REFERRAL_CHANNEL_DESCRIPTION)
        )
    }

    private const val REFERRAL_CHANNEL_ID = "hedvig-referral"
    private const val REFERRAL_NOTIFICATION_ID = 2
}