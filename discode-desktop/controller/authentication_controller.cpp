#include "authentication_controller.h"

#include <string>

#include <QDebug>

void authentication_controller::onAuthenticationRequested(QString const &username, QString const &password) {
    qDebug() << "Authenticating " << username << ' ' << password;
}
