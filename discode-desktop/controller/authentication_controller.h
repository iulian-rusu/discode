#ifndef DISCODE_DESKTOP_AUTHENTICATION_CONTROLLER_H
#define DISCODE_DESKTOP_AUTHENTICATION_CONTROLLER_H

#include <QObject>
#include <QString>

#include <network_helpers>

class authentication_controller {
    Q_OBJECT
public slots:
    void onAuthenticationRequested(QString, QString);

};


#endif //DISCODE_DESKTOP_AUTHENTICATION_CONTROLLER_H
