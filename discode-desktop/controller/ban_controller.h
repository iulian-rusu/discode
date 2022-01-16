#ifndef DISCODE_DESKTOP_BAN_CONTROLLER_H
#define DISCODE_DESKTOP_BAN_CONTROLLER_H

#include <QObject>
#include <QString>

#include <network_helpers.h>

class ban_controller : public QObject {
    Q_OBJECT
public slots:
    void onBan(long long, long long, QString const &);
    void onUnban(long long);

};


#endif //DISCODE_DESKTOP_BAN_CONTROLLER_H
