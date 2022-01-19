#include "authentication_controller.h"

#include <string>

#include <QDebug>

authentication_controller::authentication_controller(authentication_service i_as, QObject *parent)
    : QObject(parent), as(i_as) { /* Do nothing */ }

void authentication_controller::onAuthenticationRequested(QString const &username, QString const &password) {
    auto on_success = [this](std::string jwt) {
        try {
            std::cout << jwt << '\n'; // TODO Create session
            emit authenticated();
        }
        catch (std::runtime_error &exception) {
            emit authenticationError();
        }
    };
    auto on_failure = [this]() {
        emit authenticationError();
    };
    as.authenticate(username.toStdString(), password.toStdString(), std::move(on_success), std::move(on_failure));
}

void authentication_controller::onLogoutRequested() {
    // TODO Drop session
    qDebug() << "Logging out";
}
