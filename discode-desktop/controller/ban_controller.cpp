#include "ban_controller.h"

#include <string>

#include <QDebug>

void ban_controller::onBan(long long userId, long long seconds, QString const &reason) {
    qDebug() << "Unbanning " << userId << " for " << seconds << " because " << reason;
}

void ban_controller::onUnban(long long banId) {
    qDebug() << "Unbanning " << banId;
}
