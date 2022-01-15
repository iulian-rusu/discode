#include "authentication_controller.h"

#include <string>

#include <QDebug>

void authentication_controller::onAuthenticationRequested(QString username, QString password) {
    qDebug() << "Authenticating " << username << ' ' << password;
}
