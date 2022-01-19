#ifndef DISCODE_DESKTOP_AUTHENTICATION_CONTROLLER_H
#define DISCODE_DESKTOP_AUTHENTICATION_CONTROLLER_H

#include "authentication_service.h"
#include "session_service.h"

#include <memory>

#include <QObject>
#include <QString>

class authentication_controller : public QObject {
    Q_OBJECT
public:
    explicit authentication_controller(std::shared_ptr<authentication::authentication_service>, std::shared_ptr<session_service>, QObject * = nullptr);

signals:
    void authenticated();
    void authenticationError();

public slots:
    void onAuthenticationRequested(QString const &, QString const &);
    void onLogoutRequested();

private:
    std::shared_ptr<authentication::authentication_service> as{};
    std::shared_ptr<session_service> ss{};
};


#endif //DISCODE_DESKTOP_AUTHENTICATION_CONTROLLER_H
