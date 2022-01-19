#include "authentication_controller.h"

#include <string>

#include <QDebug>

authentication_controller::authentication_controller(std::shared_ptr<authentication::authentication_service> i_as, std::shared_ptr<session_service> i_ss, QObject *parent)
    : QObject(parent), as(std::move(i_as)), ss(std::move(i_ss)) { /* Do nothing */ }

void authentication_controller::onAuthenticationRequested(QString const &username, QString const &password) {
    auto on_success = [this](std::string jwt) {
        ss->create_session(std::move(jwt));
        emit authenticated();
    };
    auto on_failure = [this]() {
        emit authenticationError();
    };
    as->authenticate(username.toStdString(), password.toStdString(), std::move(on_success), std::move(on_failure));
}

void authentication_controller::onLogoutRequested() {
    ss->drop_session();
    qDebug() << "Logging out";
}
