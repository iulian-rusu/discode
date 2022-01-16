#ifndef DISCODE_DESKTOP_AUTHENTICATION_CONTROLLER_H
#define DISCODE_DESKTOP_AUTHENTICATION_CONTROLLER_H

#include <QObject>
#include <QString>

#include <network_helpers.h>

class authentication_controller : public QObject {
    Q_OBJECT
public slots:
    void onAuthenticationRequested(QString const &, QString const &);

};


#endif //DISCODE_DESKTOP_AUTHENTICATION_CONTROLLER_H
