#ifndef DISCODE_DESKTOP_AUTHENTICATION_CONTROLLER_H
#define DISCODE_DESKTOP_AUTHENTICATION_CONTROLLER_H

#include "authentication_service.h"

#include <QObject>
#include <QString>

class authentication_controller : public QObject {
    Q_OBJECT
public:
    explicit authentication_controller(authentication_service, QObject * = nullptr);

signals:
    void authenticated();
    void authenticationError();

public slots:
    void onAuthenticationRequested(QString const &, QString const &);
    void onLogoutRequested();

private:
    authentication_service as;
};


#endif //DISCODE_DESKTOP_AUTHENTICATION_CONTROLLER_H
