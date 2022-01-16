#include "ban_controller.h"

#include <string>

#include <QDebug>

void ban_controller::onBanClicked(long long messageId, long long userId) {
    qDebug() << "Clicked ban for " << userId << " on message " << messageId;
    emit showBanPopup(messageId, userId);
}

void ban_controller::onBan(long long messageId, long long userId, long long seconds, QString const &reason) {
    qDebug() << "Banning " << userId << " for " << seconds << " because " << reason << " on message " << messageId;
}

void ban_controller::onUnban(long long banId) {
    qDebug() << "Unbanning " << banId;
}
