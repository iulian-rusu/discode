#ifndef DISCODE_DESKTOP_BAN_CONTROLLER_H
#define DISCODE_DESKTOP_BAN_CONTROLLER_H

#include <QObject>
#include <QString>

#include <network_helpers.h>

class ban_controller : public QObject {
    Q_OBJECT

signals:
    void showBanPopup(long long messageId, long long userId);

public slots:
    void onBanClicked(long long, long long);
    void onBan(long long, long long, long long, QString const &);
    void onUnban(long long);
};


#endif //DISCODE_DESKTOP_BAN_CONTROLLER_H
